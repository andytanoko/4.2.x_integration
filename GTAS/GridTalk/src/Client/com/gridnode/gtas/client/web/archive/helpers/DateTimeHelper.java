/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DateTimeHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 26, 2006        Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.web.archive.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.pdip.framework.util.TimeUtil;

/**
 * A helper class to convert the date time to UTC time based on the given
 * timezone.
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since
 */
public class DateTimeHelper
{
	private static String DATE_TIME_PATTERN = "EEE, MMM dd, yyyy HH:mm:ss a zzz";
	
	/**
	 * Convert the date time to UTC time based on the given
   * timezone
	 * @param date
	 * @param time
	 * @param datePattern
	 * @param timePattern
	 * @param tz
	 * @return
	 */
	public static long convertDateTimeInUTC(String date, String time, String datePattern,
			                                      String timePattern, TimeZone tz)
	{
		if(date == null || "".equals(date))
		{
			return 0;
		}
		//Parse the dateStr
		Date d = DateUtils.parseDate(date+time, null, null, new SimpleDateFormat(datePattern+timePattern));	
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(d.getTime());
		
		//Construct a dateTime based on the given Timezone
		Calendar c1 = Calendar.getInstance(tz);
		c1.set(Calendar.YEAR, c.get(Calendar.YEAR));
		c1.set(Calendar.MONTH,c.get(Calendar.MONTH));
		c1.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
		c1.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
		c1.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
		c1.set(Calendar.SECOND, c.get(Calendar.SECOND));
		c1.set(Calendar.MILLISECOND, c.get(Calendar.MILLISECOND));
		
		
		return TimeUtil.localToUtc(c1.getTimeInMillis());
	}
	
	/**
	 * Convert the given date and time into the format that conform to the given timezone.
	 * @param date
	 * @param time
	 * @param dateTimePattern
	 * @param tz
	 * @param outputDateTimePattern
	 * @return
	 */
	public static String convertDateTimeInTimeZone(String date, String time, String dateTimePattern,
			                                           TimeZone tz, String outputDateTimePattern)
	{
		if(date == null || "".equals(date))
		{
			return "";
		}
		
		//Parse the dateStr
		SimpleDateFormat df = new SimpleDateFormat(dateTimePattern);
		Date d = DateUtils.parseDate(date+time, tz, null, df);		
		
		//Reformat the output format of the Datetime based on the user specfied pattern.
		if(outputDateTimePattern == null || "".equals(outputDateTimePattern))
		{
			outputDateTimePattern = DATE_TIME_PATTERN;
		}
		df = new SimpleDateFormat(outputDateTimePattern);
		df.setTimeZone(tz);
		
		return df.format(d);
	}
	
	public static void main(String[] args)
	throws Exception
{
	/*
	String[] availableTimeZones = TimeZone.getAvailableIDs();
	for(int i =0; i < availableTimeZones.length; i++)
	{
		System.out.println("TZ is "+ availableTimeZones[i]);
	} 
	

	// TODO Auto-generated method stub
	String date = "2005-12-26";
	String time = "15:23:56";
	String dateP = "yyyy-MM-dd";
	String timeP = "hh:mm:ss";
	String dateTimePattern = "EEE, MMM dd, yyyy HH:mm:ss zzz";
	
	DateFormat df = new SimpleDateFormat(dateP);
	Date d = df.parse(date);
	
	TimeZone baghdadTZ = TimeZone.getTimeZone("Europe/Amsterdam");
	Calendar c = Calendar.getInstance(baghdadTZ);
	c.setTimeInMillis(d.getTime());
	System.out.println("Date is "+ c.getTime());
	
	df = new SimpleDateFormat(timeP);
	Date d1 = df.parse(time);
	c = Calendar.getInstance();
	c.setTimeInMillis(d1.getTime());
	System.out.println("time is "+ c.getTime());

	
	System.out.println(convertDateTimeInTimeZone(date, time, dateP+timeP, baghdadTZ, DATE_TIME_PATTERN));
	
	//test for covnertDate in UTC
	System.out.println(new Date(convertDateTimeInUTC(date, time, dateP, timeP,baghdadTZ)));
	
	/*
	TimeZone baghdadTZ = TimeZone.getTimeZone("Etc/GMT-10");
	Calendar c = Calendar.getInstance();
	c.setTimeZone(baghdadTZ);
	
	String pattern = "EEE, MMM dd, yyyy HH:mm:ss zzz";
	c.set(2006,2,26,21,23,23);
	Date d = c.getTime();
	SimpleDateFormat df = new SimpleDateFormat(pattern);
	df.setTimeZone(baghdadTZ);
	//DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL);
	//df.setTimeZone(baghdadTZ);
	System.out.println( "Formatted date is "+df.format(d));
	*/
} 
}
