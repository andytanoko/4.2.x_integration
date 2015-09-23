/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransactionFlowMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27, 2006    Tam Wei Xiang       Created
 * Mar 07 2007		Alain Ah Ming				Catch IllegalArgumentException and log as warning
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 * Jul 30, 2008   Tam Wei Xiang       #69:1) Remove explicitly checked for redeliverd msg and dropped that msg.
 *                                        2) Rollback the entire transaction if encounter retryable exception. 
 */
package com.gridnode.gtas.audit.extraction.listeners.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.extraction.ProcessNotificationHandler;
import com.gridnode.gtas.audit.extraction.exception.AuditInfoCollatorException;
import com.gridnode.gtas.audit.extraction.helper.IGTATConstant;
import com.gridnode.gtas.server.document.notification.DocumentTransactionNotification;
import com.gridnode.pdip.app.workflow.notification.ProcessTransactionNotification;
import com.gridnode.pdip.framework.notification.AbstractNotification;
import com.gridnode.pdip.framework.notification.DocumentFlowNotification;
import com.gridnode.util.exceptions.JMSFailureException;
import com.gridnode.util.jms.JMSRedeliveredHandler;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class will listen to the NotifierTopic. It will delegate the AbstractNotification to 
 * ProcessNotificationHandler for processing. 
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class TransactionFlowMDBean
  implements MessageDrivenBean, MessageListener
{
  /**
   * 
   */
  private static final long serialVersionUID = -8618248727052623983L;

  private MessageDrivenContext _ctxt;
  private static final String CLASS_NAME = "TransactionFlowMDBean";
  private Logger _logger = null;
  
  public void setMessageDrivenContext(MessageDrivenContext ctx)
  {
    _ctxt = ctx;
  }
  
  public void onMessage(Message message)
  {
  	String mn = "onMessage";
    _logger.debugMessage(mn, null, "TransactionFlowMDBean Entered !");
    Object[] params = new Object[]{message};
    String method = "onMessage";
    _logger.logEntry(method, params);
    String jmsMessageID = "";  
    
    try
    {
      jmsMessageID = message.getJMSMessageID();
      if(message.getJMSRedelivered())
      {
        _logger.logMessage(mn, null, "Redelivered msg found. Message: "+message);
      }
      
      if(! (message instanceof ObjectMessage))
      {
        //a Logger for Audit-Trail component
        throw new IllegalArgumentException("Expecting Message type is ObjectMessage !");
      }
      Object obj = ((ObjectMessage)message).getObject();
    
      if( obj instanceof AbstractNotification)
      {
        processNotification((AbstractNotification)obj);
      }
      else
      {
        throw new IllegalArgumentException("Expecting INotification obj !");
      }
    }
    catch(IllegalArgumentException e)
    {
    	_logger.logWarn(mn, null ,"No action taken: "+e.getMessage(), e);
    }
    catch(JMSException ex)
    {
      _logger.logError(ILogErrorCodes.AT_TXFLOW_MDB_ONMESSAGE_ERROR,
                       method, params, "Failed to read request: "+ex.getMessage(), ex);
    }
    catch(Exception ex)
    {
      _logger.logError(ILogErrorCodes.AT_TXFLOW_MDB_ONMESSAGE_ERROR,
                       method, params, "Failed to process notification: "+ex.getMessage(), ex);
      
      if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 rollback the entire transaction if encounter JMS related error
      {
        _logger.logWarn(mn, null, "encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID, ex);
        _ctxt.setRollbackOnly();
      }
    }
    finally
    {
      _logger.logExit(method, params);
    }
  }
  
  private void processNotification(AbstractNotification notification) throws AuditInfoCollatorException
  {
    Object[] params = new Object[]{notification};
    String method = "processNotification";
    
    if(notification instanceof DocumentFlowNotification || notification instanceof DocumentTransactionNotification ||notification instanceof ProcessTransactionNotification)
    {
      ProcessNotificationHandler.getInstance().handleNotification(notification);
    }
    else
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".processNotification] The AbstractNotification "+notification.toString()+" is not supported !");
    }
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IGTATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  public void ejbRemove()
  {
    
  }
  
  public void ejbCreate()
  {
    _logger = getLogger();
  }
}
