/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILicense.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.license.model;

/**
 * This interface defines the field IDs and constants for the License
 * entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface ILicense
{
  /**
   * Entity name for License entity.
   */
  static final String ENTITY_NAME     = "License";

  /**
   * FieldID for UID. A Long.
   */
  static final Number UID             = new Integer(0);

  /**
   * FieldID for Product Key. A String.
   */
  static final Number PRODUCT_KEY     = new Integer(1);  // String(30)

  /**
   * FieldID for Name of the Licensed product. A String.
   */
  static final Number PRODUCT_NAME    = new Integer(2);  // String(80)

  /**
   * FieldID for the version of the Licensed product. A String.
   */
  static final Number PRODUCT_VERSION = new Integer(3);  // String(20)

  /**
   * FieldID for License Start Date. A Date.
   */
  static final Number START_DATE      = new Integer(4);

  /**
   * FieldID for License End Date. A Date.
   */
  static final Number END_DATE        = new Integer(5);

  /**
   * FieldID for the state of the License. A Short.
   */
  static final Number STATE           = new Integer(6);

  /**
   * FieldID for Version of the entity. A Double.
   */
  static final Number VERSION         = new Integer(7);


  // Values for STATE
  /**
   * Possible Value for STATE field. This indicates that the License is
   * currently valid.
   */
  static final short STATE_VALID         = 0;

  /**
   * Possible value for STATE field. This indicates that the License will not
   * be valid until the START_DATE.
   */
  static final short STATE_NOT_COMMENCED = 1;

  /**
   * Possible value for STATE field. This indicates that the License has expired.
   */
  static final short STATE_EXPIRED       = 2;

  /**
   * Possible value for STATE field. This indicates that the License is revoked
   * when it has not expired.
   */
  static final short STATE_REVOKED       = 3;

}