/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridTalkLicenseLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 04 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for GridTalkLicense
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public interface IGridTalkLicenseLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new GridTalkLicense
   *
   * @param license The GridTalkLicense entity.
   * @return EJBLocalObject as a proxy to GridTalkLicenseBean for the created
   *         GridTalkLicense.
   */
  public IGridTalkLicenseLocalObj create(IEntity license)
    throws CreateException;

  /**
   * Load a GridTalkLicenseBean
   *
   * @param primaryKey The primary key to the GridTalkLicense record
   * @return EJBLocalObject as a proxy to the loaded GridTalkLicenseBean.
   */
  public IGridTalkLicenseLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find GridTalkLicense records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IGridTalkLicenseLocalObjs for the found licenses.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}