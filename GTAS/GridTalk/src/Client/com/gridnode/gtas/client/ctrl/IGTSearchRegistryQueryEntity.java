/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.enterprise.ISearchRegistryQuery;

public interface IGTSearchRegistryQueryEntity extends IGTEntity
{
  //Fields
//  public static final Number UID              = ISearchRegistryQuery.UID;
  public static final Number SEARCH_ID        = ISearchRegistryQuery.SEARCH_ID;
  public static final Number DT_SUBMITTED     = ISearchRegistryQuery.DT_SUBMITTED;
  public static final Number DT_RESPONDED     = ISearchRegistryQuery.DT_RESPONDED;
  public static final Number CRITERIA         = ISearchRegistryQuery.CRITERIA;
  public static final Number RESULTS          = ISearchRegistryQuery.RESULTS;
  public static final Number IS_EXCEPTION     = ISearchRegistryQuery.IS_EXCEPTION;
  public static final Number EXCEPTION_STR    = ISearchRegistryQuery.EXCEPTION_STR;
}
