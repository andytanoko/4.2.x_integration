/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcedureDefFileLocalHome.java
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
 * LocalHome interface for ProcedureDefFile Entity.
 *
 * @author Jagadeesh
 * @version 2.0
 * @since 2.0
 */

public interface IProcedureDefFileLocalHome extends EJBLocalHome
{

  /**
   * Create a new Procedure Definition File.
   *
   * @param procedureDefFile The ProcedureDefFile entity.
   * @return EJBLocalObject.
   */
  public IProcedureDefFileLocalObj create(IEntity procedureDefFile)
    throws CreateException;

  /**
   * Load a ProcedureDefFile.
   *
   * @param primaryKey The primary key to the User Procedure Definition File record
   * @return EJBLocalObject as a proxy.
   */
  public IProcedureDefFileLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find UserProcedureDef File records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IProcedureDefFileLocalObjs for the given Filter.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;


}


