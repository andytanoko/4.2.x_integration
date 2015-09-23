/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetNetworkSettingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.actions;

import com.gridnode.gtas.events.connection.GetNetworkSettingEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.connection.model.NetworkSetting;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
 
/**
 * Action class for getting the NetworkSetting. This action guarantees
 * a non-null return of NetworkSetting map object even though network settings
 * has not been configured before.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetNetworkSettingAction extends AbstractConnectionAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3063814969056981654L;
	private final String ACTION_NAME = "GetNetworkSettingAction";

  public GetNetworkSettingAction()
  {
  }

  // ********* Methods from AbstractGridTalkAction **************

  protected Class getExpectedEventClass()
  {
    return GetNetworkSettingEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };
    return constructEventResponse(
             IErrorCode.NETWORK_SETTING_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    GetNetworkSettingEvent getEvent = (GetNetworkSettingEvent)event;

    NetworkSetting setting = getNetworkSetting();

    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params,
             convertToMap(setting));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}