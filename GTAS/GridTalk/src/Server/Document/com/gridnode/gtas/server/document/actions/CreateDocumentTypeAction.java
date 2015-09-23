/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateDocumentTypeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 * Sep 07 2005    Neo Sok Lay         Change to use AbstractCreateEntityAction
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Map;

import com.gridnode.gtas.events.document.CreateDocumentTypeEvent;
import com.gridnode.gtas.model.document.DocumentTypeEntityFieldID;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the creation of a new DocumentType.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class CreateDocumentTypeAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1436167612722001987L;
	private static final String ACTION_NAME = "CreateDocumentTypeAction";

	protected Map convertToMap(AbstractEntity entity)
	{
		return DocumentType.convertToMap(entity, DocumentTypeEntityFieldID.getEntityFieldID(), null);
	}

	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		return getManager().createDocumentType((DocumentType)entity);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		return new Object[] {DocumentType.ENTITY_NAME};
	}

	protected AbstractEntity prepareCreationData(IEvent event)
	{
		CreateDocumentTypeEvent createEvent = (CreateDocumentTypeEvent)event;
		
    DocumentType newDocType = new DocumentType();
    newDocType.setName(createEvent.getdocTypeName());
    newDocType.setDescription(createEvent.getdocTypeDesc());

    return newDocType;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().findDocumentType(key);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return CreateDocumentTypeEvent.class;
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