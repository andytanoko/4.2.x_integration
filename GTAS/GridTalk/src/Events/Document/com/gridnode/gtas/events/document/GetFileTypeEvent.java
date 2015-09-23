/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFileTypeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 23 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a FileType based on
 * UID.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetFileTypeEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3923005623016732886L;
	public static final String FILE_TYPE_UID  = "FileType UID";

  public GetFileTypeEvent(Long fileTypeUID)
  {
    setEventData(FILE_TYPE_UID, fileTypeUID);
  }

  public Long getFileTypeUID()
  {
    return (Long)getEventData(FILE_TYPE_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetFileTypeEvent";
  }

}