/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HousekeepingException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 4, 2003      Mahesh         Created
 */
package com.gridnode.gtas.server.housekeeping.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class HousekeepingException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5283923821370568888L;

	public HousekeepingException(String message)
  {
    super(message);
  }

  public HousekeepingException(Throwable ex)
  {
    super(ex);
  }

  public HousekeepingException(String message, Throwable ex)
  {
    super(message, ex);
  }
}
