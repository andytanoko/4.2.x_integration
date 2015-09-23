/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGWFArchiveProcessHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.workflow.runtime.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IGWFArchiveProcessHome extends EJBHome
{
  public IGWFArchiveProcessObj create(IEntity data) throws RemoteException, CreateException;
}
