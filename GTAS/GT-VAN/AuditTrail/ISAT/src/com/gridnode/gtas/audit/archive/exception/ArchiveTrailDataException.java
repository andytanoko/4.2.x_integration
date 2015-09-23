/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveTrailDataException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 14, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.exception;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveTrailDataException extends Exception
{
  public ArchiveTrailDataException(String errMsg)
  {
    super(errMsg);
  }
  
  public ArchiveTrailDataException(String msg, Throwable t)
  {
    super(msg, t);
  }
}
