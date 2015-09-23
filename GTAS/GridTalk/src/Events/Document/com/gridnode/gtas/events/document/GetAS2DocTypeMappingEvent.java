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
 * 2008-10-17    Wong Yee Wah         Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class GetAS2DocTypeMappingEvent extends EventSupport
{
  private static final long serialVersionUID = 971188556048196345L;
  public static final String AS2_DOC_TYPE_UID  = "AS2DocType UID";
  
  public GetAS2DocTypeMappingEvent(Long docTypeUID)
  {
    setEventData(AS2_DOC_TYPE_UID, docTypeUID);
  }
  
  public Long getAS2DocTypeUID()
  {
    return (Long)getEventData(AS2_DOC_TYPE_UID);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/GetAS2DocTypeMappingEvent";
  }
  
}
