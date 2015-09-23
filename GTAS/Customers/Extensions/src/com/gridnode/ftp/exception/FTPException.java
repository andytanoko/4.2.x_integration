/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FTPException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * APR 30 2003    Jagadeesh               Created
 */


package com.gridnode.ftp.exception;

public class FTPException extends NestingException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5484422034346636706L;
	private int replyCode;

  public FTPException(String msg)
  {
    super(msg);
    replyCode = -1;

  }

  public FTPException(String msg, String replyCode)
  {
    super(msg);
    this.replyCode = -1;
    try
    {
        this.replyCode = Integer.parseInt(replyCode);
    }
    catch(NumberFormatException _ex)
    {
        this.replyCode = -1;
    }
  }

  public FTPException(String msg,Throwable th)
  {
    super(msg,th);
  }

  public int getReplyCode()
  {
    return replyCode;
  }

  public String toString()
  {
    return getMessage()+" "+getReplyCode();
  }

}