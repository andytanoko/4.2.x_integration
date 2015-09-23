/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PostingException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class PostingException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6568932287360818653L;

	public PostingException(String reason)
  {
    super(reason);
  }

  public PostingException(Throwable ex)
  {
    super(ex);
  }

  public PostingException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}