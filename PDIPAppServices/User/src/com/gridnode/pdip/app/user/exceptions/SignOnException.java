/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SignOnException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 14 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.user.exceptions;

import com.gridnode.pdip.framework.exceptions.NestingException;

/**
 * Exception class for Sign On/Off services.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class SignOnException
  extends    NestingException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6795050593562173833L;

	public SignOnException(String reason)
  {
    super(reason);
  }

  public SignOnException(Throwable ex)
  {
    super(ex);
  }

  public SignOnException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}