/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AS2DocTypeMappingEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AS2DocTypeMappingBean.
 *
 * @author Wong Yee Wah
 *
 * @version 4.1.3
 * @since 4.1.3
 */

public interface IAS2DocTypeMappingLocalHome extends EJBLocalHome
{
  /**
   * Create a new AS2DocTypeMapping
   *
   * @param AS2DocTypeMapping The AS2DocTypeMapping entity.
   * @return EJBLocalObject as a proxy to AS2DocTypeMappingBean for the created
   *         AS2DocTypeMapping.
   */
  public IAS2DocTypeMappingLocalObj create(IEntity as2DocTypeMapping)
    throws CreateException;

  /**
   * Load a AS2DocTypeMapping
   *
   * @param primaryKey The primary key to the AS2DocTypeMapping record
   * @return EJBLocalObject as a proxy to the loaded AS2DocTypeMappingBean.
   */
  public IAS2DocTypeMappingLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find AS2DocTypeMapping records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IAS2DocTypeMappingLocalObj for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
}
