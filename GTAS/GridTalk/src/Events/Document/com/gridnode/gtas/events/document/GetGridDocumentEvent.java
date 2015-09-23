/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridDocumentEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a GridDocument based on
 * UID.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetGridDocumentEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4251118881900889193L;
	public static final String G_DOC_UID  = "GridDocument UID";

  public GetGridDocumentEvent(Long gDocUID)
  {
    setEventData(G_DOC_UID, gDocUID);
  }

  public Long getGridDocumentUID()
  {
    return (Long)getEventData(G_DOC_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetGridDocumentEvent";
  }

}