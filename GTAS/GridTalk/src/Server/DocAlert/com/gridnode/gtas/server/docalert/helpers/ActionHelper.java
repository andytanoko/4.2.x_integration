/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 28 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.model.docalert.DocAlertEntityFieldID;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.gtas.server.docalert.facade.ejb.IDocAlertManagerObj;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;

import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

/**
 * This is a helper class for use by the Actions of the DocAlert module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ActionHelper
{
  /**
   * Find the ResponseTrackRecord using UID. The ReminderAlerts are retrieved
   * with the record if exists.
   *
   * @param uid UID of the ResponseTrackRecord.
   * @return The retrieved ResponseTrackRecord.
   * @throws Exception Error in executing the find method.
   */
  public static ResponseTrackRecord findResponseTrackRecord(Long uid)
    throws Exception
  {
    IDocAlertManagerObj mgr = ServiceLookupHelper.getDocAlertMgr();

    ResponseTrackRecord record = mgr.findResponseTrackRecord(uid);

    //retrieve ReminderAlerts
    Collection reminderAlerts = mgr.findReminderAlertsByTrackRecord(
                                  (Long)record.getKey());
    record.setReminderAlerts(reminderAlerts);

    return record;
  }

  /**
   * Find the ResponseTrackRecord(s) using a filtering condition.
   * The RemiderAlert(s) are retrieved with each retrieved ResponseTrackRecord.
   *
   * @param filter The filtering condition.
   * @return Collection of ResponseTrackRecord, or empty Collection if none.
   * @throws Exception Error in executing the find method.
   */
  public static Collection findResponseTrackRecords(IDataFilter filter)
    throws Exception
  {
    IDocAlertManagerObj mgr = ServiceLookupHelper.getDocAlertMgr();

    Collection recordList = mgr.findResponseTrackRecords(filter);

    //retrieve ReminderAlerts
    Collection reminderAlerts = null;
    for (Iterator i=recordList.iterator(); i.hasNext(); )
    {
      ResponseTrackRecord record = (ResponseTrackRecord)i.next();

      reminderAlerts = mgr.findReminderAlertsByTrackRecord(
                         (Long)record.getKey());
      record.setReminderAlerts(reminderAlerts);
    }

    return recordList;
  }

  /**
   * Delete ResponseTrackRecord(s) base on UIDs.
   *
   * @param uids The UIDs of the ResponseTrackRecord(s) to delete.
   * @return Collection of those UIDs that the system failed to delete the
   * ResponseTrackRecord, either due to invalid UID or some other system error.
   * @throws Exception Error in executing the deletion method.
   */
  public static Collection deleteResponseTrackRecords(Collection uids)
    throws Exception
  {
    ArrayList failed = new ArrayList();
    try
    {
      Logger.debug("[ActionHelper.deleteResponseTrackRecords] Enter");
      IDocAlertManagerObj mgr = ServiceLookupHelper.getDocAlertMgr();
      for (Iterator i=uids.iterator(); i.hasNext(); )
      {
        Long uid = null;
        try
        {
          uid = (Long)i.next();
          mgr.deleteResponseTrackRecord(uid);
        }
        catch (Throwable t)
        {
          Logger.warn(
            "[ActionHelper.deleteResponseTrackRecords] Unable to delete record "+uid,
            t);
          failed.add(uid);
        }
      }
    }
    finally
    {
      Logger.debug("[ActionHelper.deleteResponseTrackRecords] Exit");
    }
    return failed;
  }

  /**
   * Create a ResponseTrackRecord in the database. The ReminderAlert(s) in
   * the ResponseTrackRecord are also created.
   *
   * @param record The ResponseTrackRecord to be created.
   * @return The UID of the created ResponseTrackRecord.
   * @throws Exception Error in executing the create method.
   */
  public static Long createResponseTrackRecord(ResponseTrackRecord record)
    throws Exception
  {
    Long recordUID = null;
    try
    {
      Logger.debug("[ActionHelper.createResponseTrackRecord] Enter");
      Collection reminderAlerts = record.getReminderAlerts();

      IDocAlertManagerObj mgr = ServiceLookupHelper.getDocAlertMgr();

      recordUID = mgr.createResponseTrackRecord(record);

      if (reminderAlerts != null)
      {
        createReminderAlerts(reminderAlerts, recordUID, mgr);
      }
    }
    finally
    {
      Logger.debug("[ActionHelper.createResponseTrackRecord] Exit");
    }

    return recordUID;
  }

  /**
   * Update a ResponseTrackRecord in the database.
   *
   * @param record The ResponseTrackRecord with changes to update.
   * @param currAlerts The Collection of ReminderAlert(s) that the current
   * persistented ResponseTrackRecord has.
   * @throws Exception Error in executing the update method.
   */
  public static void updateResponseTrackRecord(ResponseTrackRecord record,
                                               Collection currAlerts)
    throws Exception
  {
    try
    {
      Logger.debug("[ActionHelper.updateResponseTrackRecord] Enter");
      Collection updAlerts = record.getReminderAlerts();

      IDocAlertManagerObj mgr = ServiceLookupHelper.getDocAlertMgr();
      mgr.updateResponseTrackRecord(record);

      if (updAlerts != null)
      {
        Long recordUID = (Long)record.getKey();
        if (updAlerts.isEmpty())
        {
          // remove all reminderalerts from record
          deleteReminderAlerts(currAlerts, mgr);
        }
        else if (currAlerts.isEmpty())
        {
          // add all updAlerts
          createReminderAlerts(updAlerts, recordUID, mgr);
        }
        else
        {
          updateReminderAlerts(updAlerts, currAlerts, recordUID, mgr);
        }
      }
    }
    finally
    {
      Logger.debug("[ActionHelper.updateResponseTrackRecord] Exit");
    }
  }

  /**
   * Update the ReminderAlert list for a ResponseTrackRecord.
   *
   * @param updMaps Collection of Map(s) representing the new list of ReminderAlert(s)
   * for the ResponseTrackRecord.
   * @param currAlerts Collection of ReminderAlert(s) that the ResponseTrackRecord
   * currently has.
   * @param trackRecordUID UID of the ResponseTrackRecord.
   * @param mgr EJBObject for DocAlertManagerBean.
   * @throws Exception Error while executing the update method.
   */
  private static void updateReminderAlerts(Collection updMaps,
                                           Collection currAlerts,
                                           Long trackRecordUID,
                                           IDocAlertManagerObj mgr)
    throws Exception
  {
    try
    {
      Logger.debug("[ActionHelper.updateReminderAlerts] Enter");
      Iterator cIter = currAlerts.iterator();
      ReminderAlert alert = null;
      for (Iterator i=updMaps.iterator(); i.hasNext(); )
      {
        Map map = (Map)i.next();
        Object key = map.get(ReminderAlert.UID);
        if (key == null) // new reminder-> create
        {
          createReminderAlert(map, trackRecordUID, mgr);
        }
        else
        {
          boolean match = false;
          while (cIter.hasNext() && !match)
          {
            alert = (ReminderAlert)cIter.next();
            if (key.equals(alert.getKey())) // existing reminder -> update
            {
              copyMapToReminderAlert(alert, map);
              mgr.updateReminderAlert(alert);
              match = true;
            }
            else // not in new list -> delete and try next
            {
              mgr.deleteReminderAlert((Long)alert.getKey());
            }
          }
        }
      }
    }
    finally
    {
      Logger.debug("[ActionHelper.updateReminderAlerts] Exit");
    }
  }

  /**
   * Delete ReminderAlerts.
   *
   * @param reminderAlerts Collection of ReminderAlert(s) to delete.
   * @param mgr The EJBObject for the DocAlertManagerBean.
   *
   */
  private static void deleteReminderAlerts(Collection reminderAlerts,
                                           IDocAlertManagerObj mgr)
    throws Exception
  {
    ReminderAlert alert = null;
    for (Iterator i=reminderAlerts.iterator(); i.hasNext(); )
    {
      alert = (ReminderAlert)i.next();
      mgr.deleteReminderAlert((Long)alert.getKey());
    }
  }

  /**
   * Create ReminderAlert(s).
   *
   * @param maps Collection of Map(s) representing the ReminderAlert(s) to create.
   * @param trackRecordUID UID of the ResponseTrackRecord that the ReminderAlert(s)
   * will belong to.
   * @param mgr The EJBObject for the DocAlertManagerBean.
   */
  private static void createReminderAlerts(Collection maps,
                                           Long trackRecordUID,
                                           IDocAlertManagerObj mgr)
    throws Exception
  {
    for (Iterator i=maps.iterator(); i.hasNext(); )
    {
      createReminderAlert((Map)i.next(), trackRecordUID, mgr);
    }
  }

  /**
   * Create a ReminderAlert in the database.
   *
   * @param map Map representing the ReminderAlert to create.
   * @param trackRecordUID UID of the ResponseTrackRecord that the ReminderAlert
   * will belong to.
   * @param mgr The EJBObject for the DocAlertManagerBean.
   */
  private static void createReminderAlert(Map map,
                                          Long trackRecordUID,
                                          IDocAlertManagerObj mgr)
    throws Exception
  {
    ReminderAlert alert = convertMapToReminderAlert(map, false);
    alert.setTrackRecordUID(trackRecordUID);
    mgr.createReminderAlert(alert);
  }

  /**
   * Copy fields from a Map to the ReminderAlert entity. The keys in the Map
   * must match the Field IDs in the ReminderAlert entity.
   *
   * @param alert The ReminderAlert to copy fields to.
   * @param map The Map to copy fields from.
   */
  public static void copyMapToReminderAlert(ReminderAlert alert, Map map)
  {
    alert.setFieldValue(ReminderAlert.ALERT_TO_RAISE,
                        map.get(ReminderAlert.ALERT_TO_RAISE));
    alert.setFieldValue(ReminderAlert.DAYS_TO_REMINDER,
                        map.get(ReminderAlert.DAYS_TO_REMINDER));
    alert.setFieldValue(ReminderAlert.DOC_RECPT_XPATH,
                        map.get(ReminderAlert.DOC_RECPT_XPATH));
    alert.setFieldValue(ReminderAlert.DOC_SENDER_XPATH,
                        map.get(ReminderAlert.DOC_SENDER_XPATH));
  }

  /**
   * Convert a Map to a ReminderAlert entity.
   *
   * @param map The Map to convert.
   * @param setUID <b>true</b> to set the UID field into the ReminderAlert entity.
   */
  public static ReminderAlert convertMapToReminderAlert(Map map, boolean setUID)
  {
    ReminderAlert alert = new ReminderAlert();

    copyMapToReminderAlert(alert, map);
    if (setUID)
    {
      alert.setFieldValue(ReminderAlert.UID,
                          map.get(ReminderAlert.UID));
    }
    return alert;
  }

  /**
   * Convert a ResponseTrackRecord to Map object.
   *
   * @param record The ResponseTrackRecord to convert.
   * @return A Map object converted from the specified ResponseTrackRecord.
   *
   * @since 2.0 I7
   */
  public static Map convertResponseTrackRecordToMap(ResponseTrackRecord record)
  {
    return ResponseTrackRecord.convertToMap(
             record,
             DocAlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of ResponseTrackRecord(s) to Map objects.
   *
   * @param recList The collection of ResponseTrackRecord(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of ResponseTrackRecord(s).
   *
   * @since 2.0 I7
   */
  public static Collection convertResponseTrackRecordsToMapObjects(Collection recList)
  {
    return ResponseTrackRecord.convertEntitiesToMap(
             (ResponseTrackRecord[])recList.toArray(new ResponseTrackRecord[recList.size()]),
             DocAlertEntityFieldID.getEntityFieldID(),
             null);
  }

}