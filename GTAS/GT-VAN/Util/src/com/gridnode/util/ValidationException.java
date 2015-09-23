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
 * Nov 10 2006    Neo Sok Lay         Created
 */
package com.gridnode.util;

/**
 * Exception indicating a validation error on the inputs.
 * 
 * @author Neo Sok Lay
 */
public class ValidationException extends Exception
{
 
	/**
   * Serial Version UID
   */
  private static final long serialVersionUID = -5057773608316610641L;

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