/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityTestHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.server.actions;

import com.gridnode.pdip.framework.db.entity.IEntity;


import java.util.Collection;

/**
 * This defines the interface for testing the GridTalk Action class.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public interface IEntityTestHelper
{
  public IEntity create(IEntity entity) throws Exception;

  public void delete(Long uID) throws Exception;

  public void deleteAll() throws Exception;

  public IEntity get(Long uID) throws Exception;

  public Collection getAll() throws Exception;
}