/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAuditTrailArchiveManagerLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 16, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IAuditTrailArchiveManagerLocalHome extends EJBLocalHome
{
  public IAuditTrailArchiveManagerLocalObj create() throws CreateException;
}
