/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMappingFileLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for MappingFileBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IMappingFileLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new MappingFile
   *
   * @param mappingFile The MappingFile entity.
   * @return EJBLocalObject as a proxy to MappingFileBean for the created
   *         MappingFile.
   */
  public IMappingFileLocalObj create(IEntity mappingFile)
    throws CreateException;

  /**
   * Load a MappingFileBean
   *
   * @param primaryKey The primary key to the MappingFile record
   * @return EJBLocalObject as a proxy to the loaded MappingFileBean.
   */
  public IMappingFileLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find MappingFile records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IUserLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}