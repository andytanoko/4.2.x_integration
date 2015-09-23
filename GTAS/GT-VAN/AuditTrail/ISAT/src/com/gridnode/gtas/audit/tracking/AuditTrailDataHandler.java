/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailDataHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 14, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerHome;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to handle the AuditTrailData while we receive it from
 * the OTC plug in. It will persist the record in DB first. It also provide the service
 * for retrieving the outstanding AuditTrailData that are waiting for processing. 
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class AuditTrailDataHandler
{
  private static final String CLASS_NAME = "AuditTrailDataHandler";
  private static final AuditTrailDataHandler _handler = new AuditTrailDataHandler();
  private Properties _jndiLookupProperties = null;
  private Logger _logger = null;
  
  private AuditTrailDataHandler()
  {
    _logger = getLogger();
  }
  
  public static AuditTrailDataHandler getInstance()
  {
    return _handler;
  }
  
  public void persistAuditTrailData(AuditTrailData trailData) throws Exception
  {
    IAuditTrailManagerObj auditMgr = getAuditTrailMgr();
    auditMgr.delayProcessAuditTrailData(trailData);
  }
  
  public boolean processOutstandingAuditTrailData(String dataRecordUID) throws Exception
  {
    String method = "processOutstandingAuditTrailData";
    
    System.out.println("AuditTrailDataHandler processOutstandingAuditTrailDataRecord starting with UID "+dataRecordUID);
    _logger.logMessage(method, null, "Process outstanding data with uid "+dataRecordUID);
    
    IAuditTrailManagerObj auditMgr = getAuditTrailMgr();
    //String dataRecordUID = auditMgr.retrieveOutstandingAuditTrailDataUID(maxAttemptCount);
    if(dataRecordUID != null)
    {
      try
      {
        auditMgr.handleAuditTrailData(dataRecordUID);
        return true;
      }
      catch(AuditTrailTrackingException ex)
      {
        _logger.logMessage(method, null, "Errr AuditTrailTrackingException catched");
        ex.printStackTrace();
        auditMgr.updateAuditTrailRecord(dataRecordUID, ex.getMessage());
        throw ex;
      }
      catch(Exception ex)
      {
        _logger.logMessage(method, null, "General exception catch");
        ex.printStackTrace();
        //auditMgr.updateAuditTrailRecord(dataRecordUID, ex.getMessage()); Ignore the those System Exception
        throw ex;
      }
    }
    else
    {
      System.out.println("AuditTrailDataHandler No Outstanding AuditTrailDataRecord .....");
      _logger.logMessage(method, null, "AuditTrailDataHandler No Outstanding AuditTrailDataRecord .....");
      return false;
    }
  }
  
  public List<String> getAuditTrailInfoRecordUIDs(int maxAttemptCount, int totalRecordForProcessPerCall) throws Exception
  {
    IAuditTrailManagerObj auditMgr = getAuditTrailMgr();
    return auditMgr.retrieveAuditTrailDataRecordUID(maxAttemptCount, totalRecordForProcessPerCall);
  }
  
  public void setJndiLookupProperties(Properties jndiLookupPro)
  {
    _jndiLookupProperties = jndiLookupPro;
  }
  
  public Properties getJndiLookupProperties()
  {
    return _jndiLookupProperties;
  }
  
  private IAuditTrailManagerObj getAuditTrailMgr() throws Exception
  {
    JndiFinder finder = null;;
		try
		{
			finder = new JndiFinder(getJndiLookupProperties());
		}
		catch (NamingException e)
		{
			throw new Exception("Failed to initialise JNDI");
		}
    IAuditTrailManagerHome home = (IAuditTrailManagerHome)finder.lookup(IAuditTrailManagerHome.class.getName(),
                                                                        IAuditTrailManagerHome.class);
    try
		{
			return home.create();
		}
		catch (RemoteException e)
		{
			throw new Exception("EJB Invocation Error", e);
		}
		catch (CreateException e)
		{
			throw new Exception("EJB Creation Error", e);
		}
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
