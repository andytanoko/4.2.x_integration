/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveAlertProvider.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Aug 8, 2004 			Mahesh             	Created
 * Mar 31, 2006     Tam Wei Xiang       New elements added in the provider list
 * Sep 19, 2006     Tam Wei Xiang       Added new elements PARTNER_FOR_ARCHIVE,
 *                                      IS_ENABLE_SEARCH_ARCHIVE,IS_ENABLE_RESTORE_ARCHIVE 
 * Jun 14, 2007     Tam Wei Xiang       Added BE_ID_FOR_ARCHIVE, IS_ARCHIVE_ORPHAN_RECORD,
 *                                      ARCHIVE_ID                                       
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.gridnode.pdip.app.alert.providers.AbstractDetails;

public class ArchiveAlertProvider extends AbstractDetails
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5305837123064641087L;

	public static final String NAME = "ARCHIVE";
  
  public static final String FIELD_STATUS   = "STATUS";
  
  //TWX: 31 Mar 2006 new
  public static final String FIELD_ARCHIVE_BY = "ARCHIVE_BY";
  public static final String FIELD_ARCHIVE_START_TIME = "START_TIME";
  public static final String FIELD_ARCHIVE_START_TIME_UTC = "START_TIME_UTC";
  
  public static final String FIELD_FROM_DATE_TIME = "FROM_DATE_TIME";
  public static final String FIELD_FROM_DATE_TIME_UTC = "FROM_DATE_TIME_UTC";
  
  public static final String FIELD_TO_DATE_TIME = "TO_DATE_TIME";
  public static final String FIELD_TO_DATE_TIME_UTC = "TO_DATE_TIME_UTC";
  
  public static final String FIELD_PROCESS_DEF_LIST = "PROCESS_DEF_LIST";
  public static final String FIELD_DOC_TYPE_LIST = "DOC_TYPE_LIST"; //It can be process definition type or doc type
  public static final String FIELD_INCLUDE_INCOMPLETE = "INCLUDE_INCOMPLETE";
  public static final String FIELD_NUM_ARCHIVED = "NUM_ARCHIVED";
  public static final String FIELD_FOLDER_LIST = "FOLDER_LIST";
  public static final String FIELD_NUM_FAILED = "NUM_FAILED";
  
  public static final String FIELD_IS_DOC_TYPE_NULL = "IS_DOC_TYPE_NULL";
  private final String DATE_TIME_FORMAT = "dd MMM yyyy HH:mm:ss zzz";
  
  public static final String FIELD_PARTNER_FOR_ARCHIVE = "PARTNER_FOR_ARCHIVE";
  public static final String FIELD_IS_ENABLE_SEARCH_ARCHIVE = "IS_ENABLE_SEARCH_ARCHIVE";
  public static final String FIELD_IS_ENABLE_RESTORE_ARCHIVE = "IS_ENABLE_RESTORE_ARCHIVE";
  
  public static final String FIELD_BE_ID_FOR_ARCHIVE = "BE_ID_FOR_ARCHIVE";
  public static final String FIELD_IS_ARCHIVE_ORPHAN_RECORD = "IS_ARCHIVE_ORPHAN_RECORD";
  public static final String FIELD_ARCHIVE_ID = "ARCHIVE_ID";
  
  public ArchiveAlertProvider(String status)
  {
    set(FIELD_STATUS,status);
  }
  
  /**
   * Applicable for archive by process instance
   * @param byProcess
   * @param archiveStartTime
   * @param fromDateTime
   * @param toDateTime
   * @param processDefList
   * @param includeIncomplete
   * @param numDocs
   */
  public ArchiveAlertProvider(Boolean byProcess, Date archiveStartTime, Date fromDateTime,
  		                        Date toDateTime, List processDefList, Boolean includeIncomplete,
  		                        String numDocs, Boolean isEnableRestoreArchive, Boolean isEnableSearchArchive,
  		                        List partnerForArchive, List beIDForArchive, String archiveID, Boolean isArchiveOrphan)
  {
  	set(FIELD_ARCHIVE_BY, ((byProcess.booleanValue())? "Process" : "Document"));
  	set(FIELD_ARCHIVE_START_TIME, archiveStartTime);
  	set(FIELD_ARCHIVE_START_TIME_UTC, archiveStartTime);
  	
  	set(FIELD_FROM_DATE_TIME, fromDateTime);
  	set(FIELD_FROM_DATE_TIME_UTC, fromDateTime); //an appropriate conversion will be executed while we get the field
  	
  	set(FIELD_TO_DATE_TIME, toDateTime);
  	set(FIELD_TO_DATE_TIME_UTC, toDateTime); //an appropriate conversion will be executed while we get the field
  	
  	set(FIELD_PROCESS_DEF_LIST, convertListToString(processDefList));
  	set(FIELD_NUM_ARCHIVED, numDocs);
  	set(FIELD_INCLUDE_INCOMPLETE, includeIncomplete);
  	set(FIELD_IS_DOC_TYPE_NULL, null); //a field to determine whether we are archive by process or doc.
  	                                   //we are not using FIELD_ARCHIVE_BY to determine it because
  	                                   //the implementation of the template require the value
  	                                   //to be either null or not null in order to differentiate.
  	set(FIELD_IS_ENABLE_RESTORE_ARCHIVE, isEnableRestoreArchive);
  	set(FIELD_IS_ENABLE_SEARCH_ARCHIVE, isEnableSearchArchive);
  	set(FIELD_PARTNER_FOR_ARCHIVE, partnerForArchive);
    
    set(FIELD_BE_ID_FOR_ARCHIVE, beIDForArchive);
    set(FIELD_ARCHIVE_ID, archiveID);
    set(FIELD_IS_ARCHIVE_ORPHAN_RECORD, isArchiveOrphan);
  }
  
  /**
   * Applicable for archive by document
   * @param byProcess
   * @param archiveStartTime
   * @param fromDateTime
   * @param toDateTime
   * @param docTypeList
   * @param folderType
   * @param numDocs
   */
  public ArchiveAlertProvider(Boolean byProcess, Date archiveStartTime, Date fromDateTime,
      Date toDateTime, List docTypeList, List folderType,
      String numDocs, Boolean isEnableRestoreArchive, Boolean isEnableSearchArchive,
      List partnerForArchive, List beIDForArchive, String archiveID, Boolean isArchiveOrphan)
  {
  	set(FIELD_ARCHIVE_BY, ((byProcess.booleanValue())? "Process" : "Document"));
  	set(FIELD_ARCHIVE_START_TIME, archiveStartTime);
  	set(FIELD_ARCHIVE_START_TIME_UTC, archiveStartTime);

  	set(FIELD_FROM_DATE_TIME, fromDateTime);
  	set(FIELD_FROM_DATE_TIME_UTC, fromDateTime);

  	set(FIELD_TO_DATE_TIME, toDateTime);
  	set(FIELD_TO_DATE_TIME_UTC, toDateTime);
  	
  	set(FIELD_FOLDER_LIST, convertListToString(folderType));
  	set(FIELD_DOC_TYPE_LIST, convertListToString(docTypeList));
  	set(FIELD_NUM_ARCHIVED, numDocs);
  	
  	set(FIELD_IS_DOC_TYPE_NULL, "");
  	
  	set(FIELD_IS_ENABLE_RESTORE_ARCHIVE, isEnableRestoreArchive);
  	set(FIELD_IS_ENABLE_SEARCH_ARCHIVE, isEnableSearchArchive);
  	set(FIELD_PARTNER_FOR_ARCHIVE, partnerForArchive);
    
  	set(FIELD_BE_ID_FOR_ARCHIVE, beIDForArchive);
    set(FIELD_ARCHIVE_ID, archiveID);
    set(FIELD_IS_ARCHIVE_ORPHAN_RECORD, isArchiveOrphan);
  }
  
  public void setTotalFailed(String numFailed)
  {
  	set(FIELD_NUM_FAILED, numFailed);
  }
  
  public final static List getFieldNameList()
  {
    List<String> list = new ArrayList<String>();
    list.add(FIELD_STATUS);
    
    //new
    list.add(FIELD_ARCHIVE_BY);
    list.add(FIELD_ARCHIVE_START_TIME);
    list.add(FIELD_ARCHIVE_START_TIME_UTC);
    
    list.add(FIELD_FROM_DATE_TIME);
    list.add(FIELD_FROM_DATE_TIME_UTC);
    
    list.add(FIELD_TO_DATE_TIME);
    list.add(FIELD_TO_DATE_TIME_UTC);
    
    list.add(FIELD_PROCESS_DEF_LIST);
    list.add(FIELD_DOC_TYPE_LIST);
    list.add(FIELD_NUM_ARCHIVED);
    list.add(FIELD_INCLUDE_INCOMPLETE);
    
    list.add(FIELD_FOLDER_LIST);
    list.add(FIELD_NUM_FAILED);
    
    list.add(FIELD_PARTNER_FOR_ARCHIVE);
    list.add(FIELD_IS_ENABLE_RESTORE_ARCHIVE);
    list.add(FIELD_IS_ENABLE_SEARCH_ARCHIVE);
    
    list.add(FIELD_IS_ARCHIVE_ORPHAN_RECORD);
    list.add(FIELD_BE_ID_FOR_ARCHIVE);
    list.add(FIELD_ARCHIVE_ID);
    
    return list;
  }
    
  public String getName()
  {
    return NAME;
  }
  
  private String convertListToString(List list)
  {
  	String str =  "";
  	for(int i = 0; i < list.size(); i++)
  	{
  		str += (String)list.get(i)+", ";
  	}
  	if(str.length() > 0)
  	{
  		return str.substring(0,str.lastIndexOf(","));
  	}
  	else
  	{
  		return null;
  	}
  }
  
/**
   * @Override
   * 
   * Get the value of a field, formatted to a specified pattern.
   * @param fieldName The name of the field.
   * @param pattern The pattern to format to. <b>null</b> if pattern is not
   * required, in which case, default pattern may be applied to certain value
   * types like Date, Double, or Float.
   * @return The field value, formatted to the specified pattern.
   */
  public String get(String fieldName, String pattern)
  {
    Object value = getFieldValue(fieldName);
    if (value == null)
      return null;

    String val = null;

    if (value instanceof Date) //to cater for the need of new Archive alert
    {
    	
    	boolean isConvertedToUTC = fieldName.endsWith("UTC");
    	if(isConvertedToUTC)
    	{
    		TimeZone outputTZ = TimeZone.getTimeZone("Etc/UTC");
    		val = formatDateTime((Date)value,pattern,null, outputTZ);
    	}
    	else
    	{
    		val = formatDateTime((Date)value, pattern, null, null);
    	}
    }
    else if(value instanceof Number)  // Value obtained is of type Number
    {
      val = formatNumber(value, pattern);
    }
    else
    {
    	if (pattern != null && pattern.length()>0)
    	{
    		val = MessageFormat.format(pattern, new Object[]{value});
    	}
    }
    return (val == null ? value.toString() : val);
  }
  
  private String formatDateTime(Date date, String pattern, TimeZone givenDateTZ, TimeZone outputTZ)
  { 
  	if(givenDateTZ == null)
  	{
  		givenDateTZ = TimeZone.getTimeZone("Etc/UTC"); //the given timezone is set to UTC
  	}
  	if(outputTZ == null)
  	{
  		outputTZ = TimeZone.getDefault(); //system default TZ will be used.
  	}
  	
  	if(pattern== null || "".equals(pattern.trim()))
  	{
  		pattern = DATE_TIME_FORMAT;
  	}
  	
  	Calendar c = Calendar.getInstance();
  	c.setTimeInMillis(date.getTime());
  	
  	//Reconstruct the date time based on the givenDateTZ
  	Calendar c1 = Calendar.getInstance(givenDateTZ);
  	c1.set(Calendar.YEAR, c.get(Calendar.YEAR));
		c1.set(Calendar.MONTH,c.get(Calendar.MONTH));
		c1.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
		c1.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
		c1.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
		c1.set(Calendar.SECOND, c.get(Calendar.SECOND));
		c1.set(Calendar.MILLISECOND, c.get(Calendar.MILLISECOND));
  	
		//Based on the outputTZ, we format the newly construct date time to the given pattern 
  	SimpleDateFormat df = new SimpleDateFormat(pattern);
  	df.setTimeZone(outputTZ);
  	
  	return df.format(c1.getTime());
  }
  
  /*
  public static void main(String[] args)
  	throws Exception
  {
  	TimeZone tz = TimeZone.getTimeZone("Etc/UTC");
  	TimeZone givenTZ = TimeZone.getTimeZone("Etc/GMT-5");
  	Calendar c = Calendar.getInstance();
  	c.set(2006,3,3,1,0,0);
  	
  	String datePattern = "dd/MM/yyyy HH:mm:ss";
  	//String afterF = formatDateTime(c.getTime(), datePattern, givenTZ, tz);
  	//System.out.println("after formatted is "+ afterF);
  	
  	SimpleDateFormat df = new SimpleDateFormat(datePattern);
  	df.setTimeZone(tz);
  	System.out.println("after format is "+df.parse("03/04/2006 13:30:00"));
  	
  	//from local TZ to UTC
  	Calendar c1 = Calendar.getInstance();
  	c = Calendar.getInstance(tz);
  	c.setTimeInMillis(c1.getTimeInMillis());
  	
  	System.out.println("time is "+new Date(TimeUtil.localToUtc(c1.getTimeInMillis())));
  	
  	//tz = TimeZone.getTimeZone("Etc/GMT-5");
  	df.setTimeZone(tz);
  	
  	System.out.println("date in utc is "+ df.format(c.getTime()));
  	
  	Date d = DateUtils.parseDate("03/04/2006 13:00:00", null, null, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
  	
  	Timestamp ts = new Timestamp(d.getTime());
  	System.out.println("ts is "+d.toString());
  	
  	/*
  	long inUTC = DateTimeHelper.convertDateTimeInUTC("03/04/2006","13:00","dd/MM/yyyy","HH:mm",TimeZone.getDefault());
  	c = Calendar.getInstance(tz);
  	c.setTimeInMillis(inUTC);
  	d = c.getTime();
  	df = new SimpleDateFormat(datePattern);
  	String s = df.format(d);
  	System.out.println("UTC time is "+s);
  	
  } */
}
