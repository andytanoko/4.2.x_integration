/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncResourceDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 7, 2004    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.helpers;

import com.gridnode.gtas.server.enterprise.facade.ejb.ISharedResourceManagerObj;
import com.gridnode.gtas.server.enterprise.model.SharedResource;
import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeObj;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.FindEntityException;

import java.util.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Delegate class to perform application logic for synchronizing shared resources to targeted enterprises.
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class SyncResourceDelegate
{
  /**
   * Synchronize the resource to the enterprises (that it is shared to), including GridMaster.
   * The resource will be shared to GridMaster if it is not already shared.
   *
   * @param resourceType The Resource type to synchronize
   * @param resourceUid The UID of the resource
   * @throws Exception Error performing the synchronization
   */
  public static void synchronizeResource(String resourceType, Long resourceUid) throws Exception
  {
    shareResourceToGridMasters(resourceType, resourceUid);
    updateResourceToEnterprises(resourceType, resourceUid);
  }

  /**
   * Share a new resource to all GridMaster enterprises. The resource will be synchronized to the
   * currently online GridMaster, if any.
   *
   * @param resourceType The resource type
   * @param resourceUid The UID of the resource to share.
   * @throws Exception Error sharing the resources
   */
  public static void shareResourceToGridMasters(String resourceType, Long resourceUid) throws Exception
  {
    ISharedResourceManagerObj sharedResMgr = ServiceLookupHelper.getSharedResourceMgr();

    // -- syncToGM --
    // retrieve GNs with State=GM
    Collection gms = getGridMasters();
    //String gmNodeId = getOnlineGridMasterNodeId();
    Hashtable sharedResTable = new Hashtable();
    for (Iterator i=gms.iterator(); i.hasNext(); )
    {
      GridNode gm = (GridNode)i.next();

      //   shareResource
      //Long sharedResUid = sharedResMgr.addSharedResource(
      //                      resourceType,
      //                      resourceUid,
      //                      gm.getID());

      //if (gmNodeId != null && gmNodeId.equals(gm.getID()))
      //  sharedResMgr.synchronizeSharedResource(sharedResUid, null);

      sharedResMgr.shareResourceIfNotShared(resourceType, new Long[] {resourceUid}, gm.getID());
    }
  }

  /**
   * Mark for synchronization of a resource to all enterprises that the resource is shared to,
   * and attempt to perform synchronization to online enterprises.
   *
   * @param resourceType The resource type.
   * @param resourceUid The resource Uid.
   * @throws Exception Error performing the update.
   */
  public static void updateResourceToEnterprises(String resourceType, Long resourceUid) throws Exception
  {
    ISharedResourceManagerObj mgr = ServiceLookupHelper.getSharedResourceMgr();
    Collection sharedResources = mgr.getSharedResources(
                                   resourceType, resourceUid);

    long checkSum = System.currentTimeMillis();
    String gmNodeId = getOnlineGridMasterNodeId();

    SharedResource sharedRes;
    Long sharedResUid;
    GridNode gridnode;
    String enterpriseId;
    List sharedResList = new ArrayList();

    for (Iterator i=sharedResources.iterator(); i.hasNext(); )
    {
      sharedRes = (SharedResource)i.next();
      sharedResUid = new Long(sharedRes.getUId());
      enterpriseId = sharedRes.getToEnterpriseID();
      gridnode = getGridNode(enterpriseId);
      if (gridnode != null)
      {
        mgr.setSharedResourceUnsync(sharedResUid, checkSum);

        if (isGridMaster(gridnode))
        {
          if (gmNodeId != null && gmNodeId.equals(enterpriseId)) // only include for online gm
            sharedResList.add(new Long[]{sharedResUid, null});
        }
        else
        {
          sharedResList.add(new Long[]{
                      sharedResUid,
                      getChannel(gridnode)});
        }
      }
    }

    // syncRes
    for (Iterator i=sharedResList.iterator(); i.hasNext(); )
    {
      Long[] res = (Long[])i.next();
      try
      {
        mgr.synchronizeSharedResource(res[0], res[1]);
      }
      catch (Exception e)
      {
        Logger.warn("[SyncResourceDelegate.updateResourceToEnterprises] Error: "+e.getMessage());
      }
    }
  }

  /**
   * Perform synchronization of unsynchronized resources shared to an enterprise.
   *
   * @param resourceType The resource type.
   * @param enterpriseId The enterprise id.
   * @throws Exception Error performing the synchronization.
   */
  public static void syncResourcesToEnterprise(String resourceType, String enterpriseId)
    throws Exception
  {
    ISharedResourceManagerObj mgr = ServiceLookupHelper.getSharedResourceMgr();
    Collection sharedResList = mgr.getUnsyncSharedResourceUIDs(resourceType, enterpriseId);

    Long destChannelUid = getChannel(getGridNode(enterpriseId));
    mgr.synchronizeSharedResources(sharedResList, destChannelUid);
  }

  // ****************** Utility Methods *************************************

  /**
   * Get the Node id of the GridMaster that is currently online.
   *
   * @return GridNode Id of the online GridMaster, or <b>null</b> if no GridMaster is online.
   */
  private static String getOnlineGridMasterNodeId()
  {
    String nodeId = null;
    try
    {
      IPostOfficeObj gmPostOffice = ServiceLookupHelper.getPostOffice();
      nodeId = gmPostOffice.getOpenedGridMasterPostOfficeID();
    }
    catch (Exception e)
    {
      Logger.warn("[SyncResourceDelegate.getOnlineGridMasterNodeId] Error: "+e.getMessage());
    }

    return nodeId;
  }

  /**
   * Get the Master channel to use for synchronization to a GridNode.
   *
   * @param gn The GridNode.
   * @return The UID of the Master Channel to use, or <b>null</b> if the GridNode is a GridMaster.
   * @throws Exception Error retrieving the Master channel for the GridNode.
   */
  private static Long getChannel(GridNode gn) throws Exception
  {
    return isGridMaster(gn) ? null : ActionHelper.getMasterChannelUID(gn.getID());
  }

  /**
   * Check if a Gridnode is a GridMaster
   *
   * @param gn The GridNode to check
   * @return <b>true</b> if the GridNode State = STATE_GM.
   */
  private static boolean isGridMaster(GridNode gn)
  {
    return gn.getState() == GridNode.STATE_GM;
  }

  /**
   * Get the GridMasters available to the system.
   *
   * @return Collection of GridNode having State = STATE_GM.
   * @throws Exception Error retrieving the GridMaster GridNodes.
   */
  private static Collection getGridMasters() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();

    filter.addSingleFilter(null, GridNode.STATE, filter.getEqualOperator(), new Short(GridNode.STATE_GM), false);
    return ServiceLookupHelper.getGridNodeManager().findGridNodesByFilter(filter);
  }

  /**
   * Get the GridNode with the specified enterpriseId (ID)
   *
   * @param enterpriseId The enterprise id
   * @return The retrieved GridNode having ID = enterpriseId, or <b>null</b> if no such GridNode exists.
   * @throws Exception Error retrieving the GridNode
   */
  private static GridNode getGridNode(String enterpriseId) throws Exception
  {
    try
    {
      return ServiceLookupHelper.getGridNodeManager().findGridNodeByID(enterpriseId);
    }
    catch (FindEntityException e)
    {
      Logger.warn("[ResourceChangeMDBean.getGridNode] Error: "+e.getMessage());
    }
    return null;
  }

}
