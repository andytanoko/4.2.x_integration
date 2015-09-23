/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailTrackingException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 24, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.exception;

/**
 * This class will be thrown while we have problem in consolidating the 
 * necessary info from the Remote-Event queue or persisting the tracking info.
 *  
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class AuditTrailTrackingException extends Exception
{
  public AuditTrailTrackingException(String errMsg)
  {
    super(errMsg);
  }
  
  public AuditTrailTrackingException(String errMsg, Throwable th)
  {
    super(errMsg, th);
  }
}
