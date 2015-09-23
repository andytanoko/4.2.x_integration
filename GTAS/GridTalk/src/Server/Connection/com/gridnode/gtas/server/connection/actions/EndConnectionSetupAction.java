/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EndConnectionSetupAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.actions;

import com.gridnode.gtas.events.connection.EndConnectionSetupEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * Action class for setting up the Connection environment to GridMaster.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class EndConnectionSetupAction extends AbstractConnectionAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3138441507226392383L;
	private final String ACTION_NAME = "EndConnectionSetupAction";

  public EndConnectionSetupAction()
  {
  }

  // ********* Methods from AbstractGridTalkAction **************

  protected Class getExpectedEventClass()
  {
    return EndConnectionSetupEvent.class;
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
    EndConnectionSetupEvent endEvent = (EndConnectionSetupEvent)event;

    // extract the UIDs only.
    //Collection gmUIDs = getUIDs(endEvent.getAvailableGridMasters());
    //Collection routerUIDs = getUIDs(endEvent.getAvailableRouters());

    ServiceLookupHelper.getConnectionService().reorderConnectionPriority(
      endEvent.getAvailableGridMasters(), endEvent.getAvailableRouters());
      //gmUIDs, routerUIDs);

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
  /*
  private Collection getUIDs(Collection entityList)
  {
    ArrayList list = new ArrayList();

    for (Iterator i = entityList.iterator(); i.hasNext(); )
    {
      IEntity entity = (IEntity)i.next();
      list.add(entity.getKey());
    }

    return list;
  }*/
}