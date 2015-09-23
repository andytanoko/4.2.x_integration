/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractEntityVisitor.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 16 May 2003    Koh Han Sing        Created.
 */

package com.gridnode.pdip.framework.db.entity;

import java.util.Collection;

/**
 * Abstract class for entity visitor.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public abstract class AbstractEntityVisitor
{
  public abstract void visit(IEntity entity) throws Exception;

  public abstract void visit(Collection entityList) throws Exception;
}