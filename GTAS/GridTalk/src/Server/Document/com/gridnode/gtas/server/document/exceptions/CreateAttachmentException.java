/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateAttachmentException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 25 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class CreateAttachmentException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8546594994819927938L;

	public CreateAttachmentException(String message)
  {
    super(message);
  }

  public CreateAttachmentException(Throwable ex)
  {
    super(ex);
  }

  public CreateAttachmentException(String message, Throwable ex)
  {
    super(message, ex);
  }
}