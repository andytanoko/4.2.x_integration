/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreatePortEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 * May 26 2003    Jagadeesh           Added- Fileds for Port Enchancement.
 * Aug 23 2005    Tam Wei Xiang       Instance variable ATTACHMENT_DIR:String has 
 *                                    been removed.
 * Mar 03 2006    Tam Wei Xiang       Added field 'FileGrouping'                                   
 */
package com.gridnode.gtas.events.backend;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the creation of new Port.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class CreatePortEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1221245496902099391L;
	public static final String NAME               = "Name";
  public static final String DESCRIPTION        = "Description";
  public static final String IS_RFC             = "Is Rfc";
  public static final String RFC_UID            = "Rfc Uid";
  public static final String HOST_DIR           = "Host Dir";
  public static final String IS_DIFF_FILE_NAME  = "Is Diff Filename";
  public static final String IS_OVERWRITE       = "Is Overwrite";
  public static final String FILENAME           = "Filename";
  public static final String IS_ADD_FILE_EXT    = "Is Add File Ext";
  public static final String FILE_EXT_TYPE      = "File Ext Type";
  public static final String FILE_EXT_VALUE     = "File Ext Value";
  //public static final String ATTACHMENT_DIR     = "Attachment Dir";
  public static final String IS_EXPORT_GDOC     = "Is Export Gdoc";
  public static final String START_NUM          = "Start Number";
  public static final String ROLLOVER_NUM       = "RollOver Number";
  public static final String NEXT_NUM           = "Next Number";
  public static final String IS_PADDED          = "Is Padded";
  public static final String FIXEDNUM_LENGTH    = "Fixed Number Length";
  public static final String FILE_GROUPING      = "File Grouping";

  public CreatePortEvent(String name,
                         String description,
                         Boolean isRfc,
                         Long rfcUid,
                         String hostDir,
                         Boolean isDiffFileName,
                         Boolean isOverwrite,
                         String fileName,
                         Boolean isAddFileExt,
                         Integer fileExtType,
                         String fileExtValue,
                         Boolean isExportGdoc,
                         Integer startNum,
                         Integer rollOverNum,
                         Integer nextNum,
                         Boolean isPadded,
                         Integer fixedNum,
                         Integer fileGrouping)
  {
    setEventData(NAME, name);
    setEventData(DESCRIPTION, description);
    setEventData(IS_RFC, isRfc);
    setEventData(RFC_UID, rfcUid);
    setEventData(HOST_DIR, hostDir);
    setEventData(IS_DIFF_FILE_NAME, isDiffFileName);
    setEventData(IS_OVERWRITE, isOverwrite);
    setEventData(FILENAME, fileName);
    setEventData(IS_ADD_FILE_EXT, isAddFileExt);
    setEventData(FILE_EXT_TYPE, fileExtType);
    setEventData(FILE_EXT_VALUE, fileExtValue);
    //setEventData(ATTACHMENT_DIR, attachmentDir);
    setEventData(IS_EXPORT_GDOC, isExportGdoc);
    setEventData(START_NUM,startNum);
    setEventData(ROLLOVER_NUM,rollOverNum);
    setEventData(NEXT_NUM,nextNum);
    setEventData(IS_PADDED,isPadded);
    setEventData(FIXEDNUM_LENGTH,fixedNum);
    setEventData(FILE_GROUPING, fileGrouping);
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

  public Boolean getIsRfc()
  {
    return (Boolean)getEventData(IS_RFC);
  }

  public Long getRfcUid()
  {
    return (Long)getEventData(RFC_UID);
  }

  public String getHostDir()
  {
    return (String)getEventData(HOST_DIR);
  }

  public Boolean getIsDiffFileName()
  {
    return (Boolean)getEventData(IS_DIFF_FILE_NAME);
  }

  public Boolean getIsOverwrite()
  {
    return (Boolean)getEventData(IS_OVERWRITE);
  }

  public String getFilename()
  {
    return (String)getEventData(FILENAME);
  }

  public Boolean getIsAddFileExt()
  {
    return (Boolean)getEventData(IS_ADD_FILE_EXT);
  }

  public Integer getFileExtType()
  {
    return (Integer)getEventData(FILE_EXT_TYPE);
  }

  public String getFileExtValue()
  {
    return (String)getEventData(FILE_EXT_VALUE);
  }
  
  /*
  public String getAttachmentDir()
  {
    return (String)getEventData(ATTACHMENT_DIR);
  } */

  public Boolean getIsExportGdoc()
  {
    return (Boolean)getEventData(IS_EXPORT_GDOC);
  }

  public Integer getStartNumber()
  {
    return (Integer)getEventData(START_NUM);
  }

  public Integer getRollOverNumber()
  {
    return (Integer)getEventData(ROLLOVER_NUM);
  }

  public Integer getNextNumber()
  {
    return (Integer)getEventData(NEXT_NUM);
  }

  public Boolean getIsPadded()
  {
    return (Boolean)getEventData(IS_PADDED);
  }

  public Integer getFixedNumberLength()
  {
    return (Integer)getEventData(FIXEDNUM_LENGTH);
  }

  public Integer getFileGrouping()
  {
  	return (Integer)getEventData(FILE_GROUPING);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/CreatePortEvent";
  }

}