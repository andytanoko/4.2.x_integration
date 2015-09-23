/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateServiceAssignmentEvent.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */

package com.gridnode.gtas.events.servicemgmt;

import java.util.Collection;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
 

public class CreateServiceAssignmentEvent
  extends EventSupport
{
  
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1079195603829353038L;
	public static final String USER_NAME = "USER_NAME";
  public static final String PASSWORD = "PASSWORD";
  public static final String USER_TYPE = "USER_TYPE";
  public static final String WEBSERVICE_UIDS = "WEBSERVICE_UIDS";

  public CreateServiceAssignmentEvent(String userName, String password,
                                String userType, Collection webServiceUIds)
  {
    setEventData(USER_NAME, userName);
    setEventData(PASSWORD, password);
    setEventData(USER_TYPE, userType);
    setEventData(WEBSERVICE_UIDS, webServiceUIds);
  }

  public String getPassword()
  {
    return (String)getEventData(PASSWORD);
  }

  public String getUserName()
  {
    return (String)getEventData(USER_NAME);
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
    return "java:comp/env/param/event/CreateServiceAssignmentEvent";
  }

}