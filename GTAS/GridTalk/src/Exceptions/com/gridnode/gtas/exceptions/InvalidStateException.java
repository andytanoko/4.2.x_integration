/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidStateException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 16 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.exceptions;

import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This exception indicates the current state in the StateMachine is incorrect,
 * or a value to be set into the StateMachine is not acceptable.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class InvalidStateException
  extends EventException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -675695964329593836L;

	public InvalidStateException(String msg)
  {
    super(msg);
  }
}