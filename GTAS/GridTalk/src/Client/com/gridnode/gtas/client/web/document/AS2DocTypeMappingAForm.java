/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-10-17    Wong Yee Wah         Created
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class AS2DocTypeMappingAForm extends GTActionFormBase
{
  private String _as2DocType;
  private String _docType;
  private String _partnerId;

  public String getAs2DocType()
  {
    return _as2DocType;
  }
  
  public void setAs2DocType(String value)
  {
    _as2DocType = value;
  }
  
  public String getDocType()
  {
    return _docType;
  }
  
  public void setDocType(String value)
  {
    _docType = value;
  }
  
  public String getPartnerId()
  {
    return _partnerId;
  }
  
  public void setPartnerId(String value)
  {
    _partnerId = value;
  }
  
}
