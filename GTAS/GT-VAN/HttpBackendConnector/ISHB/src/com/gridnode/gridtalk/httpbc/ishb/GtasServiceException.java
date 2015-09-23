/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GtasServiceException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 09, 2007   i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb;

/**
 * @author i00107
 * Wraps exceptionx thrown when accessing Gtas.
 */
public class GtasServiceException extends Exception
{

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -6494875001056755386L;

  /**
   * @param arg0
   */
  public GtasServiceException(String arg0)
  {
    super(arg0);
  }

  /**
   * @param arg0
   * @param arg1
   */
  public GtasServiceException(String arg0, Throwable arg1)
  {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   */
  public GtasServiceException(Throwable arg0)
  {
    super(arg0);
  }

}
