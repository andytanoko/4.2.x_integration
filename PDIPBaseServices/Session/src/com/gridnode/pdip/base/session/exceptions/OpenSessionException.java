/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OpenSessionException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 14 2002    Ooi Hui Linn        Created
 * Jun 04 2002    Neo Sok Lay         Extend from ApplicationException instead
 *                                    of BasicTypedException.
 */
package com.gridnode.pdip.base.session.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class OpenSessionException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2922414191235119853L;

	public OpenSessionException(String reason)
  {
    super(reason);
  }

  public OpenSessionException(Throwable ex)
  {
    super(ex);
  }

  public OpenSessionException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}