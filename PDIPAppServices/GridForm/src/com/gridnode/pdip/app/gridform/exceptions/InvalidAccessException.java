/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidAccessException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.exceptions;

import com.gridnode.pdip.framework.exceptions.NestingException;

/**
 * Exception to generate when access to the data depository failed.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class InvalidAccessException extends NestingException
{
  public InvalidAccessException(String cause)
  {
    super(cause);
  }

  public InvalidAccessException(Throwable ex)
  {
    super(ex);
  }

  public InvalidAccessException(String cause, Throwable ex)
  {
    super(cause, ex);
  }
}