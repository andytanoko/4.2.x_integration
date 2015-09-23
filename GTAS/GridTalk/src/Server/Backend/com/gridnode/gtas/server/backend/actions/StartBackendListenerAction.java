/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StartBackendListenerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 20 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.actions;

import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;

import com.gridnode.gtas.events.backend.StartBackendListenerEvent;
import com.gridnode.gtas.server.backend.helpers.ActionHelper;

import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the starting up of the GTAS backend listener.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class StartBackendListenerAction
  extends    AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9193452001762785048L;
	public static final String ACTION_NAME = "StartBackendListenerAction";

  protected Class getExpectedEventClass()
  {
    return StartBackendListenerEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    return constructEventResponse(
             IErrorCode.GENERAL_ERROR,
             getErrorMessageParams(event),
             ex);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    StartBackendListenerEvent aEvent = (StartBackendListenerEvent)event;
    return new Object[]
           {
             aEvent.getEventName()
           };
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ActionHelper.getManager().startListener();

    return constructEventResponse();
  }
}