/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFeatureEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 19 2002    Neo Sok Lay             Created
 */
package com.gridnode.gtas.events.acl;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a Feature based on
 * the UID or name of the Feature.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */

public class GetFeatureEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -604127066937073588L;
	public static final String FEATURE_NAME = "Feature Name";
  public static final String FEATURE_UID  = "Feature UID";

  public GetFeatureEvent(String featureName) throws EventException
  {
    checkSetString(FEATURE_NAME, featureName);
  }

  public GetFeatureEvent(Long featureUID) throws EventException
  {
    checkSetLong(FEATURE_UID, featureUID);
  }

  public String getFeatureName()
  {
    return (String) getEventData(FEATURE_NAME);
  }

  public Long getFeatureUID()
  {
    return (Long)getEventData(FEATURE_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetFeatureEvent";
  }

}