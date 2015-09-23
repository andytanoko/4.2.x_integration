/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReprocessActivityException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 1, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.reprocess.exception;

/**
 * This class will be thrown while we encounter problem in triggering the reprocess operation.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ReprocessActivityException extends Exception
{
  public ReprocessActivityException(String errReason)
  {
    super(errReason);
  }
  
  public ReprocessActivityException(String errReason, Throwable th)
  {
    super(errReason, th);
  }
}
