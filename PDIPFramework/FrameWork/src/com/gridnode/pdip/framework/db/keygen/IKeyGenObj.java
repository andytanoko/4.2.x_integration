/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IKeyGenObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 09 2005    Neo Sok Lay         Created
 * Nov 13 2006    Neo Sok Lay         GNDB00027928: change getNextId(String,Collection)
 *                                    to getNextId(String, Long)
 */
package com.gridnode.pdip.framework.db.keygen;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * Remote interface for KeyGenBean. This replaces the IKeyGeneratorObj.
 * 
 * @author i00107
 */
public interface IKeyGenObj extends EJBObject
{
	public long getNextId(String refName) throws SystemException, RemoteException;
	
	public long getNextId(String refName, Long currentMaxId) throws SystemException, RemoteException;
	
  //public long getNextId(String refName, Collection excludeKeySet) throws SystemException, RemoteException;
	
}
