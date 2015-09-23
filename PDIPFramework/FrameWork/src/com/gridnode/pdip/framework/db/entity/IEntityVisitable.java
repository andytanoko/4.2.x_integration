/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityVisitable.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 16 May 2003    Koh Han Sing        Created.
 */

package com.gridnode.pdip.framework.db.entity;

/**
 * Interface for any visitor class that visits an entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public interface IEntityVisitable
{

  public void accepts(AbstractEntityVisitor visitor) throws Exception;

}