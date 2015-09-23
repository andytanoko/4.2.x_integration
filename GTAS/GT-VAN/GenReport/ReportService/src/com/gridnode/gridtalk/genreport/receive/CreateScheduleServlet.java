/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateScheduleServlet.java
 *
 ****************************************************************************
 * Date            		   Author              Changes
 ****************************************************************************
 * Jan 17, 2007         Regina Zeng          Created
 * Mar 05, 2007			    Alain Ah Ming		     Added error code to error logs
 * Mar 26, 2007			    Regina Zeng			     Added TimeZone
 * Apr 19, 2007         Regina Zeng          Modified TimeZone* 
 * Aug 13, 2007         Tam Wei Xiang        Modified the way we convert the user time to
 *                                           server time.
 */

package com.gridnode.gridtalk.genreport.receive;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gridtalk.genreport.ejb.IGenScheduleHandler;
import com.gridnode.gridtalk.genreport.ejb.IGenScheduleHandlerHome;
import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.model.Schedule;
import com.gridnode.gridtalk.genreport.util.AbstractServlet;
import com.gridnode.gridtalk.genreport.util.DateUtil;
import com.gridnode.gridtalk.genreport.util.IInputConstants;
import com.gridnode.gridtalk.genreport.util.IJndiNames;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.gridtalk.genreport.util.IReportConstants;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Regina Zeng
 * This servlet is used when the web.xml maps to <url-pattern>/create_schedule</url-pattern>.
 */
public class CreateScheduleServlet extends AbstractServlet
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -5870535565248571709L;
  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_RECEIVE, "CreateScheduleServlet");
  private static final String SUC = IReportConstants.CONFIRM+"Schedule Created Successfully";
  private static final String UNSUC = IReportConstants.CONFIRM+"Schedule NOT Successfully";
  private TimeZone _tz = TimeZone.getDefault();
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      createSchedule(request, response);
    }
    catch (IOException ex)
    {
      _logger.logError(ILogErrorCodes.CREATE_SCHEDULE_SERVLET_POST, "doPost", null, "IO Error. Unable to handle request", ex);
    }
		catch (NamingException e)
		{
      _logger.logError(ILogErrorCodes.CREATE_SCHEDULE_SERVLET_POST, "doPost", null, "JNDI Error. Unable to handle request", e);
		}
		catch (CreateException e)
		{
      _logger.logError(ILogErrorCodes.CREATE_SCHEDULE_SERVLET_POST, "doPost", null, "EJB Creation Error. Unable to handle request", e);
		}
  }
  
  /**
   * This method takes in all mandatory fills from the UI and insert a new record into the database(schedule table).
   * @param request
   * @param response
   * @throws IOException
   * @throws CreateException 
   * @throws NamingException 
   */
  private void createSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException, NamingException, CreateException
  {
    String mn = "createSchedule";
    
    String username =request.getParameter(IInputConstants.USERNAME);
    String group = request.getParameter(IInputConstants.USER_GROUP);
    String timezone = request.getParameter(IInputConstants.TIMEZONE);
    String reportType = request.getParameter(IInputConstants.REPORT_TYPE);
    String frequency = request.getParameter(IInputConstants.FREQUENCY);
    String effectiveStartDate = request.getParameter(IInputConstants.EFFECTIVE_START_DATE);
    String reportRunTime = request.getParameter(IInputConstants.REPORT_RUN_TIME);    
    String rf = request.getParameter(IInputConstants.REPORT_FORMAT);
    String[] customerList = (String[])request.getParameterValues(IInputConstants.CUSTOMER_LIST);
    String emailList = request.getParameter(IInputConstants.EMAIL_LIST);    
    long curDate = System.currentTimeMillis();
    //Date currentDate = new Date(curDate);
    _logger.logMessage(mn, null, group+reportType+frequency+effectiveStartDate+reportRunTime+rf+emailList+customerList[0]);
     String customer = getCustomer(customerList);
    String reportFormat = rf.toLowerCase().trim();
    
    DateFormat df = new SimpleDateFormat(IReportConstants.DATE_FORMAT);
    DateFormat dtf = new SimpleDateFormat(IReportConstants.DATETIME_FORMAT);
    DateFormat tf = new SimpleDateFormat(IReportConstants.TIME_FORMAT);
    
    if(timezone != null && !"".equals(timezone))
    {
      _tz = TimeZone.getTimeZone(timezone.trim());      
    }
    df.setTimeZone(_tz);
    dtf.setTimeZone(_tz);
    tf.setTimeZone(_tz);
    Date ntRunDateTime = null;
    Date effStartDate = null;
    Date timeInDate = null;
    Date nrdt = null;
    Date nsdt = null;
    Date nedt = null;
    Date currentDate = null;
    String runTime = null;
    
    try
    {
      effStartDate = df.parse(effectiveStartDate);
      ntRunDateTime = dtf.parse(effectiveStartDate.trim()+" "+reportRunTime.trim());
      timeInDate = tf.parse(reportRunTime.trim());
      runTime = DateUtil.convertTimeToServerTime(reportRunTime.trim(), _tz); //TWX 13082007
      String tempCurrentDate = (String)dtf.format(new Date(curDate));
      currentDate = dtf.parse(tempCurrentDate);
    }
    catch (ParseException e)
    {
      _logger.logWarn(mn, null, "Error while parsing DateFormat", e);
    }
    Date[] nr = (Date[])DateUtil.nextRunDateTime(ntRunDateTime, frequency); 
    if(nr!=null)
    {
      nrdt = nr[0];
      nsdt = nr[1];
      nedt = nr[2];
    }
    
    if(effStartDate!=null && runTime!=null && nrdt!=null && group!=null && nrdt!=null && nsdt!=null && nedt!=null && username!=null)
    {
      Schedule schedule = new Schedule(reportType, frequency, effStartDate, runTime, reportFormat, group, emailList, customer, username, currentDate);
      schedule.setNextRunDateTime(nrdt);
      schedule.setNextStartDateTime(nsdt);
      schedule.setNextEndDateTime(nedt);
      createScheduleReport(schedule, request, response);
    }    
  }
  
  private String getCustomer(String[] customerList)
  {
    String customer = "";
    for(int i=0; i<customerList.length; i++)
    {
      if(i!=customerList.length-1)
      {
        customer = customer + customerList[i] + ",";
      }
      else
      {
        customer = customer + customerList[i];
      }
    }
    return customer;
  }
  
  private void createScheduleReport(Schedule schedule, HttpServletRequest request, HttpServletResponse response) throws NamingException, CreateException, IOException
  {
    String mn = "createScheduleReport";
    IGenScheduleHandlerHome home = lookup();
    IGenScheduleHandler genSchedule = home.create();
    
//    try
//    {
      String scheduleNo = (String)genSchedule.createSchedule(schedule);
      if(scheduleNo!=null)
      {
        _logger.logMessage(mn, null, "Successfully created a schedule with scheduleId "+scheduleNo+
                           ".\nThe next run date time will be "+schedule.getNextRunDateTime());
        
        String url = genSchedule.getValue(IReportConstants.CAT_1, IReportConstants.KEY_REPORTSERVICE);
        String forward = url+IReportConstants.CREATE_SCHEDULE_PATH+SUC; 
        response.sendRedirect(forward);
      }
      else
      {
        String url = genSchedule.getValue(IReportConstants.CAT_1, IReportConstants.KEY_REPORTSERVICE);
        String forward = url+IReportConstants.CREATE_SCHEDULE_PATH+UNSUC;     
        response.sendRedirect(forward);
      }
//    }
//    catch(Exception e)
//    {
//      _logger.logError("createSchedule", null, "Error in persisting data.", e);
//    }
  }
  
  private IGenScheduleHandlerHome lookup() throws NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    return (IGenScheduleHandlerHome)finder.lookup(IJndiNames.GENSCHEDULE_HANDLER, IGenScheduleHandlerHome.class);
  }
}
