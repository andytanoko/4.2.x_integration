/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Feb 06 2007    Chong SoonFui       Commented IPartner.CAN_DELETE
 */
package com.gridnode.gtas.model.partner;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Partner module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0
 */
public class PartnerEntityFieldID
{
  private Hashtable _table;
  private static PartnerEntityFieldID _self = null;

  private PartnerEntityFieldID()
  {
    _table = new Hashtable();

    //Partner
    _table.put(IPartner.ENTITY_NAME,
      new Number[]
      {
//        IPartner.CAN_DELETE,
        IPartner.CREATE_BY,
        IPartner.CREATE_TIME,
        IPartner.DESCRIPTION,
        IPartner.NAME,
        IPartner.PARTNER_GROUP,
        IPartner.PARTNER_ID,
        IPartner.PARTNER_TYPE,
        IPartner.STATE,
        IPartner.UID,
      });

    //PartnerGroup
    _table.put(IPartnerGroup.ENTITY_NAME,
      new Number[]
      {
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
      _self = new PartnerEntityFieldID();
    }
    return _self._table;
  }
}