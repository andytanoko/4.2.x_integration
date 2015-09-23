/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResumeSendException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class ResumeSendException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -724192661523944483L;

	public ResumeSendException(String message)
  {
    super(message);
  }

  public ResumeSendException(Throwable ex)
  {
    super(ex);
  }

  public ResumeSendException(String message, Throwable ex)
  {
    super(message, ex);
  }
}