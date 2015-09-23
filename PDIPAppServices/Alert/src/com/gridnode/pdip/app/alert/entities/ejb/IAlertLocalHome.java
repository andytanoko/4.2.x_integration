/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	          Created
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;


/**
 * This interface defines the method of creating the EJBobject interface for the AlertBean.
 *
 * @author Srinath
 *
 */

public interface IAlertLocalHome extends EJBLocalHome
{
  /**
   * Create a new Alert.
   *
   * @param alert The Alert entity.
   * @return EJBLocalObject as a proxy to AlertBean for the created alert.
   */
  public IAlertLocalObj create(IEntity alert) throws CreateException;

  /**
   * Load a AlertBean
   *
   * @param primaryKey The primary key to the alert record
   * @return EJBLocalObject as a proxy to the loaded AlertBean.
   */
  public IAlertLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find alert records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IAlertLocalObjs for the found alert.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
}