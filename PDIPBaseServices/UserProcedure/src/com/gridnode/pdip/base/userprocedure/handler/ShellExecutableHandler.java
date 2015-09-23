/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ShellExecutableHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 02 2002    Jagadeesh               Created
 * Oct 17 2002    Ang Meng Hua            Refactor to enhance code hygiene
 * Mar 21 2006    Neo Sok Lay             GNDB00017182: Return the exit value
 *                                        from execution of shell script.
 */
package com.gridnode.pdip.base.userprocedure.handler;

import java.util.Vector;

import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.base.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.helpers.NativeExecutable;
import com.gridnode.pdip.base.userprocedure.model.ProcedureHandlerInfo;
import com.gridnode.pdip.base.userprocedure.model.ShellExecutable;
import com.gridnode.pdip.framework.exceptions.SystemException;

public class ShellExecutableHandler extends AbstractProcedureHandler
{
  public ShellExecutableHandler()
  {
  }

  public Object execute(ProcedureHandlerInfo info)
    throws UserProcedureExecutionException,SystemException
  {
   try
   {
      ShellExecutable shellExec = (ShellExecutable)info.getProcedureDef();
      String fullPath = info.getFullPath();
      String fileName = info.getFileName();
      Vector paramDef = info.getParamDef();
      String delimitArgs =   shellExec.getArguments();
      Logger.debug("[ShellExecutable][execute()] Arguments="+delimitArgs);
      String actualArgs = replaceDelimitedText(delimitArgs,
                                               IProcedureHandler.ARGUMENT_DELIMITER,
                                               paramDef);

      int status;
      if(fileName.endsWith(".exe") || fileName.endsWith(".bat") || fileName.endsWith(".sh"))
      {
        status = NativeExecutable.executeNative(fullPath + fileName+ " " + actualArgs);
      }
      else
      {
        status = NativeExecutable.executeNative(fullPath + " " + fileName + " " +actualArgs);
      }
      Logger.debug("[ShellExecutableHandler.execute] userprocedure exec status="+status);
      return status;
   }
   catch(Exception ex)
   {
     throw new UserProcedureExecutionException("[ShellExecutableHandler][execute()]"+
     "Cannot Execute Shell Executalbe"+ex.getMessage(),ex);
   }
   //return null;
  }
}