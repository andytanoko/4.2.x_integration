/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDocumentManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       Created
 */

package com.gridnode.gtas.server.dbarchive.facade.ejb;

import com.gridnode.gtas.server.dbarchive.model.DocInfo;
import com.gridnode.gtas.server.dbarchive.model.ProcessInstanceInfo;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import javax.ejb.EJBObject;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
/**
* Remote Interface for ProcessDocumentManagerBean
*
* @author Tam Wei Xiang
* 
* @version
* @since
*/

public interface IProcessDocumentManagerObj extends EJBObject
{
	/**
	 * Populate the DocInfo obj into DB. Extra processing will be performed if the
	 * archivedType for the info is archivedByDocument.
	 * @param info
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void processDocInfo(DocInfo info)
							throws Exception, RemoteException;
	
	/**
	 * Populate the process instance record into DB. All the related documents (eg audit, udoc, gdoc, attachment)
	 * will be packaged into one zip file.
	 * @param proInfo
	 * @throws RemoteException
	 */
	public void populateProcessInstance(ProcessInstanceInfo proInfo) 
		throws ApplicationException, RemoteException;
}
