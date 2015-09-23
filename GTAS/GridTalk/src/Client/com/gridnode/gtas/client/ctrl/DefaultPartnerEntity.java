/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultPartnerEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;


class DefaultPartnerEntity
  extends AbstractGTEntity
  implements IGTPartnerEntity
{
  //private Short[] _allowedStates = { STATE_ENABLED, STATE_DISABLED };

//  DefaultPartnerEntity(IGTSession session, IGTFieldMetaInfo[] metaInfo)
//  {
//    super(session, IGTEntity.ENTITY_PARTNER, metaInfo);
//  }

//  public long getUID()
//  {
//    Long uid = (Long) _fields.get(IGTPartnerEntity.UID);
//    return uid.longValue();
//  }

  /*public String getStateLabelKey()
    throws GTClientException
  {
    try
    {
      Short state = (Short)getFieldValue(STATE);
      return getStateLabelKey(state);
    }
    catch(Exception e)
    {
      throw new GTClientException("Unable to determine state key",e);
    }
  }

  public String getStateLabelKey(Short state)
    throws GTClientException
  {
    if(STATE_DISABLED.equals(state))
      return STATE_DISABLED_LABEL;
    if(STATE_ENABLED.equals(state))
      return STATE_ENABLED_LABEL;
    if(STATE_DELETED.equals(state))
      return STATE_DELETED_LABEL;
    throw new java.lang.IllegalStateException("Invalid state:" + state);
  }

  public Collection getAllowedStates()
    throws GTClientException
  {
    ArrayList states = new ArrayList(_allowedStates.length);
    for(int i = 0; i < _allowedStates.length; i++)
    {
      states.add(_allowedStates[i]);
    }
    return states;
  }*/
}