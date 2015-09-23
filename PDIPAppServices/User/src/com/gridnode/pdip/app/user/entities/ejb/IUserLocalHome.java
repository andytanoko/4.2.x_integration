/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 11 2002    NSL/OHL             Created
 * Apr 26 2002    Neo Sok Lay         Remove the finder methods.
 */
package com.gridnode.pdip.app.user.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for UserBean
 *
 * @author Neo Sok Lay
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public interface IUserLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new User account
   *
   * @param userAccount The UserAccount entity.
   * @return EJBLocalObject as a proxy to UserBean for the created UserAccount.
   */
  public IUserLocalObj create(IEntity userAccount)
    throws CreateException;

  /**
   * Load a UserBean
   *
   * @param primaryKey The primary key to the UserAccount record
   * @return EJBLocalObject as a proxy to the loaded UserBean.
   */
  public IUserLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find UserAccount records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IUserLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}