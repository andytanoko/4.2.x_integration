/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPortLocalHome.java
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
 * LocalHome interface for PortBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IPortLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new Port
   *
   * @param port The Port entity.
   * @return EJBLocalObject as a proxy to PortBean for the created
   *         Port.
   */
  public IPortLocalObj create(IEntity port)
    throws CreateException;

  /**
   * Load a PortBean
   *
   * @param primaryKey The primary key to the Port record
   * @return EJBLocalObject as a proxy to the loaded PortBean.
   */
  public IPortLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find Port records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IPortLocalObjs for the found ports.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}