/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 23, 2005        Tam Wei Xiang       Created
 * Nov 11, 2005        Tam Wei Xiang       modified method getManager()
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import com.gridnode.gtas.server.dbarchive.facade.ejb.*;

/**
 *
 *
 * @author Tam Wei Xiang
 * 
 * @version
 * @since
 */
public class ActionHelper
{
	/**
	 * Acquire the ProcessDocumentManager remote interface using ServiceLocator
	 * @return ProcessDocumentManager remote interface
	 * @throws ServiceLookupException Error in lookup
	 */
	public static ISearchESDocumentManagerObj getManager() throws ServiceLookupException
	{
		return (ISearchESDocumentManagerObj)ServiceLocator.instance(
       		ServiceLocator.CLIENT_CONTEXT).getObj(
       		ISearchESDocumentManagerHome.class.getName(),
       		ISearchESDocumentManagerHome.class,
       		new Object[0]);
	}
}
