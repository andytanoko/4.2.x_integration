/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITransactionHeaderConstants.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 8, 2006    i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishc;

/**
 * @author i00107
 * This interface defines the constants for the HTTP headers
 * required to describe the incoming transaction.
 */
public interface ITransactionHeaderConstants
{
  static final String SENDER_IDENTITY = "sender"; //map to partner id
  static final String RECEIVER_IDENTITY = "recipient"; //map to cust be id
  static final String DOCUMENT_TYPE = "doctype";
  static final String FILE_NAME = "filename";
}
