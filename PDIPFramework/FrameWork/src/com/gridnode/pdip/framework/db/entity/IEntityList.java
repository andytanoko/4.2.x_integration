/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityList.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 19 May 2003    Koh Han Sing        Created.
 */

package com.gridnode.pdip.framework.db.entity;

import java.util.Collection;

/**
 * Interface for any container holding a list of IEntity for serializing.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public interface IEntityList
{

  public Collection getEntities();

}