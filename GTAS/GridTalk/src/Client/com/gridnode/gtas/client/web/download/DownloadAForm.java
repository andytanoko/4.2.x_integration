/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DownloadAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.download;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class DownloadAForm extends GTActionFormBase
{
  public static final String DOMAIN                 = "domain";
  public static final String FILE_PATH              = "filePath";

  private String _contentDisposition;
  private String _contentType;
  private String _domain;
  private String _filePath;
  private String _filename;
  private String _actualFilename;

  public void setContentDisposition(String contentDisposition)
  { _contentDisposition = contentDisposition; }

  public String getContentDisposition()
  { return _contentDisposition; }

  public void setContentType(String contentType)
  { _contentType = contentType; }

  public String getContentType()
  { return _contentType; }

  public void setDomain(String domain)
  { _domain = domain; }

  public String getDomain()
  { return _domain; }

  public void setFilePath(String filePath)
  {
    _filePath = filePath;
  }

  public String getFilePath()
  { return _filePath; }

  public void setFilename(String filename)
  { _filename = filename; }

  public String getFilename()
  { return _filename; }

  public void setActualFilename(String actualFilename)
  { _actualFilename = actualFilename; }

  public String getActualFilename()
  { return _actualFilename; }
}