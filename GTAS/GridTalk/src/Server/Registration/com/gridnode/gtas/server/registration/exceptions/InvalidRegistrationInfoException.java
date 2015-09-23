/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidRegistrationInfoException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when error initializing the RegistrationInfo.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class InvalidRegistrationInfoException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4977970447682131741L;
	private static final String MSG_PREFIX = "Invalid Registration Info! - ";

  public InvalidRegistrationInfoException(String msg)
  {
    super(MSG_PREFIX+msg);
  }

  public InvalidRegistrationInfoException(String msg, Throwable t)
  {
    super(MSG_PREFIX+msg, t);
  }
}