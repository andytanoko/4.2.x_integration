/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSFailureException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 24, 2008   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.exceptions;

/**
 * It signal that the underlying JMS service is encoutering some problem. And the 
 * Top Handler eg GWFProcessMDBean should rollback all transaction, and request
 * a redelivered from JMS Server.
 * 
 * Use it if only a rollback of the all transaction and redelivered of JMS is required.
 * 
 * @author Tam Wei Xiang
 * @version GT 4.1.3 (GTVAN)
 * @since GT4.1.3 (GTVAN)
 */
public class JMSFailureException extends SystemException
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 4869114339522932171L;

  public JMSFailureException(String msg, Throwable th)
  {
    super(msg, th);
  }
  
  public JMSFailureException(Throwable th)
  {
    super(th);
  }
}
