/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRoleLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * Oct 20 2005    Neo Sok Lay             The throws clause of a create<METHOD> 
 *                                        create method must include javax.ejb.CreateException
 */

package com.gridnode.pdip.base.acl.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * This interface defines the method of creating the EJBobject interface for the RoleBean.
 *
 * @author Goh Kan Mun
 *
 * @version 4.0
 * @since 2.0
 */

public interface IRoleLocalHome extends EJBLocalHome
{
  /**
   * Create a new Role.
   *
   * @param role The Role entity.
   * @return EJBLocalObject as a proxy to RoleBean for the created Role.
   */
  public IRoleLocalObj create(IEntity role) throws CreateException;

  /**
   * Load a RoleBean
   *
   * @param primaryKey The primary key to the Role record
   * @return EJBLocalObject as a proxy to the loaded RoleBean.
   */
  public IRoleLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find Role records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IRoleLocalObjs for the found Roles.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}