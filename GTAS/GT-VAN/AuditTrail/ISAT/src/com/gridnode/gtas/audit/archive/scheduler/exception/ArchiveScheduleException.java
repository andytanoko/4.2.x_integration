/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveScheduleException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 12, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.scheduler.exception;

/**
 * This class will be thrown if we have error in performing ArchiveScheduler services.
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveScheduleException extends Exception
{
  public ArchiveScheduleException(String errReason)
  {
    super(errReason);
  }
  
  public ArchiveScheduleException(String errReason, Throwable tx)
  {
    super(errReason, tx);
  }
}
