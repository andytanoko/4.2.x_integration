/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeletePortAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Created
 * Jul 14 2003    Neo Sok Lay         Cater for changes in AbstractDeleteEntityAction.
 */
package com.gridnode.gtas.server.backend.actions;

import com.gridnode.gtas.events.backend.DeletePortEvent;
import com.gridnode.gtas.server.backend.helpers.ActionHelper;
import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

/**
 * This Action class handles the deletion of a Port.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeletePortAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 235974041587432065L;
	public static final String ACTION_NAME = "DeletePortAction";

  protected Class getExpectedEventClass()
  {
    return DeletePortEvent.class;
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
    return ((Port) entity).canDelete();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity,Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    ActionHelper.getManager().deletePort((Long) entity.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findPortsKeys(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findPorts(filter);
  }

}