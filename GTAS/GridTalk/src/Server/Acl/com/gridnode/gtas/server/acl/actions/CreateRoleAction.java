/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateRoleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Change due to exception handling changes
 *                                    in PDIP layer.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractCreateEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jul 25 2002    Neo Sok Lay         Return created entity to client.
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Map;

import com.gridnode.gtas.events.acl.CreateRoleEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class CreateRoleAction extends AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8316224133346010561L;
	public static final String ACTION_NAME = "CreateRoleAction";

  // ****************** AbstractGridTalkAction methods *****************

  protected Class getExpectedEventClass()
  {
    return CreateRoleEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws java.lang.Exception
  {
    return ActionHelper.getACLManager().getRoleByUId(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateRoleEvent addEvent = (CreateRoleEvent)event;

    Role newRole = new Role();
    newRole.setDescr(addEvent.getDescription());
    newRole.setRole(addEvent.getRole());

    return newRole;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateRoleEvent addEvent = (CreateRoleEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             Role.ENTITY_NAME,
           };
  }

  protected Long createEntity(AbstractEntity entity) throws java.lang.Exception
  {
    return ActionHelper.getACLManager().createRole((Role)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertRoleToMap((Role)entity);
  }

  // ********************* Own methods ***********************************

}