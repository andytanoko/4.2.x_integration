/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidSecurityPasswordException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 13 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when Security Password validation fails.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class InvalidSecurityPasswordException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7352505829629667382L;

	/**
   * Thrown if the security password is incorrect.
   */
  public InvalidSecurityPasswordException()
  {
    super("Invalid Security password!");
  }

  /**
   * Thrown when exception occurs while validating the security password
   *
   * @param msg The error message.
   */
  public InvalidSecurityPasswordException(String msg)
  {
    super("Security Password validation fails due to " +msg);
  }
}