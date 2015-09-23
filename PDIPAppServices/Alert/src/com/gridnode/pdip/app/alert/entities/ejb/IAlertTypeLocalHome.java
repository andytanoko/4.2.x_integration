/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertTypeLocalHome.java
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
 * This interface defines the method of creating the EJBobject interface for the AlertTypeBean.
 *
 * @author Srinath
 *
 */

public interface IAlertTypeLocalHome extends EJBLocalHome
{
  /**
   * Create a new alertType.
   *
   * @param alerttype The AlertType entity.
   * @return EJBLocalObject as a proxy to AlertTypeBean for the created AlertType.
   */
  public IAlertTypeLocalObj create(IEntity alerttype) throws CreateException;

  /**
   * Load a AlertTypeBean
   *
   * @param primaryKey The primary key to the AlertType record
   * @return EJBLocalObject as a proxy to the loaded AlertTypeBean.
   */
  public IAlertTypeLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find AlertType records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IAlertTypeLocalObjs for the found AlertType.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}