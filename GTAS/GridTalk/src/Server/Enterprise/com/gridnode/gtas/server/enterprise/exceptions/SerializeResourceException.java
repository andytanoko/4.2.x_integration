/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SerializeResourceException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class SerializeResourceException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5739766554542552161L;

	public SerializeResourceException(String reason)
  {
    super(reason);
  }

  public SerializeResourceException(Throwable ex)
  {
    super(ex);
  }

  public SerializeResourceException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}