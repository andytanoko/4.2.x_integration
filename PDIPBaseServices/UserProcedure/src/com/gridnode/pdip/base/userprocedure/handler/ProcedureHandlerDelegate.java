/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureHandlerDelegate.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 02 2002    Jagadeesh               Created
 * Oct 17 2002    Ang Meng Hua            Refactor to enhance code hygiene
 */

package com.gridnode.pdip.base.userprocedure.handler;

import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.base.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.model.IProcedureType;
import com.gridnode.pdip.base.userprocedure.model.ProcedureHandlerInfo;
import com.gridnode.pdip.framework.exceptions.SystemException;

public class ProcedureHandlerDelegate
{
  public ProcedureHandlerDelegate()
  {
  }

  public static Object execute(ProcedureHandlerInfo info)
    throws UserProcedureExecutionException,SystemException
  {
    IProcedureHandler handler = null;
    int procedureType = info.getProcedureType();
    if (procedureType == IProcedureType.PROC_TYPE_JAVA)
    {
      Logger.log("[ProcedureHandlerDelegate.execute] Executing Java Procedure");
      handler = new JavaProcedureHandler();
    }
    else if(procedureType == IProcedureType.PROC_TYPE_EXEC)
    {
      Logger.log("[ProcedureHandlerDelegate.execute] Executing Shell Executable");
      handler = new ShellExecutableHandler();
    }
    else if(procedureType == IProcedureType.PROC_TYPE_SOAP)
    {
      Logger.log("[ProcedureHandlerDelegate.execute] Executing Soap Procedure");
      handler = new SoapProcedureHandler();
    }
    if (handler != null)
      return handler.execute(info);
    else
    throw new UserProcedureExecutionException("No handler exist for Procedure Type " + procedureType);
  }
}