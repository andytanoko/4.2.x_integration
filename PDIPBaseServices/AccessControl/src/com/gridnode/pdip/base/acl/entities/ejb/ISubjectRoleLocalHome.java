/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISubjectRoleLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 15 2002    Goh Kan Mun             Created
 * Oct 20 2005    Neo Sok Lay             The throws clause of a create<METHOD> 
 *                                        create method must include javax.ejb.CreateException.
 */

package com.gridnode.pdip.base.acl.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * This interface defines the method of creating the EJBobject interface for the SubjectRoleBean.
 *
 * @author Goh Kan Mun
 *
 * @version 4.0
 * @since 2.0
 */

public interface ISubjectRoleLocalHome extends EJBLocalHome
{
  /**
   * Create a new SubjectRole.
   *
   * @param role The SubjectRole entity.
   * @return EJBLocalObject as a proxy to SubjectRoleBean for the created SubjectRole.
   */
  public ISubjectRoleLocalObj create(IEntity role) throws CreateException;

  /**
   * Load a SubjectRoleBean
   *
   * @param primaryKey The primary key to the SubjectRole record
   * @return EJBLocalObject as a proxy to the loaded SubjectRoleBean.
   */
  public ISubjectRoleLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find SubjectRole records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the ISubjectRoleLocalObjs for the found SubjectRole.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}