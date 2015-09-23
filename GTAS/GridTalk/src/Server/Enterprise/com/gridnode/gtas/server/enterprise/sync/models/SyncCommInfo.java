/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncCommInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 01 2002    Neo Sok Lay         Created
 * Dec 03 2002    Neo Sok Lay         CommInfo data model change.
 * Sep 08 2003    Neo Sok Lay         Add method:
 *                                    - sync(int mode)
 */
package com.gridnode.gtas.server.enterprise.sync.models;

import com.gridnode.gtas.model.channel.ICommInfo;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This data object is a modified model of CommInfo for data transfer &
 * synchronization purpose. This object encapsulates the CommInfo to
 * be synchronized.<p>
 *
 * The additional modes assigned to this model is 0x10000,0x20000,0x40000,0x80000.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I6
 */
public class SyncCommInfo extends AbstractSyncModel
{ 
  //public static final int MODE_OVERWRITE_EXISTING = 0x80;

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8208119029317319292L;

	private transient IChannelManagerObj _channelMgr;

  private CommInfo _commInfo;

  private static final Number[]   COMMINFO_SYNC_FIELDS =
          {
            CommInfo.DESCRIPTION,
            //CommInfo.HOST,
            CommInfo.IS_DEFAULT_TPT,
            CommInfo.NAME,
            //CommInfo.PORT,
            //CommInfo.PROTOCOL_DETAIL,
            CommInfo.PROTOCOL_TYPE,
            //CommInfo.PROTOCOL_VERSION,
            CommInfo.URL,
            CommInfo.REF_ID,
            CommInfo.TPT_IMPL_VERSION,
            CommInfo.IS_PARTNER,
            CommInfo.PARTNER_CAT,
          };

  public SyncCommInfo()
  {
  }

  public SyncCommInfo(CommInfo commInfo)
  {
    setCommInfo(commInfo);
  }

  // ******************* Getters & Setters ********************************

  public CommInfo getCommInfo()
  {
    return _commInfo;
  }

  public void setCommInfo(CommInfo commInfo)
  {
    _commInfo = commInfo;
  }

  public boolean isDefaultTpt()
  {
    if (_commInfo != null)
      return _commInfo.isDefaultTpt();
    else
      return false;
  }

  public void setDefaultTpt(boolean defTpt)
  {
    if (_commInfo != null)
      _commInfo.setIsDefaultTpt(defTpt);
  }

  // **************** Overrides DataObject *********************************

  public void preSerialize()
  {
    _commInfo.preSerialize();
  }

  public void postSerialize()
  {
    _commInfo.postSerialize();
  }

  public void postDeserialize()
  {
    if (_commInfo != null)
      _commInfo.postDeserialize();
  }

  // ******************* Implement AbstractSyncModel ***********************

  /**
   * Synchronize the content for a CommInfo to the database. The CommInfo
   * will either be created (if not already exist) or updated.<p>
   * By default, the synchronization mode is: <pre>
   *   IS_PARTNER | GT_PARTNER
   * </pre>
   *
   * @exception Throwable Error in performing the operation.
   * @since 2.0 I6
   */
  public void sync()
    throws Throwable
  {
    // default is IS_PARTNER AND GRIDTALK_PARTNER AND CANNOT DELETE
    sync(MODE_IS_PARTNER | MODE_GT_PARTNER);

    /*030908NSL
    _commInfo.setIsPartner(true);
    _commInfo.setPartnerCategory(ICommInfo.CATEGORY_GRIDTALK);
    _commInfo.setCanDelete(false);

    //check comminfo
    CommInfo existCommInfo = getCommInfo(_commInfo.getName(), _commInfo.getRefId());

    if (existCommInfo == null)
    {
      //add comminfo
      Long uID = getChannelMgr().createCommInfo(_commInfo);
      _commInfo.setUId(uID.longValue());
    }
    else
    {
      //update comminfo
      copyFields(_commInfo, existCommInfo, COMMINFO_SYNC_FIELDS);
      getChannelMgr().updateCommInfo(existCommInfo);
      _commInfo.setUId(existCommInfo.getUId());
    }
    */
  }

  /**
   * Supports combinations of MODE_IS_PARTNER, MODE_GT_PARTNER and MODE_CAN_DELETE.
   * @see com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel#sync(int)
   */
  public void sync(int mode) throws Throwable
  {
    _commInfo.setCanDelete(isSet(mode, MODE_CAN_DELETE));
    _commInfo.setIsPartner(isSet(mode, MODE_IS_PARTNER));
    if (_commInfo.isPartner())
    {
      _commInfo.setPartnerCategory(
        isSet(mode, MODE_GT_PARTNER)?
        ICommInfo.CATEGORY_GRIDTALK :
        ICommInfo.CATEGORY_OTHERS);
    }

    //check comminfo
    CommInfo existCommInfo = getCommInfo(_commInfo.getName(), _commInfo.getRefId());

    if (existCommInfo == null)
    {
      //add comminfo
      Long uID = getChannelMgr().createCommInfo(_commInfo);
      _commInfo.setUId(uID.longValue());
    }
    else
    {
      //update comminfo
      if (_commInfo.getUId() != existCommInfo.getUId())
      {
        copyFields(_commInfo, existCommInfo, COMMINFO_SYNC_FIELDS);
        getChannelMgr().updateCommInfo(existCommInfo);
        _commInfo.setUId(existCommInfo.getUId());
      }
    }
  }

  // ****************** Own Methods ****************************************

  /**
   * Set the enterprise ID that the CommInfo belongs to. The
   * enterprise ID will be used as ReferenceId of the CommInfo.
   *
   * @param enterpriseID The enterpriseID.
   */
  public void setEnterpriseID(String enterpriseID)
  {
    if (_commInfo != null)
      _commInfo.setRefId(enterpriseID);
  }

  /**
   * Set the prefix for the names of the encapsulated CommInfo.
   *
   * @param prefix The name prefix.
   */
  public void setNamePrefix(String prefix)
  {
    if (_commInfo != null)
      _commInfo.setName(prefix + _commInfo.getName());
  }

  /**
   * Get a CommInfo with the specified name.
   *
   * @param name The name of the CommInfo.
   * @param refId The ReferenceId of the CommInfo.
   * @return The CommInfo retrieved, or null if none exist with that name.
   * @exception Throwable Error during retrieval.
   *
   * @since 2.0 I6
   */
  private CommInfo getCommInfo(String name, String refId)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.NAME, filter.getEqualOperator(),
      name, false);
    filter.addSingleFilter(filter.getAndConnector(), CommInfo.REF_ID,
      filter.getEqualOperator(), refId, false);

    Collection results = getChannelMgr().getCommInfo(filter);

    if (results != null && results.size() > 0)
      return (CommInfo)results.iterator().next();

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