/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReprocessHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 2, 2007    Tam Wei Xiang       Created
 * May 03, 2007   Tam Wei Xiang       The reprocess event will be directly insert via
 *                                    AuditTrailManagerBean instead of sending to
 *                                    localEventQ
 */
package com.gridnode.gtas.audit.reprocess.helper;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.common.model.EventInfo;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.gtas.audit.reprocess.IReprocessServletConstant;
import com.gridnode.gtas.audit.reprocess.exception.ReprocessActivityException;
import com.gridnode.gtas.audit.reprocess.facade.ejb.IReprocessManagerHome;
import com.gridnode.gtas.audit.reprocess.facade.ejb.IReprocessManagerObj;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerHome;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * It provide the services to handling the reprocessing operation. 
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ReprocessHelper
{
  private static String CLASS_NAME = "ReprocessHelper";
  private static Logger _logger = getLogger();
  
  /**
   * 
   * @param tracingID
   * @return
   * @throws Exception 
   */
  public static String[] isAllowedReprocess(String tracingID) throws Exception 
  {
    String methodName = "isAllowedReprocess";
    String[] param = new String[]{tracingID};
    _logger.logEntry(methodName, param);
    
    IReprocessManagerObj mgr;
		try
		{
			mgr = getReprocessMgr();
		}
		catch (RemoteException e)
		{
			throw new Exception("EJB Invocation Error. tracingID :"+tracingID, e);
		}
		catch (NamingException e)
		{
			throw new Exception("JNDI Lookup Error tracingID :"+tracingID, e);
		}
		catch (CreateException e)
		{
			throw new Exception("EJB Creation Error tracingID :"+tracingID, e);
		}
    try
		{
			return mgr.isAllowedReprocessDoc(tracingID);
		}
		catch (RemoteException e)
		{
			throw new Exception("EJB Invocation Error tracingID :"+tracingID, e);
		}
		catch (ReprocessActivityException e)
		{
			throw e;
		}
  }
  
  /**
   * Trigger the Reprocess Event to Local Event Q.
   * @param eventName
   * @param eventRemark
   * @param isSuccess
   * @param errorReason
   * @param tracingID
   * @throws Exception
   */
  public static void triggerReprocessEvent(String eventName, String eventRemark, boolean isSuccess,
                                           String errorReason, String tracingID) throws Exception
  {

    try
    {
      String methodName = "triggerReprocessEvent";
      _logger.logEntry(methodName, null);
      
      IReprocessManagerObj reprocessMgr = getReprocessMgr();
      String beID = reprocessMgr.getBeID(tracingID);
    
      String messageID = reprocessMgr.getTracingIDCorrespondFirstInsertMsgID(tracingID);
      String eventType = "";
      String status = isSuccess ? IAuditTrailConstant.STATUS_SUCCESS: IAuditTrailConstant.STATUS_FAIL;
      eventRemark = addPrefixToReprocessEvent(eventRemark);
      EventInfo info = new EventInfo(eventName, new Date(), messageID, status, errorReason, tracingID, eventType,
                                   beID, eventRemark);
      
      BusinessDocument[] businessDoc = null;
      getAuditTrailManagerObj().handleAuditTrailData(new AuditTrailData(info, false, businessDoc));
      _logger.logExit(methodName, null);
    }
    catch(Exception ex)
    {
      _logger.logWarn("", null, "Error in triggering the reprocess event for tracingID: "+tracingID+" eventName: "+eventName, ex);
      throw ex;
    }
  }
  
  private static String addPrefixToReprocessEvent(String eventRemark)
  {
    if(eventRemark != null && ! "".equals(eventRemark))
    {
      return "Reprocess By:"+eventRemark;
    }
    return eventRemark;
  }
  
  public static String getErrCodeExplanation(String errCode)
  {
    if(IReprocessServletConstant.STATUS_REPROCESS_DENIED_NO_IB.equals(errCode))
    {
      return IReprocessServletConstant.EXP_REPROCESS_DENIED_NO_IB;
    }
    else if(IReprocessServletConstant.STATUS_REPRPOCESS_DENIED_HAS_OB.equals(errCode))
    {
      return IReprocessServletConstant.EXP_REPRPOCESS_DENIED_HAS_OB;
    }
    else if(IReprocessServletConstant.STATUS_REPROCESS_DENIED_HAS_HTTP_BC_EVENT.equals(errCode))
    {
      return IReprocessServletConstant.EXP_REPROCESS_DENIED_HAS_HTTP_BC;
    }
    else
    {
      //throw new IllegalArgumentException("["+CLASS_NAME+".getErrCodeExplanation] The errCode "+errCode+" is not recognized !");
      return "";
    }
  }
  
  public static String getResponseURL()
  {
    /*
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    String responseURL = configStore.getProperty(IISATProperty.CATEGORY, IISATProperty.RESPONSE_URL, null);
    return responseURL; */
    try
    {
      return getReprocessMgr().getResponseURL();
    }
    catch(Exception ex)
    {
      _logger.logWarn("", null, "Error getting response url", ex);
      return null;
    }
  }
  
  /**
   * 
   * @return
   * @throws NamingException
   * @throws RemoteException
   * @throws CreateException
   */
  private static IReprocessManagerObj getReprocessMgr() throws NamingException, RemoteException, CreateException
  {
    JndiFinder jndi = new JndiFinder(null);
    IReprocessManagerHome home = (IReprocessManagerHome)jndi.lookup(IReprocessManagerHome.class.getName(),IReprocessManagerHome.class);
    return home.create();
  }
  
  private static IAuditTrailManagerObj getAuditTrailManagerObj() throws NamingException, RemoteException, CreateException
  {
    JndiFinder jndi = new JndiFinder(null);
    IAuditTrailManagerHome home = (IAuditTrailManagerHome)jndi.lookup(IAuditTrailManagerHome.class.getName(), IAuditTrailManagerHome.class);
    return home.create();
  }
  
  private static Properties getJMSSenderProperties() throws Exception
  {
    IAuditPropertiesManagerObj propertiesMgr = getPropertiesMgr();
    Properties pro = propertiesMgr.getAuditProperties(IISATProperty.LOCAL_EVENT_Q_JMS_CATEGORY);
    return pro;
  }
  
  private static IAuditPropertiesManagerObj getPropertiesMgr() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditPropertiesManagerHome home = (IAuditPropertiesManagerHome)finder.lookup(IAuditPropertiesManagerHome.class.getName(),
                                                                                  IAuditPropertiesManagerHome.class);
    return home.create();
  }
 
  private static Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
}
