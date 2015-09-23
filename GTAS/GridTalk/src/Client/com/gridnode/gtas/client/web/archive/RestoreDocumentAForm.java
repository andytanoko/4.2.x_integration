/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RestoreDocumentAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-22     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.archive;

import org.apache.struts.upload.FormFile;

import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class RestoreDocumentAForm extends GTActionFormBase
{
  private static final String ARCHIVE_FILE = "archiveFile";
  private FormFileElement[] _archiveFile = null;

  //Setters and Getters to support the archiveFile field:
  public void setArchiveFileDelete(String[] deletions)
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField(ARCHIVE_FILE,deletions[i]);
    }
  }

  public void setArchiveFileUpload(FormFile file)
  {
    addFileToField(ARCHIVE_FILE,file);
  }

  public FormFileElement[] getArchiveFile()
  { return _archiveFile; }

  public void setArchiveFile(FormFileElement[] formFileElements)
  {
    _archiveFile = formFileElements;
  }
  //.....................................................
}