/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertActionLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 14 2002    Srinath	             Created
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

/**
 * This interface defines the method of creating the EJBobject interface for the AlertActionBean.
 *
 * @author Srinath
 *
 */

public interface IAlertActionLocalHome extends EJBLocalHome
{
  /**
   * Create a new AlertAction.
   *
   * @param alertAction The AlertAction entity.
   * @return EJBLocalObject as a proxy to AlertActionBean for the created AlertAction.
   */
  public IAlertActionLocalObj create(IEntity alertAction) throws CreateException;

  /**
   * Load a AlertActionBean
   *
   * @param primaryKey The primary key to the AlertAction record
   * @return EJBLocalObject as a proxy to the loaded AlertActionBean.
   */
  public IAlertActionLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find AlertAction records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IAlertActionObjs for the found AlertAction.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}