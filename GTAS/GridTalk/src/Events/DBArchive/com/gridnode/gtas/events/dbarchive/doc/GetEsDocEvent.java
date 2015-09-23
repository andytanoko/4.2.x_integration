/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEsDocEvent.java
 *
 ****************************************************************************
 * Date           Author              		Changes
 ****************************************************************************
 * 8 Oct 2005			Sumedh Chalermkanjana		Created
 */
package com.gridnode.gtas.events.dbarchive.doc;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a estore document based on
 * UID of document.
 */
public class GetEsDocEvent extends EventSupport
{
  public static final String UID  = "UID";

  public GetEsDocEvent(Long uid)
  {
    setEventData(UID, uid);
  }

  public Long getUid()
  {
    return (Long)getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetEsDocEvent";
  }

}