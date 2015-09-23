/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetProcessMappingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.partnerprocess.GetProcessMappingEvent;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a ProcessMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetProcessMappingAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6551977450686009316L;
	public static final String ACTION_NAME = "GetProcessMappingAction";

  protected Class getExpectedEventClass()
  {
    return GetProcessMappingEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetProcessMappingEvent getEvent = (GetProcessMappingEvent)event;
    return ActionHelper.getManager().findProcessMappingByUID(getEvent.getUID());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertProcessMappingToMap((ProcessMapping)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetProcessMappingEvent getEvent = (GetProcessMappingEvent)event;
    return new Object[]
           {
             ProcessMapping.ENTITY_NAME,
             String.valueOf(getEvent.getUID())
           };
  }
}