/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventIdAlreadyRegisteredException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 14 2002    Goh Kan Mun             Created
 */

package com.gridnode.pdip.app.channel.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Signals that an event is already registered with the ChannelManager.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class EventIdAlreadyRegisteredException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5117863909794072935L;

	public EventIdAlreadyRegisteredException(String msg)
  {
    super(msg);
  }

  public EventIdAlreadyRegisteredException(String msg, Throwable t)
  {
    super(msg, t);
  }

  public EventIdAlreadyRegisteredException(Throwable t)
  {
    super(t);
  }

}