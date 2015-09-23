/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionSetupParam.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.connection;

/**
 * This interface defines the field IDs and constants for the ConnectionSetupParam
 * entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IConnectionSetupParam
{
  /**
   * Entity name for ConnectionSetupParam entity.
   */
  static final String ENTITY_NAME     = "ConnectionSetupParam";

  /**
   * FieldID for CurrentLocation. A String.
   */
  static final Number CURRENT_LOCATION     = new Integer(0); // String(3)

  /**
   * FieldID for ServicingRouter. A String.
   */
  static final Number SERVICING_ROUTER   = new Integer(1); // String(50)

  /**
   * FieldID for OriginalLocation. A String.
   */
  static final Number ORIGINAL_LOCATION  = new Integer(2); // String(3)

  /**
   * FieldID for OriginalServicingRouter. An String.
   */
  static final Number ORIGINAL_SERVICING_ROUTER = new Integer(3); // String(50)

}