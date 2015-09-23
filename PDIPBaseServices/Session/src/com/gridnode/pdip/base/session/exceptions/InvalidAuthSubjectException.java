/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidAuthSubjectException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 16 2002    Ooi Hui Linn        Created
 * Jun 04 2002    Neo Sok Lay         Extend from ApplicationException instead
 *                                    of BasicTypedException
 */
package com.gridnode.pdip.base.session.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class InvalidAuthSubjectException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2492003074093078909L;

	public InvalidAuthSubjectException(String reason)
  {
    super(reason);
  }

  public InvalidAuthSubjectException(Throwable ex)
  {
    super(ex);
  }

  public InvalidAuthSubjectException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}