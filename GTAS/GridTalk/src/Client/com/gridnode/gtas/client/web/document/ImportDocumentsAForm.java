/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportDocumentsAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-26     Andrew Hill         Created
 * 2002-12-05     Andrew Hill         Attachments
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import javax.servlet.http.*;

import com.gridnode.gtas.client.web.strutsbase.*;

public class ImportDocumentsAForm extends GTActionFormBase
{
  private String _senderId;
  private String _docType;
  private FormFileElement[] _filenames;
  private String[] _recipients;
  private String _isManual;
  private String[] _gdocUids;
  private FormFileElement[] _attachments;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _recipients = null;
    _gdocUids = null;
  }

  public String getSenderId()
  { return _senderId; }

  public void setSenderId(String senderId)
  { _senderId=senderId; }

  public String getDocType()
  { return _docType; }

  public void setDocType(String docType)
  { _docType=docType; }

  // Filenames fields:
  public void setFilenamesDelete(String[] deletions)
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField("filenames",deletions[i]);
    }
  }

  public void setFilenamesUpload(FormFile file)
  {
    addFileToField("filenames",file);
  }

  public FormFileElement[] getFilenames()
  { return _filenames; }

  public void setFilenames(FormFileElement[] filenamesElements)
  { _filenames=filenamesElements; }
  //...............

  // Attachments fields:
  public void setAttachmentsDelete(String[] deletions)
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField("attachments",deletions[i]);
    }
  }

  public void setAttachmentsUpload(FormFile file)
  {
    addFileToField("attachments",file);
  }

  public FormFileElement[] getAttachments()
  { return _attachments; }

  public void setAttachments(FormFileElement[] attachmentsElements)
  { _attachments=attachmentsElements; }
  //...............

  public String[] getRecipients()
  { return _recipients; }

  public void setRecipients(String[] recipients)
  { _recipients=recipients; }

  public String getIsManual()
  { return _isManual; }

  public void setIsManual(String isManual)
  { _isManual=isManual; }

  public String[] getGdocUids()
  { return _gdocUids; }

  public void setGdocUids(String[] gdocUids)
  { _gdocUids=gdocUids; }
}