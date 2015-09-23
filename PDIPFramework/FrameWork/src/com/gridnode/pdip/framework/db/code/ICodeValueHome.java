/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICodeValueHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 28, 2005    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.db.code;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

/**
 * The home interface for CodeValueBean.
 * 
 * @author Tam Wei Xiang
 * @since GT 2.4.7
 */
public interface ICodeValueHome
	extends EJBHome
{
	public ICodeValueObj create(CodeValue cv) throws CreateException, RemoteException;
	
	public ICodeValueObj findByPrimaryKey(Long uid) throws FinderException, RemoteException;
	
	/**
	 * Return a list of CodeValue obj which fulfil the entityType and fieldID
	 * @param entityType
	 * @param fieldID
	 * @return Collection of CodeValue objects found.
	 * @throws FinderException
	 * @throws RemoteException
	 */
	public Collection findByEntityTypeAndFieldID(String entityType, Integer fieldID) throws FinderException, RemoteException;
	
	/**
	 * Return a list of CodeValue obj which fulfil the code, entityType and fieldID
	 * @param code
	 * @param entityType
	 * @param fieldID
	 * @return Collection of CodeValue objects found.
	 * @throws FinderException
	 * @throws RemoteException
	 */
	public Collection findByCodeEntTypeAndFieldID(String code, String entityType, Integer fieldID) throws FinderException, RemoteException;
	
}
