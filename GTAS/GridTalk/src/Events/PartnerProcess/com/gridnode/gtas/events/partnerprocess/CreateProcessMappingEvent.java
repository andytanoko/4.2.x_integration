/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateProcessMappingEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 * Mar 17 2003    Daniel D'Cotta      Removed checking for DocType
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the creation of new ProcessMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class CreateProcessMappingEvent
  extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8662966290100900865L;
	public static final String PROCESS_DEF        = "Process Def";
  public static final String DOC_TYPE           = "Document Type";
  public static final String IS_INITIATING_ROLE = "Is Initiating Role";
  public static final String SEND_CHANNEL_UID   = "Send Channel UID";
  public static final String PARTNER_ID         = "Partner ID";

  public CreateProcessMappingEvent(
    String processDef,
    String partnerID,
    Boolean isInitiatingRole,
    String docType,
    Long sendChannelUID) throws EventException
  {
    checkSetString(PROCESS_DEF, processDef);
    checkSetString(PARTNER_ID, partnerID);
    checkSetObject(IS_INITIATING_ROLE, isInitiatingRole, Boolean.class);
//    if (isInitiatingRole.booleanValue())
    checkSetLong(SEND_CHANNEL_UID, sendChannelUID);

// 20030317 DDJ: Removed checking for DocType
//    if (!isInitiatingRole.booleanValue())
//      checkSetString(DOC_TYPE, docType);
    setEventData(DOC_TYPE, docType);
  }

  public String getProcessDef()
  {
    return (String)getEventData(PROCESS_DEF);
  }

  public String getPartnerID()
  {
    return (String)getEventData(PARTNER_ID);
  }

  public boolean isInitiatingRole()
  {
    return ((Boolean)getEventData(IS_INITIATING_ROLE)).booleanValue();
  }

  public String getDocType()
  {
    return (String)getEventData(DOC_TYPE);
  }

  public Long getSendChannelUID()
  {
    return (Long)getEventData(SEND_CHANNEL_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateProcessMappingEvent";
  }

}