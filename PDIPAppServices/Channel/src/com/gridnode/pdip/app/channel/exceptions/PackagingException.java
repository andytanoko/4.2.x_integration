/* This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackageException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2003    Ang Meng Hua            Created
 */
package com.gridnode.pdip.app.channel.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class PackagingException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1167784459592750165L;

	public PackagingException(String message)
  {
    super(message);
  }

  public PackagingException(String message, Throwable th)
  {
    super(message, th);
  }

  public PackagingException(Throwable th)
  {
    super(th);
  }
}
