/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityEvent.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * Aug 12 2002    Neo Sok Lay         Modified from GT1.3.x
 */

package com.gridnode.pdip.framework.db.entity;

import java.util.EventObject;

/**
 * An EventObject for event acted upon an entity: created, updated and deleted.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since   2.0 I4
 */
public class EntityEvent extends EventObject
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4133104554104914836L;
	public static final int CREATED    = 1;
  public static final int UPDATED    = 2;
  public static final int DELETED    = 3;

  private int _type;
  private IEntity _entity = null;

  /**
   * Construct a entity event object
   *
   * @since 2.0 I4
   */
  public EntityEvent(Object source, int eventType, IEntity modifiedEntity)
  {
    super(source);
    _type = eventType;
    _entity = modifiedEntity;
  }

  /**
   * Get the entity event type
   *
   * @return entity event type
   *
   * @since 2.0 I4
   */
  public int getEventType()
  {
    return _type;
  }

  /**
   * Return the entity object that is being acted upon
   *
   * @return the entity
   *
   * @since 2.0 I4
   */
  public IEntity getEntity()
  {
    return _entity;
  }

  public String getEventTypeDesc()
  {
    String s = "";
    switch (_type)
    {
      case CREATED : s = "CREATED"; break;
      case UPDATED : s = "UPDATED"; break;
      case DELETED : s = "DELETED"; break;
    }

    return s;
  }

  public String toString()
  {
    String s = "EntityEvent: "+getEventTypeDesc() + " " + _entity.getEntityName();
    s += " source: "+getSource();
    return s;
  }
}