/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IResourceLinkLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
 
/**
 * LocalHome interface for ResourceLinkBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IResourceLinkLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new ResourceLink.
   *
   * @param bizEntity The ResourceLink entity.
   * @return EJBLocalObject as a proxy to ResourceLinkBean for the created ResourceLink.
   */
  public IResourceLinkLocalObj create(IEntity bizEntity)
    throws CreateException;

  /**
   * Load a ResourceLinkBean
   *
   * @param primaryKey The primary key to the ResourceLink record
   * @return EJBLocalObject as a proxy to the loaded ResourceLinkBean.
   */
  public IResourceLinkLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ResourceLink records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IResourceLinkLocalObjs for the found BusinessEntities.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}