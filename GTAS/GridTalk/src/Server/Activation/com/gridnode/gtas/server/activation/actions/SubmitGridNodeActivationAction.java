/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubmitGridNodeActivationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.actions;

import com.gridnode.gtas.events.activation.SubmitGridNodeActivationEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.activation.exceptions.ConnectionRequiredException;
import com.gridnode.gtas.server.activation.helpers.ActionHelper;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the SubmitGridNodeActivation event.
 * <p>An activation request can be submitted to activate an inactive GridNode.
 * This action will submit this request to the GridMaster who forwards it to
 * the potential partner GridTalk where decision is to be made whether to
 * approve or deny the request.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SubmitGridNodeActivationAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4886001849226022442L;
	public static final String ACTION_NAME = "SubmitGridNodeActivationAction";

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return SubmitGridNodeActivationEvent.class;
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
    SubmitGridNodeActivationEvent actEvent = (SubmitGridNodeActivationEvent)event;

    ServiceLookupHelper.getActivationManager().submitActivationRequest(
      actEvent.getGridNodeID(),
      actEvent.getGridNodeName(),
      actEvent.getActivateReason(),
      actEvent.getExchangeBeUIDs());

    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(IErrorCode.NO_ERROR, params);
  }

  protected void doSemanticValidation(IEvent event) throws java.lang.Exception
  {
    SubmitGridNodeActivationEvent actEvent = (SubmitGridNodeActivationEvent)event;

    //check gridnode is not active/mysqlf/gm
    ActionHelper.assertInactivePartner(actEvent.getGridNodeID());

    //check at least one be
    if (actEvent.getExchangeBeUIDs().isEmpty())
      throw new Exception("At least one Business Entity required for activation!");
  }

  // **************************** Own Methods *****************************

}