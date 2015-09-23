/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityListVisitableDecorator.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 16 May 2003    Koh Han Sing        Created.
 */

package com.gridnode.pdip.framework.db.entity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Decorator that allows a list of entities to be visited.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class EntityListVisitableDecorator implements IEntityVisitable
{
  protected ArrayList _entityList;

  public EntityListVisitableDecorator(Collection entityList)
  {
    _entityList = new ArrayList();
    _entityList.addAll(entityList);
  }

  public void accepts(AbstractEntityVisitor visitor) throws Exception
  {
    visitor.visit(_entityList);
  }

}