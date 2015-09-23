/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSFailedMsgHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.db.jms;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class JMSFailedMsgHelper
{
  private static JMSFailedMsgHelper _helper = new JMSFailedMsgHelper();
  private static final String LOG_CAT = "JMSFailedMsgHelper";
  
  private JMSFailedMsgHelper()
  {
    
  }
  
  public static JMSFailedMsgHelper getInstance()
  {
    return _helper;
  }
  
  public JMSFailedMsg getNextFailedMsg(int maxRetry, Long processedRecordUID) throws FindEntityException
  {
    try
    {
      //TODO get the min UID, if none exist, rturn null. So that can kick start for the first time.
      Long uid =  getJMSFailedMsgHome().getNextUID(maxRetry, processedRecordUID);
      JMSFailedMsg failedMsg = null;
      if(uid != null)
      {
        IJMSFailedMsgObj jmsFailedMsg = getJMSFailedMsgHome().findByPrimaryKey(uid);
        failedMsg = new JMSFailedMsg(jmsFailedMsg.getDestinationType(), jmsFailedMsg.getJmsConfigProps(),
                                                  jmsFailedMsg.getDestName(), jmsFailedMsg.getMsgObj(), jmsFailedMsg.getMsgProps()
                                                  );
        failedMsg.setCreatedDate(jmsFailedMsg.getCreatedDate());
        failedMsg.setRetryCount(jmsFailedMsg.getRetryCount());
        failedMsg.setUid(new Long(jmsFailedMsg.getUID()));
        Log.debug(LOG_CAT, "Fetched failedMsg: "+failedMsg);
        
      }
      return failedMsg;
    }
    catch(ObjectNotFoundException ex)
    {
      Log.debug(LOG_CAT, "Can't find any FailedJMS given criteria Max Retry:"+maxRetry+" processedRecordUID: "+processedRecordUID, ex);
      return null;
    }
    catch(Exception ex)
    {
      throw new FindEntityException("Error retrieving the JMSFailedMsg entity given retryCount: "+maxRetry, ex);
    }
  }
  
  public void persistFailedJMSMsg(JMSFailedMsg msg) throws CreateEntityException
  {
    try
    {
      Log.debug(LOG_CAT,"Creating failed jms msg: "+msg);
      getJMSFailedMsgHome().create(msg);
    }
    catch(Exception ex)
    {
      throw new CreateEntityException("Error in persisting failedJMSMsg: "+msg, ex);
    }
  }
  
  public void deleteFailedJMSMsg(JMSFailedMsg failedMsg) throws DeleteEntityException
  {
    try
    {
      IJMSFailedMsgHome home = getJMSFailedMsgHome();
      IJMSFailedMsgObj failedJMSMsg = home.findByPrimaryKey(failedMsg.getUid());      
      failedJMSMsg.remove();
      
      Log.debug(LOG_CAT, "deleteFailedJMSMsg:"+failedMsg);
    }
    catch(Exception ex)
    {
      throw new DeleteEntityException("Error deleting msg: "+failedMsg, ex);
    }
  }
  
  public void updateFailedJMSMsg(JMSFailedMsg failedMsg) throws UpdateEntityException
  {
    try
    {
      Log.debug(LOG_CAT,"Updating failed jms: "+failedMsg);
      IJMSFailedMsgObj obj = getJMSFailedMsgHome().findByPrimaryKey(failedMsg.getUid());
      obj.setJmsConfigProps(failedMsg.getConfigPrope());
      obj.setCreatedDate(failedMsg.getCreatedDate());
      obj.setDestinationType(failedMsg.getDestinationType());
      obj.setDestName(failedMsg.getDestName());
      obj.setMsgObj(failedMsg.getMsgObj());
      obj.setMsgProps(failedMsg.getMsgProps());
      obj.setRetryCount(failedMsg.getRetryCount());
      
      Log.debug(LOG_CAT, "Update failed jms completed");
    }
    catch(Exception ex)
    {
      throw new UpdateEntityException("Error updating the msg: "+failedMsg, ex);
    }
  }
  
  public IJMSFailedMsgHome getJMSFailedMsgHome()
    throws ServiceLookupException
  {
    return (IJMSFailedMsgHome)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
                                                                                             IJMSFailedMsgHome.class.getName(), IJMSFailedMsgHome.class);
  }
}
