/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateProcessMappingAction.java
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

import com.gridnode.gtas.events.partnerprocess.UpdateProcessMappingEvent;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a ProcessMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class UpdateProcessMappingAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1404135514046026658L;
	private ProcessMapping _mapping;
  private ProcessDef     _processDef;

  public static final String ACTION_NAME = "UpdateProcessMappingAction";

  protected Class getExpectedEventClass()
  {
    return UpdateProcessMappingEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertProcessMappingToMap((ProcessMapping)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateProcessMappingEvent updEvent = (UpdateProcessMappingEvent)event;
    _mapping = ActionHelper.getManager().findProcessMappingByUID(updEvent.getUID());

    _processDef = ActionHelper.getProcessDefManager().findProcessDefByName(updEvent.getProcessDef());
    if (_processDef == null)
      throw new Exception("Bad Process Def: "+updEvent.getProcessDef());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateProcessMappingEvent updEvent = (UpdateProcessMappingEvent)event;

    _mapping.setProcessDef(updEvent.getProcessDef());
    _mapping.setPartnerID(updEvent.getPartnerID());
    _mapping.setInitiatingRole(updEvent.isInitiatingRole());
    _mapping.setSendChannelUID(updEvent.getSendChannelUID());

    if (!updEvent.isInitiatingRole() ||
        _processDef.TYPE_TWO_ACTION.equals(_processDef.getProcessType()))
      _mapping.setDocumentType(updEvent.getDocType());
    else
      _mapping.setDocumentType(null);

/*
    if (updEvent.isInitiatingRole())
    {
//      _mapping.setSendChannelUID(updEvent.getSendChannelUID());
      _mapping.setDocumentType(null);
    }
    else
    {
      _mapping.setDocumentType(updEvent.getDocType());
//      _mapping.setSendChannelUID(null);
    }
*/
    _mapping.setProcessIndicatorCode(_processDef.getGProcessIndicatorCode());
    _mapping.setProcessVersionID(_processDef.getVersionIdentifier());


    return _mapping;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.getManager().updateProcessMapping((ProcessMapping)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findProcessMappingByUID(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateProcessMappingEvent updEvent = (UpdateProcessMappingEvent)event;
    return new Object[]
           {
             ProcessMapping.ENTITY_NAME,
             String.valueOf(updEvent.getUID()),
             updEvent.getProcessDef(),
             updEvent.getPartnerID(),
             String.valueOf(updEvent.isInitiatingRole()),
             updEvent.getDocType(),
             String.valueOf(updEvent.getSendChannelUID()),
           };
  }

}