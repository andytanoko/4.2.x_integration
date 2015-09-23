/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertListLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 26 2002    Srinath	          Created
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

/**
 * This interface defines the method of creating the EJBobject interface for the AlertListBean.
 *
 * @author Srinath
 *
 */

public interface IAlertListLocalHome extends EJBLocalHome
{
  /**
   * Create a new Alert List.
   *
   * @param alertList The AlertList entity.
   * @return EJBLocalObject as a proxy to AlertListBean for the created AlertList.
   */
  public IAlertListLocalObj create(IEntity alertList) throws CreateException;

  /**
   * Load a AlertListBean
   *
   * @param primaryKey The primary key to the AlertList record
   * @return EJBLocalObject as a proxy to the loaded AlertListBean.
   */
  public IAlertListLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find AlertList records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IAlertListLocalObjs for the found AlertList.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}