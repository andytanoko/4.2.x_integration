/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SimpleResolver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.GTClientException;
 
public class SimpleResolver extends AbstractResolver
{
  private String _navgroup;

  public String toString()
  {
    return "SimpleResolver[" + getId() + ", " + getNavgroup() + "]";
  }

  public void setNavgroup(String navgroup)
  {
    assertNotFrozen();
    _navgroup = navgroup;
  }

  public String getNavgroup()
  { return _navgroup; }

  public String getNavgroupId(HttpServletRequest request) throws GTClientException
  {
    return getNavgroup();
  }

  public void freeze()
  {
    if(getNavgroup() == null)
      throw new NullPointerException("navgroup not set in SimpleResolver:" + getId());
    super.freeze();
  }
}