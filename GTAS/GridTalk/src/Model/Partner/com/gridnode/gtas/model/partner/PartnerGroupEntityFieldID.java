/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerGroupEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Feb 06 2007    Chong SoonFui       Commented IPartnerGroup.CAN_DELETE
 */
package com.gridnode.gtas.model.partner;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the PartnerGroup module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0
 */
public class PartnerGroupEntityFieldID
{
  private Hashtable _table;
  private static PartnerGroupEntityFieldID _self = null;

  private PartnerGroupEntityFieldID()
  {
    _table = new Hashtable();

    //PartnerGroup
    _table.put(IPartnerGroup.ENTITY_NAME,
      new Number[]
      {
//        IPartnerGroup.CAN_DELETE,
        IPartnerGroup.PARTNER_TYPE,
        IPartnerGroup.DESCRIPTION,
        IPartnerGroup.NAME,
        IPartnerGroup.UID,
      });

    //PartnerType
    _table.put(IPartnerType.ENTITY_NAME,
      new Number[]
      {
        IPartnerType.DESCRIPTION,
        IPartnerType.NAME,
        IPartnerType.UID,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new PartnerGroupEntityFieldID();
    }
    return _self._table;
  }
}