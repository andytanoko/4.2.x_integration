/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Feb 24 2007    i00107              Reversed sender/receiver setting.
 */

package com.gridnode.gridtalk.httpbc.ishc;

import java.util.Hashtable;
import java.util.Properties;
import java.util.UUID;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.model.FileContent;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.IConfigCats;
import com.gridnode.gridtalk.httpbc.common.util.IJndiNames;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.gridtalk.httpbc.ishb.ejb.ITransactionHandler;
import com.gridnode.gridtalk.httpbc.ishb.ejb.ITransactionHandlerHome;
import com.gridnode.util.StringUtil;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * Delegate for handling requests from Backend
 */
public class ReceiveDelegate
{
  private static ReceiveDelegate _self = null;
  private Logger _logger;
  
  private ReceiveDelegate()
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHC, "ReceiveDelegate");
  }
  
  public static final synchronized ReceiveDelegate getInstance()
  {
    if (_self == null)
    {
      _self = new ReceiveDelegate();
    }
    return _self;
  }
  
  public String receive(Hashtable<String,String> headers, byte[] content)
  {
    String partnerId = headers.get(ITransactionHeaderConstants.RECEIVER_IDENTITY);
    String custBeId = headers.get(ITransactionHeaderConstants.SENDER_IDENTITY);
    String docType = headers.get(ITransactionHeaderConstants.DOCUMENT_TYPE);
    String filename = headers.get(ITransactionHeaderConstants.FILE_NAME);
    if (filename == null)
    {
      filename = generateFilename(custBeId, partnerId, docType);
    }
    FileContent fc = new FileContent(filename, content);
    _logger.debugMessage("receive",null, "headers="+headers);
    _logger.debugMessage("receive",null, "payload="+fc);
    String tracingId = generateTracingId();
    TransactionDoc tDoc = new TransactionDoc(tracingId, partnerId, custBeId, docType, fc, null, TransactionDoc.DIRECTION_OUT);
    tDoc.setTimestamp(System.currentTimeMillis());
    
    //JmsSender sender = new JmsSender();
    //sender.setSendProperties(getSendProperties());

    String response = null;
    String txNo = null;
    try
    {
      //sender.send(tDoc);
      
      txNo = lookupHandler().handleOutgoingTransaction(tDoc);
      String logmsg = "Saved Tx: txNo="+txNo;
      _logger.logMessage("receive", null, logmsg);
    }
    catch (Exception ex)
    {
      _logger.logError(ILogErrorCodes.OUTGOING_TRANSACTION_PROCESS, "receive", null, "JMS Error. Unable to save transaction for processing", ex);
    }
    return response;
  }
  
  public String validateHeaders(Hashtable<String,String> headers)
  {
    String partnerId = headers.get(ITransactionHeaderConstants.SENDER_IDENTITY);
    String custBeId = headers.get(ITransactionHeaderConstants.RECEIVER_IDENTITY);
    String docType = headers.get(ITransactionHeaderConstants.DOCUMENT_TYPE);
    
    StringBuffer missing = new StringBuffer();
    if (StringUtil.isBlank(partnerId))
    {
      missing.append(" ").append(ITransactionHeaderConstants.SENDER_IDENTITY);
    }
    if (StringUtil.isBlank(custBeId))
    {
      missing.append(" ").append(ITransactionHeaderConstants.RECEIVER_IDENTITY);
    }
    if (StringUtil.isBlank(docType))
    {
      missing.append(" ").append(ITransactionHeaderConstants.DOCUMENT_TYPE);
    }
    if (missing.length() > 0)
    {
      return "Missing HTTP request headers:" + missing.toString();
    }
    return null;
  }
  
  private synchronized String generateFilename(String sender, String recipient, String docType)
  {
    return docType+"-"+sender+"-"+recipient+System.currentTimeMillis()+"."+getFileExt(docType);
  }
  
  private String getFileExt(String docType)
  {
    return ConfigurationStore.getInstance().getProperty(IConfigCats.DOC_TYPE_FILE_EXT_MAP, docType, "dat");
  }
  
  private Properties getSendProperties()
  {
    return ConfigurationStore.getInstance().getProperties(IConfigCats.ISHB_TX_OUT);
  }
  
  private String generateTracingId()
  {
    //TODO to be reviewed
    return UUID.randomUUID().toString();
  }
  
  private ITransactionHandler lookupHandler() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    ITransactionHandlerHome home = (ITransactionHandlerHome)finder.lookup(IJndiNames.TRANSACTION_HANDLER, ITransactionHandlerHome.class);
    return home.create();
  }

}
