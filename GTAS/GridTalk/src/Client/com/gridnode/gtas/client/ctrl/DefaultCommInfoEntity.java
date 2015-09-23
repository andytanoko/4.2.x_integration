/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultCommInfoEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" stuff
 */
package com.gridnode.gtas.client.ctrl;
 
import com.gridnode.gtas.client.GTClientException;

class DefaultCommInfoEntity extends AbstractProfileEntity
  implements IGTCommInfoEntity
{
  public Boolean getIsPartner() throws GTClientException
  {
    return (Boolean)getFieldValue(IS_PARTNER);
  }

  public Short getPartnerCat() throws GTClientException
  {
    return (Short)getFieldValue(PARTNER_CAT);
  }
}