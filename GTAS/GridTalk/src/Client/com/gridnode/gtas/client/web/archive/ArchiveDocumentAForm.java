/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveDocumentAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-17     Andrew Hill         Created
 * 2002-09-22     Daniel D'Cotta      Commented out GDOC_FILTER, added re-designed 
 *                                    ProcessInstance and GridDocument options
 * 2006-09-14     Tam Wei Xiang       Added enableSearchArchived & enableRestoreArchived 
 *                                    option. Partner also included                                   
 */

package com.gridnode.gtas.client.web.archive;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ArchiveDocumentAForm extends GTActionFormBase
{
  //private DataFilterAForm _gdocFilter;
  //private String _archiveFile;
  //private String _archiveFilePath;

  private String _name;
  private String _description;
  private String _fromDate;
  private String _fromTime;
  private String _toDate;
  private String _toTime;
  private String _archiveType;
  private String _enableRestoreArchived;
  private String _enableSearchArchived;
  
  private String[] _partner = new String[]{};
  private String[] _processDef;
  private String _includeIncomplete;

  private String[] _folder;
  private String[] _docType;
  
  public ArchiveDocumentAForm()
  {
    //_gdocFilter = new DataFilterAForm();
  }
  
  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    //_gdocFilter.reset(mapping, request);
    
    _processDef = null;

    _folder = null;
    _docType = null;

    _enableRestoreArchived = null;
    _enableSearchArchived = null;
    
    _partner = new String[]{};
  }

  /*
  public DataFilterAForm getGdocFilter()
  {
    return _gdocFilter;
  }

  public void setGdocFilter(DataFilterAForm gdocFilter)
  {
    _gdocFilter = gdocFilter;
  }

  public String getArchiveFile()
  {
    return _archiveFile;
  }

  public String getArchiveFilePath()
  {
    return _archiveFilePath;
  }

  public void setArchiveFile(String archiveFile)
  {
    _archiveFile = archiveFile;
  }

  public void setArchiveFilePath(String archiveFilePath)
  {
    _archiveFilePath = archiveFilePath;
  }
  */

  public String getName()
  {
    return _name;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public String getDescription()
  {
    return _description;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public String getFromDate()
  {
    return _fromDate;
  }

  public void setFromDate(String fromDate)
  {
    _fromDate = fromDate;
  }

  public String getFromTime()
  {
    return _fromTime;
  }

  public void setFromTime(String fromTime)
  {
    _fromTime = fromTime;
  }

  public String getToDate()
  {
    return _toDate;
  }

  public void setToDate(String toDate)
  {
    _toDate = toDate;
  }

  public String getToTime()
  {
    return _toTime;
  }

  public void setToTime(String toTime)
  {
    _toTime = toTime;
  }

  public String getArchiveType()
  {
    return _archiveType;
  }

  public void setArchiveType(String archiveType)
  {
    _archiveType = archiveType;
  }

  public String[] getProcessDef()
  {
    return _processDef;
  }

  public void setProcessDef(String[] processDef)
  {
    _processDef = processDef;
  }

  public String getIncludeIncomplete()
  {
    return _includeIncomplete;
  }

  public void setIncludeIncomplete(String includeIncomplete)
  {
    _includeIncomplete = includeIncomplete;
  }

  public String[] getFolder()
  {
    return _folder;
  }

  public void setFolder(String[] folder)
  {
    _folder = folder;
  }

  public String[] getDocType()
  {
    return _docType;
  }

  public void setDocType(String[] docType)
  {
    _docType = docType;
  }

  public String getEnableRestoreArchived()
  {
    return _enableRestoreArchived;
  }

  public void setEnableRestoreArchived(String restoreArchived)
  {
		_enableRestoreArchived = restoreArchived;
  }

  public String getEnableSearchArchived()
  {
		return _enableSearchArchived;
  }

  public void setEnableSearchArchived(String searchArchived)
  {
		_enableSearchArchived = searchArchived;
  }

  public String[] getPartner()
  {
		return _partner;
  }
  
  public void setPartner(String[] _partnerid)
  {
		_partner = _partnerid;
  }
	
	
}