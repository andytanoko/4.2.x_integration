/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportUtility.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 4 2002    H.Sushil            Created
 */


package com.gridnode.pdip.app.reportgen.util;

import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;


import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import java.io.File;

 /** Utility class for simple date functions,accessing temporary follder
   *
   * @author H.Sushil
   *
   * @version 1.0
   * @since 1.0
   */


public class ReportUtility {

  private static ReportUtility _reportUtility = null;

  private ReportUtility()
  {

  }

  static public ReportUtility instance()
  {
    if (_reportUtility == null)
    {
          _reportUtility = new ReportUtility();
    }
    return _reportUtility;
  }

  public Date getDateWithWeekDay(Date date,Integer dayOfWeek)
  {

    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date);

    Calendar cal2 = (Calendar)cal1.clone();

    cal1.set(Calendar.DAY_OF_WEEK,dayOfWeek.intValue());

    if(cal2.get(Calendar.DAY_OF_WEEK) > cal1.get(Calendar.DAY_OF_WEEK))
    {
	    cal1.add(Calendar.DAY_OF_MONTH,7);
    }

    return cal1.getTime();
  }

  public Date getDateWithDayOfMonth(Date date,Integer dayOfMonth)
  {

    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date);

    Calendar cal2 = (Calendar)cal1.clone();

    cal1.set(Calendar.DAY_OF_MONTH,dayOfMonth.intValue());

    if(cal2.get(Calendar.DAY_OF_MONTH) > cal1.get(Calendar.DAY_OF_MONTH))
    {
	    cal1.add(Calendar.MONTH,1);
    }

    return cal1.getTime();
  }

  public Date stringToDate(String date)
  {
    StringTokenizer token = new StringTokenizer(date,"/");
    if(token.hasMoreElements())
    {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(token.nextToken()));
	cal.set(Calendar.MONTH,Integer.parseInt(token.nextToken()));
	cal.add(Calendar.MONTH,-1);
	cal.set(Calendar.YEAR,Integer.parseInt(token.nextToken()));
	cal.set(Calendar.HOUR,0);
	cal.set(Calendar.MINUTE,0);
	cal.set(Calendar.SECOND,0);
	return cal.getTime();
    }
    else
      return null;

  }

   public String getTempFolderPath()
    {
        try
        {
            ConfigurationManager configManager =
                ConfigurationManager.getInstance();
            Configuration config =
                configManager.getConfig(IReportTempDirConfig.TEMP_DIR_CONFIG);

            java.io.File file =
                new java.io.File(config.getString(IReportTempDirConfig.TEMP_DIR));
            if (!file.exists())
            {
                file.mkdir();
            }
            return file.getCanonicalPath();
        }
        catch (Exception ex)
        {
            return null;
        }
    }

   public String pathTranslator(String path)
   {
    String pathTranslated = "";
    StringTokenizer token = new StringTokenizer(path,File.separator);
    while(token.hasMoreElements())
    {
      pathTranslated = pathTranslated + token.nextElement() + File.separator + File.separator;
    }
    pathTranslated = pathTranslated.substring(0,pathTranslated.length()-2);
    return pathTranslated;
   }


}