/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReProcessPIActivityServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 24, 2007    Tam Wei Xiang       Created
 * May 18, 2007    Tam Wei Xiang       The in appropriate handling in the exception.
 *                                     Some exception will not be captured in the 'Reprocess Doc'
 *                                     event.
 */
package com.gridnode.gtas.audit.reprocess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.IReprocessConstant;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.gtas.audit.reprocess.exception.ReprocessActivityException;
import com.gridnode.gtas.audit.reprocess.helper.ReprocessHelper;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This servlet help to validate whether the reprocess request from the TM user is allowed or not. If the request
 * is permitted, the servlet will send the reprocess request to GT using queue.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ReProcessPIActivityServlet extends HttpServlet
{
  private static final String CLASS_NAME = "ReProcessPIActivityServlet";
  private Logger _logger;
  
  public void init() throws ServletException
  {
    _logger = getLogger();
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    doPost(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {    
  	String mn = "doPost";
    String activityType = request.getParameter(IReprocessServletConstant.ACTIVITY_TYPE);
    String tracingID = request.getParameter(IReprocessServletConstant.TRACING_ID);
    String processUID = request.getParameter(IReprocessServletConstant.PROCESS_UID);
    String userName = request.getParameter(IReprocessServletConstant.USER_NAME);
    String operationName = request.getParameter(IReprocessServletConstant.OPERATION_NAME); //to faciliate the UI display
    
    _logger.debugMessage("doPost", null, "activityType :"+activityType+"username from TM: "+ userName+" tracingID :"+tracingID+" processUID :"+processUID+" operationName: "+operationName);
    
    String errMsg = "";
    boolean isSuccess = true;
    String msgForClient = "";
    boolean isAllowedReprocess = true;
    
    try
    {
      if(activityType != null && ! "".equals(activityType))
      {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(IReprocessConstant.USER_NAME, userName);
      
        if(IReprocessServletConstant.ACTIVITY_REPROCESS_ACTIVITY_TRACE_EVENTS.equals(activityType))
        {
          assertParam(tracingID, IReprocessServletConstant.TRACING_ID);
          String[] validateResult = null;
        
          try
          {
            validateResult = ReprocessHelper.isAllowedReprocess(tracingID);
          }
          catch(Exception ex)
          {
            msgForClient = IReprocessServletConstant.STATUS_INITIATE_FAIL;
            _logger.logWarn(mn, null, "Fail to validate the reprocess request for tracingID: "+tracingID, ex);
//            _logger.logWarn("Failed to validate the reprocess request. Error: "+ex.getMessage(), ex);
            throw ex;
          }
        
          isAllowedReprocess = new Boolean(validateResult[0]);
          if(! isAllowedReprocess)
          {
            msgForClient = validateResult.length > 1 ? validateResult[1] : "";
            _logger.debugMessage(mn, null, "Request for reprocess tracingID "+tracingID+" isDisallowed.");
            throw new ReprocessActivityException("Request for reprocessing was denied. "+ReprocessHelper.getErrCodeExplanation(msgForClient));
          }
          else
          {
            map.put(IReprocessConstant.TRACING_ID, tracingID);
            map.put(IReprocessConstant.ACTIVITY_TYPE, activityType);
          }
        }
        else if(IReprocessServletConstant.ACTIVITY_CANCEL_PROCESS.equals(activityType))
        {
          assertParam(processUID, IReprocessServletConstant.PROCESS_UID);
          map.put(IReprocessConstant.PROCESS_UID, processUID);
          map.put(IReprocessConstant.ACTIVITY_TYPE, activityType);
        }
        else
        {
          throw new IllegalArgumentException("Reprocess Activity "+activityType+" is not supported !");
        }
                
        msgForClient = IReprocessServletConstant.STATUS_SUCCESS_SEND_TO_GT;
        try
        {
          sendToQueue(map);
        }
        catch(Exception ex)
        {
          msgForClient = IReprocessServletConstant.STATUS_FAIL_SEND_TO_GT;
          _logger.logWarn(mn, null, "["+CLASS_NAME+".doPost] Error in sending request to queue for re-processing.", ex);
          throw ex;
        }
      }
    }
		catch (ReprocessActivityException e)
		{
		  errMsg = e.getMessage();
      isSuccess = false;
      if(isAllowedReprocess) //If !isAllowedReprocess, meaning from application state of view, the request is disallowed, we dun need to send
                             //the error
			{
        _logger.logError(ILogErrorCodes.AT_GENERIC_HTTP_RESPONSE_POSTING_ERROR,mn, null, "Unable to handle reprocess document request. TracingID is "+tracingID+" "+e.getMessage(), e);     
      }
		}
    catch(Exception ex)
    {
      errMsg = ex.getMessage();
      isSuccess = false;
      String id = IReprocessServletConstant.ACTIVITY_REPROCESS_ACTIVITY_TRACE_EVENTS.equals(activityType) ? tracingID : processUID;
      
      _logger.logError(ILogErrorCodes.AT_GENERIC_HTTP_RESPONSE_POSTING_ERROR,
                       "doPost", null, "Failed to handle activity: "+activityType+" tracingID/processID : "+id+". Error: "+ex.getMessage(), ex);  
    }
    finally
    {
      if(IReprocessServletConstant.ACTIVITY_REPROCESS_ACTIVITY_TRACE_EVENTS.equals(activityType))
      {
        try
        {
          ReprocessHelper.triggerReprocessEvent(IAuditTrailConstant.REPROCESS_DOC, userName, isSuccess, errMsg, tracingID);
        }
        catch(Exception ex)
        {
          _logger.logError(ILogErrorCodes.AT_GENERIC_HTTP_RESPONSE_POSTING_ERROR,
                           "doPost", null, "Error in triggering reprocess event with tracing id "+tracingID, ex);
        }
      }
      handleHttpResponse(response, msgForClient, operationName); 
    }
  }
  
  
  private void sendToQueue(HashMap<String,String> map) throws Exception
  {
    JmsSender sender = new JmsSender();
    sender.setSendProperties(getJMSSenderProperties());
    sender.send(map);
  }
  
  /*
  private Properties getJMSSenderProperties()
  {
    Properties pro = new Properties();
    pro.setProperty(JmsSender.DESTINATION, IISATConstant.REPROCESS_Q_JNDI);
    pro.setProperty(JmsSender.CONN_FACTORY, IISATConstant.CONNECTION_FACTORY_JNDI);
    return pro;
  }*/
  
  private Properties getJMSSenderProperties() throws Exception
  {
    IAuditPropertiesManagerObj propertiesMgr = getPropertiesMgr();
    Properties pro = propertiesMgr.getAuditProperties(IISATProperty.REPROCESS_JMS_CATEGORY);
    return pro;
  }
  
  private IAuditPropertiesManagerObj getPropertiesMgr() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditPropertiesManagerHome home = (IAuditPropertiesManagerHome)finder.lookup(IAuditPropertiesManagerHome.class.getName(),
                                                                                  IAuditPropertiesManagerHome.class);
    return home.create();
  }
  
  /**
   * Append the status of the reprocess request, and delegate back to the UI for rendering
   * @param statusCode the status of the reprocessing request
   * @param operationName the name of the operation that passed from UI. Require it while sending the response back to UI.
   */
  private void handleHttpResponse(HttpServletResponse servletResponse, String statusCode, String operationName) throws IOException
  {
    String responseURL = ReprocessHelper.getResponseURL();
    if(responseURL == null)
    {
      throw new NullPointerException("["+CLASS_NAME+".handleHttpResponse] Response URL is null. Pls initialize it in DB.");
    }
    servletResponse.sendRedirect(responseURL+"?"+IReprocessServletConstant.STATUS_CODE+"="+statusCode
                                                                  +"&"+IReprocessServletConstant.OPERATION_NAME+"="+operationName);
  }
  
  private void assertParam(String paramValue, String paramName)
  {
    if(paramValue == null)
    {
      throw new IllegalArgumentException("Expecting the requested param "+paramName+" to contain value !");
    }
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
}
