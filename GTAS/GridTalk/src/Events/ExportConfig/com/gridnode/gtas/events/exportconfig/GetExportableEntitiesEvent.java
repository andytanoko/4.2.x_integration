/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetExportableEntitiesEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 08 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.exportconfig;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class will retrieve a list of all exportable entites in GTAS.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class GetExportableEntitiesEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1474517916951618277L;

	public GetExportableEntitiesEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetExportableEntitiesEvent";
  }

}