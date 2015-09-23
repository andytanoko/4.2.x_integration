/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 11 2002    Srinath	             Created
 * Feb 06 2003    Neo Sok Lay             Change MSGID to MSG_UID.
 * Mar 03 2003    Neo Sok Lay             Add Version and CanDelete fields.
 */
package com.gridnode.pdip.app.alert.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in Alert entity.
 *
 * @author Srinath
 *
 */
public interface IAction
{
  /**
   * Name for Action entity.
   */
  public static final String ENTITY_NAME = "Action";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for Name.
   */
  public static final Number NAME = new Integer(1); // String 30

  /**
   * FieldId for description.
   */
  public static final Number DESCRIPTION = new Integer(2); // string 50

  /**
   * FieldId for MsgUid.
   */
  public static final Number MSG_UID = new Integer(3); // bigint

  /**
   * FieldId for Version. A Double.
   */
  public static final Number VERSION = new Integer(4); //double

  /**
   * FieldId for CanDelete. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(5); //boolean


}