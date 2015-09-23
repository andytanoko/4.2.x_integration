/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMappingEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay         Add method: updateEntity(IEntity entity)                                   
 */
package com.gridnode.gtas.server.partnerprocess.helpers;

import com.gridnode.gtas.server.partnerprocess.entities.ejb.IProcessMappingLocalHome;
import com.gridnode.gtas.server.partnerprocess.entities.ejb.IProcessMappingLocalObj;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ProcessMappingBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I7
 */
public final class ProcessMappingEntityHandler
  extends          LocalEntityHandler
{
  private ProcessMappingEntityHandler()
  {
    super(ProcessMapping.ENTITY_NAME);
  }

  /**
   * Get an instance of a ProcessMappingEntityHandler.
   */
  public static ProcessMappingEntityHandler getInstance()
  {
    ProcessMappingEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ProcessMapping.ENTITY_NAME, true))
    {
      handler = (ProcessMappingEntityHandler)EntityHandlerFactory.getHandlerFor(
                  ProcessMapping.ENTITY_NAME, true);
    }
    else
    {
      handler = new ProcessMappingEntityHandler();
      EntityHandlerFactory.putEntityHandler(ProcessMapping.ENTITY_NAME,
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
      IProcessMappingLocalHome.class.getName(),
      IProcessMappingLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IProcessMappingLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IProcessMappingLocalObj.class;
  }

  public void update(IEntity entity) throws java.lang.Throwable
  {
    ProcessMappingDAOHelper.getInstance().checkDuplicate(
      (ProcessMapping)entity, true);

    super.update(entity);
  }

  /**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    ProcessMappingDAOHelper.getInstance().checkDuplicate((ProcessMapping)entity, true);
		return super.updateEntity(entity);
	}

	public void removeByFilter(IDataFilter filter) throws java.lang.Throwable
  {
    Long[] affectedUIDs = (Long[])getKeyByFilterForReadOnly(filter).toArray(new Long[0]);
    for (int i=0; i<affectedUIDs.length; i++)
      remove(affectedUIDs[i]);
  }

  public void remove(Long uId) throws java.lang.Throwable
  {
    ProcessMappingDAOHelper.getInstance().checkCanDelete(
      (ProcessMapping)getEntityByKeyForReadOnly(uId));

    super.remove( uId);
  }


}