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
 * Jan 18 2003    Jagadeesh               Created
 */



package com.gridnode.gtas.model.userprocedure;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the ProcedureDefFile module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */


public class ParamDefEntitiyFieldID
{

  private Hashtable _table;
  private static ParamDefEntitiyFieldID _self = null;

  public ParamDefEntitiyFieldID()
  {
      _table = new Hashtable();

      _table.put(IParamDef.ENTITY_NAME,
      new Number[]
      {
        IParamDef.NAME,
        IParamDef.DESCRIPTION,
        IParamDef.SOURCE,
        IParamDef.TYPE,
        IParamDef.VALUE,
        IParamDef.DATE_FORMAT
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new ParamDefEntitiyFieldID();
    }
    return _self._table;
  }


}



