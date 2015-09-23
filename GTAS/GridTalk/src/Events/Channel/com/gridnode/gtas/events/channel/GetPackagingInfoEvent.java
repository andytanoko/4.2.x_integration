/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPackagingInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh               Created
 */


package com.gridnode.gtas.events.channel;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This event class returns the data for PackagingInfo.
 *
 *
 * @author Jagadeesh.
 *
 * @version 2.0
 * @since 2.0
 */

public class GetPackagingInfoEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6030841320985531564L;
	/**
   * FieldId for UId.
   */
  public static final String UID = "uId";

  public GetPackagingInfoEvent(Long uId)
    throws EventException
  {
    checkSetLong(UID, uId);
  }

  public Long getUId()
  {
    return (Long) getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetPackagingInfoEvent";
  }


}

