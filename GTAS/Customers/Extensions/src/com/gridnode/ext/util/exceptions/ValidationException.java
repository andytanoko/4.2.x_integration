/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValidationException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 04 2003    Neo Sok Lay         Created
 */
package com.gridnode.ext.util.exceptions;

/**
 * Exception indicating a validation error on the inputs.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class ValidationException extends Exception
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8516578377830862235L;

	/**
   * Constructs a ValidationException
   * 
   * @param msg The error message.
   */
  public ValidationException(String msg)
  {
    super(msg);
  }
}