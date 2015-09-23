/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveHousekeepingInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 7, 2003      Mahesh         Created
 */ 
package com.gridnode.gtas.server.housekeeping.actions;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.housekeeping.HousekeepingService;
import com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import java.util.Map;
import com.gridnode.gtas.events.housekeeping.SaveHousekeepingInfoEvent;

public class SaveHousekeepingInfoAction extends AbstractGridTalkAction
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1211848480253860111L;
	private final String ACTION_NAME = "SaveHousekeepingInfoAction";
  
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

    SaveHousekeepingInfoEvent saveEvent = (SaveHousekeepingInfoEvent)event;
    HousekeepingInfo hkInfo=convertToEntity(saveEvent.getHousekeepingInfo());
    HousekeepingService.getInstance().saveHousekeepingInfo(hkInfo);
    
    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params);
  }

  protected HousekeepingInfo convertToEntity(Map housekeepingInfoMap)
  {
    HousekeepingInfo housekeepingInfo = new HousekeepingInfo();
    Object[] keys = housekeepingInfoMap.keySet().toArray();
    for (int i=0; i<keys.length; i++)
    {
      housekeepingInfo.setFieldValue((Number)keys[i], housekeepingInfoMap.get(keys[i]));
    }
    return housekeepingInfo;
  }
  
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return SaveHousekeepingInfoEvent.class;
  }

}
