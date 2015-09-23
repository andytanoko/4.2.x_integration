/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProductRegistrationException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 18 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when a registration service method is invoked but the current
 * registration state is inconsistent.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ProductRegistrationException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4307658111274285206L;
	private static final String MSG_PREFIX = "Product Registration Error! - ";

  public ProductRegistrationException(String msg)
  {
    super(MSG_PREFIX+msg);
  }

  public ProductRegistrationException(String msg, Throwable t)
  {
    super(MSG_PREFIX+msg, t);
  }
}