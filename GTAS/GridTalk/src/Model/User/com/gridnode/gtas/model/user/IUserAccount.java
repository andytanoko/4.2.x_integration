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
 * Apr 24 2002    NSL/OHL             Created
 */
package com.gridnode.gtas.model.user;

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
  public static final Number EMAIL      = new Integer(5);  //string(255)

  /**
   * FieldId for Property. A String.
   */
  public static final Number PROPERTY   = new Integer(6); //string(255)

  /**
   * FieldId for user Account State. AccountState.
   */
  public static final Number ACCOUNT_STATE = new Integer(7); //AccountState

}