/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ViewReportServlet.java
 *
 ****************************************************************************
 * Date             	Author              Changes
 ****************************************************************************
 * Mar 12, 2007        Regina Zeng          Created
 */

package com.gridnode.gridtalk.genreport.receive;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gridtalk.genreport.ejb.IGenReportHandler;
import com.gridnode.gridtalk.genreport.ejb.IGenReportHandlerHome;
import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.model.Report;
import com.gridnode.gridtalk.genreport.util.AbstractServlet;
import com.gridnode.gridtalk.genreport.util.IInputConstants;
import com.gridnode.gridtalk.genreport.util.IJndiNames;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Regina Zeng
 * This servlet is used when the web.xml maps to <url-pattern>/view_report</url-pattern>.
 */
public class ViewReportServlet extends AbstractServlet
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -7019594864273066809L;
  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_RECEIVE, "ViewReportServlet");
  private String _reportLocation = null;
  private String _reportFormat = null;
  private byte[] _reportContent = null;
  private String _contentType = null;
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {      
      viewReport(request, response);
    }
    catch (IOException ex)
    {
      _logger.logError(ILogErrorCodes.VIEW_REPORT_SERVLET_POST,"doPost", null,  "IO Error. Unable to handle request", ex);
    }
	catch (NamingException e)
	{
      _logger.logError(ILogErrorCodes.VIEW_REPORT_SERVLET_POST, "doPost", null, "JNDI Error. Unable to handle request", e);
	}
	catch (CreateException e)
	{
      _logger.logError(ILogErrorCodes.VIEW_REPORT_SERVLET_POST, "doPost", null, "EJB Creation Error. Unable to handle request", e);
	}
  }
  
  private void viewReport(HttpServletRequest request, HttpServletResponse response) throws IOException, NamingException, CreateException
  {
    String mn = "viewReport";
    
    String reportUid = request.getParameter(IInputConstants.REPORT_UID);
 
    if(reportUid != null)
    {
      view(reportUid, response);
    }
    else
    {
      _logger.logWarn(mn, null, "Unable to view report properly due to null input.", null);
    }
  }
  
  private void view(String reportUid, HttpServletResponse response) throws IOException, NamingException, CreateException
  {
    String mn = "view";
    String reportLocationTemp;
    
    IGenReportHandlerHome home = lookup();
    IGenReportHandler genReport = home.create();
    
    Report report = (Report)genReport.getReport(reportUid);
    if(report != null)
    {
      reportLocationTemp = report.getReportLocation();
      _reportFormat = report.getReportFormat();
      _reportContent = (byte[])report.getReportContent();
      if(reportLocationTemp != null)
      {
        _reportLocation = reportLocationTemp.replace(" ", "");
      }
    }   
    
    if(_reportContent != null && _reportLocation != null && _reportFormat != null && !"".equals(_reportContent))
    {
      _contentType = checkReportFormat(_reportFormat);
      try
      {
        uploadReport(_reportContent, _reportLocation, _contentType, response);
      }
      catch(Exception ex)
      {
        _logger.logError(ILogErrorCodes.VIEW_REPORT_SERVLET_VIEW_ONLINE, mn, null, "Error in getting the report with filename "+_reportLocation, ex);
      }
    }
    else
    {
      ServletOutputStream os = response.getOutputStream();
      response.setContentType("text/html");
      os.println("<html>");
      os.println("<head><title>View Report Status</title></head>");
      os.println("<body>");
      os.println("<p>Sorry! This report was generated to be viewed only once therefore is unavailable for viewing now.</p>");
      os.println("</body></html>");
      os.flush();
      _logger.logMessage(mn, null, "Report NOT available for viewing.");		
    }
  }
  
  private IGenReportHandlerHome lookup() throws NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    return (IGenReportHandlerHome)finder.lookup(IJndiNames.GENREPORT_HANDLER, IGenReportHandlerHome.class);
  }
  
  private String checkReportFormat(String reportFormat)
  {
    String format;

    if (reportFormat.trim().equalsIgnoreCase("txt")) 
    {
      format = "text/text";
    } 
    else if (reportFormat.trim().equalsIgnoreCase("xls")) 
    {
      format = "application/x-excel"; 
    } 
    else if (reportFormat.trim().equalsIgnoreCase("pdf")) 
    {
      format = "application/pdf";
    }
    else if (reportFormat.trim().equalsIgnoreCase("html"))
    {
      format = "text/html";
    }
    else if (reportFormat.trim().equalsIgnoreCase("rtf"))
    {
      format = "application/rtf";
    }
    else if (reportFormat.trim().equalsIgnoreCase("xml"))
    {
      format = "text/xml";
    }
    else 
    {
      format = "application/octet-stream";
    }
    return format;
  }
  
  private void uploadReport(byte[] reportContent, String reportLocation, String contentType, HttpServletResponse response) throws Exception
  {
    String mn = "uploadReport";
    ServletOutputStream op = null;
    ByteArrayInputStream input = null;
    try
    {
      op = response.getOutputStream();

      response.setContentType(contentType);
      response.setContentLength(reportContent.length);
      response.setHeader( "Content-Disposition", "attachment; filename=\"" + reportLocation + "\"" );
      
      byte[] buffer = new byte[512];
      input = new ByteArrayInputStream(reportContent);
      
      int readSoFar = 0;
      while( (readSoFar = input.read(buffer)) != -1)
      {
          op.write(buffer,0,readSoFar);
      }
    }
    catch(Exception ex)
    {
      _logger.logWarn(mn, null, "Error in reading the report content from filename "+_reportLocation, ex);
    }
    finally
    {
      if(input != null)
      {
    	op.flush();
        input.close();
      }
    }
  }
}
