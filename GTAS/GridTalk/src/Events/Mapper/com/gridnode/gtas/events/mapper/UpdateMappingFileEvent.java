/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateMappingFileEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 * Feb 26 2004    Daniel D'Cotta			Added SubPath
 */
package com.gridnode.gtas.events.mapper;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the updating of MappingFile.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateMappingFileEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -355898570165569291L;
	public static final String MAPPING_FILE_UID				= "MappingFile UID";
  public static final String MAPPING_FILE_DESC			= "MappingFile Desc";
  public static final String MAPPING_FILE_PATH			= "MappingFile Path";
  public static final String MAPPING_FILE_SUB_PATH	= "MappingFile SubPath";
  public static final String MAPPING_FILE_TYPE			= "MappingFile Type";

  public UpdateMappingFileEvent(Long mappingFileUID,
                                String mappingFileDesc,
                                String mappingFilePath,
                                String mappingFileSubPath,
                                Short mappingFileType)
  {
    setEventData(MAPPING_FILE_UID, mappingFileUID);
    setEventData(MAPPING_FILE_DESC, mappingFileDesc);
    setEventData(MAPPING_FILE_PATH, mappingFilePath);
    setEventData(MAPPING_FILE_SUB_PATH, mappingFileSubPath);
    setEventData(MAPPING_FILE_TYPE, mappingFileType);
  }

  public Long getMappingFileUID()
  {
    return (Long)getEventData(MAPPING_FILE_UID);
  }

  public String getMappingFileDesc()
  {
    return (String)getEventData(MAPPING_FILE_DESC);
  }

  public String getMappingFilePath()
  {
    return (String)getEventData(MAPPING_FILE_PATH);
  }

  public String getMappingFileSubPath()
  {
    return (String)getEventData(MAPPING_FILE_SUB_PATH);
  }

  public Short getMappingFileType()
  {
    return (Short)getEventData(MAPPING_FILE_TYPE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateMappingFileEvent";
  }

}