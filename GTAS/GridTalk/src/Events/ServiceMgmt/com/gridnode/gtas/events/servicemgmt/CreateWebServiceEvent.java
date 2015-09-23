/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateWebServiceEvent.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */

package com.gridnode.gtas.events.servicemgmt;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

 

public class CreateWebServiceEvent
  extends EventSupport
{
  
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1966275610768846014L;
	public static final String WSDL_URL = "WSDL_URL";
  public static final String END_POINT = "END_POINT";
  public static final String SERVICE_NAME = "SERVICE_NAME";
  public static final String SEVICE_GROUP = "SEVICE_GROUP";

  public CreateWebServiceEvent(String wsdlUrl, String endPoint,
                                String serviceName, String serviceGroup)
  {
    setEventData(WSDL_URL, wsdlUrl);
    setEventData(END_POINT, endPoint);
    setEventData(SERVICE_NAME, serviceName);
    setEventData(SEVICE_GROUP, serviceGroup);
  }


  public String getEndPoint()
  { 
    return (String)getEventData(END_POINT);
  }

  public String getServiceGroup()
  {
    return (String)getEventData(SEVICE_GROUP);
  }

  public String getServiceName()
  {
    return (String)getEventData(SERVICE_NAME);
  }

  public String getWsdlUrl()
  {
    return (String)getEventData(WSDL_URL);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateWebServiceEvent";
  }
  
}