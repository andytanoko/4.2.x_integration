/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbortUserProcedureException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Jagadeesh           Created
 */

package com.gridnode.gtas.server.userprocedure.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class AbortUserProcedureException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5447114676141787271L;

	public AbortUserProcedureException(String message)
  {
    super(message);
  }

  public AbortUserProcedureException(Throwable th)
  {
    super(th);
  }

  public AbortUserProcedureException(String message,Throwable th)
  {
    super(message,th);
  }

}



