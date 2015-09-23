/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICommInfoLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 13 2002    Goh Kan Mun             Created
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
 * This interface defines the methods of the CommInfoBean.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface ICommInfoLocalHome extends EJBLocalHome
{
  /**
   * Create a new local object with the specified CommInfo entity.
   *
   * @param role The CommInfo entity.
   *
   * @return EJBLocalObject as a proxy to CommInfoBean for the created CommInfo.
   */
  public ICommInfoLocalObj create(IEntity CommInfo) throws CreateException;

  /**
   * Load a CommInfo local object.
   *
   * @param primaryKey The primary key to the CommInfo record
   * @return EJBLocalObject as a proxy to the loaded CommInfoBean.
   */
  public ICommInfoLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find CommInfo local object, using a data filter.
   *
   * @param filter The data filter.
   *
   * @return A Collection of the ICommInfoLocalObjs for the found CommInfo.
   */
  public Collection findByFilter(IDataFilter filter) throws FinderException;

}