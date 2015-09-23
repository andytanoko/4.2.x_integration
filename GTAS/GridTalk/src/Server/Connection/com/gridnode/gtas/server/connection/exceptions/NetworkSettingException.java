/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NetworkSettingException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when a the Connection Service has unresolvable problem with the
 * Network Setting.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class NetworkSettingException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2420756752874969024L;
	private static final String MSG_PREFIX = "Network Setting Error! - ";

  public NetworkSettingException(String msg)
  {
    super(MSG_PREFIX+msg);
  }

  public NetworkSettingException(String msg, Throwable t)
  {
    super(MSG_PREFIX+msg, t);
  }
}