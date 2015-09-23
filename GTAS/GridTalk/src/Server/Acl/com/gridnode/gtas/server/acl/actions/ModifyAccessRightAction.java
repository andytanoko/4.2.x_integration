/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ModifyAccessRightAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 30 2002    Neo Sok Lay         Created
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractUpdateEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jun 21 2002    Neo Sok Lay         Allow update of Feature & Action.
 * Jul 25 2002    Neo Sok Lay         Return updated entity to client.
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Map;

import com.gridnode.gtas.events.acl.ModifyAccessRightEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.base.acl.model.AccessRight;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the modification of a role's access right.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class ModifyAccessRightAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2407142102688726729L;

	public static final String ACTION_NAME = "ModifyAccessRightAction";

  private AccessRight _acrToUpdate;

  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
    ActionHelper.getACLManager().updateAccessRight(
      (AccessRight)entity);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    ModifyAccessRightEvent modEvent = (ModifyAccessRightEvent)event;

    _acrToUpdate.setCriteria(modEvent.getUpdCriteria());
    _acrToUpdate.setDataType(modEvent.getUpdDataType());
    _acrToUpdate.setDescr(modEvent.getUpdDescription());

    if (!ActionHelper.isEmpty(modEvent.getUpdAction()))
      _acrToUpdate.setAction(modEvent.getUpdAction());

    if (!ActionHelper.isEmpty(modEvent.getUpdFeature()))
      _acrToUpdate.setFeature(modEvent.getUpdFeature());

    return _acrToUpdate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    ModifyAccessRightEvent modEvent = (ModifyAccessRightEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             AccessRight.ENTITY_NAME,
             modEvent.getAccessRightUID(),
           };
  }

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return ModifyAccessRightEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    ModifyAccessRightEvent modEvent = (ModifyAccessRightEvent)event;

    _acrToUpdate = ActionHelper.verifyAccessRight(modEvent.getAccessRightUID());

     verifyFeatureActionDataType(_acrToUpdate, modEvent);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getACLManager().getAccessRight(key);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAccessRightToMap((AccessRight)entity);
  }


  // ****************** Own methods **************************

  private void verifyFeatureActionDataType(AccessRight acr, ModifyAccessRightEvent event)
    throws Exception
  {
    if (ActionHelper.isEmpty(event.getUpdFeature()))
      ActionHelper.verifyFeatureActionDataType(
        acr.getFeature(), acr.getAction(), event.getUpdDataType());
    else
      ActionHelper.verifyFeatureActionDataType(
        event.getUpdFeature(), event.getUpdAction(), event.getUpdDataType());
  }

}