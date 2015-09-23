/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefFileEntityFieldId.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh               Created
 * Feb 06 2007    Chong SoonFui           Commented IProcedureDefFile.CAN_DELETE
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


public class ProcedureDefFileEntityFieldID
{

  private Hashtable _table;
  private static ProcedureDefFileEntityFieldID _self = null;

  public ProcedureDefFileEntityFieldID()
  {
      _table = new Hashtable();

      _table.put(IProcedureDefFile.ENTITY_NAME,
      new Number[]
      {
        IProcedureDefFile.NAME,
        IProcedureDefFile.DESCRIPTION,
        IProcedureDefFile.FILE_NAME,
        IProcedureDefFile.FILE_PATH,
        IProcedureDefFile.TYPE,
        IProcedureDefFile.UID,
//        IProcedureDefFile.CAN_DELETE
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new ProcedureDefFileEntityFieldID();
    }
    return _self._table;
  }


}




