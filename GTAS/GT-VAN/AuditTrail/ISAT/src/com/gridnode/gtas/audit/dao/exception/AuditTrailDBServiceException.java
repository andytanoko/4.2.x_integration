/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailPersistException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 29, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.dao.exception;

/**
 * This class will be thrown while we encounter error during performing database services eg
 * CRUD operations
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class AuditTrailDBServiceException extends Exception
{
  public AuditTrailDBServiceException(String errMsg)
  {
    super(errMsg);
  }
  
  public AuditTrailDBServiceException(String errMsg, Throwable th)
  {
    super(errMsg, th);
  }
}
