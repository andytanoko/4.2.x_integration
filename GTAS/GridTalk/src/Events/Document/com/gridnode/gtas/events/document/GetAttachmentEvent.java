/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAttachmentEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a Attachment based on
 * UID.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAttachmentEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2551002583088222929L;
	public static final String ATT_UID  = "Attachment UID";

  public GetAttachmentEvent(Long attUid)
  {
    setEventData(ATT_UID, attUid);
  }

  public Long getAttachmentUID()
  {
    return (Long)getEventData(ATT_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetAttachmentEvent";
  }

}