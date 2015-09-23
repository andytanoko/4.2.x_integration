/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2009 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteArchiveAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2009    Ong Eu Soon         Created
 */
package com.gridnode.gtas.server.dbarchive.actions;

import com.gridnode.gtas.events.dbarchive.DeleteArchiveEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.gtas.server.dbarchive.facade.ejb.IArchiveManagerHome;
import com.gridnode.gtas.server.dbarchive.facade.ejb.IArchiveManagerObj;
import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;

public class DeleteArchiveAction extends AbstractDeleteEntityAction
{ 
  
	private static final String ACTION_NAME = "DeleteArchiveAction";

  private IArchiveManagerObj getManager() throws ServiceLookupException
  {
    return (IArchiveManagerObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IArchiveManagerHome.class.getName(),
        IArchiveManagerHome.class,
        new Object[0]);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    return ((ArchiveMetaInfo) entity).canDelete();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity,Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    getManager().deleteArchive((Long) entity.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return getManager().getArchiveUIDs(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return getManager().getArchive(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getActionName()
   * @since GT 2.2 I1
   */
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getExpectedEventClass()
   * @since GT 2.2 I1
   */
  protected Class getExpectedEventClass()
  {
    return DeleteArchiveEvent.class;
  }
}