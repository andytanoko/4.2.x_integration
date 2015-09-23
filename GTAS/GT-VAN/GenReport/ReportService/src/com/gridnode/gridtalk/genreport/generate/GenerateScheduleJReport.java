/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GenerateScheduleJReport.java
 *
 ****************************************************************************
 * Date                Author               Changes
 ****************************************************************************
 * Apr 03, 2007        Regina Zeng          Created
 * Jun 08, 2007		   Regina Zeng			To persist generate_report into db
 */

package com.gridnode.gridtalk.genreport.generate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;

import com.gridnode.gridtalk.genreport.ejb.IGenReportHandler;
import com.gridnode.gridtalk.genreport.ejb.IGenReportHandlerHome;
import com.gridnode.gridtalk.genreport.ejb.IGenScheduleHandler;
import com.gridnode.gridtalk.genreport.ejb.IGenScheduleHandlerHome;
import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.model.Report;
import com.gridnode.gridtalk.genreport.model.Schedule;
import com.gridnode.gridtalk.genreport.util.DateUtil;
import com.gridnode.gridtalk.genreport.util.IJndiNames;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.gridtalk.genreport.util.IReportConstants;
import com.gridnode.util.StringUtil;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.gridnode.util.mail.EmailSender;

public class GenerateScheduleJReport extends GenerateReportHelper
{
  private Logger _logger = null;
  private EmailSender _email = null;
  private List<String> _list = null;
  private TimeZone _tz = TimeZone.getDefault();
  private String _msg = "";
  private ArrayList<String> _fileList = new ArrayList<String>();
  private final String _imageTitle = IReportConstants.KEY_IMAGE_TITLE;
  private final String _imageSmallLogo = IReportConstants.KEY_IMAGE_SMALL_LOGO;
  
  public GenerateScheduleJReport(TimeZone tz)
  {
    _tz = tz;
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_GENERATE, "GenerateScheduleJReport");
  }
  
  public boolean generateReportSendEmail(String filename, JRExporter exporter, 
		  								 Schedule schedule, String scheduleNo, 
		  								 Date timeOfCall, String reportTemplate, 
		  								 String datasourceType)
  {
    String mn = "generateReportSendEmail";
    boolean status = false;
    JasperReport jasperReport = null;
    String outputFileName = getEnginePath(filename);
    List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
    String[] customerArray = (String[])StringUtil.split(schedule.getCustomerList(), ",");
    
    try 
    {
      jasperReport = JasperCompileManager.compileReport(getReportSub(reportTemplate));
      jasperReport = JasperCompileManager.compileReport(getReportMain(reportTemplate));
      for(int i=0; i<customerArray.length; i++)
      {
        String nextStartDateTime = DateUtil.processDate(schedule.getNextStartDateTime());
        String nextEndDateTime = DateUtil.processDate(schedule.getNextEndDateTime());
        String customer = customerArray[i];
        HashMap<String, Object> parameter = getParameter(reportTemplate, nextStartDateTime, nextEndDateTime, customer);  
        JasperPrint jasperPrint = getJasperPrint(jasperReport, parameter, datasourceType);
        jasperPrintList.add(jasperPrint);
      }

      if(jasperPrintList.size() > 0)
      {     
        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFileName); 
        if(schedule.getReportFormat().equals("xls"))
        {
          exporter.setParameter(JExcelApiExporterParameter.SHEET_NAMES, customerArray);
        }
        exporter.exportReport();
        _fileList.add(outputFileName);
        
        for(int i=0; i<_fileList.size(); i++)
        {
          File file = new File((String)_fileList.get(i));
          createReport(schedule, file, timeOfCall, schedule.getGroupName(), schedule.getUsername());
          Schedule newSchedule = (Schedule) updateScheduleInfo(schedule);
          Properties props = (Properties)updateSchedule(newSchedule, file, scheduleNo);
          boolean emailStatus = sendEmail(schedule, file, props);          
          file.delete();          
          if(emailStatus)
            status = true;
        }
      }
    }
    catch (JRException e) 
    {
      _logger.logError(ILogErrorCodes.SCHEDULE_REPORT_GENERATOR_GENERATE_EMAIL, mn, null, "JasperReport Error. Unable to generate a schedule report.", e);
    }
    catch(Exception e)
    {
      _logger.logError(ILogErrorCodes.SCHEDULE_REPORT_GENERATOR_GENERATE_EMAIL, mn, null, "Unable to update schedule setting into database: "+e.getMessage(), e);
    }
    return status;
  }
  
  public boolean generateReportViewOnline(String contentType, String filename, JRExporter exporter, 
                                          Schedule schedule, Date currentDate, String group, 
                                          String username,HttpServletResponse response, 
                                          String reportTemplate, String datasourceType)
  {
    String mn = "generateReportViewOnline";
    boolean status = false;
    JasperReport jasperReport = null;
    String outputFileName = getEnginePath(filename);
    List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
    String[] customerArray = (String[])StringUtil.split(schedule.getCustomerList(), ",");
    
    try 
    {
      jasperReport = JasperCompileManager.compileReport(getReportSub(reportTemplate));
      jasperReport = JasperCompileManager.compileReport(getReportMain(reportTemplate));
      for(int i=0; i<customerArray.length; i++)
      {
        String nextStartDateTime = DateUtil.processDate(schedule.getNextStartDateTime());
        String nextEndDateTime = DateUtil.processDate(schedule.getNextEndDateTime());
        String customer = customerArray[i];
        HashMap<String, Object> parameter = getParameter(reportTemplate, nextStartDateTime, nextEndDateTime, customer);  
        JasperPrint jasperPrint = getJasperPrint(jasperReport, parameter, datasourceType);
        jasperPrintList.add(jasperPrint);
      }
      
      if(jasperPrintList.size() > 0)
      {     
        OutputStream os = (OutputStream)response.getOutputStream();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFileName); 
        if(schedule.getReportFormat().equals("xls"))
        {
          exporter.setParameter(JExcelApiExporterParameter.SHEET_NAMES, customerArray);
        }
        exporter.exportReport();
        _fileList.add(outputFileName);
        boolean generateStatus = generateOnline(filename, os, response, contentType);
        if(generateStatus)
        {
          status = generateStatus;  
          for(int i=0; i<_fileList.size(); i++)
          {
        	File file = new File((String)_fileList.get(i));
        	createReport(schedule, file, currentDate, group, username);
        	file.delete();
          }
        }
      }
    }
    catch (JRException e) 
    {
      _logger.logError(ILogErrorCodes.SCHEDULE_REPORT_GENERATOR_GENERATE_ONLINE, mn, null, "JasperReport Error. Unable to generate a schedule report.", e);
    }
    catch (IOException e) 
    {
      _logger.logError(ILogErrorCodes.SCHEDULE_REPORT_GENERATOR_GENERATE_ONLINE, mn, null, "IO Error. Unable to generate schedule report online.", e);
    }
    catch(Exception e)
    {
      _logger.logError(ILogErrorCodes.SCHEDULE_REPORT_GENERATOR_GENERATE_ONLINE, mn, null, "Unable to get connection from database: "+e.getMessage(), e);
    }
    return status;
  } 
  
  private JasperPrint getJasperPrint(JasperReport jasperReport, HashMap parameter, String datasourceType) throws Exception
  {    
    String mn = "getJasperPrint";
    try
    {
      IGenReportHandlerHome home = lookupGenReport();
      IGenReportHandler genReport = home.create();  
      String datasourceDAO = genReport.getValue(IReportConstants.CAT_1, IReportConstants.KEY_TEMPLATE_DATASOURCE+datasourceType);
      JasperPrint jasperPrint = genReport.getJasperPrint(jasperReport, parameter, datasourceDAO);
      return jasperPrint;
    }
    catch (IOException e)
    {
      String message = "Unable to get connection from the database.";
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
  
  private Schedule updateScheduleInfo(Schedule s)
  {
    Schedule newSchedule = s;
    Date dateTime = s.getNextRunDateTime();
    Date nrdt = null;
    Date nsdt = null;
    Date nedt = null;
    Date[] nr = DateUtil.nextRunDateTime(dateTime, s.getFrequency()); 
    if(nr!=null)
    {
      nrdt = nr[0];
      nsdt = nr[1];
      nedt = nr[2];
    }
    if(nrdt!=null && nsdt!=null && nedt!=null)
    {
      newSchedule.setNextRunDateTime(nrdt);
      newSchedule.setNextStartDateTime(nsdt);
      newSchedule.setNextEndDateTime(nedt);
    }    
    return newSchedule;
  }
  
  private Properties updateSchedule(Schedule s, File file, String scheduleNo) throws Exception
  {
    String mn = "updateSchedule";
    Object[] params = {s, file, scheduleNo};
    
    IGenScheduleHandlerHome home = lookup();
    IGenScheduleHandler genSchedule = (IGenScheduleHandler)home.create();
    
    _msg = (String)genSchedule.getValue(IReportConstants.CAT_1, IReportConstants.KEY_EMAIL_BODY);
    
    if(file!=null)
    {
      InputStream is = new FileInputStream(file);
      long length = file.length();
      byte[] b = new byte[(int)length];
      int offset = 0;
      int numRead = 0;
      while(offset < b.length && (numRead = is.read(b, offset, b.length - offset)) >=0)
      {
      offset +=numRead;
      }
      if(offset < b.length)
      {
      _logger.logWarn(mn, params, "Could not completely read file " +file.getName() , null);
      }
      is.close();
        
      s.setReportContent(b);
      s.setReportLocation("");
    }
    else
    {
      s.setReportContent(null);
      _logger.logMessage(mn, params, "No report content save in database.");
    }    
    Properties props = (Properties)genSchedule.updateSchedule(s, scheduleNo);
    return props;
  }
  
  private boolean sendEmail(Schedule s, File newFile, Properties props)
  {
    String mn = "sendEmail";
    boolean status = false;
    File[] files = {newFile};
    _list = getRecipients(s);
    String sub = s.getReportType();
    if(_list!=null && sub!=null && _msg!=null && newFile!=null && !_list.isEmpty())
    {
      _logger.logMessage(mn, null, "Recipient email: "+s.getEmailList());
      _email = new EmailSender();
      _email.setSendProperties(props);
      _email.sendWithAttachments(_list, sub, _msg, files);
      status = true;
    }
    else
    {
      _logger.logError(ILogErrorCodes.SCHEDULE_REPORT_GENERATOR_GENERATE_EMAIL, mn, null, "Email Params Error. Unable to send email", null);
    }
    return status;
  }
  
  private List<String> getRecipients(Schedule s)
  {
    String recipientsStr = s.getEmailList();
    if (recipientsStr != null && recipientsStr.trim().length()>0)
    {
      List<String> recipientList = new ArrayList<String>();
      StringTokenizer stoken = new StringTokenizer(recipientsStr, ";");
      while (stoken.hasMoreTokens())
      {
        recipientList.add(stoken.nextToken());
      }
      return recipientList;
    }
    else
    {
      return null;
    }
  }
  
  private HashMap<String, Object> getParameter(String reportTemplate, String nextStartDateTime, String nextEndDateTime, String customer)
  {
    String mn = "getParameter";
    
    HashMap<String, Object> parameter = new HashMap<String, Object>();
    parameter.put("SUBREPORT_DIR", getSubReportDir(reportTemplate));
    parameter.put("REPORT_TIME_ZONE", _tz);
    parameter.put("customer_name", customer);
    parameter.put("start_time", nextStartDateTime);
    parameter.put("end_time", nextEndDateTime);
    try
    {
      String imagePathTemp = getPropertyValue(IReportConstants.KEY_REPORTSERVICE);
      String imagePath = imagePathTemp.replace(IReportConstants.UNWANTED_PATH, "");
      parameter.put("image_dir", imagePath+IReportConstants.IMAGE_PATH);
      parameter.put("image_title", getPropertyValue(_imageTitle)); 
      parameter.put("image_smallLogo", getPropertyValue(_imageSmallLogo));
    }
    catch (Exception e)
    {
      _logger.logError(ILogErrorCodes.SCHEDULE_REPORT_GENERATOR_GENERATE, mn, null, "Unable to retrieve image name from database.", e);
    }    
    return parameter;
  }
  
  private String getPropertyValue(String key) throws Exception
  {
    String mn = "getPropertyValue"; 
    try
    {
      IGenScheduleHandlerHome home = lookup();
      IGenScheduleHandler genReport = home.create();
      String value = genReport.getValue(IReportConstants.CAT_1, key);
      return value;
    }
    catch (IOException e)
    {
      String message = "Unable to get connection from the database.";
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
  
  private void createReport(Schedule s, File file, Date currentDate, String group, String username) throws Exception
  {
    String mn = "createReport";
    Object[] params = {s, file};
    
    IGenReportHandlerHome home = lookupGenReport();
    IGenReportHandler genReport = (IGenReportHandler)home.create();
    if(file!=null)
    {
      InputStream is = new FileInputStream(file);
      long length = file.length();
      byte[] b = new byte[(int)length];
      int offset = 0;
      int numRead = 0;
      while(offset < b.length && (numRead = is.read(b, offset, b.length - offset)) >=0)
      {
        offset +=numRead;
      }
      if(offset < b.length)
      {
      _logger.logWarn(mn, params, "Could not completely read file " +file.getName() , null);
      }
      is.close();
        
      s.setReportContent(b);
      s.setReportLocation(file.getName());
    }
    else
    {
      s.setReportContent(null);
      _logger.logMessage(mn, params, "No report content save in database when scheduling a report.");
    }
    Report report = new Report(s.getReportType(), s.getFrequency(), IReportConstants.REPORT_STATUS_INVALID, 
                               s.getCustomerList(), s.getNextStartDateTime(), s.getNextEndDateTime(), 
                               s.getReportFormat(), s.getReportContent(), s.getEmailList(), String.valueOf(s.getScheduleId()),
                               s.getReportLocation(), group, s.getNextRunDateTime(), username, currentDate);
    String[] rpt = (String[])genReport.createReport(report);
    if(rpt!=null)
    {
      //rpt[0]=reportNo, rpt[1]=template name, rpt[2]=datasource type
      _logger.logMessage(mn, params, "A report has been persisted in the database.");
    }
  }
  
  private IGenScheduleHandlerHome lookup() throws NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    return (IGenScheduleHandlerHome)finder.lookup(IJndiNames.SCHEDULE_HANDLER, IGenScheduleHandlerHome.class);
  }
  
  private IGenReportHandlerHome lookupGenReport() throws NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    return (IGenReportHandlerHome)finder.lookup(IJndiNames.REPORT_HANDLER, IGenReportHandlerHome.class);
  }
}
