/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IncompleteFieldsException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-21     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

/**
 * Thrown when an entity needs other fields initialised (for example: for load on demand fields)
 * but they arent, or their data is not valid.
 */
public class IncompleteFieldsException extends GTClientException
{
  IncompleteFieldsException(String message)
  {
    super(message);
  }

  /*public IncompleteFieldsException(String message, Throwable t)
  {
    super(message,t);
  }*/
}