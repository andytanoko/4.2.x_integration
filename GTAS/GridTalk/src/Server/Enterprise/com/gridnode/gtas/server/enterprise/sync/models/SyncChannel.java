/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncChannel.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 01 2002    Neo Sok Lay         Created
 * Sep 08 2003    Neo Sok Lay         Add method:
 *                                    - sync(int mode)
 * Dec 31 2003    Neo Sok Lay         Add FlowControlInfo field.
 */
package com.gridnode.gtas.server.enterprise.sync.models;

import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This data object is a modified model of ChannelInfo for data transfer &
 * synchronization purpose. This object encapsulates the ChannelInfo to
 * be synchronized and all related profiles (CommInfo, PackagingInfo, and
 * SecurityInfo) are also wrapped by a Sync<profile> objects.<p>
 *
 * The additional modes assigned to this model is 0x100000,0x200000,0x400000,0x800000.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class SyncChannel extends AbstractSyncModel
{ 
  //public static final int MODE_OVERWRITE_EXISTING = 0x40;

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4541634694809809514L;

	private transient IChannelManagerObj  _channelMgr;

  private ChannelInfo         _channel;
  private SyncCommInfo        _commInfo;
  private SyncPackagingInfo   _packagingInfo;
  private SyncSecurityInfo    _securityInfo;

  private static final Number[]   CHANNEL_SYNC_FIELDS =
          {
            ChannelInfo.DESCRIPTION,
            ChannelInfo.NAME,
            ChannelInfo.REF_ID,
            ChannelInfo.TPT_COMM_INFO,
            ChannelInfo.TPT_PROTOCOL_TYPE,
            ChannelInfo.IS_MASTER,
            ChannelInfo.PACKAGING_PROFILE,
            ChannelInfo.SECURITY_PROFILE,
            ChannelInfo.IS_PARTNER,
            ChannelInfo.PARTNER_CAT,
            ChannelInfo.FLOWCONTROL_PROFILE,
          };

  public SyncChannel()
  {
  }

  public SyncChannel(ChannelInfo channel)
  {
    setChannel(channel);
    setCommProfile(new SyncCommInfo(channel.getTptCommInfo()));
    setPackagingProfile(new SyncPackagingInfo(channel.getPackagingProfile()));
    setSecurityProfile(new SyncSecurityInfo(channel.getSecurityProfile()));
  }

  // *************** Getters & Setters ***************************

  public ChannelInfo getChannel()
  {
    return _channel;
  }

  public void setChannel(ChannelInfo channel)
  {
    _channel = channel;
  }

  public SyncCommInfo getCommProfile()
  {
    return _commInfo;
  }

  public void setCommProfile(SyncCommInfo commInfo)
  {
    _commInfo = commInfo;
  }

  public SyncPackagingInfo getPackagingProfile()
  {
    return _packagingInfo;
  }

  public void setPackagingProfile(SyncPackagingInfo packagingInfo)
  {
    _packagingInfo = packagingInfo;
  }

  public SyncSecurityInfo getSecurityProfile()
  {
    return _securityInfo;
  }

  public void setSecurityProfile(SyncSecurityInfo securityInfo)
  {
    _securityInfo = securityInfo;
  }

  public boolean isMaster()
  {
    if (_channel != null)
      return _channel.isMaster();
    else
      return false;
  }

  public void setMaster(boolean master)
  {
    if (_channel != null)
      _channel.setIsMaster(master);
  }

  // **************** Overrides DataObject *********************************

  public void preSerialize()
  {
    _channel.preSerialize();
    //_commInfo.preSerialize();
    //_packagingInfo.preSerialize();
    //_securityInfo.preSerialize();
  }

  public void postSerialize()
  {
    //_commInfo.postSerialize();
    //_packagingInfo.postSerialize();
    //_securityInfo.postSerialize();
    _channel.postSerialize();
  }

  public void postDeserialize()
  {
    //if (_commInfo != null)
    //  _commInfo.postDeserialize();
    //if (_packagingInfo != null)
    //  _packagingInfo.postDeserialize();
    //if (_securityInfo != null)
    //  _securityInfo.postDeserialize();

    if (_channel != null)
    {
      _channel.setTptCommInfo(_commInfo.getCommInfo());
      _channel.setPackagingProfile(_packagingInfo.getPackagingInfo());
      _channel.setSecurityProfile(_securityInfo.getSecurityInfo());
      _channel.postDeserialize();
      if (_securityInfo.getEncryptionCert() != null)
        _securityInfo.getEncryptionCert().postDeserialize();
    }

  }

  // **************** Implements AbstractSyncModel **************************
  /**
   * Synchronize the content for the encapsulated ChannelInfo to the database.
   * The ChannelInfo will either be created (if not already exist) or updated.
   * Prior to synchronizing the ChannelInfo, the profiles (CommInfo, SecurityInfo,
   * and PackagingInfo) are first synchronized.<p>
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
    if (_commInfo != null)
    {
      _commInfo.sync();
      _channel.setTptCommInfo(_commInfo.getCommInfo());
    }
    if (_packagingInfo != null)
    {
      _packagingInfo.sync();
      _channel.setPackagingProfile(_packagingInfo.getPackagingInfo());
    }
    if (_securityInfo != null)
    {
      _securityInfo.sync();
      _channel.setSecurityProfile(_securityInfo.getSecurityInfo());
    }

    _channel.setPartnerCategory(IChannelInfo.CATEGORY_GRIDTALK);
    _channel.setIsMaster(isMaster());
    _channel.setIsPartner(true);

    ChannelInfo existChannel = getChannelInfo(_channel.getName(), _channel.getReferenceId());

    if (existChannel == null)
    {
      //add channel
      Long uID = getChannelMgr().createChannelInfo(_channel);
      _channel.setUId(uID.longValue());
    }
    else
    {
      //update channel
      copyFields(_channel, existChannel, CHANNEL_SYNC_FIELDS);
      getChannelMgr().updateChannelInfo(existChannel);
      _channel.setUId(existChannel.getUId());
    }
    */
  }

  /**
   * Supports combinations of MODE_IS_PARTNER, MODE_GT_PARTNER and MODE_CAN_DELETE.
   * @see com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel#sync(int)
   */
  public void sync(int mode) throws Throwable
  {
    if (_commInfo != null)
    {
      _commInfo.sync(mode);
      _channel.setTptCommInfo(_commInfo.getCommInfo());
    }
    if (_packagingInfo != null)
    {
      _packagingInfo.sync(mode);
      _channel.setPackagingProfile(_packagingInfo.getPackagingInfo());
    }
    if (_securityInfo != null)
    {
      _securityInfo.sync(mode);
      _channel.setSecurityProfile(_securityInfo.getSecurityInfo());
    }

    _channel.setCanDelete(isSet(mode, MODE_CAN_DELETE));
    _channel.setIsPartner(isSet(mode, MODE_IS_PARTNER));
    if (_channel.isPartner())
    {
      _channel.setPartnerCategory(
        isSet(mode, MODE_GT_PARTNER)?
        IChannelInfo.CATEGORY_GRIDTALK :
        IChannelInfo.CATEGORY_OTHERS);
    }
    _channel.setIsMaster(isMaster());

    ChannelInfo existChannel = getChannelInfo(_channel.getName(), _channel.getReferenceId());
    if (existChannel == null)
    {
      //add channel
      Long uID = getChannelMgr().createChannelInfo(_channel);
      _channel.setUId(uID.longValue());
    }
    else
    {
      //update channel
      if (_channel.getUId() != existChannel.getUId())
      {
        copyFields(_channel, existChannel, CHANNEL_SYNC_FIELDS);
        getChannelMgr().updateChannelInfo(existChannel);
        _channel.setUId(existChannel.getUId());
      }
    }
  }

  // ********************* Own methods **************************************
  /**
   * Set the enterprise ID that the ChannelInfo and CommInfo belongs to. The
   * enterprise ID will be used as ReferenceId of the ChannelInfo and CommInfo.
   *
   * @param enterpriseID The enterpriseID.
   */
  public void setEnterpriseID(String enterpriseID)
  {
    _channel.setReferenceId(enterpriseID);

    _commInfo.setEnterpriseID(enterpriseID);
    _packagingInfo.setEnterpriseID(enterpriseID);
    _securityInfo.setEnterpriseID(enterpriseID);
  }

  /**
   * Set the prefix for the names of the objects: ChannelInfo, CommInfo,
   * PackagingInfo, and SecurityInfo.
   *
   * @param prefix The name prefix.
   */
  public void setNamePrefix(String prefix)
  {
    _channel.setName(prefix + _channel.getName());

    _commInfo.setNamePrefix(prefix);
    _packagingInfo.setNamePrefix(prefix);
    _securityInfo.setNamePrefix(prefix);
  }

  /**
   * Get a ChannelInfo with the specified name.
   *
   * @param name The name of the ChannelInfo.
   * @param refId Reference Id of the ChannelInfo
   * @return The ChannelInfo retrieved, or null if none exist with that name.
   * @exception Throwable Error during retrieval.
   *
   * @since 2.0 I6
   */
  private ChannelInfo getChannelInfo(String name, String refId)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.NAME, filter.getEqualOperator(),
      name, false);
    filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.REF_ID,
      filter.getEqualOperator(), refId, false);

    Collection results = getChannelMgr().getChannelInfo(filter);

    if (results != null && results.size() > 0)
      return (ChannelInfo)results.iterator().next();

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