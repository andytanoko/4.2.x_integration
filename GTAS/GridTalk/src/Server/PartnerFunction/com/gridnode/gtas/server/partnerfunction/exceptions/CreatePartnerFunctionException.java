/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreatePartnerFunctionException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 25 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class CreatePartnerFunctionException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3135972219847681928L;

	public CreatePartnerFunctionException(String message)
  {
    super(message);
  }

  public CreatePartnerFunctionException(Throwable ex)
  {
    super(ex);
  }

  public CreatePartnerFunctionException(String message, Throwable ex)
  {
    super(message, ex);
  }
}