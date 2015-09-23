/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveNetworkSettingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.actions;

import com.gridnode.gtas.events.connection.SaveNetworkSettingEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
 
/**
 * Action class for saving the NetworkSetting.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SaveNetworkSettingAction extends AbstractConnectionAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1377607485322305320L;
	private final String ACTION_NAME = "SaveNetworkSettingAction";

  public SaveNetworkSettingAction()
  {
  }

  // ********* Methods from AbstractGridTalkAction **************

  protected Class getExpectedEventClass()
  {
    return SaveNetworkSettingEvent.class;
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
    SaveNetworkSettingEvent saveEvent = (SaveNetworkSettingEvent)event;

    ServiceLookupHelper.getConnectionService().saveNetworkSetting(
      convertToEntity(saveEvent.getNetworkSetting()));

    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params,
             convertToMap(getNetworkSetting()));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}