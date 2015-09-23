/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSFailedMsgHandlerMgr.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 15, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.jms.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.framework.db.jms.JMSFailedMsg;
import com.gridnode.pdip.framework.db.jms.JMSFailedMsgHelper;
import com.gridnode.pdip.framework.jms.JMSSender;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.notification.AbstractNotification;
import com.gridnode.pdip.framework.notification.DocumentFlowNotification;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class JMSMsgHandlerMgrBean implements SessionBean
{
  /**
   * 
   */
  private static final long serialVersionUID = -5215769589613463528L;
  private SessionContext _ctxt;
  private static final String LOG_CAT = "JMSFailedMsgHandlerMgrBean";
  
  public void processFailedJMS(JMSFailedMsg failedMsg) throws Exception
  {
    Log.debug(LOG_CAT, "processFailedJMS enter");
    JMSFailedMsgHelper helper = JMSFailedMsgHelper.getInstance();
    try
    { 
        sendJms(failedMsg);
        helper.deleteFailedJMSMsg(failedMsg);
        
    }
    catch(Exception ex)
    {
      Log.warn(LOG_CAT, "Error in processing the failed JMS "+failedMsg, ex);
      _ctxt.setRollbackOnly();
      throw ex;
    }
    Log.debug(LOG_CAT, "processFailedJMS ended");
  }
  
  public void updateFailedJMSMsg(JMSFailedMsg failedMsg, int maxRetry) throws Exception
  {
    failedMsg.setRetryCount(failedMsg.getRetryCount()+1);
    JMSFailedMsgHelper helper = JMSFailedMsgHelper.getInstance();
    
    Log.debug(LOG_CAT, "Max Retry: "+maxRetry+" FailedJMSMsg: "+failedMsg);
    
    if(maxRetry <= failedMsg.getRetryCount())
    {
      Log.debug(LOG_CAT, "Retry count limit reached. Delete the failed jms "+failedMsg+" from DB.");
      helper.deleteFailedJMSMsg(failedMsg);
    }
    else
    {
      helper.updateFailedJMSMsg(failedMsg);
    }
    Log.debug(LOG_CAT, "Update using session bean trans completed.");
  }
  
  private void sendJms(JMSFailedMsg failedMsg) throws Exception
  {
    Hashtable configProps = failedMsg.getConfigPrope();
    String destName = failedMsg.getDestName();
    String destType = failedMsg.getDestinationType();
    Serializable msgObj = failedMsg.getMsgObj();
    Hashtable msgProps = failedMsg.getMsgProps();
    
    Log.debug(LOG_CAT, "Sending jms msg");
    JMSSender.deliverJMSMessage(configProps, destName, msgObj, msgProps);
  }
  
  public JMSFailedMsg getNextFailedMsg(int maxRetry, Long processFailedMsgUID) throws Exception, RemoteException
  {
    JMSFailedMsgHelper helper = JMSFailedMsgHelper.getInstance();
    return helper.getNextFailedMsg(maxRetry, processFailedMsgUID);
  }
  
  public void sendMessage(String configName, String destName, Serializable msgObj, Hashtable msgProps) throws Exception
  {
    JMSSender.sendMessage(configName, destName, msgObj, msgProps);
  }
  
  public void sendMessage(Hashtable<String,String> jmsSendProps, String destName, Serializable msgObj, Hashtable msgProps) throws Exception
  {
    JMSSender.deliverJMSMessage(jmsSendProps, destName, msgObj, msgProps);
  }
  
  public void ejbCreate() throws CreateException
  {
    
  }
  
  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#ejbActivate()
   */
  public void ejbActivate() throws EJBException, RemoteException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#ejbPassivate()
   */
  public void ejbPassivate() throws EJBException, RemoteException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#ejbRemove()
   */
  public void ejbRemove() throws EJBException, RemoteException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
   */
  public void setSessionContext(SessionContext ctxt) throws EJBException,
                                                    RemoteException
  {
    _ctxt = ctxt;
  }
}
