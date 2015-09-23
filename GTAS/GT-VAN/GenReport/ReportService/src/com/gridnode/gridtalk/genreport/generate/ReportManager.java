/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportManager.java
 *
 ****************************************************************************
 * Date                 Author              Changes
 ****************************************************************************
 * Jan 23, 2007        Regina Zeng          Created
 * Mar 05, 2007			   Alain Ah Ming				Added error code to error logs
 * Apr 05, 2007        Regina Zeng          Modified all methods to support 
 *                                          rendering of JasperReport
 */

package com.gridnode.gridtalk.genreport.generate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;

import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.model.Report;
import com.gridnode.gridtalk.genreport.model.Schedule;
import com.gridnode.gridtalk.genreport.util.DateUtil;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Regina Zeng
 * This class will manage whether to generate Jreport or generate scheduled Jreport.
 * This class will also manage the type of content type and the type of JRExporter required.
 */
public class ReportManager
{
  private Logger _logger = null;
  private TimeZone _tz = TimeZone.getDefault();
  private GenerateJReport _genJReport;
  private GenerateScheduleJReport _genJSchedule;
  
  public ReportManager(TimeZone tz)
  {
    _tz = tz;
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_GENERATE, "ReportManager");
  }
  
  /**
   * This method will call the GenerateJReport class to generate report, thereafter to be viewed online.
   * @param report Report
   * @param response HttServletResponse
   * @param reportNo Report UID
   * @param reportTemplate report template name
   * @param datasourceType for the respective report template 
   * @return status
   */
  public boolean viewOnline(Report report, HttpServletResponse response, String reportNo, String reportTemplate, String datasourceType)
  {    
    String outputType = report.getReportFormat();
    String contentType = validateReportFormat(outputType);
    String filenameWithoutSpace = report.getReportType().replace(" ", "");
    String filename = filenameWithoutSpace+"."+outputType;
    JRExporter exporter = getJRExporter(outputType);
    _genJReport = new GenerateJReport(_tz);
    
    boolean status = _genJReport.generateReportViewOnline(contentType, filename, exporter, response, reportTemplate, report, reportNo, datasourceType);
    return status;
  }
  
  /**
   * This method will call the GenerateJReport class to generate report, thereafter to be 
   * sent to customer(s) via email.
   * @param report Report
   * @param reportNo Report UID
   * @param reportTemplate report template name
   * @param datasourceType for the respective report template
   * @return status
   */
  public boolean sendEmail(Report report, String reportNo, String reportTemplate, String datasourceType)
  {
    String outputType = report.getReportFormat();
    String filenameWithoutSpace = report.getReportType().replace(" ", "");
    String filename = filenameWithoutSpace+"."+outputType;
    JRExporter exporter = getJRExporter(outputType);
    _genJReport = new GenerateJReport(_tz);
    
    boolean status = _genJReport.generateReportSendEmail(filename, exporter, reportTemplate, report, reportNo, datasourceType);
    return status;
  }
  
  /**
   * This method will call the GenerateScheduleJReport class to generate report, thereafter 
   * to be sent to customer(s) via email.
   * @param schedule Schedule
   * @param scheduleNo schedule UID
   * @param timeOfCall Date of which the generate report is triggered
   * @param reportTemplate report template name
   * @param datasourceType for the respective report template
   */
  public void scheduleReport(Schedule schedule, String scheduleNo, Date timeOfCall, String reportTemplate, String datasourceType)
  {
    String mn = "scheduleReport";
    
    String outputType = schedule.getReportFormat();
    String filenameWithoutSpace = schedule.getReportType().replace(" ", "");
    String filename = filenameWithoutSpace+"."+outputType;
    JRExporter exporter = getJRExporter(outputType);
    _genJSchedule = new GenerateScheduleJReport(_tz);
    
    boolean status = _genJSchedule.generateReportSendEmail(filename, exporter, schedule, scheduleNo, timeOfCall, reportTemplate, datasourceType);
    if(status)
      _logger.logMessage(mn, null, "ScheduleReport will be generated on : "+DateUtil.processDate(schedule.getNextRunDateTime()));
    else
      _logger.logMessage(mn, null, "ScheduleReport NOT be generated - encounter internal error.");
  }
  
  /**
   * This method will call the GenerateScheduleJReport class to generate report, thereafter to be viewed online.
   * @param schedule Schedule
   * @param currentDate Date which this generate_report function is triggered
   * @param groupname
   * @param username
   * @param response schedule UID
   * @param reportTemplate report template name
   * @param datasourceType for the respective report template
   * @return status
   */
  public boolean viewOnline(Schedule schedule, Date currentDate, String group, String username, 
		  					HttpServletResponse response, String reportTemplate, String datasourceType)
  {    
    String outputType = schedule.getReportFormat();
    String contentType = validateReportFormat(outputType);
    String filenameWithoutSpace = schedule.getReportType().replace(" ", "");
    String filename = filenameWithoutSpace+"."+outputType;
    JRExporter exporter = getJRExporter(outputType);
    _genJSchedule = new GenerateScheduleJReport(_tz);
    
    boolean status = _genJSchedule.generateReportViewOnline(contentType, filename, exporter, schedule, 
    														currentDate, group, username, response, 
    														reportTemplate, datasourceType);
    return status;
  }
  
  /**
   * To validate the type of content type required base on the type of report format
   * @param reportFormat
   * @return content type
   */
  private String validateReportFormat(String reportFormat)
  {
    String outputType;
      
    if (reportFormat.trim().equalsIgnoreCase("txt")) 
    {
      outputType = "text/text";
    } 
    else if (reportFormat.trim().equalsIgnoreCase("xls")) 
    {
      outputType = "application/x-excel"; 
    } 
    else if (reportFormat.trim().equalsIgnoreCase("pdf")) 
    {
      outputType = "application/pdf";
    }
    else if (reportFormat.trim().equalsIgnoreCase("html"))
    {
      outputType = "text/html";
    }
    else if (reportFormat.trim().equalsIgnoreCase("rtf"))
    {
      outputType = "application/rtf";
    }
    else if (reportFormat.trim().equalsIgnoreCase("xml"))
    {
      outputType = "text/xml";
    }
    else 
    {
      outputType = "application/octet-stream";
    }
    return outputType;
  }
  
  /**
   * To determine the type of JRExporter required base on the different report format
   * @param outputType aka report format
   * @return JRExporter
   */
  private JRExporter getJRExporter(String outputType) 
  {
    String mn = "getJRExporter";
    Object[] params = {outputType};
    
    JRExporter exporter = null;
    if ("html".equals(outputType)) 
    {      
      Map imageMap = new HashMap();
      exporter = new JRHtmlExporter();
      exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
      exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
      exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imageMap);
      exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);
      exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "<hr/>");
    } 
    else if ("pdf".equals(outputType)) 
    {
      exporter = new JRPdfExporter();
    } 
    else if ("xls".equals(outputType)) 
    { 
      exporter = new JExcelApiExporter();      
      exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
      exporter.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
      exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
    } 
    else if ("csv".equals(outputType)) 
    { 
      exporter = new JRCsvExporter();
      exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ",");
      exporter.setParameter(JRCsvExporterParameter.RECORD_DELIMITER, "\n"); 
    } 
    else if ("xml".equals(outputType)) 
    { 
      exporter = new JRXmlExporter();
    }
    else if ("txt".equals(outputType))
    {
      exporter = new JRTextExporter();
      exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, new Integer(80));
      exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Integer(6)); 
    }
    else if ("rtf". equals(outputType))
    {
      exporter = new JRRtfExporter();      
    }
    else
    {
      _logger.logError(ILogErrorCodes.REPORT_MANAGER_GET_JREXPORTER, mn, params, "Unable to interpret the report format.", null);
    }
    return exporter;
  }
}