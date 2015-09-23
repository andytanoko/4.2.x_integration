/* This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FlowControlException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 24 2002    Jagadeesh              Created
 */

package com.gridnode.pdip.app.channel.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class FlowControlException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6679857172182677325L;

	public FlowControlException(String message)
  {
    super(message);
  }

  public FlowControlException(String message, Throwable th)
  {
    super(message, th);
  }

  public FlowControlException(Throwable th)
  {
    super(th);
  }

}
