/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateAlertTriggerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.alert;

import java.util.Collection;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for updating a AlertTrigger.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class UpdateAlertTriggerEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3394390997407819156L;
	public static final String UID            = "UID";
  public static final String DOC_TYPE       = "DocType";
  public static final String PARTNER_TYPE   = "PartnerType";
  public static final String PARTNER_GROUP  = "PartnerGroup";
  public static final String PARTNER_ID     = "PartnerID";
  public static final String ALERT_UID      = "AlertUID";
  public static final String IS_ENABLED     = "IsEnabled";
  public static final String IS_ATTACH_DOC  = "IsAttachDoc";
  public static final String RECIPIENTS     = "Recipients";

  public UpdateAlertTriggerEvent(
    Long uid,
    String docType,
    String partnerType,
    String partnerGroup,
    String partnerId,
    Long   alertUID,
    Boolean isEnabled,
    Boolean isAttachDoc,
    Collection recipients) throws EventException
  {
    checkSetLong(UID, uid);
    checkSetLong(ALERT_UID, alertUID);
    setEventData(DOC_TYPE, docType);
    setEventData(PARTNER_TYPE, partnerType);
    setEventData(PARTNER_GROUP, partnerGroup);
    setEventData(PARTNER_ID, partnerId);
    setEventData(IS_ENABLED, isEnabled);
    setEventData(IS_ATTACH_DOC, isAttachDoc);

    if (recipients != null && recipients.size() > 0)
      checkCollection(RECIPIENTS, recipients, String.class);

    setEventData(RECIPIENTS, recipients);
  }

  public String getDocType()
  {
    return (String)getEventData(DOC_TYPE);
  }

  public String getPartnerType()
  {
    return (String)getEventData(PARTNER_TYPE);
  }

  public String getPartnerGroup()
  {
    return (String)getEventData(PARTNER_GROUP);
  }

  public Long getAlertUID()
  {
    return (Long)getEventData(ALERT_UID);
  }

  public String getPartnerID()
  {
    return (String)getEventData(PARTNER_ID);
  }

  public Collection getRecipients()
  {
    return (Collection)getEventData(RECIPIENTS);
  }

  public Boolean getIsEnabled()
  {
    return (Boolean)getEventData(IS_ENABLED);
  }

  public Boolean getIsAttachDoc()
  {
    return (Boolean)getEventData(IS_ATTACH_DOC);
  }

  public Long getUID()
  {
    return (Long)getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateAlertTriggerEvent";
  }

}