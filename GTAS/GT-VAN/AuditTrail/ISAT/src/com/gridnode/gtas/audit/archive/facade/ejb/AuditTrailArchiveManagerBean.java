/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailArchiveManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 16, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import com.gridnode.gtas.audit.archive.ArchiveCriteria;
import com.gridnode.gtas.audit.archive.ArchiveSummary;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.helper.ArchiveActivityHelper;
import com.gridnode.gtas.audit.archive.helper.ArchiveHelper;
import com.gridnode.gtas.audit.common.IGTArchiveConstant;
import com.gridnode.gtas.audit.dao.*;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.model.*;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.db.DAO;
import com.gridnode.util.io.IOUtil;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import javax.ejb.*;
import javax.naming.NamingException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class AuditTrailArchiveManagerBean
  implements SessionBean
{
  private SessionContext _ctxt;
  private Logger _logger;
  private static final String CLASS_NAME = "AuditTrailArchiveManager";
  private static final int SUB_SELECT_LIMIT = 1000;
  
  public Collection retrieveProcessTransactionUID(Hashtable criteria)
  {
    Long fromStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
    Long toStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
    String groupListInStr = (String)criteria.get(IArchiveConstant.GROUP_INFO);
    Collection<String> groupList = convertStrToCollection(groupListInStr);
    
    if(fromStartDateTime == null)
        throw new NullPointerException("[AuditTrailArchiveManager.retrieveProcessTransactionUID] fromStartDateTime can't be Null ...");
    if(toStartDateTime == null)
    {
        throw new NullPointerException("[AuditTrailArchiveManager.retrieveProcessTransactionUID] toStartDateTime can't be Null ...");
    } 
    else
    {
        ProcessTransactionDAO dao = new ProcessTransactionDAO();
        return dao.retrieveProInstanceUID(new Date(fromStartDateTime.longValue()), new Date(toStartDateTime.longValue()), groupList);
    }
  }
  
  /**
   * Return those tracing IDs that its correspond TraceEventInfo is tagged with group info.
   * @param criteria 
   * @return
   */
  public Collection<String> retrieveTraceEventInfoTracingIDWithGroup(Hashtable criteria)
  {
    String mn = "retrieveTraceEventInfoTracingIDWithGroup";
    
    Long fromStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
    Long toStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
    String groupListInStr = (String)criteria.get(IArchiveConstant.GROUP_INFO);
    Collection<String> groupList = convertStrToCollection(groupListInStr);
    
    if(fromStartDateTime == null)
        throw new NullPointerException("[AuditTrailArchiveManager.archiveOrphanTraceEventInfo] fromStartDateTime can't be Null ...");
    if(toStartDateTime == null)
    {
        throw new NullPointerException("[AuditTrailArchiveManager.archiveOrphanTraceEventInfo] toStartDateTime can't be Null ...");
    } 
    else
    {
      ArrayList<String> tracingIDsWithGroup = new ArrayList<String>();
      TraceEventInfoDAO dao = new TraceEventInfoDAO();
      
      List<String> tracingIDs = dao.getTraceEventInfoTracingIDByEventOccuredTime(new Date(fromStartDateTime), new Date(toStartDateTime));
      
      return filterTracingIDs(tracingIDs, true, groupList);
    }
  }
  
  private Collection<String> filterTracingIDs(List<String> tracingIDs, boolean isIncludedGroupInfo, Collection<String> groupList)
  {
    ArrayList<String> filterTracingIDs = new ArrayList<String>();
    if(tracingIDs != null && (tracingIDs.size() > 0))
    {
      int tracingIDsize = tracingIDs.size();
      int limit = SUB_SELECT_LIMIT;
      int beginIndex = 0;
      int endIndex = SUB_SELECT_LIMIT; //oracle limit the sql in select to 1000
      long nRound = Math.round( Math.ceil(tracingIDsize / new Double(SUB_SELECT_LIMIT)));
      int atRound = 1;
      TraceEventInfoDAO dao = new TraceEventInfoDAO();
      
      while(atRound <= nRound)
      {
        int sIndex = 0;
        int eIndex = 0;
        if(beginIndex < tracingIDs.size() && endIndex >= tracingIDs.size())
        {
          sIndex = beginIndex;
          eIndex = tracingIDs.size();
        }
        else if(beginIndex < tracingIDs.size() && endIndex < tracingIDs.size())
        {
          sIndex = beginIndex;
          eIndex = endIndex;
        }
        System.out.println("sIndex is "+sIndex+" eIndex is "+eIndex);
        
        beginIndex += limit ;
        endIndex+= limit;
        
        List<String> subsetTracingIDs = copyTracingIDs(tracingIDs, sIndex, eIndex);
        List<String> subFilteringTraceID = null;
        if(isIncludedGroupInfo)
        {
          subFilteringTraceID = dao.getTracingIDWithGroup(subsetTracingIDs, groupList);
        }
        else
        {
          subFilteringTraceID = dao.getTracingIDWithoutGroup(subsetTracingIDs);
        }
        
        if(subFilteringTraceID != null)
        {
          filterTracingIDs.addAll(subFilteringTraceID);
        }
        
        atRound++;
      }
    }
    return filterTracingIDs;
  }
  
  private List<String> copyTracingIDs(List<String> sourceTracingIDs, int beginIndex, int endIndex)
  {
    ArrayList<String> subsetTraceIDs = new ArrayList<String>();
    for(int i = beginIndex; i < endIndex ; i++ )
    {
      subsetTraceIDs.add(sourceTracingIDs.get(i));
    }
    return subsetTraceIDs;
  }
  
  /**
   * Retrieve the Orphan(not belong to any group) TraceEvent info tracingID list given the criteria.
   * @param criteria
   * @return
   */
  public Collection<String> retrieveOrphanTraceEventInfoTracingID(Hashtable criteria)
  {  
    Long fromStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
    Long toStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
    if(fromStartDateTime == null)
        throw new NullPointerException("[AuditTrailArchiveManager.archiveOrphanTraceEventInfo] fromStartDateTime can't be Null ...");
    if(toStartDateTime == null)
    {
        throw new NullPointerException("[AuditTrailArchiveManager.archiveOrphanTraceEventInfo] toStartDateTime can't be Null ...");
    } 
    else
    {
      TraceEventInfoDAO dao = new TraceEventInfoDAO();
      List<String> tracingIDs = dao.getTraceEventInfoTracingIDByEventOccuredTime(new Date(fromStartDateTime), new Date(toStartDateTime));
      return filterTracingIDs(tracingIDs, false, null);
    }
  }
  
/**
   * Archive the Process Transaction related doc eg the DocumentTransaction, EventInfo, BizDocument
   * @param processUID The process instance UID (use in GT)
   * @param archiveHelper The archive zip helper
   * @throws ArchiveTrailDataException throw while we have error in the archival process
   * @throws RemoteException
   */
  public void archiveProcessTransaction(Long processUID, ArchiveHelper archiveHelper)
    throws ArchiveTrailDataException
  {
    String methodName = "archiveProcessTransaction";
    long timeInMilli = System.currentTimeMillis();
    
    ProcessTransactionDAO processTransDAO = new ProcessTransactionDAO();
    ProcessTransaction processTrans = processTransDAO.retrieveProTransactionByProcessInstanceUID(processUID);
    if(processTrans == null)
        throw new ArchiveTrailDataException("No ProcessTransaction entity found for process instance UID: "+processTrans.getProcessInstanceUID());
    
    _logger.logMessage(methodName, null, "Archive Process Trans is "+processTrans);
    
    //archive the doc trans
    DocTransDAO docTransDAO = new DocTransDAO();
    Collection<DocumentTransaction> docTransList = docTransDAO.getDocTransByProcessInstanceUID(processTrans.getProcessInstanceUID());
    if(docTransList == null || docTransList.size() == 0)
    {
        _logger.logWarn(methodName, null ,"No document transaction can found given process UID "+processUID, null);
    } 
    else
    {
        archiveDocumentTransaction(docTransList, archiveHelper, docTransDAO);  
    }
    
    archiveHelper.addAuditTrailEntity(processTrans, null, archiveHelper.getArchiveZipCategory()+IArchiveConstant.CATEGORY_PROCESS_TRANS);
    deleteAuditTrailEntity(processTransDAO, processTrans);
    
    long totalTimeSpend = System.currentTimeMillis() - timeInMilli;
    _logger.logMessage(methodName, null, "total time spend "+ totalTimeSpend/60000);
  }

  /**
   * Archive the document transaction and its correspond event info, event header, and the business document
   * @param docTransList A collection of DocumentTransaction entity
   * @param archiveHelper The archive zip helper
   * @throws ArchiveTrailDataException
   */
  private void archiveDocumentTransaction(Collection<DocumentTransaction> docTransList, ArchiveHelper archiveHelper,
                                          DocTransDAO docTransDAO)
    throws ArchiveTrailDataException
  {
    String methodName = "archiveDocumentTransaction";
    if(docTransList != null && docTransList.size() > 0)
    {
        _logger.logMessage(methodName, null, "Retrieve doc trans size is "+docTransList.size());
        
        TraceEventInfoDAO eventDAO = new TraceEventInfoDAO();
        Hashtable<String, Boolean> archivedTracingIDs = new Hashtable<String, Boolean>();
        
        Iterator<DocumentTransaction> ite = docTransList.iterator();
        
        while(ite.hasNext())
        {
          DocumentTransaction docTrans = (DocumentTransaction)ite.next();
          String tracingID = docTrans.getTracingID();
          
          //Check whether some Document transaction from diff Process Trans sharing the same tracing ID. It can happen
          //while in the reprocessing of OB doc. 2 action PIP auto confirmation
          boolean isExistDependentDoc = isExistDependentDocumentTrans(docTrans.getTracingID(), docTrans.getProcessInstanceUID(), docTransDAO);
          if(! isExistDependentDoc)
          {
            if(!archivedTracingIDs.containsKey(tracingID)) //to handle the resend case, auto 3A4C that share the same tracingID
            {
              //archive doc correspond event
              _logger.logMessage(methodName, null, "Archiving DocumentTrans with UID "+docTrans.getUID());
                Collection eventInfoList = eventDAO.getTraceEventInfoByTracingID(tracingID);
                archiveTraceEventInfo(eventInfoList, archiveHelper);
                
                //archive event header
                archiveTraceEventInfoHeader(tracingID, eventDAO, archiveHelper);
                
                archivedTracingIDs.put(tracingID, true);
            }
          }
          else
          {
            _logger.logMessage(methodName, null, "DocumentTransaction "+docTrans+ "correspond event history with tracingID "+tracingID+"is refered from " +
                               "other docuemnt transaction. No archive will be performed on the event history list.");
          }
          
//        archive doc trans
          archiveHelper.addAuditTrailEntity(docTrans, null, archiveHelper.getArchiveZipCategory()+IArchiveConstant.CATEGORY_DOC_TRANS);
          deleteAuditTrailEntity(eventDAO, docTrans);
          
          //archvie biz document
          String bizDocumentUID = docTrans.getBizDocumentUID();
          archiveBizDocument(bizDocumentUID, archiveHelper);
        }

    }
  }
  
  /**
   * Check is there any Document transaction that is depent on the same tracingID as well. This will happend while
   * user perform OB reprocessing.
   * @param tracingID
   * @param processInstanceUID
   * @param docTransDAO
   * @return
   */
  private boolean isExistDependentDocumentTrans(String tracingID, Long processInstanceUID, DocTransDAO docTransDAO)
  {
    DocumentTransaction docTrans = docTransDAO.getDocTransByTracingID(tracingID, processInstanceUID);
    _logger.logMessage("isExistDependentDocumentTran", null, "Dependent doc trans is "+docTrans);
    return docTrans != null;
  }
  
  /**
   * While archiving orphan record, the trace event and its business document still may link up to
   * the existing DocumentTransaction.
   * @param tracingID
   * @param docTransDAO
   * @return
   */
  private boolean isExistDependentDocumentTrans(String tracingID, DocTransDAO docTransDAO)
  {
    int docTransCount = docTransDAO.getDocumentTransCount(tracingID);
    _logger.logMessage("isExistDependentDocumentTrans", null, "Document Trans Count is "+docTransCount);
    return docTransCount > 0;
  }
  
  public boolean isDependentOnDocument(String tracingID)
  {
    DocTransDAO dao = new DocTransDAO();
    boolean isExist = isExistDependentDocumentTrans(tracingID, dao);
    if(isExist)
    {
      _logger.logMessage("archiveOrphanTraceEventInfo", null, "Exist dependent doc trans given tracingID "+tracingID);
    }
    
    return isExist;
  }
  
  /**
   * Archive the TraceEventInfoHeader given the tracingID.
   * @param tracingID 
   * @param eventDAO the dao to be used to perform DB operation
   * @param archiveHelper The archive zip helper
   * @param subPath The subPath we store the TraceEventInfo Header in the archive zip
   * @throws ArchiveTrailDataException If we can't find any TraceEventHeader given the tracingID or we have problenm adding the entity into 
   *                                   the arcihve zip.
   */
  private void archiveTraceEventInfoHeader(String tracingID, TraceEventInfoDAO eventDAO, ArchiveHelper archiveHelper)
    throws ArchiveTrailDataException
  {
    String methodName = "archiveTraceEventInfoHeader";
    TraceEventInfoHeader header = getTraceEventHeader(tracingID, eventDAO);
    if(header != null)
    {
      archiveHelper.addAuditTrailEntity(header, null, archiveHelper.getArchiveZipCategory()+IArchiveConstant.CATEGORY_TRACE_EVENT_HEADER);
      deleteAuditTrailEntity(eventDAO, header);
    }
  }
  
  /**
   * Archive the TraceEventInfo
   * @param eventInfoList the list of TraceEventInfo to be deleted.
   * @param archiveHelper The archive zip helper
   * @param subPath The subPath we store the TraceEventInfo in the archive zip
   * @throws ArchiveTrailDataException
   */
  private void archiveTraceEventInfo(Collection<TraceEventInfo> eventInfoList, ArchiveHelper archiveHelper)
    throws ArchiveTrailDataException
  {
    String methodName = "archiveTraceEventInfo";
    TraceEventInfoDAO dao = new TraceEventInfoDAO();
    
    if(eventInfoList != null && eventInfoList.size() > 0)
    {
      Iterator<TraceEventInfo> ite = eventInfoList.iterator();
      while(ite.hasNext())
      {
        TraceEventInfo eventInfo = (TraceEventInfo)ite.next();
        String bizDocumentUID = eventInfo.getBizDocumentUID();
    
        _logger.logMessage(methodName, null, "Archive trace evetn info is "+ eventInfo);
        
        archiveHelper.addAuditTrailEntity(eventInfo, null, archiveHelper.getArchiveZipCategory()+IArchiveConstant.CATEGORY_TRACE_EVENT_INFO);
        deleteAuditTrailEntity(dao, eventInfo);
        
        if(bizDocumentUID != null && !"".equals(bizDocumentUID))
        {
            archiveBizDocument(bizDocumentUID, archiveHelper);
        }
        
        _logger.logMessage(methodName, null, ""+eventInfo);
      }

    }
    else
    {
      ArchiveTrailDataException ex = new ArchiveTrailDataException("[AuditTrailArchiveManager]."+methodName+" the given event info list is empty or null !");
      _logger.logWarn(methodName, null, "the given event info list is empty or null !", ex);
    }
  }
  
  /**
   * Archve the BizDocument given the bizDocumentUID
   * @param bizDocumentUID
   * @param archiveHelper
   * @param subPath
   * @throws ArchiveTrailDataException
   */
  private void archiveBizDocument(String bizDocumentUID, ArchiveHelper archiveHelper)
    throws ArchiveTrailDataException
    
  {
    String methodName = "archiveBizDocument";
    BizDocumentDAO bizDocDAO = new BizDocumentDAO();
    BizDocument bizDoc = bizDocDAO.getBizDocumentByUID(bizDocumentUID);
    if(bizDoc != null)
    {
        archiveHelper.addAuditTrailEntity(bizDoc, null,archiveHelper.getArchiveZipCategory()+IArchiveConstant.CATEGORY_BIZ_DOCUMENT);
        deleteAuditTrailEntity(bizDocDAO, bizDoc);
    } else
    {
        throw new ArchiveTrailDataException("No BizDocument entity found for uid: "+bizDocumentUID);
    }
  }
  
  /**
   * Return the Archive folder dir path
   * @return
   */
  public String getArchiveFolderPath()
  {
    String currentWorkingDir = SystemUtil.getWorkingDirPath()+"/";
    
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    String archiveFolder = configStore.getProperty(IISATProperty.CATEGORY, IISATProperty.ARCHIVE_FOLDER, null);
    String absArchiveFolderPath = currentWorkingDir + archiveFolder;
    _logger.logMessage("getArchiveFolderPath", null, "Abs archive folder path is "+absArchiveFolderPath);
    return absArchiveFolderPath;
  }
  
  /**
   * Return the total allowable number of ProcessTrans entities that can be stored in the arcihve zip file.
   * @return
   */
  public int getTotalProcessTransEntryInArchiveZip()
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    String totalProcessTransInZip = configStore.getProperty(IISATProperty.CATEGORY, IISATProperty.TOTAL_PROCESS_TRANS_IN_ARCHIVE_FILE, null);
    if(totalProcessTransInZip != null)
    {
      return Integer.parseInt(totalProcessTransInZip);
    }
    else
    {
      return 1000;
    }
  }
  
  /**
   * Return the total allowable number of orphan TraceEventInfo entities that can be stored in the arcihve zip file.
   * @return
   */
  public int getTotalOrphanTraceEventInfoInArchiveZip()
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    String totalEventInfoInZip = configStore.getProperty(IISATProperty.CATEGORY, IISATProperty.TOTAL_ORPHAN_TRACE_EVENT_INFO_IN_ZIPFILE, "3000");
    return Integer.parseInt(totalEventInfoInZip);
  }
  
  /**
   * Archive trace event info, biz document, trace event info header that linked to the given tracingID.
   * @param tracingID The tracingID of the trace event info
   * @param archiveHelper The helper that convert the entity into xml form, and add into archive zip file
   * @throws ArchiveTrailDataException Throw while we have problem in archive the audit trail data.
   */
  public void archiveTraceEventInfoGroup(String tracingID, ArchiveHelper archiveHelper) throws ArchiveTrailDataException
  {
    System.out.println("Archiving trace event info group");
    TraceEventInfoDAO eventDAO = new TraceEventInfoDAO();
    DocTransDAO docDAO = new DocTransDAO();
    
    String archivedTraceEventHeader = "";
    Collection<TraceEventInfo> eventInfos = eventDAO.getTraceEventInfoByTracingID(tracingID);
    if(eventInfos != null && eventInfos.size() > 0)
    {
      Iterator<TraceEventInfo> ite = eventInfos.iterator();
      while(ite.hasNext())
      {
        TraceEventInfo info = ite.next();
        ArrayList<TraceEventInfo> eventInfoList = new ArrayList<TraceEventInfo>();
        eventInfoList.add(info);
        
        archiveTraceEventInfo(eventInfoList, archiveHelper);
        
        if("".equals(archivedTraceEventHeader))
        {
          archiveTraceEventInfoHeader(info.getTracingID(), eventDAO, archiveHelper);
          archivedTraceEventHeader = info.getTracingID();
        }
        System.out.println("Archived trace event header is "+archivedTraceEventHeader);
        _logger.logMessage("archiveOrphanTraceEventInfo", null, "Archived trace event header is "+archivedTraceEventHeader);
      }
    }
    else
    {
      throw new ArchiveTrailDataException("[AuditTrailArchiveManagerBean.archiveOrphanTraceEventInfo] No trace event info can be found given tracingID "+tracingID);
    }
    System.out.println("end archiving orphan record ...");
  }
  
  //TODO : refine !!!!!!!!
  /**
   * Restore the audit trail entity back to DB.
   */
  public void restoreAuditTrailEntity(IAuditTrailEntity trailEntity)
  {
    String methodName = "restoreAuditTrailEntity";
    
    DAO dao = new DAO(false);
    String uid = trailEntity.getUID();
    IAuditTrailEntity entity = (IAuditTrailEntity)dao.get(trailEntity.getClass(), uid);
    if(entity == null)
    {
      dao.create(trailEntity);
    }
    else
    {
      _logger.logMessage(methodName, null, "The AuditTrail entity "+entity+" already exist in DB.");
    }
  }
  
  /**
   * Retrieve all the TM summary filename, and the file last modified date
   * @return
   */
  public Collection<String[]> getAllArchiveSummaryFile()
  {
    ArrayList<String[]> tmSummaryInfoList = new ArrayList<String[]>();
    
    String archiveFolderPath = getArchiveFolderPath();
    File archiveFolder = new File(archiveFolderPath);
    File[] files = archiveFolder.listFiles();
    
    for(int i= 0; files != null && i < files.length; i++)
    {
      File tmSummaryFile = files[i];
      String summaryFilename = tmSummaryFile.getName();
      
      if(tmSummaryFile.isFile() && summaryFilename.indexOf(".xml") >= 0)
      {
        String lastModifiedDate = new Date(tmSummaryFile.lastModified()).toString();
        String[] summaryInfo = new String[]{summaryFilename, lastModifiedDate};
        tmSummaryInfoList.add(summaryInfo);
      }
    }
    return tmSummaryInfoList;
  }
  
  /**
   * Get the TM summary file in byte[] format given the summary filename
   * @param summaryFilename the name of the archive summary file.
   * @return
   * @throws Exception 
   */
  public byte[] getTMSummaryFileContent(String summaryFilename) throws Exception
  {
    String methodName = "summaryFilename";
    
    String archiveFolderPath = getArchiveFolderPath();
    File summaryFile = new File(archiveFolderPath+summaryFilename);
    if(summaryFile.isDirectory() || ! summaryFile.exists())
    {
      _logger.logMessage(methodName, null ,"The given summary filename "+summaryFilename+" is not exist !");
      return new byte[]{};
    }
    else
    {
      FileInputStream input = new FileInputStream(summaryFile);
      byte[] summaryContent = IOUtil.read(input);
      String summaryInXML = new String(summaryContent);
      _logger.logMessage(methodName, null, "Retrieve summary file is "+summaryInXML);
      return summaryContent;
    }
  }
  
  public String getArchiveSummaryRenderingUrl()
  {
    ConfigurationStore config = ConfigurationStore.getInstance();
    return config.getProperty(IISATProperty.CATEGORY, IISATProperty.SUMMARY_FILE_URL, null);
  }
  
  private void deleteAuditTrailEntities(AuditTrailEntityDAO dao, Collection<IAuditTrailEntity> trailEntities)
  {
    if(trailEntities != null && trailEntities.size() > 0)
    {
      Iterator<IAuditTrailEntity> ite = trailEntities.iterator();
      deleteAuditTrailEntity(dao, ite.next());
    }
  }
  
  private TraceEventInfoHeader getTraceEventHeader(String tracingID, TraceEventInfoDAO eventDAO) throws ArchiveTrailDataException
  {
    String method = "getTraceEventHeader";
    TraceEventInfoHeader header = eventDAO.getTraceEventInfoHeader(tracingID);
    if(header == null)
    {
        _logger.logMessage("", null, "No TraceEventHeader entity found for tracingID: "+tracingID);
    }
    
    return header;
  }
  
  /**
   * Get the URL to forward the archive/restore status to
   * @return the response URL or null if the properties is not set in DB
   */
  public String getArchiveStatusForwardUrl()
  {
    String methosName = "getArchiveStatusForwardUrl";
    ConfigurationStore config = ConfigurationStore.getInstance();
    String archiveStatusForwardURL = config.getProperty(IISATProperty.CATEGORY, IISATProperty.ARCHIVE_STATUS_RESPONSE_URL, null);
    _logger.logMessage(methosName, null, "URL retrieve is "+archiveStatusForwardURL);
    
    return archiveStatusForwardURL;
  }
  
  /**
   * Delete the trailEntity from DB.
   * @param dao
   * @param trailEntity
   */
  private void deleteAuditTrailEntity(AuditTrailEntityDAO dao, IAuditTrailEntity trailEntity)
  {
    dao.delete(trailEntity);
  }
  
  private Collection<String> convertStrToCollection(String groupListInStr)
  {
    ArrayList<String> groupList = new ArrayList<String>();
    StringTokenizer st = new StringTokenizer(groupListInStr, ";");
    while(st.hasMoreTokens())
    {
      groupList.add(st.nextToken());
    }
    return groupList;
  }
  
  public Collection<Long> getOrphanProcessInstanceUIDWithGroupInfo(Hashtable criteria)
  {
    String mn = "getOrphanProcessInstanceUIDWithGroupInfo";
    Long fromStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
    Long toStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
    String groupListInStr = (String)criteria.get(IArchiveConstant.GROUP_INFO);
    Collection<String> groupList = ArchiveActivityHelper.convertStrToList(groupListInStr, ";");
    
    ProcessTransactionDAO dao = new ProcessTransactionDAO();
    Long minProcessInstanceUID = dao.retrieveProInstanceUIDOrderByStartTime(new Date(fromStartDateTime), new Date(toStartDateTime), groupList);
    _logger.logMessage(mn, null, "Min Process instance uid is "+minProcessInstanceUID);
    
    return dao.retrieveProInstanceUIDLesserThan(minProcessInstanceUID, groupList);
  }
  
  public Collection<Long> getOrphanProcessInstanceUIDWithoutGroupInfo(Hashtable criteria)
  {
    String mn = "getOrphanProcessInstanceUIDWithoutGroupInfo";
    Long fromStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
    Long toStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
    
    _logger.logMessage(mn, null, "From Start Date "+new Date(fromStartDateTime)+" to start date "+new Date(toStartDateTime));
    
    ProcessTransactionDAO dao = new ProcessTransactionDAO();
    return dao.retrieveProInstanceUID(new Date(fromStartDateTime), new Date(toStartDateTime));
  }
  
  public void delegateArchiveRequest(Hashtable criteria, String category) throws Exception
  {
    String mn = "delegateArchiveRequest";
    _logger.logMessage(mn ,null, "Audit MGR delegating request to TM plugin "+criteria);
    
    JmsSender jmsSender = new JmsSender();
    Properties p = null;
    try
    {
      p = ArchiveActivityHelper.getJMSSenderProperties(category);
    }
    catch (RemoteException e)
    {
      throw new Exception("EJB Invocation Error. Unable to retrieve JMS sender properties", e);
    }
    catch (NamingException e)
    {
      throw new Exception("JNDI Error. Unable to retrieve JMS sender properties", e);
    }
    catch (CreateException e)
    {
      throw new Exception("EJB Creation Error. Unable to retrieve JMS sender properties", e);
    }
    jmsSender.setSendProperties(p);
    
    //Hashtable msgSelector = getJmsMsgProps();
    jmsSender.send(criteria);
  }
  
/**
   * Determine whether we will perform the archive by document in GT based on the archiveRestoreStatus
   * @param archiveStatusHT
   * @throws Exception
   */
  public void handleGTArchiveStatus(Hashtable archiveStatusHT) throws Exception
  {
    String methodName = "handleGTArchiveRestoreStatus";
    _logger.logMessage(methodName, null, "Receving the GT achive status !");
    ArchiveActivityHelper.obtainArchiveSummaryLock(); //Obtain global lock so that the update on ArchiveSummary, ArchiveJob.properties will in sync
    
    String archiveOp = (String)archiveStatusHT.get(IGTArchiveConstant.ARCHIVE_OPERATION);
    if(IGTArchiveConstant.ARCHIVE_OP_ARCHIVE.equals(archiveOp))
    {
      boolean isArchived = IGTArchiveConstant.ARCHIVE_OP_ARCHIVE.equals(archiveOp) ? true : false;
      String archiveType = (String)archiveStatusHT.get(IGTArchiveConstant.ARCHIVE_TYPE);
      Boolean isArchiveSuccess = (Boolean)archiveStatusHT.get(IGTArchiveConstant.ARCHIVE_STATUS);
      String summaryFilename = (String)archiveStatusHT.get(IGTArchiveConstant.ARCHIVE_SUMMARY_FILE);
      String archiveID = (String)archiveStatusHT.get(IGTArchiveConstant.ARCHIVE_ID);
      String jobID = (String)archiveStatusHT.get(IGTArchiveConstant.ARCHIVE_JOBS_ID);
      boolean isArchiveOrphanRecord = (Boolean)archiveStatusHT.get(IGTArchiveConstant.ARCHIVE_ORPHAN_RECORD);
      Long fromStartDate = (Long)archiveStatusHT.get(IGTArchiveConstant.FROM_START_DATE_TIME);
      Long toStartDate = (Long)archiveStatusHT.get(IGTArchiveConstant.TO_START_DATE_TIME);
      
      _logger.debugMessage(methodName, null,"isArchived: "+isArchived+" archiveRestoreStatusHT: "+archiveStatusHT);
      
      updateGTArchiveStatus(archiveType,isArchiveSuccess, summaryFilename, archiveID);
      
      if(IGTArchiveConstant.ARCHIVE_TYPE_PI.equals(archiveType) && isArchiveSuccess && ! isArchiveOrphanRecord)
      {
        delegateArchiveByDocumentRequest(archiveID, jobID, fromStartDate, toStartDate);
      }
      
      //update the archive job properties of the current node and the master node
      updateJobStatus(archiveType, jobID, isArchiveSuccess, archiveID, isArchiveOrphanRecord);
      
    }
  }
  
/**
   * Delegate the archive request to Gridtalk. 
   * @param criteria The criteria that is used for archive out the Process
   * @param archiveID The unique ID that is used to link back the archive summary in TM.
   * @throws Exception
   */
  public void delegateArchiveRequestToGT(Hashtable criteria, String archiveID) throws ArchiveTrailDataException
  {
    String methodName = "delegateArchiveRequestToGT";
    
    _logger.logMessage(methodName, null, "Delegating archive request to GT ..... ");
    String archiveName = (String)criteria.get(IArchiveConstant.ARCHIVE_NAME);
    String description = (String)criteria.get(IArchiveConstant.ARCHIVE_DESCRIPTION);
    Long fromStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
    Long toStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME); 
    Boolean archiveOrphanRecord = (Boolean)criteria.get(IArchiveConstant.ARCHIVE_ORPHAN_RECORD);
    String jobID = (String)criteria.get(IArchiveConstant.ARCHIVE_JOBS_ID);
    String archiveType = IGTArchiveConstant.ARCHIVE_TYPE_PI;
    
    boolean isEnabledSearchArchived = false;
    boolean isEnabledRestore = true;
    
    String groupList = (String)criteria.get(IArchiveConstant.GROUP_INFO);
    _logger.logMessage("", null, "Group List is "+groupList);
    
    String customerList = mapGroupToCustomerID(groupList);
    _logger.logMessage("", null, "Customer list is "+customerList);
    
    //create map msg
    Hashtable<String, Object> archiveCriteria = new Hashtable<String, Object>();
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_NAME, archiveName);
    
    if(description != null)
    {
      archiveCriteria.put(IGTArchiveConstant.ARCHIVE_DESCRIPTION, description);
    }
    
    archiveCriteria.put(IGTArchiveConstant.FROM_START_DATE_TIME, fromStartDateTime);
    archiveCriteria.put(IGTArchiveConstant.TO_START_DATE_TIME, toStartDateTime);
    archiveCriteria.put(IGTArchiveConstant.IS_ENABLED_ARCHIVED_SEARCHED, isEnabledSearchArchived);
    archiveCriteria.put(IGTArchiveConstant.IS_ENABLED_RESTORE, isEnabledRestore);
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_ID, archiveID);
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_TYPE, archiveType);
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_OPERATION, IGTArchiveConstant.ARCHIVE_OP_ARCHIVE);
    archiveCriteria.put(IGTArchiveConstant.CUSTOMER_ID, customerList);
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_ORPHAN_RECORD, archiveOrphanRecord);
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_JOBS_ID, jobID);
    
    _logger.logMessage(methodName, null, "TM Archive Criteria for Process passing to GT is "+archiveCriteria);
    
    try
    {
        //ArchiveActivityHelper.sendJmsMsg(archiveCriteria, IISATProperty.GT_ARCHIVE_JMS_CATEGORY);
        delegateArchiveRequest(archiveCriteria, IISATProperty.GT_ARCHIVE_JMS_CATEGORY);
    }
    catch(Exception ex)
    {
      archiveCriteria.put(IGTArchiveConstant.ARCHIVE_STATUS, false);
      
      try
      {
        handleGTArchiveStatus(archiveCriteria);
      }
      catch(Exception e)
      {
        _logger.logError(ILogErrorCodes.AT_ARCHIVE_STATUS_UPDATE_ERROR, methodName, null, "Can't update the archive status. ArchiveID: "+archiveID+" jobID: "+jobID+" Archive type: "+archiveType+" archiveStatus: "+Boolean.FALSE.toString(), e);
      }
      
      throw new ArchiveTrailDataException("Error in sending the archive request to GT "+ex.getMessage(), ex);
    }
  }
  
  private void delegateArchiveByDocumentRequest(String archiveID, String jobID, Long fromStartDate, Long toStartDate) throws Exception
  {
    String mn = "delegateArchiveByDocumentRequest";
    
    //  Archive By PI success.Trigger the archive by document request to TM plug-in
    File archiveSummaryFile = ArchiveActivityHelper.getArchiveSummaryFile(archiveID, getArchiveFolderPath());
    ArchiveSummary summary = ArchiveActivityHelper.getArchiveSummary(archiveSummaryFile);
    
    if(fromStartDate != null && toStartDate != null)
    {
      _logger.logMessage(mn, null, "New set fromStartDate: "+ new Date(fromStartDate)+" toStartDate :"+new Date(toStartDate));
      
      ArchiveCriteria criteria = summary.getArchiveCriteria();
      criteria.setFromStartDate(new Date(fromStartDate));
      criteria.setToStartDate(new Date(toStartDate));
    }
    
    String archiveType = IGTArchiveConstant.ARCHIVE_TYPE_DOCUMENT;
    Hashtable gtArchiveByDocArchiveInfo = getGTArchiveByDocumentArchiveInfo(summary, jobID); 
    _logger.logMessage(mn, null, "TM Archive Criteria for Document passing to GT is "+gtArchiveByDocArchiveInfo);
    
    try
    {
      delegateArchiveRequest(gtArchiveByDocArchiveInfo, IISATProperty.GT_ARCHIVE_JMS_CATEGORY);
    }
    catch(Exception ex)
    {
      gtArchiveByDocArchiveInfo.put(IGTArchiveConstant.ARCHIVE_STATUS, false);
      try
      {
        handleGTArchiveStatus(gtArchiveByDocArchiveInfo);
      }
      catch(Exception e)
      {
        _logger.logError(ILogErrorCodes.AT_ARCHIVE_STATUS_UPDATE_ERROR, mn, null, "Can't update the archive status. ArchiveID: "+archiveID+" jobID: "+jobID+" Archive type: "+archiveType +" Archive status: "+Boolean.FALSE.toString(), e);
      }
      
      throw ex;
    }
  }
  
/**
   * Get the necessary info that is required to be populated into the GT summary file. It also include the
   * criteria for performing the archive for example the date range.
   * @param archiveSummary
   * @return
   */
  private Hashtable getGTArchiveByDocumentArchiveInfo(ArchiveSummary archiveSummary, String jobID)
  {
    String archiveName = archiveSummary.getArchiveName()+"_DOC";
    String description = archiveSummary.getArchiveDescription();
    boolean isEnabledSearchArchived = false;
    boolean isEnabledRestore = true;
    String archiveID = archiveSummary.getArchiveID();
    
    ArchiveCriteria criteria = archiveSummary.getArchiveCriteria();
    Long fromStartDateTime = criteria.getFromStartDate().getTime();
    Long toStartDateTime = criteria.getToStartDate().getTime();
    
    String groupList = criteria.getGroup();
    _logger.logMessage("", null, "Group List is "+groupList);
    
    String customerList = mapGroupToCustomerID(groupList);
    _logger.logMessage("", null, "Customer list is "+customerList);
    
    Boolean archiveOrphanRecord = criteria.isArchiveOrphanRecord();
    
    Hashtable<String, Object> archiveInfo = new Hashtable<String, Object>();
    archiveInfo.put(IGTArchiveConstant.ARCHIVE_NAME, archiveName);
    
    if(description != null)
    {
      archiveInfo.put(IGTArchiveConstant.ARCHIVE_DESCRIPTION, description);
    }
    archiveInfo.put(IGTArchiveConstant.FROM_START_DATE_TIME, fromStartDateTime);
    archiveInfo.put(IGTArchiveConstant.TO_START_DATE_TIME, toStartDateTime);
    archiveInfo.put(IGTArchiveConstant.IS_ENABLED_ARCHIVED_SEARCHED, isEnabledSearchArchived);
    archiveInfo.put(IGTArchiveConstant.IS_ENABLED_RESTORE, isEnabledRestore);
    archiveInfo.put(IGTArchiveConstant.ARCHIVE_ID, archiveID);
    archiveInfo.put(IGTArchiveConstant.ARCHIVE_TYPE, IGTArchiveConstant.ARCHIVE_TYPE_DOCUMENT);
    archiveInfo.put(IGTArchiveConstant.ARCHIVE_OPERATION, IGTArchiveConstant.ARCHIVE_OP_ARCHIVE);
    archiveInfo.put(IGTArchiveConstant.CUSTOMER_ID, customerList);
    archiveInfo.put(IGTArchiveConstant.ARCHIVE_ORPHAN_RECORD, archiveOrphanRecord);
    archiveInfo.put(IGTArchiveConstant.ARCHIVE_JOBS_ID, jobID);
    
    return archiveInfo;
  }
  
  public String mapGroupToCustomerID(String groupInfo)
  {
    String customerInStr = "";
    ArrayList<String> customerIDs = ArchiveActivityHelper.mapGroupToCustomerID(groupInfo);
    if(customerIDs != null && customerIDs.size() > 0)
    {
      customerInStr = ArchiveActivityHelper.convertListToStr(customerIDs, ";");
    }
    return customerInStr;
  }
  
  public List<String> getAllAvailableGroup()
  {
    return ArchiveActivityHelper.getAllGroup();
  }
  
/**
   * The GT side has finished performed the archive. We will update the GT summary filename.
   * NOTE: associated a global lock prior calling this method.
   * @param archiveType
   * @param isArchiveSuccess
   * @param gtSummaryFilename
   * @param archiveID
   * @throws Exception
   */
  public void updateGTArchiveStatus(String archiveType, Boolean isArchiveSuccess, String gtSummaryFilename, String archiveID) throws Exception
  {
    String methodName = "updateGTArchiveStatus";
    //ArchiveActivityHelper.obtainArchiveSummaryLock();
    
    File summaryFile = ArchiveActivityHelper.getArchiveSummaryFile(archiveID, getArchiveFolderPath());
    _logger.logMessage(methodName, null, "Summary File retrieve is "+summaryFile.getAbsolutePath());
    
    ArchiveSummary archiveSummary = ArchiveActivityHelper.getArchiveSummary(summaryFile);
    _logger.logMessage(methodName, null, "ArchiveSummary before update is "+archiveSummary);
    
    if(IGTArchiveConstant.ARCHIVE_TYPE_PI.equals(archiveType))
    {
      String gtSummary = archiveSummary.getGtPISummaryFilename();
      if(gtSummary != null && gtSummary.length() > 0)
      {
        gtSummaryFilename = gtSummary + ","+ gtSummaryFilename;
      }
      archiveSummary.setGtPISummaryFilename(gtSummaryFilename);
      
      if(archiveSummary.isGTPIArchivedSuccess())
      {
        archiveSummary.setGTPIArchivedSuccess(isArchiveSuccess);
      }
      
      if(! isArchiveSuccess)
      {
        archiveSummary.setGTDocArchivedSuccess(false);
      }
    }
    else if(IGTArchiveConstant.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
    {
      String gtSummary = archiveSummary.getGtDocSummaryFilename();
      if(gtSummary != null && gtSummary.length() > 0)
      {
        gtSummaryFilename = gtSummary +"," + gtSummaryFilename;
      }
      archiveSummary.setGtDocSummaryFilename(gtSummaryFilename);
      
      if(archiveSummary.isGTDocArchivedSuccess())
      {
        archiveSummary.setGTDocArchivedSuccess(isArchiveSuccess);
      }
    }
    
    _logger.logMessage(methodName, null, "ArchiveSummary after update is "+archiveSummary);
    ArchiveActivityHelper.generateArchiveSummaryFile(archiveSummary, summaryFile);
  }
  
/**
   * Update the status of a particular job activity. The activity include GT archive by process, GT archive by document, and the TM archive.
   * If all archive activities completed, it will update the job status to the master node.
   * 
   * NOTE: associated a global lock prior calling this method.
   * @param archiveType
   * @param jobID
   * @param isArchiveSuccess
   * @param archiveID
   * @param isArchiveOrphan
   * @throws Exception
   */
  public void updateJobStatus(String archiveType, String jobID, boolean isArchiveSuccess, String archiveID,
                                          boolean isArchiveOrphan) throws Exception
  {
    //ArchiveActivityHelper.obtainArchiveSummaryLock();
    String mn = "updateJobStatus";
    
    String folderPath = getArchiveFolderPath();
    Properties pro = ArchiveActivityHelper.getPropertiesFile(folderPath, jobID);
    String archiveStatus = isArchiveSuccess ? (Boolean.TRUE.toString()) : IArchiveConstant.ARCHIVE_STATUS_FAILED;
    
    _logger.logMessage(mn, null, "Updating job status: archive type: "+archiveType+" jobID:"+jobID+" isArchiveSuccess: "+isArchiveSuccess+" isArchiveOrphan: "+isArchiveOrphan);
    _logger.logMessage(mn, null, "Job Properties prior update: "+pro);
    
    if(IArchiveConstant.ARCHIVE_TYPE_TM.equals(archiveType))
    {
      pro.setProperty(IArchiveConstant.ARCHIVE_BY_TM, archiveStatus);
    }
    else if(IGTArchiveConstant.ARCHIVE_TYPE_PI.equals(archiveType))
    {
      pro.setProperty(IArchiveConstant.ARCHIVE_BY_PI, archiveStatus);
      
      if(isArchiveOrphan)
      {
        pro.setProperty(IArchiveConstant.ARCHIVE_BY_DOCUMENT, Boolean.TRUE.toString());
      }
      else if(! isArchiveOrphan && ! isArchiveSuccess)
      {
        _logger.logMessage(mn, null, "archive document set to failed");
        pro.setProperty(IArchiveConstant.ARCHIVE_BY_DOCUMENT, IArchiveConstant.ARCHIVE_STATUS_FAILED);
      }
    }
    else if(IGTArchiveConstant.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
    {
      pro.setProperty(IArchiveConstant.ARCHIVE_BY_DOCUMENT, archiveStatus);
    }
    else
    {
      throw new IllegalArgumentException("Failed to update current node job status. Archive Type: "+archiveType+" is not supported.");
    }
    
    _logger.logMessage(mn, null, "Job Properties after update: "+pro);
    ArchiveActivityHelper.generatePropertiesFile(folderPath, pro, jobID);
    
    updateMasterNodeStatus(pro, jobID, archiveID);
  }
  
/**
   * Update the archive job status of the current node to master node.
   * @param jobProperties
   * @param jobID
   * @param archiveID
   * @throws Exception
   */
  private void updateMasterNodeStatus(Properties jobProperties, String jobID, String archiveID) throws Exception
  {
    String mn = "updateMasterNodeStatus";
    Enumeration enu = jobProperties.propertyNames();
    boolean isAllActivityCompleted = true;
    boolean isActivitySuccess = true;
    
    
    if(! enu.hasMoreElements())
    {
      _logger.logWarn(mn, null, "Job properties given jobID: "+jobID+" contains no element", null);
      return;
    }
    
    while(enu.hasMoreElements())
    {
      String activity = (String)enu.nextElement();
      String status = jobProperties.getProperty(activity);
      if(Boolean.FALSE.toString().equals(status))
      {
        isAllActivityCompleted = false;
      }
      else if(IArchiveConstant.ARCHIVE_STATUS_FAILED.equals(status))
      {
        isActivitySuccess = false;
      }
    }
    
    if(isAllActivityCompleted)
    {
      persistArchiveStatus(archiveID, jobID, isActivitySuccess);
    }
  }
  
/**
   * Save the archive job status of the current node
   * @param archiveID
   * @param jobID
   * @param isArchiveSuccess
   * @throws Exception
   */
  private void persistArchiveStatus(String archiveID, String jobID, boolean isArchiveSuccess) throws Exception
  {
    String mn = "updateArchiveJobStatus";
    //ArchiveActivityHelper.obtainArchiveSummaryLock();
    
    String archiveFolderPath = getArchiveFolderPath();
    Properties archiveJobProperties = ArchiveActivityHelper.getArchiveJobProperties(archiveID, archiveFolderPath);
    String jobStatus = archiveJobProperties.getProperty(jobID);
    if(jobStatus == null)
    {
      throw new NullPointerException("Can't get the job status given the archiveID: "+archiveID+" jobID: "+jobID);
    }
    else
    {
      if(! isArchiveSuccess)
      {
        jobStatus = IArchiveConstant.ARCHIVE_STATUS_FAILED;
      }
      else
      {
        jobStatus = Boolean.TRUE.toString();
      }
    }
    _logger.logMessage(mn, null, "Update archive job with jobID: "+jobID+" status :"+jobStatus);
    archiveJobProperties.setProperty(jobID, jobStatus);
    ArchiveActivityHelper.updateArchiveProperties(archiveID, archiveJobProperties, archiveFolderPath);
  }
  
  public void updateTMArchiveStatus(String archiveID, String jobID, boolean isTMArchivedSuccess,
                                    String[] zipFileList, int totalArchiveProcessTransaction, int totalIncompleteDocTrans,
                                    int totalIncompleteProcess) throws Exception
  {
    String methodName = "updateTMArchiveStatus";
    
    ArchiveActivityHelper.obtainArchiveSummaryLock(); //Obtain global lock so that the update on ArchiveSummary, ArchiveJob.properties will in sync
    
    File summaryFile = new File(getArchiveFolderPath() + archiveID+".xml");
    if(summaryFile.exists())
    {
      //udpate the archive summary share by all nodes
      ArchiveSummary summary = ArchiveActivityHelper.getArchiveSummary(summaryFile);
      _logger.logMessage(methodName, null, "Archive summary is "+summary);
      
      String zipFiles = summary.getZipFileList() == null ? "" : summary.getZipFileList();
      Integer currentTotalArchiveProcessTrans = summary.getTotalArchiveProcessTransaction() == null ? 0 : summary.getTotalArchiveProcessTransaction();
      Integer currentTotalIncompleteDocTrans = summary.getTotalIncompleteDocument() == null ? 0 : summary.getTotalIncompleteDocument();
      Integer currentTotalIncompleteProcessTrans = summary.getTotalIncompleteProcessTrans() == null ? 0 : summary.getTotalIncompleteProcessTrans();
      
      if(zipFiles.length() > 0)
      {
        zipFiles += ",";
      }
      summary.setZipFileList(zipFiles + ArchiveActivityHelper.convertArrToStr(zipFileList, ","));
      summary.setTotalArchiveProcessTransaction(totalArchiveProcessTransaction + currentTotalArchiveProcessTrans);
      summary.setTotalIncompleteDocument(totalIncompleteDocTrans + currentTotalIncompleteDocTrans);
      summary.setTotalIncompleteProcessTrans(totalIncompleteProcess + currentTotalIncompleteProcessTrans);
      
      if(summary.isTMArchivedSuccess())
      {
        summary.setTMArchivedSuccess(isTMArchivedSuccess);
      }
      
      _logger.logMessage(methodName, null, "Archive Summary after update "+summary);
      ArchiveActivityHelper.generateArchiveSummaryFile(summary, summaryFile);
      
      //update the archive job properties of the current node
      updateJobStatus(IArchiveConstant.ARCHIVE_TYPE_TM, jobID, isTMArchivedSuccess, archiveID, false);
    }
    else
    {
      throw new ArchiveTrailDataException("The archive summary file :"+summaryFile.getAbsolutePath()+" is not exist");
    }
  }
  
  public void updateGTRestoreStatus(String archiveType, Boolean isRestoreSuccess,String archiveID) throws Exception
  {
    String methodName = "updateGTRestoreStatus";
    ArchiveActivityHelper.obtainArchiveSummaryLock();
    
    File summaryFile = ArchiveActivityHelper.getArchiveSummaryFile(archiveID, getArchiveFolderPath());
    _logger.logMessage(methodName, null, "Summary File retrieve is "+summaryFile.getAbsolutePath());
    
    ArchiveSummary archiveSummary = ArchiveActivityHelper.getArchiveSummary(summaryFile);
    
    if(IGTArchiveConstant.ARCHIVE_TYPE_PI.equals(archiveType))
    {
      archiveSummary.setGTPIRestoreSuccess(isRestoreSuccess);
    }
    else if(IGTArchiveConstant.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
    {
      archiveSummary.setGTDocRestoreSuccess(isRestoreSuccess);
    }
    
    ArchiveActivityHelper.generateArchiveSummaryFile(archiveSummary, summaryFile);
  }
  
  public void updateTMRestoreStatus(boolean isRestoreSuccess, String summaryFilename) throws Exception
  {
    ArchiveActivityHelper.obtainArchiveSummaryLock();
    
    String archiveFolderDir = getArchiveFolderPath();
    String summaryAbsPath = archiveFolderDir+summaryFilename;
    ArchiveSummary summary = ArchiveActivityHelper.getArchiveSummary(summaryAbsPath);
    summary.setTMRestoreSuccess(isRestoreSuccess);
    ArchiveActivityHelper.generateArchiveSummaryFile(summary, new File(summaryAbsPath));
  }
  
  public void ejbCreate()
  {
    _logger = LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }

  public void ejbActivate()
    throws EJBException, RemoteException
  {
  }

  public void ejbPassivate()
    throws EJBException, RemoteException
    {
    }

  public void ejbRemove()
    throws EJBException, RemoteException
    {
    }

  public void setSessionContext(SessionContext ctxt)
    throws EJBException, RemoteException
  {
    _ctxt = ctxt;
  }
}
