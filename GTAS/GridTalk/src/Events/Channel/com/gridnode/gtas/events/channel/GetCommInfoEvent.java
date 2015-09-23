/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCommInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 18 2002    Goh Kan Mun             Created
 */
package com.gridnode.gtas.events.channel;

/**
 * This event class contains the data for creation of a CommInfo.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class GetCommInfoEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4897791035393748891L;
	/**
   * FieldId for UId.
   */
  public static final String UID = "uId";

  public GetCommInfoEvent(Long uId)
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
    return "java:comp/env/param/event/GetCommInfoEvent";
  }


}