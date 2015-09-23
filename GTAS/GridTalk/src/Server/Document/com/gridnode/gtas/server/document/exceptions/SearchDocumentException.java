/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchDocumentException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class SearchDocumentException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5116038011949836815L;

	public SearchDocumentException(String message)
  {
    super(message);
  }

  public SearchDocumentException(Throwable ex)
  {
    super(ex);
  }

  public SearchDocumentException(String message, Throwable ex)
  {
    super(message, ex);
  }
}