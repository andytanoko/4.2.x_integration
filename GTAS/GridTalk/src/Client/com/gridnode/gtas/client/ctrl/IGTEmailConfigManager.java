/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEmailConfigManager.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTEmailConfigManager extends IGTManager
{
  public IGTEmailConfigEntity getEmailConfig() throws GTClientException; 
  public void saveEmailConfig(IGTEmailConfigEntity emailConfig) throws GTClientException; 

}
