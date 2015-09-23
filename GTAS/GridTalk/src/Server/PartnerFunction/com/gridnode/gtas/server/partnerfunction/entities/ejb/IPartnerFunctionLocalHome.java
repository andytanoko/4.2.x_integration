/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerFunctionLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * LocalHome interface for PartnerFunctionBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IPartnerFunctionLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new PartnerFunction
   *
   * @param partnerFunction The PartnerFunction entity.
   * @return EJBLocalObject as a proxy to PartnerFunctionBean for the created
   *         PartnerFunction.
   */
  public IPartnerFunctionLocalObj create(IEntity partnerFunction)
    throws CreateException;

  /**
   * Load a PartnerFunctionBean
   *
   * @param primaryKey The primary key to the PartnerFunction record
   * @return EJBLocalObject as a proxy to the loaded PartnerFunctionBean.
   */
  public IPartnerFunctionLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find PartnerFunction records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IPartnerFunctionLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}