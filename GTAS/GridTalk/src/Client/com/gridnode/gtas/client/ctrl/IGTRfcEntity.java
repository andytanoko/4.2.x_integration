/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTRfcEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.backend.IRfc;

public interface IGTRfcEntity extends IGTEntity
{
  //CONNECTION_TYPE constants
  public static final String CONNECTION_TYPE_TCPIP  = IRfc.TCPIP;

  //Fields
  public static final Number UID              = IRfc.UID;
  public static final Number CAN_DELETE       = IRfc.CAN_DELETE;
  public static final Number NAME             = IRfc.NAME;
  public static final Number DESCRIPTION      = IRfc.DESCRIPTION;
  //public static final Number CONNECTION_TYPE  = IRfc.CONNECTION_TYPE;
  public static final Number HOST             = IRfc.HOST;
  public static final Number PORT_NUMBER      = IRfc.PORT_NUMBER;
  public static final Number COMMAND_FILE_DIR = IRfc.COMMAND_FILE_DIR;
  public static final Number COMMAND_FILE     = IRfc.COMMAND_FILE;
  public static final Number COMMAND_LINE     = IRfc.COMMAND_LINE;
  public static final Number USE_COMMAND_FILE = IRfc.USE_COMMAND_FILE;
}
