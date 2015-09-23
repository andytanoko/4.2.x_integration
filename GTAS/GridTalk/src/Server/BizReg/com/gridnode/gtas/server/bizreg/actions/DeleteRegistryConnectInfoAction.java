/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteRegistryConnectInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 19 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Extend from AbstractDeleteEntityAction.
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.DeleteRegistryConnectInfoEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

import java.util.Collection;

/**
 * This event handles the DeleteRegistryConnectInfoEvent to delete
 * RegistryConnectInfo from the RegistryConnectInfoList.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DeleteRegistryConnectInfoAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6226729035478375633L;
	public static final String ACTION_NAME = "DeleteRegistryConnectInfoAction";

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#constructErrorResponse(com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.exceptions.TypedException)
   */
  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    return constructEventResponse(
              IErrorCode.DELETE_ENTITY_LIST_ERROR,
              new Object[]{}, 
              ex);  
  }
  
  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getActionName()
   */
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getExpectedEventClass()
   */
  protected Class getExpectedEventClass()
  {
    return DeleteRegistryConnectInfoEvent.class;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    RegistryConnectInfo connInfo = (RegistryConnectInfo)entity;
    return connInfo.canDelete();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(com.gridnode.pdip.framework.db.entity.IEntity, java.lang.Number)
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    ActionHelper.getBizRegManager().deleteRegistryConnectInfo(
      (Long)entity.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(com.gridnode.pdip.framework.db.filter.IDataFilter)
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return ActionHelper.getBizRegManager().findRegistryConnectInfoKeys(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(com.gridnode.pdip.framework.db.filter.IDataFilter)
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.getBizRegManager().findRegistryConnectInfos(filter);
  }

}
