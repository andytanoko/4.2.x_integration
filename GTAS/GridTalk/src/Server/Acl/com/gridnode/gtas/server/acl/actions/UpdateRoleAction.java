/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateRoleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Change due to exception handling changes
 *                                    in PDIP layer.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractUpdateEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jul 25 2002    Neo Sok Lay         Return updated entity to client.
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.UpdateRoleEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;

import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

public class UpdateRoleAction extends AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6695716814411511022L;

	public static final String ACTION_NAME = "UpdateRoleAction";

  private Role _roleToUpdate;

  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
    ActionHelper.getACLManager().updateRole(
      (Role)entity);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateRoleEvent updEvent = (UpdateRoleEvent)event;

    _roleToUpdate.setDescr(updEvent.getUpdatedDesc());
    _roleToUpdate.setRole(updEvent.getUpdatedRole());

    return _roleToUpdate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateRoleEvent updEvent = (UpdateRoleEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             Role.ENTITY_NAME,
             updEvent.getUId(),
           };
  }

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return UpdateRoleEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateRoleEvent updEvent = (UpdateRoleEvent)event;

    _roleToUpdate = ActionHelper.verifyRole(updEvent.getUId());
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getACLManager().getRoleByUId(key);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertRoleToMap((Role)entity);
  }


  // ****************** Own methods **************************

}