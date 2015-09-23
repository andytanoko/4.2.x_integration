/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GenerateReportServlet.java
 *
 ****************************************************************************
 * Date                 Author              Changes
 ****************************************************************************
 * Feb 14, 2007        Regina Zeng          Created
 * Mar 05, 2007		   Alain Ah Ming		Added error code to error logs
 * Apr 19, 2007        Regina Zeng          Modified TimeZone
 * Jun 08, 2007		   Regina Zeng			To persist generate_report into db 
 */

package com.gridnode.gridtalk.genreport.receive;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gridtalk.genreport.ejb.IGenScheduleHandler;
import com.gridnode.gridtalk.genreport.ejb.IGenScheduleHandlerHome;
import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.generate.ReportManager;
import com.gridnode.gridtalk.genreport.model.Schedule;
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
 * This servlet is used when the web.xml maps to <url-pattern>/generate_report</url-pattern>.
 */
public class GenerateReportServlet extends AbstractServlet
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -1441021698854337214L;
  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_RECEIVE, "GenerateReportServlet");
  private TimeZone _tz = TimeZone.getDefault();
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {      
      generateReport(request, response);
    }
    catch (IOException ex)
    {
      _logger.logError(ILogErrorCodes.GENERATE_REPORT_SERVLET_POST,"doPost", null,  "IO Error. Unable to handle request", ex);
    }
		catch (NamingException e)
		{
      _logger.logError(ILogErrorCodes.GENERATE_REPORT_SERVLET_POST, "doPost", null, "JNDI Error. Unable to handle request", e);
		}
		catch (CreateException e)
		{
      _logger.logError(ILogErrorCodes.GENERATE_REPORT_SERVLET_POST, "doPost", null, "EJB Creation Error. Unable to handle request", e);
		}
  }
  
  private void generateReport(HttpServletRequest request, HttpServletResponse response) throws IOException, NamingException, CreateException
  {
    String mn = "generateReport";
    
    String group = request.getParameter(IInputConstants.USER_GROUP);
    String username = request.getParameter(IInputConstants.USERNAME); 
    String scheduleId = request.getParameter(IInputConstants.SCHEDULE_ID);
    String timezone = request.getParameter(IInputConstants.TIMEZONE);
    long curDate = System.currentTimeMillis();
    int id = Integer.parseInt(scheduleId.trim());
    DateFormat df = new SimpleDateFormat(IReportConstants.DATETIME_FORMAT);

    if(timezone != null && !"".equals(timezone))
    {
      _tz = TimeZone.getTimeZone(timezone.trim());      
    } 
    
    df.setTimeZone(_tz);
    Date currentDate = null;
    try
    {
      String tempCurrentDate = (String)df.format(new Date(curDate));
      currentDate = df.parse(tempCurrentDate);
    }
    catch(Exception e)
    {
      _logger.logWarn(mn, null, "Error while parsing DateFormat", e);
    }
    
    if(scheduleId != null && group != null && username != null)
    {
    	generate(id, response, currentDate, group, username, _tz);
    }
    else
    {
      _logger.logWarn(mn, null, "Unable to generate a report properly due to null input.", null);
    }      

//    try
//    {
//      generate(id, response);
//    }
//    catch (Exception e)
//    {
//      _logger.logError("generateReport", null, "", e);
//    }
  }
  
  private void generate(int id, HttpServletResponse response, Date currentDate, String group, 
		  				String username, TimeZone tz) throws IOException, NamingException, CreateException
  {
    String mn = "generate";
    
    IGenScheduleHandlerHome home = lookup();
    IGenScheduleHandler genSchedule = home.create();
    ReportManager rm = new ReportManager(tz);
    
//    try
//    {
      Schedule s = (Schedule)genSchedule.getSchedule(id);
      String templateName = (String)genSchedule.getTemplateName(s.getReportType());
      String datasourceType = (String)genSchedule.getDatasourceType(s.getReportType());
      if(s!=null && templateName!=null)
      {
        boolean status = (boolean)rm.viewOnline(s, currentDate, group.trim(), username.trim(), response, templateName, datasourceType);
        if(status)
        {  
          _logger.logMessage(mn, null, "Report generated.");
        }
        else
        {  
          ServletOutputStream os = response.getOutputStream();
          response.setContentType("text/html");
          os.println("<html>");
          os.println("<head><title>Generate Report Status</title></head>");
          os.println("<body>");
          os.println("<p>Encounter internal error, unable to generate report.</p>");
          os.println("</body></html>");
          os.flush();
          _logger.logMessage(mn, null, "Report NOT generated.");
        }
      }
//    }
//    catch(Exception e)
//    {
//      _logger.logError("generate", null, "Error in retrieving data.", e);
//    }
  }
  
  private IGenScheduleHandlerHome lookup() throws NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    return (IGenScheduleHandlerHome)finder.lookup(IJndiNames.GENSCHEDULE_HANDLER, IGenScheduleHandlerHome.class);
  }
}
