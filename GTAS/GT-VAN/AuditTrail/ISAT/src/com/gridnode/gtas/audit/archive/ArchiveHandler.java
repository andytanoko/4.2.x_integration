/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 12, 2007    Tam Wei Xiang       Created
 * Mar 07 2007		Alain Ah Ming				Added warning log before throwing
 * 																		exceptions
 * Apr 04 2007     Tam Wei Xiang      To auto create the archive folder if 
 *                                    it doesn't exist.
 * Jun 06 2007     Tam Wei Xiang      Performance tuning on archival                                   
 */
package com.gridnode.gtas.audit.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalObj;
import com.gridnode.gtas.audit.archive.helper.ArchiveActivityHelper;
import com.gridnode.gtas.audit.archive.helper.ArchiveHelper;
import com.gridnode.gtas.audit.archive.helper.ArchiveZipFile;
import com.gridnode.gtas.audit.common.IGTArchiveConstant;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.model.BizEntityGroupMapping;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.alert.AlertUtil;
import com.gridnode.util.io.IOUtil;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to archive the obsolete record inside the OTC DB.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveHandler
{
  private static final String CLASS_NAME = "ArchiveHandler";
  private Logger _logger;
  
  private String _archiveDescription;
  private String _archiveName;
  
  private ArrayList<String> _zipFileList = new ArrayList<String>();
  private String _archiveFolderPath;
  private int _totalEntriesInZip;
  private String _archiveID;
  private String _jobID;
  
  private Hashtable _archiveCriteria;
  private ArchiveHelper _archiveHelper;
  
  private Integer _totalArchiveProcessTransaction = 0;
  private Integer _totalIncompleteDocTrans = 0; //those documents that are not associated with the process. EG. doc received in HTTP BC, but it is not able to be delivered to GT
  private Integer _totalIncompleteProcess = 0;  //those process without the process start time. (if the actual process instance is not get populated)
  
  public ArchiveHandler() 
  {
    _logger = getLogger();
  }
  
  public ArchiveHandler(Hashtable criteria, String archiveID, String jobID) 
  {
    this();
    _archiveCriteria = criteria;
    _archiveID = archiveID;
    _jobID = jobID;
  }

  public void archiveTMGTRecord(Hashtable criteria, String archiveID) throws ArchiveTrailDataException
  {
    String mn = "archiveTMGTRecord";
    Boolean isArchiveOrphanRecord = (Boolean)criteria.get(IArchiveConstant.ARCHIVE_ORPHAN_RECORD);
    String groupList = (String)criteria.get(IArchiveConstant.GROUP_INFO);
    Exception error = null;
    boolean isArchiveSuccess = true;
    _archiveID = archiveID;
    
    triggerArchiveStartupAlert();
    
    try
    {
      delegateArchiveRequestToGT(criteria, archiveID);
      if(isArchiveOrphanRecord)
      {
        _logger.logMessage(mn, null, "Archive Orphan record");
        archiveOrphanTMGT(criteria, archiveID);
      }
      else if(groupList != null)
      {
        _logger.logMessage(mn, null, "Archive record with group");
        archiveTMGT(criteria, archiveID);
      }
      else
      {
        _logger.logMessage(mn, null, "Archive record with date range");
        
        List<String> groups = ArchiveActivityHelper.getArchiveMgr().getAllAvailableGroup();
        String groupsInStr = ArchiveActivityHelper.convertListToStr(groups, ";");
        _logger.logMessage("", null, "all groups is "+groupsInStr);
        criteria.put(IArchiveConstant.GROUP_INFO, groupsInStr);
        
        archiveTMGT(criteria, archiveID);
        archiveOrphanTMGT(criteria, archiveID);
      }
    }
    catch(ArchiveTrailDataException ex)
    {
      error = ex;
      isArchiveSuccess = false;
      throw ex;
    }
    catch(Exception ex)
    {
      error = ex;
      isArchiveSuccess = false;
      throw new ArchiveTrailDataException("Error in archiving TM, GT record "+ex.getMessage(), ex);
    }
    finally
    {
      triggerArchivelAlert(isArchiveSuccess, error);
    }
  }
  
  public void archiveTMGT(Hashtable criteria, String archiveID) throws ArchiveTrailDataException
  {
  	String mn = "archiveTMGT";
    String methodName = "archiveTMGT";
    _logger.logEntry(methodName, null);
    
    boolean isArchivedSuccess = true;
    Exception error = null;
    try
    {
      _logger.logMessage(mn, null, "Archive TM GT record");
      archive(criteria);
    }
    catch(ArchiveTrailDataException ex)
    {
      isArchivedSuccess = false;
      error = ex;
      throw ex;
    }
    catch(Exception ex)
    {
      isArchivedSuccess = false;
      error = ex;
      _logger.logWarn(mn, null, "Error delegating to GT OR archiving: "+ex.getMessage(), ex);
      throw new ArchiveTrailDataException("Error in archiving GT, TM: "+ex.getMessage(), ex);
    }
    /*
    finally
    {
      //send email alert
      triggerArchivelAlert(isArchivedSuccess, error);
      _logger.logExit(methodName, null);
    }*/
  }
  
  /**
   * Archive the record in TM and GT that is not belong to any group
   * @param criteria
   * @param archiveID
   */
  public void archiveOrphanTMGT(Hashtable criteria, String archiveID) throws ArchiveTrailDataException
  {
    String mn = "archiveOrphanTMGT";
    boolean isArchiveSuccess = true;
    Exception error = null;
    try
    {
      _logger.logMessage(mn, null, "Archive Orphan TM GT record");
      //triggerArchiveStartupAlert();
      //delegateArchiveRequestToGT(criteria, archiveID);
      archiveOrphanRecord(criteria);
    }
    catch(Exception ex)
    {
      isArchiveSuccess = false;
      error = ex;
      throw new ArchiveTrailDataException("Error in archiving Orphan record GT, TM: "+ex.getMessage(), ex);
    }
    /*
    finally
    {
      triggerArchivelAlert(isArchiveSuccess, error);
    }*/
  }
  
  private void archive(Hashtable criteria) throws ArchiveTrailDataException
  {
    String methodName = "archive";
    
    try
    {
      long startTime = System.currentTimeMillis();
      _logger.logMessage(methodName, null, "Archive criteria is "+criteria.toString());
      
      Long fromStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
      Long toStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
      if(fromStartDateTime == null || toStartDateTime == null)
      {
        throw new ArchiveTrailDataException("fromStartDateTime or toStartDateTime can't be null");
      }
      
      IAuditTrailArchiveManagerLocalObj archiveMgr = getArchiveMgr();
      initArchiveFolderPath();
      _totalEntriesInZip = archiveMgr.getTotalProcessTransEntryInArchiveZip();
      
      try
      {
        initArchiveHelper();
        archiveIncompleteProcessTrans(criteria);
        archiveProcessTransaction(criteria);
        archiveTraceEvent(criteria);
      }
      catch(Exception ex)
      {
        throw ex;
      }
      finally
      {
        _archiveHelper.closeArchiveZip();
      }
      
      long totalTime = System.currentTimeMillis() - startTime;
      _logger.logMessage(methodName, null, "End time is "+new Date(totalTime)); 
    }
    catch(ArchiveTrailDataException ex)
    {
      throw ex;
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("["+CLASS_NAME+".archive] Error in archiving the Transaction Monitoring entity given criteria "+criteria, ex);
    }
  }
  
  private void triggerArchiveStartupAlert()
  {
    Long fromStartDate = (Long)_archiveCriteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME); 
    Long toStartDate = (Long)_archiveCriteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
    String customerList = (String)_archiveCriteria.get(IArchiveConstant.GROUP_INFO);
    Boolean isArchiveOrphanRecord = (Boolean)_archiveCriteria.get(IArchiveConstant.ARCHIVE_ORPHAN_RECORD);
    
    
    String archiveType = IArchiveConstant.ISAT_ARCHIVE_STARTED;
    ArrayList substitueList = new ArrayList();
    substitueList.add(ArchiveActivityHelper.formatDateToString(new Date(), ArchiveActivityHelper.getDatePattern())); //started at time....
    
    String fromStartDateInStr = fromStartDate != null? ArchiveActivityHelper.formatDateToString(new Date(fromStartDate), ArchiveActivityHelper.getDatePattern()) : ""; //criteria FromStart Time
    substitueList.add(fromStartDateInStr);
    
    String toStartDateInStr = toStartDate != null ? ArchiveActivityHelper.formatDateToString(new Date(toStartDate), ArchiveActivityHelper.getDatePattern()) : ""; //criteria ToStart Time
    substitueList.add(toStartDateInStr);
    
    String isArchiveOrphanRecordInStr = isArchiveOrphanRecord == null ? Boolean.FALSE.toString(): isArchiveOrphanRecord.toString();
    substitueList.add(isArchiveOrphanRecordInStr);
    
    String customerListInStr = customerList == null ? "" : customerList;
    substitueList.add(customerListInStr);
    
    String archiveID = _archiveID == null? "" : _archiveID;
    substitueList.add(archiveID);
    
    AlertUtil.getInstance().sendAlert(archiveType, substitueList.toArray());
  }
  
  private void triggerArchivelAlert(boolean isArchiveSuccess, Exception ex)
  {
    _logger.logMessage("", null, "Triggering alert ..... isArchiveSuccess "+isArchiveSuccess);
    Long fromStartDate = (Long)_archiveCriteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME); 
    Long toStartDate = (Long)_archiveCriteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
    String customerList = (String)_archiveCriteria.get(IArchiveConstant.GROUP_INFO);
    Boolean isArchiveOrphanRecord = (Boolean)_archiveCriteria.get(IArchiveConstant.ARCHIVE_ORPHAN_RECORD);
    
    String archiveType = IArchiveConstant.ISAT_ARCHIVE_COMPLETED;
    ArrayList substitueList = new ArrayList();
    substitueList.add(ArchiveActivityHelper.formatDateToString(new Date(), ArchiveActivityHelper.getDatePattern())); //finshed at time....
    
    String fromStartDateInStr = fromStartDate != null? ArchiveActivityHelper.formatDateToString(new Date(fromStartDate), ArchiveActivityHelper.getDatePattern()) : ""; //criteria FromStart Time
    substitueList.add(fromStartDateInStr);
    
    String toStartDateInStr = toStartDate != null ? ArchiveActivityHelper.formatDateToString(new Date(toStartDate), ArchiveActivityHelper.getDatePattern()) : ""; //criteria ToStart Time
    substitueList.add(toStartDateInStr);
    
    String isArchiveOrphanRecordInStr = isArchiveOrphanRecord == null ? Boolean.FALSE.toString(): isArchiveOrphanRecord.toString();
    substitueList.add(isArchiveOrphanRecordInStr);
    
    String customerListInStr = customerList == null ? "" : customerList;
    substitueList.add(customerListInStr);
    
    int totalArchivePI = _totalArchiveProcessTransaction != null ? _totalArchiveProcessTransaction : 0; //Total Process Trans archive
    substitueList.add(new Integer(totalArchivePI));
    
    int totalIncompletePI = _totalIncompleteProcess != null ? _totalIncompleteProcess : 0;
    substitueList.add(new Integer(totalIncompletePI));
    
    int totalArchiveDocumentRecord = _totalIncompleteDocTrans != null ? _totalIncompleteDocTrans: 0; //Total orpahn record archive
      substitueList.add(new Integer(totalArchiveDocumentRecord));
    
    String archiveID = _archiveID == null? "" : _archiveID;
    substitueList.add(archiveID);  
      
    substitueList.add(_archiveID+".xml");  
      
    if(! isArchiveSuccess)
    {
      substitueList.add(ex.getMessage()); //Exception msg
      substitueList.add(ArchiveActivityHelper.getStackTrace(ex)); //Stack trace
      archiveType = IArchiveConstant.ISAT_ARCHIVE_FAILED;
    }
    
    
    AlertUtil.getInstance().sendAlert(archiveType, substitueList.toArray());
  }
  
  /**
   * Archive the process transaction record and its correspond document trans, trace event header, trace event, biz document.
   * @param criteria
   * @throws Exception
   */
  private void archiveProcessTransaction(Hashtable criteria) throws Exception
  {
    String methodName = "archiveProcessTransaction";
    IAuditTrailArchiveManagerLocalObj archiveMgr = getArchiveMgr();
    Collection<Long> processInstanceUIDs = archiveMgr.retrieveProcessTransactionUID(criteria);
    //Collection<Long> leftOverProcessInstanceUIDs = archiveMgr.getOrphanProcessInstanceUIDWithGroupInfo(criteria); //handling the exception case where the process transaction record without the process start time
    
    if(processInstanceUIDs != null && processInstanceUIDs.size() > 0)
    {
      try
      {
        _logger.logMessage(methodName, null, "Retrieve Process Instance UID size is "+processInstanceUIDs.size());
        Iterator<Long> ite = processInstanceUIDs.iterator();
        while(ite.hasNext())
        {
          Long processInstanceUID = ite.next();
          _archiveHelper.incrementTotalInZip();
          archiveMgr.archiveProcessTransaction(processInstanceUID, _archiveHelper);
          ++_totalArchiveProcessTransaction;
        }
      }
      catch(Exception ex)
      {
        throw ex;
      }
    }
  }
  
  /**
   * Archive those process transactions record without the process start time( this may be due to some error has occured during the update
   * of the process time). We based on the smallest process instance uid given the criteria, and archive those process which process
   * instance uid less than the smallest instance uid and its start time is null.
   * @param criteria
   * @throws Exception
   */
  private void archiveIncompleteProcessTrans(Hashtable criteria) throws Exception
  {
    String mn = "archiveIncompleteProcessTrans";
    IAuditTrailArchiveManagerLocalObj archiveMgr = getArchiveMgr();
    Collection<Long> incompleteProcessInstanceUIDs = archiveMgr.getOrphanProcessInstanceUIDWithGroupInfo(criteria); //handling the exception case where the process transaction record without the process start time
    if(incompleteProcessInstanceUIDs != null && incompleteProcessInstanceUIDs.size() > 0)
    {
      for(Iterator<Long> i = incompleteProcessInstanceUIDs.iterator(); i.hasNext(); )
      {
        Long processInstanceUID = i.next();
        _archiveHelper.incrementTotalInZip();
        archiveMgr.archiveProcessTransaction(processInstanceUID, _archiveHelper);
        _totalIncompleteProcess++;
      }
    }
  }
  
  /**
   * Archive the trace event info that are not associated to any Document Transaction record, and the trace event info
   * is tagged with the group info. (For example, the document imported into the HTTPBC can't transfer to GT. Those event
   * will contain the group info. )
   * @param criteria
   */
  private void archiveTraceEvent(Hashtable criteria) throws Exception
  {
    String mn = "archiveTraceEvent";
    
    IAuditTrailArchiveManagerLocalObj archiveMgr = getArchiveMgr();
    Collection<String> tracingIDsWithGroup = archiveMgr.retrieveTraceEventInfoTracingIDWithGroup(criteria);
    if(tracingIDsWithGroup != null && tracingIDsWithGroup.size() > 0)
    {
      try
      {
        for(Iterator<String> i = tracingIDsWithGroup.iterator(); i.hasNext(); )
        {
          String tracingID = i.next();
          
          //checkCreateNewArchiveZip(_totalSetInZipSoFar);
          if(! archiveMgr.isDependentOnDocument(tracingID))
          {
            _archiveHelper.incrementTotalInZip();
            _logger.logMessage(mn, null, "TracingID with group info is "+tracingID);
            archiveMgr.archiveTraceEventInfoGroup(tracingID, _archiveHelper);
            ++_totalIncompleteDocTrans;
          }
        }
      }
      catch(Exception ex)
      {
        throw ex;
      }
    }
    else
    {
      _logger.logMessage(mn, null, "no incomplete trace event info can be found");
    }
  }
  
  private void initArchiveHelper()
  {
    String[] archiveFilename = null;
    if(_archiveHelper != null)//handle the archive by date range case. The archive by group and archive orphan record will share the same summary file
    {
       archiveFilename = _archiveHelper.getArchiveFilename();
       for(int i = 0; i < archiveFilename.length; i++)
       {
         _logger.logMessage("initArchiveHelper", null, "archive filename "+archiveFilename[i]);
       }
    }
    
    _archiveHelper = new ArchiveHelper();    
    _archiveHelper.addArchiveFilename(archiveFilename);
    
    archiveFilename = _archiveHelper.getArchiveFilename();
    for(int i = 0; i < archiveFilename.length; i++)
    {
      _logger.logMessage("after insert initArchiveHelper", null, "archive filename "+archiveFilename[i]);
    }
    
    _archiveHelper.setMaxAllowedSetInZipFile(_totalEntriesInZip);
    _archiveHelper.setArchiveDir(_archiveFolderPath + _archiveID);
    
  }
  
  /**
   * Handle the record without the group info.
   */
  private void archiveOrphanRecord(Hashtable criteria) throws Exception
  {
    String methodName = "archiveOrphanRecord";
    _logger.logMessage(methodName, null, "archiving orphan record .... with criteria "+criteria);
    
    initArchiveFolderPath();
    IAuditTrailArchiveManagerLocalObj archiveMgr = getArchiveMgr();
    _totalEntriesInZip = archiveMgr.getTotalOrphanTraceEventInfoInArchiveZip();
    
    try
    {
      initArchiveHelper();
      
      //Orphan process transaction
      Collection<Long> processTransUIDs = archiveMgr.getOrphanProcessInstanceUIDWithoutGroupInfo(criteria);
      archiveOrphanProcessTransaction(processTransUIDs, archiveMgr);
    
      //Orphan trace event info
      Collection<String> traceEventInfoTracingIDs = archiveMgr.retrieveOrphanTraceEventInfoTracingID(criteria);
      archiveOrphanTraceEventInfo(traceEventInfoTracingIDs, archiveMgr);
    }
    catch(Exception ex)
    {
      throw ex;
    }
    finally
    {
      _archiveHelper.closeArchiveZip();
    }
  }
  
  private void archiveOrphanTraceEventInfo(Collection<String> traceEventInfoTracingIDs, IAuditTrailArchiveManagerLocalObj archiveMgr) throws Exception
  {
    String methodName = "archiveOrphanTraceEventInfo";
    
    _logger.logMessage(methodName, null, "archive orphan event info ... size is "+(traceEventInfoTracingIDs != null? traceEventInfoTracingIDs.size()+"" : ""));
    if(traceEventInfoTracingIDs != null && traceEventInfoTracingIDs.size() > 0)
    {
      _archiveHelper.setArchiveZipCategory(IArchiveConstant.CATEGORY_ORPHAN_RECORD);
      
      try
      {
        int archiveSoFar = 0;
        Iterator<String> ite = traceEventInfoTracingIDs.iterator();
        while(ite.hasNext())
        {
          String tracingID = ite.next();
          if(! archiveMgr.isDependentOnDocument(tracingID))
          {
            _logger.logMessage(methodName, null, "Orphan trace event info tracing ID is "+tracingID);
            archiveMgr.archiveTraceEventInfoGroup(tracingID, _archiveHelper);
          
            ++_totalIncompleteDocTrans;
          }
        }
      }
      catch(Exception ex)
      {
        throw ex;
      }
    }
    else
    {
        _logger.logMessage(methodName, null, "No orphan trace event record can be found");
    }
  }
  
  private void archiveOrphanProcessTransaction(Collection<Long> processTransactionUIDs, IAuditTrailArchiveManagerLocalObj archiveMgr) throws Exception
  {
    String methodName = "archiveOrphanProcessTransaction";
    _logger.logMessage(methodName, null, "archive orphan process transaction size is "+(processTransactionUIDs != null? processTransactionUIDs.size()+"" : ""));
    
    if(processTransactionUIDs != null && processTransactionUIDs.size() > 0)
    {
      _archiveHelper.setArchiveZipCategory(IArchiveConstant.CATEGORY_ORPHAN_RECORD);
      int archiveSoFar = 0;
      
      try
      {
        for(Iterator<Long> i = processTransactionUIDs.iterator(); i.hasNext(); )
        {
          _archiveHelper.incrementTotalInZip();
          archiveMgr.archiveProcessTransaction(i.next(), _archiveHelper);
          ++_totalArchiveProcessTransaction;
        }
      }
      catch(Exception ex)
      {
        throw ex;
      }
    }
    else
    {
      _logger.logMessage(methodName, null, "No orphan process transaction record can be found");
    }
  }
  
  //TEST
  private void archiveOrphanProcess(Hashtable criteria) throws Exception
  {
    String mn = "archiveOrphanProcess";
    IAuditTrailArchiveManagerLocalObj archiveMgr = getArchiveMgr();
    
    Collection<Long> orphanProcessWithGroup = archiveMgr.getOrphanProcessInstanceUIDWithGroupInfo(criteria);
    for(Iterator i = orphanProcessWithGroup.iterator(); orphanProcessWithGroup!= null && orphanProcessWithGroup.size() > 0 && i.hasNext(); )
    {
      _logger.logMessage(mn, null, "Process with group "+i.next());
    }
    
    Collection<Long> orphanProcessWithoutGroupInfo = archiveMgr.getOrphanProcessInstanceUIDWithoutGroupInfo(criteria);
    for(Iterator i = orphanProcessWithoutGroupInfo.iterator(); orphanProcessWithoutGroupInfo!= null && orphanProcessWithoutGroupInfo.size() > 0 && i.hasNext(); )
    {
      _logger.logMessage(mn, null, "Process without group "+i.next());
    }
  }
  
  private String getZipFilename()
  {
    return IArchiveConstant.ARCHIVE_FILENAME_PREFIX+"-"+UUID.randomUUID().toString()+".zip";
  }
  
  //TODO We implement Queue load balancing deliver msg mechanism, so each node should group the
  //archive zip under the same folder !!! The share folder should be propage in the jms msg
  private  String getGroupForArchiveZip()
  {
    String outputPattern = "yyyyMMdd_hhmmssSSS";
    
    Calendar c = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat(outputPattern);
    return formatter.format(c.getTime());
  }
  
  /**
   * Generate a archive summary file
   * @return
   * @throws Exception
   */
  public File generateArchiveSummaryInfo() throws Exception
  {
    if(_archiveID == null || "".equals(_archiveID))
    {
      generateArchiveID();
    }

    initArchiveFolderPath();
    File summaryFile = new File(_archiveFolderPath + _archiveID+".xml");
    if(summaryFile.isDirectory())
    {
      throw new ArchiveTrailDataException("The summary file path "+summaryFile.getAbsolutePath()+" is a dir !");
    }
    ArchiveActivityHelper.generateArchiveSummaryFile(getArchiveSummary(), summaryFile);
    
    _logger.logMessage("generateArchiveSummaryInfo", null, "Generated ! "+summaryFile.getAbsolutePath());
    return summaryFile; 
  }
  
  /**
   * Generate the sub archive task properties which keep track the archive status
   * in TM, GT (archive by PI, archive by doc)
   * @param jobID
   * @throws Exception
   */
  public void generateArchiveSubJobProperties(String jobID) throws Exception
  {
    String defValue = Boolean.FALSE.toString();
    Properties subJobProperties = new Properties();
    subJobProperties.setProperty(IArchiveConstant.ARCHIVE_BY_PI, defValue);
    subJobProperties.setProperty(IArchiveConstant.ARCHIVE_BY_DOCUMENT, defValue);
    subJobProperties.setProperty(IArchiveConstant.ARCHIVE_BY_TM, defValue);
    
    _archiveFolderPath = ArchiveActivityHelper.getArchiveMgr().getArchiveFolderPath();
    initArchiveFolderPath();
    
    ArchiveActivityHelper.generatePropertiesFile(_archiveFolderPath, subJobProperties, jobID);
  }
  
  private void delegateArchiveRequestToGT(Hashtable criteria, String archiveID) throws ArchiveTrailDataException
  {
    try
    {
      getArchiveMgr().delegateArchiveRequestToGT(criteria, archiveID);
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Error in delegating archive request to GT given criteria: "+criteria+" archiveID: "+archiveID, ex);
    }
  }
  
  /**
   * Archive in both GT and TM has been performed(not guaranteed the status is success). We update the archive info
   * into the summary file.
   * @return
   * @throws Exception
   */
  public void updateTMArchiveStatus(boolean isTMArchivedSuccess) throws Exception
  {
    String methodName = "updateTMArchiveStatus";
    initArchiveFolderPath();
    
    String[] zipFileList = _archiveHelper == null ? null : _archiveHelper.getArchiveFilename();
    getArchiveMgr().updateTMArchiveStatus(_archiveID, _jobID, isTMArchivedSuccess, zipFileList, _totalArchiveProcessTransaction, _totalIncompleteDocTrans, _totalIncompleteProcess);
  }
  
  private void initArchiveFolderPath() throws Exception
  {
    if(_archiveFolderPath == null || "".equals(_archiveFolderPath))
    {
      IAuditTrailArchiveManagerLocalObj archiveMgr = ArchiveActivityHelper.getArchiveMgr();
      _archiveFolderPath = archiveMgr.getArchiveFolderPath();
    }
    
    if(_archiveFolderPath != null && ! "".equals(_archiveFolderPath))
    {
      File archiveFolder = new File(_archiveFolderPath);
      if(! archiveFolder.exists())
      {
        boolean isCreatedSuccess = archiveFolder.mkdirs();
        if(! isCreatedSuccess)
        {
          throw new ArchiveTrailDataException("The archive folder dir is failed to be created.");
        }
      }
    }
  }
  
  //TODO the actual creating of summary should be done in the coordinator !!!
  /**
   * Get the TM archive summary
   */
  private ArchiveSummary getArchiveSummary()
  {
    ArchiveSummary summary = new ArchiveSummary();
    summary.setArchiveDescription(getArchiveDescription());
    summary.setArchiveName(getArchiveName());
    summary.setGroup(_archiveID);
    summary.setArchiveID(_archiveID);
    
    String[] zipFileList = _archiveHelper == null ? null : _archiveHelper.getArchiveFilename();
    summary.setZipFileList(ArchiveActivityHelper.convertArrToStr(zipFileList, ","));
    
    summary.setTotalArchiveProcessTransaction(_totalArchiveProcessTransaction);
    summary.setTotalIncompleteDocument(_totalIncompleteDocTrans);
    summary.setTotalIncompleteProcessTrans(_totalIncompleteProcess);
    
    if(_archiveCriteria != null && _archiveCriteria.size() > 0)
    {
      Long fromStartDateTime = (Long)_archiveCriteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
      Long toStartDateTime = (Long)_archiveCriteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
      String archiveName = (String)_archiveCriteria.get(IArchiveConstant.ARCHIVE_NAME);
      String archiveDesc = (String)_archiveCriteria.get(IArchiveConstant.ARCHIVE_DESCRIPTION);
      String customerList = (String)_archiveCriteria.get(IArchiveConstant.GROUP_INFO);
      Boolean isArchiveOrphanRecord = (Boolean)_archiveCriteria.get(IArchiveConstant.ARCHIVE_ORPHAN_RECORD);
      
      ArchiveCriteria criteria = new ArchiveCriteria();
      criteria.setFromStartDate(new Date(fromStartDateTime));
      criteria.setToStartDate(new Date(toStartDateTime));
      criteria.setGroup(customerList);
      criteria.setArchiveOrphanRecord(isArchiveOrphanRecord);
      
      summary.setArchiveCriteria(criteria);
      summary.setArchiveName(archiveName);
      summary.setArchiveDescription(archiveDesc);
    }
    
    return summary;
  }
  
  private IAuditTrailArchiveManagerLocalObj getArchiveMgr() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditTrailArchiveManagerLocalHome archiveHome = (IAuditTrailArchiveManagerLocalHome)finder.lookup(IAuditTrailArchiveManagerLocalHome.class.getName(), 
                                                                             IAuditTrailArchiveManagerLocalHome.class);
    return archiveHome.create();
  }
  
  public String getArchiveDescription()
  {
    return _archiveDescription;
  }

  public void setArchiveDescription(String description)
  {
    _archiveDescription = description;
  }

  public String getArchiveName()
  {
    return _archiveName;
  }

  public void setArchiveName(String name)
  {
    _archiveName = name;
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
    getArchiveMgr().updateGTArchiveStatus(archiveType, isArchiveSuccess, gtSummaryFilename, archiveID);
  }

  public String generateArchiveID() throws ArchiveTrailDataException
  {
  	String mn = "generateArchiveID";
    if(_archiveCriteria == null || _archiveCriteria.size() == 0)
    {
    	_logger.logWarn(mn, null, "Archive criteria is empty, pls use other constructor to init ArchiveHandler instance !", null);
      throw new ArchiveTrailDataException("No archive criterion found");
    }
    
    //String archiveName = (String)_archiveCriteria.get(IGTArchiveConstant.ARCHIVE_NAME);
    
    _archiveID = getGroupForArchiveZip();
    return _archiveID;
  }
  
  public String getArchiveID()
  {
    return _archiveID;
  }

  public void setArchiveID(String _archiveid)
  {
    _archiveID = _archiveid;
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
    getArchiveMgr().handleGTArchiveStatus(archiveStatusHT);
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
    getArchiveMgr().updateJobStatus(archiveType, jobID, isArchiveSuccess, archiveID, isArchiveOrphan);
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  public static void main(String[] args) throws Exception
  {
    ArchiveZipFile zipFile = new ArchiveZipFile("test");
    ArchiveHelper helper = new ArchiveHelper(zipFile);
    System.out.println("Archive helper "+helper.toString());
    
    ArchiveHandler handler = new ArchiveHandler();
    handler.archiveOrphanProcessTransaction(null, null);
  }
}
