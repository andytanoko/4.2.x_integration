/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultChannelInfoEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" stuff
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

class DefaultChannelInfoEntity extends AbstractProfileEntity
  implements IGTChannelInfoEntity
{

  public Boolean getIsPartner() throws GTClientException
  {
    return (Boolean)getFieldValue(IS_PARTNER);
  }

  public Short getPartnerCat() throws GTClientException
  {
    return (Short)getFieldValue(PARTNER_CAT);
  }

/*
  public String getTptProtocolTypeLabelKey() throws GTClientException
  {
    String tptProtocolType = (String)getFieldValue(TPT_PROTOCOL_TYPE);
    return getTptProtocolTypeLabelKey(tptProtocolType);
  }

  public String getTptProtocolTypeLabelKey(String tptProtocolType) throws GTClientException
  {
    if(TPT_PROTOCOL_TYPE_JMS.equals(tptProtocolType))
    {
      return "channelInfo.tptProtocolType.jms";
    }
    throw new GTClientException("Unsupported tptProtocolType:" + tptProtocolType);
  }

  public Collection getAllowedTptProtocolTypes() throws GTClientException
  {
    ArrayList types = new ArrayList(1);
    types.add(TPT_PROTOCOL_TYPE_JMS);
    return types;
  }
  
*/  
}