/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteEntityException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
* May 07 2002     Ang Meng Hua        Created
 */
package com.gridnode.pdip.framework.exceptions;

public class DeleteEntityException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1290243755377180124L;

	public DeleteEntityException(String msg)
  {
    super(msg);
  }

  public DeleteEntityException(Throwable ex)
  {
    super(ex);
  }

  public DeleteEntityException(String msg, Throwable ex)
  {
    super(msg, ex);
  }
}