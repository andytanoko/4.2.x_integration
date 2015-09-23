/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JavaProcedureAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;


public class JavaProcedureAForm extends GTActionFormBase
{
  private String _className;
  private String _methodName;
  private String _isLocal;
  private String _jvmOptions;
  private String _arguments;

  public String getClassName()
  { return _className; }

  public void setClassName(String className)
  { _className=className; }

  public String getMethodName()
  { return _methodName; }

  public void setMethodName(String methodName)
  { _methodName=methodName; }

  public String getIsLocal()
  { return _isLocal; }

  public void setIsLocal(String isLocal)
  { _isLocal=isLocal; }

  public String getJvmOptions()
  { return _jvmOptions; }

  public void setJvmOptions(String jvmOptions)
  { _jvmOptions=jvmOptions; }

  public String getArguments()
  { return _arguments; }

  public void setArguments(String arguments)
  { _arguments=arguments; }

  public void doReset(ActionMapping actionMapping, HttpServletRequest httpServletRequest)
  {
    _isLocal = null;
  }
}