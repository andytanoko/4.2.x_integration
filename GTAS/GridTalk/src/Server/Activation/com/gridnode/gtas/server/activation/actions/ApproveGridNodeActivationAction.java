/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ApproveGridNodeActivationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.actions;

import com.gridnode.gtas.events.activation.ApproveGridNodeActivationEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.activation.exceptions.ConnectionRequiredException;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the ApproveGridNodeActivationEvent.
 * <p>On an incoming activation request can be approved, and at least one
 * BusinessEntity must be selected to trade with the potential partner.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ApproveGridNodeActivationAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2735286292703673192L;
	public static final String ACTION_NAME = "ApproveGridNodeActivationAction";

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return ApproveGridNodeActivationEvent.class;
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
    ApproveGridNodeActivationEvent approveEvent = (ApproveGridNodeActivationEvent)event;

    ServiceLookupHelper.getActivationManager().approveActivationRequest(
      approveEvent.getRecordUID(),
      approveEvent.getExchangeBeUIDs());

    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(IErrorCode.NO_ERROR, params);
  }

  protected void doSemanticValidation(IEvent event) throws java.lang.Exception
  {
    ApproveGridNodeActivationEvent approveEvent = (ApproveGridNodeActivationEvent)event;

    //check at least one be
    if (approveEvent.getExchangeBeUIDs().isEmpty())
      throw new Exception("At least one Business Entity required for activation!");
  }

  // **************************** Own Methods *****************************

}