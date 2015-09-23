/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TokenManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 25 2002    Koh Han Sing        Created
 *
 */
package com.gridnode.gtas.server.document.helpers;


public class Token
{
  private long _id;
  private boolean _issued = false;
  private long _timeIssued;

  public Token(long id)
  {
    _id = id;
  }

  public long getId()
  {
    return _id;
  }

  public boolean isIssued()
  {
    return _issued;
  }

  public void issue()
  {
    _issued = true;
    _timeIssued = System.currentTimeMillis();
  }

  public long getTimeIssued()
  {
    return _timeIssued;
  }

  public void returned()
  {
    _issued = false;
  }
}