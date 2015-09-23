/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFeatureListEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 28 2002    Goh Kan Mun             Created
 */
package com.gridnode.gtas.events.acl;

import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;

/** 
 * This Event class contains the data for retrieve a list of Feature.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GetFeatureListEvent extends GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5121559794312341264L;
	public static final String GET_ROLE_NAME = "Get List Name";

  public GetFeatureListEvent()
  {
    super();
  }

//  public GetFeatureListEvent(IDataFilter filter)
//  {
//    super(filter);
//  }
//
//  public GetFeatureListEvent(IDataFilter filter, int maxRows)
//  {
//    super(filter, maxRows);
//  }
//
//  public GetFeatureListEvent(IDataFilter filter, int maxRows, int startRow)
//  {
//    super(filter, maxRows, startRow);
//  }
//
//  public GetFeatureListEvent(String listID, int maxRows, int startRow)
//  {
//    super(listID, maxRows, startRow);
//  }
//
  public String getEventName()
  {
    return "java:comp/env/param/event/GetFeatureListEvent";
  }

}