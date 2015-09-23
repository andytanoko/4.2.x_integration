/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionSetupResult.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.connection;

/**
 * This interface defines the field IDs and constants for the ConnectionSetupResult
 * entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IConnectionSetupResult
{
  /**
   * Entity name for ConnectionSetupResult entity.
   */
  static final String ENTITY_NAME     = "ConnectionSetupResult";

  /**
   * FieldID for Status. A Short.
   */
  static final Number STATUS     = new Integer(0);

  /**
   * FieldID for SetupParameters. A ConnectionSetupParam.
   */
  static final Number SETUP_PARAMETERS   = new Integer(1);

  /**
   * FieldID for FailureReason. A String.
   */
  static final Number FAILURE_REASON  = new Integer(2);

  /**
   * FieldID for AvailableGridMasters. A List of UIDs.
   */
  static final Number AVAILABLE_GRIDMASTERS = new Integer(3);

  /**
   * FieldID for AvailableRouters. A Collection of UIDs.
   */
  static final Number AVAILABLE_ROUTERS = new Integer(4);

  // values for STATUS
  /**
   * Possible value for STATUS.
   * This indicates that connection setup has not been done before.
   */
  static final short STATUS_NOT_DONE = 0;

  /**
   * Possible value for STATUS.
   * This indicates that connection setup has been completed successfully.
   */
  static final short STATUS_SUCCESS = 1;

  /**
   * Possible value for STATUS.
   * This indicates that connection setup has been attempted but failed.
   */
  static final short STATUS_FAILURE = 2;

}