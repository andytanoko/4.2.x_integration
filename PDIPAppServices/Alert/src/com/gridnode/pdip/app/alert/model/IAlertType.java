/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertType.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Dec 05 2002    Srinath	             Created
 * Mar 03 2003    Neo Sok Lay             Add Version and CanDelete fields.
 */

package com.gridnode.pdip.app.alert.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in Alert type entity.
 *
 * @author Srinath
 *
 */

public interface IAlertType
{
  /**
   * Name for Role entity.
   */
  public static final String ENTITY_NAME = "AlertType";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for name.
   */
  public static final Number NAME = new Integer(1); // String 35

  /**
   * FieldId for description.
   */
  public static final Number DESCRIPTION = new Integer(2); // string 100

  /**
   * FieldId for Version. A Double.
   */
  public static final Number VERSION = new Integer(3); //double

  /**
   * FieldId for CanDelete. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(4); //boolean


}