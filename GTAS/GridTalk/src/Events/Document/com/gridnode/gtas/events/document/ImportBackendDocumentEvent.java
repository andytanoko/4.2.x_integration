/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportBackendDocumentEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 22 2002    Koh Han Sing        Created
 * Jun 20 2003    Koh Han Sing        Add in unique document id.
 * 9 Dec 2005	  SC                  Add new construct to support process instance id. 
 *                                    Add new method to get process instance id. Add process instance id constant.
 * Nov 10 2006    Tam Wei Xiang       Added tracingID. Require in Audit-Trail                                   
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.List;

/**
 * This Event class contains the data for the importing documents to the
 * server from the backend system.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ImportBackendDocumentEvent
  extends EventSupport
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 3670401540465983627L;
  public static final String SENDER_ID = "Sender ID";
  public static final String DOCUMENT_TYPE = "Document Type";
  public static final String IMPORT_FILES = "Import Files";
  public static final String RECIPIENTS = "Recipients";
  public static final String ATTACHMENTS = "Attachments";
  public static final String RN_PROFILE = "RnProfile";
  public static final String UNIQUE_DOC_ID = "Unique Document ID";
  public static final String PROCESS_INSTANCE_ID = "Process Instance ID";
  public static final String TRACING_ID = "Tracing ID";

  public ImportBackendDocumentEvent(String senderId,
                                    String documentType,
                                    List importFiles,
                                    List recipients,
                                    List attachments,
                                    String rnProfile,
                                    String uniqueDocId,
                                    String processInstanceId,
                                    String tracingID)
  {
    setEventData(SENDER_ID, senderId);
    setEventData(DOCUMENT_TYPE, documentType);
    setEventData(IMPORT_FILES, importFiles);
    setEventData(RECIPIENTS, recipients);
    setEventData(ATTACHMENTS, attachments);
    setEventData(RN_PROFILE, rnProfile);
    setEventData(UNIQUE_DOC_ID, uniqueDocId);
    setEventData(PROCESS_INSTANCE_ID, processInstanceId);
    setEventData(TRACING_ID, tracingID);
  }
  
  public ImportBackendDocumentEvent(String senderId,
                                    String documentType,
                                    List importFiles,
                                    List recipients,
                                    List attachments,
                                    String rnProfile,
                                    String uniqueDocId)
  {
    this(senderId, documentType, importFiles, recipients, attachments, rnProfile, uniqueDocId, "", "");
  }

  public ImportBackendDocumentEvent(String senderId,
                                    String documentType,
                                    List importFiles,
                                    List recipients,
                                    List attachments,
                                    String rnProfile)
  {
    this(senderId, documentType, importFiles, recipients, attachments,
         rnProfile, "");
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

  public String getRnProfile()
  {
    return (String)getEventData(RN_PROFILE);
  }

  public String getUniqueDocId()
  {
    return (String)getEventData(UNIQUE_DOC_ID);
  }
  
  public String getProcessInstanceId()
  {
    return (String)getEventData(PROCESS_INSTANCE_ID);
  }
  
  public String getTracingID()
  {
    return (String)getEventData(TRACING_ID);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/ImportBackendDocumentEvent";
  }

}