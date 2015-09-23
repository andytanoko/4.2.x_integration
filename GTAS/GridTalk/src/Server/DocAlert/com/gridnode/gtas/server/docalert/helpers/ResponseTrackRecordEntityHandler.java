/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay         Add method: updateEntity(IEntity entity)                                   
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.docalert.entities.ejb.IResponseTrackRecordLocalHome;
import com.gridnode.gtas.server.docalert.entities.ejb.IResponseTrackRecordLocalObj;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ResponseTrackRecordBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I7
 */
public final class ResponseTrackRecordEntityHandler
  extends          LocalEntityHandler
{
  private ResponseTrackRecordEntityHandler()
  {
    super(ResponseTrackRecord.ENTITY_NAME);
  }

  /**
   * Get an instance of a ResponseTrackRecordEntityHandler.
   */
  public static ResponseTrackRecordEntityHandler getInstance()
  {
    ResponseTrackRecordEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ResponseTrackRecord.ENTITY_NAME, true))
    {
      handler = (ResponseTrackRecordEntityHandler)EntityHandlerFactory.getHandlerFor(
                  ResponseTrackRecord.ENTITY_NAME, true);
    }
    else
    {
      handler = new ResponseTrackRecordEntityHandler();
      EntityHandlerFactory.putEntityHandler(ResponseTrackRecord.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IResponseTrackRecordLocalHome.class.getName(),
      IResponseTrackRecordLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IResponseTrackRecordLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IResponseTrackRecordLocalObj.class;
  }

  public void update(IEntity entity) throws java.lang.Throwable
  {
    ResponseTrackRecordDAOHelper.getInstance().checkDuplicate(
      (ResponseTrackRecord)entity, true);

    super.update(entity);
  }

	/**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    ResponseTrackRecordDAOHelper.getInstance().checkDuplicate((ResponseTrackRecord)entity, true);
		return super.updateEntity(entity);
	}

}