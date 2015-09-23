/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IHousekeepingInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2003      Mahesh         Created
 * Jun 27,2006    Tam Wei Xiang    Added new field 'WF_RECORDS_DAYS_TO_KEEP'
 */ 
package com.gridnode.gtas.model.housekeeping;

public interface IHousekeepingInfo
{
  public static final String ENTITY_NAME = "HousekeepingInfo";

  public static final Number TEMP_FILES_DAYS_TO_KEEP = new Integer(1);
  public static final Number LOG_FILES_DAYS_TO_KEEP = new Integer(2);
  public static final Number WF_RECORDS_DAYS_TO_KEEP = new Integer(3);
}
