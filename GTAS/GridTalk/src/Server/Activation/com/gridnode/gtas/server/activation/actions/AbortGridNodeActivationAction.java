/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbortGridNodeActivationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.actions;

import java.util.ArrayList;

import com.gridnode.gtas.events.activation.AbortGridNodeActivationEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.activation.exceptions.ConnectionRequiredException;
import com.gridnode.gtas.server.activation.exceptions.GridNodeActivationException;
import com.gridnode.gtas.server.activation.helpers.Logger;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the AbortGridNodeActivationEvent.
 * <p>Multiple outgoing activation requests can be aborted at one go.
 * If any of the abort fails, the EventResponse will indicate error.
 * However, the process will continue until all relevant activation requests
 * are processed for abort. At the end of the process, the error response
 * (if any) will indicate the UIDs of the list of activation requests that
 * failed to be aborted.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class AbortGridNodeActivationAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3958204830728510453L;

	public static final String ACTION_NAME = "AbortGridNodeActivationAction";

  private ArrayList _failedList = new ArrayList();

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return AbortGridNodeActivationEvent.class;
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
    AbortGridNodeActivationEvent abortEvent = (AbortGridNodeActivationEvent)event;

    _failedList.clear();
    Object[] uIDs = abortEvent.getRecordUIDs().toArray();
    for (int i=0; i<uIDs.length; i++)
    {
      try
      {
        ServiceLookupHelper.getActivationManager().abortActivationRequest(
          (Long)uIDs[i]);
      }
      catch (GridNodeActivationException ex)
      {
        Logger.warn("[AbortGridNodeActivationAction.doProcess] Error aborting record "+
          uIDs[i]);
        _failedList.add(uIDs[i]);
      }
    }
    if (!_failedList.isEmpty())
      throw new GridNodeActivationException("Some Activation requests cannot be cancelled!");

    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(IErrorCode.NO_ERROR, params);
  }

  // **************************** Own Methods *****************************

}