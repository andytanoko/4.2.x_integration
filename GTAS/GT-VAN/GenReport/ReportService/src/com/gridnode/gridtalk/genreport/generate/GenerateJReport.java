/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GenerateJReport.java
 *
 ****************************************************************************
 * Date                Author               Changes
 ****************************************************************************
 * Apr 02, 2007        Regina Zeng          Created
 * Jun 08, 2007		   Regina Zeng			To persist view online report into db
 */

package com.gridnode.gridtalk.genreport.generate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.model.Report;
import com.gridnode.gridtalk.genreport.util.DateUtil;
import com.gridnode.gridtalk.genreport.util.IJndiNames;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.gridtalk.genreport.util.IReportConstants;
import com.gridnode.util.StringUtil;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.gridnode.util.mail.EmailSender;

public class GenerateJReport extends GenerateReportHelper
{
  private Logger _logger = null;
  private EmailSender _email = null;
  private List<String> _list = null;
  private TimeZone _tz = TimeZone.getDefault();
  private String _msg = "";
  private ArrayList<String> _fileList = new ArrayList<String>();
  private final String _imageTitle = IReportConstants.KEY_IMAGE_TITLE;
  private final String _imageSmallLogo = IReportConstants.KEY_IMAGE_SMALL_LOGO;
  
  public GenerateJReport(TimeZone tz)
  {
    _tz = tz;
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_GENERATE, "GenerateJReport");
  }
  
  public boolean generateReportViewOnline(String contentType, String filename, JRExporter exporter, 
                                          HttpServletResponse response, String reportTemplate, 
                                          Report report, String reportNo, String datasourceType)
  {
    String mn = "generateReportViewOnline";
    boolean status = false;
    JasperReport jasperReport = null;
    List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
    String outputFileName = getEnginePath(filename);
    String[]customerArray = (String[])StringUtil.split(report.getCustomerList(), ",");
    
    try 
    {
      jasperReport = JasperCompileManager.compileReport(getReportMain(reportTemplate));
      for(int i=0; i<customerArray.length; i++)
      {
        String startTime = DateUtil.processDate(report.getStartDateTime());
        String endTime = DateUtil.processDate(report.getEndDateTime());  
        String customer = customerArray[i];
        HashMap<String, Object> parameter = getParameter(reportTemplate, startTime, endTime, customer);                                                         
        JasperPrint jasperPrint = getJasperPrint(jasperReport, parameter, datasourceType);
        jasperPrintList.add(jasperPrint);
      }    

      if(jasperPrintList.size() > 0 )
      {     
        OutputStream os = (OutputStream)response.getOutputStream();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFileName);
        if(report.getReportFormat().equals("xls"))
        {
          exporter.setParameter(JExcelApiExporterParameter.SHEET_NAMES, customerArray);
        }
        exporter.exportReport();
        _fileList.add(outputFileName);
        boolean printStatus = generateOnline(filename, os, response, contentType);
        if(printStatus)
        {
          status = true;
          for(int i=0; i<_fileList.size(); i++)
          {
        	File file = new File((String)_fileList.get(i));
        	updateReportSetting(report, file, reportNo);
        	file.delete();
          }
        }
      }
    }
    catch (JRException e) 
    {
      _logger.logError(ILogErrorCodes.REPORT_GENERATOR_GENERATE_ONLINE, mn, null, "JasperReport Error. Unable to generate a report.", e);
    }
    catch (IOException e) 
    {
      _logger.logError(ILogErrorCodes.REPORT_GENERATOR_GENERATE_ONLINE, mn, null, "IO Error. Unable to generate report online.", e);
    }
    catch(Exception e)
    {
      _logger.logError(ILogErrorCodes.REPORT_GENERATOR_GENERATE_ONLINE, mn, null, "Unable to view online report: "+e.getMessage(), e);
    }
    return status;
  }
  
  public boolean generateReportSendEmail(String filename, JRExporter exporter, 
                                         String reportTemplate, Report report, String reportNo,
                                         String datasourceType)
  {
    String mn = "generateReportSendEmail";
    boolean status = false;
    JasperReport jasperReport = null;
    List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
    String outputFileName = getEnginePath(filename);
    String[]customerArray = (String[])StringUtil.split(report.getCustomerList(), ",");      
    
    try 
    {
      jasperReport = JasperCompileManager.compileReport(getReportMain(reportTemplate));
      for(int i=0; i<customerArray.length; i++)
      {
        String startTime = DateUtil.processDate(report.getStartDateTime());
        String endTime = DateUtil.processDate(report.getEndDateTime()); 
        String customer = customerArray[i];
        HashMap<String, Object> parameter = getParameter(reportTemplate, startTime, endTime, customer);
        JasperPrint jasperPrint = getJasperPrint(jasperReport, parameter, datasourceType);
        jasperPrintList.add(jasperPrint);
      }
      
      if(jasperPrintList.size() > 0)
      {     
        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputFileName); 
        if(report.getReportFormat().equals("xls"))
        {
          exporter.setParameter(JExcelApiExporterParameter.SHEET_NAMES, customerArray);
        }
        exporter.exportReport();
        _fileList.add(outputFileName);
        
        for(int i=0; i<_fileList.size(); i++)
        {
          File file = new File((String)_fileList.get(i));
          
          Properties props = (Properties)updateReportSetting(report, file, reportNo);
          boolean emailStatus = sendEmail(report, file, props);          
          file.delete();          
          if(emailStatus)
            status = true;
        }
      }
    }
    catch (JRException e) 
    {
      _logger.logError(ILogErrorCodes.REPORT_GENERATOR_GENERATE_EMAIL, mn, null, "JasperReport Error. Unable to generate a report.", e);
    }

    catch(Exception e)
    {
      _logger.logError(ILogErrorCodes.REPORT_GENERATOR_GENERATE_EMAIL, mn, null, "Unable to update report setting into database: "+e.getMessage(), e);
    }
    return status;   
  }
  
  
  //This method not confirmed yet
  private boolean sendEmail(Report r, File newFile, Properties props)
  {
    String mn = "sendEmail";
    boolean status = false;
    File[] files = {newFile};
    _list = getRecipients(r);
    String sub = r.getReportType();
    if(_list!=null && sub!=null && _msg!=null && newFile!=null && !_list.isEmpty())
    {
      _logger.logMessage(mn, null, "Recipient email: "+r.getEmailList());
      _email = new EmailSender();
      _email.setSendProperties(props);
      _email.sendWithAttachments(_list, sub, _msg, files);
      status = true;
    }
    else
    {
      _logger.logError(ILogErrorCodes.REPORT_GENERATOR_GENERATE_EMAIL, mn, null, "Email Params Error. Unable to send email", null);
    }
    return status;
  }
  
  private List<String> getRecipients(Report r)
  {
    String recipientsStr = r.getEmailList();
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
  
  private HashMap<String, Object> getParameter(String reportTemplate, String startTime, String endTime, String customer)
  {
    String mn = "getParameter";
    
    HashMap<String, Object> parameter = new HashMap<String, Object>();
    parameter.put("SUBREPORT_DIR", getSubReportDir(reportTemplate));
    parameter.put("REPORT_TIME_ZONE", _tz);    
    parameter.put("customer_name", customer);
    parameter.put("start_time", startTime);
    parameter.put("end_time", endTime);
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
      _logger.logError(ILogErrorCodes.REPORT_GENERATOR_GENERATE, mn, null, "Unable to retrieve image name from database.", e);
    }    
    return parameter;
  }
  
  private JasperPrint getJasperPrint(JasperReport jasperReport, HashMap parameter, String datasourceType) throws Exception
  {    
    String mn = "getJasperPrint";
    try
    {
      IGenReportHandlerHome home = lookup();
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
  
  private Properties updateReportSetting(Report r, File file, String reportNo) throws Exception
  {
    String mn = "updateReportSetting";
    Object[] params = {r, file, reportNo};    
  
    IGenReportHandlerHome home = lookup();
    IGenReportHandler genReport = home.create();
    
    _msg = (String)genReport.getValue(IReportConstants.CAT_1, IReportConstants.KEY_EMAIL_BODY);
   
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

      r.setReportContent(b);
      r.setReportLocation(file.getName()); 
      _logger.logMessage(mn, null, "Report content save in database with filename "+file.getName());
    }
    else
    {
      r.setReportContent(null);
      _logger.logMessage(mn, null, "No report content save in database.");
    }
    r.setStatus(IReportConstants.REPORT_STATUS_INVALID);   
    Properties props = (Properties)genReport.updateReport(r, reportNo);
    return props;
  }
  
  private String getPropertyValue(String key) throws Exception
  {
    String mn = "getPropertyValue"; 
    try
    {
      IGenReportHandlerHome home = lookup();
      IGenReportHandler genReport = home.create();
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
  
  private IGenReportHandlerHome lookup() throws NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    return (IGenReportHandlerHome)finder.lookup(IJndiNames.GENREPORT_HANDLER, IGenReportHandlerHome.class);
  }
}
