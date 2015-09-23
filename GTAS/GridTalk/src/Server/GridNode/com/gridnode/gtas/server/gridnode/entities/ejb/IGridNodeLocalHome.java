/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridNodeLocalHome.java
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
 * LocalHome interface for GridNodeBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface IGridNodeLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new GridNode.
   *
   * @param gridnode The GridNode entity.
   * @return EJBLocalObject as a proxy to GridNodeBean for the created GridNode.
   */
  public IGridNodeLocalObj create(IEntity gridnode)
    throws CreateException;

  /**
   * Load a GridNodeBean
   *
   * @param primaryKey The primary key to the GridNode record
   * @return EJBLocalObject as a proxy to the loaded GridNodeBean.
   */
  public IGridNodeLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find GridNode records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IGridNodeLocalObjs for the found BusinessEntities.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}