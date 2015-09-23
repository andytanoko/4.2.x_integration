/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.model;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

import com.gridnode.pdip.base.exportconfig.helpers.Logger;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * This class wraps around a entity and provides addition information that is
 * needed during the importing of the entity.
 */

public class ImportEntity implements Serializable
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3470469695246831098L;
	private IEntity _entity;
  private Long _oldUid;
  private Long _newUid;
  private boolean _isOverwrite = false;
  private boolean _isEmbedded = false;
  private Hashtable _persistedValues;

  public ImportEntity(IEntity entity, Long oldUid, Long newUid,
    boolean isOverwrite, boolean isEmbedded)
  {
    _entity = entity;
    _oldUid = oldUid;
    _newUid = newUid;
    _isOverwrite = isOverwrite;
    _isEmbedded = isEmbedded;
    _persistedValues = new Hashtable();
    Logger.debug("[ImportEntity] entity ="+_entity.getEntityDescr());
    Logger.debug("[ImportEntity] oldUid ="+oldUid);
    Logger.debug("[ImportEntity] newUid ="+newUid);
  }

  public IEntity getEntity()
  {
    return _entity;
  }

  public Long getOldUid()
  {
    return _oldUid;
  }

  public Long getNewUid()
  {
    return _newUid;
  }

  public boolean isOverwrite()
  {
    return _isOverwrite;
  }

  public boolean isEmbedded()
  {
    return _isEmbedded;
  }

  public void setEntity(IEntity entity)
  {
    _entity = entity;
    Logger.debug("[ImportEntity.setEntity] entity ="+_entity.getEntityDescr());
    Logger.debug("[ImportEntity.setEntity] entity uid ="+_entity.getKey());
  }

  public void setNewUid(Long newUid)
  {
    _newUid = newUid;
    Logger.debug("[ImportEntity.setNewUid] entity ="+_entity.getEntityDescr());
    Logger.debug("[ImportEntity.setNewUid] entity new uid ="+_newUid);
  }

  public void setIsOverwrite(boolean isOverwrite)
  {
    _isOverwrite = isOverwrite;
  }

  public void setIsEmbedded(boolean isEmbedded)
  {
    _isEmbedded = isEmbedded;
  }

  public void setPersistedValue(Number fieldId, Object value)
  {
    Logger.debug("[ImportEntity.setPersistedValue] entity ="+_entity.getEntityDescr());
    Logger.debug("[ImportEntity.setPersistedValue] fieldId ="+fieldId+" value ="+value.getClass().getName());
    _persistedValues.put(fieldId, value);
  }

  public void updateEntity()
  {
    Logger.debug("[ImportEntity.updateEntity] entity ="+_entity.getEntityDescr());
    Logger.debug("[ImportEntity.updateEntity] entity uid ="+_entity.getKey());
    Enumeration enu = _persistedValues.keys();
    while (enu.hasMoreElements())
    {
      Number fieldId = (Number)enu.nextElement();
      Object value = _persistedValues.get(fieldId);
      _entity.setFieldValue(fieldId, value);
      Logger.debug("[ImportEntity.updateEntity] fieldId ="+fieldId+" value ="+value.getClass().getName());
    }
  }
}