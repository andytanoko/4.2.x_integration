/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertCategory.java
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
 * in Category entity.
 *
 * @author Srinath
 *
 */

public interface IAlertCategory
{
  /**
   * Name for Category entity.
   */
  public static final String ENTITY_NAME = "AlertCategory";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for Code.
   */
  public static final Number CODE = new Integer(1); // tinyint

  /**
   * FieldId for Name.
   */
  public static final Number NAME = new Integer(2); // String 30

  /**
   * FieldId for description.
   */
  public static final Number DESCRIPTION = new Integer(3); // string 50

  /**
   * FieldId for Version. A Double.
   */
  public static final Number VERSION = new Integer(4); //double

  /**
   * FieldId for CanDelete. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(5); //boolean


}