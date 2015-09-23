/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 14 2002    Srinath	             Created
 * Feb 06 2003    Neo Sok Lay             Change ALERTID to ALERT_UID.
 *                                        Change ACTIONID to ACTION_UID.
 */

package com.gridnode.pdip.app.alert.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in AlertAction entity.
 *
 * @author Srinath
 *
 */

public interface IAlertAction
{
  /**
   * Entityname for alertaction.
   */
  public static final String ENTITY_NAME = "AlertAction";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for alertUid.
   */
  public static final Number ALERT_UID = new Integer(1); // long

  /**
   * FieldId for actionUid.
   */
  public static final Number ACTION_UID = new Integer(2); // long
}