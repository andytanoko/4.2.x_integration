/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-18     Andrew Hill         Created
 * 2003-01-17     Andrew Hill         getInstance() to allow sharing one token
 */
package com.gridnode.gtas.client.ctrl;

class UnloadedFieldToken
{
  private static UnloadedFieldToken _instance = null;

  UnloadedFieldToken()
  {
  }

  synchronized static UnloadedFieldToken getInstance()
  {
    if(_instance == null) _instance = new UnloadedFieldToken();
    return _instance;
  }

  public String toString()
  {
    return "[UNLOADED_FIELD]";
  }
}