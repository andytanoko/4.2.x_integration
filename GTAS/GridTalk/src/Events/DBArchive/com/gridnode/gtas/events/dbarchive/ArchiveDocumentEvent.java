
package com.gridnode.gtas.events.dbarchive;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
/**
 */
public class ArchiveDocumentEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4995700433591691378L;

	public static final String ARCHIVE_TYPE = "Archive Type"; 

  public static final String ARCHIVE_NAME = "Archive Name";
  public static final String ARCHIVE_DESCRIPTION = "Archive Description";
  public static final String FROM_START_TIME = "From Start Time";
  public static final String TO_START_TIME = "To Start Time";

  //for processinstances    
  public static final String PROCESS_DEF_NAME_LIST = "Process Definition Name List";
  public static final String INCLUDE_INCOMPLETE_PROCESSES = "Include Incomplete Processes";
  
  //for griddocuments  
  public static final String FOLDER_LIST = "Folder List";
	public static final String DOCUMENT_TYPE_LIST = "Document Type List";
	
	//TWX 14092006 new archive criteria
  public static final String ENABLE_SEARCH_ARCHIVED =  "Enable Search Archived";
  public static final String ENABLE_RESTORE_ARCHIVED =  "Enable Restore Archived";
  public static final String PARTNER_ID_FOR_ARCHIVE =  "Partner ID For Archive";
  
  //TWX 23022007 To link up the archive summary file in Transaction Monitoring.
  public static final String ARCHIVE_ID = "Archive ID";
  
  //TWX 18052007 archive by customer
  public static final String CUSTOMER_BE_ID_FOR_ARCHIVE = "Customer BE ID";
  public static final String ARCHIVE_ORPHAN_RECORD = "Archive Orphan Record";
  public static final String ARCHIVE_JOB_ID = "Archive Job ID";
  
  public ArchiveDocumentEvent()
   throws EventException
  {
  }

  public void setArchiveType(String archiveType)
    throws EventException
  {
    setEventData(ARCHIVE_TYPE, archiveType);
  }

  public void setArchiveName(String archiveName)
    throws EventException
  {
    setEventData(ARCHIVE_NAME, archiveName);
  }

  public void setArchiveDescription(String archiveDescription)
    throws EventException
  {
    setEventData(ARCHIVE_DESCRIPTION, archiveDescription);
  }

  public void setFromStartTime(Timestamp fromStartTime)
    throws EventException
  {
    setEventData(FROM_START_TIME, fromStartTime);
  }

  public void setToStartTime(Timestamp toStartTime)
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
	
	public void setPartnerIDForArchive(List<String> partnerIDForArchive)
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

  public Timestamp getFromStartTime()
  {
    return (Timestamp)getEventData(FROM_START_TIME);
  }

  public Timestamp getToStartTime()
  {
    return (Timestamp)getEventData(TO_START_TIME);
  }

  public List getProcessDefNameList()
  {
    return (List)getEventData(PROCESS_DEF_NAME_LIST);
  }
  
  public Boolean getIncludeIncompleteProcesses()
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
  
  public List<String>  getPartnerIDForArchive()
  {
  	 return (List<String>)getEventData(PARTNER_ID_FOR_ARCHIVE);
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
  public void setArchiveID(String archiveID)
  {
    setEventData(ARCHIVE_ID, archiveID);
  }
  
  public List<String> getCustomerIDForArchive()
  {
    return (List<String>)getEventData(CUSTOMER_BE_ID_FOR_ARCHIVE);
  }
  
  public void setCustomerIDForArchive(List<String> customerID)
  {
    setEventData(CUSTOMER_BE_ID_FOR_ARCHIVE, customerID);
  }
  
  public Boolean isArchiveOrphanRecord()
  {
    return (Boolean)getEventData(ARCHIVE_ORPHAN_RECORD);
  }
  
  public void setArchiveOrphanRecord(Boolean isArchiveOrphanRecord)
  {
    setEventData(ARCHIVE_ORPHAN_RECORD, isArchiveOrphanRecord);
  }
  
  public void setArchiveJobID(String archiveJobID)
  {
    setEventData(ARCHIVE_JOB_ID, archiveJobID);
  }
  
  public String getArchiveJobID()
  {
    return (String)getEventData(ARCHIVE_JOB_ID);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/ArchiveDocumentEvent";
  }
  
  /*
  public void setIsEstore(String isEstore)
  	throws EventException
  {
  	setEventData(IS_ESTORE, isEstore);
  }
  
  public String getIsEstore()
  {
    return (String) getEventData(IS_ESTORE);
  }*/
}