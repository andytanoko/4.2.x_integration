/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SenderException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 10, 2006   i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

/**
 * @author i00107
 * This exception is thrown for problems related to Http connection.
 */
public class SenderException extends Exception
{

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 5665555784573116019L;

  /**
   * @param message
   */
  public SenderException(String message)
  {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public SenderException(String message, Throwable cause)
  {
    super(message, cause);
  }

}
