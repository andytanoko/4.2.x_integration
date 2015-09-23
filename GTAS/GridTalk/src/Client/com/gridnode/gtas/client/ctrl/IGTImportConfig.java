/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTImportConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-28     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;


public interface IGTImportConfig extends IGTEntity
{
  public static final Number IMPORT_FILE = new Integer(-10);
  public static final Number IS_OVERWRITE = new Integer(-20);
  public static final Number CONFLICTING_ENTITIES = new Integer(-30);
  public static final Number OVERWRITE_ENTITIES = new Integer(-40);
  
}