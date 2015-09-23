/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGFDefinitionLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.entities.ejb;

import com.gridnode.pdip.app.gridform.model.GFDefinition;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Local home interface for GFDefinitionBean.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public interface IGFDefinitionLocalHome extends EJBLocalHome
{
  /**
   * Create a new GFDefinition.
   *
   * @param definition The GFDefinition entity.
   * @return The local remote interface for GFDefinitionBean.
   * @since 2.0
   */
  public IGFDefinitionLocalObj create(IEntity definition) throws CreateException;

  /**
   * Find a GFDefinition by primary key.
   *
   * @param primaryKey The primary key of the GFDefinition record.
   * @return The local remote interface for GFDefinitionBean.
   * @since 2.0
   */
  public IGFDefinitionLocalObj findByPrimaryKey(Long primaryKey) throws FinderException;

  /**
   * Find GFDefinition records using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of IGFDefinitionLocalObj found.
   * @since 2.0
   */
  public Collection findByFilter(IDataFilter filter) throws FinderException;
}