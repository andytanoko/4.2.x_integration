/* This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 24 2002    Jagadeesh              Created
 */

package com.gridnode.pdip.app.channel.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This exception is thrown by the services in ChannelManager.
 *
 * @author Jagadeesh.
 *
 * @version 2.0
 * @since 2.0I6.
 */

public class ChannelException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1122186253228669714L;

	public ChannelException(String message)
  {
    super(message);
  }

  public ChannelException(String message, Throwable tr)
  {
    super(message, tr);
  }

  public ChannelException(Throwable tr)
  {
    super(tr);
  }

}