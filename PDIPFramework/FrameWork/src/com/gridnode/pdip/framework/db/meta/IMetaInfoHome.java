/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMetaInfoHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 02 2005    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.meta;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Remote home interface for MetaInfoBean. This replaces the IEntityMetaInfoHome
 * for retrieving entity metainfo.
 * @author i00107
 *
 */
public interface IMetaInfoHome extends EJBHome
{
	public IMetaInfoObj create() throws CreateException, RemoteException;
}
