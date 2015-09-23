/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSearchQueryEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.searchquery.ISearchQuery;

public interface IGTSearchQueryEntity extends IGTEntity
{
  //Fields
  public static final Number UID                = ISearchQuery.UID;         // Integer(?)
  public static final Number CAN_DELETE         = ISearchQuery.CAN_DELETE;  // Boolean
  public static final Number NAME               = ISearchQuery.NAME;        // String(30)
  public static final Number DESCRIPTION        = ISearchQuery.DESCRIPTION; // String(80)
  public static final Number CREATED_BY         = ISearchQuery.CREATED_BY;  // String(80)
  public static final Number CONDITIONS         = ISearchQuery.CONDITIONS;  // ArrayList
  public static final Number IS_PUBLIC          = ISearchQuery.IS_PUBLIC;   // boolean
}
