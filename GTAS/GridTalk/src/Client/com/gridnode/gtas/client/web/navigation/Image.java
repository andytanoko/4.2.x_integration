/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Image.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-10     Andrew Hill         Created [EXPERIMENTAL]
 */
package com.gridnode.gtas.client.web.navigation;

import com.gridnode.gtas.client.utils.IdentifiedBean;
 
public class Image extends IdentifiedBean
{
  private String _src;
  private boolean _rewrite;

  public String toString()
  {
    return "Image[" + getId() + "," + _src + "]";
  }

  public void setSrc(String src)
  {
    assertNotFrozen();
    _src = src;
  }

  public String getSrc()
  { return _src; }

  public void setRewrite(boolean rewrite)
  {
    assertNotFrozen();
    _rewrite = rewrite;
  }

  public boolean isRewrite()
  { return _rewrite; }

}