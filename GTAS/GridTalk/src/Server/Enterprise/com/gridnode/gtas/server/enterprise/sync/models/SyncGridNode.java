/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncGridNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 01 2002    Neo Sok Lay         Created
 * Oct 14 2002    Neo Sok Lay         Implement sync().
 * Nov 28 2002    Neo Sok Lay         Do not sync the BusinessEntities during
 *                                    sync(). The sync() method should only
 *                                    take care of the GridNode. SyncGridNodeDelegate
 *                                    controls whether to synchronize the
 *                                    BusinessEntities.
 * Sep 08 2003    Neo Sok Lay         Add method: 
 *                                    - sync(int mode)
 */
package com.gridnode.gtas.server.enterprise.sync.models;

import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;

/**
 * This data object is a modified model of GridNode for data transfer &
 * synchronization purpose. This object encapsulates the GridNode to
 * be synchronized and, in addition, related information can be encapsulated
 * as well, such as the BusinessEntity(s).<p>
 *
 * The additional modes assigned to this model is 0x1000,0x2000,0x4000,0x8000.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I6
 */
public class SyncGridNode extends AbstractSyncModel
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9028686657954746946L;

	//public static final int MODE_OVERWRITE_EXISTING = 0x100;
  
  private static final Number[]  GN_SYNC_FIELDS =
          {
            GridNode.ACTIVATION_REASON,
            GridNode.CATEGORY,
            GridNode.NAME,
            GridNode.STATE,
            GridNode.DT_ACTIVATED,
            GridNode.DT_REQ_ACTIVATE,
            GridNode.DT_DEACTIVATED,
          };

  private GridNode _gridnode;
  private CompanyProfile _coyProfile;
  private SyncBusinessEntity[] _bizEntities;

  public SyncGridNode()
  {
  }

  public SyncGridNode(GridNode gn)
  {
    setGridnode(gn);
    if (gn != null)
      setCoyProfile(findCompanyProfile());
  }

  // ******************* Setters & Getters ****************************

  public GridNode getGridnode()
  {
    return _gridnode;
  }

  public void setGridnode(GridNode gridnode)
  {
    _gridnode = gridnode;
  }

  public CompanyProfile getCoyProfile()
  {
    return _coyProfile;
  }

  public void setCoyProfile(CompanyProfile coyProfile)
  {
    _coyProfile = coyProfile;
  }

  public SyncBusinessEntity[] getBusinessEntities()
  {
    return _bizEntities;
  }

  public void setBusinessEntities(SyncBusinessEntity[] bizEntities)
  {
    _bizEntities = bizEntities;
  }

  // **************** Overrides DataObject *********************************

  public void preSerialize()
  {
    _gridnode.preSerialize();
    if (_coyProfile != null)
      _coyProfile.preSerialize();
    if (_bizEntities != null)
    {
      for (int i=0; i<_bizEntities.length; i++)
        _bizEntities[i].preSerialize();
    }
  }

  public void postSerialize()
  {
    _gridnode.postSerialize();
    if (_coyProfile != null)
      _coyProfile.postSerialize();
    if (_bizEntities != null)
    {
      for (int i=0; i<_bizEntities.length; i++)
        _bizEntities[i].postSerialize();
    }
  }

  public void postDeserialize()
  {
    if (_gridnode != null)
      _gridnode.postDeserialize();
    if (_coyProfile != null)
      _coyProfile.postDeserialize();
    if (_bizEntities != null)
    {
      for (int i=0; i<_bizEntities.length; i++)
        _bizEntities[i].postDeserialize();
    }
  }

  // ***************** Implement AbstractSyncModel ***************************
  public void sync() throws Throwable
  {
    GridNode existGn = findGridNode(_gridnode.getID());

    if (existGn == null)
    {
      Long uID = ServiceLookupHelper.getGridNodeManager().createGridNode(_gridnode);
      _gridnode.setUId(uID.longValue());
    }
    else
    {
      //copy fields to existing GridNode
      copyFields(_gridnode, existGn, GN_SYNC_FIELDS);
      ServiceLookupHelper.getGridNodeManager().updateGridNode(existGn);

      _gridnode.setUId(existGn.getUId());
    }

    //syncBusinessEntities();
  }

  /**
   * Modes not supported.
   * 
   * @see com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel#sync(int)
   */
  public void sync(int mode) throws Throwable
  {
    sync();

  }

  // ********************** Own methods ************************************
  /* 021128NSL: Do not synchronize Business Entity(s) here.
   * Synchronize the Business Entity(s) to local database. This will in turn
   * synchronize the Channels and related profiles.
   *
   * @throws Throwable Error during synchronization.
   *
  private void syncBusinessEntities()
    throws Throwable
  {
    if (_bizEntities != null)
    {
      for (int i=0; i<_bizEntities.length; i++)
      {
        _bizEntities[i].sync();
      }
    }
  }
  */

  private CompanyProfile findCompanyProfile()
  {
    CompanyProfile prof = null;
    try
    {
      prof = ServiceLookupHelper.getGridNodeManager().getCompanyProfile(
                              (Long)_gridnode.getKey());
    }
    catch (Exception ex)
    {
      Logger.log("[SyncGridNode.findCompanyProfile] Unable to retrieve company profile for GridNode "+
        _gridnode.getID()+ ": "+ex.getMessage());
    }
    return prof;
  }

  private GridNode findGridNode(String gridnodeID)
  {
    GridNode gn = null;
    try
    {
      gn = ServiceLookupHelper.getGridNodeManager().findGridNodeByID(gridnodeID);
    }
    catch (Exception ex)
    {
      Logger.log("[SyncGridNode.findGridNode] No GridNode found with ID "+gridnodeID
        + ", error: "+ex.getMessage());
    }
    return gn;
  }

}