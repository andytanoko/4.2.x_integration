/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendRNDocNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 13 2002    Koh Han Sing        Created
 * Jan 29 2004    Neo Sok Lay         Add isResumeSend flag.
 */
package com.gridnode.gtas.server.notify;

/**
 * Notification message for the sending of a RosettaNet document.
 *
 */
public class SendRNDocNotification
  extends    AbstractNotification
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2746327520466957840L;
	private Object[] _gdocs;
  private String   _processDefId;
  private Boolean  _isRequest;
  private boolean _isResumeSend;

  public SendRNDocNotification(Object[] gdocs, String processDefId,
                               Boolean isRequest, boolean isResumeSend)
  {
    _gdocs = gdocs;
    _processDefId = processDefId;
    _isRequest = isRequest;
    _isResumeSend = isResumeSend;
  }

  public Object[] getGdocs()
  {
    return _gdocs;
  }

  public String getProcessDefId()
  {
    return _processDefId;
  }

  public Boolean getIsRequest()
  {
    return _isRequest;
  }

  public boolean isResumeSend()
  {
    return _isResumeSend;
  }
  
  public String getNotificationID()
  {
    return "SendRNDoc";
  }

//  public String toString()
//  {
//    StringBuffer buff = new StringBuffer(getNotificationID());
//    buff.append(" - Node[").append(getPartnerNode()).append("], State[").append(
//      getState()).append("]");
//
//    return buff.toString();
//  }
}