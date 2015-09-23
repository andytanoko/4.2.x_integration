/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateDocumentTypeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Map;

import com.gridnode.gtas.events.document.UpdateDocumentTypeEvent;
import com.gridnode.gtas.model.document.DocumentTypeEntityFieldID;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;
 
/**
 * This Action class handles the update of a DocumentType.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class UpdateDocumentTypeAction
  extends    AbstractUpdateEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1996034232208696190L;
	private static final String ACTION_NAME = "UpdateDocumentTypeAction";
	
	private DocumentType _docTypeToUpdate;
	
	protected Map convertToMap(AbstractEntity entity)
	{
		return DocumentType.convertToMap(entity, DocumentTypeEntityFieldID.getEntityFieldID(), null);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdateDocumentTypeEvent updEvent = (UpdateDocumentTypeEvent)event;
		return new Object[] {DocumentType.ENTITY_NAME, updEvent.getDocumentTypeUID()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdateDocumentTypeEvent updEvent = (UpdateDocumentTypeEvent)event;
		
    _docTypeToUpdate.setDescription(updEvent.getUpdDocumentTypeDesc());

		return _docTypeToUpdate;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().findDocumentType(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		getManager().updateDocumentType((DocumentType)entity);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdateDocumentTypeEvent.class;
	}

	protected void doSemanticValidation(IEvent event) throws Exception
	{
		UpdateDocumentTypeEvent updEvent = (UpdateDocumentTypeEvent)event;
		
		_docTypeToUpdate = getManager().findDocumentType(updEvent.getDocumentTypeUID());
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