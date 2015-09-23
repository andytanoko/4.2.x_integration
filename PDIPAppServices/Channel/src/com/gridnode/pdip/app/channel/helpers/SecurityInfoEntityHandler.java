/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfoEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 26 2002    Jagadeesh               Created
 * Mar 05 2004    Neo Sok Lay             Check Duplicate on update.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay             GNDB00026947: To inform ChannelInfoEntityHandler
 *                                        on update of SecurityInfo.                                   
 */
package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.entities.ejb.ISecurityInfoLocalHome;
import com.gridnode.pdip.app.channel.entities.ejb.ISecurityInfoLocalObj;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
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
public class SecurityInfoEntityHandler extends LocalEntityHandler
{

  private static final String ENTITY_NAME = SecurityInfo.ENTITY_NAME;
  private static final Object lock = new Object();

  public SecurityInfoEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of SecurityInfoEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  public static SecurityInfoEntityHandler getInstance()
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
            new SecurityInfoEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of SecurityInfoEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  private static SecurityInfoEntityHandler getFromEntityHandlerFactory()
  {
    return (SecurityInfoEntityHandler) EntityHandlerFactory.getHandlerFor(
      ENTITY_NAME,
      true);
  }

  protected Class getHomeInterfaceClass() throws Exception
	{
		return ISecurityInfoLocalHome.class;
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
      ISecurityInfoLocalHome.class.getName(),
      ISecurityInfoLocalHome.class);
  }*/

  protected Class getProxyInterfaceClass() throws Exception
  {
    return ISecurityInfoLocalObj.class;
  }
  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#update(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  public void update(IEntity entity) throws Throwable
  {
  	/*NSL20060412
    DAOHelper.getInstance().checkDuplicate((SecurityInfo) entity, true);
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
    DAOHelper.getInstance().checkDuplicate((SecurityInfo) entity, true);
		IEntity updEntity = super.updateEntity(entity);
		ChannelInfoEntityHandler.getInstance().securityProfileChanged((SecurityInfo)updEntity);
		return updEntity;
	}
  
  
}
