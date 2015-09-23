/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ScheduledArchiveMetaInfoLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18, 2008   Ong Eu Soon         Created
 */
package com.gridnode.gtas.server.dbarchive.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface IArchiveMetaInfoLocalHome extends EJBLocalHome
{
  /**
   * Create a new ArchiveMetaInfo
   *
   * @param archiveMetaInfo The ArchiveMetaInfo entity.
   * @return EJBLocalObject as a proxy to ArchiveMetaInfoBean for the created
   *         ArchiveMetaInfo.
   */
  public IArchiveMetaInfoLocalObj create(IEntity archiveMetaInfo)
    throws CreateException;
  
  /**
   * Create a new ArchiveMetaInfo, can specify whether use the PK
   * in the entity obj or create a new PK.
   *
   * @param archiveMetaInfo The ArchiveMetaInfo entity.
   * @return EJBLocalObject as a proxy to ArchiveMetaInfoBean for the created
   *         ArchiveMetaInfo.
   */
  public IArchiveMetaInfoLocalObj create(IEntity archiveMetaInfo, Boolean isCreateNewUID)
    throws CreateException;
  
  /**
   * Load a ArchiveMetaInfoBean
   *
   * @param primaryKey The primary key to the ArchiveMetaInfo record
   * @return EJBLocalObject as a proxy to the loaded ArchiveMetaInfoBean.
   */
  public IArchiveMetaInfoLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ArchiveMetaInfo records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IArchiveMetaInfoLocalObj for the found ArchiveMetaInfo.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
}
