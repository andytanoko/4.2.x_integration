/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBizEntityLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for BizEntityBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IBizEntityLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new BusinessEntity.
   *
   * @param bizEntity The BusinessEntity entity.
   * @return EJBLocalObject as a proxy to BizEntityBean for the created BusinessEntity.
   */
  public IBizEntityLocalObj create(IEntity bizEntity)
    throws CreateException;

  /**
   * Load a BizEntityBean
   *
   * @param primaryKey The primary key to the BusinessEntity record
   * @return EJBLocalObject as a proxy to the loaded BizEntityBean.
   */
  public IBizEntityLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find BusinessEntity records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IBizEntityLocalObjs for the found BusinessEntities.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}