/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEsPiEvent.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 7 Oct 2005	      Sumedh Chalermkanjana   Created
 */
package com.gridnode.gtas.events.dbarchive;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This class contains process instance uid for retrieving specific process instance.
 * The implementation is based on GetProcessInstanceEvent.java.
 */
public class GetEsPiEvent
  extends    EventSupport
{
//  public static final String DEF_NAME  = "ProcessDef Name";

  public static final String UID  = "EsPi UID";

  public GetEsPiEvent(Long uid)
    throws EventException
  {
    checkSetLong(UID, uid);
//    setEventData(DEF_NAME, defName);
  }

  public Long getUid()
  {
    return (Long)getEventData(UID);
  }
  
//  public String getDefName()
//  {
//    return (String)getEventData(DEF_NAME);
//  }
  

  public String getEventName()
  {
    return "java:comp/env/param/event/GetEsPiEvent";
  }
}