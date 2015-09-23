/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAccessRightLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21 2002    Neo Sok Lay         Created
 * Oct 20 2005    Neo Sok Lay         The throws clause of a create<METHOD> 
 *                                    create method must include javax.ejb.CreateException.
 */
package com.gridnode.pdip.base.acl.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import java.util.Collection;

/**
 * Local Home interface for AccessRightBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0
 */
public interface IAccessRightLocalHome extends EJBLocalHome
{
  /**
   * Create a new AccessRight record.
   *
   * @param role The AccessRight entity.
   * @return EJBLocalObject as a proxy to AccessRightBean for the created
   * AccessRight record.
   */
  public IAccessRightLocalObj create(IEntity accessRight) throws CreateException;

  /**
   * Load a AccessRightBean
   *
   * @param primaryKey The primary key to the AcessRight record
   * @return EJBLocalObject as a proxy to the loaded AccessRightBean.
   */
  public IAccessRightLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find AccessRight records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IAccessRightLocalObjs for the found AcessRights.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
}