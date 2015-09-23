/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-12-17     Ong Eu Soon         Created      
 * 2009-03-30     Tam Wei Xiang       #122 - Added ARchive_older_than, is_archive_frequency_once,
 *                                           client_tz                            
 */

package com.gridnode.gtas.client.web.archive;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ArchiveAForm extends GTActionFormBase
{

//Archive Document entity fields
  private String _archiveId;
  private String _archiveName;
  private String _archiveDescription;
  private String _fromStartDate;
  private String _fromStartTime;
  private String _toStartDate;
  private String _toStartTime;
  private String _archiveType;
  private String _enableRestoreArchived;
  private String _enableSearchArchived;
  
  private String[] _partner = new String[]{};
  private String[] _processDef;
  private String _includeIncomplete;

  private String[] _folder;
  private String[] _docType;
  
  private String _archiveRecordOlderThan;
  private String _isArchivedFrequencyOnce;
  
  private boolean _isNewEntity;
  
  public ArchiveAForm()
  {

  }
  
  public void setIsNewEntity(boolean flag)
  { 
    _isNewEntity = flag; 
  }

  public boolean isNewEntity()
  { 
    return  _isNewEntity; 
  }

  
  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    //if(_isNewEntity)
    //{
      _isArchivedFrequencyOnce = "false";
      System.out.println("ArchiveAform: setArchivedFrequenceOnce false");
    //}
    
    _processDef = null;

    _folder = null;
    _docType = null;

    _enableRestoreArchived = null;
    _enableSearchArchived = null;
    
    _partner = new String[]{};
    
    //_isArchivedFrequencyOnce = "true";
  }
  public String getArchiveName()
  {
    return _archiveName;
  }

  public void setArchiveName(String name)
  {
    _archiveName = name;
  }

  public String getArchiveDescription()
  {
    return _archiveDescription;
  }

  public void setArchiveDescription(String description)
  {
    _archiveDescription = description;
  }

  public String getFromStartDate()
  {
    return _fromStartDate;
  }

  public void setFromStartDate(String fromDate)
  {
    _fromStartDate = fromDate;
  }

  public String getFromStartTime()
  {
    return _fromStartTime;
  }

  public void setFromStartTime(String fromTime)
  {
    _fromStartTime = fromTime;
  }

  public String getToStartDate()
  {
    return _toStartDate;
  }

  public void setToStartDate(String toDate)
  {
    _toStartDate = toDate;
  }

  public String getToStartTime()
  {
    return _toStartTime;
  }

  public void setToStartTime(String toTime)
  {
    _toStartTime = toTime;
  }

  public String getArchiveType()
  {
    return _archiveType;
  }

  public void setArchiveType(String archiveType)
  {
    _archiveType = archiveType;
  }

  public String[] getProcessDefNameList()
  {
    return _processDef;
  }

  public void setProcessDefNameList(String[] processDef)
  {
    _processDef = processDef;
  }

  public String getIsIncludeIncomplete()
  {
    return _includeIncomplete;
  }

  public void setIsIncludeIncomplete(String includeIncomplete)
  {
    _includeIncomplete = includeIncomplete;
  }

  public String[] getFolderList()
  {
    return _folder;
  }

  public void setFolderList(String[] folder)
  {
    _folder = folder;
  }

  public String[] getDocumentTypeList()
  {
    return _docType;
  }

  public void setDocumentTypeList(String[] docType)
  {
    _docType = docType;
  }

  public String getIsEnableRestoreArchived()
  {
    return _enableRestoreArchived;
  }

  public void setIsEnableRestoreArchived(String restoreArchived)
  {
    _enableRestoreArchived = restoreArchived;
  }

  public String getIsEnableSearchArchived()
  {
    return _enableSearchArchived;
  }

  public void setIsEnableSearchArchived(String searchArchived)
  {
    _enableSearchArchived = searchArchived;
  }

  public String[] getPartnerIDForArchive()
  {
    return _partner;
  }
  
  public void setPartnerIDForArchive(String[] _partnerid)
  {
    _partner = _partnerid;
  }
  public void setArchiveID(String value)
  {
    _archiveId = value;
  }
  
  public String getArchiveID()
  {
    return _archiveId;
  }

  public String getArchiveRecordOlderThan()
  {
    return _archiveRecordOlderThan;
  }

  public void setArchiveRecordOlderThan(String olderThan)
  {
    _archiveRecordOlderThan = olderThan;
  }

  public String getisArchiveFrequencyOnce()
  {
    return _isArchivedFrequencyOnce;
  }

  public void setIsArchiveFrequencyOnce(String archivedFrequencyOnce)
  {
    _isArchivedFrequencyOnce = archivedFrequencyOnce;
  }

  
  
}