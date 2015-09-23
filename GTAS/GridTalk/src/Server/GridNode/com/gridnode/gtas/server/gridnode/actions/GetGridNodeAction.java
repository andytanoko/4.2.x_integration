/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridNodeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 28 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.server.gridnode.helpers.ActionHelper;
import com.gridnode.gtas.events.gridnode.GetGridNodeEvent;
import com.gridnode.gtas.server.gridnode.model.GridNode;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of one GridNode.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetGridNodeAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3166417241656383946L;
	public static final String ACTION_NAME = "GetGridNodeAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertGridNodeToMap((GridNode)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetGridNodeEvent getEvent = (GetGridNodeEvent)event;

    return ActionHelper.findGridNodeByUID(getEvent.getGridNodeUID());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetGridNodeEvent getEvent = (GetGridNodeEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getGridNodeUID()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetGridNodeEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}