/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportBackendDocNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 22 2002    Koh Han Sing        Created
 * Jun 20 2003    Koh Han Sing        Add in unique document id
 * 9  Dec 2005     SC                  add process instance attribute.
 * 10 Nov 2006    Tam Wei Xiang       Added tracingID.
 * 15 Mar 2007    Neo Sok Lay         Allow backend import with login.
 */
package com.gridnode.gtas.server.notify;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Handle;

/**
 * Notification message for the importing of documents from backend.
 *
 */
public class ImportBackendDocNotification
  extends    AbstractNotification
{ 
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 280602573631941771L;
  private String    _bizEntId;
  private String    _docType;
  private ArrayList _inputFilenames;
  private ArrayList _partners;
  private ArrayList _attachmentFilenames;
  private String    _rnProfile;
  private String    _uniqueDocId;
  private Handle    _ejbClientCntHandle;
  private String    _processInstanceId;
  private String    _tracingID;
  
  //NSL20070315 for skipping login
  private String _userId;
  private String _userName;
  private String _sourcePathKey;
  private String _sourceSubPath;
  
  public ImportBackendDocNotification(String bizEntId,
                                      String docType,
                                      ArrayList inputFilenames,
                                      ArrayList partners,
                                      ArrayList attachmentFilenames,
                                      String rnProfile,
                                      String uniqueDocId,
                                      String processInstanceId,
	                                  Handle ejbClientCntHandle,
                                      String tracingID)
  {
    _bizEntId = bizEntId;
    _docType = docType;
    _inputFilenames = inputFilenames;
    _partners = partners;
    _attachmentFilenames = attachmentFilenames;
    _rnProfile = rnProfile;
    _uniqueDocId = uniqueDocId;
    _ejbClientCntHandle = ejbClientCntHandle;
    _processInstanceId = processInstanceId;
    _tracingID = tracingID;
  }

  public ImportBackendDocNotification(String userId, String userName, String senderEnterpriseId, String senderBizEntId, List recipients,
                                      String docType, List importFiles, List attachments, String sourcePathKey, String sourceSubPath,
                                      String rnProfile, String uniqueDocId, String processInstanceId, String tracingID)
  {
    this(senderBizEntId, docType, new ArrayList(importFiles), new ArrayList(recipients), new ArrayList(attachments), rnProfile, uniqueDocId,
         processInstanceId, null, tracingID);
    _userId = userId;
    _userName = userName;
    _sourcePathKey = sourcePathKey;
    _sourceSubPath = sourceSubPath;
  }
  
  public String getBizEntId()
  {
    return _bizEntId;
  }

  public String getDocType()
  {
    return _docType;
  }

  public ArrayList getInputFilenames()
  {
    return _inputFilenames;
  }

  public ArrayList getPartners()
  {
    return _partners;
  }

  public ArrayList getAttachmentFilenames()
  {
    return _attachmentFilenames;
  }

  public String getRnProfile()
  {
    return _rnProfile;
  }

  public String getUniqueDocId()
  {
    return _uniqueDocId;
  }

  public Handle getEjbClientCntHandle()
  {
    return _ejbClientCntHandle;
  }
  
  public String getProcessInstanceId()
  {
  	return _processInstanceId;
  }
  
  public String getTracingID()
  {
    return _tracingID;
  }
  
  public String getSourcePathKey()
  {
    return _sourcePathKey;
  }

  public String getSourceSubPath()
  {
    return _sourceSubPath;
  }

  public String getUserId()
  {
    return _userId;
  }

  public String getUserName()
  {
    return _userName;
  }

  public String getNotificationID()
  {
    return "ImportBackendDoc";
  }

}