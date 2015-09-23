/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJmsRouter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.model;

/**
 * This interface defines the field IDs and constants for the JmsRouter
 * entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IJmsRouter
{
  /**
   * Entity name for JmsRouter entity.
   */
  static final String ENTITY_NAME     = "JmsRouter";

  /**
   * FieldID for UID. A Long.
   */
  static final Number UID     = new Integer(0);

  /**
   * FieldID for Name. A String.
   */
  static final Number NAME   = new Integer(1); // String(50)

  /**
   * FieldID for IpAddress. A String.
   */
  static final Number IP_ADDRESS  = new Integer(2); // String(50)


}