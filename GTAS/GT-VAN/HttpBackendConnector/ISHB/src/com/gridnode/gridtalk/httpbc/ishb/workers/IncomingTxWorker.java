/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IncomingTxWorker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Feb 06 2007    i00107              Add destination for event notification.
 * Feb 24 2007    i00107              Set send status as event remark.
 * Mar 05 2007		Alain Ah Ming				Added error code to error logs
 */

package com.gridnode.gridtalk.httpbc.ishb.workers;

import java.util.Properties;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.EventNotifier;
import com.gridnode.gridtalk.httpbc.common.util.IConfigCats;
import com.gridnode.gridtalk.httpbc.ishb.senders.HttpBackendConnStore;
import com.gridnode.gridtalk.httpbc.ishb.senders.HttpSender;
import com.gridnode.gridtalk.httpbc.ishb.senders.HttpTargetConnInfo;
import com.gridnode.gridtalk.httpbc.ishb.senders.SenderException;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.StringUtil;

/**
 * @author i00107
 * This TransactionWorker is responsible for handling Incoming transactions.
 */
public class IncomingTxWorker extends TransactionWorker
{
  public IncomingTxWorker()
  {
    super(TransactionDoc.DIRECTION_IN);
  }
  
  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.workers.TransactionWorker#processTransaction(com.gridnode.gridtalk.httpbc.common.model.TransactionDoc)
   */
  protected void processTransaction(TransactionDoc doc)
  {
    String mtdName = "processTransaction";
    HttpBackendConnStore store = HttpBackendConnStore.getInstance();
    
    HttpSender sender = new HttpSender(store.getProxyAuthConnectionInfo());
    HttpTargetConnInfo connInfo = store.getTargetConnectionInfo(doc.getBizEntId());
    if (connInfo != null)
    {

      try
      {
        Properties docHeaders = store.getDocSpecificHeaders(doc.getDocType());
        byte[] payload = createPayload(doc);
        getLogger().debugMessage(mtdName, null, "payload size="+payload.length);
        sender.createConnection(connInfo);
        substituteVariables(docHeaders, doc);
        sender.sendMessage(docHeaders, payload);
        sender.closeConnection();
        
        removeCurrTransaction(doc);
        EventNotifier.getInstance().onDocumentDelivered(TARGET_BACKEND, doc, getStatusStr(sender.getResponseCode(), sender.getResponseMessage()));
      }
      catch (SenderException ex)
      {
        getLogger().logError(ILogErrorCodes.INCOMING_TRANSACTION_PROCESS,  mtdName, null, "Unable to send document via Http: "+ex.getMessage(), ex);
        pendCurrTransactionForLater(doc);
       EventNotifier.getInstance().onDocumentDeliveryFailed(TARGET_BACKEND, doc, 
                                                             "Unable to send document via Http: "+
                                                             ExceptionUtil.getStackStraceStr(ex),
                                                             getStatusStr(sender.getResponseCode(), sender.getResponseMessage())); 
      }
    }
    else
    {
      getLogger().logError(ILogErrorCodes.INCOMING_TRANSACTION_PROCESS, mtdName, null, "Unable to find target Http connection info for "+doc.getBizEntId(), null);
      pendCurrTransactionForLater(doc);
      EventNotifier.getInstance().onDocumentDeliveryFailed(TARGET_BACKEND, doc, "Unable to find target Http connection info for "+doc.getBizEntId(),
                                                           getStatusStr(sender.getResponseCode(), sender.getResponseMessage())); 
    }
  }

  private String getStatusStr(int respCode, String respMsg)
  {
    String s = "HTTP Response: "+respCode;
    if (respMsg != null)
    {
      s+= (" " + respMsg);
    }
    return s;
  }
  
  private byte[] createPayload(TransactionDoc doc)
  {
    //TODO what to do with Attachments????
    return doc.getDocContent().getContent();
  }
  
  private void substituteVariables(Properties docHeaders, TransactionDoc doc)
  {
    String[] keys = docHeaders.keySet().toArray(new String[docHeaders.size()]);
    for (String key : keys)
    {
      String val = docHeaders.getProperty(key);
      val = StringUtil.substitute(val, IConfigCats.DOC_HEADER_BIZENT_ID, doc.getBizEntId());
      val = StringUtil.substitute(val, IConfigCats.DOC_HEADER_DOC_TYPE, doc.getDocType());
      val = StringUtil.substitute(val, IConfigCats.DOC_HEADER_PARTNER_ID, doc.getPartnerId());
      val = StringUtil.substitute(val, IConfigCats.DOC_HEADER_TRACING_ID, doc.getTracingId());
      val = StringUtil.substitute(val, IConfigCats.DOC_HEADER_FILENAME, doc.getDocContent().getFilename());
      docHeaders.put(key, val);
    }
  }
  
  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.workers.TransactionWorker#getName()
   */
  @Override
  protected String getName()
  {
    return "IncomingTxWorker";
  }
  
  
}
