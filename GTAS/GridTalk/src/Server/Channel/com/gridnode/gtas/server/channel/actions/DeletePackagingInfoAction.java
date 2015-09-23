/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteCommInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh           Created
 * Jul 15 2003    Neo Sok Lay         Extend from AbstractDeleteEntityAction
 */

package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.events.channel.DeletePackagingInfoEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;

public class DeletePackagingInfoAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 432554829963890295L;
	private static final String ACTION_NAME = "DeletePackagingInfoAction";

  private IChannelManagerObj getManager() throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IChannelManagerHome.class.getName(),
        IChannelManagerHome.class,
        new Object[0]);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    return ((PackagingInfo) entity).canDelete();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity,Number)
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    getManager().deletePackigingInfo((Long) entity.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return getManager().getPackagingInfoUIDs(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return getManager().getPackagingInfo(filter);
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
    return DeletePackagingInfoEvent.class;
  }
}