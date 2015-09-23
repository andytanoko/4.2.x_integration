/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveDelegateException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.exception;

/**
 * This class will be thrown while we encounter error in delegating the archive request to GT.
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveDelegateException extends Exception
{
  public ArchiveDelegateException(String msg)
  {
    super(msg);
  }
  
  public ArchiveDelegateException(String msg, Throwable th)
  {
    super(msg, th);
  }
}
