/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridDocumentLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 01 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for GridDocumentBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IGridDocumentLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new GridDocument
   *
   * @param fileType The GridDocument entity.
   * @return EJBLocalObject as a proxy to GridDocumentBean for the created
   *         GridDocument.
   */
  public IGridDocumentLocalObj create(IEntity fileType)
    throws CreateException;

  /**
   * Load a GridDocumentBean
   *
   * @param primaryKey The primary key to the GridDocument record
   * @return EJBLocalObject as a proxy to the loaded GridDocumentBean.
   */
  public IGridDocumentLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find GridDocument records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IGridDocumentLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}