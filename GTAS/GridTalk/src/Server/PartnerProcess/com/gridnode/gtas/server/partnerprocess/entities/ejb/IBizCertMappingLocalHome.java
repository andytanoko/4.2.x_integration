/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBizCertMappingLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * LocalHome interface for BizCertMappingBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IBizCertMappingLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new BizCertMapping
   *
   * @param mapping The BizCertMapping entity.
   * @return EJBLocalObject as a proxy to BizCertMappingBean for the created
   *         BizCertMapping.
   */
  public IBizCertMappingLocalObj create(IEntity mapping)
    throws CreateException;

  /**
   * Load a BizCertMappingBean
   *
   * @param primaryKey The primary key to the BizCertMapping record
   * @return EJBLocalObject as a proxy to the loaded BizCertMappingBean.
   */
  public IBizCertMappingLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find BizCertMapping records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IBizCertMappingLocalObjs for the found mappings.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}