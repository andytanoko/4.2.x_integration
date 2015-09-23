/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetChannelInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 08 2002    Goh Kan Mun             Created
 */
package com.gridnode.gtas.events.channel;

/**
 * This event class contains the data for creation of a ChannelInfo.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class GetChannelInfoEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7491863282393708245L;
	/**
   * FieldId for UId.
   */
  public static final String UID = "uId";

  public GetChannelInfoEvent(Long uId)
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
    return "java:comp/env/param/event/GetChannelInfoEvent";
  }


}