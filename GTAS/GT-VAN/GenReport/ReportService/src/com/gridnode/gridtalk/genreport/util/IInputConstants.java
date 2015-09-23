/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IInputConstants.java
 *
 ****************************************************************************
 * Date             	Author              Changes
 ****************************************************************************
 * Feb 15, 2007        Regina Zeng          Created
 * Mar 12, 2007		   Regina Zeng			Added REPORT_UID, BY_DATE, NUM_DAYS
 * Mar 26, 2007		   Regina Zeng			Added TIMEZONE
 */

package com.gridnode.gridtalk.genreport.util;

/**
 * @author Regina Zeng
 * This interface defines the constants used for GenReport module.
 */
public interface IInputConstants
{
  public static final String USER_GROUP = "user_group";
  public static final String USERNAME = "username";
  public static final String REPORT_TYPE = "ReportType";
  public static final String FREQUENCY = "Frequency";
  public static final String REPORTING_START_DATE_TIME = "ReportingStartDateTime";
  public static final String REPORTING_END_DATE_TIME = "ReportingEndDateTime";
  public static final String REPORT_FORMAT = "ReportFormat";
  public static final String CUSTOMER_LIST = "CustomerList";
  public static final String EMAIL_LIST = "EmailList";
  public static final String VIEW_ONLINE = "ViewOnline";
  public static final String SEND_EMAIL = "SendEmail";

  public static final String EFFECTIVE_START_DATE = "EffectiveStartDate";
  public static final String REPORT_RUN_TIME = "ReportRunTime";
  public static final String SCHEDULE_ID = "schedule_id";
  
  public static final String REPORT_UID = "report_uid";
  public static final String BY_DATE = "by_date";
  public static final String NUM_DAYS = "num_days";
  public static final String TIMEZONE = "timezone";
}
