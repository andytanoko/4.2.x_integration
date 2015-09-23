/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AddAccessRightAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 30 2002    Neo Sok Lay         Created
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractCreateEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jul 25 2002    Neo Sok Lay         Return updated entity to client.
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Map;

import com.gridnode.gtas.events.acl.AddAccessRightEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.base.acl.model.AccessRight;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the addition of new access right to a Role.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class AddAccessRightAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -838217578944555267L;
	public static final String ACTION_NAME = "AddAccessRightAction";

  // ****************** AbstractGridTalkAction methods *****************

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    AddAccessRightEvent addEvent = (AddAccessRightEvent)event;

    ActionHelper.verifyRole(addEvent.getRoleUID());
    ActionHelper.verifyFeatureActionDataType(
      addEvent.getFeature(),
      addEvent.getGrantedAction(),
      addEvent.getGrantedDataType());
  }

  protected Class getExpectedEventClass()
  {
    return AddAccessRightEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws java.lang.Exception
  {
    return ActionHelper.getACLManager().getAccessRight(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    AddAccessRightEvent addEvent = (AddAccessRightEvent)event;

    AccessRight newAcr = new AccessRight();
    newAcr.setAction(addEvent.getGrantedAction());
    newAcr.setCriteria(addEvent.getGrantCriteria());
    newAcr.setDataType(addEvent.getGrantedDataType());
    newAcr.setDescr(addEvent.getDescription());
    newAcr.setFeature(addEvent.getFeature());
    newAcr.setRoleUID(addEvent.getRoleUID());

    return newAcr;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    AddAccessRightEvent addEvent = (AddAccessRightEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             AccessRight.ENTITY_NAME,
           };
  }

  protected Long createEntity(AbstractEntity entity) throws java.lang.Exception
  {
    return ActionHelper.getACLManager().createAccessRight((AccessRight)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAccessRightToMap((AccessRight)entity);
  }

  // ***************************** Own Methods &&&&&&&&&&&&&&&&&&&&&&&&&&
}