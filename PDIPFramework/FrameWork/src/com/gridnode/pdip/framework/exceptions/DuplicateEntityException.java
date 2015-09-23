/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DuplicateEntityException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.exceptions;

public class DuplicateEntityException extends CreateEntityException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8839262317208970084L;

	public DuplicateEntityException(String msg)
  {
    super(msg);
  }

  public DuplicateEntityException(Throwable ex)
  {
    super(ex);
  }

  public DuplicateEntityException(String msg, Throwable ex)
  {
    super(msg, ex);
  }
}