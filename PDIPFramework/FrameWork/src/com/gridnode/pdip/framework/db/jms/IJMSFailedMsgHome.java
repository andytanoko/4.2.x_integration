/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJMSFailedMsgHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.db.jms;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IJMSFailedMsgHome extends EJBHome
{
  public IJMSFailedMsgObj create(JMSFailedMsg msg) throws CreateException, RemoteException;
  public IJMSFailedMsgObj findByPrimaryKey(Long uid) throws FinderException, RemoteException;
  public Collection findByCreatedDate(Integer retryCount, Integer limit) throws FinderException, RemoteException;
  public Long getNextUID(Integer retryCount, Long processedUID) throws FinderException, RemoteException;
  public String getMinFailedMsgUID() throws FinderException, RemoteException;
}
