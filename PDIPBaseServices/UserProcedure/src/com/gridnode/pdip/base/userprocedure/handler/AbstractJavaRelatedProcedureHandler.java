/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractJavaRelatedProcedureHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 28 2003    Koh Han Sing            Created
 */
package com.gridnode.pdip.base.userprocedure.handler;

import com.gridnode.pdip.base.userprocedure.model.IDataType;
import com.gridnode.pdip.base.userprocedure.model.ParamDef;
import com.gridnode.pdip.base.userprocedure.helpers.Logger;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public abstract class AbstractJavaRelatedProcedureHandler
  extends AbstractProcedureHandler
{

  private static final String CLASS_NAME = "AbstractJavaRelatedProcedureHandler";

  protected static Hashtable classMaps = new Hashtable();
  protected static Hashtable _classMaps = new Hashtable();

  static
  {
    classMaps.put(String.class,new Integer(IDataType.DATA_TYPE_STRING));
    classMaps.put(Boolean.class,new Integer(IDataType.DATA_TYPE_BOOLEAN));
    classMaps.put(Integer.class,new Integer(IDataType.DATA_TYPE_INTEGER));
    classMaps.put(Double.class,new Integer(IDataType.DATA_TYPE_DOUBLE));
    classMaps.put(Long.class,new Integer(IDataType.DATA_TYPE_LONG));
    classMaps.put(Date.class,new Integer(IDataType.DATA_TYPE_DATE));
    classMaps.put(Object.class,new Integer(IDataType.DATA_TYPE_OBJECT));
  }

  static
  {
    _classMaps.put(new Integer(IDataType.DATA_TYPE_STRING),String.class);
    _classMaps.put(new Integer(IDataType.DATA_TYPE_BOOLEAN),Boolean.class);
    _classMaps.put(new Integer(IDataType.DATA_TYPE_INTEGER),Integer.class);
    _classMaps.put(new Integer(IDataType.DATA_TYPE_DOUBLE),Double.class);
    _classMaps.put(new Integer(IDataType.DATA_TYPE_LONG),Long.class);
    _classMaps.put(new Integer(IDataType.DATA_TYPE_DATE),Date.class);
    _classMaps.put(new Integer(IDataType.DATA_TYPE_OBJECT),Object.class);
  }

  protected Vector setParamTypeValue(Vector paramDef)
    throws Exception
  {
    final String MESSAGE_FORMAT = "["+CLASS_NAME+"[setParamTypeValue()] ";
    //Vector paramValues = getParamVectByParamDef(paramDef);
    Vector paramValues = paramDef;
    Class[] paramType  = new Class[paramValues.size()];
    Object[] paramValue = new Object[paramValues.size()];
    Iterator iter = paramValues.iterator();
    Vector returnList = new Vector();
    int i=0;
    while(iter.hasNext())
    {
      ParamDef value  = (ParamDef)iter.next();
      Logger.debug(MESSAGE_FORMAT+"Value="+value);
      try
      {
        if (value.getActualValue() instanceof byte[])
        {
          paramType[i] = value.getActualValue().getClass();
          paramValue[i] = value.getActualValue();
        }
        else if (value.getActualValue() instanceof byte[][])
        {
          paramType[i] = value.getActualValue().getClass();
          paramValue[i] = value.getActualValue();
        }
        else
        {
          paramType[i] = (Class)_classMaps.get(new Integer(value.getType()));
          if( paramType[i] == null || (classMaps.get(paramType[i])) == null)  //Do Not return a Null
          {
            paramType[i] = value.getActualValue().getClass();
            Logger.log(MESSAGE_FORMAT+"User Defined Data Type "+paramType[i]);
          }
          paramValue[i] =  AbstractEntity.convert(
                                value.getActualValue(),paramType[i].getName());

        }
        Logger.debug(MESSAGE_FORMAT+"ParamType='"+paramType[i]+"'");
        Logger.debug(MESSAGE_FORMAT+"ParamValue='"+paramValue[i]+"'");
        i++;
      }
      catch(Exception e)
      {
        throw new Exception(MESSAGE_FORMAT+"Cannot Set Parameter to Class");
      }
    }
    returnList.add(paramType);
    returnList.add(paramValue);
    return returnList;
  }

}