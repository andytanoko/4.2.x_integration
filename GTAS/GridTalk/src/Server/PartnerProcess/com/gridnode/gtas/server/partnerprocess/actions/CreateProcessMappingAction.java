/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateProcessMappingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 * Jan 23 2003    Ang Meng Hua        Modified prepareCreationData to set
 *                                    channel irregardless of participating roles
 * Feb 19 2003    Neo Sok Lay         Save DocType for responding role or the
 *                                    process is two-action.
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import java.util.Map;

import com.gridnode.gtas.events.partnerprocess.CreateProcessMappingEvent;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new ProcessMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class CreateProcessMappingAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7792874168871935546L;

	public static final String ACTION_NAME = "CreateProcessMappingAction";

  private ProcessDef _processDef = null;

  protected Class getExpectedEventClass()
  {
    return CreateProcessMappingEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findProcessMappingByUID(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateProcessMappingEvent createEvent = (CreateProcessMappingEvent)event;

    ProcessMapping newProcessMapping = new ProcessMapping();
    newProcessMapping.setProcessDef(createEvent.getProcessDef());
    newProcessMapping.setPartnerID(createEvent.getPartnerID());
    newProcessMapping.setInitiatingRole(createEvent.isInitiatingRole());
//    if (createEvent.isInitiatingRole())
//      newProcessMapping.setSendChannelUID(createEvent.getSendChannelUID());
//    else
//      newProcessMapping.setDocumentType(createEvent.getDocType());

    newProcessMapping.setSendChannelUID(createEvent.getSendChannelUID());

//    if (!createEvent.isInitiatingRole())
    if (!createEvent.isInitiatingRole() ||
        _processDef.TYPE_TWO_ACTION.equals(_processDef.getProcessType()))
      newProcessMapping.setDocumentType(createEvent.getDocType());

    newProcessMapping.setProcessIndicatorCode(_processDef.getGProcessIndicatorCode());
    newProcessMapping.setProcessVersionID(_processDef.getVersionIdentifier());

    return newProcessMapping;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateProcessMappingEvent createEvent = (CreateProcessMappingEvent)event;
    return new Object[]
           {
             ProcessMapping.ENTITY_NAME,
             createEvent.getProcessDef(),
             createEvent.getPartnerID(),
             String.valueOf(createEvent.isInitiatingRole()),
             createEvent.getDocType(),
             String.valueOf(createEvent.getSendChannelUID()),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.getManager().createProcessMapping((ProcessMapping)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertProcessMappingToMap((ProcessMapping)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    CreateProcessMappingEvent createEvent = (CreateProcessMappingEvent)event;
    _processDef = ActionHelper.getProcessDefManager().findProcessDefByName(createEvent.getProcessDef());
    if (_processDef == null)
      throw new Exception("Bad Process Def: "+createEvent.getProcessDef());
  }


}