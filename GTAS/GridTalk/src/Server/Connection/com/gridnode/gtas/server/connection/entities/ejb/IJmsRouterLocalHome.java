/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJmsRouterLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for JmsRouterBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IJmsRouterLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new JmsRouter.
   *
   * @param connectionStatus The JmsRouter entity.
   * @return EJBLocalObject as a proxy to JmsRouterBean for the created JmsRouter.
   */
  public IJmsRouterLocalObj create(IEntity connectionStatus)
    throws CreateException;

  /**
   * Load a JmsRouterBean
   *
   * @param primaryKey The primary key to the JmsRouter record
   * @return EJBLocalObject as a proxy to the loaded JmsRouterBean.
   */
  public IJmsRouterLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find JmsRouter records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IJmsRouterLocalObjs for the found JmsRouters.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}