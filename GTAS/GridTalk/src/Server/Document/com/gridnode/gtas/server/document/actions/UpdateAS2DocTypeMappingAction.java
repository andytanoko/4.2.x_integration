/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Map;


import com.gridnode.gtas.events.document.UpdateAS2DocTypeMappingEvent;
import com.gridnode.gtas.model.document.AS2DocTypeMappingEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;
import com.gridnode.gtas.server.document.model.DocumentType;


public class UpdateAS2DocTypeMappingAction extends AbstractUpdateEntityAction
{

  private static final long serialVersionUID = 1996034232208696190L;
  private static final String ACTION_NAME = "UpdateAS2DocTypeMappingAction";
  
  private AS2DocTypeMapping _AS2DocTypeMapping = null;
  
  protected Map convertToMap(AbstractEntity entity)
  {
    return AS2DocTypeMapping.convertToMap(entity, AS2DocTypeMappingEntityFieldID.getEntityFieldID(), null);
  }
  
  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateAS2DocTypeMappingEvent updEvent = (UpdateAS2DocTypeMappingEvent)event;
    return new Object[] {DocumentType.ENTITY_NAME, updEvent.getAS2DocTypeMappingUID()};
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateAS2DocTypeMappingEvent updEvent = (UpdateAS2DocTypeMappingEvent)event;
    
    _AS2DocTypeMapping.setAs2DocType(updEvent.getAS2DocType());
    _AS2DocTypeMapping.setDocType(updEvent.getDocType());
    _AS2DocTypeMapping.setPartnerID(updEvent.getPartnerID());

    return _AS2DocTypeMapping;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return getManager().findAS2DocTypeMapping(key);
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    getManager().updateAS2DocTypeMapping((AS2DocTypeMapping)entity);
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return UpdateAS2DocTypeMappingEvent.class;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateAS2DocTypeMappingEvent updEvent = (UpdateAS2DocTypeMappingEvent)event;
    _AS2DocTypeMapping = getManager().findAS2DocTypeMapping(updEvent.getAS2DocTypeMappingUID());
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
