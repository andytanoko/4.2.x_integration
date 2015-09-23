/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AsyncBackendPush.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Jan 5, 2007    i00107              Add MessageId in TransactionDoc
 * Feb 06 2007    i00107              Input param for Gdoc must be Object instead of GridDocument.
 * Feb 24 2007    i00107              Add logging.
 * Apr 25 2007    i00107              Change to use LoggerManager for one-time use.
 */

package com.gridnode.gridtalk.httpbc.gthb;

import java.io.*;
import java.util.Properties;

import com.gridnode.gridtalk.httpbc.common.model.FileContent;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.IConstantValue;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.util.jms.JmsRetrySender;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This class is to serve as an user procedure to push a document for backend
 * in an asynchronous way, which in this case, is via JMS.
 */
public class AsyncBackendPush
{
  LoggerManager _logMgr;
  Logger _logger;
  public AsyncBackendPush()
  {
    _logMgr = LoggerManager.getOneTimeInstance();
    _logger = _logMgr.getLogger(ILogTypes.TYPE_HTTPBC_GTHB, "AsyncBackendPush");
    
  }
  /**
   * Push the document to backend.
   * @param gdocObj The GridDocument object.
   * @param udoc The user document byte content
   * @param attachments The attachments byte content
   * @param attFilenames The filesnames for the attachments.
   * @param config The configuration properties file for JMS sending.
   * @throws Exception
   */
  public void pushDocument(Object gdocObj, byte[] udoc, byte[][] attachments, String[] attFilenames, String config)
    throws Exception
  {
    String mtdName = "pushDocument";
    _logger.logEntry(mtdName, null);
    try
    {
      Properties props = loadConfigProperties(config);
      TransactionDoc tDoc = createTransactionDoc((GridDocument)gdocObj, udoc, attachments, attFilenames);
      _logger.debugMessage(mtdName, null, "Send to ISHB, txdoc="+tDoc);
      
      JmsRetrySender retrySender = new JmsRetrySender(props);
      if(! retrySender.isSendViaDefMode())
      {
        retrySender.retrySend(tDoc, null, IConstantValue.FAILED_JMS_CAT, props);
        //sendMsg(tDoc, props);
      }
      else
      {
        JmsSender sender = new JmsSender(_logMgr);
        sender.setSendProperties(props);
        sender.send(tDoc);
      }
    }
    finally
    {
      _logger.logExit(mtdName, null);
    }
  }
  
  private Properties loadConfigProperties(String propFN) throws IOException
  {
    Properties props = new Properties();
    props.load(new FileInputStream(propFN));
    return props;
  }
  
  private TransactionDoc createTransactionDoc(GridDocument gdoc, byte[] udoc, byte[][] attachments, String[] attFilenames)
  {
    FileContent udocFC = new FileContent(gdoc.getUdocFilename(), udoc);
    _logger.debugMessage("createTransactionDoc", null, "udocFC="+udocFC);
    
    FileContent[] attFC = null;
    if (attachments != null)
    {
      attFC = new FileContent[attachments.length];
      
      for (int i=0; i<attachments.length; i++)
      {
        attFC[i] = new FileContent(attFilenames[i], attachments[i]);
      }
    }
    
    String partnerId = gdoc.getSenderPartnerId();
    String custBeId = gdoc.getRecipientBizEntityId();
    String docType = gdoc.getUdocDocType();
    String tracingId = gdoc.getTracingID(); 
    String messageId = getFolderAbbr(gdoc.getFolder())+gdoc.getGdocId();
    
    TransactionDoc tDoc = new TransactionDoc(tracingId, partnerId, custBeId, docType, udocFC, attFC, TransactionDoc.DIRECTION_IN);
    tDoc.setMessageID(messageId);
    return tDoc;
  }
  
  private String getFolderAbbr(String folder)
  {
    if ("inbound".equalsIgnoreCase(folder))
    {
      return "IB-";
    }
    if ("outbound".equalsIgnoreCase(folder))
    {
      return "OB-";
    }
    if ("import".equalsIgnoreCase(folder))
    {
      return "IP-";
    }
    if ("export".equalsIgnoreCase(folder))
    {
      return "EP-";
    }
    return folder+"-";
  }

}
