/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRoleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Change due to exception handling changes
 *                                    in PDIP layer.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGetEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Map;

import com.gridnode.gtas.events.acl.GetRoleEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class GetRoleAction extends AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5550977493191231883L;
	public static final String ACTION_NAME = "GetRoleAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertRoleToMap((Role)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetRoleEvent getEvent = (GetRoleEvent)event;

    if (getEvent.getRoleUID() == null)
      return ActionHelper.getACLManager().getRoleByRoleName(
               getEvent.getRoleName());
    else
      return ActionHelper.getACLManager().getRoleByUId(
               getEvent.getRoleUID());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetRoleEvent getEvent = (GetRoleEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             Role.ENTITY_NAME,
             String.valueOf(getEvent.getRoleUID()),
             String.valueOf(getEvent.getRoleName()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetRoleEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}