/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteBusinessEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * Jul 14 2003    Neo Sok Lay         Cater for changes in AbstractDeleteEntityAction.
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.DeleteBusinessEntityEvent;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This Action class handles the deletion of one or more BusinessEntities.
 * This will only mark the BusinessEntities as deleted. Subsequent retrieval of
 * list of accounts will exclude this "deleted" account.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0 I4
 */
public class DeleteBusinessEntityAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2111579828924551626L;

	public static final String ACTION_NAME = "DeleteBusinessEntityAction";

  private Collection _beListToDelete;

  // **************** AbstractDeleteEntityAction methods *******************

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    _beListToDelete = new ArrayList();
    _beListToDelete.add(entity.getKey());
    boolean canDelete = false;
    try
    {
      ActionHelper.verifyNonGtasPartnerBusinessEntity(
        _beListToDelete,
        "delete");

      canDelete = ((BusinessEntity) entity).canDelete();
    }
    catch (Exception ex)
    {
    }

    return canDelete;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity,Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    ActionHelper.getBizRegManager().markDeleteBusinessEntity(
      (Long) entity.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return ActionHelper.getBizRegManager().findBusinessEntitiesKeys(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.getBizRegManager().findBusinessEntities(filter);
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return DeleteBusinessEntityEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}