/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegisterAttachmentException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 07 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class RegisterAttachmentException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1214061953865247513L;

	public RegisterAttachmentException(String message)
  {
    super(message);
  }

  public RegisterAttachmentException(Throwable ex)
  {
    super(ex);
  }

  public RegisterAttachmentException(String message, Throwable ex)
  {
    super(message, ex);
  }
}