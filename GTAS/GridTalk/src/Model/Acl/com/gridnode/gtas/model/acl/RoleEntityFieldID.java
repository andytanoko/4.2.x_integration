/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RoleEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * Feb 06 2007    Chong SoonFui           Commented IRole.CAN_DELETE
 */

package com.gridnode.gtas.model.acl;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Role module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class RoleEntityFieldID
{
  private Hashtable _table;
  private static RoleEntityFieldID _self = null;

  private RoleEntityFieldID()
  {
    _table = new Hashtable();

    _table.put(IRole.ENTITY_NAME,
      new Number[]
      {
        IRole.UID,
        IRole.VERSION,
//        IRole.CAN_DELETE,
        IRole.DESCRIPTION,
        IRole.ROLE
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new RoleEntityFieldID();
    }
    return _self._table;
  }

}