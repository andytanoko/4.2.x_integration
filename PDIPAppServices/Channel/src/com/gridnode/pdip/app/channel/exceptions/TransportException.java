/* This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransportException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2003    Ang Meng Hua            Created
 */
package com.gridnode.pdip.app.channel.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class TransportException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8227104129838239385L;

	public TransportException(String message)
  {
    super(message);
  }

  public TransportException(String message, Throwable th)
  {
    super(message, th);
  }

  public TransportException(Throwable th)
  {
    super(th);
  }
}
