/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IResponseTrackRecordLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay        Created
 */
package com.gridnode.gtas.server.docalert.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for ResponseTrackRecordBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IResponseTrackRecordLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new ResponseTrackRecord
   *
   * @param documentType The ResponseTrackRecord entity.
   * @return EJBLocalObject as a proxy to ResponseTrackRecordBean for the created
   *         ResponseTrackRecord.
   */
  public IResponseTrackRecordLocalObj create(IEntity responseTrackRecord)
    throws CreateException;

  /**
   * Load a ResponseTrackRecordBean
   *
   * @param primaryKey The primary key to the ResponseTrackRecord record
   * @return EJBLocalObject as a proxy to the loaded ResponseTrackRecordBean.
   */
  public IResponseTrackRecordLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ResponseTrackRecord records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IResponseTrackRecordLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}