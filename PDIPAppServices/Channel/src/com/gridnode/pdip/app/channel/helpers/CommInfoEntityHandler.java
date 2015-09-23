/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfoEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 14 2002    Goh Kan Mun             Created
 * Jul 03 2002    Goh Kan Mun             Modified - Change in CommInfo model.
 *                                                 - Delete the retrieve methods. (Replace
 * Jul 25 2002	  Jagadeesh				        Modified - add getgetHome and
 *                                                    getProxyInterfaceClass Methods.
 * Mar 05 2004    Neo Sok Lay             Check duplicate on update.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay             GNDB00026947: To inform ChannelInfoEntityHandler
 *                                        on update of CommInfo.                                   
 */
package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.entities.ejb.ICommInfoLocalHome;
import com.gridnode.pdip.app.channel.entities.ejb.ICommInfoLocalObj;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the CommInfoBean.
 *
 * @author Goh Kan Mun
 *
 * @version GT 4.0
 * @since 2.0
 */
public class CommInfoEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = CommInfo.ENTITY_NAME;
  private static final Object lock = new Object();

  public CommInfoEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of CommInfoEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  public static CommInfoEntityHandler getInstance()
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
            new CommInfoEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of CommInfoEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  private static CommInfoEntityHandler getFromEntityHandlerFactory()
  {
    return (CommInfoEntityHandler) EntityHandlerFactory.getHandlerFor(
      ENTITY_NAME,
      true);
  }

  protected Class getHomeInterfaceClass() throws Exception
	{
		return ICommInfoLocalHome.class;
	}

	/**
   * Look Up the home interface in the local context.
   * @return LocalHome Object.
   * @throws Exception
   * @since   2.0
   * @version 2.0
   */
/*
  protected Object getHome() throws Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      ICommInfoLocalHome.class.getName(),
      ICommInfoLocalHome.class);
  }*/

  protected Class getProxyInterfaceClass() throws Exception
  {
    return ICommInfoLocalObj.class;
  }

  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#update(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  public void update(IEntity entity) throws Throwable
  {
  	/*NSL20060412
    DAOHelper.getInstance().checkDuplicate((CommInfo) entity, true);
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
    DAOHelper.getInstance().checkDuplicate((CommInfo) entity, true);
		IEntity updEntity = super.updateEntity(entity);
		ChannelInfoEntityHandler.getInstance().commProfileChanged((CommInfo)updEntity);
		return updEntity;
	}

}