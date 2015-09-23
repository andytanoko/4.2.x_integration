/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSearchGridNodeQueryEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.activation.ISearchGridNodeQuery;

public interface IGTSearchGridNodeQueryEntity extends IGTEntity
{
  public static final Number SEARCH_ID      = ISearchGridNodeQuery.SEARCH_ID;
  public static final Number DT_SUBMITTED   = ISearchGridNodeQuery.DT_SUBMITTED;
  public static final Number DT_RESPONDED   = ISearchGridNodeQuery.DT_RESPONDED;
  public static final Number CRITERIA       = ISearchGridNodeQuery.CRITERIA;
  public static final Number RESULTS        = ISearchGridNodeQuery.RESULTS;
}