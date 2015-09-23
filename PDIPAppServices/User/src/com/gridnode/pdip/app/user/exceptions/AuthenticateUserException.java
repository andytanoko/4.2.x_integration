/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuthenticateUserException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 12 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.user.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class AuthenticateUserException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8580753222523948235L;

	public AuthenticateUserException(String reason)
  {
    super(reason);
  }

  public AuthenticateUserException(Throwable ex)
  {
    super(ex);
  }

  public AuthenticateUserException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}