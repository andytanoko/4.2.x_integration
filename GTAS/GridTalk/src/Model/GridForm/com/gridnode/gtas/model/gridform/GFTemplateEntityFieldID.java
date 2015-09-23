/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFTemplateEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Daniel D'Cotta      Created
 * Feb 06 2007    Chong SoonFui       Commented IGFTemplate.CAN_DELETE
 */
package com.gridnode.gtas.model.gridform;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the GF Template module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GFTemplateEntityFieldID
{
  private Hashtable _table;
  private static GFTemplateEntityFieldID _self = null;

  private GFTemplateEntityFieldID()
  {
    _table = new Hashtable();

    //GFTemplate
    _table.put(IGFTemplate.ENTITY_NAME,
      new Number[]
      {
        IGFTemplate.UID,
        IGFTemplate.VERSION,
//        IGFTemplate.CAN_DELETE,
        IGFTemplate.NAME,
        IGFTemplate.FILENAME,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new GFTemplateEntityFieldID();
    }
    return _self._table;
  }
}