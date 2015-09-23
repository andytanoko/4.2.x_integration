/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateAS2DocTypeMappingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Map;

import com.gridnode.gtas.events.document.CreateAS2DocTypeMappingEvent;
import com.gridnode.gtas.model.document.AS2DocTypeMappingEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.gtas.exceptions.IErrorCode;


public class CreateAS2DocTypeMappingAction extends AbstractCreateEntityAction
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 7792874168871935546L;
  
  public static final String ACTION_NAME = "CreateAS2DocTypeMappingAction";
  
  protected Class getExpectedEventClass()
  {
    return CreateAS2DocTypeMappingEvent.class;
  }
  
  protected String getActionName()
  {
    return ACTION_NAME;
  }
  
  protected Map convertToMap(AbstractEntity entity)
  {
    return AS2DocTypeMapping.convertToMap(entity, AS2DocTypeMappingEntityFieldID.getEntityFieldID(), null);
  }
  
  protected Object[] getErrorMessageParams(IEvent event)
  {
    return new Object[] {AS2DocTypeMapping.ENTITY_NAME};
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
  
  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(
             IErrorCode.CREATE_ENTITY_ERROR,
             params,
             ex);
  }
  
  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return getManager().createAS2DocTypeMapping((AS2DocTypeMapping)entity);
  }
  
  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateAS2DocTypeMappingEvent createEvent = (CreateAS2DocTypeMappingEvent)event;

    AS2DocTypeMapping newDocTypeMapping = new AS2DocTypeMapping();
    
    newDocTypeMapping.setAs2DocType(createEvent.getAS2DocType());
    newDocTypeMapping.setDocType(createEvent.getDocType());
    newDocTypeMapping.setPartnerID(createEvent.getPartnerID());
    
    return newDocTypeMapping;
  }
  
  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return getManager().findAS2DocTypeMapping(key);
  }
  
  
}
