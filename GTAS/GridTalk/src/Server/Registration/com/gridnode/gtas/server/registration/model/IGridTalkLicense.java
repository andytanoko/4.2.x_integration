/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridTalkLicense.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 04 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.model;

/**
 * This interface defines the field IDs and constants for the GridTalkLicense
 * entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public interface IGridTalkLicense
{
  /**
   * Entity name for License entity.
   */
  static final String ENTITY_NAME     = "GridTalkLicense";

  /**
   * FieldID for UID. A Long.
   */
  static final Number UID             = new Integer(0);

  /**
   * FieldID for Version of the entity. A Double.
   */
  static final Number VERSION         = new Integer(1);

  /**
   * FieldId for CanDelete flag. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(2);  //Boolean

  /**
   * FieldID for License UID. A Long.
   */
  static final Number LICENSE_UID     = new Integer(3);

  /**
   * FieldID for Encrypted OS Name. A String
   */
  static final Number OS_NAME         = new Integer(4);

  /**
   * FieldID for Encrypted OS Version. A String
   */
  static final Number OS_VERSION      = new Integer(5);

  /**
   * FieldID for Encrypted Machine Name 3. A String
   */
  static final Number MACHINE_NAME    = new Integer(6);

  /**
   * FieldID for Encrypted Start Date. A String
   */
  static final Number START_DATE      = new Integer(7);

  /**
   * FieldID for Encrypted End Date. A String
   */
  static final Number END_DATE        = new Integer(8);

}