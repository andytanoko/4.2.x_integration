/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfoEntityFieldId.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 18 2002    Goh Kan Mun             Created
 * Jul 11 2002    Goh Kan Mun             Modified - Update for the new CommInfo
 * Feb 06 2007    Chong SoonFui           Commented ICommInfo.CAN_DELETE
 */

package com.gridnode.gtas.model.channel;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the CommInfo module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class CommInfoEntityFieldId
{

  private Hashtable _table;
  private static CommInfoEntityFieldId _self = null;

  public CommInfoEntityFieldId()
  {
    _table = new Hashtable();

    _table.put(ICommInfo.ENTITY_NAME,
      new Number[]
      {
        ICommInfo.DESCRIPTION,
//        ICommInfo.HOST,
        ICommInfo.NAME,
//        ICommInfo.PORT,
        ICommInfo.REF_ID,
//        ICommInfo.PROTOCOL_DETAIL,
        ICommInfo.TPT_IMPL_VERSION,
        ICommInfo.PROTOCOL_TYPE,
//        ICommInfo.PROTOCOL_VERSION,
        ICommInfo.UID,
//        ICommInfo.CAN_DELETE,
        ICommInfo.PARTNER_CAT,
        ICommInfo.IS_DISABLE,
        ICommInfo.IS_PARTNER,
        ICommInfo.URL,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new CommInfoEntityFieldId();
    }
    return _self._table;
  }

}

