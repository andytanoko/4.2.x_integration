/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SynchronizeResourceException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class SynchronizeResourceException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3284490066930410557L;

	public SynchronizeResourceException(String reason)
  {
    super(reason);
  }

  public SynchronizeResourceException(Throwable ex)
  {
    super(ex);
  }

  public SynchronizeResourceException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}