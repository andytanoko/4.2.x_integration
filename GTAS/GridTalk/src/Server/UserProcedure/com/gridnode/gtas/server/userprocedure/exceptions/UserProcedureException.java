/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Jagadeesh           Created
 */

package com.gridnode.gtas.server.userprocedure.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class UserProcedureException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7114716363052350414L;

	public UserProcedureException(String message)
  {
    super(message);
  }

  public UserProcedureException(Throwable th)
  {
    super(th);
  }

  public UserProcedureException(String message,Throwable th)
  {
    super(message,th);
  }

}


