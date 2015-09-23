/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureExecutionException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 26 2002    Jagadeesh           Created
 */


package com.gridnode.pdip.base.userprocedure.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;


/**
 * Thrown when a the UserProcedure cannot Execute a JavaProcedure or ShellExecutable
 * Procedures.
 *
 * This class encuplates, exceptions thrown such as
 *
 * 1. ClassNotFoundException
 * 2. NoSuchMethodException
 * 3. InvocationTargetException
 * 4. IllegalAccessException
 * 5. InstantiationException
 * 6. IOException
 *
 * @author Jagadeesh
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */

public class UserProcedureExecutionException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3424403327609946071L;

	public UserProcedureExecutionException(String msg)
  {
    super(msg);
  }

  public UserProcedureExecutionException(String msg, Throwable t)
  {
    super(msg,t);
  }

}



