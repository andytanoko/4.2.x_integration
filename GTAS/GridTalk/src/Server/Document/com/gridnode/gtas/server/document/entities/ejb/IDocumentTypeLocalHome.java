/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentTypeLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
 
/**
 * LocalHome interface for DocumentTypeBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IDocumentTypeLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new DocumentType
   *
   * @param documentType The DocumentType entity.
   * @return EJBLocalObject as a proxy to DocumentTypeBean for the created
   *         DocumentType.
   */
  public IDocumentTypeLocalObj create(IEntity documentType)
    throws CreateException;

  /**
   * Load a DocumentTypeBean
   *
   * @param primaryKey The primary key to the DocumentType record
   * @return EJBLocalObject as a proxy to the loaded DocumentTypeBean.
   */
  public IDocumentTypeLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find DocumentType records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IDocumentTypeLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}