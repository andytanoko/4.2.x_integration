/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConstantValue.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 29, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.gridtalk.httpbc.common.util;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IConstantValue
{
  public static final String FAILED_JMS_CAT = "HC";
  
  //#1105 - lock name
  public static final String INCOMING_TRANSACTION_LOCK = "incomingTransLock";
  public static final String OUTGOING_TRANSACTION_LOCK = "outgoingTransLock";
}
