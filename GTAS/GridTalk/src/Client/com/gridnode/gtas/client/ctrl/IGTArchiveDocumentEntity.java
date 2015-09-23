/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTArchiveDocumentEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-17     Andrew Hill         Created
 * 2002-09-22     Daniel D'Cotta      Commented out GDOC_FILTER, added re-designed 
 *                                    ProcessInstance and GridDocument options
 * 2006-09-14     Tam Wei Xiang       Added ENABLE_SEARCH_ARCHIVED, ENABLE_RESTORE_ARCHIVED,
 *                                    PARTNER_ID, PARTNER                                    
 */

package com.gridnode.gtas.client.ctrl;


public interface IGTArchiveDocumentEntity extends IGTEntity
{
  /*
  public static final Number ARCHIVE_FILE       = new Integer(-10);
  public static final Number ARCHIVE_FILE_PATH  = new Integer(-20);
  public static final Number GDOC_FILTER        = new Integer(-30);
  public static final Number GDOC_NUM           = new Integer(-40);
  */

  public static final Number NAME               = new Integer(-50);
  public static final Number DESCRIPTION        = new Integer(-60);
  public static final Number FROM_DATE          = new Integer(-70);
  public static final Number FROM_TIME          = new Integer(-75);
  public static final Number TO_DATE            = new Integer(-80);
  public static final Number TO_TIME            = new Integer(-85);
  public static final Number ARCHIVE_TYPE       = new Integer(-90);

  public static final Number PROCESS_DEF        = new Integer(-100);
  public static final Number INCLUDE_INCOMPLETE = new Integer(-110);
  public static final Number PARTNER         = new Integer(-111); //TWX 15092006
  
  public static final Number FOLDER             = new Integer(-120);
  public static final Number DOC_TYPE           = new Integer(-130);
  
  public static final Number ENABLE_SEARCH_ARCHIVED = new Integer(-140); //TWX 14092006
  public static final Number ENABLE_RESTORE_ARCHIVED = new Integer(-150); // ""
  
  // Constants for ARCHIVE_TYPE
  public static final String ARCHIVE_TYPE_PROCESS_INSTANCE  = "ProcessInstance";
  public static final String ARCHIVE_TYPE_DOCUMENT          = "Document";
}