/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NodeLockException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 03 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when the license key file nodelock information does not match the
 * machine used for running gtas server.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class NodeLockException
  extends    ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6686966330076290502L;

	public NodeLockException()
  {
    super("The license cannot be use on this machine");
  }

}