/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ParamDefEntitiyFieldID.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 19 2003    Jagadeesh               Created
 */


package com.gridnode.gtas.model.userprocedure;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the ReturnDef.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */

public class ReturnDefEntityFieldID
{

  private Hashtable _table;
  private static ReturnDefEntityFieldID _self = null;

  public ReturnDefEntityFieldID()
  {
      _table = new Hashtable();

      _table.put(IReturnDef.ENTITY_NAME,
      new Number[]
      {
        IReturnDef.ACTION,
        IReturnDef.ALERT,
        IReturnDef.OPERATOR,
        IReturnDef.VALUE
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new ReturnDefEntityFieldID();
    }
    return _self._table;
  }
}




