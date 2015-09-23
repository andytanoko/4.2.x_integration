/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportDocumentEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 16 2002    Koh Han Sing        Created
 * Nov 25 2002    Koh Han Sing        Add in attachments
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.List;

/**
 * This Event class contains the data for the importing documents to the
 * server.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ImportDocumentEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 436405735393454668L;
	public static final String SENDER_ID = "Sender ID";
  public static final String DOCUMENT_TYPE = "Document Type";
  public static final String IMPORT_FILES = "Import Files";
  public static final String RECIPIENTS = "Recipients";
  public static final String ATTACHMENTS = "Attachments";

  public ImportDocumentEvent(String senderId,
                             String documentType,
                             List importFiles,
                             List recipients,
                             List attachments)
  {
    setEventData(SENDER_ID, senderId);
    setEventData(DOCUMENT_TYPE, documentType);
    setEventData(IMPORT_FILES, importFiles);
    setEventData(RECIPIENTS, recipients);
    setEventData(ATTACHMENTS, attachments);
  }

  public String getSenderId()
  {
    return (String)getEventData(SENDER_ID);
  }

  public String getDocumentType()
  {
    return (String)getEventData(DOCUMENT_TYPE);
  }

  public List getImportFiles()
  {
    return (List)getEventData(IMPORT_FILES);
  }

  public List getRecipients()
  {
    return (List)getEventData(RECIPIENTS);
  }

  public List getAttachments()
  {
    return (List)getEventData(ATTACHMENTS);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ImportDocumentEvent";
  }

}