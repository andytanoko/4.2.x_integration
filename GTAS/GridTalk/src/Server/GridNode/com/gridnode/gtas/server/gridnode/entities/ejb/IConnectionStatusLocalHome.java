/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionStatusLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for ConnectionStatusBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface IConnectionStatusLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new ConnectionStatus.
   *
   * @param connectionStatus The ConnectionStatus entity.
   * @return EJBLocalObject as a proxy to ConnectionStatusBean for the created ConnectionStatus.
   */
  public IConnectionStatusLocalObj create(IEntity connectionStatus)
    throws CreateException;

  /**
   * Load a ConnectionStatusBean
   *
   * @param primaryKey The primary key to the ConnectionStatus record
   * @return EJBLocalObject as a proxy to the loaded ConnectionStatusBean.
   */
  public IConnectionStatusLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ConnectionStatus records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IConnectionStatusLocalObjs for the found BusinessEntities.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}