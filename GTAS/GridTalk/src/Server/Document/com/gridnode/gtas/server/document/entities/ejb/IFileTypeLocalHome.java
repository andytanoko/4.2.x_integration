/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFileTypeLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 23 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for FileTypeBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IFileTypeLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new FileType
   *
   * @param fileType The FileType entity.
   * @return EJBLocalObject as a proxy to FileTypeBean for the created
   *         FileType.
   */
  public IFileTypeLocalObj create(IEntity fileType)
    throws CreateException;

  /**
   * Load a FileTypeBean
   *
   * @param primaryKey The primary key to the FileType record
   * @return EJBLocalObject as a proxy to the loaded FileTypeBean.
   */
  public IFileTypeLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find FileType records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IFileTypeLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}