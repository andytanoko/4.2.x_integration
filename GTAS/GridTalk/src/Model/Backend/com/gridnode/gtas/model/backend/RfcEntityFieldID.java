/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RfcEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 * Feb 06 2007    Chong SoonFui       Commented IRfc.CAN_DELETE
 */
package com.gridnode.gtas.model.backend;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the Rfc entity.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class RfcEntityFieldID
{
  private Hashtable _table;
  private static RfcEntityFieldID _self = null;

  private RfcEntityFieldID()
  {
    _table = new Hashtable();

    // Rfc
    _table.put(IRfc.ENTITY_NAME,
      new Number[]
      {
//        IRfc.CAN_DELETE,
        IRfc.COMMAND_FILE,
        IRfc.COMMAND_FILE_DIR,
        IRfc.COMMAND_LINE,
        IRfc.DESCRIPTION,
        IRfc.HOST,
        IRfc.NAME,
        IRfc.PORT_NUMBER,
        IRfc.UID,
        IRfc.USE_COMMAND_FILE
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new RfcEntityFieldID();
    }
    return _self._table;
  }
}