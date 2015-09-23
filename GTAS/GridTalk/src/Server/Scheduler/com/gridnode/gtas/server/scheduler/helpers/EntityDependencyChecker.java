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
 * Feb 18 2004    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.scheduler.helpers;

import com.gridnode.gtas.server.scheduler.model.Schedule;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS Scheduler module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * Schedule - dependent on UserProcedure
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
   * Checks whether there are dependent Schedules on the specified UserProcedure.
   * 
   * @param userProcName The Name of the UserProcedure.
   * @return A Set of Schedule entities that are dependent on the UserProcedure, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentSchedulesForUserProcedure(String userProcName)
  {
    Set dependents = null;
    try
    {
      dependents = getScheduleListByUserProcedure(userProcName);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentSchedulesForUserProcedure] Error", t);
    }
    
    return dependents;
  }

  /**
   * Get the list of Schedule(s) that have dependency on the specified UserProcedure.
   * 
   * @param userProcName The Name of the UserProcedure.
   * @return A Set of Schedule entities that are associated to the
   * specified UserProcedure (via TaskID).
   * @throws Throwable Error in retrieving the associations from iCalTimeMgr.
   */  
  private Set getScheduleListByUserProcedure(String userProcName) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Schedule.TASK_ID,
      filter.getEqualOperator(), userProcName, false);
    filter.addSingleFilter(filter.getAndConnector(), Schedule.TASK_TYPE,
      filter.getEqualOperator(), Schedule.TASK_USER_PROCEDURE, false);

    Set set = new HashSet();

    Collection scheduleList = ActionHelper.findSchedulesByFilter(filter);

    if (scheduleList != null)
    {
      set.addAll(scheduleList);
    }

    return set;    
  }
}
