/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FindEntityException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.framework.exceptions;

public class FindEntityException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7093174668645386917L;

	public FindEntityException(String msg)
  {
    super(msg);
  }

  public FindEntityException(Throwable ex)
  {
    super(ex);
  }

  public FindEntityException(String msg, Throwable ex)
  {
    super(msg, ex);
  }
}