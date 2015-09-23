/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetDocumentTypeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a DocumentType based on
 * UID.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetDocumentTypeEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 971188556048196345L;
	public static final String DOCUMENT_TYPE_UID  = "DocumentType UID";

  public GetDocumentTypeEvent(Long docTypeUID)
  {
    setEventData(DOCUMENT_TYPE_UID, docTypeUID);
  }

  public Long getDocumentTypeUID()
  {
    return (Long)getEventData(DOCUMENT_TYPE_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetDocumentTypeEvent";
  }

}