/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICheckConflict.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import com.gridnode.pdip.framework.db.entity.IEntity;

public interface ICheckConflict
{

  /**
   * This method will check if the given entity has any duplicate entry in
   * the database, if there is, it will return the duplicated entity in the
   * database.
   */
  public IEntity checkDuplicate(IEntity entity) throws Exception;

}