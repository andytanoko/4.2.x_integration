/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateWebServiceEvent.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */

package com.gridnode.gtas.events.servicemgmt;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class UpdateWebServiceEvent
  extends EventSupport
{
  
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5781068490995881093L;
	public static final String WEBSERVICE_UID  = "WEBSERVICE_UID";
  public static final String WSDL_URL = "WSDL_URL";
  public static final String END_POINT = "END_POINT";
  public static final String SEVICE_GROUP = "SEVICE_GROUP";

  public UpdateWebServiceEvent(Long wsUid, String wsdlUrl,
                                String endPoint,String serviceGroup)
  {
    setEventData(WEBSERVICE_UID, wsUid);
    setEventData(WSDL_URL, wsdlUrl);
    setEventData(END_POINT, endPoint);
    setEventData(SEVICE_GROUP, serviceGroup);
  }

  public Long getWebServiceUid()
  {
    return (Long)getEventData(WEBSERVICE_UID);
  }

  public String getEndPoint()
  { 
    return (String)getEventData(END_POINT);
  }

  public String getWsdlUrl()
  {
    return (String)getEventData(WSDL_URL);
  }
  
  public String getServiceGroup()
  {
    return (String)getEventData(SEVICE_GROUP);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateWebServiceEvent";
  }

}