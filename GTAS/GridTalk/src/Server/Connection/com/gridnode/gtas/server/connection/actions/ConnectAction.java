/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 * Nov 16 2002    Neo Sok Lay         Needs security password.
 */
package com.gridnode.gtas.server.connection.actions;

import com.gridnode.gtas.events.connection.ConnectEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;
import com.gridnode.gtas.server.registration.exceptions.InvalidSecurityPasswordException;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
 
/**
 * Action class for connecting to the GridMaster.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectAction extends AbstractConnectionAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 705500256880809624L;
	private final String ACTION_NAME = "ConnectAction";

  public ConnectAction()
  {
  }

  // ********* Methods from AbstractGridTalkAction **************

  protected Class getExpectedEventClass()
  {
    return ConnectEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };

    short errorCode =  IErrorCode.CONNECTION_ERROR;
    if (ex instanceof InvalidSecurityPasswordException)
      errorCode = IErrorCode.INVALID_SEC_PWD_ERROR;

    return constructEventResponse(
             errorCode,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    ConnectEvent conEvent = (ConnectEvent)event;

    ConnectionSetupResult setup = getConnectionSetupResult();
    if (setup.getStatus().shortValue() != ConnectionSetupResult.STATUS_SUCCESS)
    {
//      Logger.log("[ConnectAction.doProcess] No connection setup done, performing auto setup...");
//      ServiceLookupHelper.getConnectionService().setupConnection(
//        setup.getSetupParams().getCurrentLocation(),
//        setup.getSetupParams().getServicingRouter(),
//        conEvent.getSecurityPassword());
//      Logger.log("[ConnectAction.doProcess] Connection setup done, proceeding to connect...");

      throw new ApplicationException("Please perform Connection Setup before connecting to GridMaster");
    }
    else
    {
      ServiceLookupHelper.getRegistrationService().verifyAndSetSecurityPassword(
        conEvent.getSecurityPassword());
    }

    ServiceLookupHelper.getConnectionService().connect();

    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params);
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}