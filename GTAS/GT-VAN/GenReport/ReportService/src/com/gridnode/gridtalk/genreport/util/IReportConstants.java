/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReportConstants.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 18, 2007        Regina Zeng          Created
 */

package com.gridnode.gridtalk.genreport.util;

/**
 * @author Regina Zeng
 * This interface defines the constants used for GenReport module.
 */
public interface IReportConstants
{
  public static final String CONFIRM = "&confirmation=";
  
  public static final String CAT_1 = "genreport";
  
  public static final String CAT_2 = "housekeep.report";
  
  public static final String KEY_REPORTSERVICE = "reportservice.url";
  
  public static final String KEY_FREQUENCY = "frequency";
  
  public static final String KEY_NEXT_DELETE_DATE = "next.delete.date";
  
  public static final String KEY_ARCHIVE_DURATION = "archive.duration";
  
  public static final String KEY_IMAGE_TITLE = "imageTitle";
  
  public static final String KEY_IMAGE_SMALL_LOGO = "imageSmallLogo";
  
  public static final String KEY_TEMPLATE_DATASOURCE = "template.datasource.";
  
  public static final String KEY_EMAIL_BODY = "email.body";
  
  public static final String FREQUENCY_ONCE = "Once";
  
  public static final String REPORT_STATUS_VALID = "valid";
  
  public static final String REPORT_STATUS_INVALID = "invalid";  
  
  public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  
  public static final String TIME_FORMAT = "HH:mm:ss";
  
  public static final String DATE_TO_STRING_FORMAT = "MM/dd/yyyy HH:mm:ss";
  
  public static final String TIME_TO_STRING_FORMAT = "hh:mm:ss a Z";
  
  public static final String CREATE_REPORT_PATH = "report&rr=selected";
  
  public static final String CREATE_SCHEDULE_PATH = "add_schedule_report";
  
  public static final String UPDATE_SCHEDULE_PATH = "update_schedule&schedule_id=";
  
  public static final String HOUSEKEEP_REPORT_PATH = "housekeep_report";
      
  public static final String TEMPLATE_PATH = "gtvan/genreport/templates/";
  
  public static final String ENGINE_PATH = "gtvan/genreport";
  
  public static final String IMAGE_PATH = "/images/report/";
  
  public static final String UNWANTED_PATH = "/mainpage.jsp?included=";
}
