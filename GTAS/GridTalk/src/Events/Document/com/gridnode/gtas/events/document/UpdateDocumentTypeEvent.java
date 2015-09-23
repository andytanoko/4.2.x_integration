/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateDocumentTypeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for updating a DocumentType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateDocumentTypeEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6413958257338526245L;
	public static final String DOCUMENT_TYPE_UID      = "DocumentType UID";
  public static final String UPD_DOCUMENT_TYPE_DESC = "Updated DocumentType Desc";

  public UpdateDocumentTypeEvent(Long docTypeUID, String docTypeDesc)
  {
    setEventData(DOCUMENT_TYPE_UID, docTypeUID);
    setEventData(UPD_DOCUMENT_TYPE_DESC, docTypeDesc);
  }

  public Long getDocumentTypeUID()
  {
    return (Long)getEventData(DOCUMENT_TYPE_UID);
  }

  public String getUpdDocumentTypeDesc()
  {
    return (String)getEventData(UPD_DOCUMENT_TYPE_DESC);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateDocumentTypeEvent";
  }

}