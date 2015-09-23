/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAuditTrailHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 4, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IAuditTrailManagerHome extends EJBHome
{
  public IAuditTrailManagerObj create() throws CreateException, RemoteException;
}
