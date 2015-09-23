/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAccessRight.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.acl.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in AccessRight entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */

public interface IAccessRight
{
  /**
   * Name for AccessRight entity.
   */
  public static final String ENTITY_NAME = "AccessRight";

  /**
   * FieldId for uid. A Long.
   */
  public static final Number UID = new Integer(0); // Long

  /**
   * FieldId for RoleUID. A Long
   */
  public static final Number ROLE = new Integer(1); // Long

  /**
   * FieldId for Feature. A String.
   */
  public static final Number FEATURE = new Integer(2); // String(50)

  /**
   * FieldId for Description.
   */
  public static final Number DESCRIPTION = new Integer(3); // String 80

  /**
   * FieldId for Action. A String.
   */
  public static final Number ACTION = new Integer(4); // String(30)

  /**
   * FieldId for DataType.
   */
  public static final Number DATA_TYPE = new Integer(5); // String 30

  /**
   * FieldId for Criteria.
   */
  public static final Number CRITERIA = new Integer(6); // Free text

  /**
   * FieldId for CanDelete.
   */
  public static final Number CAN_DELETE = new Integer(7); // Boolean

  /**
   * FieldId for Version.
   */
  public static final Number VERSION = new Integer(8); // Double

}