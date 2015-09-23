/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdatePartnerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.events.partner;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for updating a Partner.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdatePartnerEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4734546422518079327L;
	public static final String UID               = "UID";
  public static final String NAME              = "Name";
  public static final String DESCRIPTION       = "Description";
  public static final String PARTNER_TYPE_UID  = "Partner Type UID";
  public static final String PARTNER_GROUP_UID = "Partner Group UID";
  public static final String IS_ENABLED        = "Is Enabled";

  public UpdatePartnerEvent(
    Long    uID,
    String  name,
    String  description,
    Long    partnerTypeUID,
    Long    partnerGroupUID,
    boolean isEnabled)
    throws EventException
  {
    checkSetLong(UID, uID);
    checkSetString(NAME, name);
    checkSetString(DESCRIPTION, description);
    checkSetLong(PARTNER_TYPE_UID, partnerTypeUID);
    setEventData(PARTNER_GROUP_UID, partnerGroupUID);
    setEventData(IS_ENABLED, new Boolean(isEnabled));
  }

  public Long getUID()
  {
    return (Long)getEventData(UID);
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

  public Long getPartnerTypeUID()
  {
    return (Long)getEventData(PARTNER_TYPE_UID);
  }

  public Long getPartnerGroupUID()
  {
    return (Long)getEventData(PARTNER_GROUP_UID);
  }

  public boolean isEnabled()
  {
    return ((Boolean)getEventData(IS_ENABLED)).booleanValue();
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdatePartnerEvent";
  }

}