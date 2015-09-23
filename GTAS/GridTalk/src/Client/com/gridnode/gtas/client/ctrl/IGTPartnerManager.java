/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-17     Andrew Hill         Created
 * 2002-06-19     Andrew Hill         Removed getPartnerByName() method
 * 2002-06-19     Andrew Hill         Added getPartnerByPartnerId() method
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTPartnerManager extends IGTManager
{
  public IGTPartnerEntity getPartnerByUID(long uid)
    throws GTClientException;

  public IGTPartnerEntity getPartnerByPartnerId(String partnerId)
    throws GTClientException;

  public Object[] getPartnerWatchListData()
   throws GTClientException;
}