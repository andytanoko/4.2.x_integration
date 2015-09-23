/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTExportConfigManager.java.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 28/05/2003     Andrew Hill         Created 
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTExportConfigManager
{
  public IGTImportConfig getImportConfig() throws GTClientException;
    
  public void importConfig(IGTImportConfig importConfig)
    throws GTClientException;
}
