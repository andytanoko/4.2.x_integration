/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DomainIdentifiersConstants.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * 16 Dec 2005			SC									Created
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.pdip.framework.util.AssertUtil;

public class DomainIdentifiersUtil
{
	public static void assertDomainIdentifierType(String type)
	{
		AssertUtil.assertTrue(
		                      (type != null)
		                      && (type.equals(DomainIdentifiersConstants.BUSINESS_ENTITY_ID)
		                      		|| type.equals(DomainIdentifiersConstants.BUSINESS_ENTITY_UID)
		                      		|| type.equals(DomainIdentifiersConstants.DUNS_NUMBER)
		                      		|| type.equals(DomainIdentifiersConstants.STARFISH_ID)
		                      		|| type.equals(DomainIdentifiersConstants.AS2_IDENTIFIER)
		                      		|| type.equals(DomainIdentifiersConstants.DISCOVERY_URL)
		                      		|| type.equals(DomainIdentifiersConstants.GLOBAL_LOCATION_NUMBER)
		                      		));
		                      
	}
}
