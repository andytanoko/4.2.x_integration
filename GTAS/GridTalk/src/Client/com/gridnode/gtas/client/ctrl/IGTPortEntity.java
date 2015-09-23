/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPortEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 * 2002-05-22     Daniel D'Cotta      Added running sequence number support
 * 2005-08-23     Tam Wei Xiang       Variable ATTACHMENT_DIR:Number has 
 *                                    been removed.(GDOC will be put in the same 
 *                                    folder as UDOC)
 * 2006-03-03     Tam Wei Xiang       Added new field 'fileGrouping'. This give more
 *                                    option for user to choose the way they organize
 *                                    exported gdoc, udoc, and attachments                                  
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.backend.IPort;
 
public interface IGTPortEntity extends IGTEntity
{
  //FILE_EXT_TYPE constants
  public static final Integer FILE_EXT_TYPE_DATE_TIME   = IPort.DATE_TIME;
  public static final Integer FILE_EXT_TYPE_GDOC        = IPort.GDOC;
  public static final Integer FILE_EXT_TYPE_SEQUENCE    = IPort.SEQ_RUNNING_NUM;
  
  //FILE_GROUPING_OPTION constants
  public static final Integer FILE_GROUPING_OPTION_FLAT = 1; //Files will be placed under hostDirectory
  public static final Integer FILE_GROUPING_OPTION_ATTACHMENT_GDOC = 2; //udoc will be place under hostDirectory. Gdoc and attachment will be placed under udoc name dir or port.Filename
  public static final Integer FILE_GROUPING_OPTION_GROUP_ALL = 3; //Files will be placed in udoc named dir or based on port.Filename.
  
  //Fields
  public static final Number UID                = IPort.UID;
  public static final Number CAN_DELETE         = IPort.CAN_DELETE;
  public static final Number NAME               = IPort.NAME;
  public static final Number DESCRIPTION        = IPort.DESCRIPTION;
  public static final Number HOST_DIR           = IPort.HOST_DIR;
  public static final Number IS_RFC             = IPort.IS_RFC;
  public static final Number RFC                = IPort.RFC;
  public static final Number IS_DIFF_FILE_NAME  = IPort.IS_DIFF_FILE_NAME;
  public static final Number IS_OVERWRITE       = IPort.IS_OVERWRITE;
  public static final Number FILE_NAME          = IPort.FILE_NAME;
  public static final Number IS_ADD_FILE_EXT    = IPort.IS_ADD_FILE_EXT;
  public static final Number FILE_EXT_TYPE      = IPort.FILE_EXT_TYPE;
  public static final Number FILE_EXT_VALUE     = IPort.FILE_EXT_VALUE;
  public static final Number IS_EXPORT_GDOC     = IPort.IS_EXPORT_GDOC;
  public static final Number START_NUM          = IPort.START_NUM;
  public static final Number ROLLOVER_NUM       = IPort.ROLLOVER_NUM;
  public static final Number NEXT_NUM           = IPort.NEXT_NUM;
  public static final Number IS_PADDED          = IPort.IS_PADDED;
  public static final Number FIX_NUM_LENGTH     = IPort.FIX_NUM_LENGTH;
  public static final Number FILE_GROUPING      = IPort.FILE_GROUPING;
}
