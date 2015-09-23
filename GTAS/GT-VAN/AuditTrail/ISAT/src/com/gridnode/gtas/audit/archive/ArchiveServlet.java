/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 16, 2007    Tam Wei Xiang       Created
 * May 25, 2007    Tam Wei Xiang       Support archive by customer
 */
package com.gridnode.gtas.audit.archive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.TimeZone;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveServiceManagerHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveServiceManagerObj;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerObj;
import com.gridnode.gtas.audit.archive.helper.ArchiveActivityHelper;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveServlet extends HttpServlet
{
  private static final String CLASS_NAME = "ArchiveServlet";
  private Logger _logger;
  
  private String DATE_PATTERN = "yyyy-MM-dd HH:mm";
  
  public void init() throws ServletException
  {
    _logger = getLogger();
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String methodName = "doGet";
    doPost(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  { 
    String methodName = "doPost";
    String activity = request.getParameter(IArchiveConstant.ARCHIVE_ACTIVITY);
    
    Hashtable criteria = criteria = new Hashtable();
    if(activity != null)
    {
      criteria.put(IArchiveConstant.ARCHIVE_ACTIVITY, activity);
    }
    
    if(IArchiveConstant.ARCHIVE_ACT_ARCHIVE.equals(activity))
    {
      assertRequestParam(request);
      String archiveDescription = request.getParameter(IArchiveConstant.ARCHIVE_DESCRIPTION);
      String archiveName = request.getParameter(IArchiveConstant.ARCHIVE_NAME);
      String fromStartDate = request.getParameter(IArchiveConstant.FROM_START_DATE);
      String fromStartTime = request.getParameter(IArchiveConstant.FROM_START_TIME);
      String toStartDate = request.getParameter(IArchiveConstant.TO_START_DATE);
      String toStartTime = request.getParameter(IArchiveConstant.TO_START_TIME);
      String userTZ = request.getParameter(IArchiveConstant.USER_TIMEZONE);
      String[] customerList = request.getParameterValues(IArchiveConstant.GROUP_INFO);
      String isArchiveOrphanRecord = request.getParameter(IArchiveConstant.ARCHIVE_ORPHAN_RECORD);
      
      _logger.logMessage(methodName, null, "archiveDescription :"+archiveDescription+" archiveName: "+archiveName+" fromStartDate: "+fromStartDate+
                       " fromStartTime: "+fromStartTime+" toStartDate: "+toStartDate+" toStartTime: "+toStartTime+" userTZ: "+userTZ+
                       " customerList: "+customerList+" isArchiveOrphanRecord "+isArchiveOrphanRecord);
      
      long fromStartDateTime = 0;
      long toStartDateTime = 0;
      try
      {
        fromStartDateTime = getDateTime(fromStartDate, fromStartTime, userTZ);
        toStartDateTime = getDateTime(toStartDate, toStartTime, userTZ);
      }
      catch(Exception ex)
      {
        _logger.logWarn(methodName, null, "Error in converting DATE TIME", ex);
        return;
      }
      
      criteria.put(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME, fromStartDateTime);
      criteria.put(IArchiveConstant.CRITERIA_TO_START_DATE_TIME, toStartDateTime);
      criteria.put(IArchiveConstant.ARCHIVE_NAME, archiveName);
      
      if(archiveDescription != null)
      {
        criteria.put(IArchiveConstant.ARCHIVE_DESCRIPTION, archiveDescription);
      }
      
      if(customerList != null)
      {
        String groups = ArchiveActivityHelper.convertArrToStr(customerList, ";");
        criteria.put(IArchiveConstant.GROUP_INFO, groups);
      }
      
      boolean convertIsArchiveOrphanRecord = false;
      if(isArchiveOrphanRecord != null)
      {
        int orphanEnable = Integer.parseInt(isArchiveOrphanRecord);
        convertIsArchiveOrphanRecord = orphanEnable == 0 ? false : true;
      }
      
      criteria.put(IArchiveConstant.ARCHIVE_ORPHAN_RECORD, convertIsArchiveOrphanRecord);
      
      delegateRequestToQ(criteria, activity, response);
    }
    else if(IArchiveConstant.ARCHIVE_ACT_RESTORE.equals(activity))
    {
      String summaryFilename = request.getParameter(IArchiveConstant.RESTORE_SUMMARY_FILE);
      _logger.logMessage(methodName, null, "summaryFilename: "+summaryFilename);
      criteria.put(IArchiveConstant.RESTORE_SUMMARY_FILE, summaryFilename);
      delegateRequestToQ(criteria, activity, response);
    }
    else if(IArchiveConstant.ARCHIVE_ACT_DOWNLOAD_SUMMARY.equals(activity))
    {
      String summaryFilename = request.getParameter(IArchiveConstant.SUMMARY_FILENAME);
      try
      {
        sendSummaryFile(summaryFilename, response);
      }
      catch(Exception ex)
      {
        _logger.logError(ILogErrorCodes.AT_GENERIC_HTTP_RESPONSE_POSTING_ERROR,
                         methodName, null , "Failed to send the archive summary file with filename "+summaryFilename+". Error: "+ex.getMessage(), ex);
      }
    }
    else
    {
      _logger.logMessage(methodName, null, "Archive activity "+activity+" is not supported !");
      return;
    }
    
  }
  
  private void delegateRequestToQ(Hashtable criteria, String activity, HttpServletResponse response)
  {
    String methodName = "delegateRequestToQ";
    boolean isFail = false;
    try
    { 
      if(IArchiveConstant.ARCHIVE_ACT_RESTORE.equals(activity))
      {
        sendToQueue(criteria);
      }
      else
      {
        getArchiveServiceManager().initArchive(criteria);
      }
    }
    catch(Exception ex)
    {
      isFail = true;
      _logger.logError(ILogErrorCodes.AT_GENERIC_HTTP_RESPONSE_POSTING_ERROR,
                       methodName, null, "Failed to send archive criteria to Transaction Monitoring. Activity type ["+activity+"]. Error: "+ex.getMessage(), ex);
    }
    finally
    {
      try
      {
        writeResponseToClient(response, activity, isFail? 0 : 1);
      }
      catch(Exception ex)
      {
        _logger.logWarn(methodName, null, "Error in writting the "+activity+" status response to client", ex);
      }
    }
  }
  
  /**
   * Send back the archive/restore request status to the client. The status just inform client that the archive/restore 
   * request has successfully send to the handler. But it can't guarantee the archive or restore is successfully executed.
   * @param response
   * @param activity the activity. the archive or restore activity
   * @param statusCode 0 if the requeset fail to send to the handler via JMS. 1 if the request sucessfully delivered to the Q.
   * @throws Exception
   */
  private void writeResponseToClient(HttpServletResponse response, String activity, int statusCode) throws Exception
  {
    IAuditTrailArchiveManagerObj archiveMgr = getArchiveMgr();
    String archiveStatusReplyURL = archiveMgr.getArchiveStatusForwardUrl();
    if(archiveStatusReplyURL == null)
    {
      throw new NullPointerException("The url for sending the archive/restore status to is null. Pls initilize it in DB.");
    }
    
    String responseURL = archiveStatusReplyURL+"?"+IArchiveConstant.ARCHIVE_ACT+"="+activity+"&"+IArchiveConstant.ARCHIVE_ACT_STATUS+"="+statusCode;
    _logger.logMessage("", null, "Responsed URL is "+responseURL);
    response.sendRedirect(responseURL);
  }
  
  private void sendSummaryFile(String summaryFilename, HttpServletResponse response) throws Exception
  {
  	String mn = "sendSummaryFile";
    ServletOutputStream op = null;
    ByteArrayInputStream input = null;
    try
    {
      IAuditTrailArchiveManagerObj archiveMgr = getArchiveMgr();
      byte[] archiveSummary = archiveMgr.getTMSummaryFileContent(summaryFilename);
      op = response.getOutputStream();

      response.setContentType( "application/octet-stream");
      response.setContentLength(archiveSummary.length);
      response.setHeader( "Content-Disposition", "attachement; filename=\"" + summaryFilename + "\"" );

      //
      //  Stream to the requester.
      //
      byte[] buffer = new byte[512];
      input = new ByteArrayInputStream(archiveSummary);
      int readSoFar = 0;
      while( (readSoFar = input.read(buffer)) != -1)
      {
          op.write(buffer,0,readSoFar);
      }
    }
    catch(IOException e)
    {
    	_logger.logWarn(mn, null, "Unable to write to response output stream. Connection might be closed", e);
    }
		catch (NamingException e)
		{
			throw new Exception("Failed to look up archive manager", e);
		}
		catch (CreateException e)
		{
			throw new Exception("Failed to create archive manager", e);
		}
    
    catch(Exception ex)
    {
    	_logger.logWarn(mn, null, "Failed to send summary file: "+summaryFilename, ex);
      throw ex;
    }
    finally
    {
      if(input != null)
      {
        try
				{
					input.close();
				}
				catch (IOException e)
				{
					_logger.logWarn(mn, null, "Unable to close input stream for summary file: "+summaryFilename, e);
				}
      }
    }
  }
  
  private void sendToQueue(Hashtable<String,String> map) throws Exception
  {
    JmsSender sender = new JmsSender();
    sender.setSendProperties(getJMSSenderProperties());
    sender.send(map);
  }
  
  private Properties getJMSSenderProperties() throws Exception
  {
    IAuditPropertiesManagerObj propertiesMgr = getPropertiesMgr();
    Properties pro = propertiesMgr.getAuditProperties(IISATProperty.ARCHIVE_JMS_CATEGORY);
    return pro;
    /*
    Properties pro = new Properties();
    pro.setProperty(JmsSender.DESTINATION, IISATConstant.ARCHIVE_TRAIL_DATA_JNDI);
    pro.setProperty(JmsSender.CONN_FACTORY, IISATConstant.CONNECTION_FACTORY_JNDI);
    return pro;*/
  }
  
  private IAuditPropertiesManagerObj getPropertiesMgr() throws RemoteException, CreateException, NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditPropertiesManagerHome home = (IAuditPropertiesManagerHome)finder.lookup(IAuditPropertiesManagerHome.class.getName(),
                                                                                  IAuditPropertiesManagerHome.class);
    return home.create();
  }
  
  /**
   * 
   * @return
   * @throws NamingException
   * @throws RemoteException
   * @throws CreateException
   */
  private IAuditTrailArchiveManagerObj getArchiveMgr() throws NamingException, RemoteException, CreateException
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditTrailArchiveManagerHome home = (IAuditTrailArchiveManagerHome)finder.lookup(IAuditTrailArchiveManagerHome.class.getName(),
                                                                                      IAuditTrailArchiveManagerHome.class);
    return home.create();
  }
  
  private IArchiveServiceManagerObj getArchiveServiceManager() throws Exception
  {
    JndiFinder finder;
    try
    {
      finder = new JndiFinder(null);
    }
    catch(Exception ex)
    {
      throw new Exception("Failed to initialize jndi finder.", ex);
    }
    IArchiveServiceManagerHome home = (IArchiveServiceManagerHome)finder.lookup(
                                                IArchiveServiceManagerHome.class.getName(), IArchiveServiceManagerHome.class);
    
    try
    {
      return home.create();
    }
    catch(Exception ex)
    {
      throw new Exception("Failed to create ArchiveServiceManager", ex);
    }
  }
  
  private void assertRequestParam(HttpServletRequest request)
  {
    //String archiveDescription = request.getParameter(IArchiveConstant.ARCHIVE_DESCRIPTION);
    String archiveName = request.getParameter(IArchiveConstant.ARCHIVE_NAME);
    String fromStartDate = request.getParameter(IArchiveConstant.FROM_START_DATE);
    String fromStartTime = request.getParameter(IArchiveConstant.FROM_START_TIME);
    String toStartDate = request.getParameter(IArchiveConstant.TO_START_DATE);
    String toStartTime = request.getParameter(IArchiveConstant.TO_START_TIME);
    
    //assertParam(archiveDescription, IArchiveConstant.ARCHIVE_DESCRIPTION);
    assertParam(archiveName, IArchiveConstant.ARCHIVE_NAME);
    assertParam(fromStartDate, IArchiveConstant.FROM_START_DATE);
    assertParam(fromStartTime, IArchiveConstant.FROM_START_TIME);
    assertParam(toStartDate, IArchiveConstant.TO_START_DATE);
    assertParam(toStartTime, IArchiveConstant.TO_START_TIME);
  }
  
  private void assertParam(String paramValue, String paramName)
  {
    if(paramValue == null || "".equals(paramValue))
    {
      _logger.logMessage("assertParam", null, "Expecting the requested param "+paramName+" to contain value !");
      throw new IllegalArgumentException("Some param value is missing !");
    }
  }
  
  private long getDateTime(String dateInStr, String timeInStr, String userTimezone) throws Exception
  {
    String dateTime = dateInStr +" "+ timeInStr;
    
    Date date = getDate(dateTime, DATE_PATTERN, userTimezone);
    _logger.logMessage("getDateTime", null, "Converted date time is "+date);
    
    return date.getTime();
  }
  
  /**
   * Convert the date from the userTimzone to the system timezone
   * @param date
   * @param datePattern
   * @param userTimezone
   * @return
   * @throws Exception
   */
  private Date getDate(String date, String datePattern, String userTimezone) throws Exception
  {
    SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
    
    TimeZone tz = null;
    if(userTimezone == null)
    {
      tz = TimeZone.getDefault();
    }
    else
    {
      tz = TimeZone.getTimeZone(userTimezone);
    }
    formatter.setTimeZone(tz);
    return formatter.parse(date);
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  public static void main(String[] args) throws Exception
  {
    ArchiveServlet ser = new ArchiveServlet();
    String date = "2007-05-17";
    String time = "11:20";
    String timezone = "GMT+10";
    
    long converted = ser.getDateTime(date, time, timezone);
    System.out.println("Converted t is "+new Date(converted));
    /*
    ser.getDateTime("2007-10-01", "23:30", "GMT+1:00");
    
    Date d = ser.getDate("2007-10-01", "yyyy-MM-dd", "GMT+1:00");
    Date time = ser.getDate("23:30", "HH:mm", "GMT+1:00");
    
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    
    System.out.println("date "+c.getTime());
    
    c = Calendar.getInstance();
    c.setTime(time);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    
    System.out.println("time "+c.getTime()); */
    
    /*
    c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, minute);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);*/
  }
}
