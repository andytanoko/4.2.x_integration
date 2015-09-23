/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggeringException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 03 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.alert.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This Exception indicates an application error occurs while trying to
 * trigger an alert.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class AlertTriggeringException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -516383188963681764L;

	public AlertTriggeringException(String reason)
  {
    super(reason);
  }

  public AlertTriggeringException(Throwable ex)
  {
    super(ex);
  }

  public AlertTriggeringException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}