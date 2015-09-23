/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ManualExportDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 02 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.events.document.ManualExportDocumentEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the manual export of documents.
 *
 * @author Koh Han Sing
 *
 * @version 2.2
 * @since 2.2
 */
public class ManualExportDocumentAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2805454293763600087L;
	public static final String ACTION_NAME = "ManualExportDocumentAction";

  protected Class getExpectedEventClass()
  {
    return ManualExportDocumentEvent.class;
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
    ManualExportDocumentEvent msEvent = (ManualExportDocumentEvent)event;
    return new Object[]
           {
             msEvent.getGridDocumentUIDs()
           };
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ManualExportDocumentEvent msEvent = (ManualExportDocumentEvent)event;
    List gdocsUids = msEvent.getGridDocumentUIDs();

    for (Iterator i = gdocsUids.iterator(); i.hasNext(); )
    {
      Long gdocUid = (Long)i.next();
      GridDocument gdoc = ActionHelper.getManager().findGridDocument(gdocUid);
      ActionHelper.getManager().exportDoc(gdoc);
    }

    return constructEventResponse();
  }
}