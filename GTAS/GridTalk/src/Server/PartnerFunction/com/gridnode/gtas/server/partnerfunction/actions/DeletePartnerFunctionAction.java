/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeletePartnerFunctionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Cater for changes in AbstractDeleteEntityAction.
 */
package com.gridnode.gtas.server.partnerfunction.actions;

import com.gridnode.gtas.events.partnerfunction.DeletePartnerFunctionEvent;
import com.gridnode.gtas.server.partnerfunction.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerfunction.helpers.Logger;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This Action class handles the deletion of a PartnerFunction.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeletePartnerFunctionAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3712323811846448128L;
	public static final String ACTION_NAME = "DeletePartnerFunctionAction";

  protected Class getExpectedEventClass()
  {
    return DeletePartnerFunctionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  private void deleteActivites(List activities)
  {
    try
    {
      for (Iterator i = activities.iterator(); i.hasNext();)
      {
        Long uid = new Long(i.next().toString());
        ActionHelper.getManager().deleteWorkflowActivity(uid);
      }
    }
    catch (Exception ex)
    {
      Logger.warn("Error deleting workflow activities", ex);
    }
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    return ((PartnerFunction) entity).canDelete();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity, Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    PartnerFunction partnerFunction = (PartnerFunction) entity;

    List activities = partnerFunction.getWorkflowActivityUids();
    deleteActivites(activities);
    ActionHelper.getManager().deletePartnerFunction(
      (Long) partnerFunction.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findPartnerFunctionsKeys(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findPartnerFunctions(filter);
  }

}