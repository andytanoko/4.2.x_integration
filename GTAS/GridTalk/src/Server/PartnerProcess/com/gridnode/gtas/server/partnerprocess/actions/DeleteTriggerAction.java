/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteTriggerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Cater for changes in AbstractDeleteEntityAction.
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import com.gridnode.gtas.events.partnerprocess.DeleteTriggerEvent;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

/**
 * This Action class handles the deletion of a Trigger.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteTriggerAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5754846116158221983L;
	public static final String ACTION_NAME = "DeleteTriggerAction";

  protected Class getExpectedEventClass()
  {
    return DeleteTriggerEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    return ((Trigger) entity).canDelete();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity, Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    ActionHelper.getManager().deleteTrigger((Long) entity.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findTriggersKeys(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findTriggers(filter);
  }
}