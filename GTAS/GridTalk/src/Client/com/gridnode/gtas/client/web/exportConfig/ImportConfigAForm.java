/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportConfigAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-06-02     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.exportConfig;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.gridnode.gtas.client.ctrl.IGTMheReference;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ImportConfigAForm extends GTActionFormBase
{
  private static final String IMPORT_FILE = "importFile";
  private FormFileElement[] _archiveFile = null;
  private String _isOverwrite;
  private IGTMheReference _conflictingEntities;
  private String[] _overwriteEntities;

  //Setters and Getters to support the importFile field:
  public void setImportFileDelete(String[] deletions)
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField(IMPORT_FILE,deletions[i]);
    }
  }

  public void setImportFileUpload(FormFile file)
  {
    addFileToField(IMPORT_FILE,file);
  }

  public FormFileElement[] getImportFile()
  { return _archiveFile; }

  public void setImportFile(FormFileElement[] formFileElements)
  {
    _archiveFile = formFileElements;
  }
  //..........................................................................................

  public String getIsOverwrite()
  {
    return _isOverwrite;
  }

  public void setIsOverwrite(String isOverwrite)
  {
    _isOverwrite = isOverwrite;
  }

  public IGTMheReference getConflictingEntities()
  {
    return _conflictingEntities;
  }

  public String[] getOverwriteEntities()
  {
    return _overwriteEntities;
  }

  void setConflictingEntities(IGTMheReference conflictingEntities)
  { //package protected
    _conflictingEntities = conflictingEntities;
  }

  public void setOverwriteEntities(String[] overwriteEntities)
  {
    _overwriteEntities = overwriteEntities;
  }

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    setOverwriteEntities( StaticUtils.EMPTY_STRING_ARRAY );
    setIsOverwrite( StaticUtils.FALSE_STRING );
  }

}