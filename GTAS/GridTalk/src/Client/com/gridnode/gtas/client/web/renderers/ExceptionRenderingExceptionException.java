/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RenderingException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

 
public class ExceptionRenderingExceptionException extends RenderingException
{
  Throwable _original = null;

  public ExceptionRenderingExceptionException(String message, Throwable nestedException, Throwable original)
  {
    super(message, nestedException);
    _original = original;
  }

  public Throwable getOriginal()
  {
    return _original;
  }
}