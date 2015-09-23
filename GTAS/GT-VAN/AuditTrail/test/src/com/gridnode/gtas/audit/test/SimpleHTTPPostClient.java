/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SimpleHTTPPostClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 1, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.test;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.gridnode.gtas.audit.archive.scheduler.ISchedulerConstant;
import com.gridnode.gtas.audit.reprocess.IReprocessServletConstant;

/**
 * This class is a test client for sending the http post to the reprocess,archive,archiveSchedule servlet.
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class SimpleHTTPPostClient
{
  public static void main(String[] args) throws Exception
  {
    
    String processDocRaw = getRawDataForReprocessDoc("842296f5-9137-4676-adb4-c2708e227446");
    
    //String processDocRaw = getRawDataForCancelProcess("7686");
    //System.out.println("process doc raw "+processDocRaw);
    String type = "application/x-www-form-urlencoded";
    //postRequest(processDocRaw, "http://localhost:8080/gridtalk/van/isat/reprocess", type);
    
    /* 
    //Test for archival
    String contentType = "application/x-www-form-urlencoded";
    postRequest(getSelect(), "http://localhost:8080/gridtalk/van/isat/archive", contentType);*/ 
    
    //Start test for archive scheduler
    String addSchedule = getRawDataForAddSchedule();
    //String addSchedule = getRawDataForUpdateSchedule();
    //String addSchedule = getRawDataForRunSchedule();
    System.out.println(addSchedule);
   postRequest(addSchedule, "http://192.168.213.240:8080/gridtalk/van/isat/archiveSchedule", type);
    
    //America/Santiago GTM - 4
    //Asia/Baghdad GMT+3
    //Asia/Bangkok GMT + 7
    /*
    String[] tzs = TimeZone.getAvailableIDs();
    for(String s : tzs)
    {
      System.out.println("timezone is "+s);
    }*/
  }
  
  //Create archive schedule
  public static String getRawDataForAddSchedule()
  {
    return ISchedulerConstant.SCHEDULE_ACT+"="+ISchedulerConstant.SCHEDULE_ACT_CREATE+"&"+
           ISchedulerConstant.FREQUENCY+"="+"daily"+"&"+
           ISchedulerConstant.ARCHIVE_EVERY_NTH+"="+"1"+"&"+
           ISchedulerConstant.EFFECTIVE_START_DATE+"="+"2007-06-17"+"&"+
           ISchedulerConstant.EFFECTIVE_START_TIME+"="+"16:01"+"&"+
           ISchedulerConstant.IS_ENABLED+"="+"1"+"&"+
           ISchedulerConstant.CUSTOMER_LIST+"="+"Customer1"+"&"+
           ISchedulerConstant.ARCHIVE_OLDER_THAN+"="+"320"+"&"+
           ISchedulerConstant.USER_TIME_ZONE+"="+"Asia/Singapore";
           
  }
  
  //update existing schedule
  public static String getRawDataForUpdateSchedule()
  {
    return ISchedulerConstant.SCHEDULE_ACT+"="+ISchedulerConstant.SCHEDULE_ACT_UPDATE+"&"+
      ISchedulerConstant.FREQUENCY+"="+"monthly"+"&"+
      ISchedulerConstant.ARCHIVE_EVERY_NTH+"="+"1"+"&"+
      ISchedulerConstant.EFFECTIVE_START_DATE+"="+"2005-04-10"+"&"+
      ISchedulerConstant.EFFECTIVE_START_TIME+"="+"12:43"+"&"+
      ISchedulerConstant.IS_ENABLED+"="+"1"+"&"+
      ISchedulerConstant.CUSTOMER_LIST+"="+"GT"+"&"+
      ISchedulerConstant.ARCHIVE_OLDER_THAN+"="+"10"+"&"+
      ISchedulerConstant.USER_TIME_ZONE+"=America/Santiago&"+
      ISchedulerConstant.ARCHIVE_SCHEDULE_UID+"=eec39652-fbef-4d1a-8581-379a4a7354d2";
  }
  
  //Delete the Schedule
  public static String getRawDataForDeleteSchedule()
  {
    return ISchedulerConstant.SCHEDULE_ACT+"="+ISchedulerConstant.SCHEDULE_ACT_DELETE+"&"+
      ISchedulerConstant.ARCHIVE_SCHEDULE_UID+"="+"e3f5af3c-3b37-4f07-90b6-dfca98e0136b"+"&";
  }
  
  //Run schedule immediately
  public static String getRawDataForRunSchedule()
  {
    return ISchedulerConstant.SCHEDULE_ACT+"="+ISchedulerConstant.SCHEDULE_ACT_ARCHIVE_NOW+"&"+
    ISchedulerConstant.ARCHIVE_SCHEDULE_UID+"="+"eeb382b8-98f6-43e4-ab73-698e09aba9c0"+"&";
  }
  
  public static String getRawDataForReprocessDoc(String tracingID)
  {
    return IReprocessServletConstant.ACTIVITY_TYPE+"="+IReprocessServletConstant.ACTIVITY_REPROCESS_ACTIVITY_TRACE_EVENTS+"&"+
           IReprocessServletConstant.TRACING_ID+"="+tracingID+"&"+IReprocessServletConstant.USER_NAME+"=anonymous";
  }
  
  public static String getRawDataForCancelProcess(String processUID)
  {
    return IReprocessServletConstant.ACTIVITY_TYPE+"="+IReprocessServletConstant.ACTIVITY_CANCEL_PROCESS+"&"+
           IReprocessServletConstant.PROCESS_UID+"="+processUID+"&"+IReprocessServletConstant.USER_NAME+"=anonymous";
  }
  
  public static String getSelect() throws Exception
  {
    /*
    StringBuilder builder = new StringBuilder();
    builder.append("<form>")
    builder.append("<Select name=\"SummaryInfo\">");
    builder.append("<option value=\"12345\" SELECTED></option>");
    builder.append("<option value=\"value\" SELECTED></option>");
    
    builder.append("<option value=\"12345\" SELECTED></option>");
    builder.append("<option value=\"value\" SELECTED></option>");
    builder.append("</select>");
    
    return builder.toString(); */
    String data = "summaryFilename=value1,test,123";
    data += "&" + "key2=value2";
    data += "&" +"archiveAct=listSummaryFile";
    return data;
  }
  
  public static void postRequest(String rawData, String url, String type) throws Exception
  {
    System.out.println("Posting data "+rawData);
    
    String agent = "Mozilla/4.0";
    
    
    HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty( "User-Agent", agent );
    conn.setRequestProperty( "Content-Type", type );
    conn.setRequestProperty( "Content-Length", 
                             rawData.length()+"" );
    conn.setDoOutput(true);
    conn.setDoInput(true);
    conn.connect();
    
    OutputStream out = conn.getOutputStream();
    out.write(rawData.getBytes("UTF-8"));
    
    InputStream in = new BufferedInputStream(conn.getInputStream(), 32 * 1024);
    int character = in.read();
    while (character != -1) {
        System.out.print((char) character);
        character = in.read();
    }
  }
}
