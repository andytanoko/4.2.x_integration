/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HousekeepingFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2003      Mahesh         Created
 * Jun 27 2006    Tam Wei Xiang       Added new field 'WF_RECORDS_DAYS_TO_KEEP'
 */ 
package com.gridnode.gtas.model.housekeeping;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Housekeeping module.
 * The fieldIDs will be the ones that are available for the client to access.
 */
public class HousekeepingFieldID
{
  private Hashtable _table;
  private static final HousekeepingFieldID _self= new HousekeepingFieldID();

  private HousekeepingFieldID()
  {
    _table= new Hashtable();
    _table.put(
      IHousekeepingInfo.ENTITY_NAME,
      new Number[] {
				IHousekeepingInfo.TEMP_FILES_DAYS_TO_KEEP,
				IHousekeepingInfo.LOG_FILES_DAYS_TO_KEEP,
				IHousekeepingInfo.WF_RECORDS_DAYS_TO_KEEP
        });
  }

  public static Hashtable getEntityFieldID()
  {
    return _self._table;
  }
}