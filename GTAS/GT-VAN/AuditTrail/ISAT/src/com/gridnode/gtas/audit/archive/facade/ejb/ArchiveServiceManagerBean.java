/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveServiceManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 4, 2007    Tam Wei Xiang       Created
 * Jan 16,2008    Tam Wei Xiang       To support the archive process being running
 *                                    on single node mode.
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.ejb.SessionContext;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;

import com.gridnode.gtas.audit.archive.ArchiveCriteria;
import com.gridnode.gtas.audit.archive.ArchiveHandler;
import com.gridnode.gtas.audit.archive.ArchiveInitiatorFactory;
import com.gridnode.gtas.audit.archive.ArchiveSummary;
import com.gridnode.gtas.audit.archive.IArchivalInitiator;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.cluster.ArchivalController;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.helper.ArchiveActivityHelper;
import com.gridnode.gtas.audit.common.IGTArchiveConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class delegate the archive request from the client side to the backend side.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.1.2)
 */
public class ArchiveServiceManagerBean implements SessionBean
{
  private static final String CLASS_NAME = ArchiveServiceManagerBean.class.getSimpleName();
  
  private SessionContext _ctxt;
  private Logger _logger;
  
  public void initArchive(Hashtable criteria) throws ArchiveTrailDataException
  {
    String method = "initArchive";
    try
    {
      _logger.logEntry(method, new Object[]{criteria});
      IArchivalInitiator initiator = ArchiveInitiatorFactory.getInstance().getArchivalInitiator();
      initiator.initArchive(criteria);
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Error in invoking the ArciveInitiator for processing the archive request. ", ex);
    }
  }
  
  public String archive(Hashtable criteria, boolean isEnabledMultiNode) throws ArchiveTrailDataException
  {
    String mn = "archive";
    
    _logger.logMessage(mn, null, "Receive archive request from client: "+criteria);
    String archiveID = ArchivalController.getInstance().archive(criteria, isEnabledMultiNode);
    _logger.logMessage(mn, null, "Distribute archive request complete.");
    return archiveID;
  }
  
  public boolean checkArchiveStatus(String archiveID) throws ArchiveTrailDataException
  {
    String mn = "checkArchiveStatus";
    boolean isAllJobCompleted = true;
    boolean isArchiveSuccess = true;
    
    _logger.debugMessage(mn, null, "Checking archive task ID: "+archiveID);
    
    ArchiveActivityHelper.obtainArchiveSummaryLock();
    Properties archiveProperties = ArchiveActivityHelper.getArchiveJobProperties(archiveID);
    Enumeration archiveJobIDs = archiveProperties.propertyNames();
    
    if(! archiveJobIDs.hasMoreElements())
    {
      _logger.logMessage(mn, null, "Archive Properties is 0 given archiveID :"+ archiveID);
      return false;
    }
    
    while(archiveJobIDs.hasMoreElements())
    {
        String jobID = (String)archiveJobIDs.nextElement();
        String jobStatus = archiveProperties.getProperty(jobID);
        
        _logger.logMessage("", null, "Job status for jobID: "+jobID+" is "+jobStatus);
        if(IArchiveConstant.ARCHIVE_STATUS_FAILED.equals(jobStatus))
        {
          isArchiveSuccess = false;
        }
        else if(Boolean.FALSE.toString().equals(jobStatus))
        {
          isAllJobCompleted = false;
        }
     }
      
    if(isAllJobCompleted)
    {
        triggerArchiveAlert(isArchiveSuccess, archiveID);
    }
      
    return isAllJobCompleted;
  }
  
  public void deleteTempArchiveProps(String archiveID) throws Exception
  {
    String mn = "deleteTempArchiveProps";
    
    String archiveFolder = ArchiveActivityHelper.getArchiveMgr().getArchiveFolderPath();
    File propertiesFile = new File(archiveFolder+archiveID+".properties");
    
    if(! propertiesFile.exists() || propertiesFile.isDirectory())
    {
      _logger.logWarn(mn, null, "Archive Properties file is not existed or is a directory rather than a file: "+propertiesFile.getAbsolutePath(), null);
      return;
    }
    
    Properties archiveProps = ArchiveActivityHelper.getPropertiesFile(archiveFolder, archiveID); 
    if(archiveProps != null && archiveProps.size() > 0)
    {
      Enumeration jobIDs = archiveProps.keys();
      while(jobIDs.hasMoreElements())
      {
        File jobIDProps = new File(archiveFolder+"/"+(String)jobIDs.nextElement()+".properties");
        deleteFile(jobIDProps);
      }
    }
    else
    {
      _logger.logWarn(mn, null, "Can't find any related job ID given archiveID: "+ archiveID, null);
    }
    
    File archivePropsFile = new File(archiveFolder+"/"+archiveID+".properties");
    deleteFile(archivePropsFile);
  }
  
  private void triggerArchiveAlert(boolean isArchiveSuccess, String archiveID)
  {
    String mn = "triggerArchiveAlert";
    try
    {
      String archiveFolder = ArchiveActivityHelper.getArchiveMgr().getArchiveFolderPath();
      File archiveSummary = new File(archiveFolder+archiveID+".xml");
      ArchiveSummary summ = ArchiveActivityHelper.getArchiveSummary(archiveSummary);
      ArchiveCriteria criteria = summ.getArchiveCriteria();
    
      Date fromStartDate = criteria.getFromStartDate();
      Date toStartDate = criteria.getToStartDate();
    
      if(fromStartDate == null || toStartDate == null)
      {
        throw new NullPointerException("The fromStartDate or toStartDate is null");
      }
      
      boolean isArchivedOrphan = criteria.isArchiveOrphanRecord();
      String groups = criteria.getGroup();
      if(groups == null)
      {
        groups = "";
      }
      
      Hashtable<String, Object> archiveCriteria = new Hashtable<String, Object>();
      archiveCriteria.put(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME, fromStartDate.getTime()); 
      archiveCriteria.put(IArchiveConstant.CRITERIA_TO_START_DATE_TIME, toStartDate.getTime());
      archiveCriteria.put(IArchiveConstant.GROUP_INFO, groups);
      archiveCriteria.put(IArchiveConstant.ARCHIVE_ORPHAN_RECORD, isArchivedOrphan);
    
      ArchivalController controller = ArchivalController.getInstance();
      String archiveType = IArchiveConstant.ISAT_GT_TM_ARCHIVE_COMPLETED;
      if(! isArchiveSuccess)
      {
        archiveType = IArchiveConstant.ISAT_GT_TM_ARCHIVE_FAILED;
      }
      controller.sendArchiveStatusAlert(archiveCriteria, archiveID, isArchiveSuccess, null, archiveType);
    }
    catch(Exception ex)
    {
      _logger.logWarn(mn ,null, "Can't send out archive alert for archiveID: "+archiveID+" isArchivedSuccess: "+isArchiveSuccess, ex);
    }
  }

  
  public void updateArchiveStatus(String archiveID, String jobID, String archiveType, boolean status, String summaryFilename) throws ArchiveTrailDataException
  {
    try
    {
      if(! (IGTArchiveConstant.ARCHIVE_TYPE_DOCUMENT.equals(archiveType) || IGTArchiveConstant.ARCHIVE_TYPE_PI.equals(archiveType)) )
      {
        throw new IllegalArgumentException("Archive type: "+ archiveType+" currently not supported");
      }
      
      if(archiveID == null || "".equals(archiveID))
      {
         throw new IllegalArgumentException("ArchiveID can't be null or empty");
      }
      
      if(jobID == null || "".equals(jobID))
      {
         throw new IllegalArgumentException("jobID can't be null or empty");
      }
      
      String archiveFolder = ArchiveActivityHelper.getArchiveMgr().getArchiveFolderPath();
      File summaryFile = new File(archiveFolder+"/"+archiveID+".xml");
      ArchiveSummary summary = ArchiveActivityHelper.getArchiveSummary(summaryFile);
      ArchiveCriteria criteria = summary.getArchiveCriteria();
      boolean isArchiveOrphan = criteria.isArchiveOrphanRecord();
    
      ArchiveHandler archiveHandler = new ArchiveHandler();
      ArchiveActivityHelper.obtainArchiveSummaryLock(); //to ensure not concurrently modified the same file
    
      //update archive summary
      archiveHandler.updateGTArchiveStatus(archiveType, status, summaryFilename, archiveID);
    
      //update job properties
      archiveHandler.updateJobStatus(archiveType, jobID, status, archiveID, isArchiveOrphan);
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Error in updating the archive status given archiveID: "+archiveID+" jobID: "+jobID+" archiveType:"+archiveType+" isArchiveSuccess: "+status+" summaryFilename: "+summaryFilename, ex);
    }
  }
  
  private void deleteFile(File f)
  {
    String mn = "deleteFile";
    
    if(f.exists() && f.isFile())
    {
      boolean isDeleted = f.delete();
      if(! isDeleted)
      {
        _logger.logWarn(mn, null, "Can't delete the archive/job properties file: "+ f.getAbsolutePath(), null);
      }
    }
    else
    {
      _logger.logWarn(mn, null, "Archive Properties file "+f.getAbsolutePath()+" is not exist or is a directory instead of a file.", null);
    }
  }
  
  public void ejbCreate() throws CreateException
  {
    _logger = getLogger();
  }
  
  public void ejbActivate() throws EJBException, RemoteException
  {
  
  }

  public void ejbPassivate() throws EJBException, RemoteException
  {

  }

  public void ejbRemove() throws EJBException, RemoteException
  {

  }

  public void setSessionContext(SessionContext ctxt) throws EJBException,
                                                    RemoteException
  {
    _ctxt = ctxt;
  }
  
  public Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
