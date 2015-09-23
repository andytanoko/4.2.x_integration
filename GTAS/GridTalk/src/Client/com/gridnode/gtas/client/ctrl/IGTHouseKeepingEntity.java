/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTHouseKeepingEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-01-14     Daniel D'Cotta      Created
 * 2004-09-16     Mahesh              Modified to remove some fields
 * 2006-06-27     Tam Wei Xiang       Added new field 'WF_RECORDS_DAYS_TO_KEEP'
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.housekeeping.IHousekeepingInfo;

public interface IGTHouseKeepingEntity extends IGTEntity
{
  public static final Number TEMP_FILES_DAYS_TO_KEEP        = IHousekeepingInfo.TEMP_FILES_DAYS_TO_KEEP;
  public static final Number LOG_FILES_DAYS_TO_KEEP         = IHousekeepingInfo.LOG_FILES_DAYS_TO_KEEP;
  public static final Number WF_RECORDS_DAYS_TO_KEEP        = IHousekeepingInfo.WF_RECORDS_DAYS_TO_KEEP;
}