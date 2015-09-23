/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RestoreHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 16, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;

import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalObj;
import com.gridnode.gtas.audit.archive.helper.ArchiveActivityHelper;
import com.gridnode.gtas.audit.archive.helper.ArchiveZipFile;
import com.gridnode.gtas.audit.common.IGTArchiveConstant;
import com.gridnode.gtas.audit.model.BizDocument;
import com.gridnode.gtas.audit.model.DocumentTransaction;
import com.gridnode.gtas.audit.model.IAuditTrailEntity;
import com.gridnode.gtas.audit.model.ProcessTransaction;
import com.gridnode.gtas.audit.model.TraceEventInfo;
import com.gridnode.gtas.audit.model.TraceEventInfoHeader;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.gtas.audit.util.XMLBeanMarshal;
import com.gridnode.util.alert.AlertUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * TODO: what other thing can cause restore fail ? how we ensure the consistency of the data?
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class RestoreHandler
{ 
  private static final String CLASS_NAME = "RestoreHandler";
  private Logger _logger;
  
  public RestoreHandler()
  {
    _logger = getLogger();
  }
  
  public void restoreTMGTArchive(String summaryFilename) throws Exception
  {
    String methodName = "restoreTMGTArchive";
    
    boolean isRestoreSuccess = true;
    Exception restoreEx = null;
    
    _logger.logEntry(methodName, null);
    try
    {
      delegateRestoreRequestToGT(summaryFilename, true);
      restoreArchiveFiles(summaryFilename);
    }
    catch(Exception ex)
    {
      isRestoreSuccess = false;
      restoreEx = ex;
      throw ex;
    }
    finally
    {
      triggerRestoreAlert(isRestoreSuccess, restoreEx,summaryFilename);
      _logger.logEntry(methodName, null);
    }
  }
  
  private void restoreArchiveFiles(String summaryFilename) throws Exception
  {
    String methodName = "restoreArchiveFiles";
    boolean isTMRestoreSuccess = true;
    Exception restoreEx = null;
    
    _logger.logMessage(methodName, null, "Restoring summary file "+summaryFilename);
    
    try
    {
      IAuditTrailArchiveManagerLocalObj mgr = ArchiveActivityHelper.getArchiveMgr();
      String archiveFolderDir = mgr.getArchiveFolderPath();
      ArchiveSummary summary = ArchiveActivityHelper.getArchiveSummary(archiveFolderDir+summaryFilename);
    
    
      String groupDir = summary.getGroup()+"/";
      Collection<String> zipFiles = ArchiveActivityHelper.convertStrToList(summary.getZipFileList(),",");
    
      if(zipFiles != null && zipFiles.size() > 0)
      {
        Iterator<String> ite = zipFiles.iterator();
        while(ite.hasNext())
        {
          String archiveFilename = ite.next();
          File archiveZipFile = new File(archiveFolderDir+groupDir+archiveFilename);
        
          _logger.logMessage(methodName, null, "Restoring archive zip file "+archiveZipFile.getAbsolutePath());
          restoreArchiveFile(archiveZipFile);
        }
      }
    }
    catch(Exception ex)
    {
      isTMRestoreSuccess = false;
      restoreEx = ex;
      throw ex;
    }
    finally
    {
      ArchiveActivityHelper.getArchiveMgr().updateTMRestoreStatus(isTMRestoreSuccess, summaryFilename);
    }
  }
  
  private void triggerRestoreAlert(boolean isRestoreSuccess, Exception ex, String summaryFilename)
  {
    ArrayList substitue = new ArrayList();
    String archiveType = isRestoreSuccess ? IArchiveConstant.ISAT_RESTORE_COMPLETED : IArchiveConstant.ISAT_RESTORE_FAILED;
    
    substitue.add(ArchiveActivityHelper.formatDateToString(new Date(), ArchiveActivityHelper.getDatePattern())); //finished at what time
    substitue.add(summaryFilename); //summary filename
    
    if(! isRestoreSuccess)
    {
      substitue.add(ex.getMessage()); //exception msg
      String stackTrace = ArchiveActivityHelper.getStackTrace(ex); //stack trace
      substitue.add(stackTrace);
    }
    
    AlertUtil.getInstance().sendAlert(archiveType, substitue.toArray());
  }
  
  private void restoreArchiveFile(File archiveFile) throws Exception
  {
    String methodName = "restoreArchiveFile";
    
    ArchiveZipFile zipFile = new ArchiveZipFile(archiveFile.getAbsolutePath());
    zipFile.open(ArchiveZipFile.OPEN_MODE_READ);
    Enumeration zipEntriesEnum = zipFile.getZipEntries();
    IAuditTrailArchiveManagerLocalObj mgr = ArchiveActivityHelper.getArchiveMgr();
    
    //restore back the BizDocument first
    while(zipEntriesEnum.hasMoreElements())
    {
      ZipEntry entry = (ZipEntry)zipEntriesEnum.nextElement();
      String zipEntryName = entry.getName();
      if(! entry.isDirectory() && zipEntryName.indexOf(IArchiveConstant.CATEGORY_BIZ_DOCUMENT) >= 0)
      {
        byte[] content = zipFile.getZipEntryContentInByte(entry);
        String contentInXml = new String(content);
        
        BizDocument bizDoc = (BizDocument)XMLBeanMarshal.xmlToObj(contentInXml, BizDocument.class);
        if(bizDoc == null)
        {
          throw new ArchiveTrailDataException("[RestoreHandler.restoreArchiveFile] Error in deserialize from XML to bean. The xml content is "+contentInXml+". Zip entry name is "+entry);
        }
        //
        mgr.restoreAuditTrailEntity(bizDoc);
      }
    }
    
    //reload the zipEntries to restore other entities. For trace event header, the stored procedure will handle
    zipEntriesEnum = zipFile.getZipEntries();
    while(zipEntriesEnum.hasMoreElements())
    {
      ZipEntry entry = (ZipEntry)zipEntriesEnum.nextElement();
      String zipEntryName = entry.getName();
      Class entityClass = null;
      if(entry.isDirectory())
      {
        continue;
      }
      else if(zipEntryName.indexOf(IArchiveConstant.CATEGORY_DOC_TRANS) >= 0)
      {
        entityClass = DocumentTransaction.class;
      }
      else if(zipEntryName.indexOf(IArchiveConstant.CATEGORY_PROCESS_TRANS) >= 0)
      {
        entityClass = ProcessTransaction.class;
      }
      else if(zipEntryName.indexOf(IArchiveConstant.CATEGORY_TRACE_EVENT_HEADER) >= 0)
      {
        continue;
      }
      else if(zipEntryName.indexOf(IArchiveConstant.CATEGORY_TRACE_EVENT_INFO) >= 0)
      {
        entityClass = TraceEventInfo.class;
      }
      else
      {
        continue;
      }
      byte[] content = zipFile.getZipEntryContentInByte(entry);
      String contentInXml = new String(content);
      
      IAuditTrailEntity trailEntity = (IAuditTrailEntity)XMLBeanMarshal.xmlToObj(contentInXml, entityClass);
      
      if(trailEntity == null)
      {
        throw new ArchiveTrailDataException("[RestoreHandler.restoreArchiveFile] Error in deserialize from XML to bean. The xml content is "+contentInXml+". Zip entry name is "+entry);
      }
      mgr.restoreAuditTrailEntity(trailEntity);
    }
  }
  
  //TODO summary filename list
  private void delegateRestoreRequestToGT(String summaryFilename, boolean isRestorePI) throws Exception
  {
    String methodName = "delegateRestoreRequestToGT";
    IAuditTrailArchiveManagerLocalObj mgr = ArchiveActivityHelper.getArchiveMgr();
    String archiveFolderDir = mgr.getArchiveFolderPath();
    ArchiveSummary summary = ArchiveActivityHelper.getArchiveSummary(archiveFolderDir+summaryFilename);
    
    if(isRestorePI)
    {
      String gtPiSummaryFile = summary.getGtPISummaryFilename();
      if(gtPiSummaryFile != null && ! "".equals(gtPiSummaryFile))
      {
        _logger.logMessage(methodName, null, "Delegating restore request to GT ..... GT PI Summary filename is "+gtPiSummaryFile);
      
        Hashtable restoreCriteria = createMsgToSend(gtPiSummaryFile, summary.getArchiveID(), isRestorePI);
        mgr.delegateArchiveRequest(restoreCriteria, IISATProperty.GT_ARCHIVE_JMS_CATEGORY);
      }
    }
    else
    {
      String gtDocSummaryFile = summary.getGtDocSummaryFilename();
      if(gtDocSummaryFile != null && ! "".equals(gtDocSummaryFile))
      {
        _logger.logMessage(methodName, null, "Delegating restore request to GT ..... GT Doc Summary filename is "+gtDocSummaryFile);
        Hashtable restoreCriteria = createMsgToSend(gtDocSummaryFile, summary.getArchiveID(), isRestorePI);
        mgr.delegateArchiveRequest(restoreCriteria, IISATProperty.GT_ARCHIVE_JMS_CATEGORY);
      }
    }
  }
  
  /**
   * Handle the restore status from GT. If the restore status for PI is success, we will
   * restore the Doc archive.
   * @param restoreStatus
   */
  public void handleRestoreStatus(Hashtable restoreStatus) throws Exception
  {
    String methodName = "handleRestoreStatus";
    _logger.logMessage(methodName, null, "Receving GT restore status");
    
    String archiveType = (String)restoreStatus.get(IGTArchiveConstant.ARCHIVE_TYPE);
    Boolean isRestoreSuccess = (Boolean)restoreStatus.get(IGTArchiveConstant.ARCHIVE_STATUS);
    String archiveID = (String)restoreStatus.get(IGTArchiveConstant.ARCHIVE_ID);
    
    _logger.logMessage(methodName, null, "archiveType: "+archiveType+" isRestoreSuccess: "+isRestoreSuccess+" archiveID: "+archiveID);
    
    ArchiveActivityHelper.getArchiveMgr().updateGTRestoreStatus(archiveType, isRestoreSuccess, archiveID);
    if(IGTArchiveConstant.ARCHIVE_TYPE_PI.equals(archiveType) && isRestoreSuccess)
    {
      String summaryFilename = archiveID+".xml";
      delegateRestoreRequestToGT(summaryFilename, false);
    }
  }
  
  private Hashtable createMsgToSend(String summaryFilename, String archiveID, boolean isRestorePI)
  {
    //  create map msg
    Hashtable archiveCriteria = new Hashtable();
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_OPERATION, IGTArchiveConstant.ARCHIVE_OP_RESTORE);
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_SUMMARY_FILE, summaryFilename);
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_ID, archiveID);
    
    String archiveType = "";
    if(isRestorePI)
    {
      archiveType = IGTArchiveConstant.ARCHIVE_TYPE_PI;
    }
    else
    {
      archiveType = IGTArchiveConstant.ARCHIVE_TYPE_DOCUMENT;
    }
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_TYPE, archiveType);
    return archiveCriteria;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
