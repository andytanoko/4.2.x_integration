/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DateUtil.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 22, 2007        Regina Zeng          Created
 * Mar 05, 2007			Alain Ah Ming				Added error codes to error logs
 * Aug 13, 2007     Tam Wei Xiang       Added method convertTimeToServerTime(...,...)
 */

package com.gridnode.gridtalk.genreport.util;

import java.util.*;
import java.text.*;

import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Regina Zeng
 * This aids to configure date and time formating as well as timezone.
 */
public class DateUtil
{
  private static Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_UTIL, "DateUtil");
    
  /**
   * Convert a Date to a String
   * @param date Date
   * @return String in MM/dd/yyyy hh:mm:ss a format
   */
  public static String processDate(Date date)
  {
    DateFormat df = new SimpleDateFormat(IReportConstants.DATE_TO_STRING_FORMAT);
    return (String)df.format(date);  
  }  
  
  /**
   * Convert a Date to a String
   * @param date Date
   * @return String in hh:mm:ss a Z format
   */
  public static String processTime(Date date)
  {
    DateFormat df = new SimpleDateFormat(IReportConstants.TIME_TO_STRING_FORMAT);
    return (String)df.format(date);  
  }  
  
  /**
   * TWX: Convert the given time in the userTZ to the server timezone. 
   * @param time the time in format HH:mm:ss
   * @param userTz
   * @return
   */
  public static String convertTimeToServerTime(String time, TimeZone userTz)
  {
    StringTokenizer st = new StringTokenizer(time, ":");
    int hour = Integer.parseInt(st.nextToken());
    int minute = Integer.parseInt(st.nextToken());
    int second = 0;
    
    if(st.hasMoreTokens())
    {
      second = Integer.parseInt(st.nextToken());   
    }
    
    Calendar c = Calendar.getInstance();
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, minute);
    c.set(Calendar.SECOND, second);
    c.setTimeZone(userTz);
    
    SimpleDateFormat df1 = new SimpleDateFormat(IReportConstants.TIME_TO_STRING_FORMAT);
    return df1.format(c.getTime());
  }
  
  public static Date processEndRangeDate(Date date, int preloadTime)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+preloadTime);
    //DateFormat df = new SimpleDateFormat(IReportConstants.DATE_TO_STRING_FORMAT);
    return cal.getTime(); //(String)df.format(cal.getTime())
  }
  
  /**
   * Calculates the nrdt, nsdt, nedt
   * @param dateTime date in Date format 
   * @param frequency Daily, Weekly, Monthly
   * @return Date[0]=nrdt, Date[1]=nsdt, Date[2]=nedt
   */
  public static Date[] nextRunDateTime(Date dateTime, String frequency)
  {
    Date[] date = new Date[3]; 
    
    if(frequency.equals("Daily"))
    { 
      Date nrdt = DateUtil.getNextDaily(dateTime);
      System.out.println("\n\nDateUtil LN 58 Next Run Date Time: "+DateUtil.processDate(nrdt));
      Date[] nt = (Date[])DateUtil.getDaily(nrdt);
      Date[] newDate = {nrdt, nt[0], nt[1]};
      return newDate;
    }
    else if(frequency.equals("Weekly"))
    {
      Date nrdt = DateUtil.getNextWeekly(dateTime);
      System.out.println("\n\nDateUtil LN 77 Next Run Date Time: "+DateUtil.processDate(nrdt));
      Date[] nt = (Date[])DateUtil.getWeekly(nrdt);
      Date[] newDate = {nrdt, nt[0], nt[1]};
      return newDate;
    }
    else if(frequency.equals("Monthly"))
    {
      Date nrdt = DateUtil.getNextMonthy(dateTime);
      System.out.println("\n\nDateUtil LN 92 Next Run Date Time: "+DateUtil.processDate(nrdt));
      Date[] nt = (Date[])DateUtil.getMonthly(nrdt);
      Date[] newDate = {nrdt, nt[0], nt[1]};
      return newDate;
    }
    else
    {
      _logger.logWarn("nextRunDateTime", null, "Error while reading frequency from UI", null);
    }
    return date;
  }

  /**
   * Calculates the next run date time for frequency Daily.
   * @param dateTime date in Date format
   * @return next run date time
   */
  public static Date getNextDaily(Date dateTime)
  {
    try
    {
      GregorianCalendar gCal = (GregorianCalendar) Calendar.getInstance();
      gCal.setTime(dateTime);
      gCal.add(GregorianCalendar.DAY_OF_MONTH, 1);
      return gCal.getTime();
    }
    catch (Exception e)
    {
      _logger.logWarn("getDaily", null, "Error while calculating the next run date time for frequency equals to Daily.", e);
      return null;
    }
  }
  
  /**
   * Calculates the next run date time for frequency Weekly.
   * @param dateTime date in Date format
   * @return next run date time
   */
  public static Date getNextWeekly(Date dateTime)
  {
    try
    {
      GregorianCalendar gCal = (GregorianCalendar)Calendar.getInstance();
      gCal.setTime(dateTime);
      gCal.add(GregorianCalendar.DAY_OF_MONTH, 7);
      return gCal.getTime();
    }
    catch (Exception e)
    {
      _logger.logWarn("getNextWeekly", null, "Error while calculating the next run date time for frequency equals to Weekly.", e);
      return null;
    }
  }
  
  /**
   * Calculates the next run date time for frequency Monthly.
   * @param dateTime date in Date format 
   * @return next run date time
   */
  public static Date getNextMonthy(Date dateTime)
  {
    try
    {
      GregorianCalendar gCal = (GregorianCalendar) Calendar.getInstance();
      gCal.setTime(dateTime);
      gCal.set(GregorianCalendar.DAY_OF_MONTH, 1);
      gCal.add(GregorianCalendar.MONTH, 1);
      return gCal.getTime();
    }
    catch (Exception e)
    {
      _logger.logWarn("getMonthly", null, "Error while calculating the next run date time for frequency equals to Monthly.", e);
      return null;
    }
  }
  
  /**
   * Takes in the next run date time as parameter to calculate the next 
   * reporting start date and end date with frequency equals Daily.
   * @param NRDT next run date time
   * @return Date[0]=next start date time Date[1]=next end date time 
   */
  public static Date[] getDaily(Date NRDT)
  {
    try
    {
      GregorianCalendar gCal1 = (GregorianCalendar)Calendar.getInstance();
      GregorianCalendar gCal2 = (GregorianCalendar)Calendar.getInstance();
      
      gCal1.setTime(NRDT);     
      gCal2.setTime(NRDT); 
      
      int firstMonth = GregorianCalendar.JANUARY;
      if(gCal1.get(GregorianCalendar.DAY_OF_MONTH)!=1)
      {
        //set calendar as normal
        gCal1.add(GregorianCalendar.DAY_OF_MONTH, -1);        
        gCal2.add(GregorianCalendar.DAY_OF_MONTH, -1);
        gCal1.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
        gCal2.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
        gCal1.set(GregorianCalendar.HOUR, 0);
        gCal1.set(GregorianCalendar.MINUTE, 0);
        gCal1.set(GregorianCalendar.SECOND, 0);
        gCal2.set(GregorianCalendar.HOUR, 23);
        gCal2.set(GregorianCalendar.MINUTE, 59);
        gCal2.set(GregorianCalendar.SECOND, 59);    
      }
      else
      {
        //first day of the month, set calendar month-1, day = last day of the month 
        if(gCal1.get(GregorianCalendar.MONTH)!=firstMonth)
        {
          gCal1.add(GregorianCalendar.MONTH, -1);
          gCal1.set(GregorianCalendar.DAY_OF_MONTH, gCal1.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
          gCal2.add(GregorianCalendar.MONTH, -1);
          gCal2.set(GregorianCalendar.DAY_OF_MONTH, gCal2.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
          gCal1.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
          gCal2.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
          gCal1.set(GregorianCalendar.HOUR, 0);
          gCal1.set(GregorianCalendar.MINUTE, 0);
          gCal1.set(GregorianCalendar.SECOND, 0);          
          gCal2.set(GregorianCalendar.HOUR, 23);
          gCal2.set(GregorianCalendar.MINUTE, 59);
          gCal2.set(GregorianCalendar.SECOND, 59);
        }        
        else
        {
          //first day of the year, set calendar year-1, month=DECEMBER, day = last day of the month
          gCal1.add(GregorianCalendar.YEAR, -1);
          gCal1.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
          gCal1.set(GregorianCalendar.DAY_OF_MONTH, gCal1.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
          gCal2.add(GregorianCalendar.YEAR, -1);
          gCal2.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
          gCal2.set(GregorianCalendar.DAY_OF_MONTH, gCal2.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
          gCal1.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
          gCal2.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
          gCal1.set(GregorianCalendar.HOUR, 0);
          gCal1.set(GregorianCalendar.MINUTE, 0);
          gCal1.set(GregorianCalendar.SECOND, 0);          
          gCal2.set(GregorianCalendar.HOUR, 23);
          gCal2.set(GregorianCalendar.MINUTE, 59);
          gCal2.set(GregorianCalendar.SECOND, 59);
        }
      }
      System.out.println("\n\nNext Start Date Time: "+DateUtil.processDate(gCal1.getTime()));
      System.out.println("\n\nNext End Date Time: "+DateUtil.processDate(gCal2.getTime()));
      Date[] startNEnd = {gCal1.getTime(),gCal2.getTime()}; 
      return startNEnd;
    }
    catch (Exception e)
    {
      _logger.logWarn("getDaily", null, "Error while calculating the next run date time for frequency equals to Daily.", e);
      return null;
    }
  }
  
  /**
   * Takes in the next run date time as parameter to calculate the next 
   * reporting start date and end date with frequency equals Weekly.
   * @param NRDT next run date time
   * @return Date[0]=next start date time Date[1]=next end date time 
   */
  public static Date[] getWeekly(Date NRDT)
  {
    try
    {
      GregorianCalendar gCal1 = (GregorianCalendar)Calendar.getInstance();
      GregorianCalendar gCal2 = (GregorianCalendar)Calendar.getInstance();
      gCal1.setTime(NRDT);
      gCal2.setTime(NRDT);
      gCal1.add(GregorianCalendar.DAY_OF_MONTH, -7);
      gCal2.add(GregorianCalendar.DAY_OF_MONTH, -7);
      int dayOfWeek = gCal1.get(GregorianCalendar.DAY_OF_WEEK);
      System.out.println("GregorianCalendar.DAY_OF_WEEK: "+dayOfWeek);
      
      switch(dayOfWeek)
      {
        case 1: //sunday
                gCal1.setTime(gCal1.getTime());
                gCal2.add(GregorianCalendar.DAY_OF_MONTH, 6);
                break;
        case 2: //monday
                gCal1.add(GregorianCalendar.DAY_OF_MONTH, -1);
                gCal2.add(GregorianCalendar.DAY_OF_MONTH, 5);
                break;
        case 3: //tuesday
                gCal1.add(GregorianCalendar.DAY_OF_MONTH, -2);
                gCal2.add(GregorianCalendar.DAY_OF_MONTH, 4);
                break;
        case 4: //wednesday
                gCal1.add(GregorianCalendar.DAY_OF_MONTH, -3);
                gCal2.add(GregorianCalendar.DAY_OF_MONTH, 3);
                break;
        case 5: //thursday
                gCal1.add(GregorianCalendar.DAY_OF_MONTH, -4);
                gCal2.add(GregorianCalendar.DAY_OF_MONTH, 2);
                break;
        case 6: //friday
                gCal1.add(GregorianCalendar.DAY_OF_MONTH, -5);
                gCal2.add(GregorianCalendar.DAY_OF_MONTH, 1);
                break;
        case 7: //saturday
                gCal1.add(GregorianCalendar.DAY_OF_MONTH, -6);
                gCal2.setTime(gCal2.getTime());
                break;
        default: //Error
                 break;
      }
      gCal1.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
      gCal2.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
      gCal1.set(GregorianCalendar.HOUR, 0);
      gCal1.set(GregorianCalendar.MINUTE, 0);
      gCal1.set(GregorianCalendar.SECOND, 0);      
      gCal2.set(GregorianCalendar.HOUR, 23);
      gCal2.set(GregorianCalendar.MINUTE, 59);
      gCal2.set(GregorianCalendar.SECOND, 59);        
      System.out.println("\n\nNext Start Date Time: "+DateUtil.processDate(gCal1.getTime()));
      System.out.println("\n\nNext End Date Time: "+DateUtil.processDate(gCal2.getTime()));
      
      Date[] startNEnd = {gCal1.getTime(), gCal2.getTime()};
      return startNEnd;
    }
    catch (Exception e)
    {
      _logger.logWarn("getDaily", null, "Error while calculating the next run date time for frequency equals to Weekly.", e);
      return null;
    }
  }
  
  /**
   * Takes in the next run date time as parameter to calculate the next 
   * reporting start date and end date with frequency equals Monthly.
   * @param NRDT next run date time
   * @return Date[0]=next start date time Date[1]=next end date time 
   */
  public static Date[] getMonthly(Date NRDT)
  {
    try
    {
      GregorianCalendar gCal1 = (GregorianCalendar)Calendar.getInstance();
      GregorianCalendar gCal2 = (GregorianCalendar)Calendar.getInstance();
      gCal1.setTime(NRDT);
      gCal2.setTime(NRDT);
      
      int firstMonth = GregorianCalendar.JANUARY;

      //first day of the month, set calendar month-1, day = last day of the month 
      if(gCal1.get(GregorianCalendar.MONTH)!=firstMonth)
      {
        gCal1.add(GregorianCalendar.MONTH, -1);
        gCal1.set(GregorianCalendar.DAY_OF_MONTH, 1);
        gCal2.add(GregorianCalendar.MONTH, -1);
        gCal2.set(GregorianCalendar.DAY_OF_MONTH, gCal2.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
        gCal1.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
        gCal2.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
        gCal1.set(GregorianCalendar.HOUR, 0);
        gCal1.set(GregorianCalendar.MINUTE, 0);
        gCal1.set(GregorianCalendar.SECOND, 0);   
        gCal2.set(GregorianCalendar.HOUR, 23);
        gCal2.set(GregorianCalendar.MINUTE, 59);
        gCal2.set(GregorianCalendar.SECOND, 59);
      }        
      else
      {
        //first day of the year, set calendar year-1, month=DECEMBER, day = last day of the month
        gCal1.add(GregorianCalendar.YEAR, -1);
        gCal1.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
        gCal1.set(GregorianCalendar.DAY_OF_MONTH, 1);
        gCal2.add(GregorianCalendar.YEAR, -1);
        gCal2.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
        gCal2.set(GregorianCalendar.DAY_OF_MONTH, gCal2.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
        gCal1.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
        gCal2.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
        gCal1.set(GregorianCalendar.HOUR, 0);
        gCal1.set(GregorianCalendar.MINUTE, 0);
        gCal1.set(GregorianCalendar.SECOND, 0);      
        gCal2.set(GregorianCalendar.HOUR, 23);
        gCal2.set(GregorianCalendar.MINUTE, 59);
        gCal2.set(GregorianCalendar.SECOND, 59);
      }    
      System.out.println("\n\nNext Start Date Time: "+DateUtil.processDate(gCal1.getTime()));
      System.out.println("\n\nNext End Date Time: "+DateUtil.processDate(gCal2.getTime()));
      Date[] startNEnd = {gCal1.getTime(),gCal2.getTime()}; 
      return startNEnd;
    }
    catch (Exception e)
    {
      _logger.logWarn("getDaily", null, "Error while calculating the next run date time for frequency equals to Daily.", e);
      return null;
    }
  }
}

