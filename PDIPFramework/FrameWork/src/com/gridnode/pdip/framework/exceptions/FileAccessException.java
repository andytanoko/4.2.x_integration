/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileAccessException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
* Jul 08 2002     Daniel D'Cotta      Created
 */
package com.gridnode.pdip.framework.exceptions;

public class FileAccessException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2303962722780181326L;

	public FileAccessException(String msg)
  {
    super(msg);
  }

  public FileAccessException(Throwable ex)
  {
    super(ex);
  }

  public FileAccessException(String msg, Throwable ex)
  {
    super(msg, ex);
  }
}