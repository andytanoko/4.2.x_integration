package com.gridnode.pdip.base.certificate.exceptions;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidPasswordException
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 28-April-2008    Qingsong           Created.
 */

public class InvalidPasswordException extends ApplicationException
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5882245200332684406L;

public InvalidPasswordException(String message)
  {
    super(message);
  }

  public InvalidPasswordException(Throwable th)
  {
    super(th);
  }

  public InvalidPasswordException(String message,Throwable th)
  {
    super(message,th);
  }
}