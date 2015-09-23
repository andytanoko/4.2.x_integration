/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISenderReturnCodes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27 2003    Neo Sok Lay         Created
 */
package com.gridnode.utadaptor;

/**
 * This interface defines the set of possible Return codes from the Sender program.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public interface ISenderReturnCodes
{
  /**
   * Indicates a successful execution and processing.
   */
  static final int SUCCESS              = 0;
  
  /**
   * Indicates problems with the input arguments provided to the
   * Sender program.
   */
  static final int INVALID_INPUT_ARGS   = -10;
  
  /**
   * Indicates problems with the Adaptor run configuration file provided
   * to the Sender program. 
   */
  static final int ADAPTOR_CONFIG_ERROR = -20;
  
  /**
   * Indicates problems with running the Adaptor.
   */
  static final int ADAPTOR_RUN_ERROR    = -30;
  
  /**
   * Indicates errors returned from the Adaptor after its run. 
   */
  static final int ADAPTOR_RETURNS_ERROR= -40;
  
  /**
   * Indicates unexpected errors encountered throughout the execution
   * of the Sender program.
   */
  static final int UNKNOWN_ERROR        = -100;

}