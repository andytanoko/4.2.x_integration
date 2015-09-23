/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IActiveTrackRecordLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for ActiveTrackRecordBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IActiveTrackRecordLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new ActiveTrackRecord
   *
   * @param fileType The ActiveTrackRecord entity.
   * @return EJBLocalObject as a proxy to ActiveTrackRecordBean for the created
   *         ActiveTrackRecord.
   */
  public IActiveTrackRecordLocalObj create(IEntity activeTrackRecord)
    throws CreateException;

  /**
   * Load a ActiveTrackRecordBean
   *
   * @param primaryKey The primary key to the ActiveTrackRecord record
   * @return EJBLocalObject as a proxy to the loaded ActiveTrackRecordBean.
   */
  public IActiveTrackRecordLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ActiveTrackRecord records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IActiveTrackRecordLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}