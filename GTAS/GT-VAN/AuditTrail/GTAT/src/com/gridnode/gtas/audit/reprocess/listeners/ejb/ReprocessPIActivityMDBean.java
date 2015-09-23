/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReprocessDocMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 19, 2007    Tam Wei Xiang       Created
 * Mar 07 2007		Alain Ah Ming				Catch illegal argument exception, log warning and do nothing
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 */
package com.gridnode.gtas.audit.reprocess.listeners.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.gridnode.gtas.audit.common.IReprocessConstant;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.extraction.helper.IGTATConstant;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerHome;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.util.jms.JMSRedeliveredHandler;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to receive the msg from queue 'gtvan/gtat/reprocessDocQueue'. Based on the activity type,
 * we will re-process the activity for example we will restart a particular partner function, cancel a particular
 * ProcessInstance, etc.
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ReprocessPIActivityMDBean implements MessageListener, MessageDrivenBean
{
  /**
   * 
   */
  private static final long serialVersionUID = 5660130638400670458L;
  private static final String CLASS_NAME = "ReprocessPIActivityMDBean";
  private MessageDrivenContext _ctxt;
  private Logger _logger;
  
  public void onMessage(Message msg)
  {
    String mn = "onMessage";
    String activityType = "";
    if(msg instanceof MapMessage)
    { 
      MapMessage mapMsg = (MapMessage)msg;
      
      try
      {
        if(msg.getJMSRedelivered() && ! JMSRedeliveredHandler.isEnabledJMSRedelivered())
        {
          _logger.logMessage(mn, null, "Redelivered msg found, ignored it. Message: "+msg);
          return;
        }
        
        activityType = mapMsg.getString(IReprocessConstant.ACTIVITY_TYPE);
        String userName = mapMsg.getString(IReprocessConstant.USER_NAME);
        
          if(IReprocessConstant.ACTIVITY_REPROCESS_ACTIVITY_TRACE_EVENTS.equals(activityType))
          {
            String tracingID = mapMsg.getString(IReprocessConstant.TRACING_ID);
            reprocessDoc(tracingID);
          }
          else if(IReprocessConstant.ACTIVITY_CANCEL_PROCESS.equals(activityType))
          {
            String processUID = mapMsg.getString(IReprocessConstant.PROCESS_UID);
            cancelProcessInstance(processUID, userName);
          }
          else
          {
            throw new IllegalArgumentException("["+CLASS_NAME+".onMessage] Activity Type "+activityType+" is not supported !");
          }
      }
      catch(IllegalArgumentException e)
      {
      	_logger.logWarn(mn, null, "Illegal argument: "+e.getMessage(), e);
      }
      catch(JMSException ex)
      {
        _logger.logError(ILogErrorCodes.AT_REPROCESS_MDB_ONMESSAGE_ERROR,
                         mn, null, "Failed to read request: "+ex.getMessage(), ex);
      }
      catch(Exception ex)
      {
        _logger.logError(ILogErrorCodes.AT_REPROCESS_MDB_ONMESSAGE_ERROR,
                         mn, null, "Failed to reprocess activity type("+activityType+") : "+ex.getMessage(), ex);
      }
    }
  }

  public void ejbRemove() throws EJBException
  {
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IGTATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  public void ejbCreate()
  {
    _logger = getLogger();
  }
  
  public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException
  {
    _ctxt = ctx;
  }
  
  /**
   * Reprocess the doc as identified by the tracingID.
   * @param tracingID
   * @throws Exception 
   * @throws Exception
   */
  private void reprocessDoc(String tracingID) throws Exception
  {
    try
		{
			getDocMgr().reprocessDoc(tracingID);
		}
		catch (ServiceLookupException e)
		{
			throw new Exception("Service lookup failure. Tracing ID :"+tracingID,e);
		}
		catch (ApplicationException e)
		{
			throw e;
		}
		catch (RemoteException e)
		{
			throw new Exception("EJB invocation failure. Tracing ID "+tracingID,e);
		}
  }
  
  private void cancelProcessInstance(String processUID, String userName) throws Exception
  {
    try
		{
			getRnifMgr().cancelProcess(new Long(processUID), "User Cancelled", userName);
		}
		catch (ServiceLookupException e)
		{
			throw new Exception("Service lookup failure. Process UID "+processUID,e);
		}
		catch (NumberFormatException e)
		{
			throw new Exception("Invalid Process UID format. Process UID "+processUID, e);
		}
		catch (RemoteException e)
		{
			throw new Exception("EJB invocation failure. Process UID "+processUID, e);
		}
		catch (Exception e)
		{
			throw new Exception("Error in cancelling process instance with process UID "+processUID, e);
		}
  }
  
  private IDocumentManagerObj getDocMgr() throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
                            ServiceLocator.CLIENT_CONTEXT).getObj(
                            IDocumentManagerHome.class.getName(),
                            IDocumentManagerHome.class,
                            new Object[0]);
  }
  
  private IRnifManagerObj getRnifMgr() throws ServiceLookupException
  {

		return (IRnifManagerObj)ServiceLocator.instance(
			                                                  ServiceLocator.CLIENT_CONTEXT).getObj(
			                                                  IRnifManagerHome.class.getName(),
			                                                  IRnifManagerHome.class,
			                                                  new Object[0]);

  }
}