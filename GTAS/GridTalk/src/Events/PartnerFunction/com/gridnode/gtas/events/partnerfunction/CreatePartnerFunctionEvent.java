/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreatePartnerFunctionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 10 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.partnerfunction;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.List;

/**
 * This Event class contains the data for the creation of new PartnerFunction.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class CreatePartnerFunctionEvent
  extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8075162302135072779L;
	public static final String PARTNER_FUNCTION_ID   = "PartnerFunction Id";
  public static final String PARTNER_FUNCTION_DESC = "PartnerFunction Desc";
  public static final String TRIGGER_ON            = "Trigger On";
  public static final String WORKFLOW_ACTIVITIES   = "Workflow Activities";

  public CreatePartnerFunctionEvent(String partnerFunctionId,
                                    String partnerFunctionDesc,
                                    Integer triggerOn,
                                    List workflowActivities)
  {
    setEventData(PARTNER_FUNCTION_ID, partnerFunctionId);
    setEventData(PARTNER_FUNCTION_DESC, partnerFunctionDesc);
    setEventData(TRIGGER_ON, triggerOn);
    setEventData(WORKFLOW_ACTIVITIES, workflowActivities);
  }

  public String getPartnerFunctionId()
  {
    return (String)getEventData(PARTNER_FUNCTION_ID);
  }

  public String getPartnerFunctionDesc()
  {
    return (String)getEventData(PARTNER_FUNCTION_DESC);
  }

  public Integer getTriggerOn()
  {
    return (Integer)getEventData(TRIGGER_ON);
  }

  public List getWorkflowActivities()
  {
    return (List)getEventData(WORKFLOW_ACTIVITIES);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreatePartnerFunctionEvent";
  }

}