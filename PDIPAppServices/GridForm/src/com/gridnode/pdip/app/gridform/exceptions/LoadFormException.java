/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoadFormException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.exceptions;

import com.gridnode.pdip.framework.exceptions.NestingException;

/**
 * Exception to generate when loading of form failed.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class LoadFormException extends NestingException
{
  public LoadFormException(String cause)
  {
    super(cause);
  }

  public LoadFormException(Throwable ex)
  {
    super(ex);
  }

  public LoadFormException(String cause, Throwable ex)
  {
    super(cause, ex);
  }
}