/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JavaProcedure.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Sep 19 2002    Jagadeesh              Created
 */


package com.gridnode.pdip.base.userprocedure.model;

public class JavaProcedure extends  ProcedureDef  implements IJavaProcedure
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1170640342141277044L;
	private String _classPath = null;
  private String _className = null;
  private String _methodName = null;
  private boolean _isLocal = false;
  private String _jvmOptions = null;
  private String _arguments = null;

  public void setClassPath(String classPath)
  {
    _classPath = classPath;
  }


  public void setClassName(String className)
  {
    _className = className;
  }

  public void setMethodName(String methodName)
  {
    _methodName = methodName;
  }

  public void setIsLocal(boolean isLocal)
  {
    _isLocal = isLocal;
  }

  public void setJVMOptions(String jvmOptions)
  {
    _jvmOptions = jvmOptions;
  }

  public void setArguments(String arguments)
  {
    _arguments = arguments;
  }

/************** Getter's ********************/

  public String getClassPath()
  {
    return _classPath;
  }


  public String getClassName()
  {
    return _className;
  }

  public String getMethodName()
  {
    return _methodName;
  }

  public boolean isLocal()
  {
    return _isLocal;
  }

  public String getJVMOptions()
  {
    return _jvmOptions;
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
    return getEntityName()+"/"+getClassName()+"/"+getMethodName();
  }


  public Number getKeyId()
  {
    return null;
  }

}