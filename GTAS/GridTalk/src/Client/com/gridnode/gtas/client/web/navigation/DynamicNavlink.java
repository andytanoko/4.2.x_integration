/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Navlink.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.navigation;


/**
 * This is a NavLink which can have dynamically generated child nodes.
 * There should be 1 dummy child node below for it to do stamping.
 */
public class DynamicNavlink extends Navlink
{
  public static final String NAV_RENDERER_PROCESS_INSTANCE = "com.gridnode.gtas.client.web.bp.ProcessInstanceNavRenderer";

  private String _navRenderer;

  public String toString()
  {
    return "DynamicNavlink[" + getId() + "," + _label + "," + _value + "," + _clear + "," + _navRenderer + "]";
  }

  public void setNavRenderer(String navRenderer)
  {
    assertNotFrozen();
    _navRenderer = navRenderer;
  }

  public String getNavRenderer()
  { return _navRenderer; }
}