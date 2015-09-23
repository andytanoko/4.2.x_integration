/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPortAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.backend.GetPortEvent;
import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.gtas.server.backend.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a Port.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetPortAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1915184037783310420L;
	public static final String ACTION_NAME = "GetPortAction";

  protected Class getExpectedEventClass()
  {
    return GetPortEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetPortEvent getEvent = (GetPortEvent)event;
    return ActionHelper.getManager().findPort(getEvent.getPortUid());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertPortToMap((Port)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetPortEvent getEvent = (GetPortEvent)event;
    return new Object[]
           {
             Port.ENTITY_NAME,
             String.valueOf(getEvent.getPortUid())
           };
  }
}