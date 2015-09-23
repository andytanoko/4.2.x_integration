/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Insert.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 17/06/2003     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import com.gridnode.gtas.client.utils.IdentifiedBean;
 
public class Insert extends IdentifiedBean
{
  private String _document;
  private String _from;
  
  public String toString()
  {
    return "Insert[" + getId() + ", from=," + getFrom() + ", in=" + getDocument() + "]";
  }

  public void freeze()
  {
    super.freeze();
  }
  
  public String getDocument()
  {
    return _document;
  }

  public String getFrom()
  {
    return _from;
  }

  public void setDocument(String string)
  {
    _document = string;
  }

  public void setFrom(String string)
  {
    _from = string;
  }

}