/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetServerWatchlistEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 02 2002    Neo Sok Lay         Created
 * Nov 06 2002    Neo Sok Lay         Renamed to ServerWatchlist instead
 *                                    of PartnerWatchlist.
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve the Server watchlist.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetServerWatchlistEvent
  extends    EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4191376328115217759L;

	public GetServerWatchlistEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetServerWatchlistEvent";
  }

}