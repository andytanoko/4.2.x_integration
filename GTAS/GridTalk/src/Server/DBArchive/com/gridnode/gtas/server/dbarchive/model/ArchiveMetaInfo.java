/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 16, 2008   Ong Eu Soon         Created
 * Apr 11, 2009   Tam Wei Xiang       #122 - Remove ARCHIVE_NAME
 * 
 */
package com.gridnode.gtas.server.dbarchive.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is a value object for ArchiveMetaInfoBean.
 *
 * Ong Eu Soon
 * 
 * @version
 * @since
 */
public class ArchiveMetaInfo 
						 extends AbstractEntity implements IArchiveMetaInfo
						 
{
  /**
   * 
   */
  private static final long serialVersionUID = -3484660221166987986L;
  public static final String ARCHIVE_TYPE_DOCUMENT = "Document";
  public static final String ARCHIVE_TYPE_PROCESS_INSTANCE = "ProcessInstance";
  private String _archiveType;   //Archive Type
	private String _archiveDescription; //Archive Description
	private String _fromStartDate; //From Start Date
  private String _toStartDate; //To Start Date
	private String _fromStartTime; //From Start Time
	private String _toStartTime; //To Start Time
	private String _processDefNameList; //Process Definition Name List
	private Boolean _includeIncomplete; //Include Incomplete Processes
	private String _folderList; //Folder List
	private String _documentTypeList; //Document Type List
	private Boolean _enableSearchArchived; //Enable Search Archived
	private Boolean _enableRestoreArchived; //Enable Restore Archived
	private String _partnerIDList; //Partner ID For Archive
	private String _archiveID; //Archive ID
//	private String _customerList; //Customer BE ID
//	private Boolean _archiveOrphanRecord;  //Archive Orphan Record
  private String _clientTz;
  private Boolean _isArchiveFrequencyOnce = false;
  private Integer _archiveRecordOlderThan = null;
  
	/**
	 * Empty constructor is required since the underlying layer dun
	 * know the signature of the constructor.
	 *
	 */
	public ArchiveMetaInfo()
	{		
	}
	
	
	
  // **************** Methods from AbstractEntity *********************
	public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getArchiveID();
  }

  public Number getKeyId()
  {
    return UID;
  }



  public String getArchiveType()
  {
    return _archiveType;
  }



  public void setArchiveType(String type)
  {
    _archiveType = type;
  }

  public String getArchiveDescription()
  {
    return _archiveDescription;
  }



  public void setArchiveDescription(String description)
  {
    this._archiveDescription= description;
  }



  public String getFromStartDate()
  {
    return _fromStartDate;
  }



  public void setFromStartDate(String startDate)
  {
    _fromStartDate = startDate;
  }



  public String getToStartDate()
  {
    return _toStartDate;
  }



  public void setToStartDate(String startDate)
  {
    _toStartDate = startDate;
  }

  public String getFromStartTime()
  {
    return _fromStartTime;
  }



  public void setFromStartTime(String startTime)
  {
    _fromStartTime = startTime;
  }



  public String getToStartTime()
  {
    return _toStartTime;
  }



  public void setToStartTime(String startTime)
  {
    _toStartTime = startTime;
  }

  public String getProcessDefNameList()
  {
    return _processDefNameList;
  }



  public void setProcessDefNameList(String defNameList)
  {
    _processDefNameList = defNameList;
  }



  public Boolean isIncludeIncompleteProcesses()
  {
    return _includeIncomplete;
  }



  public void setIncludeIncompleteProcesses(Boolean incomplete)
  {
    _includeIncomplete = incomplete;
  }



  public String getFolderList()
  {
    return _folderList;
  }



  public void setFolderList(String list)
  {
    _folderList = list;
  }



  public String getDocumentTypeList()
  {
    return _documentTypeList;
  }



  public void setDocumentTypeList(String typeList)
  {
    _documentTypeList = typeList;
  }



  public Boolean isEnableSearchArchived()
  {
    return _enableSearchArchived;
  }



  public void setEnableSearchArchived(Boolean searchArchived)
  {
    _enableSearchArchived = searchArchived;
  }



  public Boolean isEnableRestoreArchived()
  {
    return _enableRestoreArchived;
  }



  public void setEnableRestoreArchived(Boolean restoreArchived)
  {
    _enableRestoreArchived = restoreArchived;
  }



  public String getPartnerIDForArchive()
  {
    return _partnerIDList;
  }



  public void setPartnerIDForArchive(String list)
  {
    _partnerIDList = list;
  }



  public String getArchiveID()
  {
    return _archiveID;
  }



  public void setArchiveID(String archiveid)
  {
    _archiveID = archiveid;
  }



  public String getClientTz()
  {
    return _clientTz;
  }



  public void setClientTz(String tz)
  {
    _clientTz = tz;
  }



  public Boolean isArchiveFrequencyOnce()
  {
    return _isArchiveFrequencyOnce;
  }



  public void setArchiveFrequencyOnce(Boolean archiveFrequencyOnce)
  {
    _isArchiveFrequencyOnce = archiveFrequencyOnce;
  }

  public Integer getArchiveRecordOlderThan()
  {
    return _archiveRecordOlderThan;
  }

  public void setArchiveRecordOlderThan(Integer recordOlderThan)
  {
    _archiveRecordOlderThan = recordOlderThan;
  }

//  public String getCustomerIDForArchive()
//  {
//    return _customerList;
//  }
//
//
//
//  public void setCustomerIDForArchive(String customeridList)
//  {
//    _customerList = customeridList;
//  }
//
//
//
//  public Boolean isArchiveOrphanRecord()
//  {
//    return _archiveOrphanRecord;
//  }
//
//
//
//  public void setArchiveOrphanRecord(Boolean orphanRecord)
//  {
//    _archiveOrphanRecord = orphanRecord;
//  }



  //***************** Getters and Setters for attributes ***********************

}
