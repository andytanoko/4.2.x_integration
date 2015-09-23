/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertTriggerLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17 2003    Neo Sok Lay        Created
 */
package com.gridnode.gtas.server.alert.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

import java.util.Collection;

/**
 * LocalHome interface for AlertTriggerBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public interface IAlertTriggerLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new AlertTrigger
   *
   * @param trigger The AlertTrigger entity.
   * @return EJBLocalObject as a proxy to AlertTriggerBean for the created
   *         Trigger.
   */
  public IAlertTriggerLocalObj create(IEntity trigger)
    throws CreateException;

  /**
   * Load a AlertTriggerBean
   *
   * @param primaryKey The primary key to the AlertTrigger record
   * @return EJBLocalObject as a proxy to the loaded AlertTriggerBean.
   */
  public IAlertTriggerLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find AlertTrigger records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IAlertTriggerLocalObjs for the found triggers.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}