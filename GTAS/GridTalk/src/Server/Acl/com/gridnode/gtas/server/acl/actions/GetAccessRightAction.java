/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAccessRightAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 30 2002    Neo Sok Lay         Created
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGetEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Map;

import com.gridnode.gtas.events.acl.GetAccessRightEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.base.acl.model.AccessRight;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the retrieving of a User account.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAccessRightAction
  extends    AbstractGetEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6419224915738853590L;
	public static final String ACTION_NAME = "GetAccessRightAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAccessRightToMap((AccessRight)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetAccessRightEvent getEvent = (GetAccessRightEvent)event;

    return ActionHelper.getACLManager().getAccessRight(
             getEvent.getAccessRightUID());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetAccessRightEvent getEvent = (GetAccessRightEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             AccessRight.ENTITY_NAME,
             String.valueOf(getEvent.getAccessRightUID()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetAccessRightEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}