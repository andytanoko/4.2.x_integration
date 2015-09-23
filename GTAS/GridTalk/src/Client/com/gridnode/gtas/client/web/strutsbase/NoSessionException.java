/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NoSessionException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

public class NoSessionException extends java.lang.RuntimeException
{
  public NoSessionException(String message)
  {
    super(message);
  }
}
