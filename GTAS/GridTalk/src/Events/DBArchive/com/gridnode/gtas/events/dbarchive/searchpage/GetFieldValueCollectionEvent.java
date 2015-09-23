/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFieldValueCollectionEvent.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 20 Oct 2005	      Sumedh Chalermkanjana   Created
 */
package com.gridnode.gtas.events.dbarchive.searchpage;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

public class GetFieldValueCollectionEvent
  extends    EventSupport
{
  public static final String UID  = "FieldValueCollection UID";

  public GetFieldValueCollectionEvent(Long uid)
    throws EventException
  {
    checkSetLong(UID, uid);
  }

  public Long getUid()
  {
    return (Long)getEventData(UID);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/GetFieldValueCollectionEvent";
  }
}