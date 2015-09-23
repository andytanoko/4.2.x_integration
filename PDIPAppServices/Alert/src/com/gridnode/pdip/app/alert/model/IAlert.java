/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlert.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	             Created
 * Feb 07 2003    Neo Sok Lay             Add BIND_ACTIONS_UIDS.
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

public interface IAlert
{
  /**
   * Name for Alert entity.
   */
  public static final String ENTITY_NAME = "Alert";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for Name.
   */
  public static final Number NAME = new Integer(1); // String 80

  /**
   * FieldId for AlertType. A Long.
   */
  public static final Number ALERT_TYPE_UID = new Integer(2); //

  /**
   * FieldId for Alert Category.  A Long.
   */
  public static final Number CATEGORY_UID = new Integer(3); //

  /**
   * FieldId for Trigger.
   */
  public static final Number TRIGGER = new Integer(4); // Long

  /**
   * FieldId for Description.
   */
  public static final Number DESCRIPTION = new Integer(5); // string 255

  /**
   * FieldId for BindActions. A Collection of Long (UIDs).
   */
  public static final Number BIND_ACTIONS_UIDS = new Integer(6); //Collection

  /**
   * FieldId for Version. A Double.
   */
  public static final Number VERSION = new Integer(7); //double

  /**
   * FieldId for CanDelete. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(8); //boolean


}