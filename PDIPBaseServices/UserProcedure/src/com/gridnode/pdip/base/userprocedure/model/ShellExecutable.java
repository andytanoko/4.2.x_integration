/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ShellExecutable.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Sep 19 2002    Jagadeesh              Created
 */


package com.gridnode.pdip.base.userprocedure.model;

public class ShellExecutable extends ProcedureDef implements IShellExecutable
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7172529635969256442L;
	public String _arguments = null;

  public void setArguments(String arguments)
  {
    _arguments = arguments;
  }

  public String getArguments()
  {
    return _arguments;
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getEntityName()+"/"+getArguments();
  }


  public Number getKeyId()
  {
    return null;
  }

}