/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTypeAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-21     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class DocumentTypeAForm extends GTActionFormBase
{
  private String _docType;
  private String _description;

  public void setDocType(String value)
  {
    _docType = value;
  }

  public String getDocType()
  {
    return _docType;
  }

  public void setDescription(String value)
  {
    _description = value;
  }

  public String getDescription()
  {
    return _description;
  }
}