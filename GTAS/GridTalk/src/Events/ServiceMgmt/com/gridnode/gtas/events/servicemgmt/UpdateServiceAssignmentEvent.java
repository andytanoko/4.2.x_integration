/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateServiceAssignmentEvent.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */

package com.gridnode.gtas.events.servicemgmt;

import java.util.Collection;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class UpdateServiceAssignmentEvent
  extends EventSupport
{
  
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8688717844683948045L;
	public static final String SERVICEASSIGNMENT_UID  = "SERVICEASSIGNMENT_UID";
  public static final String PASSWORD = "PASSWORD";
  public static final String USER_TYPE = "USER_TYPE"; 
  public static final String WEBSERVICE_UIDS = "WEBSERVICE_UIDS";

  public UpdateServiceAssignmentEvent(Long saUid,String password,String userType,Collection webServiceUIds)
  {
    setEventData(SERVICEASSIGNMENT_UID, saUid);
    setEventData(PASSWORD, password);
    setEventData(USER_TYPE, userType);
    setEventData(WEBSERVICE_UIDS, webServiceUIds);
  }

  public Long getServiceAssignmentUid()
  {
    return (Long)getEventData(SERVICEASSIGNMENT_UID);
  }

  public String getPassword()
  {
    return (String)getEventData(PASSWORD);
  }


  public Collection getWebServiceUIds()
  {
    return (Collection)getEventData(WEBSERVICE_UIDS);
  }

  public String getUserType()
  {
    return (String)getEventData(USER_TYPE);
  }  
  
  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateServiceAssignmentEvent";
  }

}