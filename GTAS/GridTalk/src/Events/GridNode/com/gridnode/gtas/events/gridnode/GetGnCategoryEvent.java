/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGnCategoryEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.gridnode;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a GnCategory based on
 * CategoryCode.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetGnCategoryEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7549088262291402117L;
	public static final String CATEGORY_CODE  = "Category Code";

  public GetGnCategoryEvent(String catCode)
    throws EventException
  {
    checkSetString(CATEGORY_CODE, catCode);
  }

  public String getCategoryCode()
  {
    return (String)getEventData(CATEGORY_CODE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetGnCategoryEvent";
  }

}