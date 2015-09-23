/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DependencyDescriptor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 8 2003     Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.dependency;

/**
 * This is a descriptor for dependency checking for an entity.
 * This descriptor contains the following:<p>
 * <PRE>
 * Name     - Name of the entity this descriptor is for.
 * Checkers - Array of CheckerDescriptor defining the dependency checkers
 *            for this entity.
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DependencyDescriptor
{
  private String _name;
  private CheckerDescriptor[] _checkers;

  public DependencyDescriptor()
  {
  }  

  public void setName(String name)
  {
    _name = name;
  }
  
  public String getName()
  {
    return _name;
  }
  
  public void setCheckers(CheckerDescriptor[] checkers)
  {
    _checkers = checkers;
  }
  
  public CheckerDescriptor[] getCheckers()
  {
    return _checkers;
  }
}
