/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateDocumentTypeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the creation of new DocumentType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateDocumentTypeEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 513066174735249857L;
	public static final String DOCUMENT_TYPE_NAME = "DocumentType Name";
  public static final String DOCUMENT_TYPE_DESC = "DocumentType Desc";

  public CreateDocumentTypeEvent(String docTypeName, String docTypeDesc)
  {
    setEventData(DOCUMENT_TYPE_NAME, docTypeName);
    setEventData(DOCUMENT_TYPE_DESC, docTypeDesc);
  }

  public String getdocTypeName()
  {
    return (String)getEventData(DOCUMENT_TYPE_NAME);
  }

  public String getdocTypeDesc()
  {
    return (String)getEventData(DOCUMENT_TYPE_DESC);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateDocumentTypeEvent";
  }

}