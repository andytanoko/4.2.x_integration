/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetConnectionSetupResultAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.actions;

import com.gridnode.gtas.events.connection.GetConnectionSetupResultEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * Action class for getting the ConnectionSetupResult. This action guarantees
 * a non-null return of ConnectionSetupResult map object even though connection
 * setup has not been done before.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetConnectionSetupResultAction extends AbstractConnectionAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5982681354930111877L;
	private final String ACTION_NAME = "GetConnectionSetupResultAction";

  public GetConnectionSetupResultAction()
  {
  }

  // ********* Methods from AbstractGridTalkAction **************

  protected Class getExpectedEventClass()
  {
    return GetConnectionSetupResultEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };
    return constructEventResponse(
             IErrorCode.CONNECTION_SETUP_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    GetConnectionSetupResultEvent getEvent = (GetConnectionSetupResultEvent)event;

    ConnectionSetupResult setup = getConnectionSetupResult();

    // cache the JmsRouters and GridMaster nodes
    setup.setFieldValue(setup.AVAILABLE_GRIDMASTERS, getGridMasters(setup.getAvailableGridMastersUIDs()));
    setup.setFieldValue(setup.AVAILABLE_ROUTERS, getJmsRouters(setup.getAvailableRouterUIDs()));

    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params,
             convertToMap(setup));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}