/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistryObjectMappingLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14, 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import java.util.Collection;

/**
 * Local Home interface for RegistryObjectMappingBean 
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IRegistryObjectMappingLocalHome extends EJBLocalHome
{
  /**
   * Create a new RegistryObjectMapping.
   *
   * @param mapping The RegistryObjectMapping entity.
   * @return EJBLocalObject as a proxy to RegistryObjectMappingBean 
   * for the created RegistryObjectMapping.
   */
  public IRegistryObjectMappingLocalObj create(IEntity mapping)
    throws CreateException;

  /**
   * Load a RegistryObjectMappingBean
   *
   * @param primaryKey The primary key to the RegistryObjectMapping record
   * @return EJBLocalObject as a proxy to the loaded RegistryObjectMappingBean.
   */
  public IRegistryObjectMappingLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find RegistryObjectMapping records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IRegistryObjectMappingLocalObjs for the found BusinessEntities.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
  
}
