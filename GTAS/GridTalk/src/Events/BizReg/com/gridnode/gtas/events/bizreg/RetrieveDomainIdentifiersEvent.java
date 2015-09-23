/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RetrieveDomainIdentifiersEvent.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * 6 Dec 2005       SC									Created
 * 14 Dec 2005			SC									Modify code to add support for retrival of various types of id.
 * 16 Dec 2005			SC									Remove commented code.
 * 30 Dec 2005      Neo Sok Lay         Add Serial Version UID
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class RetrieveDomainIdentifiersEvent extends EventSupport
{
	/**
   * Serial Version UID
   */
  private static final long serialVersionUID = 8360831930569963458L;
  public static final String SOURCE_IDENTIFIERS = "SOURCE_IDENTIFIERS";
	public static final String SOURCE_TYPE = "SOURCE_TYPE";
	public static final String TARGET_TYPE = "TARGET_TYPE";
	public static final String ENTERPRISE_IDS = "ENTERPRISE_IDS";
	
	/* accessor methods */
	public void setSourceIdentifiers(String[] ar)
	{
		setEventData(SOURCE_IDENTIFIERS, ar);
	}
	
	public void setSourceType(String str) throws EventException
	{
		DomainIdentifiersUtil.assertDomainIdentifierType(str);
		checkSetString(SOURCE_TYPE, str);
	}
	
	public void setTargetType(String str) throws EventException
	{
		DomainIdentifiersUtil.assertDomainIdentifierType(str);
		checkSetString(TARGET_TYPE, str);
	}
	
	public void setEnterpriseIds(String[] ar)
	{
		setEventData(ENTERPRISE_IDS, ar);
	}
	
	public String[] getSourceIdentifiers()
	{
		return (String[]) getEventData(SOURCE_IDENTIFIERS);
	}
	
	public String getSoureType()
	{
		return (String) getEventData(SOURCE_TYPE);
	}
	
	public String getTargetType()
	{
		return (String) getEventData(TARGET_TYPE);
	}
	
	public String[] getEnterpriseIds()
	{
		return (String[]) getEventData(ENTERPRISE_IDS);
	}
	
	public String getEventName()
  {
    return "java:comp/env/param/event/RetrieveDomainIdentifiersEvent";
  }
}
