/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAuditPropertiesManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 22, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.properties.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IAuditPropertiesManagerHome extends EJBHome
{
  public IAuditPropertiesManagerObj create() throws CreateException, RemoteException;
}
