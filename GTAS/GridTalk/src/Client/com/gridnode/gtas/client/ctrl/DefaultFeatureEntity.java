/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultFeatureEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;


class DefaultFeatureEntity
  extends AbstractGTEntity
  implements IGTFeatureEntity
{

//  DefaultFeatureEntity(IGTSession session, IGTFieldMetaInfo[] metaInfo)
//  {
//    super(session, IGTEntity.ENTITY_FEATURE, metaInfo);
//  }

//  public long getUID()
//  {
//    Long uid = (Long) _fields.get(IGTFeatureEntity.UID);
//    return uid.longValue();
//  }

  public void setFieldValue(Number field, Object value)
  {
    throw new java.lang.UnsupportedOperationException("Feature entities are constant and cannot be modified");
  }
}