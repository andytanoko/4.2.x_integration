/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJmsSenderManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 27, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.util.jms.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Properties;

import javax.ejb.EJBObject;

import com.gridnode.util.jms.JmsSenderException;
import com.gridnode.util.jms.model.JMSFailedMsg;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IJmsHandlerManagerObj extends EJBObject
{
  public void send(Serializable obj, Hashtable msgProperties, Properties sendProperties) throws RemoteException, JmsSenderException;
  
  public void sendWithNewTrans(Serializable obj, Hashtable msgProperties, Properties sendProperties) throws RemoteException, JmsSenderException;
  
  public void saveFailedJMS(Serializable obj, Hashtable msgProperties, Properties sendProperties, String category) throws RemoteException;
  
  public Collection<JMSFailedMsg> retrieveFailedJMS(int maxRetry, String category, int numRetrieve) throws RemoteException;
  
  public void updateFailedJMS(JMSFailedMsg msg, int maxRetry) throws RemoteException;
  
  public void deleteFailedJMS(JMSFailedMsg msg) throws RemoteException;
  
  /**
   * Handle the failed jms by resending them to their destination.
   * @param msg The failed jms we persisted in DB for retry.
   */
  public void handleFailedJMS(JMSFailedMsg msg) throws Exception, RemoteException;
}
