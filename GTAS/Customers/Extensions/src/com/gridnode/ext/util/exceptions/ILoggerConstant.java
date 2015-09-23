/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILoggerConstant.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 19, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.ext.util.exceptions;

/**
 * The following capture the error code needed for the GT extension component
 * 
 * @author Tam Wei Xiang
 * @version GT 4.1.3 (GTVAN)
 */
public interface ILoggerConstant
{
  public static final String LOG_TYPE = "GN.EXTENSION";
  
  public static final String LOG_ERROR_PREFIX = "006.001.";
  
  public static final String FTP_PUSH_FAILED = LOG_ERROR_PREFIX + "050";
  public static final String FTP_EMAIL_FAILED = LOG_ERROR_PREFIX + "051";
  public static final String FTP_PULL_FAILED = LOG_ERROR_PREFIX + "052";
  public static final String FTP_TXMR_EVENT_TRIGGER_FAILED = LOG_ERROR_PREFIX + "053";
}
