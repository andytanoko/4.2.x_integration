/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTLoginException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2003-04-23     Andrew Hill         Added NO_PRIVATE_CERT_PASSWORD type
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public class LoginException extends GTClientException
{
  public static final int SERVER_ERROR  = 1;
  public static final int INVALID_LOGIN = 2;
  public static final int NO_PRIVATE_CERT_PASSWORD = 3; //20030423AH

  private int _type;

  public LoginException(int type, String message)
  {
    super(message);
    _type = type;
  }

  public LoginException(int type, String message, Throwable t)
  {
    super(message, t);
    _type = type;
  }

  public int getType()
  {
    return _type;
  }

  protected void setType(int type)
  {
    _type = type;
  }
}