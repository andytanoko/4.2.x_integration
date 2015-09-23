/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityListener.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 06 Jun 2001    Lim Soon Hsiung
 *              & Liu Xiao Hua        Created (DMS 0.0.1)
 * 09 Jul 2001    Lim Soon Hsiung
 *              & Liu Xiao Hua        Updated for GT 1.1 User Office
 */

package com.gridnode.pdip.framework.db.entity;

import java.util.EventListener;

public interface IEntityListener extends EventListener
{
  /**
   * Invoked when there is a change in any of the entity.
   *
   * @param event event object
   *
   * @since DMS 0.0.1
   */
  public void entityModified(EntityEvent event);
}