/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendFalureNotificationEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 19, 2006   i00107              Created
 */

package com.gridnode.gtas.events.rnif;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * @author i00107
 * Event class for sending failure notification back to partner.
 */
public class SendFailureNotificationEvent extends EventSupport
{
  /**
   * Serial version UID
   */
  private static final long serialVersionUID = -3813419869086415670L;
  
  private static final String _REQUEST_MSG = "REQUEST_MSG";
  private static final String _PROCESS_INSTANCEID = "PROCESS_INSTANCE_ID";
  private static final String _REASON_STR = "FAILURE_REASON";
  private static final String _SENDER_DUNS = "SENDER_DUNS";
  /**
   * 
   */
  public SendFailureNotificationEvent(String processInstanceId, String senderDUNS, boolean isRequestMsg, String reasonStr)
    throws EventException
  {
    setEventData(_REQUEST_MSG, new Boolean(isRequestMsg));
    checkSetString(_PROCESS_INSTANCEID, processInstanceId);
    checkSetString(_SENDER_DUNS, senderDUNS);
    checkSetString(_REASON_STR, reasonStr);
  }

  public boolean getIsRequestMsg()
  {
    return ((Boolean)getEventData(_REQUEST_MSG)).booleanValue();
  }
  
  public String getProcessInstanceId()
  {
    return (String)getEventData(_PROCESS_INSTANCEID);
  }
 
  public String getSenderDUNS()
  {
    return (String)getEventData(_SENDER_DUNS);
  }
  
  public String getReasonStr()
  {
    return (String)getEventData(_REASON_STR);
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.EventSupport#getEventName()
   */
  @Override
  public String getEventName()
  {
    return "java:comp/env/param/event/SendFailureNotificationEvent";
  }
  
  
}
