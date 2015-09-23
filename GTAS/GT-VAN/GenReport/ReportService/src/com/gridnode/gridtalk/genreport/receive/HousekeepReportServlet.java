/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HousekeepReportServlet.java
 *
 ****************************************************************************
 * Date  	               Author              Changes
 ****************************************************************************
 * Mar 14, 2007         Regina Zeng          Created
 * Mar 26, 2007		      Regina Zeng			     Added TimeZone
 * Apr 19, 2007         Regina Zeng          Modified TimeZone
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

import com.gridnode.gridtalk.genreport.ejb.IGenReportHandler;
import com.gridnode.gridtalk.genreport.ejb.IGenReportHandlerHome;
import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
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
 * This servlet is used when the web.xml maps to <url-pattern>/housekeep_report</url-pattern>.
 */
public class HousekeepReportServlet extends AbstractServlet
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 2055760342639702675L;
  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_RECEIVE, "HousekeepReportServlet");
  private static final String SUC = IReportConstants.CONFIRM+"Schedule Updated Successfully";
  private static final String UNSUC = IReportConstants.CONFIRM+"Schedule NOT Updated";
  private static final String DAILY = "Daily";
  private static final String WEEKLY = "Weekly";
  private static final String MONTHLY = "Monthly";
  private static final String ONCE = "Once";
  private static final DateFormat _df = new SimpleDateFormat(IReportConstants.DATE_FORMAT);
  private TimeZone _tz = TimeZone.getDefault();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      housekeepReport(request, response);
    }
    catch (IOException ex)
    {
      _logger.logError(ILogErrorCodes.HOUSEKEEP_REPORT_SERVLET_POST,"doPost", null,  "IO Error. Unable to handle request", ex);
    }
		catch (NamingException e)
		{
      _logger.logError(ILogErrorCodes.HOUSEKEEP_REPORT_SERVLET_POST, "doPost", null, "JNDI Error. Unable to handle request", e);
		}
		catch (CreateException e)
		{
      _logger.logError(ILogErrorCodes.HOUSEKEEP_REPORT_SERVLET_POST, "doPost", null, "EJB Creation Error. Unable to handle request", e);
		}
  }
  
  private void housekeepReport (HttpServletRequest request, HttpServletResponse response) throws NamingException, CreateException, IOException
  {
    String mn = "housekeepReport";
    
    String timezone = request.getParameter(IInputConstants.TIMEZONE);
    String frequency = request.getParameter(IInputConstants.FREQUENCY);
    String byDate = request.getParameter(IInputConstants.BY_DATE);
    String numDays = request.getParameter(IInputConstants.NUM_DAYS);
    long curDate = System.currentTimeMillis();
    //Date currentDate = new Date(curDate);
    _logger.logMessage(mn, null, frequency+byDate+numDays);
    
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
    
    if(numDays != null && (frequency.equals(DAILY) || frequency.equals(WEEKLY) || frequency.equals(MONTHLY)))
    {
      String nextDeleteDate = generateNextDeleteDate(currentDate, frequency);
      if(nextDeleteDate!=null)
      {
        updateDeleteScheduler(frequency, nextDeleteDate, numDays, response);
      }
    }
    else if(numDays != null && frequency.equals(ONCE) && byDate != null)
    {
      Date generateDate = null;
      try
      {
    	  generateDate = (Date)_df.parse(byDate.trim());  
      }
      catch(Exception e)
      {
    	_logger.logWarn(mn, null, "Error while parsing DateFormat", e);    	 
      }
      if(generateDate != null)
      {
        String generateDateString = (String)_df.format(generateDate);
        String nextDeleteDate = generateDateString;
        updateDeleteScheduler(frequency, nextDeleteDate, numDays, response);
      }
    }
    else
    {
      _logger.logWarn(mn, null, "Unable to view report properly due to null input.", null);
    }
  }

  private void updateDeleteScheduler(String frequency, String byDate, String numDays, HttpServletResponse res) throws NamingException, CreateException, IOException
  {
    IGenReportHandlerHome home = lookup();
    IGenReportHandler genReport = home.create();
     
    boolean statusForDeleteScheduler = (boolean)genReport.updateDeleteScheduler(IReportConstants.CAT_2, IReportConstants.KEY_FREQUENCY, 
                                                  														  IReportConstants.KEY_NEXT_DELETE_DATE, 
                                                                                IReportConstants.KEY_ARCHIVE_DURATION, 
                                                  														  frequency, byDate, numDays);
    genReport.updateReport(numDays);
    if(statusForDeleteScheduler)
    {   
      String url = genReport.getValue(IReportConstants.CAT_1, IReportConstants.KEY_REPORTSERVICE);
      String forward = url+IReportConstants.HOUSEKEEP_REPORT_PATH+SUC;    
      res.sendRedirect(forward);
    }
    else
    {
      String url = genReport.getValue(IReportConstants.CAT_1, IReportConstants.KEY_REPORTSERVICE);
      String forward = url+IReportConstants.HOUSEKEEP_REPORT_PATH+UNSUC;   
      res.sendRedirect(forward);
    }
  }
  
  private IGenReportHandlerHome lookup() throws NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    return (IGenReportHandlerHome)finder.lookup(IJndiNames.GENREPORT_HANDLER, IGenReportHandlerHome.class);
  }
  
  private String generateNextDeleteDate(Date currentDate, String frequency)
  {
    String deleteDate = null;
    if(frequency.equals(DAILY))
    {
      deleteDate = (String)_df.format(DateUtil.getNextDaily(currentDate));	
    }
    else if(frequency.equals(WEEKLY))
    {
      deleteDate = (String)_df.format(DateUtil.getNextWeekly(currentDate));	
    }
    else 
    {
      deleteDate = (String)_df.format(DateUtil.getNextMonthy(currentDate));	
    }
    return deleteDate;
  }
}