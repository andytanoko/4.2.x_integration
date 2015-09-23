/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidFileTypeException
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 30-Jan-2003    Jagadeesh           Created.
 */


package com.gridnode.pdip.base.certificate.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class InvalidPasswordOrFileTypeException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1593291031260881858L;

	public InvalidPasswordOrFileTypeException(String message)
  {
    super(message);
  }

  public InvalidPasswordOrFileTypeException(Throwable th)
  {
    super(th);
  }

  public InvalidPasswordOrFileTypeException(String message,Throwable th)
  {
    super(message,th);
  }

}


