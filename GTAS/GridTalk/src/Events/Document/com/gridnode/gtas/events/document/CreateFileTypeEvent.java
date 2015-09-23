/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateFileTypeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 23 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the creation of new FileType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateFileTypeEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4084252907384922053L;
	public static final String FILE_TYPE_NAME = "FileType Name";
  public static final String FILE_TYPE_DESC = "FileType Desc";
  public static final String PROGRAM_NAME   = "Program Name";
  public static final String PROGRAM_PATH   = "Program Path";
  public static final String PARAMETERS     = "Parameters";
  public static final String WORKING_DIR    = "Working Directory";

  public CreateFileTypeEvent(String fileTypeName,
                             String fileTypeDesc,
                             String programName,
                             String programPath,
                             String parameters,
                             String workingDir)
  {
    setEventData(FILE_TYPE_NAME, fileTypeName);
    setEventData(FILE_TYPE_DESC, fileTypeDesc);
    setEventData(PROGRAM_NAME, programName);
    setEventData(PROGRAM_PATH, programPath);
    setEventData(PARAMETERS, parameters);
    setEventData(WORKING_DIR, workingDir);
  }

  public String getFileTypeName()
  {
    return (String)getEventData(FILE_TYPE_NAME);
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
    return "java:comp/env/param/event/CreateFileTypeEvent";
  }

}