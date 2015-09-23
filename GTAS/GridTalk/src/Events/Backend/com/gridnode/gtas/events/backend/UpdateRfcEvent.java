/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateRfcEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.backend;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the updating of a Rfc.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateRfcEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2802436644689755077L;
	public static final String RFC_UID            = "Rfc Uid";
  public static final String DESCRIPTION        = "Description";
  public static final String HOST               = "Host";
  public static final String CONNECTION_TYPE    = "Connection Type";
  public static final String PORT_NUMBER        = "Port Number";
  public static final String USE_COMMAND_FILE   = "Use Command File";
  public static final String COMMAND_FILE_DIR   = "Command File Dir";
  public static final String COMMAND_FILE       = "Command File";
  public static final String COMMAND_LINE       = "Command Line";

  public UpdateRfcEvent(Long rfcUid,
                        String description,
                        String host,
                        String connectionType,
                        Integer portNumber,
                        Boolean useCommandFile,
                        String commandFileDir,
                        String commandFile,
                        String commandLine)
  {
    setEventData(RFC_UID, rfcUid);
    setEventData(DESCRIPTION, description);
    setEventData(HOST, host);
    setEventData(CONNECTION_TYPE, connectionType);
    setEventData(PORT_NUMBER, portNumber);
    setEventData(USE_COMMAND_FILE, useCommandFile);
    setEventData(COMMAND_FILE_DIR, commandFileDir);
    setEventData(COMMAND_FILE, commandFile);
    setEventData(COMMAND_LINE, commandLine);
  }

  public Long getRfcUid()
  {
    return (Long)getEventData(RFC_UID);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

  public String getHost()
  {
    return (String)getEventData(HOST);
  }

  public String getConnectionType()
  {
    return (String)getEventData(CONNECTION_TYPE);
  }

  public Integer getPortNumber()
  {
    return (Integer)getEventData(PORT_NUMBER);
  }

  public Boolean getUseCommandFile()
  {
    return (Boolean)getEventData(USE_COMMAND_FILE);
  }

  public String getCommandFileDir()
  {
    return (String)getEventData(COMMAND_FILE_DIR);
  }

  public String getCommandFile()
  {
    return (String)getEventData(COMMAND_FILE);
  }

  public String getCommandLine()
  {
    return (String)getEventData(COMMAND_LINE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateRfcEvent";
  }

}