/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteProcessInstanceAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 15 2003    Neo Sok Lay         Cater for changes in AbstractDeleteEntityAciton.
 * Sep 04 2003    Guo Jianyu          Modified deleteEntity() to delete GridDoc too.
 */
package com.gridnode.gtas.server.rnif.actions;

import java.util.Collection;

import com.gridnode.gtas.events.rnif.DeleteProcessInstanceEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DomainValueFilter;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the deletion of one or more ProcessInstances.
 *
 * @version GT 2.2 I1
 */
public class DeleteProcessInstanceAction extends AbstractDeleteEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2006770871401518704L;
	public static final String ACTION_NAME = "DeleteProcessInstanceAction";

  // **************** AbstractDeleteEntityAction methods *******************

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    return true;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity, Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    DeleteProcessInstanceEvent event =
      (DeleteProcessInstanceEvent) getCurrentEvent();

    //jianyu - a quick change to enable deletion of GridDocs.
    boolean deleteGdoc = true; //Boolean.TRUE.equals(event.getDeleteGridDoc());

    ProcessInstanceActionHelper.deleteProcessInstance(
      (Long) entity.getFieldValue(keyId),
      deleteGdoc);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    DomainValueFilter valueFilter = (DomainValueFilter) filter.getValueFilter();

    return ProcessInstanceActionHelper.findProcessInstanceKeysByUIDs(
      valueFilter.getDomainValues());
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    DomainValueFilter valueFilter = (DomainValueFilter) filter.getValueFilter();

    return ProcessInstanceActionHelper.findProcessInstancesByUIDs(
      valueFilter.getDomainValues());
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return DeleteProcessInstanceEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}