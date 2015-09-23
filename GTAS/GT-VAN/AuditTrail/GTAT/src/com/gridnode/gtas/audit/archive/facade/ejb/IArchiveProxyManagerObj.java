/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IArchiveProxyManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 7, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.EJBObject;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IArchiveProxyManagerObj extends EJBObject
{
  public Long getEarliestProcessStartDate(Hashtable criteria) throws Exception, RemoteException;
  
  public Long getEarlistGDocDateTimCreate(Hashtable criteria) throws Exception, RemoteException;
}
