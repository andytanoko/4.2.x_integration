/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISchedule.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 11 2004    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.scheduler.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in WorkflowActivity entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public interface ISchedule
{
  /**
   * Name of Scheduler entity.
   */
  public static final String  ENTITY_NAME = "Schedule";

  public static final String TASK_USER_PROCEDURE = "UserProcedure";
  public static final String TASK_CHECK_LICENSE = "CheckLicense";
  public static final String TASK_DB_ARCHIVE = "DBArchive";

  // from IiCalAlarm
  public static final Number UID = new Integer(0);  // String
  public static final Number DELAY_PERIOD = new Integer(4);  // Long
  public static final Number TASK_TYPE = new Integer(6);  // String
  public static final Number DISABLED = new Integer(9);  // Boolean
  public static final Number TIMES_TO_RUN= new Integer(11); //Integer
  public static final Number TASK_ID= new Integer(18); //String

  //Own
  public static final Number START_DATE= new Integer(104); //Date
  public static final Number START_TIME= new Integer(105); //String
  public static final Number FREQUENCY= new Integer(106); //Integer
  public static final Number DAYS_OF_WEEK= new Integer(107); //List
  public static final Number WEEK_OF_MONTH= new Integer(108); //Integer

}
