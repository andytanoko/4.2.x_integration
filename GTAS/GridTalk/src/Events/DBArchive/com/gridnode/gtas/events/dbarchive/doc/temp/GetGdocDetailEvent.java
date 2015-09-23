/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGdocDetailEvent.java
 *
 ****************************************************************************
 * Date             Author             Changes
 ****************************************************************************
 * 2 Nov 2006      Regina Zeng         Created
 */
package com.gridnode.gtas.events.dbarchive.doc.temp;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This class contains DocumentMetaInfo uid for retrieving specific gdoc detail information.
 */
public class GetGdocDetailEvent extends EventSupport
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 1169086218511453505L;
  public static final String UID  = "Gdoc Detail UID";

  public GetGdocDetailEvent(Long uid) throws EventException
  {
    checkSetLong(UID, uid);
  }
  
  public Long getUid()
  {
    return (Long)getEventData(UID);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/GetGdocDetailEvent";
  }
}
