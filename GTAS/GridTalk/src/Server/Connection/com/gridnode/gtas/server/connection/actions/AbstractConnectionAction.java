/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractConnectionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.gridnode.gtas.model.connection.ConnectionEntityFieldID;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;
import com.gridnode.gtas.server.connection.model.JmsRouter;
import com.gridnode.gtas.server.connection.model.NetworkSetting;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.db.entity.EntityOrderComparator;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

/**
 * This abstract class provides the helper methods for Action classes of
 * the Connection module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public abstract class AbstractConnectionAction extends AbstractGridTalkAction
{

  /**
   * Convert the NetworkSetting to Map representation.
   *
   * @param setting The NetworkSetting to convert.
   * @retunr Map representation for <code>setting</code>.
   */
  protected Map convertToMap(NetworkSetting setting)
  {
    return NetworkSetting.convertToMap(
             setting,
             ConnectionEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert the ConnectionSetupResult to Map representation.
   *
   * @param setup The ConnectionSetupResult to convert.
   * @retunr Map representation for <code>setting</code>.
   */
  protected Map convertToMap(ConnectionSetupResult setup)
  {
    return ConnectionSetupResult.convertToMap(
             setup,
             ConnectionEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a NetworkSetting Map object to Entity.
   *
   * @param settingMap The NetworkSetting map object.
   * @return Converted NetworkSetting entity.
   */
  protected NetworkSetting convertToEntity(Map settingMap)
  {
    NetworkSetting setting = new NetworkSetting();
    Object[] keys = settingMap.keySet().toArray();
    for (int i=0; i<keys.length; i++)
    {
      setting.setFieldValue((Number)keys[i], settingMap.get(keys[i]));
    }
    return setting;
  }

  /**
   * Retrieve the NetworkSetting object.
   *
   * @return The most current NetworkSetting.
   */
  protected NetworkSetting getNetworkSetting() throws Throwable
  {
    NetworkSetting setting = ServiceLookupHelper.getConnectionService().getNetworkSetting();
    return setting;
  }

  /**
   * Retrieve the ConnectionSetupResult object.
   *
   * @return The most current ConnectionSetupResult.
   */
  protected ConnectionSetupResult getConnectionSetupResult() throws Throwable
  {
    ConnectionSetupResult setup = ServiceLookupHelper.getConnectionService().getConnectionSetupResult();

    return setup;
  }

  /**
   * Retrieve the GridMaster nodes using the UIDs in the specified order.
   *
   * @param gmUIDs UIDs of the GridNode(s) to retrieve.
   * @return Collection of GridNode(s) retrieved in the specified order.
   */
  protected Collection getGridMasters(Collection gmUIDs)
  {
    ArrayList gms = new ArrayList();
    if (!gmUIDs.isEmpty())
    {
      try
      {
        DataFilterImpl filter = new DataFilterImpl();
        filter.addDomainFilter(null, GridNode.UID, gmUIDs, false);

        gms.addAll(ServiceLookupHelper.getGridNodeManager().findGridNodesByFilter(filter));

        EntityOrderComparator comparator = new EntityOrderComparator(GridNode.UID, gmUIDs);
        Collections.sort(gms, comparator);
      }
      catch (Exception ex)
      {
        Logger.warn("[AbstractConnectionAction.getGridMasters] Error ", ex);
      }
    }

    return gms;
  }

  /**
   * Retrieve the JmsRouters using the UIDs in the specified order.
   *
   * @param routerUIDs UIDs of the JmsRouter(s) to retrieve.
   * @return Collection of JmsRouter(s) retrieved in the specified order.
   */
  protected Collection getJmsRouters(Collection routerUIDs)
  {
    ArrayList routers = new ArrayList();
    if (!routerUIDs.isEmpty())
    {
      try
      {
        DataFilterImpl filter = new DataFilterImpl();
        filter.addDomainFilter(null, JmsRouter.UID, routerUIDs, false);

        routers.addAll(ServiceLookupHelper.getConnectionService().getJmsRouters(filter));

        EntityOrderComparator comparator = new EntityOrderComparator(JmsRouter.UID, routerUIDs);
        Collections.sort(routers, comparator);
      }
      catch (Exception ex)
      {
        Logger.warn("[AbstractConnectionAction.getJmsRouters] Error ", ex);
      }
    }

    return routers;
  }

}