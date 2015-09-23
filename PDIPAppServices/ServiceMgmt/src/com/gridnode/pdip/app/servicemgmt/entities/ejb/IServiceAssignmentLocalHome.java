/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IServiceAssignmentLocalHome.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Feb 6, 2004   	Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public interface IServiceAssignmentLocalHome extends EJBLocalHome
{
  /**
   * Create a new ServiceAssignment.
   *
   * @param serviceAssignment The ServiceAssignment entity.
   * @return EJBLocalObject as a proxy to ServiceAssignmentBean for the created ServiceAssignment.
   */
  public IServiceAssignmentLocalObj create(IEntity serviceAssignment) throws CreateException;

  /**
   * Load a ServiceAssignmentBean
   *
   * @param primaryKey The primary key to the ServiceAssignment record
   * @return EJBLocalObject as a proxy to the loaded ServiceAssignmentBean.
   */
  public IServiceAssignmentLocalObj findByPrimaryKey(Long primaryKey) throws FinderException;

  /**
   * Find ServiceAssignment records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IServiceAssignmentLocalObjs for the found ServiceAssignments.
   */
  public Collection findByFilter(IDataFilter filter) throws FinderException;

}
