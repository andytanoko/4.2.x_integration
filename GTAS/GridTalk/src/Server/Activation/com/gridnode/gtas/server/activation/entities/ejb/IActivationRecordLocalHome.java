/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IActivationRecordLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for ActivationRecordBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IActivationRecordLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new ActivationRecord.
   *
   * @param connectionStatus The ActivationRecord entity.
   * @return EJBLocalObject as a proxy to ActivationRecordBean for the created ActivationRecord.
   */
  public IActivationRecordLocalObj create(IEntity connectionStatus)
    throws CreateException;

  /**
   * Load a ActivationRecordBean
   *
   * @param primaryKey The primary key to the ActivationRecord record
   * @return EJBLocalObject as a proxy to the loaded ActivationRecordBean.
   */
  public IActivationRecordLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ActivationRecord records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IActivationRecordLocalObjs for the found ActivationRecords.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}