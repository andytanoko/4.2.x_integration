/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBizCertMappingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.partnerprocess.GetBizCertMappingEvent;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a BizCertMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetBizCertMappingAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8175788333367411075L;
	public static final String ACTION_NAME = "GetBizCertMappingAction";

  protected Class getExpectedEventClass()
  {
    return GetBizCertMappingEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetBizCertMappingEvent getEvent = (GetBizCertMappingEvent)event;
    return ActionHelper.getManager().findBizCertMappingByUID(getEvent.getUID());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertBizCertMappingToMap((BizCertMapping)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetBizCertMappingEvent getEvent = (GetBizCertMappingEvent)event;
    return new Object[]
           {
             BizCertMapping.ENTITY_NAME,
             String.valueOf(getEvent.getUID())
           };
  }
}