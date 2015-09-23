/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPackagingHandler
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 16-OCT-2002    Jagadeesh           Created.
 */


package com.gridnode.pdip.base.packaging.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class PackagingException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5470107998007706862L;

	public PackagingException(String excepMessage)
  {
    super(excepMessage);
  }

  public PackagingException(Throwable ex)
  {
    super(ex);
  }

  public PackagingException(String excepMessage, Throwable ex)
  {
    super(excepMessage, ex);
  }

}