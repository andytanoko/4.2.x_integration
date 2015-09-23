/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICodeValueObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 28, 2005    Tam Wei Xiang       Created
 * Aug 30, 2006    Tam Wei Xiang       To throw RemoteException
 */
package com.gridnode.pdip.framework.db.code;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * The remote interface for CodeValueBean
 */
public interface ICodeValueObj
	extends EJBObject
{
	public Long getUID() throws RemoteException;
  
  public String getCode() throws RemoteException;
  
  public String getDescription() throws RemoteException;
  
  public String getEntityType() throws RemoteException;
  
  public Integer getFieldID() throws RemoteException;
}
