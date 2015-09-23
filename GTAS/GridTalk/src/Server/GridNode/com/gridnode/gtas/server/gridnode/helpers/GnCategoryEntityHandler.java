/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GnCategoryEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.helpers;

import com.gridnode.gtas.server.gridnode.model.GnCategory;
import com.gridnode.pdip.framework.db.DirectDAOEntityHandler;

/**
 * This EntityHandler provides direct DAO functionality for GnCategory entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GnCategoryEntityHandler
  extends DirectDAOEntityHandler
{
  private static GnCategoryEntityHandler _self;
  private static Object _lock = new Object();

  private GnCategoryEntityHandler()
  {
    super(GnCategory.ENTITY_NAME);
  }

  public static GnCategoryEntityHandler getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new GnCategoryEntityHandler();
      }
    }
    return _self;
  }
}