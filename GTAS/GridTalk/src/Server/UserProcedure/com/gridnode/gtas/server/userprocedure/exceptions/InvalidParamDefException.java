/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidParamDefException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Jagadeesh           Created
 */


package com.gridnode.gtas.server.userprocedure.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class InvalidParamDefException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8161181679336480360L;

	public InvalidParamDefException(String message)
  {
    super(message);
  }

  public InvalidParamDefException(Throwable th)
  {
    super(th);
  }

  public InvalidParamDefException(String message, Throwable ex)
  {
    super(message,ex);
  }

}

