/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateProcedureDefFileEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh              Created.
 */


package com.gridnode.gtas.events.userprocedure;

/**
 * This event class contains the data for creation of a ProcedureDefiniton File.
 *
 *
 * @author Jagadeesh.
 *
 * @version 2.0
 * @since 2.0
 */
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class CreateProcedureDefFileEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5466038543891395581L;

	public static final String NAME = "ProcedureDefFile Name";

  public static final String DESCRIPTION = "Description";

  public static final String TYPE = "ProcedureDefFile Type";

  public static final String FILE_NAME = "FileName";

//  public static final String FILE_PATH = "FilePath";

  public CreateProcedureDefFileEvent(
    String name,
    String description,
    Integer    type,
    String filename)
    throws EventException
  {
    checkSetString(NAME,name);
    checkSetString(DESCRIPTION,description);
    checkSetInteger(TYPE,type);
    checkSetString(FILE_NAME,filename);
  }

  public String getName()
  {
    return (String) getEventData(NAME);
  }

  public String getDescription()
  {
    return (String) getEventData(DESCRIPTION);
  }

  public int getType()
  {
    return ((Integer) getEventData(TYPE)).intValue();
  }

  public String getFileName()
  {
    return (String) getEventData(FILE_NAME);
  }

/*  public String getFilePath()
  {
    return (String) getEventData(FILE_PATH);
  }
*/
  public String getEventName()
  {
    return "java:comp/env/param/event/CreateProcedureDefFileEvent";
  }
}






