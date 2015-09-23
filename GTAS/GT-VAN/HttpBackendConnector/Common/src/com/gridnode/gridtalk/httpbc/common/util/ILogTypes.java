/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILogTypes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 9, 2006    i00107              Created
 * Mar 07 2007    Alain Ah Ming       Changed category values to upper case
 * Apr 26 2007    i00107              Added GTHB log type.
 */

package com.gridnode.gridtalk.httpbc.common.util;

/**
 * @author i00107
 * This interface defines the Logger types for HTTPBC module.
 */
public interface ILogTypes
{
  /**
   * GN.HTTPBC.GTHB
   */
  public static final String TYPE_HTTPBC_GTHB = "GN.HTTPBC.GTHB";
  
  /**
   * GN.HTTPBC.ISHB
   */
  public static final String TYPE_HTTPBC_ISHB = "GN.HTTPBC.ISHB";
  
  /**
   * GN.HTTPBC.ISHC
   */
  public static final String TYPE_HTTPBC_ISHC = "GN.HTTPBC.ISHC";
  
  /**
   * GN.HTTPBC.COMMON
   */
  public static final String TYPE_HTTPBC_COMMON = "GN.HTTPBC.COMMON";
  
  /**
   * GN.HTTPBC.UTIL
   */
  public static final String TYPE_HTTPBC_UTIL = "GN.HTTPBC.UTIL";
}
