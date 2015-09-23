/**
 *
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityServiceException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 06-May-2002    Jagadeesh           Created.
 */



package com.gridnode.pdip.base.security.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * The SecurityServiceException must be included in the throw's clause of all
 * the methods of the SecurityServices.
 *
 * This Exception is used as a standard application-level exception to report a
 * failure.
 *
 */

public class SecurityServiceException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2066207092009052145L;

	public SecurityServiceException(String excepMessage)
  {
    super(excepMessage);
  }

  public SecurityServiceException(Throwable ex)
  {
    super(ex);
  }

  public SecurityServiceException(String excepMessage, Throwable ex)
  {
    super(excepMessage, ex);
  }

  }