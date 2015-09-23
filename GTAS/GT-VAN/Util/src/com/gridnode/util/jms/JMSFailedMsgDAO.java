/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSFailedMsgDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 28, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.util.jms;

import java.util.Collection;

import com.gridnode.util.db.DAO;
import com.gridnode.util.jms.model.JMSFailedMsg;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class JMSFailedMsgDAO extends DAO
{
  private Logger _logger;
  private static final String CLASS_NAME = "JMSFailedMsgDAO";
  
  public JMSFailedMsgDAO()
  {
    super(false);
    initLogger();
  }
  
  public JMSFailedMsgDAO(boolean newSession)
  {
    super(newSession);
    initLogger();
  }
  
  public String persistFailedJMS(JMSFailedMsg msg)
  {
    _logger.logMessage("persistFailedJMS", null, "Persisting jms failed msg: "+msg);
    return (String)create(msg);
  }
  
  public void updateFailedJMS(JMSFailedMsg msg)
  {
    _logger.logMessage("updateFailedJMS", null, "Update failed jms: "+msg);
    update(msg);
  }
  
  public void deleteFailedJMS(JMSFailedMsg msg)
  {
    _logger.logMessage("deleteFailedJMS", null, "Delete failed jms: "+msg);
    delete(msg);
  }
  
  public Collection retrieveFailedJMSMsgs(int maxRetry, String category, int maxResult)
  {
    String queryName = getPersistenceClass().getName()+".retrieveFailedJMS";
    String[] paramNames = new String[]{"retryCount", "category"};
    Object[] paramVals = new Object[]{maxRetry, category};
    return queryN(queryName, paramNames, paramVals, maxResult);
  }
  
  public JMSFailedMsg retrieveFailedJMS(String pk)
  {
    return (JMSFailedMsg)load(getPersistenceClass(), pk);
  }
  
  private void initLogger()
  {
    _logger = LoggerManager.getInstance().getLogger(LoggerManager.TYPE_UTIL, CLASS_NAME);
  }
  
  /**
   * Return the Persistence Class that this DAO is handling.
   */
  private Class getPersistenceClass()
  {
    return JMSFailedMsg.class;
  }
}
