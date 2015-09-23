/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingInfoEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 26 2002    Jagadeesh               Created
 * Mar 05 2004    Neo Sok Lay             Check Duplicate on update
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay             GNDB00026947: To inform ChannelInfoEntityHandler
 *                                        on update of PackagingInfo.                                   
 */
package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.entities.ejb.IPackagingInfoLocalHome;
import com.gridnode.pdip.app.channel.entities.ejb.IPackagingInfoLocalObj;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the PackagingInfoBean.
 *
 * @author Jagadeesh.
 *
 * @version GT 4.0
 * @since 2.0
 */
public class PackagingInfoEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = PackagingInfo.ENTITY_NAME;
  private static final Object lock = new Object();

  public PackagingInfoEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of PackagingInfoEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  public static PackagingInfoEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
          EntityHandlerFactory.putEntityHandler(
            ENTITY_NAME,
            true,
            new PackagingInfoEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of PackagingInfoEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  private static PackagingInfoEntityHandler getFromEntityHandlerFactory()
  {
    return (PackagingInfoEntityHandler) EntityHandlerFactory.getHandlerFor(
      ENTITY_NAME,
      true);
  }

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IPackagingInfoLocalHome.class;
	}

	/**
   * Look Up the home interface in the local context.
   * @return LocalHome Object.
   * @throws Exception
   * @since   2.0
   * @version 2.0
   *//*
  protected Object getHome() throws Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IPackagingInfoLocalHome.class.getName(),
      IPackagingInfoLocalHome.class);
  }*/

  protected Class getProxyInterfaceClass() throws Exception
  {
    return IPackagingInfoLocalObj.class;
  }
  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#update(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  public void update(IEntity entity) throws Throwable
  {
  	/*NSL20060412
    DAOHelper.getInstance().checkDuplicate((PackagingInfo) entity, true);
    super.update(entity);
    */
    updateEntity(entity);
  }

	/**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    DAOHelper.getInstance().checkDuplicate((PackagingInfo) entity, true);
		IEntity updEntity = super.updateEntity(entity);
    ChannelInfoEntityHandler.getInstance().packagingProfileChanged((PackagingInfo)updEntity);		
    return updEntity;
	}
  
}
