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
 * Oct 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when there is validation error of user inputs to update the
 * Network settings or connection setup parameters.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class FieldValidationException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2182779652659444291L;
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