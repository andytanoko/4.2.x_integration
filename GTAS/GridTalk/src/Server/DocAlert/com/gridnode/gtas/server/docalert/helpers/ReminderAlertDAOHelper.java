/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReminderAlertDAOHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 30 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
 

/**
 * Helper for DAO level checking.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ReminderAlertDAOHelper
{
  private static ReminderAlertDAOHelper _self = null;
  private static Object                  _lock = new Object();

  private ReminderAlertDAOHelper()
  {

  }

  public static ReminderAlertDAOHelper getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new ReminderAlertDAOHelper();
      }
    }
    return _self;
  }

  /**
   * Check if the specified ReminderAlert will result in duplicate when
   * created or updated.
   *
   * @param record The ReminderAlert to check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the ReminderAlert itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * ReminderAlert will result in duplicates.
   */
  public void checkDuplicate(
    ReminderAlert reminderAlert, boolean checkKey) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ReminderAlert.TRACK_RECORD_UID,
      filter.getEqualOperator(), reminderAlert.getTrackRecordUID(), false);
    filter.addSingleFilter(filter.getAndConnector(), ReminderAlert.DAYS_TO_REMINDER,
      filter.getEqualOperator(), new Long(reminderAlert.getDaysToReminder()), false);

    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), ReminderAlert.UID,
        filter.getNotEqualOperator(), reminderAlert.getKey(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException(
        "ReminderAlert ["+ reminderAlert.getEntityDescr() +
        "] already exists with same Days to Reminder!");
  }

  private IEntityDAO getDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(ReminderAlert.ENTITY_NAME);
  }


}