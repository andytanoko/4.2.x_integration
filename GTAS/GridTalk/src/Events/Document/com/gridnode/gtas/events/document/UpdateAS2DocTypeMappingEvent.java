/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

public class UpdateAS2DocTypeMappingEvent extends EventSupport
{
  
  private static final long serialVersionUID = -8394868476630271453L;
  public static final String AS2DOCTYPEMAPPING_UID  = "AS2 Document Type Mapping UID";
  public static final String AS2_DOC_TYPE  = "AS2 Document Type";
  public static final String DOC_TYPE  = "Document Type";
  public static final String PARTNER_ID  = "Partner id";
  
  
  public UpdateAS2DocTypeMappingEvent(Long mappingUID,String as2DocType, String docType, String partnerID) throws EventException
  {
    setEventData(AS2DOCTYPEMAPPING_UID, mappingUID);
    checkSetString(AS2_DOC_TYPE,as2DocType);
    checkSetString(DOC_TYPE,docType);
    checkSetString(PARTNER_ID,partnerID);
  }
  
  public Long getAS2DocTypeMappingUID()
  {
    return (Long)getEventData(AS2DOCTYPEMAPPING_UID);
  }
  
  public String getAS2DocType()
  {
    return (String)getEventData(AS2_DOC_TYPE);
  }
  
  public String getDocType()
  {
    return (String)getEventData(DOC_TYPE);
  }
  
  public String getPartnerID()
  {
    return (String)getEventData(PARTNER_ID);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateAS2DocTypeMappingEvent";
  }
  
}