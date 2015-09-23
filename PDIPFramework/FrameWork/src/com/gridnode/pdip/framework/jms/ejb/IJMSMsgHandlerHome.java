/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJMSFailedMsgHandlerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 15, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.jms.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IJMSMsgHandlerHome extends EJBHome
{
  public IJMSMsgHandlerObj create() throws CreateException, RemoteException;
}
