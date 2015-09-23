/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertCategoryLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Dec 05 2002    Srinath	          Created
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

/**
 * This interface defines the method of creating the EJBobject interface for the categoryBean.
 *
 * @author Srinath
 *
 */

public interface IAlertCategoryLocalHome extends EJBLocalHome
{
  /**
   * Create a new category.
   *
   * @param category The category entity.
   * @return EJBLocalObject as a proxy to categoryBean.
   */
  public IAlertCategoryLocalObj create(IEntity category) throws CreateException;

  /**
   * Load a categoryBean
   *
   * @param primaryKey The primary key to the category record
   * @return EJBLocalObject as a proxy to the loaded categoryBean.
   */
  public IAlertCategoryLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find Feature records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IcategoryLocalObjs for the found category.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
}