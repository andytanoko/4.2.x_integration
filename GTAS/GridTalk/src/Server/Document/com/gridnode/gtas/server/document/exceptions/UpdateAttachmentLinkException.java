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
 * Feb 10 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class UpdateAttachmentLinkException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2958425468356631643L;

	public UpdateAttachmentLinkException(String message)
  {
    super(message);
  }

  public UpdateAttachmentLinkException(Throwable ex)
  {
    super(ex);
  }

  public UpdateAttachmentLinkException(String message, Throwable ex)
  {
    super(message, ex);
  }
}