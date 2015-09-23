/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTClientExceptiom.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-04-??     Andrew Hill         Created
 * 2002-10-25     Andrew Hill         getRootException()
 */
package com.gridnode.gtas.client;

import java.io.*;

public class GTClientException extends Exception
{
  private Throwable _nestedException;
  private Object _extraData;

  /**
  * Constructs an GTException with no specified detail message.
  */
  public GTClientException()
  {
    super();
    _nestedException = null;
  }

  /**
  * Constructs an Exception with the specified detail message.
  * @param message the detail message.
  */
  public GTClientException(String message)
  {
    super(message);
    _nestedException = null;
  }

  /**
  * Constructs an Exception
  * @param message the detail message.
  */
  public GTClientException(Throwable nestedException)
  {
    super();
    _nestedException = nestedException;
  }

  /**
  * Constructs an Exception
  * @param message the detail message.
  * @param nestedException the root cause exception
  */
  public GTClientException(String message, Throwable nestedException)
  {
    super(message);
    _nestedException = nestedException;
  }

  public Throwable getNestedException()
  {
    return _nestedException;
  }

  public Throwable getRootException()
  {
    if(_nestedException == null)
    {
      return this;
    }
    else if(_nestedException instanceof GTClientException)
    {
      return ((GTClientException)_nestedException).getRootException();
    }
    else
    {
      return _nestedException;
    }
  }

  public void setExtraData(Object data)
  {
    _extraData = data;
  }

  public Object getExtraData()
  {
    return _extraData;
  }

  public void printStackTrace()
  {
    super.printStackTrace();
    if(_nestedException != null)
      _nestedException.printStackTrace();

  }

  public void printStackTrace(PrintStream stream)
  {
    super.printStackTrace(stream);
    if(_nestedException != null)
      _nestedException.printStackTrace(stream);

  }

  public void printStackTrace(PrintWriter writer)
  {
    super.printStackTrace(writer);
    if(_nestedException != null)
      _nestedException.printStackTrace(writer);
  }
}
