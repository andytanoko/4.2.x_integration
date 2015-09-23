/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldValidationException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 18 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.exceptions;

/**
 * Thrown when there is validation error of user inputs.
 * 
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class FieldValidationException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -918550913954670237L;
	private static final String MSG_PREFIX = "Field Validation Error! - ";

  public FieldValidationException(String msg)
  {
    super(MSG_PREFIX+msg);
  }

  public FieldValidationException(String msg, Throwable t)
  {
    super(MSG_PREFIX+msg, t);
  }
}