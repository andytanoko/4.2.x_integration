/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetHousekeepingInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 7, 2003      Mahesh         Created
 */ 
package com.gridnode.gtas.server.housekeeping.actions;
import java.util.Map;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.housekeeping.HousekeepingService;
import com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.gtas.model.housekeeping.HousekeepingFieldID;
import com.gridnode.gtas.events.housekeeping.GetHousekeepingInfoEvent;

public class GetHousekeepingInfoAction extends AbstractGridTalkAction
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6240611940716370220L;
	private final String ACTION_NAME = "GetHousekeepingInfoAction";
  
  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };
    return constructEventResponse(
             IErrorCode.GENERAL_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {

    GetHousekeepingInfoEvent getEvent = (GetHousekeepingInfoEvent)event;
    HousekeepingInfo hkInfo=HousekeepingService.getInstance().getHousekeepingInfo();
    
    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params,
             convertToMap(hkInfo));
  }

  private Map convertToMap(HousekeepingInfo hkInfo)
  {
    return HousekeepingInfo.convertToMap(hkInfo,HousekeepingFieldID.getEntityFieldID(),null);
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return GetHousekeepingInfoEvent.class;
  }
}
