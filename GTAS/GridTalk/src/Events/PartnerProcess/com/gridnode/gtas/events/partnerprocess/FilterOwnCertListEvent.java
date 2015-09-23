/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:FilterOwnCertListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 13 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
/**
 * This Event class contains the data for retrieve a list of Certificate(s)
 * that belong to this enterprise.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class FilterOwnCertListEvent
       extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 112646277949406256L;

	public FilterOwnCertListEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/FilterOwnCertListEvent";
  }

}