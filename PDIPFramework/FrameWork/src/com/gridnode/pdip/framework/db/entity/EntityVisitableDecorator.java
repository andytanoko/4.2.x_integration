/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityVisitableDecorator.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 16 May 2003    Koh Han Sing        Created.
 */

package com.gridnode.pdip.framework.db.entity;


/**
 * Decorator that allows an entity to be visited.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class EntityVisitableDecorator implements IEntityVisitable
{
  protected IEntity _entity;

  public EntityVisitableDecorator(IEntity entity)
  {
    _entity = entity;
  }

  public void accepts(AbstractEntityVisitor visitor) throws Exception
  {
    visitor.visit(_entity);
  }

}