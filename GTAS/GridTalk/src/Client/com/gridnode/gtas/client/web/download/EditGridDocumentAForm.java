/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EditGridDocumentAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-23     Daniel D'Cotta      Created (GridForm 2.0 intergration)
 */
package com.gridnode.gtas.client.web.download;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class EditGridDocumentAForm extends GTActionFormBase
{
  private String _domain;
  private String _filePath;
  private String _gridDocId;

  public void setDomain(String domain)
  { _domain = domain; }

  public String getDomain()
  { return _domain; }

  public void setFilePath(String filePath)
  { _filePath = filePath; }

  public String getFilePath()
  { return _filePath; }

  public void setGridDocId(String gridDocId)
  { _gridDocId = gridDocId; }

  public String getGridDocId()
  { return _gridDocId; }
}