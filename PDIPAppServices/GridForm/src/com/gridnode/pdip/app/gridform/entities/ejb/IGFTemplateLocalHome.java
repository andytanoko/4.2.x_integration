/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGFTemplateLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.entities.ejb;

import com.gridnode.pdip.app.gridform.model.GFTemplate;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Local home interface for GFTemplateBean.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public interface IGFTemplateLocalHome extends EJBLocalHome
{
  /**
   * Create a new GFTemplate.
   *
   * @param template The GFTemplate entity.
   * @return The local remote interface for GFTemplateBean.
   * @since 2.0
   */
  public IGFTemplateLocalObj create(IEntity template) throws CreateException;

  /**
   * Find a GFTemplate by primary key.
   *
   * @param primaryKey The primary key of the GFTemplate record.
   * @return The local remote interface for GFTemplateBean.
   * @since 2.0
   */
  public IGFTemplateLocalObj findByPrimaryKey(Long primaryKey) throws FinderException;

  /**
   * Find GFTemplate records using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of IGFTemplateLocalObj found.
   * @since 2.0
   */
  public Collection findByFilter(IDataFilter filter) throws FinderException;
}