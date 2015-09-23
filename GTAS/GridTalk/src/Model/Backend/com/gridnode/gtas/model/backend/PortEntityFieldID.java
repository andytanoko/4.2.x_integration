/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PortEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 * Aug 23 2005    Tam Wei Xiang       Remove the field IPort.ATTACHMENT_DIR from 
 *                                    the first array Number[] under _table.put
 * Mar 03 2006    Tam Wei Xiang       Added field 'FileGrouping' in Port    
 * Feb 06 2007    Chong SoonFui       Commented IPort.CAN_DELETE                                
 */
package com.gridnode.gtas.model.backend;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the Port entity.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class PortEntityFieldID
{
  private Hashtable _table;
  private static PortEntityFieldID _self = null;

  private PortEntityFieldID()
  {
    _table = new Hashtable();

    // Port
    _table.put(IPort.ENTITY_NAME,
      new Number[]
      {
        IPort.START_NUM,
        IPort.ROLLOVER_NUM,
        IPort.NEXT_NUM,
        IPort.IS_PADDED,
        IPort.FIX_NUM_LENGTH,
//        IPort.CAN_DELETE,
        IPort.DESCRIPTION,
        IPort.FILE_EXT_TYPE,
        IPort.FILE_EXT_VALUE,
        IPort.FILE_NAME,
        IPort.HOST_DIR,
        IPort.IS_ADD_FILE_EXT,
        IPort.IS_DIFF_FILE_NAME,
        IPort.IS_EXPORT_GDOC,
        IPort.IS_OVERWRITE,
        IPort.IS_RFC,
        IPort.NAME,
        IPort.RFC,
        IPort.UID,
        IPort.FILE_GROUPING
      });

    // Rfc
    _table.put(IRfc.ENTITY_NAME,
      new Number[]
      {
        IRfc.DESCRIPTION,
        IRfc.NAME,
        IRfc.UID
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new PortEntityFieldID();
    }
    return _self._table;
  }
}