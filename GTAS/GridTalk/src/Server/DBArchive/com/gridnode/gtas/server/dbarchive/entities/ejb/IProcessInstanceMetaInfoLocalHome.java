/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcessInstanceMetaInfoLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 *
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since
 */
public interface IProcessInstanceMetaInfoLocalHome extends EJBLocalHome
{
  /**
   * Create a new ProcessInstanceMetaInfo
   *
   * @param ProcessInstanceMetaInfo The ProcessInstanceMetaInfo entity.
   * @return EJBLocalObject as a proxy to ProcessInstanceMetaInfoBean for the created
   *         ProcessInstanceMetaInfo.
   */
  public IProcessInstanceMetaInfoLocalObj create(IEntity processInstanceMetaInfo)
    throws CreateException;

  /**
   * Create a new ProcessInstanceMetaInfo, which allowed to specify whether
   * to generate a new PK or use the PK in the entity obj. 
   *
   * @param ProcessInstanceMetaInfo The ProcessInstanceMetaInfo entity.
   * @return EJBLocalObject as a proxy to ProcessInstanceMetaInfoBean for the created
   *         ProcessInstanceMetaInfo.
   */
  public IProcessInstanceMetaInfoLocalObj create(IEntity processInstanceMetaInfo, 
  		                                           Boolean isCreateNewUID)
    throws CreateException;
  
  /**
   * Load a ProcessInstanceMetaInfoBean
   *
   * @param primaryKey The primary key to the DocumentMetaInfo record
   * @return EJBLocalObject as a proxy to the loaded DocumentMetaInfoBean.
   */
  public IProcessInstanceMetaInfoLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ProcessInstanceMetaInfo records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IProcessInstanceMetaInfoLocalObjs for the found ProcessInstanceMetaInfo.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
}
