/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TrailInfoService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 8, 2007    Tam Wei Xiang       Created
 * Mar 07 2007		Alain Ah Ming				Removed printStackTrace
 */
package com.gridnode.gtas.audit.tracking;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.system.ServiceMBeanSupport;

import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerHome;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This MBean is used to delegating the request for processing the AuditTrailData that
 * we persisted before.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class TrailInfoService extends ServiceMBeanSupport 
                                           implements TrailInfoServiceMBean
{
  private static final String CLASS_NAME = "TrailInfoService";
  private Logger _logger = LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  
  private boolean _isProcessing = false;
  private int _attemptCount; //the max attempt count for processing an AuditTrailData record.
  private int _totalRecordForProcessPerCall;
  
  private Date _lastActivateTime = null;
  private Date _lastEndProcessedTime = null;
  private Properties _jndiProperties = null;
  
  /**
   * Start the execution on processing the AuditTrail records which persisted in DB.
   */
  public void processAuditTrailData()
  {
    String methodName = "processAuditTrailData";
    _logger.logEntry(methodName, null);
    setLastActivateTime(new Date());
    
    try
    {
      if(isProcessing())
      {
        _logger.logMessage(methodName, null, "The operation for processing the AuditTrailData still in operation.");
        setLastEndProcssedTime(new Date());
        return;
      }
      
      setProcessing(true);
//      int totalRecordProcessed = 0;
      
      List<String> trailDataRecordUIDs = null; 
      if(_attemptCount > 0)
      {
        trailDataRecordUIDs = getAuditTrailDataHandler().getAuditTrailInfoRecordUIDs(_attemptCount, _totalRecordForProcessPerCall);
        if(trailDataRecordUIDs != null && trailDataRecordUIDs.size() > 0)
        {
          _logger.logMessage(methodName, null,"Retrieve outstanding trail data list size is "+trailDataRecordUIDs.size());
          
          Iterator<String> uids = trailDataRecordUIDs.iterator();
          while(uids.hasNext())
          {
            String auditTrailDataRecordUID = uids.next();
            _logger.debugMessage(methodName, null, "TrailInfoService Retrieve UID is "+auditTrailDataRecordUID);
            _logger.logMessage(methodName, null, "Retrieving outstanding trail data record "+auditTrailDataRecordUID+" for processing...");
            AuditTrailDataHandler handler = getAuditTrailDataHandler();
            
            try
            {
              handler.processOutstandingAuditTrailData(auditTrailDataRecordUID);
            }
            catch(Throwable ex)
            {
              _logger.logError(ILogErrorCodes.AT_PROCESS_AUDIT_TRAIL_DATA_ERROR, methodName, null,"Error in processing outstanding AuditTrailDataRecord. Error: "+ex.getMessage(), ex);
            }
          }
        }
      }

      _logger.logMessage(methodName, null, "The operation for processing the AuditTrailData finished .... ");
    }
    catch(Exception ex)
    {
      _logger.logError(ILogErrorCodes.AT_PROCESS_AUDIT_TRAIL_DATA_ERROR,
                       "processAuditTrailData", null, "Failed to process the AuditTrailDataRecords. Error: "+
                       ex.getMessage(), ex);
    }
    finally
    {
      setProcessing(false);
      setLastEndProcssedTime(new Date());
    }
  }
  
  private AuditTrailDataHandler getAuditTrailDataHandler()
  {
    AuditTrailDataHandler handler = AuditTrailDataHandler.getInstance();
    handler.setJndiLookupProperties(getJndiProperties());
    return handler;
  }

  public boolean isProcessing()
  {
    return _isProcessing;
  }

  public void setProcessing(boolean processing)
  {
    _isProcessing = processing;
  }

  public int getAttemptCount()
  {
    return _attemptCount;
  }

  public void setAttemptCount(int count)
  {
    if(count < -1)
    {
      throw new IllegalArgumentException("Attempt Count shouldn't be less than -1. Tips: -1 means attempt forever, 0 means no attempts will be performed. Positive integer will be attempted as specified");
    }
    _attemptCount = count;
  }
  
  public int getTotalRecordForProcessPerCall()
  {
    return _totalRecordForProcessPerCall;
  }
  
  public void setTotalRecordForProcessPerCall(int totalRecordForProcessPerCall)
  {
    if(totalRecordForProcessPerCall <= 0)
    {
      throw new IllegalArgumentException("TotalRecordForProcessPerCall must be at least 1.");
    }
    _totalRecordForProcessPerCall = totalRecordForProcessPerCall;
  }
  
  /**
   * Get the last activate time of the MBean. 
   * @return
   */
  public Date getLastActivateTime()
  {
    return _lastActivateTime;
  }
  
  private void setLastActivateTime(Date activateTime)
  {
    _lastActivateTime = activateTime;
  }
  
  /**
   * Get the last processed time of the MBean. It means the mbean has finished processing
   * the outstanding audit trail data record for the current batch. Or the exit time if
   * there is another MBean instance still processing. 
   * @return
   */
  public Date getLastEndProcessedTime()
  {
    return _lastEndProcessedTime;
  }
  
  private void setLastEndProcssedTime(Date lastProcessedTime)
  {
    _lastEndProcessedTime = lastProcessedTime;
  }

  public Properties getJndiProperties()
  {
    return _jndiProperties;
  }

  public void setJndiProperties(Properties pro)
  {
    _jndiProperties = pro;
  }
}
