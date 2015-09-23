/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateRegistryConnectInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 24 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This event contains the data for creating a RegistryConnectInfo.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class CreateRegistryConnectInfoEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5284417904360549335L;
	public static final String NAME = "Name";
  public static final String QUERY_URL = "Query Url";
  public static final String PUBLISH_URL = "Publish Url";
  public static final String USER_NAME = "Username";
  public static final String PASSWORD = "Password";
  
  /**
   * Constructs a CreateRegistryConnectInfoEvent
   * 
   * @param name The name of the RegistryConnectInfo to create/update.
   * @param queryUrl The Query URL to the registry. 
   * @param publishUrl The Publish URL to the registry.
   * @param username The Username for authentication on the registry during publish.
   * @param password The Password for authentication on the registry during publish.
   * @throws EventException Invalid input arameters 
   */
  public CreateRegistryConnectInfoEvent(
    String name,
    String queryUrl,
    String publishUrl,
    String username,
    String password)
    throws EventException
  {
    checkSetString(NAME, name);
    checkSetString(QUERY_URL, queryUrl);
    checkValidURL(QUERY_URL, queryUrl);
    if (publishUrl != null && publishUrl.trim().length() > 0)
    {
      checkSetString(USER_NAME, username);
      checkSetString(PASSWORD, password);
      checkSetString(PUBLISH_URL, publishUrl);
      checkValidURL(PUBLISH_URL, publishUrl);
    }
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }
  
  public String getQueryUrl()
  {
    return (String)getEventData(QUERY_URL);
  }
  
  public String getPublishUrl()
  {
    return (String)getEventData(PUBLISH_URL);
  }
  
  public String getUsername()
  {
    return (String)getEventData(USER_NAME);
  }
  
  public String getPassword()
  {
    return (String)getEventData(PASSWORD);
  }
  
  /**
   * @see com.gridnode.pdip.framework.rpf.event.IEvent#getEventName()
   */
  public String getEventName()
  {
    return "java:comp/env/param/event/CreateRegistryConnectInfoEvent";
  }

}
