/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetupConnectionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.actions;

import com.gridnode.gtas.server.connection.exceptions.ConnectionSetupException;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.connection.SetupConnectionEvent;

import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * Action class for setting up the Connection environment to GridMaster.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SetupConnectionAction extends AbstractConnectionAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7159373255051988206L;
	private final String ACTION_NAME = "SetupConnectionAction";

  public SetupConnectionAction()
  {
  }

  // ********* Methods from AbstractGridTalkAction **************

  protected Class getExpectedEventClass()
  {
    return SetupConnectionEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };

    short errorCode = IErrorCode.CONNECTION_SETUP_ERROR;
    if (ex instanceof ConnectionSetupException)
    {
      // to give different code if security password invalid
      if (ConnectionSetupException.CODE_INVALID_PASSWORD ==
         ((ConnectionSetupException)ex).getErrorCode())
         errorCode = IErrorCode.INVALID_SEC_PWD_ERROR;
    }
    return constructEventResponse(
             errorCode,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    SetupConnectionEvent setupEvent = (SetupConnectionEvent)event;

    ServiceLookupHelper.getConnectionService().setupConnection(
      setupEvent.getCurrentLocation(),
      setupEvent.getServicingRouter(),
      setupEvent.getSecurityPassword());

    ConnectionSetupResult result = getConnectionSetupResult();
    result.setFieldValue(ConnectionSetupResult.AVAILABLE_GRIDMASTERS,
      getGridMasters(result.getAvailableGridMastersUIDs()));
    result.setFieldValue(ConnectionSetupResult.AVAILABLE_ROUTERS,
      getJmsRouters(result.getAvailableRouterUIDs()));

    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params,
             convertToMap(result));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}