/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when a the Connection Service has unresolvable problem with the
 * Connection Setup.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionSetupException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5167560057184407169L;

	public static final int CODE_INVALID_PASSWORD = 1;

  private static final String MSG_PREFIX = "Connectio Setup Error! - ";

  private int _errorCode;

  public ConnectionSetupException(String msg)
  {
    super(MSG_PREFIX+msg);
  }

  public ConnectionSetupException(String msg, Throwable t)
  {
    super(MSG_PREFIX+msg, t);
  }

  public ConnectionSetupException(Throwable t)
  {
    super(MSG_PREFIX, t);
  }

  public int getErrorCode()
  {
    return _errorCode;
  }

  public static ConnectionSetupException invalidSecurityPassword(Throwable t)
  {
    ConnectionSetupException ex = new ConnectionSetupException(t);
    ex._errorCode = CODE_INVALID_PASSWORD;

    return ex;
  }

}