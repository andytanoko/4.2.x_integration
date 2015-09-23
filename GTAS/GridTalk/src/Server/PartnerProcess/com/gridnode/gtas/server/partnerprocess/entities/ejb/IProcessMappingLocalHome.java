/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcessMappingLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * LocalHome interface for ProcessMappingBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IProcessMappingLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new ProcessMapping
   *
   * @param mapping The ProcessMapping entity.
   * @return EJBLocalObject as a proxy to ProcessMappingBean for the created
   *         ProcessMapping.
   */
  public IProcessMappingLocalObj create(IEntity mapping)
    throws CreateException;

  /**
   * Load a ProcessMappingBean
   *
   * @param primaryKey The primary key to the ProcessMapping record
   * @return EJBLocalObject as a proxy to the loaded ProcessMappingBean.
   */
  public IProcessMappingLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ProcessMapping records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IProcessMappingLocalObjs for the found mappings.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}