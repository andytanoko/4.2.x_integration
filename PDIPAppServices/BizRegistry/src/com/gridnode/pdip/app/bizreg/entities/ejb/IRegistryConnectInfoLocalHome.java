/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistryConnectInfoLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 24 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import java.util.Collection;

/**
 * Local Home interface for RegistryConnectInfoBean 
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IRegistryConnectInfoLocalHome extends EJBLocalHome
{
  /**
   * Create a new RegistryConnectInfo.
   *
   * @param mapping The RegistryConnectInfo entity.
   * @return EJBLocalObject as a proxy to RegistryConnectInfoBean 
   * for the created RegistryConnectInfo.
   */
  public IRegistryConnectInfoLocalObj create(IEntity mapping)
    throws CreateException;

  /**
   * Load a RegistryConnectInfoBean
   *
   * @param primaryKey The primary key to the RegistryConnectInfo record
   * @return EJBLocalObject as a proxy to the loaded RegistryConnectInfoBean.
   */
  public IRegistryConnectInfoLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find RegistryConnectInfo records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IRegistryConnectInfoLocalObjs for the found BusinessEntities.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
  
}
