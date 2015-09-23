/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReminderAlertLocalHome.java
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
 * LocalHome interface for ReminderAlertBean
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IReminderAlertLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new ReminderAlert
   *
   * @param fileType The ReminderAlert entity.
   * @return EJBLocalObject as a proxy to ReminderAlertBean for the created
   *         ReminderAlert.
   */
  public IReminderAlertLocalObj create(IEntity reminderAlert)
    throws CreateException;

  /**
   * Load a ReminderAlertBean
   *
   * @param primaryKey The primary key to the ReminderAlert record
   * @return EJBLocalObject as a proxy to the loaded ReminderAlertBean.
   */
  public IReminderAlertLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find ReminderAlert records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IReminderAlertLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}