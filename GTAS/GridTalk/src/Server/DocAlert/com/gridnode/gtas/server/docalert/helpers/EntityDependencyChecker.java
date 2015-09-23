/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDependencyChecker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.docalert.facade.ejb.IDocAlertManagerObj;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS DocAlert module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * ResponseTrackRecord - dependent on Alert
 *                     - dependent on Alert via ReminderAlert
 * ResponseTrackRecord - dependent on DocumentType
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDependencyChecker
{

  /**
   * Constructor for EntityDependencyChecker.
   */
  public EntityDependencyChecker()
  {

  }


  /**
   * Checks whether there are dependent ResponseTrackRecords on the specified Alert.
   * 
   * @param alertName The Name of the Alert.
   * @return A Set of ResponseTrackRecord entities that are dependent on the Alert, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentTrackRecordsForAlert(String alertName)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResponseTrackRecord.RECEIVE_RESPONSE_ALERT,
        filter.getEqualOperator(), alertName, false);

      dependents = getTrackRecordList(filter);
      dependents.addAll(getTrackRecordListForReminderAlertByAlert(alertName));
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentTrackRecordsForAlert] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent ResponseTrackRecords on the specified DocumentType.
   * 
   * @param name The Name of the DocumentType.
   * @return A Set of ResponseTrackRecord entities that are dependent on the DocumentType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentTrackRecordsForDocumentType(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResponseTrackRecord.SENT_DOC_TYPE,
        filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getOrConnector(), ResponseTrackRecord.RESPONSE_DOC_TYPE,
        filter.getEqualOperator(), name, false);
        
      dependents = getTrackRecordList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentTrackRecordsForDocumentType] Error", t);
    }
    
    return dependents;
  }


  /**
   * Get the list of ResponseTrackRecords that satisfy the specified
   * filter condition.
   * 
   * @param filter The filtering condition.
   * @return A Set of ResponseTrackRecord entities that satisfy the filtering condition.
   * @throws Throwable Error in retrieving the associations from DocAlertManager.
   */  
  private Set getTrackRecordList(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection recordList = ServiceLookupHelper.getDocAlertMgr().findResponseTrackRecords(filter);

    if (recordList != null)
    {
      set.addAll(recordList);
    }

    return set;    
  }

  /**
   * Get the list of ResponseTrackRecords that have ReminderAlerts
   * associated with the specified Alert.
   * 
   * @param alertName The Name of the Alert.
   * @return A Set of ResponseTrackRecord entities that are associated to the
   * specified Alert (via ReminderAlert).
   * @throws Throwable Error in retrieving the associations from DocAlertManager.
   */  
  private Set getTrackRecordListForReminderAlertByAlert(String alertName) throws Throwable
  {
    IDocAlertManagerObj mgr = ServiceLookupHelper.getDocAlertMgr();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ReminderAlert.ALERT_TO_RAISE,
      filter.getEqualOperator(), alertName, false);

    Set set = new HashSet();

    Collection reminderAlertList = mgr.findReminderAlerts(filter);

    if (reminderAlertList != null)
    {
      Long trackRecordUid;
      for (Iterator i=reminderAlertList.iterator(); i.hasNext(); )
      {
        try
        {
          trackRecordUid = ((ReminderAlert)i.next()).getTrackRecordUID();
          set.add(mgr.findResponseTrackRecord(trackRecordUid));
        }
        catch (Exception ex)
        {
          //ignore if response track record not found
        }
      }
    }

    return set;    
  }
      
}
