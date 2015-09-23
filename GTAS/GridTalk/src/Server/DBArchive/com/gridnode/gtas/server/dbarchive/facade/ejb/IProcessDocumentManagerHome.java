/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDocumentManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       Created
 */

/**
 * RemoteHome interface for ProcessDocumentManagerBean
 *
 * @author Tam Wei Xiang
 * 
 * @version 1.0
 * @since 1.0
*/
package com.gridnode.gtas.server.dbarchive.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface IProcessDocumentManagerHome 
						 extends EJBHome
{
	public IProcessDocumentManagerObj create()
		throws CreateException, RemoteException;
}
