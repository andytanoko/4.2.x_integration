/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJMSFailedMsgHandlerObj.java
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
import java.util.Hashtable;

import javax.ejb.EJBObject;

import com.gridnode.pdip.framework.db.jms.JMSFailedMsg;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IJMSMsgHandlerObj extends EJBObject
{
  public void processFailedJMS(JMSFailedMsg failedMsg) throws Exception, RemoteException;
  
  public void updateFailedJMSMsg(JMSFailedMsg failedMsg, int maxRetry) throws Exception, RemoteException;
  
  public JMSFailedMsg getNextFailedMsg(int maxRetry, Long processFailedMsg) throws Exception, RemoteException;
  
  public void sendMessage(Hashtable<String, String> jmsSendProps, String destName, Serializable msgObj, Hashtable msgProps) throws Exception, RemoteException;
  
  public void sendMessage(String configName, String destName, Serializable msgObj, Hashtable msgProps) throws Exception, RemoteException;
}
