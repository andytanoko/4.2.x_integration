/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTExportConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-28     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

 
public interface IGTExportConfig extends IGTEntity
{
  public static final Number EXPORT_FILE = new Integer(-10);
  public static final Number EXPORTABLE_ENTITIES = new Integer(-20);
  public static final Number SELECTED_ENTITIES = new Integer(-30);
}