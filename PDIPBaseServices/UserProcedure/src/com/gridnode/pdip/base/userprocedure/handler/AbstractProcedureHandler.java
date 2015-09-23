/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractProcedureHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 04  2003    Jagadeesh               Created
 */


package com.gridnode.pdip.base.userprocedure.handler;

import com.gridnode.pdip.base.userprocedure.model.ProcedureHandlerInfo;
import com.gridnode.pdip.base.userprocedure.model.ParamDef;

import com.gridnode.pdip.base.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.HashMap;
import java.util.Vector;
import java.util.StringTokenizer;



public abstract class AbstractProcedureHandler implements IProcedureHandler
{

  private static final String CLASS_NAME = "AbstractProcedureHandler";

  public abstract Object execute(ProcedureHandlerInfo procedureHandlerInfo)
    throws UserProcedureExecutionException,SystemException;


   public String replaceDelimitedText(
    String str,
    String delimiter,
    Vector paramDef) throws Exception
  {
    Logger.debug("["+CLASS_NAME+"][replaceDelimitedText()] String To Process="+str);
    HashMap table = getParamMapByParamDef(paramDef);
    if ((str != null) && (!str.equals("")))
    {
      StringTokenizer strToken = new StringTokenizer(str, delimiter);
      StringBuffer strBuf = new StringBuffer(str);
      while (strToken.hasMoreTokens())
      {
        String key = strToken.nextToken().trim();
        int spaceIndex = key.indexOf(" ");
        if (spaceIndex != -1) key = key.substring(0, spaceIndex);
        if (table.get(key) == null)
          continue;

        String value = table.get(key).toString();
        int startIndex = str.indexOf(delimiter + key);
        int endIndex = startIndex + 1 + key.length();
        strBuf.replace(startIndex, endIndex, value);
        str = strBuf.toString();
        strToken = new StringTokenizer(str, delimiter);
      }
    }
    Logger.log("["+CLASS_NAME+"][replaceDelimitedText()] ReturnValue="+str);
    return str;
  }


  public Vector getParamVectByParamDef(Vector paramDef)
  {
    final String LOG_MESSAGE = "["+CLASS_NAME+"][getParamVectByParamDef()]";
    Vector paramValues = new Vector();
    for(int i=0;i<paramDef.size();i++)
    {
      ParamDef paramDefVal = (ParamDef)paramDef.get(i);
      Object value = paramDefVal.getActualValue();
      Logger.debug(LOG_MESSAGE+"[ParamValue=] "+value);
      paramValues.add(value);
    }
    return paramValues;
  }


  public HashMap getParamMapByParamDef(Vector paramDef)
  {
    final String LOG_MESSAGE = "["+CLASS_NAME+"][getParamVectByParamDef()]";
    HashMap paramMap = new HashMap();
    for(int i=0;i<paramDef.size();i++)
    {
      ParamDef paramDefVal = (ParamDef)paramDef.get(i);
      String name = paramDefVal.getName();
      Object value = paramDefVal.getActualValue();
      Logger.debug(LOG_MESSAGE+"[ParamName=] "+name);
      Logger.debug(LOG_MESSAGE+"[ParamValue=] "+value);
      paramMap.put(name,value);
    }
    return paramMap;
  }

  public Object[] getObjectArrayByParamDef(Vector paramDef)
    throws Exception
  {
    final String LOG_MESSAGE = "["+CLASS_NAME+"][getObjectArray()]";
    Object[] objects = new Object[paramDef.size()];
    for(int i=0;i<paramDef.size();i++)
    {
      ParamDef paramDefVal = (ParamDef)paramDef.get(i);
      Object value = paramDefVal.getActualValue();
      String className = value.getClass().getName();
      Logger.debug(LOG_MESSAGE+"[Param className=] "+className);
      Logger.debug(LOG_MESSAGE+"[ParamValue=] "+value);
      objects[i] = AbstractEntity.convert(value, className);
    }
    return objects;
  }


}