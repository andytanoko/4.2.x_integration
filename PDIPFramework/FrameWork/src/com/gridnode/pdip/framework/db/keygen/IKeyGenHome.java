/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IKeyGenHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 9, 2005    i00107              Created
 */
package com.gridnode.pdip.framework.db.keygen;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Remote home interface for KeyGenBean. This replaces the IKeyGeneratorHome.
 * 
 * @author i00107
 *
 */
public interface IKeyGenHome extends EJBHome
{
	public IKeyGenObj create() throws CreateException, RemoteException;
	
}
