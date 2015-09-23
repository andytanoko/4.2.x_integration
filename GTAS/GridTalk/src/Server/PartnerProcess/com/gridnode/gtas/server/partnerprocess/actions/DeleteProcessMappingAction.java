/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteProcessMappingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 * Jul 15 2003    Neo Sok Lay         Cater for changes in AbstractDeleteEntityAction.
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import com.gridnode.gtas.events.partnerprocess.DeleteProcessMappingEvent;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

/**
 * This Action class handles the deletion of a ProcessMapping.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0 I7
 */
public class DeleteProcessMappingAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1734721671077920658L;
	public static final String ACTION_NAME = "DeleteProcessMappingAction";

  protected Class getExpectedEventClass()
  {
    return DeleteProcessMappingEvent.class;
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
    return ((ProcessMapping) entity).canDelete();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity, Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    ActionHelper.getManager().deleteProcessMapping(
      (Long) entity.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findProcessMappingKeysByFilter(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findProcessMappingByFilter(filter);
  }
}