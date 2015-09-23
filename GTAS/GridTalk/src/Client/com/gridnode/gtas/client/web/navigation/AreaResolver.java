/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AreaResolver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gridnode.gtas.client.GTClientException;
 
public class AreaResolver extends AbstractResolver
{
  public static final String CURRENT_AREA_KEY = "com.gridnode.gtas.client.web.navigation.AreaResolver.currentArea";
  public static final String USER_AREA = "user";
  public static final String ADMIN_AREA = "admin";
  public static final String NO_AREA = "none";
  private static final String INITIAL_AREA = USER_AREA;

  private static final String USER_PREFIX = "ngu_";
  private static final String ADMIN_PREFIX = "nga_";

  private String _suffix;

  public String toString()
  {
    return "AreaResolver[" + getId() + ", " + getSuffix() + "]";
  }

  public void setSuffix(String suffix)
  {
    assertNotFrozen();
    _suffix = suffix;
  }

  public String getSuffix()
  { return _suffix; }

  public String getNavgroupId(HttpServletRequest request) throws GTClientException
  {
    try
    {
      String currentArea = getCurrentArea(request);
      String prefix = null;
      String suffix = getSuffix();
      if(suffix == null)
      {
        throw new NullPointerException("Internal assertion failure - null suffix");
      }
      if(ADMIN_AREA.equals(currentArea))
      {
        prefix = ADMIN_PREFIX;
      }
      else if(USER_AREA.equals(currentArea))
      {
        prefix = USER_PREFIX;
      }
      else if(NO_AREA.equals(currentArea))
      {
        prefix = "";
      }
      else
      {
        throw new java.lang.IllegalStateException("Unknown navigation area:" + currentArea);
      }
      return prefix + suffix;
    }
    catch(Throwable t)
    {
      throw new GTClientException(this.toString() + " was unable to determine navgroupId",t);
    }
  }

  public void freeze()
  {
    if(getSuffix() == null)
      throw new NullPointerException("suffix not set in AreaResolver:" + getId());
    super.freeze();
  }

  public static String getCurrentArea(HttpServletRequest request) throws GTClientException
  {
    try
    {
      HttpSession session = request.getSession(false);
      if(session == null)
      {
        throw new NullPointerException("No HttpSession");
      }
      String currentArea = (String)session.getAttribute(CURRENT_AREA_KEY);
      if(currentArea == null)
      {
        currentArea = INITIAL_AREA;
        setCurrentArea(request, currentArea);
      }
      return currentArea;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to determine current navigation area",t);
    }
  }

  public static void setCurrentArea(HttpServletRequest request, String currentArea)
    throws GTClientException
  {
    try
    {
      HttpSession session = request.getSession(false);
      if(session == null)
      {
        throw new NullPointerException("No HttpSession");
      }
      if(currentArea == null)
      {
        currentArea = USER_AREA;
      }
      session.setAttribute(CURRENT_AREA_KEY, currentArea);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error attempting to set current navigation area",t);
    }
  }
}