/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetDocumentTypeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Map;

import com.gridnode.gtas.events.document.GetDocumentTypeEvent;
import com.gridnode.gtas.model.document.DocumentTypeEntityFieldID;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the retrieving of a DocumentType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetDocumentTypeAction
  extends    AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3971978991330424185L;
	private static final String ACTION_NAME = "GetDocumentTypeAction";

	protected Map convertToMap(AbstractEntity entity)
	{
		return DocumentType.convertToMap(entity, DocumentTypeEntityFieldID.getEntityFieldID(), null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetDocumentTypeEvent getEvent = (GetDocumentTypeEvent)event;
		return getManager().findDocumentType(getEvent.getDocumentTypeUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetDocumentTypeEvent getEvent = (GetDocumentTypeEvent)event;
		return new Object[] {DocumentType.ENTITY_NAME, getEvent.getDocumentTypeUID()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetDocumentTypeEvent.class;
	}

  private IDocumentManagerObj getManager()
    throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }
}