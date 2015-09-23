/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ShellExecutableEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Sep 27 2002    Jagadeesh              Created
 */


package com.gridnode.gtas.model.userprocedure;

/**
 * This class provides the fieldIDs of JavaProcedure.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */

import java.util.Hashtable;

public class ShellExecutableEntityFieldID
{
  private Hashtable _table;
  private static ShellExecutableEntityFieldID _self = null;

  private ShellExecutableEntityFieldID()
  {
      _table = new Hashtable();

      _table.put(IShellExecutable.ENTITY_NAME,
      new Number[]
      {
        IShellExecutable.ARGUMENTS,
        IShellExecutable.TYPE
      });

  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new ShellExecutableEntityFieldID();
    }
    return _self._table;
  }


}