/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserAccountState.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 24 2002    NSL/OHL             Created
 */
package com.gridnode.gtas.model.user;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in UserAccountState entity.
 *
 * @author Neo Sok Lay
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public interface IUserAccountState
{
  /**
   * Name for UserAccountState entity.
   */
  public static final String ENTITY_NAME = "UserAccountState";

  /**
   * FieldID for UID. A Long.
   */
  public static final Number UID                = new Integer(0); //Long

  /**
   * FieldId for Num Login Tries. A Short.
   */
  public static final Number NUM_LOGIN_TRIES    = new Integer(2);  //short

  /**
   * FieldId for Is Freeze. A Boolean
   */
  public static final Number IS_FREEZE          = new Integer(3);  //Boolean

  /**
   * FieldId for Freeze Time. A Timestamp.
   */
  public static final Number FREEZE_TIME        = new Integer(4);  //Timestamp

  /**
   * FieldId for last login time. A Timestamp
   */
  public static final Number LAST_LOGIN_TIME    = new Integer(5);  //Timestamp

  /**
   * FieldId for last logout time. A Timestamp
   */
  public static final Number LAST_LOGOUT_TIME   = new Integer(6);  //Timestamp

  /**
   * FieldId for State. A Short.
   */
  public static final Number STATE              = new Integer(7);  //Short

  /**
   * FieldId for CanDelete. A Boolean
   */
  public static final Number CAN_DELETE         = new Integer(8);  //Boolean

  /**
   * FieldId for create time. A Timestamp
   */
  public static final Number CREATE_TIME        = new Integer(9);  //Timestamp

  /**
   * FieldId for create by. A String
   */
  public static final Number CREATE_BY          = new Integer(10);  //String(15)


  //Possible values for STATE
  public static final short STATE_DISABLED = 0;
  public static final short STATE_ENABLED  = 1;
  public static final short STATE_DELETED  = 2;
}