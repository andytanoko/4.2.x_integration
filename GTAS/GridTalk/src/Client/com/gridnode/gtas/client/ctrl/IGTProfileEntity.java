/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-09     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.model.channel.ISecurityInfo;

public interface IGTProfileEntity extends IGTEntity
{
  //Constants for PARTNER_CAT (We assume that the values for these for Security info are also
  //valid for all other profile types!)
  public static Short PARTNER_CAT_OTHERS = ISecurityInfo.CATEGORY_OTHERS;
  public static Short PARTNER_CAT_GRIDTALK = ISecurityInfo.CATEGORY_GRIDTALK;

  public Boolean getIsPartner() throws GTClientException;
  public Short getPartnerCat() throws GTClientException;
}