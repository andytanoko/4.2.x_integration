/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFDefinitionEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Daniel D'Cotta      Created
 * Feb 06 2007    Chong SoonFui       Commented IGFDefinition.CAN_DELETE,IGFTemplate.CAN_DELETE
 */
package com.gridnode.gtas.model.gridform;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the GF Definition module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GFDefinitionEntityFieldID
{
  private Hashtable _table;
  private static GFDefinitionEntityFieldID _self = null;

  private GFDefinitionEntityFieldID()
  {
    _table = new Hashtable();

    //GFDefinition
    _table.put(IGFDefinition.ENTITY_NAME,
      new Number[]
      {
        IGFDefinition.UID,
        IGFDefinition.VERSION,
//        IGFDefinition.CAN_DELETE,
        IGFDefinition.NAME,
        IGFDefinition.FILENAME,
        IGFDefinition.TEMPLATE,
      });

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
      _self = new GFDefinitionEntityFieldID();
    }
    return _self._table;
  }
}