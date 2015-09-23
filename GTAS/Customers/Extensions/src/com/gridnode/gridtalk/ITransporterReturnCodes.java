/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITransporterReturnCodes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 04 2003    Neo Sok Lay         Created
 */
package com.gridnode.gridtalk;

/**
 * Interface defining the Error codes used in the Transporter module.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public interface ITransporterReturnCodes
{
  /**
   * Indicates a successful execution and processing.
   */
  static final int SUCCESS              = 0;

  /**
   * Indicates an error in validating the input properties.
   */
  static final int INVALID_INPUT_PROPS  = -10;

  /**
   * Indicates an error in running the transporter to communicate with GridTalk.
   */
  static final int TRANSPORTER_RUN_ERROR = -20;

  /**
   * Indicate other general errors that occurred.
   */
  static final int UNKNOWN_ERROR        = -100;

}