/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ViewDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 25 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;

import com.gridnode.gtas.events.document.ViewDocumentEvent;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.document.model.GridDocument;

import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the setting of the isViewed field in the
 * GridDocument.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ViewDocumentAction
  extends    AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3239667258576820983L;
	public static final String ACTION_NAME = "ViewDocumentAction";

  protected Class getExpectedEventClass()
  {
    return ViewDocumentEvent.class;
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

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    ViewDocumentEvent editEvent = (ViewDocumentEvent)event;
    ActionHelper.getManager().findGridDocument(editEvent.getGridDocumentUID());
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ViewDocumentEvent editEvent = (ViewDocumentEvent)event;
    ActionHelper.getManager().viewDoc(editEvent.getGridDocumentUID());
    return constructEventResponse();

  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    ViewDocumentEvent editEvent = (ViewDocumentEvent)event;
    return new Object[]
           {
             GridDocument.ENTITY_NAME,
             String.valueOf(editEvent.getGridDocumentUID())
           };
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    ViewDocumentEvent editEvent = (ViewDocumentEvent)event;
    return new Object[]
           {
             GridDocument.ENTITY_NAME,
             String.valueOf(editEvent.getGridDocumentUID())
           };
  }
}