/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateTriggerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 * Dec 12 2002    Daniel D'Cotta      Added triggerType and isRequest
 * Aug 07 2003    Koh Han Sing        Add isLocalPending
 * Oct 20 2003    Guo Jianyu          Add numOfRetries, retryInterval and channelUID
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the creation of new Trigger.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.0
 */
public class CreateTriggerEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6025236554451094016L;
	public static final String TRIGGER_LEVEL        = "Trigger Level";
  public static final String PARTNER_FUNCTION_ID  = "PartnerFunction Id";
  public static final String PROCESS_ID           = "Process Id";
  public static final String DOC_TYPE             = "Document Type";
  public static final String PARTNER_TYPE         = "Partner Type";
  public static final String PARTNER_GROUP        = "Partner Group";
  public static final String PARTNER_ID           = "Partner Id";
  public static final String TRIGGER_TYPE         = "Trigger Type";
  public static final String IS_REQUEST           = "Is Request";
  public static final String IS_LOCAL_PENDING     = "Is Local Pending";
  public static final String NUM_OF_RETRIES       = "Number of Retries";
  public static final String RETRY_INTERVAL       = "Retry Interval";
  public static final String CHANNEL_UID          = "Channel UID";

  public CreateTriggerEvent(Integer triggerLevel,
                            String partnerFunctionId,
                            String processId,
                            String docType,
                            String partnerType,
                            String partnerGroup,
                            String partnerId,
                            Integer triggerType,
                            Boolean isRequest,
                            Boolean isLocalPending,
                            Integer numOfRetries,
                            Integer retryInterval,
                            Long channelUID)
  {
    setEventData(TRIGGER_LEVEL, triggerLevel);
    setEventData(PARTNER_FUNCTION_ID, partnerFunctionId);
    setEventData(PROCESS_ID, processId);
    setEventData(DOC_TYPE, docType);
    setEventData(PARTNER_TYPE, partnerType);
    setEventData(PARTNER_GROUP, partnerGroup);
    setEventData(PARTNER_ID, partnerId);
    setEventData(TRIGGER_TYPE, triggerType);
    setEventData(IS_REQUEST, isRequest);
    setEventData(IS_LOCAL_PENDING, isLocalPending);
    setEventData(NUM_OF_RETRIES, numOfRetries);
    setEventData(RETRY_INTERVAL, retryInterval);
    setEventData(CHANNEL_UID, channelUID);
  }

  public Integer getTriggerLevel()
  {
    return (Integer)getEventData(TRIGGER_LEVEL);
  }

  public String getPartnerFunctionId()
  {
    return (String)getEventData(PARTNER_FUNCTION_ID);
  }

  public String getProcessId()
  {
    return (String)getEventData(PROCESS_ID);
  }

  public String getDocType()
  {
    return (String)getEventData(DOC_TYPE);
  }

  public String getPartnerType()
  {
    return (String)getEventData(PARTNER_TYPE);
  }

  public String getPartnerGroup()
  {
    return (String)getEventData(PARTNER_GROUP);
  }

  public String getPartnerId()
  {
    return (String)getEventData(PARTNER_ID);
  }

  public Integer getTriggerType()
  {
    return (Integer)getEventData(TRIGGER_TYPE);
  }

  public Boolean getIsRequest()
  {
    return (Boolean)getEventData(IS_REQUEST);
  }

  public Boolean getIsLocalPending()
  {
    return (Boolean)getEventData(IS_LOCAL_PENDING);
  }

  public Integer getNumOfRetries()
  {
    return (Integer)getEventData(NUM_OF_RETRIES);
  }

  public Integer getRetryInterval()
  {
    return (Integer)getEventData(RETRY_INTERVAL);
  }

  public Long getChannelUID()
  {
    return (Long)getEventData(CHANNEL_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateTriggerEvent";
  }

}