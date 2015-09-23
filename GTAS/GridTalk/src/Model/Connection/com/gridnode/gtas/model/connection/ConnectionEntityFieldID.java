/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.connection;

import com.gridnode.gtas.model.gridnode.IGridNode;
import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Connection module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionEntityFieldID
{
  private Hashtable _table;
  private static ConnectionEntityFieldID _self = null;

  private ConnectionEntityFieldID()
  {
    _table = new Hashtable();

    //NetworkSetting
    _table.put(INetworkSetting.ENTITY_NAME,
      new Number[]
      {
        INetworkSetting.CONNECTION_LEVEL,
        INetworkSetting.HTTP_PROXY_PORT,
        INetworkSetting.HTTP_PROXY_SERVER,
        INetworkSetting.KEEP_ALIVE_INTERVAL,
        INetworkSetting.LOCAL_JMS_ROUTER,
        INetworkSetting.PROXY_AUTH_PASSWORD,
        INetworkSetting.PROXY_AUTH_USER,
        INetworkSetting.RESPONSE_TIMEOUT,
      });

    //ConnectionSetupParam
    _table.put(IConnectionSetupParam.ENTITY_NAME,
      new Number[]
      {
        IConnectionSetupParam.CURRENT_LOCATION,
        IConnectionSetupParam.ORIGINAL_LOCATION,
        IConnectionSetupParam.ORIGINAL_SERVICING_ROUTER,
        IConnectionSetupParam.SERVICING_ROUTER,
      });

    //ConnectionSetupResult
    _table.put(IConnectionSetupResult.ENTITY_NAME,
      new Number[]
      {
        IConnectionSetupResult.AVAILABLE_GRIDMASTERS,
        IConnectionSetupResult.AVAILABLE_ROUTERS,
        IConnectionSetupResult.FAILURE_REASON,
        IConnectionSetupResult.SETUP_PARAMETERS,
        IConnectionSetupResult.STATUS,
      });

    //JmsRouter
    _table.put(IJmsRouter.ENTITY_NAME,
      new Number[]
      {
        IJmsRouter.IP_ADDRESS,
        IJmsRouter.NAME,
        IJmsRouter.UID,
      });

    _table.put(IGridNode.ENTITY_NAME,
      new Number[]
      {
        IGridNode.ID,
        IGridNode.NAME,
        IGridNode.UID,
        IGridNode.STATE,
        IGridNode.CATEGORY,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new ConnectionEntityFieldID();
    }
    return _self._table;
  }
}