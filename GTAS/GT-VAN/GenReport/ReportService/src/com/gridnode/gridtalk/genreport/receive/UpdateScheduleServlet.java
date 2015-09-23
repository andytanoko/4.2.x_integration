/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateScheduleServlet.java
 *
 ****************************************************************************
 * Date           		Author              	Changes
 ****************************************************************************
 * Jan 17, 2007     Regina Zeng            	Created
 * Mar 05, 2007			Alain Ah Ming			      Added error code to error logs
 * Mar 26, 2007			Regina Zeng				      Added TimeZone
 * Apr 19, 2007     Regina Zeng             Modified TimeZone
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
import com.gridnode.gridtalk.genreport.util.AbstractServlet;
import com.gridnode.gridtalk.genreport.util.IInputConstants;
import com.gridnode.gridtalk.genreport.util.IJndiNames;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.gridtalk.genreport.util.IReportConstants;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Regina Zeng
 * This servlet is used when the web.xml maps to <url-pattern>/update_schedule</url-pattern>.
 */
public class UpdateScheduleServlet extends AbstractServlet
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -954950187877296229L;
  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_RECEIVE, "UpdateScheduleServlet");
  private static final String SUC = IReportConstants.CONFIRM+"Schedule Updated Successfully";
  private static final String UNSUC = IReportConstants.CONFIRM+"Schedule NOT Updated";
  private static final DateFormat _df = new SimpleDateFormat(IReportConstants.DATETIME_FORMAT);
  private TimeZone _tz = TimeZone.getDefault();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      updateSchedule(request, response);
    }
    catch (IOException ex)
    {
      _logger.logError(ILogErrorCodes.UPDATE_SCHEDULE_SERVLET_POST,"doPost", null,  "IO Error. Unable to handle request", ex);
    }
		catch (NamingException e)
		{
      _logger.logError(ILogErrorCodes.UPDATE_SCHEDULE_SERVLET_POST, "doPost", null, "JNDI Error. Unable to handle request", e);
		}
		catch (CreateException e)
		{
      _logger.logError(ILogErrorCodes.UPDATE_SCHEDULE_SERVLET_POST, "doPost", null, "EJB Creation Error. Unable to handle request", e);
		}
  }
  
  /**
   * This method takes in all mandatory fills from the UI and update record into the database(schedule table).
   * @param request
   * @param response
   * @throws IOException 
   * @throws CreateException 
   * @throws NamingException 
   */
  private void updateSchedule (HttpServletRequest request, HttpServletResponse response) throws NamingException, CreateException, IOException
  {
    String mn = "updateSchedule";
    
    String username = request.getParameter(IInputConstants.USERNAME);
    String group = request.getParameter(IInputConstants.USER_GROUP);
    String timezone = request.getParameter(IInputConstants.TIMEZONE);
    String rf = request.getParameter(IInputConstants.REPORT_FORMAT);
    String[] customerList = (String[])request.getParameterValues(IInputConstants.CUSTOMER_LIST);
    String emailList = request.getParameter(IInputConstants.EMAIL_LIST);
    String schedId = request.getParameter(IInputConstants.SCHEDULE_ID);
    int scheduleId = Integer.parseInt(schedId.trim());
    String reportFormat = rf.toLowerCase().trim();
    String customer = getCustomer(customerList);   
    long curDate = System.currentTimeMillis();
    //Date currentDate = new Date(curDate);
    _logger.logMessage(mn, null, username+group+rf+emailList+scheduleId+reportFormat+customer);
    
    if(timezone != null && !"".equals(timezone))
    {
      _tz = TimeZone.getTimeZone(timezone.trim());      
    }
    _df.setTimeZone(_tz);
    Date currentDate = null;
    try
    {
      String tempCurrentDate = (String)_df.format(new Date(curDate));
      currentDate = _df.parse(tempCurrentDate);
    }
    catch (ParseException e)
    {
      _logger.logWarn(mn, null, "Error while parsing DateFormat", e);
    }
    
//    try
//    {
      updateScheduleReport(scheduleId, reportFormat, customer, emailList, username, group, currentDate, request, response);
//    }
//    catch(Exception e)
//    {
//      _logger.logError("updateSchedule", null, "Error while updating schedule report.", e);
//    }
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
  
  private void updateScheduleReport(int scheduleId, String reportFormat, String customerList, String emailList, String username, String group, Date currentDate, HttpServletRequest req, HttpServletResponse res) throws NamingException, CreateException, IOException
  {
    IGenScheduleHandlerHome home = lookup();
    IGenScheduleHandler genSchedule = home.create();
     
    boolean status = (boolean)genSchedule.updateSchedule(scheduleId, reportFormat, customerList, emailList, username, group, currentDate);
    if(status)
    {   
      String url = genSchedule.getValue(IReportConstants.CAT_1, IReportConstants.KEY_REPORTSERVICE);
      String forward = url+IReportConstants.UPDATE_SCHEDULE_PATH+scheduleId+SUC;    
      res.sendRedirect(forward);
    }
    else
    {
      String url = genSchedule.getValue(IReportConstants.CAT_1, IReportConstants.KEY_REPORTSERVICE);
      String forward = url+IReportConstants.UPDATE_SCHEDULE_PATH+scheduleId+UNSUC;   
      res.sendRedirect(forward);
    }
  }
  
  private IGenScheduleHandlerHome lookup() throws NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    return (IGenScheduleHandlerHome)finder.lookup(IJndiNames.GENSCHEDULE_HANDLER, IGenScheduleHandlerHome.class);
  }
}