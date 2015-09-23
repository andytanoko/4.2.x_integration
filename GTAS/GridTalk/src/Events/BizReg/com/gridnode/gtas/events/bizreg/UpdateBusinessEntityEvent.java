/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateBusinessEntityEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * Dec 23 2003    Neo Sok Lay         Add DomainIdentifier input.
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "HIERARCHY"
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;
import java.util.Map;

/**
 * This Event class contains the data for update a BusinessEntity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I4
 */
public class UpdateBusinessEntityEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2886242284833057572L;
	public static final String GUARDED_FEATURE = "ENTERPRISE";
  public static final String GUARDED_ACTION  = "UpdateBusinessEntity";

  public static final String BE_UID          = "BusinessEntity UID";
  public static final String UPD_DESCRIPTION = "Upd Description";
  public static final String UPD_WHITEPAGE   = "Upd WhitePage";
  public static final String UPD_DOMAIN_IDENTIFIERS = "Upd DomainIdentifiers";

  /**
   * Constructs a UpdateBusinessEntityEvent with the specified values for updating
   * an existing BusinessEntity in the database. 
   * 
   * @param uID UID of the BusinessEntity to update.
   * @param description BusinessEntity description to be updated.
   * @param whitePage Map of WhitePage fields to be updated. 
   * @param domainIdentifiers Collection of Maps of DomainIdentifier fields to be updated.
   * @throws EventException Invalid specified values for constructing the event.
   */
  public UpdateBusinessEntityEvent(Long uID, String description,
    Map whitePage, Collection domainIdentifiers) throws EventException
  {
    checkSetLong(BE_UID, uID);
    checkSetString(UPD_DESCRIPTION, description);
    setEventData(UPD_WHITEPAGE, whitePage);
    if (domainIdentifiers != null)
      checkSetCollection(UPD_DOMAIN_IDENTIFIERS, domainIdentifiers, Map.class);
  }

  public Long getBeUID()
  {
    return (Long)getEventData(BE_UID);
  }

  public String getUpdDescription()
  {
    return (String)getEventData(UPD_DESCRIPTION);
  }

  public Map getUpdWhitePage()
  {
    return (Map)getEventData(UPD_WHITEPAGE);
  }

  public Collection getUpdDomainIdentifiers()
  {
    return (Collection)getEventData(UPD_DOMAIN_IDENTIFIERS);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateBusinessEntityEvent";
  }

  // ************* From IGuardedEvent *************************

  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }


}