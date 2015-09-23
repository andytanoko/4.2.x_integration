/* This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2003    Ang Meng Hua            Created
 */
package com.gridnode.pdip.app.channel.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class SecurityException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3144615242531639530L;

	public SecurityException(String message)
  {
    super(message);
  }

  public SecurityException(String message, Throwable th)
  {
    super(message, th);
  }

  public SecurityException(Throwable th)
  {
    super(th);
  }
}
