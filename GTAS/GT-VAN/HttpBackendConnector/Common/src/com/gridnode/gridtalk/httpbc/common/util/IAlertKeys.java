/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14, 2006   i00107              Created
 */

package com.gridnode.gridtalk.httpbc.common.util;

/**
 * @author i00107
 * This interface defines the keys for the alerts that may be raised
 * by the HTTPBC module.
 */
public interface IAlertKeys
{
  static final String UNEXPECTED_SYSTEM_ERROR = "unexpected.system.error";
 
  static final String TOO_MANY_FAILED_ATTEMPTS = "too.many.failed.attempts";
}
