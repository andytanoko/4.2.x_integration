/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBizEntityListForUserEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 15 Dec 2005		SC									Created
 * 30 Dec 2005    Neo Sok Lay         Add Serial Version UID
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class GetPartnerListByBizEntityEvent extends EventSupport
{
	/**
   * Serial Version UID
   */
  private static final long serialVersionUID = 7711645450268252926L;
  public static final String BUSINESS_ENTITY_UID = "BUSINESS_ENTITY_UID";
	public static final String BUSINESS_ENTITY_ID = "BUSINESS_ENTITY_ID";
	public static final String ENTERPRISE_ID = "ENTERPRISE_ID";
	
	public String getEventName()
  {
    return "java:comp/env/param/event/GetPartnerListByBizEntityEvent";
  }
	
	public GetPartnerListByBizEntityEvent(Long businessEntityUid) throws EventException
	{
		checkSetLong(BUSINESS_ENTITY_UID, businessEntityUid);
	}
	
	public GetPartnerListByBizEntityEvent(String businessEntityId, String enterpriseId) throws EventException
	{
		checkSetString(BUSINESS_ENTITY_ID, businessEntityId);
		
		// don't use method checkSetString for enterpriseId because enterpriseId can be null.
		setEventData(ENTERPRISE_ID, enterpriseId);
	}
	
	public Long getBusinessEntityUid()
	{
		return (Long) getEventData(BUSINESS_ENTITY_UID);
	}
	
	public String getBusinessEntityId()
	{
		return (String) getEventData(BUSINESS_ENTITY_ID);
	}
	
	public String getEnterpriseId()
	{
		return (String) getEventData(ENTERPRISE_ID);
	}
}
