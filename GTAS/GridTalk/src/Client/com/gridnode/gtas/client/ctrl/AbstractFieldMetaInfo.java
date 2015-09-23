/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractFieldMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-25     Andrew Hill         Created.
 */
package com.gridnode.gtas.client.ctrl;

/**
 * Maintains the entity reference and indicates to the entity constructor that it should
 * set its reference during entity construction.
 */
abstract class AbstractFieldMetaInfo implements IGTFieldMetaInfo
{
  protected IGTEntity _entity;

  void setEntity(IGTEntity entity)
  {
    _entity = entity;
  }

  public IGTEntity getEntity()
  {
    return _entity;
  }
}