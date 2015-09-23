/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: IncomingTransactionMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 3, 2009    Tam Wei Xiang       Created
 */
package com.gridnode.gridtalk.httpbc.ishb.ejb;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.InitialContext;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.util.IConstantValue;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.gridtalk.httpbc.common.util.LockUtil;
import com.gridnode.gridtalk.httpbc.ishb.model.TxDeliveryInfo;
import com.gridnode.gridtalk.httpbc.ishb.workers.TransactionDeliveryConfigStore;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * The IncomingTransactionMDBean will process the request for delivering the document to
 * the customer backend system.
 * 
 * It is designed such that only mdbeans in the cluster main node will process the request (by leveraging
 * md bean selector). It is potential that the md bean from two node will process the request concurently;
 * hence the DB lock is acquired to prevent such scenario and always ensure only one md bean performing
 * the sending of the document at any one time. 
 * 
 * @author Tam Wei Xiang
 * @version GT4.1.4.2
 * @since GT4.1.4.2
 */
public class IncomingTransactionMDBean implements MessageListener, MessageDrivenBean
{

  /**
   * 
   */
  private static final long serialVersionUID = 7719560245536500817L;
  private MessageDrivenContext _ctxt;
  private Logger _logger;

  public void onMessage(Message msg)
  {
    String method = "onMessage";
    if(msg instanceof ObjectMessage)
    {
      LockUtil lockUtil = LockUtil.getInstance();
      boolean isLockLocal = false;
      
      try
      {
        if(msg.getJMSRedelivered())
        {
          _logger.logMessage(method, null, "Redelivered msg found. Ignored it. Message: "+msg);
          return;
        }
        
        ObjectMessage objMsg = (ObjectMessage)msg;
        Object obj = objMsg.getObject();
        
        if(obj instanceof TxDeliveryInfo)
        {  
          TxDeliveryInfo deliverInfo = (TxDeliveryInfo)obj;
          TransactionDeliveryConfigStore deliverConfig = TransactionDeliveryConfigStore.getInstance();
          int maxProcessCountPerCall = deliverConfig.getMaxProcessCountPerCall(deliverInfo.getMaxProcessCountPerCall());
          int maxFailedAttemptsPerTx = deliverConfig.getMaxFailedAttemptsPerTx(deliverInfo.getMaxFailedAttemptsPerTx());
          int failedAttemptsAlertThreshold = deliverConfig.getFailedAttemptsAlertThreshold(deliverInfo.getFailedAttemptsAlertThreshold());
          _logger.debugMessage(method, null, "maxProcessCount: "+maxProcessCountPerCall+", maxFailedAttempts: "+maxFailedAttemptsPerTx+", failedAttemptAlertThreshold: "+failedAttemptsAlertThreshold);
          
          //Get a local lock
          if(! lockUtil.acquireLock(IConstantValue.INCOMING_TRANSACTION_LOCK))
          {
            _logger.logMessage(method, null, "IncomingTransaction handler is currently processing the doc, ignored.");
            return;
          }
          else
          {
            //_logger.debugMessage(method, null, "IncomingTransaction handler got lock successfully.");
            isLockLocal = true;
          }
          
          ConfigurationStore store = ConfigurationStore.getInstance();
          if(!store.lockProperty(IConstantValue.INCOMING_TRANSACTION_LOCK, IConstantValue.INCOMING_TRANSACTION_LOCK))
          {
            _logger.logMessage(method, null, "Not able to obtain global lock. IncomingTransaction handler is currently processing the record, ignored.");
            return;
          }
          
          //NSL20070305 Control the number to process here.
          int count = 0;
          boolean stopHandling = count >= maxProcessCountPerCall;
          while (!stopHandling)
          {
            boolean success = getTxHandler(deliverConfig).deliverIncomingTransaction(maxFailedAttemptsPerTx, failedAttemptsAlertThreshold);
            stopHandling = (!success || (++count >= maxProcessCountPerCall));
          }
        }
        else
        {
          _logger.debugMessage(method, null, "ObjectMessage is not processed, expecting TxDeliveryInfo!");
        }
      }
      catch(JMSException ex)
      {
        _logger.logError(ILogErrorCodes.INCOMING_TRANS_MDBEAN,
                         method, null, "Failed to read request: "+ex.getMessage(), ex);
      }
      catch(Throwable th)
      {
        //TODO: Handle clustering code here
        _logger.logError(ILogErrorCodes.INCOMING_TRANS_MDBEAN,
                         method, null, "Failed to process incoming transaction: "+th.getMessage(), th);
      }
      finally
      {
        if(isLockLocal)
        {
          lockUtil.releaseLock(IConstantValue.INCOMING_TRANSACTION_LOCK);
          _logger.debugMessage(method, null, "IncomingTransaction handler, local lock released.");
        }
      }
    }
    else
    {
      _logger.debugMessage(method, null, "Message is not processed, expecting ObjectMessage!");
    }
  }
  
  private ITransactionHandler getTxHandler(TransactionDeliveryConfigStore configStore) throws Exception
  {
    InitialContext ctx = new InitialContext(configStore.getJndiProps());
    ITransactionHandlerHome home = (ITransactionHandlerHome)ctx.lookup(configStore.getTxDeliveryMgrJndi());
    return home.create();
  }

  public void ejbRemove() throws EJBException
  {
  }

  public void setMessageDrivenContext(MessageDrivenContext arg0) throws EJBException
  {
    _ctxt = arg0;
  }

  public void ejbCreate()
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHB, "IncomingTransactionMDBean");
  } 
}
