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


public class FTPServiceException extends NestingException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2903542919256134427L;

	public FTPServiceException()
  {
    super();
  }

  public FTPServiceException(String message)
  {
    super(message);
  }

  public FTPServiceException(String message,Throwable ex)
  {
    super(message,ex);
  }


}