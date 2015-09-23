/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureEntityFieldId.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh           Created
 * Jan 20 2003    Jagadeesh           Added: FiledId's for Conversion of HashMap
 *                                    to entity.
 *
 * Feb 03 2003    Jagadeesh           Added: FiledId for CAN_DELETE for ProcedureDefFile.
 * Jul 08 2003    Koh Han Sing        Add in GridDocument field
 * Jul 28 2003    Koh Han Sing        Added SoapProcedure
 * Mar 05 2004    Neo Sok Lay         Missing SoapProcedure user name and password fieldIDs.
 *                                    Change to put fieldIDs defined in other fields
 *                                    instead of duplicating them here.
 * Feb 06 2007    Chong SoonFui       Commented IUserProcedure.CAN_DELETE
 */
package com.gridnode.gtas.model.userprocedure;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the UserProcedure module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Jagadeesh
 *
 * @version GT 2.2.10
 * @since 2.0
 */
public class UserProcedureEntityFieldID
{

  private Hashtable _table;
  private static UserProcedureEntityFieldID _self = null;

  public UserProcedureEntityFieldID()
  {
    _table = new Hashtable();

    _table.put(IUserProcedure.ENTITY_NAME,
    new Number[]
    {
      IUserProcedure.NAME,
      IUserProcedure.DESCRIPTION,
      IUserProcedure.IS_SYNCHRONOUS,
      IUserProcedure.PROC_DEF,
      IUserProcedure.PROC_DEF_FILE,
      IUserProcedure.PROC_PARAM_LIST,
      IUserProcedure.PROC_RETURN_LIST,
      IUserProcedure.PROC_TYPE,
      IUserProcedure.RETURN_DATA_TYPE,
      IUserProcedure.DEF_ACTION,
      IUserProcedure.DEF_ALERT,
      IUserProcedure.UID,
//      IUserProcedure.CAN_DELETE,
      IUserProcedure.GRID_DOC_FIELD
    }
    );

    _table.putAll(ProcedureDefFileEntityFieldID.getEntityFieldID());
    _table.putAll(JavaProcedureEntityFieldID.getEntityFieldID());
    _table.putAll(ShellExecutableEntityFieldID.getEntityFieldID());
    _table.putAll(SoapProcedureEntityFieldID.getEntityFieldID());
    _table.putAll(ParamDefEntitiyFieldID.getEntityFieldID());
    _table.putAll(ReturnDefEntityFieldID.getEntityFieldID());
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new UserProcedureEntityFieldID();
    }
    return _self._table;
  }
}
