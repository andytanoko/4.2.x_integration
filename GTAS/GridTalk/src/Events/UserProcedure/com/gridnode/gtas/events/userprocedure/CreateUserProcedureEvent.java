/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateUserProcedureEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh               Created.
 * Oct 23 2002    Daniel D'Cotta          Modified to use Integer and Boolean instead
 *                                        of int and boolean in the constructor.
 * Jun 18 2003    Daniel D'Cotta          Modified to change null into zero for
 *                                        returnType and defAction
 * Jul 09 2003    Koh Han Sing            Add in GridDocField
 */

package com.gridnode.gtas.events.userprocedure;

import java.util.HashMap;
import java.util.Vector;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class CreateUserProcedureEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2436385664073835128L;

	public static final String NAME = "Name";

  public static final String DESCRIPTION = "Description";

  public static final String IS_SYNCHRONOUS = "Is Synchronous";

  public static final String PROC_TYPE = "Procedure Type";

  public static final String PROC_DEF_FILE_UID = "Procedure Definition File UID";

  public static final String PROC_DEF = "Procedure Definition";

  public static final String PROC_PARAM_LIST = "Procedure Param List";

  public static final String RETURN_DATA_TYPE = "Return DataType";

  public static final String DEF_ACTION = "Default Action";

  public static final String DEF_ALERT = "Default Alert";

  public static final String PROC_RETURN_LIST = "Procedure Return List";

  public static final String GRID_DOC_FIELD = "GridDocument Field";

  public CreateUserProcedureEvent(
    String  name,
    String  description,
    Boolean isSynchronous,
    Integer procType,
    Long    procDefFileUID,
    HashMap procDef,
    Vector  procParamList,
    Integer returnType,
    Integer defAction,
    Long    defAlert,
    Vector  procReturnList,
    Integer gridDocField)
  throws EventException
  {
    checkSetString(NAME,name);
    checkSetString(DESCRIPTION,description);
    checkSetObject(IS_SYNCHRONOUS,isSynchronous,Boolean.class);
    checkSetObject(PROC_TYPE,procType,Integer.class);
    checkSetLong(PROC_DEF_FILE_UID,procDefFileUID);

    setEventData(PROC_DEF,procDef);
    setEventData(PROC_PARAM_LIST,procParamList);
    setEventData(RETURN_DATA_TYPE,returnType);
    setEventData(DEF_ACTION,defAction);
    setEventData(DEF_ALERT,defAlert);
    setEventData(PROC_RETURN_LIST,procReturnList);
    setEventData(GRID_DOC_FIELD,gridDocField);

  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

  public boolean isSynchronous()
  {
    return ((Boolean)getEventData(IS_SYNCHRONOUS)).booleanValue();
  }

  public int getProcedureType()
  {
    return ((Integer)getEventData(PROC_TYPE)).intValue();
  }

  public Long getProcedureDefFileUID()
  {
    return (Long)getEventData(PROC_DEF_FILE_UID);
  }

  public HashMap getProcedureDef()
  {
    return (HashMap)getEventData(PROC_DEF);
  }

  public Vector getParamList()
  {
    return (Vector)getEventData(PROC_PARAM_LIST);
  }

  public int getReturnType()
  {
//    return ((Integer)getEventData(RETURN_DATA_TYPE)).intValue();
    Integer returnType = (Integer)getEventData(RETURN_DATA_TYPE);
    return (returnType != null) ? returnType.intValue() : 0;
  }

  public int getDefaultAction()
  {
//    return ((Integer)getEventData(DEF_ACTION)).intValue();
    Integer defAction = (Integer)getEventData(DEF_ACTION);
    return (defAction != null) ? defAction.intValue() : 0;
  }

  public Long getDefaultAlert()
  {
    return (Long)getEventData(DEF_ALERT);
  }

  public Vector getReturnList()
  {
    return (Vector)getEventData(PROC_RETURN_LIST);
  }

  public Integer getGridDocField()
  {
    return (Integer)getEventData(GRID_DOC_FIELD);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateUserProcedureEvent";
  }
}