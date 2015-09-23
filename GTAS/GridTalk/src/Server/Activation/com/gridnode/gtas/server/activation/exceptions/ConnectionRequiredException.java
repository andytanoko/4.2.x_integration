/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionRequiredException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 18 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.exceptions;

/**
 * This exception indicates Connection to GridMaster is required for an
 * operation to be performed.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionRequiredException
  extends    GridNodeActivationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1732060966565430213L;

	public ConnectionRequiredException(String msg)
  {
    super(msg);
  }
}