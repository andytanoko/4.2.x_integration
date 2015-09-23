/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IChannelInfoLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 06 2002    Goh Kan Mun             Created
 * Oct 20 2005    Neo Sok Lay             The throws clause of a create<METHOD> 
 *                                        create method must include javax.ejb.CreateException
 */

package com.gridnode.pdip.app.channel.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import java.util.Collection;

/**
 * This interface defines the methods of the ChannelInfoBean.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IChannelInfoLocalHome extends EJBLocalHome
{
  /**
   * Create a new local object with the specified ChannelInfo entity.
   *
   * @param role The ChannelInfo entity.
   *
   * @return EJBLocalObject as a proxy to ChannelInfoBean for the created ChannelInfo.
   */
  public IChannelInfoLocalObj create(IEntity channelInfo) throws CreateException;

  /**
   * Load a ChannelInfo local object.
   *
   * @param primaryKey The primary key to the ChannelInfo record
   * @return EJBLocalObject as a proxy to the loaded ChannelInfoBean.
   */
  public IChannelInfoLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ChannelInfo local object, using a data filter.
   *
   * @param filter The data filter.
   *
   * @return A Collection of the IChannelInfoLocalObjs for the found ChannelInfo.
   */
  public Collection findByFilter(IDataFilter filter) throws FinderException;

}