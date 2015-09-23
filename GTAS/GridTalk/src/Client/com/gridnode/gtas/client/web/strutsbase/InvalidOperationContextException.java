/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidOperationContextException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

 
public class InvalidOperationContextException extends OperationException
{
  String _id;

  public InvalidOperationContextException(String id)
  {
    super("Operation " + id + " does not exist or has been completed");
    _id = id;
  }

  public String getInvalidId()
  {
    return _id;
  }
}