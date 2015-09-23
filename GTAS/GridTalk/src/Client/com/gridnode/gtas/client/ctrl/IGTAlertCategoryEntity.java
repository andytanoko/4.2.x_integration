/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTAlertCategoryEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-02-04     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.alert.IAlertCategory;
 
public interface IGTAlertCategoryEntity extends IGTEntity
{
  // Fields
  public static final Number UID          = IAlertCategory.UID;
  public static final Number CODE         = IAlertCategory.CODE;
  public static final Number NAME         = IAlertCategory.NAME;
  public static final Number DESCRIPTION  = IAlertCategory.DESCRIPTION;
}
