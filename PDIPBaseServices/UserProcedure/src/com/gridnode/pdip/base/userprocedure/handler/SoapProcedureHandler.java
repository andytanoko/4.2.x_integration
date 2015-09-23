/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapProcedureHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 23 2003    Koh Han Sing            Created
 * Nov 28 2003    Koh Han Sing            Modified to extend from
 *                                        AbstractJavaRelatedProcedureHandler
 *                                        so as to make use of the param type
 *                                        data conversion
 * Dec 01 2003    Koh Han Sing            Add HTTP authentication.
 */
package com.gridnode.pdip.base.userprocedure.handler;

import java.io.File;
import java.util.Vector;

import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.base.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.helpers.SoapCallHelper;
import com.gridnode.pdip.base.userprocedure.model.ProcedureHandlerInfo;
import com.gridnode.pdip.base.userprocedure.model.SoapProcedure;
import com.gridnode.pdip.framework.exceptions.SystemException;

public class SoapProcedureHandler extends AbstractJavaRelatedProcedureHandler
{

  public SoapProcedureHandler()
  {
  }

  public Object execute(ProcedureHandlerInfo info)
    throws UserProcedureExecutionException, SystemException
  {
    Object result = null;
    String wsdlFilename = info.getFileName();
    String wsdlFullpath = info.getFullPath();
    File wsdlFile = new File(wsdlFullpath, wsdlFilename);
    if (wsdlFile.exists())
    {
      try
      {
        SoapProcedure procDef = (SoapProcedure)info.getProcedureDef();
        String methodName = procDef.getMethodName();
        Vector paramDef = info.getParamDef();
        //Object[] parameters = getObjectArrayByParamDef(paramDef);
        Vector cnvParams = setParamTypeValue(paramDef);
        Object[] parameters = (Object[])cnvParams.get(1);
        for (int i = 0; i < parameters.length; i++)
        {
          Logger.debug("[SoapProcedureHandler.execute] parameter "+(i+1)+" = "+parameters[i]);
        }

        SoapCallHelper helper = new SoapCallHelper(wsdlFile.toURL().toString());
        helper.setURL(helper.getServiceURLFromWSDL());
        helper.setUsername(procDef.getUsername());
        helper.setPassword(procDef.getPassword());
        helper.setOperationName(methodName);
        Logger.debug("[SoapProcedureHandler.execute] username ="+procDef.getUsername());
        Logger.debug("[SoapProcedureHandler.execute] password ="+procDef.getPassword());
        Logger.debug("[SoapProcedureHandler.execute] methodName ="+methodName);
        result = helper.Invoke(parameters);
        Logger.debug("[SoapProcedureHandler.execute] result ="+result);
      }
      catch (Exception ex)
      {
        throw new UserProcedureExecutionException("Error invoking webservices", ex);
      }
    }
    else
    {
      throw new UserProcedureExecutionException("WSDL file : "+
        wsdlFile.getAbsolutePath()+" does not exists");
    }

    return result;
  }
}