/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransactionHandlingException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 7, 2006    i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb;

/**
 * @author i00107
 * This exception is thrown while handling transactions, e.g. persistency, retrieval.
 */
public class TransactionHandlingException extends Exception
{
  
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -6933124455703414194L;

  public TransactionHandlingException(String msg)
  {
    super(msg);
  }
  
  public TransactionHandlingException(String msg, Throwable t)
  {
    super(msg, t);
  }

}
