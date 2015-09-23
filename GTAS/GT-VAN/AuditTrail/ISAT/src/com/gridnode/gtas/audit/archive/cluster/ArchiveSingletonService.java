package com.gridnode.gtas.audit.archive.cluster;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import com.gridnode.gtas.audit.archive.cluster.helper.JbossPartitionHelper;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveServiceManagerLocalHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveServiceManagerLocalObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class serves as the Singleton for the cluster to enforce at any one time, there is only
 * one archive task being running in GT. This help us to off the load of the server.
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class ArchiveSingletonService implements ArchiveSingletonServiceMBean 
{
  private static String CLASS_NAME = "ArchiveSingletonService";
  private static String ARCHIVE_TASK_CATEGORY = "ArchivePendingTask";
  private Logger _logger = getLogger();
  
	private boolean isMasterNode;
	private String nodeID;
  
  private Date _lastActivatedTime;
  private boolean _isArchivedProcessing;
  private boolean _isEnabledMultiNodes = false;
  private String _processingArchiveID;
  
	public void startArchivalSingleton() {
		isMasterNode = true;
    nodeID = SystemUtil.getHostId();
	}

	public void stopArchivalSingleton() {
		// TODO Auto-generated method stub
		isMasterNode = false;
	}
	
	public boolean isMasterNode()
	{
		return isMasterNode;
	}
  
  public String getNodeID()
  {
    return nodeID;
  }

  public synchronized void archive(Hashtable archiveCriteria) throws Exception
  {
    String mn = "archive";
    
    setLastActivateTime(new Date());
    if(isArchiveProcessing())
    {
      long timeInMilli = new Date().getTime();
      JbossPartitionHelper helper = JbossPartitionHelper.getInstance();
      helper.setAttr(ARCHIVE_TASK_CATEGORY, timeInMilli, archiveCriteria);
      
      _logger.logMessage(mn, null, "On going archive processing, waiting for it to finish: Adding into queue: key: "+timeInMilli+ " with archive criteria: "+archiveCriteria);
      return;
    }
      
    try
    {
      _logger.logMessage(mn, null, "Archive service mbean: archive record: Current node ID: "+SystemUtil.getHostId());
      setArchiveProcessing(true);
      _processingArchiveID = getArchiveServiceManager().archive(archiveCriteria, isEnabledMultiNodes());
      _logger.logMessage(mn, null, "Archive Service: archiveID in processing :"+_processingArchiveID);
    }
    catch(Exception ex)
    {
      try
      {
        getArchiveServiceManager().deleteTempArchiveProps(_processingArchiveID);
      }
      catch(Exception e)
      {
        _logger.logWarn(mn, null, "Error in deleting the temporary archive properties for archiveID: "+_processingArchiveID, e);
      }
      
      _processingArchiveID = null;
      setArchiveProcessing(false);
      throw new ArchiveTrailDataException("Error in delegating the archive request to backend "+ex.getMessage(), ex);
    }
  }
  
  private IArchiveServiceManagerLocalObj getArchiveServiceManager() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IArchiveServiceManagerLocalHome home = (IArchiveServiceManagerLocalHome)finder.lookup(
                                                IArchiveServiceManagerLocalHome.class.getName(), IArchiveServiceManagerLocalHome.class);
    return (IArchiveServiceManagerLocalObj)home.create();
  }
  
  public String getProcessingArchiveID() throws Exception
  {
    return _processingArchiveID;
  }

  private synchronized void completeProcessArchival() throws Exception
  {
    String mn = "completeProcessArchival";
    JbossPartitionHelper helper = JbossPartitionHelper.getInstance();
    Collection taskKeys = helper.getAllKeyInDs(ARCHIVE_TASK_CATEGORY);
    
    _processingArchiveID = null;
    if(taskKeys != null && taskKeys.size() > 0)
    {
      Long[] archiveTaskKeys = (Long[])taskKeys.toArray(new Long[taskKeys.size()]);
      
      _logger.logMessage(mn, null, "Prior sorting ...");
      for(Long key : archiveTaskKeys)
      {
        _logger.logMessage(mn, null, "Archive pending task ID :"+key);
      }
      
      Arrays.sort(archiveTaskKeys);
      
      _logger.logMessage(mn, null, "After sorting ...");
      for(Long key : archiveTaskKeys)
      {
        _logger.logMessage(mn, null, "Archive pending task ID :"+key);
      }
      
      Long taskKey = archiveTaskKeys[0];
      Hashtable criteria = (Hashtable)helper.getAttr(ARCHIVE_TASK_CATEGORY, taskKey);
      helper.remoteAttr(ARCHIVE_TASK_CATEGORY, taskKey);
      archive(criteria);
    }
   
  }
  
  public synchronized void  checkArchiveStatus() throws Exception
  {
    String mn = "checkArchiveStatus";
    setLastActivateTime(new Date());
    
    _logger.logMessage(mn ,null, "Checking the archive status");
    
    if(!isArchiveProcessing()) return;
    
    if(_processingArchiveID != null)
    {
      boolean isAllCompleted = getArchiveServiceManager().checkArchiveStatus(_processingArchiveID);
      if(isAllCompleted)
      {
        setArchiveProcessing(false);	
        getArchiveServiceManager().deleteTempArchiveProps(_processingArchiveID);
        completeProcessArchival();
      }
      _logger.logMessage(mn, null, "archival: all task status :"+isAllCompleted);
    }
    else
    {
      _logger.logMessage(mn, null, "Processing Archive ID is set to null");
    }
  }
  
  public void updateArchiveStatus(String archiveID, String jobID, String archiveType, boolean isArchiveSuccess, String summaryFilename) throws Exception
  {
	  getArchiveServiceManager().updateArchiveStatus(archiveID, jobID, archiveType, isArchiveSuccess, summaryFilename);
  }

  public boolean isArchiveProcessing()
  {
    return _isArchivedProcessing;
  }
  
  private void setArchiveProcessing(boolean isProcessing)
  {
    _isArchivedProcessing = isProcessing;
  }
  
  public Date getLastActivateTime()
  {
    return _lastActivatedTime;
  }
  
  private void setLastActivateTime(Date d)
  {
    _lastActivatedTime = d;
  }
  
  public void setEnabledMultiNodes(boolean isEnabled)
  {
    _isEnabledMultiNodes = isEnabled;
  }

  public boolean isEnabledMultiNodes()
  {
    return _isEnabledMultiNodes;
  }
  
  public void dropCurrentArchiveProcess()
  {
	_isArchivedProcessing = false;
	_processingArchiveID = null;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
