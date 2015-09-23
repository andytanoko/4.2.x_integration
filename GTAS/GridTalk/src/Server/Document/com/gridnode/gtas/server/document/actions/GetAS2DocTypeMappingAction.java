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
 * 2008-10-17    Wong Yee Wah         Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Map;

import com.gridnode.gtas.events.document.GetAS2DocTypeMappingEvent;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.gtas.model.document.AS2DocTypeMappingEntityFieldID;

public class GetAS2DocTypeMappingAction
extends    AbstractGetEntityAction
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -3971978991330424185L;
  private static final String ACTION_NAME = "GetAS2DocTypeMappingAction";
  
  protected Map convertToMap(AbstractEntity entity)
  {
    return AS2DocTypeMapping.convertToMap(entity, AS2DocTypeMappingEntityFieldID.getEntityFieldID(), null);
  }
  
  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetAS2DocTypeMappingEvent getEvent = (GetAS2DocTypeMappingEvent)event;
    return getManager().findAS2DocTypeMapping(getEvent.getAS2DocTypeUID());
  }
  
  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetAS2DocTypeMappingEvent getEvent = (GetAS2DocTypeMappingEvent)event;
    return new Object[] {AS2DocTypeMapping.ENTITY_NAME, getEvent.getAS2DocTypeUID()};
  }
  
  protected String getActionName()
  {
    return ACTION_NAME;
  }
  
  protected Class getExpectedEventClass()
  {
    return GetAS2DocTypeMappingEvent.class;
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
