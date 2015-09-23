/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ManualSendDocumentEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.List;

/**
 * This Event class contains the data for executing a manual send.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ManualSendDocumentEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9176293142625995418L;
	public static final String G_DOC_UID = "GridDocument UID";

  public ManualSendDocumentEvent(List gDocUIDs)
  {
    setEventData(G_DOC_UID, gDocUIDs);
  }

  public List getGridDocumentUIDs()
  {
    return (List)getEventData(G_DOC_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ManualSendDocumentEvent";
  }

}