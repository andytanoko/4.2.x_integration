/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTGnCategoryEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-10     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.gridnode.IGnCategory;

public interface IGTGnCategoryEntity extends IGTEntity
{
  public static final Number CATEGORY_CODE = IGnCategory.CATEGORY_CODE;
  public static final Number CATEGORY_NAME = IGnCategory.CATEGORY_NAME;
}