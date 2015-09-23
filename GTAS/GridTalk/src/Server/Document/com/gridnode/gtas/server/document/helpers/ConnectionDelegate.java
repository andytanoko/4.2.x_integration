/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 14 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;
import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeHome;
import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class provides connection checking services
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ConnectionDelegate
{

  /**
   * Obtain the EJBObject for the PostOfficeBean.
   *
   * @return The EJBObject to the PostOfficeBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I7
   */
  public static IPostOfficeObj getPostOffice()
    throws ServiceLookupException
  {
    return (IPostOfficeObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPostOfficeHome.class.getName(),
      IPostOfficeHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the GridNodeManagerBean.
   *
   * @return The EJBObject to the GridNodeManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I7
   */
  public static IGridNodeManagerObj getGridNodeManager()
    throws ServiceLookupException
  {
    return (IGridNodeManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGridNodeManagerHome.class.getName(),
      IGridNodeManagerHome.class,
      new Object[0]);
  }

  /**
   * Check whether the GridTalk is currently online i.e. connected to GridMaster.
   *
   * @return <b>true</b> if GridTalk is connected to GridMaster, <b>false</b>
   * otherwise.
   *
   * @since 2.0 I7
   */
  public static boolean isOnline()
  {
    boolean online = false;
    try
    {
      online = getPostOffice().isGridMasterPostOfficeOpened();
    }
    catch (Exception ex)
    {
      Logger.log("[ConnectionDelegate.isOnline] Error: "+ex.getMessage());
    }

    return online;
  }

  /**
   * Check whether a Partner GridNode is currently Online.
   *
   * @param partnerNodeID GridNodeID of the Partner GridNode.
   *
   * @return <b>true</b> if the partner is online, <b>false</b> otherwise.
   */
  public static boolean isPartnerOnline(String partnerNodeID)
  {
    boolean online = false;
    try
    {
      ConnectionStatus status = getGridNodeManager().findConnectionStatusByNodeID(
                                  partnerNodeID);

      online = (status.getStatusFlag() == ConnectionStatus.STATUS_ONLINE);
    }
    catch (Exception ex)
    {
      Logger.log("[ConnectionDelegate.isPartnerOnline] Error: "+ex.getMessage());
    }
    return online;
  }

}