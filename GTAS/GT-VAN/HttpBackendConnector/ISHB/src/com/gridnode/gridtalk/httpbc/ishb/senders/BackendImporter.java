/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 12, 2007    i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.gridtalk.httpbc.ishb.GtasServiceException;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This class is responsible for sending(importing) the transaction doc to GridTalk.
 */
public class BackendImporter
{
  private Logger _logger;
  private boolean _success = false;
  
  
  /**
   * Constructs a BackendSender to send the files to GridTalk
   */
  public BackendImporter()
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHB, "BackendImporter");
  }
  
  
  /**
   * Send the specified document.
   * 
   * @param tDoc The document
   * @throws SenderException Unable to send the specified document
   */
  public void send(TransactionDoc tDoc) throws SenderException
  {
    String mtdName = "send";
    Object[] params = {tDoc};
    
    try
    {
      _logger.logEntry(mtdName, params);
      
      GtasClient client = GtasClient.getGtasClient(tDoc.getBizEntId());
      
      _success = client.send(tDoc.getBizEntId(), tDoc.getPartnerId(), tDoc.getDocType(), tDoc.getDocContent(), tDoc.getAttContent(), tDoc.getTracingId());
      
    }
    catch (GtasServiceException ex)
    {
      _logger.logWarn(mtdName, params, "Error sending transaction via backend import", ex); 
      throw new SenderException("Error sending transaction via backend import", ex);
    }
    finally
    {
      //cleanup temp folder
      _logger.logExit(mtdName, params);
    }
  }

  /**
   * Get import status
   * @return The status of the backend import. <b>true</b> for success, <b>false</b> otherwise.
   */
  public boolean getStatus()
  {
    return _success;
  }

}
