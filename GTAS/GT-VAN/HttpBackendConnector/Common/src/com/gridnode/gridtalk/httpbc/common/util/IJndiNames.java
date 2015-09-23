/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJndiNames.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 16, 2007   i00107              Created
 */

package com.gridnode.gridtalk.httpbc.common.util;

/**
 * @author i00107
 * This interface defines the JNDI names for EJBs in the HTTPBC component.
 */
public interface IJndiNames
{
  /**
   * JNDI name for TransactionHandlerBean
   */
  static final String TRANSACTION_HANDLER = "java:comp/env/ejb/TransactionHandlerBean";
  
  /**
   * JNDI name for TxDeliveryTimerBean
   */
  static final String TX_DELIVERY_TIMER = "java:comp/env/ejb/TxDeliveryTimer";
}
