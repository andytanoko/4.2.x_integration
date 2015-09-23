/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchESDocumentHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 8, 2005        Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * RemoteHome interface for SearchESDocumentManagerBean
 *
 * @author Tam Wei Xiang
 * 
 * @version
 * @since
 */
public interface ISearchESDocumentManagerHome extends EJBHome 
{
	public ISearchESDocumentManagerObj create() throws CreateException,RemoteException;
}
