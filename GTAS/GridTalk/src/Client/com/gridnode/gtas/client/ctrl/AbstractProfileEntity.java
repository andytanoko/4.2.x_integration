/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractProfileEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-09     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

abstract class AbstractProfileEntity extends AbstractGTEntity
implements IGTProfileEntity
{
  public boolean canDelete() throws GTClientException
  {
    try
    {
      if(PARTNER_CAT_GRIDTALK.equals(getPartnerCat()))
      {
        if(Boolean.TRUE.equals(getIsPartner()))
        {
          return false;
        }
      }
      return true;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining canDelete() status for entity: " + this);
    }
  }

  public boolean canEdit() throws GTClientException
  {
    try
    {
      return canDelete();
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining canEdit() status for entity:" + this);
    }
  }

  public abstract Boolean getIsPartner() throws GTClientException;
  public abstract Short getPartnerCat() throws GTClientException;
}