/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CollateInfoException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.extraction.exception;

/**
 * This class will be thrown while we encountered exception during collating 
 * the necessary info from GT.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class AuditInfoCollatorException extends Exception
{
  public AuditInfoCollatorException(String errMsg)
  {
    super(errMsg);
  }
  
  public AuditInfoCollatorException(String errMsg, Throwable th)
  {
    super(errMsg, th);
  }
}
