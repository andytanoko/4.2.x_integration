/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateEntityException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.framework.exceptions;

public class CreateEntityException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8522349126044319755L;

	public CreateEntityException(String msg)
  {
    super(msg);
  }

  public CreateEntityException(Throwable ex)
  {
    super(ex);
  }

  public CreateEntityException(String msg, Throwable ex)
  {
    super(msg, ex);
  }
}