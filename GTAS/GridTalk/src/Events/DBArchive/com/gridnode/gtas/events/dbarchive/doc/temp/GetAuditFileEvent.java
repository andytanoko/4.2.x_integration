/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAuditFileEvent.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 12 Oct 2005	    Sumedh Chalermkanjana   Created
 * 25 Oct 2006      Regina Zeng             Added serialVersionUID
 */
package com.gridnode.gtas.events.dbarchive.doc.temp;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This class contains DocumentMetaInfo uid for retrieving specific audit file information.
 */
public class GetAuditFileEvent
  extends    EventSupport
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 4358223344511484588L;
  public static final String UID  = "AuditFile UID";

  public GetAuditFileEvent(Long uid) throws EventException
  {
    checkSetLong(UID, uid);
  }

  public Long getUid()
  {
    return (Long)getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetAuditFileEvent";
  }
}