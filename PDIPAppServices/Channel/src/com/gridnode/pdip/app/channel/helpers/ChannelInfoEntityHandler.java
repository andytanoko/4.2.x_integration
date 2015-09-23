/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfoEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 06 2002    Goh Kan Mun             Created
 * Jun 14 2002    Goh Kan Mun             Modified - add a findByCommInfo method
 * Jul 03 2002    Goh Kan Mun             Modified - Change in Channel model.
 *                                                 - Delete the retrieve methods. (Replace
 *                                                   with the filter).
 * Jul 25 2002	  Jagadeesh				  Modified - add getgetHome and
 *                                                    getProxyInterfaceClass Methods.
 * Mar 05 2004    Neo Sok Lay             Check duplicate on update
 * Oct 31 2005    Neo Sok Lay             1. Remove implementation for getHome()
 *                                        2. Implement getHomeInterfaceClass()
 *                                        3. Remove use of getEntityByXXXForReadOnly()
 * Apr 12 2006    Neo Sok Lay             GNDB00026947: Update the channelinfo(s) on change
 *                                        of their profiles                                       
 */
package com.gridnode.pdip.app.channel.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.channel.entities.ejb.IChannelInfoLocalHome;
import com.gridnode.pdip.app.channel.entities.ejb.IChannelInfoLocalObj;
import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ChannelInfoBean.
 *
 * @author Goh Kan Mun
 *
 * @version GT 4.0
 * @since 2.0
 */
public class ChannelInfoEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = ChannelInfo.ENTITY_NAME;
  private static final Object lock = new Object();
  
  public ChannelInfoEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of RoleEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  public static ChannelInfoEntityHandler getInstance()
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
            new ChannelInfoEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of RoleEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  private static ChannelInfoEntityHandler getFromEntityHandlerFactory()
  {
    return (ChannelInfoEntityHandler) EntityHandlerFactory.getHandlerFor(
      ENTITY_NAME,
      true);
  }

  /**
   * Find a collection of ChannelInfo with the specified CommInfo UId.
   *
   * @param commInfoUId    The uId of the commInfo entity.
   *
   * @return The ChannelInfo that is linked to the specified commInfo or <B>null</B> if
   * none found.
   *
   * @exception Throwable thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection findByCommInfo(Long commInfoUId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      ChannelInfo.TPT_COMM_INFO,
      filter.getEqualOperator(),
      commInfoUId,
      false);
    //return getEntityByFilterForReadOnly(filter);
    return getEntityByFilter(filter);
  }

  /**
   * Find a collection of ChannelInfo with the specified PackagingInfo UId.
   *
   * @param packagingInfoUId    The uId of the PackagingInfo entity.
   *
   * Pls.Note here that ChannelInfo refernece to PackagingInfo, which form the user
   * prespective is PackagingProfile. To make sense of it all this constant is named
   * as PACKAGING_PROFILE not rather PACKAGING_INFO (which is logical).
   *
   * @return The ChannelInfo that is linked to the specified packagingInfo or <B>null</B> if
   * none found.
   *
   * @exception Throwable thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */

  public Collection findByPackagingInfo(Long packagingInfoUId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      ChannelInfo.PACKAGING_PROFILE,
      filter.getEqualOperator(),
      packagingInfoUId,
      false);
    //return getEntityByFilterForReadOnly(filter);
    return getEntityByFilter(filter);
  }

  /**
   * Find a collection of ChannelInfo with the specified PackagingInfo UId.
   *
   * @param securityInfoUId    The uId of the PackagingInfo entity.
   *
   * Pls.Note here that ChannelInfo refernece to SecurityInfo, which form the user
   * prespective is SecurityProfile. To make sense of it all this constant is named
   * as SECURITY_PROFILE not rather SECURITY_INFO (which is logical).
   *
   * @return The ChannelInfo that is linked to the specified securityInfo or <B>null</B> if
   * none found.
   *
   * @exception Throwable thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */

  public Collection findBySecurityInfo(Long securityInfoUId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      ChannelInfo.SECURITY_PROFILE,
      filter.getEqualOperator(),
      securityInfoUId,
      false);
    //return getEntityByFilterForReadOnly(filter);
    return getEntityByFilter(filter);
  }

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IChannelInfoLocalHome.class;
	}

	/**
   * Look Up the home interface in the local context.
   * @return LocalHome Object.
   * @throws Exception
   */
/*
  protected Object getHome() throws Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IChannelInfoLocalHome.class.getName(),
      IChannelInfoLocalHome.class);
  }*/

  protected Class getProxyInterfaceClass() throws Exception
  {
    return IChannelInfoLocalObj.class;
  }

  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#update(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  public void update(IEntity entity) throws Throwable
  {
    DAOHelper.getInstance().checkDuplicate((ChannelInfo) entity, true);
    super.update(entity);
  }
 
  
  /**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    DAOHelper.getInstance().checkDuplicate((ChannelInfo) entity, true);
		return super.updateEntity(entity);
	}

	/**
   * To be invoked when a PackagingInfo has been updated. The ChannelInfo(s) associating with
   * the updated profile will be updated.
   * @param updProfile The updated PackagingInfo.
   */
  protected void packagingProfileChanged(PackagingInfo updProfile)
  {
  	try
  	{
  		Collection channels = findByPackagingInfo((Long)updProfile.getKey());
  		for (Object o : channels)
  		{
  			ChannelInfo channel = (ChannelInfo)o;
  			channel.setPackagingProfile(updProfile);
  			update(channel);
  		}
  	}
  	catch (Throwable t)
  	{
  		ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_CHANNEL_UPDATE,
  		                       "ChannelInfoEntityHandler", "packagingProfileChanged", "Unable to update affected channels: "+t.getMessage(), t);
  	}
  }
  
  /**
   * To be invoked when a SecurityInfo has been updated. The ChannelInfo(s) associating with
   * the updated profile will be updated.
   * @param updProfile The updated SecurityInfo.
   */
  protected void securityProfileChanged(SecurityInfo updProfile)
  {
  	try
  	{
  		Collection channels = findBySecurityInfo((Long)updProfile.getKey());
  		for (Object o : channels)
  		{
  			ChannelInfo channel = (ChannelInfo)o;
  			channel.setSecurityProfile(updProfile);
  			update(channel);
  		}
  	}
  	catch (Throwable t)
  	{
  		ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_CHANNEL_UPDATE,
  		                       "ChannelInfoEntityHandler", "securityProfileChanged", "Unable to update affected channels", t);
  	}
  }
  
  /**
   * To be invoked when a CommInfo has been updated. The ChannelInfo(s) associating with
   * the updated profile will be updated.
   * @param updProfile The updated CommInfo.
   */
  protected void commProfileChanged(CommInfo updProfile)
  {
  	try
  	{
  		Collection channels = findByCommInfo((Long)updProfile.getKey());
  		for (Object o : channels)
  		{
  			ChannelInfo channel = (ChannelInfo)o;
  			channel.setTptCommInfo(updProfile);
  			channel.setTptProtocolType(updProfile.getProtocolType());
  			update(channel);
  		}
  	}
  	catch (Throwable t)
  	{
  		ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_CHANNEL_UPDATE,
  		                       "ChannelInfoEntityHandler", "commProfileChanged", "Unable to update affected channels: "+t.getMessage(), t);
  	}
  }
  
}