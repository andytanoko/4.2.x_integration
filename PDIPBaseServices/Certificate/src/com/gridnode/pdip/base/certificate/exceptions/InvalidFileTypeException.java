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

public class InvalidFileTypeException extends ApplicationException
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2823029214094555936L;

	public InvalidFileTypeException(String message)
  {
    super(message);
  }

  public InvalidFileTypeException(Throwable th)
  {
    super(th);
  }

  public InvalidFileTypeException(String message,Throwable th)
  {
    super(message,th);
  }

}


