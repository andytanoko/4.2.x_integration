/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OperationException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-13     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import com.gridnode.gtas.client.GTClientException;

public class OperationException extends GTClientException
{
  public OperationException()
  {
    super();
  }

  public OperationException(String message)
  {
    super(message);
  }

  public OperationException(Throwable nestedException)
  {
    super(nestedException);
  }

  public OperationException(String message, Throwable nestedException)
  {
    super(message, nestedException);
  }
}