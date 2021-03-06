/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetWebServiceEvent.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.events.servicemgmt;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a WebService based on
 * UID.
 */
public class GetWebServiceEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6136254326678636277L;
	public static final String QUERY_UID  = "WebService UID";

  public GetWebServiceEvent(Long queryUid)
  {
    setEventData(QUERY_UID, queryUid);
  }

  public Long getWebServiceUID()
  {
    return (Long)getEventData(QUERY_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetWebServiceEvent";
  }

}