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
 * 14 Dec 2005			SC									Add many constants to support retrival of various types of id.
 * 16 Dec 2005			SC									Change name: star fish number to starfish id.
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.pdip.app.bizreg.model.IDomainIdentifier;

public interface DomainIdentifiersConstants
{
	public static final String BUSINESS_ENTITY_ID = "BUSINESS_ENTITY_ID";
	public static final String BUSINESS_ENTITY_UID = "BUSINESS_ENTITY_UID";
	public static final String DUNS_NUMBER = IDomainIdentifier.TYPE_DUNS_NUMBER;
	public static final String STARFISH_ID = IDomainIdentifier.TYPE_STARFISH_ID;
	public static final String AS2_IDENTIFIER = IDomainIdentifier.TYPE_AS2_IDENTIFIER;
	public static final String DISCOVERY_URL = IDomainIdentifier.TYPE_DISCOVERY_URL;
	public static final String GLOBAL_LOCATION_NUMBER = IDomainIdentifier.TYPE_GLOBAL_LOCATION_NUMBER;
	
	/* Constants used in result hash map names */
	public static final String RESULT_TARGET_IDENTIFIERS = "RESULT_TARGET_IDENTIFIERS";
	public static final String RESULT_ENTERPRISE_IDENTIFIERS = "RESULT_ENTERPRISE_IDENTIFIERS";
}
