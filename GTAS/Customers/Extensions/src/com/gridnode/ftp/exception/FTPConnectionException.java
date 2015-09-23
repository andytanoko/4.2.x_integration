/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FTPServiceException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * APR 30 2003    Jagadeesh               Created
 */

package com.gridnode.ftp.exception;


public class FTPConnectionException extends NestingException
{

 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6940730998495569704L;

	public FTPConnectionException()
  {
    super();
  }

  public FTPConnectionException(String message)
  {
    super(message);
  }

  public FTPConnectionException(String message,Throwable ex)
  {
    super(message,ex);
  }

}