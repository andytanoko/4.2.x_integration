/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultUserEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-20     Neo Sok Lay         Created
 * 2002-05-21     Andrew Hill         Made constructor package protected, disabled setFieldValue()
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.*;

/**
 * Default UserProfile entity implementation.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
class DefaultAccountStateEntity
  extends AbstractGTEntity
  implements IGTAccountStateEntity
{
  private Short[] _allowedStates = { STATE_ENABLED, STATE_DISABLED };

//  DefaultAccountStateEntity(IGTSession session, IGTFieldMetaInfo[] metaInfo)
//  {
//    super(session, IGTEntity.ENTITY_ACCOUNT_STATE, metaInfo);
//  }

  public String getStateLabelKey()
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
  }


  /**
   * This method is not permitted on UserProfileEntities using SESSION_DEFAULT implementation.
   */
  public void setFieldValue(Number field, Object value)
  {
    throw new UnsupportedOperationException("User profile fields may not be modified via setFieldValue()");
  }

//  public long getUID()
//  {
//    Long uid = (Long) _fields.get(IGTAccountStateEntity.UID);
//    return uid.longValue();
//  }

}