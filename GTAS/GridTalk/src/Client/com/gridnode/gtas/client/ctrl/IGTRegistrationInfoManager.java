/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTRegistrationInfoManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;
 
import com.gridnode.gtas.client.GTClientException;

public interface IGTRegistrationInfoManager extends IGTManager
{
   public void cancelRegistration(IGTRegistrationInfoEntity rego) throws GTClientException;
   public IGTRegistrationInfoEntity getRegistrationInfo() throws GTClientException;
}