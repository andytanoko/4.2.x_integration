/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTFeatureEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-07     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.acl.IFeature;

public interface IGTFeatureEntity extends IGTEntity
{
  public static final Number UID          = IFeature.UID;
  public static final Number FEATURE      = IFeature.FEATURE;
  public static final Number DESCRIPTION  = IFeature.DESCRIPTION;
  public static final Number ACTIONS      = IFeature.ACTIONS;
  public static final Number DATA_TYPES   = IFeature.DATA_TYPES;
}