/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Mapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import com.gridnode.gtas.client.utils.*;

public class Mapping extends IdentifiedBean
{
  protected String _path;

  public String toString()
  {
    return "Mapping[" + getId() + "," + _path + "]";
  }

  public void setPath(String path)
  {
    assertNotFrozen();
    _path = path;
  }

  public String getPath()
  { return _path; }

}