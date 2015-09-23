/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GenReportHandlerBean.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 19, 2007        Regina Zeng          Created
 * Mar 05, 2007			Alain Ah Ming				Added error code in error log messages
 */

package com.gridnode.gridtalk.genreport.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.hibernate.Session;

import com.gridnode.gridtalk.genreport.dao.DefaultDAO;
import com.gridnode.gridtalk.genreport.dao.ReportDAO;
import com.gridnode.gridtalk.genreport.dao.UserDAO;
import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.model.Report;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.db.DAO;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.gridnode.util.mail.EmailSender;

/**
 * @author Regina Zeng
 * This SessionBean handles the persistence of reports to be 
 * viewed online or send to recipient(s).
 */
public class GenReportHandlerBean implements SessionBean
{
  /**
   * Serial Version UID 
   */
  private static final long serialVersionUID = -6044469293997310511L;
  private SessionContext _sc;
  private Logger _logger;
  private ReportDAO _dao;
  public List<String> _list = null;
  
  public void ejbCreate() throws EJBException, RemoteException
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_EJB, "GenReportHandlerBean");
    _dao = new ReportDAO();    
  }

  public void ejbActivate() throws EJBException, RemoteException
  {
  }

  public void ejbPassivate() throws EJBException, RemoteException
  {
  }

  public void ejbRemove() throws EJBException, RemoteException
  {
    _logger = null;
    _dao = null;
  }

  public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException
  {
    _sc = arg0;
  }
  
  public Report getReport(String uid)
  {
    String mtdName = "getReport";
    Object[] params = {uid};
    
    _logger.logEntry(mtdName, params);
    Report report = (Report)_dao.getReport(uid);
    _logger.logExit(mtdName, params);
    return report;
  }  
  
  public Properties updateReport(Report report, String reportNo)
  {
    String mtdName = "updateReport";
    Object[] params = {report, reportNo};
    
    _logger.logEntry(mtdName, params);
    Properties props = (Properties)ConfigurationStore.getInstance().getProperties(EmailSender.DEFAULT_SMTP_SERVER);
    
    _dao.associateTransactionContext(false);
    String groupName = null;
    try
    {
      _dao.beginTransaction();
      boolean status = (boolean)_dao.updateQuery(report, reportNo);
      if(status)
        _logger.logMessage(mtdName, params, "Report updated successfully!");
      else
      {
        _logger.logWarn(mtdName, params, "No record is updated.", null);
      }
      
      _dao.commitTransaction();
      _dao.flushSession();
      
      _dao.closeTransactionContext();
      groupName = report.getGroup();
      String msg = "Report updated for group name "+groupName;
      _logger.logMessage(mtdName, params, msg);
      return props;
    }
    catch(Throwable t)
    {
      String msg = "Unable to update report for group name "+groupName+": Unexpected Error: "+t.getMessage();
      _logger.logError(ILogErrorCodes.REPORT_HANDLER_UPDATE, mtdName, params, msg, t);
     
      //if transaction is still active (this is true if non-db errors are encountered), rollback
      if (_dao.hasTransaction())
      {
        _dao.rollbackTransaction();
      }
      throw new RuntimeException(msg, t);
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
  
  public String[] createReport(Report report)
  {
    String mtdName = "createReport";
    Object[] params = {report};
    
    _logger.logEntry(mtdName, params);
    String templateName = (String)getTemplateName(report.getReportType());
    String datasourceType = (String)getDatasourceType(report.getReportType());
    String groupName = null;
    _dao.associateTransactionContext(false); 
    
    try
    {
      _dao.beginTransaction();
      String reportNo = (String)_dao.create(report);
      
      _dao.commitTransaction();
      _dao.flushSession();
      
      _dao.closeTransactionContext();
      groupName = report.getGroup();
      String msg = "Saved Report "+reportNo+" for group name "+groupName;
      _logger.logMessage(mtdName, params, msg);
      if(reportNo!=null && templateName!=null) 
      {
        String[] strings = {reportNo, templateName, datasourceType};
        return strings;  
      }
      else
      {
        return null;
      }
    }
    catch(Throwable t)
    {
      String msg = "Unable to create report for group name "+groupName+": Unexpected Error: "+t.getMessage();
      _logger.logError(ILogErrorCodes.REPORT_HANDLER_CREATE, mtdName, params, msg, t);
      
      //if transaction is still active (this is true if non-db errors are encountered), rollback
      if (_dao.hasTransaction())
      {
        _dao.rollbackTransaction();
      }
      throw new RuntimeException(msg, t);
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }

  public boolean updateDeleteScheduler(String cat, String keyFrequency, String keyNextDeleteDate, 
                			   						   String archiveDuration, String frequency, String byDate, 
                			   						   String numDays)
  {
  	boolean status = false;
  	String mtdName = "updateDeleteScheduler";
  	Object[] params = {cat, keyFrequency, keyNextDeleteDate, archiveDuration, frequency, byDate, numDays};
  	
  	_logger.logEntry(mtdName, params);
  
  	boolean frequencyStatus = (boolean)ConfigurationStore.getInstance().updateProperties(cat, keyFrequency, frequency);
  	boolean nextDeleteDateStatus = (boolean)ConfigurationStore.getInstance().updateProperties(cat, keyNextDeleteDate, byDate);
  	boolean archiveDurationStatus = (boolean)ConfigurationStore.getInstance().updateProperties(cat, archiveDuration, numDays);
  	if(frequencyStatus == true && nextDeleteDateStatus == true && archiveDurationStatus == true)
  	{
  	  status = true;
  	  _logger.logMessage(mtdName, params, "Delete Report Scheduler updated successfully!");
  	}
  	else
  	{
  	  _logger.logWarn(mtdName, params, "No record is updated.", null);
  	}	  
  	_logger.logExit(mtdName, params);
  	return status;
  }
  
  public void updateReport(String numDays)
  {
    String mtdName = "updateReport";
    Object[] params = {numDays};
    
    _logger.logEntry(mtdName, params);
    _dao.associateTransactionContext(false);
    
    try
    {
      _dao.beginTransaction();
      boolean status = (boolean)_dao.updateArchiveDuration(numDays);
      if(status)
      {
        _logger.logMessage(mtdName, params, "Updated Report archive duration successfully.");
      }
      else
      {
        _logger.logWarn(mtdName, params, "No record is updated.", null);
      }
      _dao.commitTransaction();
      _dao.flushSession();
      _dao.closeTransactionContext();
    }
    catch(Exception ex)
    {
      String msg = "Unable to update report for archive duration "+numDays+" days.";
      _logger.logError(ILogErrorCodes.REPORT_HANDLER_UPDATE, mtdName, params, msg, null);
      if (_dao.hasTransaction())
      {
        _dao.rollbackTransaction();
      }
      throw new RuntimeException(msg, ex);
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
  
  private String getTemplateName(String reportType)
  {
    String mtdName = "getTemplateName";
    Object[] params = {reportType};
    
    _logger.logEntry(mtdName, params);
    String templateName = (String)_dao.getTemplateName(reportType);
    _logger.logExit(mtdName, params);
    return templateName;
  }
  
  private String getDatasourceType(String reportType)
  {
    String mtdName = "getDatasourceType";
    Object[] params = {reportType};
    
    _logger.logEntry(mtdName, params);
    String datasourceType = (String)_dao.getDatasourceType(reportType);
    _logger.logExit(mtdName, params);
    return datasourceType;
  }
  
  public String getValue(String cat, String key)
  {
    String mtdName = "getValue";
    Object[] params = {cat, key};
    _logger.logEntry(mtdName, params);
    Properties props = (Properties)ConfigurationStore.getInstance().getProperties(cat);
    _logger.logExit(mtdName, params);
    return (String)props.getProperty(key);
  }
  
  public JasperPrint getJasperPrint(JasperReport jasperReport, HashMap parameter, String datasourceDAO)
  {    
    String mtdName = "getConnection";
    Object[] params = {};
    _logger.logEntry(mtdName, params);
    JasperPrint jasperPrint = null;
    DAO dao = new DefaultDAO();
    if(datasourceDAO != null && !datasourceDAO.trim().equals("DefaultDAO"))
    {
    	dao = null;
    	dao = new UserDAO();
    }
    
    dao.associateTransactionContext(false);
    try
    {
      dao.beginTransaction();
      Session session = (Session)dao.getTransactionContext().getSession();
      Connection con = session.connection();
      if(con != null)
      {
        jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, con);
      }      
      dao.commitTransaction();
      dao.flushSession();
      dao.closeTransactionContext();
      session.close();
      con.close();
      return jasperPrint;
    }
    catch(Exception ex)
    {
      String msg = "Unable to get JasperPrint to connect to the database.";
      _logger.logError(ILogErrorCodes.REPORT_HANDLER_GET_JASPERPRINT, mtdName, params, msg, null);
      if (dao.hasTransaction())
      {
        dao.rollbackTransaction();
      }
      throw new RuntimeException(msg, ex);    
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
}
