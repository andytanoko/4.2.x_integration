/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerNotFoundException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 31 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class PartnerNotFoundException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8175414462500764110L;

	public PartnerNotFoundException(String message)
  {
    super(message);
  }

  public PartnerNotFoundException(Throwable ex)
  {
    super(ex);
  }

  public PartnerNotFoundException(String message, Throwable ex)
  {
    super(message, ex);
  }
}