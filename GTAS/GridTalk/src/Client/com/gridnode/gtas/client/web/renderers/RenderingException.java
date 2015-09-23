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
 * 2002-05-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import com.gridnode.gtas.client.GTClientException;

public class RenderingException extends GTClientException
{

  public RenderingException()
  {
    super();
  }

  public RenderingException(String message)
  {
    super(message);
  }

  public RenderingException(Throwable nestedException)
  {
    super(nestedException);
  }

  public RenderingException(String message, Throwable nestedException)
  {
    super(message, nestedException);
  }
}