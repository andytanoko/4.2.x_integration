/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateReportServlet.java
 *
 ****************************************************************************
 * Date           		    Author              Changes
 ****************************************************************************
 * Jan 17, 2007         Regina Zeng          Created
 * Mar 05, 2007			    Alain Ah Ming   	   Added error code to error logs
 * Mar 22, 2007			    Regina Zeng			     Modified createReport() to support 
 * 											                     emailList as NULL value 	
 * Mar 26, 2007			    Regina Zeng			     Added TimeZone	
 * Apr 19, 2007         Regina Zeng          Modified TimeZone
 */

package com.gridnode.gridtalk.genreport.receive;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gridtalk.genreport.ejb.IGenReportHandler;
import com.gridnode.gridtalk.genreport.ejb.IGenReportHandlerHome;
import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.generate.ReportManager;
import com.gridnode.gridtalk.genreport.model.Report;
import com.gridnode.gridtalk.genreport.util.AbstractServlet;
import com.gridnode.gridtalk.genreport.util.IInputConstants;
import com.gridnode.gridtalk.genreport.util.IJndiNames;
import com.gridnode.gridtalk.genreport.util.IReportConstants;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Regina Zeng
 * This servlet is used when the web.xml maps to <url-pattern>/create_report</url-pattern>.
 */
public class CreateReportServlet extends AbstractServlet
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 4260558457447003542L;
  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_RECEIVE, "CreateReportServlet"); 
  private Report _report = null;
  private static final String SENT = IReportConstants.CREATE_REPORT_PATH+IReportConstants.CONFIRM+"Report Sent";
  private static final String NOT_SEND = IReportConstants.CREATE_REPORT_PATH+IReportConstants.CONFIRM+"Report NOT Send";
  private static final String NOT_GEN = IReportConstants.CREATE_REPORT_PATH+IReportConstants.CONFIRM+"Report NOT Generated";
  private TimeZone _tz = TimeZone.getDefault();
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
//    try
//    {      
      createReport(request, response);
//    }
//    catch (IOException ex)
//    {
//      _logger.logError("doPost", null, ILogErrorCodes.CREATE_REPORT_SERVLET_POST, "Error while handling request", ex);
//    }
  }
  
  /**
   * This method takes in all mandatory fills from the UI and insert a new record into the database(report table).
   * @param request
   * @param response
   */
  private void createReport(HttpServletRequest request, HttpServletResponse response)
  {
  	String mn = "createReport";
  	
    String group = request.getParameter(IInputConstants.USER_GROUP);
    String username = request.getParameter(IInputConstants.USERNAME);   
    String timezone = request.getParameter(IInputConstants.TIMEZONE);
    String reportType = request.getParameter(IInputConstants.REPORT_TYPE);
    String reportingStartDateTime = request.getParameter(IInputConstants.REPORTING_START_DATE_TIME);
    String reportingEndDateTime = request.getParameter(IInputConstants.REPORTING_END_DATE_TIME);    
    String rf = request.getParameter(IInputConstants.REPORT_FORMAT);
    String[] customerList = (String[])request.getParameterValues(IInputConstants.CUSTOMER_LIST);
    String emailList = request.getParameter(IInputConstants.EMAIL_LIST);    
    String viewOnline = request.getParameter(IInputConstants.VIEW_ONLINE);
    String sendEmail = request.getParameter(IInputConstants.SEND_EMAIL);
    long curDate = System.currentTimeMillis();
    //Date currentDate = new Date(curDate);
    _logger.logMessage("createReport", null, group+reportType+reportingStartDateTime+reportingEndDateTime+rf+emailList+customerList[0]+sendEmail+viewOnline+username);
    String reportFormat = rf.toLowerCase().trim();
    DateFormat df = new SimpleDateFormat(IReportConstants.DATETIME_FORMAT);
    
    if(timezone != null && !"".equals(timezone))
    {
      _tz = TimeZone.getTimeZone(timezone.trim());      
    }   
    df.setTimeZone(_tz);
    String customer = getCustomer(customerList);
    Date startDate = null;
    Date endDate = null;
    Date currentDate = null;
    
    try
    {
      startDate = df.parse(reportingStartDateTime.trim());
      endDate = df.parse(reportingEndDateTime.trim());
      String tempCurrentDate = (String)df.format(new Date(curDate));
      currentDate = df.parse(tempCurrentDate);
    }
    catch(Exception e)
    {
      _logger.logWarn(mn, null, "Error while parsing DateFormat", e);
    }
            
    //user selected the "View Onine" option
    if(viewOnline!=null&&sendEmail==null) 
    {
      if(reportType!=null && startDate!=null && endDate!=null && group!=null && reportFormat!=null && customer!=null && username!=null)
      {
        //create a new report object
        _report = new Report(reportType, IReportConstants.FREQUENCY_ONCE, startDate, endDate, group, 
                             reportFormat, IReportConstants.REPORT_STATUS_VALID, customer, username, currentDate); 
      }
      else
      {
        _logger.logWarn(mn, null, "Unable to create a new report properly due to null input.", null);
      }
      try
      {
        viewReport(_report, response, _tz);
      }
      catch (Exception e)
      {
        _logger.logError(ILogErrorCodes.CREATE_REPORT_SERVLET_VIEW_ONLINE, mn, null, "Unable to create online report: "+e.getMessage(), e);
      }
    }
    //user selected the "Send Email" option
    else if(viewOnline==null&&sendEmail!=null) 
    {
      if(reportType!=null && startDate!=null && endDate!=null && group!=null && reportFormat!=null && emailList!=null && customer!=null && username!=null)
      {
        //create a new report object
        _report = new Report(reportType, IReportConstants.FREQUENCY_ONCE, startDate, endDate, group, 
                             reportFormat, emailList, IReportConstants.REPORT_STATUS_VALID, customer, username, currentDate); 
      }
      else
      {
        _logger.logWarn(mn, null, "Unable to create a new report properly due to null input.", null);
      }
      try
      {
        emailReport(_report, response, _tz);
      }
      catch (Exception e)
      {
        _logger.logError(ILogErrorCodes.CREATE_REPORT_SERVLET_SEND_EMAIL, mn, null, "Error while generating report to be emailed to recipient(s)", e);
      }
    }
    else
    {
      _logger.logWarn(mn, null, "Error while creating report.", null);
    }
  }
  
  private void emailReport(Report report, HttpServletResponse res, TimeZone tz) throws Exception
  {
    String mn = "emailReport";
    ReportManager rm = new ReportManager(tz);
    IGenReportHandlerHome home = lookup();
    IGenReportHandler genReport = home.create();
    try
    {
      String[] rpt = (String[])genReport.createReport(report);      
      if(rpt!=null)
      {
        //rpt[0]=reportNo, rpt[1]=template name, rpt[2]=datasource type
        boolean status = (boolean)rm.sendEmail(report, rpt[0], rpt[1], rpt[2]);
        if(status)
        {  
          _logger.logMessage(mn, null, "Report generated and sent via email.");
          String url = genReport.getValue(IReportConstants.CAT_1, IReportConstants.KEY_REPORTSERVICE);
          String forward = url+SENT;
          res.sendRedirect(forward);
        }
        else
        { 
          _logger.logMessage(mn, null, "Report NOT generated to be sent via email.");
          String url = genReport.getValue(IReportConstants.CAT_1, IReportConstants.KEY_REPORTSERVICE);
          String forward = url+NOT_SEND;
          res.sendRedirect(forward);
        }
      }
    }
    catch(Exception e)
    {    
      _logger.logError(ILogErrorCodes.CREATE_REPORT_SERVLET_SEND_EMAIL, mn, null, "Error in persisting data", e);
    }
  }
  
  private void viewReport(Report report, HttpServletResponse res, TimeZone tz) throws Exception
  {
  	String mn = "viewReport";
  	String forward = null;
    try
    {
      ReportManager rm = new ReportManager(tz);
      IGenReportHandlerHome home = lookup();
      IGenReportHandler genReport = home.create();     
      String[] rpt = (String[])genReport.createReport(report);
      if(rpt!=null)
      {      
        //rpt[0]=reportNo, rpt[1]=template name, rpt[2]=datasource type
        boolean status = rm.viewOnline(report, res, rpt[0], rpt[1], rpt[2]);
        if(status)
        {  
          _logger.logMessage(mn, null, "Report generated to be viewed online.");
        }
        else
        { 
          try
          {
            ServletOutputStream os = res.getOutputStream();
            res.setContentType("text/html");
            os.println("<html>");
            os.println("<head><title>" + report.getReportType() + "</title></head>");
            os.println("<body>");
            os.println("<p>Encounter internal error, unable to generate report.</p>");
            os.println("<a href=\"javascript:history.back()\">Back</a>");
            os.println("</body></html>");
            os.flush();
          }
          catch (IOException e)
          {
            _logger.logError(ILogErrorCodes.CREATE_REPORT_SERVLET_VIEW_ONLINE, mn, null, "IO Error. Unable to return error message in response: "+e.getMessage(), e);
          }
          _logger.logMessage(mn, null, "Report NOT generated to be viewed online.");
        }
      }
      else
      {
        String url = genReport.getValue(IReportConstants.CAT_1, IReportConstants.KEY_REPORTSERVICE);
        forward = url+NOT_GEN;
        res.sendRedirect(forward);
      }
    }
//		catch (NamingException e)
//		{
//			String message = "Unable to look up EJB";
//			_logger.logWarn(mn, null, message, e);
//			throw new Exception(message+": "+e.getMessage(), e);
//		}
//		catch (RemoteException e)
//		{
//			String message = "Unable to invoke EJB method";
//			_logger.logWarn(mn, null, message, e);
//			throw new Exception(message+": "+e.getMessage(), e);
//		}
//		catch (CreateException e)
//		{
//			String message = "Unable to invoke EJB create";
//			_logger.logWarn(mn, null, message, e);
//			throw new Exception(message+": "+e.getMessage(), e);
//		}
		catch (IOException e)
		{
			String message = "Unable to forward response to: "+forward;
      _logger.logWarn(mn, null, message, e);
      throw new Exception(message + ": "+e.getMessage(), e);
		}
    catch(Throwable t)
    {
    	String message = "Unexpected error: ";
      _logger.logWarn(mn, null, message+t.getMessage(), t);
      throw new Exception(message+t.getMessage());
    }
  }
  
  private IGenReportHandlerHome lookup() throws NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    return (IGenReportHandlerHome)finder.lookup(IJndiNames.GENREPORT_HANDLER, IGenReportHandlerHome.class);
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
}
