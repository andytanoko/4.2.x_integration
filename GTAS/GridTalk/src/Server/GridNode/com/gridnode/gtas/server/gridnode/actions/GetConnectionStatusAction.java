/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetConnectionStatusAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.actions;

import com.gridnode.gtas.server.gridnode.helpers.ActionHelper;
import com.gridnode.gtas.server.gridnode.helpers.ServiceLookupHelper;
import com.gridnode.gtas.events.gridnode.GetConnectionStatusEvent;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of one ConnectionStatus.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetConnectionStatusAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3931327841917115288L;
	public static final String ACTION_NAME = "GetConnectionStatusAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertConnStatusToMap((ConnectionStatus)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetConnectionStatusEvent getEvent = (GetConnectionStatusEvent)event;

    ConnectionStatus result = null;
    if (getEvent.isGetMyConnStatus())
    {
      GridNode gn = ServiceLookupHelper.getGridNodeManager().findMyGridNode();
      result = ActionHelper.findConnectionStatus(gn.getID());
    }
    else if (getEvent.getConnStatusUID() != null)
      result = ActionHelper.findConnectionStatus(getEvent.getConnStatusUID());
    else
      result = ActionHelper.findConnectionStatus(getEvent.getGridNodeID());

    return result;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetConnectionStatusEvent getEvent = (GetConnectionStatusEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.isGetMyConnStatus()),
             String.valueOf(getEvent.getConnStatusUID()),
             getEvent.getGridNodeID(),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetConnectionStatusEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}