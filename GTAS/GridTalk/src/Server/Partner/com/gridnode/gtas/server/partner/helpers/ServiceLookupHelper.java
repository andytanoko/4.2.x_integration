/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceLookupHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28, 2006   i00107              Created
 * Feb 07 2007    i00107              To use LocalContext for PartnerManager.
 */

package com.gridnode.gtas.server.partner.helpers;

import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * @author i00107
 * Helper for lookup Session Beans
 */
public class ServiceLookupHelper
{

	public static IPartnerManagerObj getPartnerManager() throws ServiceLookupException
	{
		return (IPartnerManagerObj)ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getObj(IPartnerManagerHome.class.getName(),
		                                                                     IPartnerManagerHome.class,
		                                                                     new Object[0]);
		
	}
}
