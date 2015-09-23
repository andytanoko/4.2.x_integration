/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DateUtils.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-09     Andrew Hill         Created
 * 2003-01-13     Andrew Hill         timeZoneCollectionContains()
 * 2003-04-07     Andrew Hill         timeZoneCollectionContains() id match version
 * 2003-05-18     Andrew Hill         parseDate()
 * 2003-05-21     NSL / AH            zeroDate(), zeroTime()
 */
package com.gridnode.gtas.client.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.ITimeConstraint;
import com.gridnode.pdip.framework.util.TimeUtil;

/**
 * Static utility class providing some useful chronological data manipulation operations
 */
public class DateUtils
{
  public static final String DATE_INPUT_PATTERN = "yy-MM-dd HH:mm:ss"; //20030520AH
  public static final String DATE_INPUT_PATTERN_YYYY = "yyyy-MM-dd HH:mm:ss"; //20030521AH
    
  /**
   * Convienience method that returns a string representation of what time the specified Date is
   * in the timezone specified using the DateFormat supplied and the specified locale from which
   * language and local 'cultural rules' are extracted for date display.
   * If the date is null a new one with current time is instantiated as a throwaway object
   * If the timezone and locale are null will use the defaults.
   * If the format is null will create a throwaway DateFormat object with the format string.
   * Any timezone encoded in the format will be restored after the operation - but will not be used
   * even if you pass null for timezone (of course if you pass tz.getTimezone() thats a different
   * matter...) Note that if you supply your own DateFormat, then the locale you pass as a parameter
   * is ignored - so if your passing a SimpleDateFormat you will need to set it yourself.
   * "EEE MMM dd HH:mm:ss zzz yyyy" (which is what Date.toString() uses at the moment)
   * @param date - date to stringify
   * @param tz - TimeZone in which date is to be used (for display usually)
   * @param locale - Locale for language info
   * @param format - a DateFormat object specifiying the output format to use
   * @return string representation of date suitable for display
   */
  public static String formatDateInZone( java.util.Date date,
                                  TimeZone tz,
                                  Locale locale,
                                  DateFormat format)
  {
    TimeZone originalZone = null;
    if(date == null) date = new Date();
    if(tz == null) tz = TimeZone.getDefault();
    if(format == null)
    {
      if(locale == null) locale = Locale.getDefault(); //locale only applies when we create the df
      format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",locale);
    }
    else
    { //If they passed in the zone we need to keep track of its original timeZone so that
      //we can restore it when we are done
      originalZone = format.getTimeZone();
    }
    format.setTimeZone(tz);
    String result = format.format(date);
    if(originalZone != null) format.setTimeZone(originalZone);
    return result;
  }

  /*
   * Convienience method to parse a string into a java.util.Date based on the specified
   * timezone, locale and date format. If DateFormat is not specified then the pattern used will
   * be "yy-MM-dd hh:mm:ss" (note that this is different to the default for formatDateInZone().
   * If the string cannot be converted into a date then null will be returned (no exception
   * thrown).
   * @param dateString A string to parse as a dateTime value
   * @param tz The timezone which the date string is in
   * @param locale Local to use if creating DateFormat automatically
   * @param format A DateFormat specifying string pattern. If null will use yy-MM-dd HH:mm:ss
   * @return date A java.util.Date object or null if string was invalid
   */
  public static java.util.Date parseDate( String dateString,
                                          TimeZone tz,
                                          Locale locale,
                                          DateFormat format)
  { //20030521AH
    return parseDate( dateString, tz, locale, format, ITimeConstraint.ADJ_NONE, true, true);
  } 
   
  public static java.util.Date parseDate( String dateString,
                                          TimeZone tz,
                                          Locale locale,
                                          DateFormat format,
                                          int adjustment,
                                          boolean hasDate,
                                          boolean hasTime)
  { //20030518AH
    //20030521AH - adjustment, hasDate, hasTime support
    if(dateString == null) return null;
    TimeZone originalZone = null;
    if(tz == null) tz = TimeZone.getDefault();
    if(format == null)
    {
      if(locale == null) locale = Locale.getDefault(); 
      format = new SimpleDateFormat(DATE_INPUT_PATTERN, locale);
    }
    else
    { 
      originalZone = format.getTimeZone();
    }
    format.setTimeZone(tz);
    java.util.Date result = null;
    try
    {
      result = format.parse(dateString);
      
      if(!hasTime)
      {
        zeroTime(result,tz);
      }
      
      if(!hasDate)
      {
        zeroDate(result,tz);
      }
      
      switch(adjustment)
      {
        case ITimeConstraint.ADJ_GTAS:
          throw new UnsupportedOperationException("GTAS adjustment not yet supported");
        case ITimeConstraint.ADJ_NONE:
          break; //No adjustment action required
        case ITimeConstraint.ADJ_GTS:
          long adjustedMs = TimeUtil.localToUtc(result.getTime());
          result.setTime(adjustedMs);
          break;
        default:
          throw new IllegalArgumentException("Bad adjustment type:" + adjustment);
      }
      
      return result;
    }
    catch(ParseException pe)
    {
      //Swallow and return null
    }
    if(originalZone != null) format.setTimeZone(originalZone);
    return result;
  }

  /*
   * Given a java.util.Date will adjust it so that the date is 1970-01-01 (epoch)
   * but the hours an minutes remain unaffected.
   * @param time a java.util.Date whose date portion is to be 'zeroed' 
   */
  public static void zeroDate(java.util.Date time, TimeZone tz)
  {  
    Calendar cal = new GregorianCalendar(tz);
    cal.setTime(time);
    cal.setTimeZone(tz);
    
    cal.set(Calendar.YEAR, 1970);
    cal.set(Calendar.MONTH, Calendar.JANUARY);
    cal.set(Calendar.DAY_OF_MONTH, 1);
     
    time.setTime(cal.getTime().getTime());
  }
  
  /*
   * Given a java.util.Date will adjust it so that the time is 00:00:00
   * but the date remains unaffected.
   * @param date a java.util.Date whose time portion is to be 'zeroed' 
   */
  public static void zeroTime(java.util.Date date, TimeZone tz)
  {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.setTimeZone(tz);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    
    date.setTime(cal.getTime().getTime() );  
  }


  /**
   * Converts milliseconds to hours using division
   * @param milliseconds - a number of milliseconds
   * @return hours - a number of hours (not rounded off)
   */
  public static double millisecondsToHours(long milliseconds)
  {
    return (double)(milliseconds / 1000 / 60 / 60);
  }

  /**
   * Converts milliseconds to minutes using division
   * @param milliseconds - a number of milliseconds
   * @return minutes - a number of minutes (not rounded off)
   */
  public static double millisecondsToMinutes(long milliseconds)
  {
    return (double)(milliseconds / 1000 / 60);
  }

  /**
   * Converts nanoseconds to milliseconds using division to return an integral number of
   * milliseconds (precision below millisecond level is lost)
   * @param nanoseconds
   * @return milliseconds (rounded off)
   */
  public static long nanosecondsToMilliseconds(long nanoseconds)
  {
    return (nanoseconds / 1000000);
  }

  /**
   * Converts milliseconds to nanoseconds using multiplication
   * @param nanoseconds
   * @return milliseconds
   */
  public static long millisecondsToNanoseconds(long milliseconds)
  {
    return (milliseconds * 1000000);
  }

  /**
   * Given a Date and a TimeZone will return the milliseconds offset between UTC and the TimeZone
   * specified in force on the Date specified (using the rules in the TimeZone class). This method
   * creates a throwaway Calendar object as part of its processing so if your doing a lot of it
   * you would be better directly using the other version that takes a Calender directly...
   * @param date - the date which will determine what rules are in force for things like DST
   * @param zone - the TimeZone for which the offset is to be returned
   * @returns number of millis the specified timezone was ahead of utc on the date specified
   */
  public static int getTimeZoneOffset(java.util.Date date, TimeZone zone)
  {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.setTimeZone(zone);
    return getTimeZoneOffset(cal);
  }

  /**
   * Given a calendar in which a Date and a TimeZone have been set, will interrogate the
   * TimeZone to return the offset between that TimeZone and the UTC timezone appropriate for
   * the date in question. This takes daylight savings into consideration - using the rules
   * encoded in the TimeZone object (nb: that these are based on rules in force when the java
   * classes were written and may not account for historical changes to the rules).
   * This offset is an additive offset in that for Singapore it will be +8 hours (as opposed to
   * the GTS utcOffset which will be about -8 hours...).
   * @param cal - Calendar object in which the date and TimeZone have been set
   * @returns number of millis the specified timezone was ahead of utc on the date specified
   */
  public static int getTimeZoneOffset(Calendar cal)
  {
    int era = cal.get(Calendar.ERA);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
    int minute = cal.get(Calendar.MINUTE);
    int second = cal.get(Calendar.SECOND);
    int millisecond = cal.get(Calendar.MILLISECOND);
    int millisecondOfDay = (hourOfDay * 60 * 60 * 1000)
                            + (minute * 60 * 1000)
                            + (second * 1000)
                            + millisecond;
    TimeZone tz = cal.getTimeZone();
    int offset = tz.getOffset(era,year,month,day,dayOfWeek,millisecondOfDay);
    return offset;
  }

  /**
   * Will extract the millisecond value from a java.util.Date using its getTime() method,
   * however is also smart enough to reconstruct the milliseconds value for a
   * java.sql.Timestamp by interrogating its getNanos() method (Timestamp rounds off the last
   * second and stores the extra milliseconds remainder in its seperate nanoseconds field).
   * @parameter date - a java.util.Date or subclass instance
   * @return ms - the (integral) number of milliseconds past epoch utc represented by the date
   */
  public static long getMilliseconds(java.util.Date date)
  {
    if(date instanceof java.sql.Timestamp)
    {
      // We need to get the fractional part of the second from the nanos fields
      // convert it to milliseconds and add to the millisecond value to get the
      // precise number of milliseconds. (Note how we just anyhow discarded the
      // fractional nonoseconds in the last millisecond!)
      java.sql.Timestamp ts = (java.sql.Timestamp)date;
      return(ts.getTime() + (long)nanosecondsToMilliseconds((long)ts.getNanos()));
    }
    else
    {
      return date.getTime();
    }
  }

  /**
   * Returns the nanosecond value of a java.util.Date or subclass instance. Smart enough to
   * interrogate the getNanos() method if the date is a java.sql.Timestamp
   * @param date - a java.util.Date instance or subclass instance such as java.sql.Timestamp
   * @return ns - the number of nanoseconds past the epoch utc represented by the Date
   */
  public static long getNanoseconds(java.util.Date date)
  {
    if(date instanceof java.sql.Timestamp)
    {
      //Timestamp can store nanoseconds so we must add them
      java.sql.Timestamp ts = (java.sql.Timestamp)date;
      return millisecondsToNanoseconds( ts.getTime() ) + ts.getNanos();
    }
    else
    {
      //Just a simple conversion
      return millisecondsToNanoseconds(date.getTime());
    }
  }

  /**
   * Convienience method to  create a java.util.Date object from a nanosecond value.
   * Note that this merely divides the nanoseconds by 1000000 resulting in loss of precision
   * beyond the level of a millisecond.
   * @param nanoseconds
   * @return date
   */
  public static java.util.Date getUtilDate(long nanoseconds)
  {
    return new java.util.Date( nanoseconds / 1000000 );
  }

  /**
   * Will create a java.sql.Timestamp based on the nanoseconds passed which will be stored in the
   * timestamps nanos field.
   * WARNING: UNTESTED - may be completely buggy!
   * @param nanoseconds
   * @return timestamp
   */
  public static java.sql.Timestamp getSqlTimestamp(long nanoseconds)
  {
    //Timestamp will store (as ms of course) only an integral number of seconds in its millisecond
    //value - the fractional second info is stored in the nanos field.
    long seconds = nanoseconds / 1000000000;
    java.sql.Timestamp ts = new java.sql.Timestamp(seconds * 1000);
    long ns = nanoseconds % 1000000000;
    ts.setNanos((int)ns);
    return ts;
  }

  /**
   * Returns true if the collection of timezones supplied already contains a timezone whose
   * rules match that of the one specified according to TimeZone.hasSameRules()
   * Including objects that arent java.util.TimeZone or null elements will cause an exception
   * to be thrown.
   * @param timeZones - collection of TimeZone objects
   * @param zone - zone to search for based on matching rules
   * @return true or false depending on whether matching zone found
   */
  public static boolean timeZoneCollectionContains(Collection timeZones, TimeZone zone)
  { //20030113AH
    return timeZoneCollectionContains(timeZones,zone,false); //20030407AH
  }

  public static boolean timeZoneCollectionContains(Collection timeZones, TimeZone zone, boolean idMatch)
  { //20030113AH, mod 20030407AH
    if( (timeZones == null) || timeZones.isEmpty() )
    {
      return false;
    }
    Iterator i = timeZones.iterator();
    while(i.hasNext())
    {
      TimeZone element = (TimeZone)i.next();
      if(element == null)
      {
        throw new java.lang.NullPointerException("timeZones collection contains a null element");
      }
      if(idMatch)
      {
        if(element.getID().equals(zone.getID()))
        {
          return true;
        }
      }
      else
      {
        if(element.hasSameRules(zone))
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns a List of timezones whose offset (at the time given in date) matches the offset passed.
   * Does not include the deprecated 3 letter id zones.
   * @param date - a date for which time rules offset is calculated for comparison
   * @param offset - offset to compare with
   * @returns candidate timezones (sublist)
   * @throws GTClientException if an error occurs
   */
  public static List getTimeZones(Date date, int offset)
    throws GTClientException
  { //20030113AH
    try
    {
      Calendar cal = new GregorianCalendar();
      if(date != null) cal.setTime(date); //20030407AH
      String[] zones = TimeZone.getAvailableIDs();
      ArrayList candidates = new ArrayList( date==null ? zones.length : 16); //20030407AH
      for(int i=0; i < zones.length; i++)
      {
        TimeZone tz = TimeZone.getTimeZone(zones[i]);
        String id = tz.getID();
        if(id.length() != 3)
        {
          if(date == null)
          { //20030407AH
            candidates.add(tz);
          }
          else
          {
           // boolean inDst = tz.inDaylightTime(date);
            cal.setTimeZone(tz);
            int zoneOffset = getTimeZoneOffset(cal);
            if( zoneOffset == offset )
            {
              candidates.add(tz);
            }
          }
        }
      }
      return candidates;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting candidate TimeZones",t);
    }
  }
  
}