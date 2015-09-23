/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTSessionCreationException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-10-24     Andrew Hill         Modified type constants
 * 2003-01-21     Andrew Hill         Package protect constructors
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public class SessionCreationException extends GTClientException
{
  public static final int GURU_MEDITATION = 0;
  public static final int UNKNOWN_SESSION_TYPE = 1;
  public static final int NO_CONTEXT = 2;
  public static final int SERVICE_LOOKUP_FAILURE = 3;

  private int _type;

  SessionCreationException(int type, String message) //20030121AH - packageprotected
  {
    super(message);
    _type = type;
  }

  SessionCreationException(int type, String message, Throwable t) //20030121AH - packageprotected
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