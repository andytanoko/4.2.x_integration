/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DenyGridNodeActivationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.actions;

import java.util.ArrayList;

import com.gridnode.gtas.events.activation.DenyGridNodeActivationEvent;
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
 * This Action class handles the DenyGridNodeActivationEvent.
 * <p>Multiple incoming activation requests can be denied at one go.
 * If any of the deny fails, the EventResponse will indicate error.
 * However, the process will continue until all relevant activation requests
 * are processed for denial. At the end of the process, the error response
 * (if any) will indicate the UIDs of the list of activation requests that
 * failed to be denied.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class DenyGridNodeActivationAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -465262286290744088L;

	public static final String ACTION_NAME = "DenyGridNodeActivationAction";

  private ArrayList _failedList = new ArrayList();

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return DenyGridNodeActivationEvent.class;
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
    DenyGridNodeActivationEvent denyEvent = (DenyGridNodeActivationEvent)event;

    _failedList.clear();
    Object[] uIDs = denyEvent.getRecordUIDs().toArray();
    for (int i=0; i<uIDs.length; i++)
    {
      try
      {
        ServiceLookupHelper.getActivationManager().denyActivationRequest(
          (Long)uIDs[i]);
      }
      catch (GridNodeActivationException ex)
      {
        Logger.warn("[AbortGridNodeActivationAction.doProcess] Error denying record "+
          uIDs[i]);
        _failedList.add(uIDs[i]);
      }
    }
    if (!_failedList.isEmpty())
      throw new GridNodeActivationException("Some Activation requests cannot be denied!");

    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(IErrorCode.NO_ERROR, params);
  }

  // **************************** Own Methods *****************************

}