/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidReturnDefException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Jagadeesh           Created
 */

package com.gridnode.gtas.server.userprocedure.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class InvalidReturnDefException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5327254978052063698L;

	public InvalidReturnDefException(String message)
  {
    super(message);
  }

  public InvalidReturnDefException(Throwable th)
  {
    super(th);
  }

  public InvalidReturnDefException(String message,Throwable ex)
  {
    super(message,ex);
  }
}
