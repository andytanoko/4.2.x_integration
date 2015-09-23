/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubmitGridNodeDeactivationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.actions;

import java.util.ArrayList;

import com.gridnode.gtas.events.activation.SubmitGridNodeDeactivationEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.activation.exceptions.ConnectionRequiredException;
import com.gridnode.gtas.server.activation.exceptions.GridNodeActivationException;
import com.gridnode.gtas.server.activation.helpers.Logger;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the SubmitGridNodeDeactivationEvent.
 * <p>Multiple currently active Gridnodes can be deactivated at one go.
 * If any of the deactivate fails, the EventResponse will indicate error.
 * However, the process will continue until all relevant GridNodes
 * are processed for deactivation. At the end of the process, the error response
 * (if any) will indicate the UIDs of the list of GridNodes that
 * failed to be deactivated.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SubmitGridNodeDeactivationAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3674031099522495890L;

	public static final String ACTION_NAME = "SubmitGridNodeDeactivationAction";

  private ArrayList _failedList = new ArrayList();

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return SubmitGridNodeDeactivationEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                       {
                         _failedList,
                       };

    short errorCode = IErrorCode.ACTIVATE_GRIDNODE_ERROR;
    if (ex instanceof ConnectionRequiredException)
      errorCode = IErrorCode.CONNECTION_REQUIRED_ERROR;

    return constructEventResponse(
             errorCode,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    SubmitGridNodeDeactivationEvent deactEvent = (SubmitGridNodeDeactivationEvent)event;

    _failedList.clear();
    Object[] nodeUIDs = deactEvent.getGridNodeUIDs().toArray();
    for (int i=0; i<nodeUIDs.length; i++)
    {
      try
      {
        GridNode gn = ServiceLookupHelper.getGridNodeManager().findGridNode((Long)nodeUIDs[i]);
        ServiceLookupHelper.getActivationManager().submitDeactivationRequest(
          Integer.valueOf(gn.getID()));
      }
      catch (GridNodeActivationException ex)
      {
        Logger.warn("[AbortGridNodeActivationAction.doProcess] Error deactivating record "+
          nodeUIDs[i]);
        _failedList.add(nodeUIDs[i]);
      }
    }
    if (!_failedList.isEmpty())
      throw new GridNodeActivationException("Some GridNodes cannot be deactivated!");

    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(IErrorCode.NO_ERROR, params);
  }

  // **************************** Own Methods *****************************

}