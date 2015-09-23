/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTUserProcedureEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 * 2003-07-16     Andrew Hill         Added GRID_DOC_FIELD field
 * 2003-07-30     Andrew Hill         Added TYPE_SOAP
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.userprocedure.IAction;
import com.gridnode.gtas.model.userprocedure.IDataType;
import com.gridnode.gtas.model.userprocedure.IProcedureType;
import com.gridnode.gtas.model.userprocedure.IUserProcedure;

public interface IGTUserProcedureEntity extends IGTEntity
{
  // Type (Enumeration for PROC_TYPE)
  public static final Integer TYPE_EXECUTABLE           = IProcedureType.PROC_TYPE_EXEC;
  public static final Integer TYPE_JAVA                 = IProcedureType.PROC_TYPE_JAVA;
  public static final Integer TYPE_SOAP                 = IProcedureType.PROC_TYPE_SOAP; //20030730AH

  // Action (Enumeration for DEF_ACTION)
  public static final Integer ACTION_ABORT              = IAction.ABORT;
  public static final Integer ACTION_CONTINUE           = IAction.CONTINUE;

  // Data Type (Enumeration for RETURN_DATA_TYPE)
  public static final Integer DATA_TYPE_BOOLEAN         = IDataType.DATA_TYPE_BOOLEAN;
  public static final Integer DATA_TYPE_DATE            = IDataType.DATA_TYPE_DATE;
  public static final Integer DATA_TYPE_DOUBLE          = IDataType.DATA_TYPE_DOUBLE;
  public static final Integer DATA_TYPE_INTEGER         = IDataType.DATA_TYPE_INTEGER;
  public static final Integer DATA_TYPE_LONG            = IDataType.DATA_TYPE_LONG;
  public static final Integer DATA_TYPE_OBJECT          = IDataType.DATA_TYPE_OBJECT;
  public static final Integer DATA_TYPE_STRING          = IDataType.DATA_TYPE_STRING;

  // Fields
  public static final Number UID                        = IUserProcedure.UID;
  public static final Number NAME                       = IUserProcedure.NAME;
  public static final Number DESCRIPTION                = IUserProcedure.DESCRIPTION;
  public static final Number IS_SYNCHRONOUS             = IUserProcedure.IS_SYNCHRONOUS;
  public static final Number PROC_TYPE                  = IUserProcedure.PROC_TYPE;
  public static final Number PROC_DEF_FILE              = IUserProcedure.PROC_DEF_FILE;
  public static final Number PROC_DEF                   = IUserProcedure.PROC_DEF;
  public static final Number PROC_PARAM_LIST            = IUserProcedure.PROC_PARAM_LIST;
  public static final Number RETURN_DATA_TYPE           = IUserProcedure.RETURN_DATA_TYPE;
  public static final Number DEF_ACTION                 = IUserProcedure.DEF_ACTION;
  public static final Number DEF_ALERT                  = IUserProcedure.DEF_ALERT;
  public static final Number PROC_RETURN_LIST           = IUserProcedure.PROC_RETURN_LIST;
  public static final Number CAN_DELETE                 = IUserProcedure.CAN_DELETE; //030130AMH
  public static final Number GRID_DOC_FIELD             = IUserProcedure.GRID_DOC_FIELD; //20030716AH
}
