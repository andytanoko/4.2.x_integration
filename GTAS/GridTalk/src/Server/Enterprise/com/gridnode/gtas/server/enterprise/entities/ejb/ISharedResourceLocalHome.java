/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISharedResourceLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for SharedResourceBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface ISharedResourceLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new SharedResource.
   *
   * @param sharedResource The SharedResource entity.
   * @return EJBLocalObject as a proxy to SharedResourceBean for the created SharedResource.
   */
  public ISharedResourceLocalObj create(IEntity sharedResource)
    throws CreateException;

  /**
   * Load a SharedResourceBean
   *
   * @param primaryKey The primary key to the SharedResource record
   * @return EJBLocalObject as a proxy to the loaded SharedResourceBean.
   */
  public ISharedResourceLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find SharedResource records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the ISharedResourceLocalObjs for the found BusinessEntities.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}