/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSessionException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15 2002    Ooi Hui Linn        Created
 * Jun 04 2002    Neo Sok Lay         Extend from SystemException instead of
 *                                    BasicTypedException
 */
package com.gridnode.pdip.base.session.exceptions;

import com.gridnode.pdip.framework.exceptions.SystemException;

public class GetSessionException
  extends    SystemException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2181779522982651952L;

	public GetSessionException(Throwable ex)
  {
    super(ex);
  }

  public GetSessionException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}