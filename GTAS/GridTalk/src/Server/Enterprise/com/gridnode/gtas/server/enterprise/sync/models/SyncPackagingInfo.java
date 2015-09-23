/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncPackagingInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 01 2002    Neo Sok Lay         Created
 * Sep 08 2003    Neo Sok Lay         Add method:
 *                                    - sync(int mode)
 * Dec 31 2003    Neo Sok Lay         Remove Zip & split settings.
 * May 17 2004    Neo Sok Lay         Add Packaging Info extensions
 */
package com.gridnode.gtas.server.enterprise.sync.models;

import com.gridnode.gtas.model.channel.IPackagingInfo;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This data object is a modified model of PackagingInfo for data transfer &
 * synchronization purpose. This object encapsulates the PackagingInfo to
 * be synchronized.<p>
 *
 * The additional modes assigned to this model is 0x100,0x200,0x400,0x800.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I6
 */
public class SyncPackagingInfo extends AbstractSyncModel
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7905775392878452188L;
	//public static final int MODE_OVERWRITE_EXISTING = 0x200;

  private transient IChannelManagerObj _channelMgr;
  private PackagingInfo _packagingInfo;

  private static final Number[]   PACKAGINGINFO_SYNC_FIELDS =
          {
            PackagingInfo.DESCRIPTION,
            PackagingInfo.ENVELOPE,
            PackagingInfo.NAME,
            //PackagingInfo.ZIP,
            //PackagingInfo.ZIPTHRESHOLD,
            PackagingInfo.IS_DISABLE,
            PackagingInfo.IS_PARTNER,
            PackagingInfo.PARTNER_CAT,
            PackagingInfo.PKG_INFO_EXTENSION,
          };

  public SyncPackagingInfo()
  {
  }

  public SyncPackagingInfo(PackagingInfo packagingInfo)
  {
    setPackagingInfo(packagingInfo);
  }

  // ******************* Getters & Setters ********************************

  public PackagingInfo getPackagingInfo()
  {
    return _packagingInfo;
  }

  public void setPackagingInfo(PackagingInfo packagingInfo)
  {
    _packagingInfo = packagingInfo;
  }
  /*
  public boolean isZip()
  {
    if (_packagingInfo != null)
      return _packagingInfo.isZip();
    else
      return false;
  }

  public void setZip(boolean zip)
  {
    if (_packagingInfo != null)
      _packagingInfo.setZip(zip);
  }
  */
  // **************** Overrides DataObject *********************************

  public void preSerialize()
  {
    _packagingInfo.preSerialize();
  }

  public void postSerialize()
  {
    _packagingInfo.postSerialize();
  }

  public void postDeserialize()
  {
    if (_packagingInfo != null)
      _packagingInfo.postDeserialize();
  }

  // ***********************Implement AbstractSyncModel *********************

  /**
   * Synchronize the encapsulated PackagingInfo to the database. The PackagingInfo
   * will either be created (if not already exist) or updated.<p>
   * By default, the synchronization mode is: <pre>
   *   IS_PARTNER | GT_PARTNER
   * </pre>
   *
   * @exception Throwable Error in performing the operation.
   * @since 2.0 I6
   */
  public void sync() throws Throwable
  {
    // default is IS_PARTNER AND GRIDTALK_PARTNER AND CANNOT DELETE
    sync(MODE_IS_PARTNER | MODE_GT_PARTNER);

    /*030908NSL
    _packagingInfo.setIsPartner(true);
    _packagingInfo.setPartnerCategory(IPackagingInfo.CATEGORY_GRIDTALK);
    _packagingInfo.setCanDelete(false);

    //check packaginginfo
    PackagingInfo existPackagingInfo = getPackagingInfo(_packagingInfo.getName(),
                                         _packagingInfo.getReferenceId());

    if (existPackagingInfo == null)
    {
      //_packagingInfo.setPartnerCategory();

      //add packaginginfo
      Long uID = getChannelMgr().createPackagingInfo(_packagingInfo);
      _packagingInfo.setUId(uID.longValue());
    }
    else
    {
      //update packaginginfo
      copyFields(_packagingInfo, existPackagingInfo, PACKAGINGINFO_SYNC_FIELDS);
      getChannelMgr().updatePackagingInfo(existPackagingInfo);
      _packagingInfo.setUId(existPackagingInfo.getUId());
    }
    */
  }

  /**
   * Supports combinations of MODE_IS_PARTNER, MODE_GT_PARTNER and MODE_CAN_DELETE.
   * @see com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel#sync(int)
   */
  public void sync(int mode) throws Throwable
  {
    _packagingInfo.setCanDelete(isSet(mode, MODE_CAN_DELETE));
    _packagingInfo.setIsPartner(isSet(mode, MODE_IS_PARTNER));
    if (_packagingInfo.isPartner())
    {
      _packagingInfo.setPartnerCategory(
        isSet(mode, MODE_GT_PARTNER)?
        IPackagingInfo.CATEGORY_GRIDTALK :
        IPackagingInfo.CATEGORY_OTHERS);
    }

    //check packaginginfo
    PackagingInfo existPackagingInfo = getPackagingInfo(_packagingInfo.getName(),
                                         _packagingInfo.getReferenceId());

    if (existPackagingInfo == null)
    {
      //_packagingInfo.setPartnerCategory();

      //add packaginginfo
      Long uID = getChannelMgr().createPackagingInfo(_packagingInfo);
      _packagingInfo.setUId(uID.longValue());
    }
    else
    {
      //update packaginginfo
      if (_packagingInfo.getUId() != existPackagingInfo.getUId())
      {
        copyFields(_packagingInfo, existPackagingInfo, PACKAGINGINFO_SYNC_FIELDS);
        getChannelMgr().updatePackagingInfo(existPackagingInfo);
        _packagingInfo.setUId(existPackagingInfo.getUId());
      }
    }
  }

  // ****************** Own Methods ****************************************

  /**
   * Set the enterprise ID that the PackagingInfo belongs to. The
   * enterprise ID will be used as ReferenceId of the Packaging.
   *
   * @param enterpriseID The enterpriseID.
   */
  public void setEnterpriseID(String enterpriseID)
  {
    if (_packagingInfo != null)
      _packagingInfo.setReferenceId(enterpriseID);
  }

  /**
   * Set the prefix for the names of the encapsulated PackagingInfo.
   *
   * @param prefix The name prefix.
   */
  public void setNamePrefix(String prefix)
  {
    if (_packagingInfo != null)
      _packagingInfo.setName(prefix + _packagingInfo.getName());
  }

  /**
   * Get a PackagingInfo with the specified name.
   *
   * @param name The name of the PackagingInfo.
   * @param refId The referenceId of the PackagingInfo.
   * @return The PackagingInfo retrieved, or null if none exist with that name.
   * @exception Throwable Error during retrieval.
   *
   * @since 2.0 I6
   */
  private PackagingInfo getPackagingInfo(String name, String refId)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PackagingInfo.NAME, filter.getEqualOperator(),
      name, false);
    filter.addSingleFilter(filter.getAndConnector(), PackagingInfo.REF_ID,
      filter.getEqualOperator(), refId, false);

    Collection results = getChannelMgr().getPackagingInfo(filter);

    if (results != null && results.size() > 0)
      return (PackagingInfo)results.iterator().next();

    return null;
  }

  /**
   * Get a handle to the ChannelManagerBean. No lookup would be done if
   * one has already been done before.
   *
   * @return A handle to ChannelManagerBean.
   * @exception Throwable Error in obtaining a handle to the ChannelManagerBean.
   *
   * @since 2.0 I6
   */
  private IChannelManagerObj getChannelMgr()
    throws Throwable
  {
    if (_channelMgr == null)
    {
      _channelMgr = ServiceLookupHelper.getChannelManager();
    }

    return _channelMgr;
  }

}