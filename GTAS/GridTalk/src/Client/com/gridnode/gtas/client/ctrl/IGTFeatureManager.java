/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTFeatureManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-07     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTFeatureManager extends IGTManager
{
  public IGTFeatureEntity getFeatureByFeature(String feature) throws GTClientException;
  public IGTFeatureEntity getFeatureByUID(long uid) throws GTClientException;
}