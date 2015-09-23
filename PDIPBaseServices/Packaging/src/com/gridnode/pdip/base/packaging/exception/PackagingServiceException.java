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


package com.gridnode.pdip.base.packaging.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class PackagingServiceException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8649877780946406857L;

	public PackagingServiceException(String excepMessage)
  {
    super(excepMessage);
  }

  public PackagingServiceException(Throwable ex)
  {
    super(ex);
  }

  public PackagingServiceException(String excepMessage, Throwable ex)
  {
    super(excepMessage, ex);
  }

}