/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateMappingFileEvent.java
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
 * This Event class contains the data for the creation of new MappingFile.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateMappingFileEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4189222234769181855L;
	public static final String MAPPING_FILE_NAME			= "MappingFile Name";
  public static final String MAPPING_FILE_DESC			= "MappingFile Desc";
  public static final String MAPPING_FILE_PATH			= "MappingFile Path";
  public static final String MAPPING_FILE_SUB_PATH	= "MappingFile SubPath";
  public static final String MAPPING_FILE_TYPE			= "MappingFile Type";

  public CreateMappingFileEvent(String mappingFileName,
                                String mappingFileDesc,
                                String mappingFilePath,
                                String mappingFileSubPath,
                                Short mappingFileType)
  {
    setEventData(MAPPING_FILE_NAME, mappingFileName);
    setEventData(MAPPING_FILE_DESC, mappingFileDesc);
    setEventData(MAPPING_FILE_PATH, mappingFilePath);
    setEventData(MAPPING_FILE_SUB_PATH, mappingFileSubPath);
    setEventData(MAPPING_FILE_TYPE, mappingFileType);
  }

  public String getMappingFileName()
  {
    return (String)getEventData(MAPPING_FILE_NAME);
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
    return "java:comp/env/param/event/CreateMappingFileEvent";
  }

}