/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessFailedJMSMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 28, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.gridtalk.httpbc.util.listener.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.util.IConstantValue;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.pdip.framework.notification.ProcessFailedJMSNotification;
import com.gridnode.util.jms.ejb.IJmsHandlerManagerLocalHome;
import com.gridnode.util.jms.ejb.IJmsHandlerManagerLocalObj;
import com.gridnode.util.jms.model.JMSFailedMsg;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class JMSFailedMsgRetryMDBean implements
                                   MessageDrivenBean,
                                   MessageListener
{
  /**
   * 
   */
  private static final long serialVersionUID = 4034933008067772660L;
  private static final String CLASS_NAME = "ProcessFailedJMSMDBean";
  private MessageDrivenContext _ctxt;
  private Logger _logger;
  
  public void ejbCreate()
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_UTIL, CLASS_NAME);
  }
  
  public void ejbRemove() throws EJBException
  {
  }


  public void setMessageDrivenContext(MessageDrivenContext ctxt) throws EJBException
  {
    _ctxt = ctxt;
  }

  public void onMessage(Message msg)
  {
    String method = "onMessage";
    _logger.debugMessage(method, null, "Receive failed notification. msg is "+msg);
    try
    {
      _logger.debugMessage(method, null, "ProcessFailedJMSMDBean: receive onMsg");
      if(! (msg instanceof ObjectMessage))
      {
        throw new IllegalArgumentException("Expecting ObjectMessage!");
      }
      
      ObjectMessage objMsg = (ObjectMessage)msg;
      Serializable obj = objMsg.getObject();
      
      if(obj == null || !(obj instanceof ProcessFailedJMSNotification))
      {
        throw new IllegalArgumentException("Expecting ProcessFailedJMSNotification!");
      }
      
      ProcessFailedJMSNotification notification = (ProcessFailedJMSNotification)objMsg.getObject();
      processFailedJMS(notification.getMaxRetry(), notification.getNumRecordFetched());
    }
    catch (NamingException e)
    {
      _logger.logError(ILogErrorCodes.PROCESS_FAILED_JMS,
                       method, null, "Failed to process Failed JMS: JNDIFinder initialisation failure", e);
    }
    catch (RemoteException e)
    {
      _logger.logError(ILogErrorCodes.PROCESS_FAILED_JMS,
                       method, null, "Failed to process Failed JMS: EJB invocation failure", e);
    }
    catch (CreateException e)
    {
      _logger.logError(ILogErrorCodes.PROCESS_FAILED_JMS,
                       method, null, "Failed to process Failed JMS: EJB creation failure", e);
    }
    catch(Throwable th)
    {
      _logger.logError(ILogErrorCodes.PROCESS_FAILED_JMS, method, null, "Failed to process Failed JMS: Unexpected error: "+th.getMessage(), th);
    }
  }
  
  /**
   * Retrieve those jms msg that is impacted by the clustering fail over and try to resend
   * those jms msg to their respective destination.
   * 
   * If the retry count of the failed jms has exceeded the given maxRetry, it will be deleted. 
   * @param maxRetry The maximum retry of sending the failed jms msg.
   * @param numRetrieve The num of record to be retrieved for handling.
   * @throws Exception
   */
  private void processFailedJMS(int maxRetry, int numRetrieve) throws Exception
  {
    String method = "processFailedJMS";
    IJmsHandlerManagerLocalObj jmsMgr = getJmsMgr();
    
    _logger.debugMessage(method, null, "Retrieve failed jms for category: "+IConstantValue.FAILED_JMS_CAT);
    Collection<JMSFailedMsg> failedMsgList = jmsMgr.retrieveFailedJMS(maxRetry, IConstantValue.FAILED_JMS_CAT, numRetrieve);
    
    if(failedMsgList != null && failedMsgList.size() > 0)
    {
      for(Iterator<JMSFailedMsg> ite = failedMsgList.iterator(); ite.hasNext(); )
      {
        JMSFailedMsg failedMsg = ite.next();
        try
        {
          jmsMgr.handleFailedJMS(failedMsg);
        }
        catch(Exception ex)
        {
          jmsMgr.updateFailedJMS(failedMsg, maxRetry);
          _logger.logError(ILogErrorCodes.PROCESS_FAILED_JMS, method, null, "Error in handling the failed jms: "+failedMsg, ex);
        }
      }
    }
  }
  
  private IJmsHandlerManagerLocalObj getJmsMgr() throws Exception 
  {
    JndiFinder finder = new JndiFinder(null);
    IJmsHandlerManagerLocalHome home = (IJmsHandlerManagerLocalHome)finder.lookup(
                                                IJmsHandlerManagerLocalHome.class.getName()+"_HC", IJmsHandlerManagerLocalHome.class);
    return home.create();
  }
}
