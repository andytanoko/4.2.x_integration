/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RemoveAccessRightAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 20 2002    Neo Sok Lay         Created
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractDeleteEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jul 14 2003    Neo Sok Lay         Cater for changes in AbstractDeleteEntityAction.
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.RemoveAccessRightEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;

import com.gridnode.pdip.base.acl.model.AccessRight;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

/**
 * This Action class handles the removal of access right from a Role.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class RemoveAccessRightAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2907113675286328674L;
	public static final String ACTION_NAME = "RemoveAccessRightAction";

  // **************** AbstractDeleteEntityAction methods *******************

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return ActionHelper.getACLManager().getAccessRightsKeys(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    return ((AccessRight) entity).canDelete();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity,Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    ActionHelper.getACLManager().removeAccessRight(
      (Long) entity.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.getACLManager().getAccessRights(filter);
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return RemoveAccessRightEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* Own methods ***************************************

}