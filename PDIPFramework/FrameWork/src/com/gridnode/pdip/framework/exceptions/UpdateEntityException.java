/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateEntityException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.framework.exceptions;

public class UpdateEntityException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7796452053486600309L;

	public UpdateEntityException(String msg)
  {
    super(msg);
  }

  public UpdateEntityException(Throwable ex)
  {
    super(ex);
  }

  public UpdateEntityException(String msg, Throwable ex)
  {
    super(msg, ex);
  }
}