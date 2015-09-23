/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserProcedureLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * JUL 31 2002    Jagadeesh              Created
 */


package com.gridnode.pdip.base.userprocedure.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for ProcedureDefEntity
 *
 * @author Jagadeesh
 * @version 2.0
 * @since 2.0
 */


public interface IUserProcedureLocalHome extends EJBLocalHome
{

  /**
   * Create a new User Procedure Definition
   *
   * @param userProcedure The UserProcedure entity.
   * @return EJBLocalObject.
   */
  public IUserProcedureLocalObj create(IEntity userProcedure)
    throws CreateException;

  /**
   * Load a User Procedure
   *
   * @param primaryKey The primary key to the UserProcedure record
   * @return EJBLocalObject as a proxy.
   */
  public IUserProcedureLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find UserProcedure records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IProcedureDefLocalObjs for the given Filter.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
}



