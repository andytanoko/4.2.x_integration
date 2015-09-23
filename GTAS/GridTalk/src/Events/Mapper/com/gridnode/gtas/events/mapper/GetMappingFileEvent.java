/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMappingFileEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.mapper;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the retrieving of a MappingFile.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetMappingFileEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2879053271069830691L;
	public static final String MAPPING_FILE_UID = "MappingFile UID";

  public GetMappingFileEvent(Long mappingFileUID)
  {
    setEventData(MAPPING_FILE_UID, mappingFileUID);
  }

  public Long getMappingFileUID()
  {
    return (Long)getEventData(MAPPING_FILE_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetMappingFileEvent";
  }

}