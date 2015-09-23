/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IXpathMappingLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 13 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for XpathMappingBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IXpathMappingLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new XpathMapping
   *
   * @param xpathMapping The XpathMapping entity.
   * @return EJBLocalObject as a proxy to XpathMappingBean for the
   *         created XpathMapping.
   */
  public IXpathMappingLocalObj create(IEntity xpathMapping)
    throws CreateException;

  /**
   * Load a XpathMappingBean
   *
   * @param primaryKey The primary key to the XpathMapping record
   * @return EJBLocalObject as a proxy to the loaded XpathMappingBean.
   */
  public IXpathMappingLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find XpathMapping records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IXpathMappingLocalObj for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}