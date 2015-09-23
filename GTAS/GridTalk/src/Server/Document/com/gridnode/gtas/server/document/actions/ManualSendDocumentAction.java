/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ManualSendDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.events.document.ManualSendDocumentEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the deletion of a GridDocument.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ManualSendDocumentAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7398432654383379584L;
	public static final String ACTION_NAME = "ManualSendDocumentAction";

  protected Class getExpectedEventClass()
  {
    return ManualSendDocumentEvent.class;
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
    ManualSendDocumentEvent msEvent = (ManualSendDocumentEvent)event;
    return new Object[]
           {
             msEvent.getGridDocumentUIDs()
           };
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ManualSendDocumentEvent msEvent = (ManualSendDocumentEvent)event;
    List gdocsUids = msEvent.getGridDocumentUIDs();

    //ArrayList gdocs = new ArrayList();
    for (Iterator i = gdocsUids.iterator(); i.hasNext(); )
    {
      Long gdocUid = (Long)i.next();
      GridDocument gdoc = ActionHelper.getManager().findGridDocument(gdocUid);
      ActionHelper.getManager().sendDoc(gdoc);
//      gdocs.add(gdoc);
    }

//    ActionHelper.getManager().sendDoc(gdocs);

    return constructEventResponse();
  }
}