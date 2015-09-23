/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveTransactionMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Jan 16 2007    i00107              Use TransactionHandlerBean instead of TransactionHandler
 *                                    to handle transactions.
 * Feb 24 2007    i00107              Set timestamp if not set.        
 * Mar 05, 2007		Alain Ah Ming				Added error code to error logs   
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.                        
 */

package com.gridnode.gridtalk.httpbc.ishb.ejb;

import java.io.Serializable;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.IAlertKeys;
import com.gridnode.gridtalk.httpbc.common.util.IJndiNames;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.alert.AlertUtil;
import com.gridnode.util.jms.JMSRedeliveredHandler;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This message driven bean is used to listen for transactions that are
 * coming from or going to the trading partners.
 */
public class SaveTransactionMDBean implements
                                      MessageListener,
                                      MessageDrivenBean
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -6550610429396308287L;
  private MessageDrivenContext _ctx = null;
  
  private Logger _logger = null;

  public void ejbCreate()
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHB, "SaveTransactionMDBean");
  }
  
  /**
   * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
   */
  public void onMessage(Message msg)
  {
    String mtdName = "onMessage";
    
    if (msg instanceof ObjectMessage)
    {
      ObjectMessage objMsg = (ObjectMessage)msg;
      TransactionDoc tDoc = null;
      try
      {
        if(msg.getJMSRedelivered() && ! JMSRedeliveredHandler.isEnabledJMSRedelivered())
        {
          _logger.logMessage(mtdName, null, "Redelivered msg found, ignored it. Message: "+msg);
          return;
        }
        
        Serializable ser = objMsg.getObject();
        if (ser != null && ser instanceof TransactionDoc)
        {
          tDoc = (TransactionDoc)ser;
          
          if (tDoc.getTimestamp() == 0)
          {
            tDoc.setTimestamp(System.currentTimeMillis());
          }
          String txNo;
          ITransactionHandler handler = lookupHandler();
          if (TransactionDoc.DIRECTION_IN.equals(tDoc.getDirection()))
          {
            _logger.debugMessage(mtdName, null, "doc="+tDoc.getDocContent());
            txNo = handler.handleIncomingTransaction(tDoc);
          } 
          else if (TransactionDoc.DIRECTION_OUT.equals(tDoc.getDirection()))
          {
            txNo = handler.handleOutgoingTransaction(tDoc);
          }
          else
          {
            _logger.logMessage(mtdName, null, "Unsupported Direction: "+tDoc.getDirection());
            return;
          }
          
          String logmsg = "Saved Tx: txNo="+txNo;
          _logger.logMessage(mtdName, null, logmsg);
        }
      }
      catch (Throwable t)
      {
        _logger.logError(ILogErrorCodes.TRANSACTION_SAVE, mtdName, null, "Unexpected Error while handling transactions: "+t.getMessage(), t);
        AlertUtil.getInstance().sendAlert(IAlertKeys.UNEXPECTED_SYSTEM_ERROR, 
                                          ILogTypes.TYPE_HTTPBC_ISHB, 
                                          "SaveTransactionMDBean",
                                          mtdName,
                                          "Unexpected Error while handling transaction: ["+tDoc+"]", 
                                          ExceptionUtil.getStackStraceStr(t));
      }
    }

  }

  private ITransactionHandler lookupHandler() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    ITransactionHandlerHome home = (ITransactionHandlerHome)finder.lookup(IJndiNames.TRANSACTION_HANDLER, ITransactionHandlerHome.class);
    return home.create();
  }
  
  /**
   * @see javax.ejb.MessageDrivenBean#ejbRemove()
   */
  public void ejbRemove()
  {
    _ctx = null;
    _logger = null;
  }

  /**
   * @see javax.ejb.MessageDrivenBean#setMessageDrivenContext(javax.ejb.MessageDrivenContext)
   */
  public void setMessageDrivenContext(MessageDrivenContext arg0) throws EJBException
  {
    _ctx = arg0;
  }

}
