/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapProcedure.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 23 2003    Koh Han Sing            Created
 * Dec 01 2003    Koh Han Sing            Add username and password for HTTP
 *                                        authentication.
 */


package com.gridnode.pdip.base.userprocedure.model;

public class SoapProcedure extends ProcedureDef implements ISoapProcedure
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1771631390609217016L;
	private String _methodName = null;
  private String _username = null;
  private String _password = null;

  public void setMethodName(String methodName)
  {
    _methodName = methodName;
  }

  public String getMethodName()
  {
    return _methodName;
  }

  public void setUsername(String username)
  {
    _username = username;
  }

  public String getUsername()
  {
    return _username;
  }

  public void setPassword(String password)
  {
    _password = password;
  }

  public String getPassword()
  {
    return _password;
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getEntityName()+"/"+getMethodName();
  }

  public Number getKeyId()
  {
    return null;
  }

}