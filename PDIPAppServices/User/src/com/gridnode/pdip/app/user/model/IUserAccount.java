/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserAccount.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 11 2002    NSL/OHL             Created
 * Apr 29 2002    Neo Sok Lay         Add version field.
 */
package com.gridnode.pdip.app.user.model;


/**
 * This interface defines the properties and FieldIds for accessing fields
 * in UserAccount entity.
 *
 * @author Neo Sok Lay
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public interface IUserAccount
{
  /**
   * Name for UserAccount entity.
   */
  public static final String ENTITY_NAME = "UserAccount";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID        = new Integer(0);  //integer

  /**
   * FieldId for user Id. A String.
   */
  public static final Number ID         = new Integer(1);  //string(15)

  /**
   * FieldId for user Name. A String.
   */
  public static final Number NAME       = new Integer(2);  //string(50)

  /**
   * FieldId for user password. A String.
   */
  public static final Number PASSWORD   = new Integer(3);  //string(12)

  /**
   * FieldId for Phone. A String.
   */
  public static final Number PHONE      = new Integer(4);  //string(16)

  /**
   * FieldId for Email. A String.
   */
  public static final Number EMAIL      = new Integer(5);  //string(50)

  /**
   * FieldId for Property. A String.
   */
  public static final Number PROPERTY   = new Integer(6); //string(120)

  /**
   * FieldId for user Account State. AccountState.
   */
  public static final Number ACCOUNT_STATE = new Integer(7); //vector

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION       = new Integer(8); //Double

}