/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateBusinessEntityEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * Dec 23 2003    Neo Sok Lay         Add DomainIdentifier input.
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "ENTERPRISE".
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.Collection;
import java.util.Map;

/**
 * This Event class contains the data for the creation of new Business Entities.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class CreateBusinessEntityEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8726102418072691512L;
	public static final String GUARDED_FEATURE = "ENTERPRISE";
  public static final String GUARDED_ACTION  = "CreateBusinessEntity";

  public static final String ENTERPRISE_ID   = "Enterprise ID";
  public static final String BE_ID           = "BusinessEntity ID";
  public static final String DESCRIPTION     = "Description";
  public static final String IS_PARTNER      = "IsPartner BizEntity";
  public static final String WHITEPAGE       = "WhitePage";
  public static final String DOMAIN_IDENTIFIERS = "DomainIdentifiers";

  /**
   * Construct a CreateBusinessEntityEvent with the specified values of the
   * BusinessEntity to create.
   * 
   * @param bizEntityID Business Entity ID.
   * @param description Business Entity description.
   * @param isPartnerBE <b>true</b> if the BusinessEntity represents a Partner, <b>false</b> otherwise.
   * @param whitePage The WhitePage of the BusinessEntity. The key values represent the fieldId and values
   * of the WhitePage fields.
   * @param domainIdentifiers Collection of Maps representing the DomainIdentifier(s) of the BusinessEntity.
   * The key values of the Maps represent the fieldId and values of the DomainIdentifier fields.
   * @throws EventException Invalid specified values for constructing the event.
   */
  public CreateBusinessEntityEvent(String bizEntityID, String description,
    boolean isPartnerBE, Map whitePage, Collection domainIdentifiers) throws EventException
  {
    checkSetString(BE_ID, bizEntityID);
    checkSetString(DESCRIPTION, description);
    setEventData(IS_PARTNER, new Boolean(isPartnerBE));
    setEventData(WHITEPAGE, whitePage);
    if (domainIdentifiers != null)
    {
      checkSetCollection(DOMAIN_IDENTIFIERS, domainIdentifiers, Map.class);
    }
    //may need to check whitepage fields in future
  }

  public String getBeID()
  {
    return (String)getEventData(BE_ID);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

  public Map getWhitePage()
  {
    return (Map)getEventData(WHITEPAGE);
  }

  public boolean isPartnerBE()
  {
    return ((Boolean)getEventData(IS_PARTNER)).booleanValue();
  }

  public Collection getDomainIdentifiers()
  {
    return (Collection)getEventData(DOMAIN_IDENTIFIERS);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/CreateBusinessEntityEvent";
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