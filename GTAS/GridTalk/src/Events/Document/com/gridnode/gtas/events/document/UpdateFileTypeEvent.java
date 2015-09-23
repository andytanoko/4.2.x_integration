/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateFileTypeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 23 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for updating a FileType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateFileTypeEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -575117242765580928L;
	public static final String FILE_TYPE_UID  = "FileType UID";
  public static final String FILE_TYPE_DESC = "FileType Desc";
  public static final String PROGRAM_NAME   = "Program Name";
  public static final String PROGRAM_PATH   = "Program Path";
  public static final String PARAMETERS     = "Parameters";
  public static final String WORKING_DIR    = "Working Directory";

  public UpdateFileTypeEvent(Long fileTypeUID,
                             String fileTypeDesc,
                             String programName,
                             String programPath,
                             String parameters,
                             String workingDir)
  {
    setEventData(FILE_TYPE_UID, fileTypeUID);
    setEventData(FILE_TYPE_DESC, fileTypeDesc);
    setEventData(PROGRAM_NAME, programName);
    setEventData(PROGRAM_PATH, programPath);
    setEventData(PARAMETERS, parameters);
    setEventData(WORKING_DIR, workingDir);
  }

  public Long getFileTypeUID()
  {
    return (Long)getEventData(FILE_TYPE_UID);
  }

  public String getFileTypeDesc()
  {
    return (String)getEventData(FILE_TYPE_DESC);
  }

  public String getProgramName()
  {
    return (String)getEventData(PROGRAM_NAME);
  }

  public String getProgramPath()
  {
    return (String)getEventData(PROGRAM_PATH);
  }

  public String getParameters()
  {
    return (String)getEventData(PARAMETERS);
  }

  public String getWorkingDir()
  {
    return (String)getEventData(WORKING_DIR);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateFileTypeEvent";
  }

}