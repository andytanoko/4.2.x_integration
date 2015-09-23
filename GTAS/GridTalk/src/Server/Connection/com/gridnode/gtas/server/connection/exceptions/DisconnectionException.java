/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DisconnectionException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 28 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when a the Connection Service has unresolvable problem with the
 * Disconnection from GridMaster.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class DisconnectionException
  extends    ApplicationException
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7563649764090616463L;
	private static final String MSG_PREFIX = "Disconnection Error! - ";

  public DisconnectionException(String msg)
  {
    super(MSG_PREFIX+msg);
  }

  public DisconnectionException(String msg, Throwable t)
  {
    super(MSG_PREFIX+msg, t);
  }
}