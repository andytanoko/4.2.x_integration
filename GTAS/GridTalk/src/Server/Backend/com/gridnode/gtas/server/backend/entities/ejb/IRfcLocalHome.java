/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRfcLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * LocalHome interface for RfcBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IRfcLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new Rfc
   *
   * @param rfc The Rfc entity.
   * @return EJBLocalObject as a proxy to RfcBean for the created
   *         Rfc.
   */
  public IRfcLocalObj create(IEntity rfc)
    throws CreateException;

  /**
   * Load a RfcBean
   *
   * @param primaryKey The primary key to the Rfc record
   * @return EJBLocalObject as a proxy to the loaded RfcBean.
   */
  public IRfcLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find Rfc records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IRfcLocalObjs for the found rfcs.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}