/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerTypeEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    AMH                 Created
 * Feb 06 2007    Chong SoonFui       Commented IPartnerType.CAN_DELETE
 */
package com.gridnode.gtas.model.partner;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the PartnerType module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0
 */
public class PartnerTypeEntityFieldID
{
  private Hashtable _table;
  private static PartnerTypeEntityFieldID _self = null;

  private PartnerTypeEntityFieldID()
  {
    _table = new Hashtable();

    //PartnerType
    _table.put(IPartnerType.ENTITY_NAME,
      new Number[]
      {
//        IPartnerType.CAN_DELETE,
        IPartnerType.DESCRIPTION,
        IPartnerType.NAME,
        IPartnerType.UID,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new PartnerTypeEntityFieldID();
    }
    return _self._table;
  }
}