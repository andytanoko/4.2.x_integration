/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSFailedMsgRetryMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.jms.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.framework.db.jms.JMSFailedMsg;
import com.gridnode.pdip.framework.db.jms.JMSFailedMsgHelper;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.notification.AbstractNotification;
import com.gridnode.pdip.framework.notification.ProcessFailedJMSNotification;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This MD Bean will listen to the ProcessFailedJMSNotification and it will invoke the JMSFailedMsg
 * handler to process the failed jms.
 *  
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
  private static final long serialVersionUID = -3197212671841091080L;
  private MessageDrivenContext _ctxt;
  private static final String LOG_CAT = "JMSFailedMsgRetryMDBean";
  
  public void onMessage(Message message)
  {
    
    try
    {
      Log.debug(LOG_CAT, "Receive msg is: "+message);
      
      if(! (message instanceof ObjectMessage))
      {
        throw new IllegalArgumentException("Expecting Message type is ObjectMessage !");
      }
      
      Object obj = ((ObjectMessage)message).getObject();
      if( obj instanceof ProcessFailedJMSNotification)
      {
        processFailedJMS((ProcessFailedJMSNotification)obj);
      }
      else
      {
        throw new IllegalArgumentException("Expecting ProcessFailedJMSNotification obj !");
      }
    }
    catch(Throwable ex)
    {
      Log.error(ILogErrorCodes.FAILED_JMS_PROCESSED_ERROR, LOG_CAT, "Error in invoking failed jms handler", ex);
    }
  }
  
  public void ejbCreate()
  {
    
  }
  
  public void ejbRemove() throws EJBException
  {

  }

  public void setMessageDrivenContext(MessageDrivenContext ctxt) throws EJBException
  {
    _ctxt = ctxt;
  }
  
  private void processFailedJMS(ProcessFailedJMSNotification notification) throws Exception
  {
    Log.debug(LOG_CAT, "Processing failed JMS");
    int maxRetry = notification.getMaxRetry();
    int numRetrieve = notification.getNumRecordFetched();
    
    Long processedUID = new Long(-999);
    IJMSMsgHandlerObj jmsMgr = getJMSMgr();
    
    for(int i = 0; i < numRetrieve; i++)
    {
      JMSFailedMsg failedMsg = jmsMgr.getNextFailedMsg(maxRetry, processedUID);
      if(failedMsg == null)
      {
        Log.debug(LOG_CAT, "No failed msg found.");
        break;
      }
      else
      {
        try
        {
          Log.debug(LOG_CAT, "Load nth: "+i+" failed jms msg: "+failedMsg);
          processedUID = failedMsg.getUid();
          jmsMgr.processFailedJMS(failedMsg);
        }
        catch(Exception ex)
        {
          jmsMgr.updateFailedJMSMsg(failedMsg, maxRetry);
          Log.error(ILogErrorCodes.FAILED_JMS_PROCESSED_ERROR, LOG_CAT, "Failed JMS is processed unsucessfully", ex);
        }
      }
    }
  }
  
  private IJMSMsgHandlerObj getJMSMgr() throws ServiceLookupException, RemoteException, CreateException
  {
    IJMSMsgHandlerHome home = (IJMSMsgHandlerHome)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
                                          IJMSMsgHandlerHome.class.getName(), IJMSMsgHandlerHome.class);
    return home.create();
  }
}
