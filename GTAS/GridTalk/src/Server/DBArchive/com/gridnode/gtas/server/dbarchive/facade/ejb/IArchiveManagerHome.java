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
 * Feb 10, 2009   Ong Eu Soon         Created
 */


package com.gridnode.gtas.server.dbarchive.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface IArchiveManagerHome 
						 extends EJBHome
{
	public IArchiveManagerObj create()
		throws CreateException, RemoteException;
}
