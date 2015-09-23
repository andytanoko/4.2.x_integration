/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserProcedure.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 30 2002    Jagadeesh               Created
 * Jul 08 2003    Koh Han Sing            Add in GridDocument field
 */
package com.gridnode.gtas.model.userprocedure;


/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in UserProcedure entity.
 *
 * @author Jagadeesh.
 *
 * @version 2.0
 * @since 2.0
 */
public interface IUserProcedure
{
  public static final String ENTITY_NAME="UserProcedure";

  public static final Number UID = new Integer(0);

  public static final Number NAME = new Integer(1);

  public static final Number DESCRIPTION = new Integer(2);

  public static final Number IS_SYNCHRONOUS = new Integer(3);

  public static final Number PROC_TYPE = new Integer(4);

  public static final Number PROC_DEF_FILE = new Integer(5);

  public static final Number PROC_DEF = new Integer(6);

  public static final Number PROC_PARAM_LIST = new Integer(7);

  public static final Number RETURN_DATA_TYPE = new Integer(8);

  public static final Number DEF_ACTION = new Integer(9);

  public static final Number DEF_ALERT = new Integer(10);

  public static final Number PROC_RETURN_LIST = new Integer(11);

  public static final Number CAN_DELETE  = new Integer(12); //Boolean

  /**
   * FieldId for GridDocField. A Integer.
   */
  public static final Number GRID_DOC_FIELD = new Integer(14); //Integer
}

