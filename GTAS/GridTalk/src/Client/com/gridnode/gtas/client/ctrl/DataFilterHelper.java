/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterHelper.java
 *
 ****************************************************************************
 * Date           			Author              			Changes
 ****************************************************************************
 * Sep 23, 2005        	Tam Wei Xiang       			Created
 * Oct 11, 2005				 	Sumedh Chalermkanjana  		Added generateAssocEsDocFilter method. 
 * Mar 24, 2006         Tam Wei Xiang             Added new search criterias
 *                                                     -processStartToDate, processStartToTime
 *                                                     -processStartFromDate, processStartFromTime  
 * Dec 04, 2006         Regina Zeng               Added new search criteras
 *                                                     -remark  
 * Dec 14  2006         Tam Wei Xiang             Added new criteria remark for searchesDoc  
 * Dec 19 2006          Tam Wei Xiang             Alter the search criteria from dateTimeSentStart
 *                                                to dateTimeSendEnd                                                                                                                                
 */
package com.gridnode.gtas.client.ctrl;

import java.sql.Timestamp;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Calendar;

import com.gridnode.gtas.server.dbarchive.model.ProcessInstanceMetaInfo;
import com.gridnode.gtas.server.dbarchive.model.DocumentMetaInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.gtas.client.web.archive.helpers.DateTimeHelper;
import com.gridnode.gtas.client.web.archive.helpers.Logger;
import com.gridnode.gtas.events.dbarchive.EsDocSearchQuery;
import com.gridnode.gtas.events.dbarchive.EsPiSearchQuery;
import com.gridnode.pdip.framework.util.*;

public class DataFilterHelper
{
		private static final String DATE_TYPE_FROM = "from";
		private static final String DATE_TYPE_TO = "to";
		private static final String DATE_FORMAT = "yyyy-MM-dd";
		private static final String TIME_FORMAT = "HH:mm:ss.SSS";
	
	/**
	 * This method will generate a datafilter which capture the search criteria that a user key in.
	 * @param docNo the document no.
	 * @param docType the doc type is PRPOCESS INSTANCE DEF.  Note: diff from search by document
	 * @param tradingPartner the PARTNER ID.
	 * @param fromDate The date we search is based on DOCUMENT DATE GENERATED.  Note: diff from search by document
	 * @param toDate same as above.
	 * @return a data filter
	 */
	public static IDataFilter generateProcessInstanceFilter(EsPiSearchQuery searchQuery,TimeZone userSelectTZ)
	{
		//This whole chunk of code is very messy !!!
		
		String docNo = searchQuery.getDocNo();
		String docType = searchQuery.getProcess();
		String processState = searchQuery.getProcessState();
		String tradingPartner = searchQuery.getPartnerId();
		String partnerName = searchQuery.getPartnerName();
		String fromDate = searchQuery.getDocDateFrom();
		String toDate = searchQuery.getDocDateTo();
		String processStartFromDate = searchQuery.getProcessStartFromDate();
		String processStartFromTime = searchQuery.getProcessStartFromTime();
		String processStartToDate = searchQuery.getProcessStartToDate();
		String processStartToTime = searchQuery.getProcessStartToTime();
		String userTrackingID = searchQuery.getUserTrackingID();
    String remark = searchQuery.getRemark();
          
		Logger.log("[DataFilter] EsPiSearchQuery "+searchQuery.toString());
		
		//boolean emptyDocNo = true, emptyDocType = true, emptyTradingPartner = true, emptyPartnerName = true, emptyProcessState = true;
		
		boolean isPreviousFieldEmpty = true;
  	DataFilterImpl filter = new DataFilterImpl();
  	if(docNo!=null && docNo.compareTo("")!=0)
  	{
  		filter.addSingleFilter(null, ProcessInstanceMetaInfo.Doc_Number, 
  													 filter.getLocateOperator(), docNo,false);
  		isPreviousFieldEmpty = false;
  	}
  	if(userTrackingID != null && userTrackingID.compareTo("") != 0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, ProcessInstanceMetaInfo.USER_TRACKING_ID,
  		                       filter.getEqualOperator(), userTrackingID, false);
  		isPreviousFieldEmpty = false;
  	}
  	
  	if(docType!=null && docType.compareTo("")!=0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, ProcessInstanceMetaInfo.Process_Def,
  				                   filter.getEqualOperator(), docType, false);
  		isPreviousFieldEmpty = false;
  	}
  	if(processState!=null && processState.compareTo("")!=0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, ProcessInstanceMetaInfo.Process_State,
  				                   filter.getEqualOperator(), processState, false);
  		isPreviousFieldEmpty = false;
  	}
  	
  	if(tradingPartner!=null && tradingPartner.compareTo("")!=0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, ProcessInstanceMetaInfo.Partner_ID,
  				                   filter.getLocateOperator(), tradingPartner, false);
  		isPreviousFieldEmpty = false;
  	}
  	
  	//26 OCT 2005 TWX: add in partner name
  	if(partnerName!=null && partnerName.compareTo("")!=0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, ProcessInstanceMetaInfo.Partner_Name,
  													 filter.getLocateOperator(), partnerName, false);
  		isPreviousFieldEmpty = false;
  	}
  	if(fromDate!=null && fromDate.compareTo("")!=0)
  	{
  		long fromDateInMillisecond = convertTimeInMilli(userSelectTZ, fromDate, DATE_TYPE_FROM);
  		
  		long toDateInMillisecond = convertTimeInMilli(userSelectTZ, toDate, DATE_TYPE_TO);;

  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		
  		if(fromDateInMillisecond > 0)
  		{
  			String fromRnDate = convertTimeInMilliToRNDate(fromDateInMillisecond);
  			filter.addSingleFilter(connector, ProcessInstanceMetaInfo.Doc_Date_Generated,
  			                       filter.getGreaterOrEqualOperator(), fromRnDate, false);
  		}
  		if(toDateInMillisecond > 0)
  		{
  			String toRnDate = convertTimeInMilliToRNDate(toDateInMillisecond);
  			filter.addSingleFilter(filter.getAndConnector(), ProcessInstanceMetaInfo.Doc_Date_Generated,
  				                   filter.getLessOperator(), toRnDate, false);
  		}
  		isPreviousFieldEmpty = false;
  		
  	}
  	
  	if(processStartFromDate != null && !processStartFromDate.equals("") )
  	{
  		processStartFromTime = getProcessTime(processStartFromTime, DATE_TYPE_FROM);
  		processStartToTime = getProcessTime(processStartToTime, DATE_TYPE_TO);
  		
  		long startFromDateInUTC = DateTimeHelper.convertDateTimeInUTC(processStartFromDate, processStartFromTime,
  		                                                              DATE_FORMAT,TIME_FORMAT,userSelectTZ);
  		
  		long startToDateInUTC = DateTimeHelper.convertDateTimeInUTC(processStartToDate, processStartToTime,
  		                                                            DATE_FORMAT,TIME_FORMAT,userSelectTZ);

  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		
  		if(startFromDateInUTC > 0)
  		{
  			filter.addSingleFilter(connector, ProcessInstanceMetaInfo.Process_Start_Date, 
  			                       filter.getGreaterOrEqualOperator(),new Timestamp(startFromDateInUTC), false );
  		}
  		if(startToDateInUTC > 0)
  		{
  			filter.addSingleFilter(filter.getAndConnector(), ProcessInstanceMetaInfo.Process_Start_Date,
  			                       filter.getLessOrEqualOperator(),new Timestamp(startToDateInUTC), false);
  		}
  			
  		isPreviousFieldEmpty = false;
  	}
    
    //04122006 RZ: Added remark search critera    
    if(remark!=null && remark.compareTo("")!=0)
    {
      FilterConnector connector = null;
      if(!isPreviousFieldEmpty)
      {
        connector = filter.getAndConnector();
      }
      if(remark.equals("Valid"))
      {
        
        filter.addSingleFilter(connector, ProcessInstanceMetaInfo.REMARK, filter.getEqualOperator(),"", false);
        filter.addSingleFilter(filter.getOrConnector(), ProcessInstanceMetaInfo.REMARK, filter.getEqualOperator(),null, false);
      }
      else
      {
        filter.addSingleFilter(connector, ProcessInstanceMetaInfo.REMARK, filter.getNotEqualOperator(),"" , false);
      }
      isPreviousFieldEmpty = false;
    }
  	
  	Logger.log("[DataFilterHelper.generateProcessInstanceFilter] Filter construct is "+ filter.getFilterExpr());
  	return filter;
	}
	
	/**
	 * This method will generate a datafilter which capture the search criteria that a user key in.
	 * @param docNo document no.
	 * @param docType It is the USER defined DOC TYPE   Note diff from search by process instance
	 * @param tradingPartner PARTNER ID
	 * @param fromDate The date we search is based on DATE TIME CREATED.  Note diff from search by process instance
	 * @param toDate same as above
	 * @return
	 */
	public static IDataFilter generateDocumentMetaInfoFilter(EsDocSearchQuery searchQuery, TimeZone userSelectTZ)
	{
		String docNo = searchQuery.getDocNo();
		String docType = searchQuery.getDocType();
		String tradingPartner = searchQuery.getPartnerId();
		String partnerName = searchQuery.getPartnerName();
		
		String sendDateFrom = searchQuery.getDocDateSentFrom();
		String sendDateTo = searchQuery.getDocDateSentTo();
		String receiveDateFrom = searchQuery.getDocDateReceivedFrom();
		String receiveDateTo = searchQuery.getDocDateReceivedTo();
		
		//TWX 27 Mar 2006 new search criterias
		String fromCreateDate = searchQuery.getFromCreateDate();
		String toCreateDate = searchQuery.getToCreateDate();
		String fromCreateDateTime = getProcessTime(searchQuery.getFromCreateDateTime(), DATE_TYPE_FROM);
		String toCreateDateTime = getProcessTime(searchQuery.getToCreateDateTime(), DATE_TYPE_TO);
		
		String sendDateFromTime = getProcessTime(searchQuery.getFromSentDateTime(), DATE_TYPE_FROM);
		String sendDateToTime = getProcessTime(searchQuery.getToSentDateTime(), DATE_TYPE_TO);
		
		String receiveDateFromTime = getProcessTime(searchQuery.getFromReceivedDateTime(),DATE_TYPE_FROM);
		String receiveDateToTime =  getProcessTime(searchQuery.getToReceivedDateTime(), DATE_TYPE_TO);
		
		String docDateFrom = searchQuery.getDocDateFrom();
		String docDateFromTime = getProcessTime(searchQuery.getFromDocDateTime(), DATE_TYPE_FROM);
		
		String docDateTo = searchQuery.getDocDateTo();
		String docDateToTime = getProcessTime(searchQuery.getToDocDateTime(), DATE_TYPE_TO);
		
		String folder = searchQuery.getFolder();
		String userTrackingID = searchQuery.getUserTrackingID();
		
    String remark = searchQuery.getRemark();
    
		Logger.log("[DataFilterHelper.generateDocumentMetaInfoFilter] searchQuery is "+searchQuery.toString());
		
		boolean isPreviousFieldEmpty = true;
		//boolean emptyDocNo = true, emptyDocType = true, emptyTradingPartner = true, emptyPartnerName = true;
  	DataFilterImpl filter = new DataFilterImpl();
  	if(docNo!=null && docNo.compareTo("")!=0)
  	{
  	
  		filter.addSingleFilter(null, DocumentMetaInfo.Doc_Number, 
  													 filter.getLocateOperator(), docNo,false);
  		isPreviousFieldEmpty = false;
  	}
  	if(docType!=null && docType.compareTo("")!=0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, DocumentMetaInfo.Doc_Type,
  				                   filter.getEqualOperator(), docType, false);
  		isPreviousFieldEmpty = false;
  	}
  	
  	if(userTrackingID != null && ! "".equals(userTrackingID))
  	{
  		FilterConnector connector = null;
  		if(! isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, DocumentMetaInfo.USER_TRACKING_ID, filter.getEqualOperator(), userTrackingID, false);
  		isPreviousFieldEmpty =false;
  	}
  	
  	if(tradingPartner!=null && tradingPartner.compareTo("")!=0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, DocumentMetaInfo.Partner_ID,
  				                   filter.getLocateOperator(), tradingPartner, false);
  		isPreviousFieldEmpty = false;
  	}
  	//26 OCT 2005 TWX: add in partner name
  	if(partnerName!=null && partnerName.compareTo("")!=0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, DocumentMetaInfo.Partner_Name,
  													 filter.getLocateOperator(), partnerName, false);
  		isPreviousFieldEmpty = false;
  	}
  	
  	//DOC date will be in T.  Z
  	if(docDateFrom!=null && docDateFrom.compareTo("")!=0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		long docDateFromInMilli = DateTimeHelper.convertDateTimeInUTC(docDateFrom,docDateFromTime,
  				                                                         DATE_FORMAT,TIME_FORMAT,userSelectTZ);
  		long docDateToInMilli = DateTimeHelper.convertDateTimeInUTC(docDateTo, docDateToTime,
  				                                                         DATE_FORMAT, TIME_FORMAT, userSelectTZ); 
  		if(docDateFromInMilli > 0)
  		{
  			String fromRNDate = convertTimeInMilliToRNDate(docDateFromInMilli);
  			filter.addSingleFilter(connector, DocumentMetaInfo.Doc_Date_Generated,
  			                       filter.getGreaterOrEqualOperator(), fromRNDate, false);
  		}
  		if(docDateToInMilli > 0)
  		{
  			String toRNDate = convertTimeInMilliToRNDate(docDateToInMilli);
  			filter.addSingleFilter(filter.getAndConnector(), DocumentMetaInfo.Doc_Date_Generated,
  				                   filter.getLessOrEqualOperator(), toRNDate, false);
  		}
  		isPreviousFieldEmpty = false;
  	}
  	
  	if(sendDateFrom!=null && sendDateFrom.compareTo("")!=0)
  	{
  		Integer dateFieldID = (Integer)DocumentMetaInfo.Date_Time_Send_End;
  		//Integer dateToFieldID = (Integer)DocumentMetaInfo.Date_Time_Receive_End;
  		addSearchDateCriteria(filter, sendDateFrom, sendDateTo, dateFieldID, userSelectTZ, isPreviousFieldEmpty,
  				                  sendDateFromTime, sendDateToTime);
  		isPreviousFieldEmpty = false;
  	}
  	
  	if(receiveDateFrom!=null && receiveDateFrom.compareTo("")!=0)
  	{
  		Integer dateFieldID = (Integer)DocumentMetaInfo.Date_Time_Receive_End;
  		addSearchDateCriteria(filter, receiveDateFrom, receiveDateTo, dateFieldID, userSelectTZ, isPreviousFieldEmpty,
  				                  receiveDateFromTime, receiveDateToTime);
  		isPreviousFieldEmpty = false;
  	}
  	
  	if(fromCreateDate!= null && fromCreateDate.compareTo("")!=0)
  	{
  		Integer dateFieldID = (Integer)DocumentMetaInfo.Date_Time_Create;
  		addSearchDateCriteria(filter, fromCreateDate, toCreateDate, dateFieldID, userSelectTZ, isPreviousFieldEmpty,
  				                 fromCreateDateTime, toCreateDateTime);
  		isPreviousFieldEmpty = false;
  	}
  	
  	if(folder!= null && folder.compareTo("") != 0)
  	{
  		FilterConnector connector = null;
  		if(!isPreviousFieldEmpty)
  		{
  			connector = filter.getAndConnector();
  		}
  		filter.addSingleFilter(connector, DocumentMetaInfo.Folder,
  													 filter.getEqualOperator(), folder, false);
  		isPreviousFieldEmpty = false;
  	}
  	
    //TWX Add criteria for remark
  	if(remark!=null && remark.compareTo("")!=0)
    {
      FilterConnector connector = null;
      if(!isPreviousFieldEmpty)
      {
        connector = filter.getAndConnector();
      }
      if(remark.equals("Valid"))
      {
        
        filter.addSingleFilter(connector, DocumentMetaInfo.REMARK, filter.getEqualOperator(),"", false);
        filter.addSingleFilter(filter.getOrConnector(), DocumentMetaInfo.REMARK, filter.getEqualOperator(),null, false);
      }
      else
      {
        filter.addSingleFilter(connector, DocumentMetaInfo.REMARK, filter.getNotEqualOperator(),"" , false);
      }
      isPreviousFieldEmpty = false;
    }
    
  	Logger.log("[ActionHelper.generateDocumentMetaInfoFilter] Filter construct is "+ filter.getFilterExpr());
  	return filter;
	}
	
	/**
	 * The returned filter is used to find a list of associated DocumentMetaInfo objects based on process instance uid: <code>uid</code>.
	 */
	public static IDataFilter generateAssocEsDocFilter(Long uid)
	{
		AssertUtil.assertTrue(uid != null);
		DataFilterImpl filter = new DataFilterImpl();
		filter.addSingleFilter(null, ProcessInstanceMetaInfo.UID, 
													 filter.getEqualOperator(), uid, false);
		return filter;
	}
	
	/**
	 * Convert the time(it follow the user selected timezone) to UTC format
	 * @param tz The timezone that user select during login
	 * @param date
	 * @return
	 */
	private static long convertTimeInMilli(TimeZone tz, String date, String dateType)
	{
		if(date == null || date.length() == 0)
		{
			return 0;
		}
		
		StringTokenizer st = new StringTokenizer(date,"-");
		int year = Integer.parseInt(st.nextToken());
	  int month = Integer.parseInt(st.nextToken());
		int day = Integer.parseInt(st.nextToken());
		
		Calendar c = Calendar.getInstance(tz);
		if(dateType.compareTo(DATE_TYPE_TO)==0)
		{
			c.set(year, month - 1, day,23,59,59);
			c.set(Calendar.MILLISECOND, 999);
		}
		else if(dateType.compareTo(DATE_TYPE_FROM)==0)
		{
			c.set(year, month -1, day, 0, 0,0);
			c.set(Calendar.MILLISECOND, 0);
		}
		return TimeUtil.localToUtc(c.getTimeInMillis());

	}
	
	private static long convertDateTimeInMilli(TimeZone tz, String date, String time, String dateType)
	{
		StringTokenizer st = new StringTokenizer(date,"-");
		int year = Integer.parseInt(st.nextToken());
	  int month = Integer.parseInt(st.nextToken());
		int day = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(time,":");
		int hour = Integer.parseInt(st.nextToken());
		int minute = Integer.parseInt(st.nextToken());
		
		Calendar c = Calendar.getInstance(tz);
		c.set(year, month-1,day, hour, minute);
		
		return TimeUtil.localToUtc(c.getTimeInMillis());
	}
	
	private static void addSearchDateCriteria(DataFilterImpl filter, String dateFrom, String dateTo, 
			Integer dateID,TimeZone userSelectTZ, boolean isPreviousFieldEmpty,
			String dateFromTime, String dateToTime)
	{
		//	fromDate, toDate format = yyyy-mm-dd
		/*
		long fromDateInMillisecond = convertTimeInMilli(userSelectTZ, dateFrom, _DATE_TYPE_FROM);
		
		long toDateInMillisecond = convertTimeInMilli(userSelectTZ, dateTo, _DATE_TYPE_TO);
		*/
		long fromDateInMillisecond = DateTimeHelper.convertDateTimeInUTC(dateFrom, dateFromTime,
				                                                             DATE_FORMAT,TIME_FORMAT,
				                                                             userSelectTZ);
		
		long toDateInMillisecond = DateTimeHelper.convertDateTimeInUTC(dateTo, dateToTime,
                                                                   DATE_FORMAT,TIME_FORMAT,
                                                                   userSelectTZ);
		
		
		FilterConnector connector = null;
		FilterConnector connector1 = null;
		if(!isPreviousFieldEmpty)
		{
			connector = filter.getAndConnector();
		}
		if(fromDateInMillisecond > 0)
		{
			filter.addSingleFilter(connector, dateID,
				                   filter.getGreaterOrEqualOperator(), new Timestamp(fromDateInMillisecond), false);
		}
		if(toDateInMillisecond > 0)
		{
			filter.addSingleFilter(filter.getAndConnector(), dateID,
				                   filter.getLessOperator(), new Timestamp(toDateInMillisecond), false);
		}
	}
	
	/**
	 * Return the def 00:00.000 for FromDate, 23:59:59.999 for ToDate if the given time is not specify by user.
	 * @param time
	 * @param dateType
	 * @return
	 */
	private static String getProcessTime(String time, String dateType)
	{
		if(time != null && ! "".equals(time))
		{
			return DATE_TYPE_TO.equals(dateType) ? time+":59.999" : time + ":00.000";
		}
		else if(DATE_TYPE_FROM.equals(dateType))
		{
			return "00:00:00.000";
		}
		else if(DATE_TYPE_TO.equals(dateType))
		{
			return "23:59:59.999";
		}
		else
		{
			throw new IllegalArgumentException("[DataFilter.getProcessTime] dateType "+ dateType+" is not supported.");
		}
	}
	
	private static String convertTimeInMilliToRNDate(long timeInMilliSecond)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeInMilliSecond);
		
		String year = c.get(Calendar.YEAR)+"";
		String month = c.get(Calendar.MONTH)+1 +"";
		String day = c.get(Calendar.DAY_OF_MONTH) + "";
		String hour = c.get(Calendar.HOUR_OF_DAY) + "";
		String minute = c.get(Calendar.MINUTE) + "";
		String second = c.get(Calendar.SECOND) + "";
		String millisecond = c.get(Calendar.MILLISECOND) + "";
		
		if(month.length() == 1)
		{
			month = "0"+month;
		}
		
		if(day.length() == 1)
		{
			day = "0"+day;
		}
		
		if(hour.length() == 1)
		{
			hour = "0"+hour;
		}
		
		if(minute.length() == 1)
		{
			minute = "0"+minute;
		}
		
		if(second.length() == 1)
		{
			second = "0"+second;
		}
		
		if(millisecond.length() == 1)
		{
			millisecond = "00"+millisecond;
		}
		else if(millisecond.length() == 2)
		{
			millisecond = "0"+millisecond;
		}
		
		StringBuilder rnDate = new StringBuilder();
		rnDate.append(year).append(month).append(day).append("T");
		rnDate.append(hour).append(minute).append(second).append(".");
		rnDate.append(millisecond).append("Z");
		return rnDate.toString();
	}
}
