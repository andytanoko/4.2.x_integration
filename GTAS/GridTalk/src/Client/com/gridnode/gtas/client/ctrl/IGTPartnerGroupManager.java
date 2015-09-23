/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerGroupManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

public interface IGTPartnerGroupManager extends IGTManager
{
  public IGTPartnerGroupEntity getPartnerGroupByUID(long uid)
    throws GTClientException;

  public IGTPartnerGroupEntity getPartnerGroupByName(String name)
    throws GTClientException;

  public Collection getPartnerGroupsForPartnerType(long partnerTypeUid)
    throws GTClientException;
}