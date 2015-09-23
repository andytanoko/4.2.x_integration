/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILicenseLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.license.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for LicenseBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface ILicenseLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new License.
   *
   * @param license The License entity.
   * @return EJBLocalObject as a proxy to LicenseBean for the created License.
   */
  public ILicenseLocalObj create(IEntity license)
    throws CreateException;

  /**
   * Load a LicenseBean
   *
   * @param primaryKey The primary key to the License record
   * @return EJBLocalObject as a proxy to the loaded LicenseBean.
   */
  public ILicenseLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find License records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the ILicenseLocalObjs for the found License(s).
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}