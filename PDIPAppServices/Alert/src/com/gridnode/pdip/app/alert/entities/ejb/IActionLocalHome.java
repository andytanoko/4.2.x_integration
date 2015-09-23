/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IActionLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 11 2002    Srinath	          Created
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

/**
 * This interface defines the method of creating the EJBobject interface for the ActionBean.
 *
 * @author Srinath
 *
 */

public interface IActionLocalHome extends EJBLocalHome
{
  /**
   * Create a new Feature.
   *
   * @param action The action entity.
   * @return EJBLocalObject as a proxy to ActionBean for the created action.
   */
  public IActionLocalObj create(IEntity action) throws CreateException;

  /**
   * Load an ActionBean
   *
   * @param primaryKey The primary key to the Action record
   * @return EJBLocalObject as a proxy to the loaded ActionBean.
   */
  public IActionLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find Action records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IActionLocalObjs for the found Action.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
}