/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: UpdateArchiveEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 11, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.events.dbarchive;

import java.util.List;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class UpdateArchiveEvent extends EventSupport
{

  /**
   * 
   */
  private static final long serialVersionUID = 5397303615008824950L;
  public static final String ARCHIVE_TYPE = "Archive Type"; 
  public static final String ARCHIVE_NAME = "Archive Name";
  public static final String ARCHIVE_DESCRIPTION = "Archive Description";
  public static final String FROM_START_DATE = "From Start Date";
  public static final String TO_START_DATE = "To Start Date";
  public static final String FROM_START_TIME = "From Start Time";
  public static final String TO_START_TIME = "To Start Time";
  public static final String PROCESS_DEF_NAME_LIST = "Process Definition Name List";
  public static final String INCLUDE_INCOMPLETE_PROCESSES = "Include Incomplete Processes";
  public static final String FOLDER_LIST = "Folder List";
  public static final String DOCUMENT_TYPE_LIST = "Document Type List";
  public static final String ENABLE_SEARCH_ARCHIVED =  "Enable Search Archived";
  public static final String ENABLE_RESTORE_ARCHIVED =  "Enable Restore Archived";
  public static final String PARTNER_ID_FOR_ARCHIVE =  "Partner ID For Archive";
  public static final String ARCHIVE_ID = "Archive ID";
  public static final String CUSTOMER_BE_ID_FOR_ARCHIVE = "Customer BE ID";
  public static final String ARCHIVE_ORPHAN_RECORD = "Archive Orphan Record";
  public static final String ARCHIVE_OLDER_THAN = "Archive Older Than";
  public static final String IS_ARCHIVE_FREQUENCY_ONCE = "is archive frequency once";
  public static final String CLIENT_TZ = "Client Login Timezone";
  public static final String UID = "UID";
  
  public UpdateArchiveEvent()
   throws EventException
  {
  }

  public void setArchiveType(String archiveType)
    throws EventException
  {
    checkSetString(ARCHIVE_TYPE, archiveType);
  }

  public void setArchiveName(String archiveName)
    throws EventException
  {
    checkSetString(ARCHIVE_NAME, archiveName);
  }

  public void setArchiveDescription(String archiveDescription)
    throws EventException
  {
    checkSetString(ARCHIVE_DESCRIPTION, archiveDescription);
  }

  public void setFromStartDate(String fromStartDate)
    throws EventException
  {
    setEventData(FROM_START_DATE, fromStartDate);
  }

  public void setToStartDate(String toStartDate)
    throws EventException
  {
    setEventData(TO_START_DATE, toStartDate);
  }
  public void setFromStartTime(String fromStartTime)
  throws EventException
  {
    setEventData(FROM_START_TIME, fromStartTime);
  }
  
  public void setToStartTime(String toStartTime)
    throws EventException
  {
    setEventData(TO_START_TIME, toStartTime);
  }
  public void setProcessDefNameList(List processDefNameList)
     throws EventException
  {
    setEventData(PROCESS_DEF_NAME_LIST, processDefNameList);
  }
  
  public void setIncludeIncompleteProcesses(Boolean includeIncompleteProcesses)
     throws EventException
  {
    setEventData(INCLUDE_INCOMPLETE_PROCESSES, includeIncompleteProcesses);
  }

  public void setFolderList(List folderList)
    throws EventException
  {
    setEventData(FOLDER_LIST, folderList);
  }

  public void setDocumentTypeList(List documentTypeList)
     throws EventException
  {
    setEventData(DOCUMENT_TYPE_LIST, documentTypeList);
  }
  
  public void setEnableSearchArchived(Boolean isEnabledSearchArchived)
  {
    setEventData(ENABLE_SEARCH_ARCHIVED, isEnabledSearchArchived);
  }
  
  public void setEnableRestoreArchived(Boolean isEnableRestoreArchived)
  {
    setEventData(ENABLE_RESTORE_ARCHIVED, isEnableRestoreArchived);
  }
  
  public void setPartnerIDForArchive(List partnerIDForArchive)
  {
    setEventData(PARTNER_ID_FOR_ARCHIVE, partnerIDForArchive);
  }
  
  public String getArchiveType()
  {
    return (String)getEventData(ARCHIVE_TYPE);
  }

  public String getArchiveName()
  {
    return (String)getEventData(ARCHIVE_NAME);
  }

  public String getArchiveDescription()
  {
    return (String)getEventData(ARCHIVE_DESCRIPTION);
  }
  public String getFromStartDate()
  {
    return (String)getEventData(FROM_START_DATE);
  }

  public String getToStartDate()
  {
    return (String)getEventData(TO_START_DATE);
  }
  public String getFromStartTime()
  {
    return (String)getEventData(FROM_START_TIME);
  }

  public String getToStartTime()
  {
    return (String)getEventData(TO_START_TIME);
  }

  public List getProcessDefNameList()
  {
    return (List)getEventData(PROCESS_DEF_NAME_LIST);
  }
  
  public Boolean isIncludeIncompleteProcesses()
  {
    return (Boolean)getEventData(INCLUDE_INCOMPLETE_PROCESSES);
  }

  public List getFolderList()
  {
    return (List)getEventData(FOLDER_LIST);
  }

  public List getDocumentTypeList()
  {
    return (List)getEventData(DOCUMENT_TYPE_LIST);
  }
  
  public Boolean  isEnableSearchArchived()
  {
     return (Boolean)getEventData(ENABLE_SEARCH_ARCHIVED);
  }
  
  public Boolean  isEnableRestoreArchived()
  {
     return (Boolean)getEventData(ENABLE_RESTORE_ARCHIVED);
  }
  
  public List  getPartnerIDForArchive()
  {
     return (List)getEventData(PARTNER_ID_FOR_ARCHIVE);
  }
  
  public String getArchiveID()
  {
    return (String)getEventData(ARCHIVE_ID);
  }
  
  /**
   * It will be set only if we receive the archive request from Transaction monitoring.
   * @param archiveID the unique identifier that we can link up the archive summary file
   *                  in TM module.
   */
  public void setArchiveID(String archiveID) throws EventException
  {
    checkSetString(ARCHIVE_ID, archiveID);
  }

  public List getCustomerIDForArchive()
  {
    return (List)getEventData(CUSTOMER_BE_ID_FOR_ARCHIVE);
  }
  
  public void setCustomerIDForArchive(String customerID) throws EventException
  {
    checkSetString(CUSTOMER_BE_ID_FOR_ARCHIVE, customerID);
  }
  
  public Boolean isArchiveOrphanRecord()
  {
    return (Boolean)getEventData(ARCHIVE_ORPHAN_RECORD);
  }
  
  public void setArchiveOrphanRecord(Boolean isArchiveOrphanRecord)
  {
    setEventData(ARCHIVE_ORPHAN_RECORD, isArchiveOrphanRecord);
  }
  
  public Integer getArchiveOlderThan()
  {
    return (Integer)getEventData(ARCHIVE_OLDER_THAN);
  }
   
  public void setArchiveOlderThan(Integer archiveOlderThan) throws EventException
  {
    checkSetInteger(ARCHIVE_OLDER_THAN, archiveOlderThan);
  }
  
  public Boolean isArchiveFrequencyOnce()
  {
    return (Boolean)getEventData(IS_ARCHIVE_FREQUENCY_ONCE);
  }
  
  public void setArchiveFrequencyOnce(Boolean isFrequencyOnce)
  {
    setEventData(IS_ARCHIVE_FREQUENCY_ONCE, isFrequencyOnce);
  }
  
  public String getClientTZ()
  {
    return (String)getEventData(CLIENT_TZ);
  }
  
  public void setClientTZ(String clientTZ) throws EventException
  {
    checkSetString(CLIENT_TZ, clientTZ);
  }
  
  public void setUID(Long uid) throws EventException
  {
    checkSetLong(UID, uid);
  }
  
  public Long getUID()
  {
    return (Long)getEventData(UID);
  }
  
  
  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateArchiveEvent";
  }
}
