/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentFlow.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 26, 2006    Tam Wei Xiang       Created
 * Dec 26, 2006    Tam Wei Xiang       remove the DocumentFlow type. Use enum instead
 */
package com.gridnode.pdip.framework.notification;

/**
 * Defines the common instants that are used in DocumentFlowNotification.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IDocumentFlow
{ 
  //Msg Type: to indicate what is the TYPE of the msg(action or signal msg) that has gone 
              //through a particular type of document flow.
  public static final String MSG_TYPE_TRANSACTION = "trans"; 
  public static final String MSG_TYPE_SIGNAL = "sig";
  
}
