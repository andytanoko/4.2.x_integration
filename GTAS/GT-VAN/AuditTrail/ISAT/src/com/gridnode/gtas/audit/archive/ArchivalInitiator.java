/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchivalInitiator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 9, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;


import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveServiceManagerHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveServiceManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class handle the init of the Archive process. It also provide the method for checking is the current archive task
 * is completed. If so, the queuing up archive task will be dequeuce and be further process if such queing task exist.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class ArchivalInitiator implements IArchivalInitiator
{
  private static final String CLASS_NAME = "ArchivalInitiator";
  private static ArchivalInitiator _initiator = new ArchivalInitiator();
  private Logger _logger = null;
  private Hashtable<Long, Hashtable> _archiveTasks = new Hashtable<Long, Hashtable>();
  private static boolean _isArchivedProcessing = false;
  private static String _processingArchiveID = null;
  
  //private Object initArchiveLock = new Object();
  //private Object checkArchiveLock = new Object();
  
  private ArchivalInitiator()
  {    
    _logger = getLogger();
  }
  
  public static ArchivalInitiator getInstance()
  {
    return _initiator;
  }
  
  public synchronized void initArchive(Hashtable criteria) throws Exception
  { 
    String method = "initArchive";
    _logger.debugMessage(method, new Object[]{criteria}, "Pass in archive criterias:");
    
    if(isArchivedProcessing())
    {
      long taskKey = new Date().getTime();
      _archiveTasks.put(taskKey, criteria);
      _logger.debugMessage(method, null, "Archiving process is on going, queue up for later process. Criteria is: "+criteria+" taskKey: "+taskKey);
      return;
    }
    else
    {
      try
      {
        setArchivedProcessing(true);
        _processingArchiveID = getArchiveServiceManager().archive(criteria, false);
      }
      catch(Exception ex)
      {
        cleanArchiveTempFile(_processingArchiveID);
        setArchivedProcessing(false);
        setProcessingArchiveID(null);
        throw new ArchiveTrailDataException("Error in delegating the archive request to backend. "+ex.getMessage(), ex);
      }
    }
  }
  
  public synchronized  void checkArchiveStatus() throws Exception
  {
    String mn = "checkArchiveStatus";
    _logger.debugMessage(mn, null, "Checking archive status. Is archived processing: "+isArchivedProcessing() +" processing archiveID: "+_processingArchiveID);
    
    if(! isArchivedProcessing())
    {
      return;
    }
    else
    {
      if(_processingArchiveID != null)
      {
        boolean isAllCompleted = getArchiveServiceManager().checkArchiveStatus(_processingArchiveID);
        if(isAllCompleted)
        {
          _logger.debugMessage(mn, null, "Archive task id: "+_processingArchiveID+" finished job");
        
          setArchivedProcessing(false);
          cleanArchiveTempFile(_processingArchiveID);
          _processingArchiveID = null;
          continueOustandingArchiveTask();
        }
        else
        {
          _logger.debugMessage(mn, null, "Archive task with ID: "+_processingArchiveID+" still processing.");
        }
      }
    }
  }
  
  private void continueOustandingArchiveTask() throws Exception
  {
    String method = "continueOustandingArchiveTask";
    
    Collection taskKeys = convertToCollection(_archiveTasks.keys());
    if(taskKeys.size() > 0)
    {
      Long[] archiveTaskKeys = (Long[])taskKeys.toArray(new Long[taskKeys.size()]);
      Arrays.sort(archiveTaskKeys);
      Long targetRunTaskKey = archiveTaskKeys[0];
      _logger.debugMessage(method, null, "Next run task id: "+targetRunTaskKey);
      
      Hashtable archiveCriteria = _archiveTasks.get(targetRunTaskKey);
      _archiveTasks.remove(targetRunTaskKey);
      
      initArchive(archiveCriteria);
    }
  }
  
  private Collection convertToCollection(Enumeration<Long> taskKeys)
  {
    ArrayList<Long> keys = new ArrayList<Long>(); 
    for(; taskKeys != null && taskKeys.hasMoreElements(); )
    {
      Long taskKey = taskKeys.nextElement();
      keys.add(taskKey);
    }
    return keys;
  }
  
  private IArchiveServiceManagerObj getArchiveServiceManager() throws Exception
  {
    JndiFinder finder;
    try
    {
      finder = new JndiFinder(null);
    }
    catch(Exception ex)
    {
      throw new Exception("Failed to initialize jndi finder.", ex);
    }
    IArchiveServiceManagerHome home = (IArchiveServiceManagerHome)finder.lookup(
                                                IArchiveServiceManagerHome.class.getName(), IArchiveServiceManagerHome.class);
    
    try
    {
      return home.create();
    }
    catch(Exception ex)
    {
      throw new Exception("Failed to create ArchiveServiceManager", ex);
    }
  }
  
  private void cleanArchiveTempFile(String archiveID)
  {
    String mn = "cleanArchiveTempFile";
    if(archiveID != null)
    {
      try
      {
        getArchiveServiceManager().deleteTempArchiveProps(archiveID);
      }
      catch(Exception ex)
      {
          _logger.logWarn(mn, null, "Error in deleting the temporary archive properties for archiveID: "+_processingArchiveID, ex);
      }
    }
  }
  
  public boolean isArchivedProcessing()
  {
    return _isArchivedProcessing;
  }

  public void setArchivedProcessing(boolean archivedProcessing)
  {
    _isArchivedProcessing = archivedProcessing;
  }

  public String getProcessingArchiveID()
  {
    return _processingArchiveID;
  }

  public void setProcessingArchiveID(String archiveID)
  {
    _processingArchiveID = archiveID;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
}
