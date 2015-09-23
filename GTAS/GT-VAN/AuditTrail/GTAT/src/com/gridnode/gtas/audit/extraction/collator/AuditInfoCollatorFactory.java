/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditInfoCollatorFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.extraction.collator;

import com.gridnode.gtas.server.document.notification.DocumentTransactionNotification;
import com.gridnode.pdip.app.workflow.notification.ProcessTransactionNotification;
import com.gridnode.pdip.framework.notification.AbstractNotification;
import com.gridnode.pdip.framework.notification.DocumentFlowNotification;

/**
 * Return a concrete AbstractInfoCollator based on the concreate type of AbstractNotification. 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class AuditInfoCollatorFactory
{
  private static final AuditInfoCollatorFactory _factory = new AuditInfoCollatorFactory();
  private static final String CLASS_NAME = "AuditInfoCollatorFactory";
  
  private AuditInfoCollatorFactory()
  {
    
  }
  
  public static AuditInfoCollatorFactory getInstance()
  {
    return _factory;
  }
  
  public AbstractInfoCollator getAuditInfoCollator(AbstractNotification notification)
  {
    if(notification instanceof DocumentTransactionNotification)
    {
      return DocInfoCollator.getInstance();
    }
    else if(notification instanceof DocumentFlowNotification)
    {
      return EventInfoCollator.getInstance();
    }
    else if(notification instanceof ProcessTransactionNotification)
    {
      return ProcessInfoCollator.getInstance();
    }
    else
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".getAuditInfoCollator]The given notification is not supported ! "+notification.toString());
    }
  }
}
