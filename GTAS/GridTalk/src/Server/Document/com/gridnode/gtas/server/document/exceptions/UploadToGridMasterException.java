/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UploadToGridMasterException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class UploadToGridMasterException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5206033239749069048L;

	public UploadToGridMasterException(String message)
  {
    super(message);
  }

  public UploadToGridMasterException(Throwable ex)
  {
    super(ex);
  }

  public UploadToGridMasterException(String message, Throwable ex)
  {
    super(message, ex);
  }
}