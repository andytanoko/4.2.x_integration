/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 23 2003    Neo Sok Lay         Add UID,CAN_DELETE,VERSION for RegistryConnectInfo.
 *                                    Remove RegistryConnectInfo fieldIds. 
 * Feb 06 2007    Chong SoonFui       Commented IRegistryConnectInfo.CAN_DELETE
 */
package com.gridnode.gtas.model.bizreg;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities for the Public registry of the BizReg module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 */
public class RegistryConnectInfoFieldID
{
  private Hashtable _table;
  private static final RegistryConnectInfoFieldID _self = new RegistryConnectInfoFieldID();

  private RegistryConnectInfoFieldID()
  {
    _table = new Hashtable();

    //RegistryConnectInfo
    _table.put(IRegistryConnectInfo.ENTITY_NAME,
      new Number[]
      {
        IRegistryConnectInfo.NAME,
        IRegistryConnectInfo.PUBLISH_PASSWORD,
        IRegistryConnectInfo.PUBLISH_URL,
        IRegistryConnectInfo.PUBLISH_USER,
        IRegistryConnectInfo.QUERY_URL,
//        IRegistryConnectInfo.CAN_DELETE,
        IRegistryConnectInfo.UID,
        IRegistryConnectInfo.VERSION,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    return _self._table;
  }
}