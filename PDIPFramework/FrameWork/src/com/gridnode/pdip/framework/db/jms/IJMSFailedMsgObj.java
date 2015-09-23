/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJMSFailedMsgObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.db.jms;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Properties;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IJMSFailedMsgObj extends EJBObject
{
  public Long getUID() throws RemoteException;
  public void setUID(Long uid) throws RemoteException;
  
  public String getDestinationType() throws RemoteException;
  public void setDestinationType(String destType) throws RemoteException;
  
  public Hashtable<String, String> getJmsConfigProps() throws RemoteException;
  public void setJmsConfigProps(Hashtable<String, String> configName) throws RemoteException;
  
  public String getDestName() throws RemoteException;
  public void setDestName(String destName) throws RemoteException;
  
  public Serializable getMsgObj() throws RemoteException;
  public void setMsgObj(Serializable msgObj) throws RemoteException;
  
  public Hashtable getMsgProps() throws RemoteException;
  public void setMsgProps(Hashtable msgProps) throws RemoteException;
  
  public Timestamp getCreatedDate() throws RemoteException;
  public void setCreatedDate(Timestamp createdDate) throws RemoteException;
  
  public Integer getRetryCount() throws RemoteException;
  public void setRetryCount(Integer retryCount) throws RemoteException;
}
