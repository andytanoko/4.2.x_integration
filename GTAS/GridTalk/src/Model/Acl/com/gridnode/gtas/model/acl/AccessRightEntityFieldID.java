/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessRightEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * Feb 06 2007    Chong SoonFui           Commented IAccessRight.CAN_DELETE
 */

package com.gridnode.gtas.model.acl;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the AccessRight module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class AccessRightEntityFieldID
{
  private Hashtable _table;
  private static AccessRightEntityFieldID _self = null;

  private AccessRightEntityFieldID()
  {
    _table = new Hashtable();

    _table.put(IAccessRight.ENTITY_NAME,
      new Number[]
      {
        IAccessRight.ACTION,
//        IAccessRight.CAN_DELETE,
        IAccessRight.CRITERIA,
        IAccessRight.DATA_TYPE,
        IAccessRight.DESCRIPTION,
        IAccessRight.FEATURE,
        IAccessRight.ROLE,
        IAccessRight.UID,
        IAccessRight.VERSION,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new AccessRightEntityFieldID();
    }
    return _self._table;
  }

}