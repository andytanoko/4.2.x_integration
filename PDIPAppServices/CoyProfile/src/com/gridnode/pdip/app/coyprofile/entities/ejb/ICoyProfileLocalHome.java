/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICoyProfileLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.coyprofile.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for CoyProfileBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface ICoyProfileLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new CompanyProfile.
   *
   * @param coyProfile The CompanyProfile entity.
   * @return EJBLocalObject as a proxy to CoyProfileBean for the created CompanyProfile.
   */
  public ICoyProfileLocalObj create(IEntity coyProfile)
    throws CreateException;

  /**
   * Load a CoyProfileBean
   *
   * @param primaryKey The primary key to the CompanyProfile record
   * @return EJBLocalObject as a proxy to the loaded CoyProfileBean.
   */
  public ICoyProfileLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find CompanyProfile records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the ICoyProfileLocalObjs for the found CompanyProfile(s).
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}