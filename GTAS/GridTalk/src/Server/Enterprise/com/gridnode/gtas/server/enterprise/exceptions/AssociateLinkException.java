/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AssociateLinkExceptionException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class AssociateLinkException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6959139612644511150L;

	public AssociateLinkException(String reason)
  {
    super(reason);
  }

  public AssociateLinkException(Throwable ex)
  {
    super(ex);
  }

  public AssociateLinkException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}