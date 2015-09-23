/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSubstitutionListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 06 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
/**
 * This Event class contains the data for retrieving SubstitutionList
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetSubstitutionListEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1162278136486535349L;

	public GetSubstitutionListEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetSubstitutionListEvent";
  }
}