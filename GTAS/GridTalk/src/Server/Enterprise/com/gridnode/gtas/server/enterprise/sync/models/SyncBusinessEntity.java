/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncBusinessEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 01 2002    Neo Sok Lay         Created
 * Nov 22 2002    Neo Sok Lay         Do not rename the sync channels & profiles.
 * Sep 08 2003    Neo Sok Lay         Add methods:
 *                                    - constructor SyncBusinessEntity(BusinessEntity,ChannelInfos)
 *                                    - sync(int mode)
 * Jan 05 2004    Neo Sok Lay         Add setEnterpriseId() method.
 * May 17 2004    Neo Sok Lay         New field to sync: DomainIdentifiers
 * Oct 17 2005    Neo Sok Lay         Change IBizRegistryManager to IBizRegistryManagerObj
 */
package com.gridnode.gtas.server.enterprise.sync.models;

import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.gtas.model.bizreg.IBusinessEntity;

import com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;

import com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.channel.model.ChannelInfo;

import java.util.ArrayList;

/**
 * This data object is a modified model of BusinessEntity for data transfer &
 * synchronization purpose. This object encapsulates the BusinessEntity to
 * be synchronized and, in addition, related information can be encapsulated
 * as well, such as the ChannelInfo.<p>
 *
 * The additional modes assigned to this model is 0x10000000,0x20000000,0x40000000,0x80000000.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I6
 */
public class SyncBusinessEntity extends AbstractSyncModel
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7786431566887931693L;

	//public static final int MODE_OVERWRITE_EXISTING = 0x10000000;
  
  private static final Number[]  BE_SYNC_FIELDS =
          {
            BusinessEntity.DESCRIPTION,
            BusinessEntity.ENTERPRISE_ID,
            BusinessEntity.ID,
            BusinessEntity.IS_PUBLISHABLE,
            BusinessEntity.IS_SYNC_TO_SERVER,
            BusinessEntity.STATE,
            BusinessEntity.WHITE_PAGE,
          };

  private transient IBizRegistryManagerObj _bizRegMgr;
  private transient IChannelManagerObj _channelMgr;
  private BusinessEntity _be = new BusinessEntity();
  private SyncChannel[]  _channels;
  
  public SyncBusinessEntity()
  {
  }

  /**
   * Constructs a SyncBusinessEntity instance for the specified
   * BusinessEntity and the existing associated channels of the BusinessEntity.
   *  
   * @param be The BusinessEntity
   */
  public SyncBusinessEntity(BusinessEntity be)
  {
    setBusinessEntity(be);
    constructSyncChannels();
  }

  /**
   * Constructs a SyncBusinessEntity instance for the specified BusinessEntity
   * and ChannelInfo(s).
   * 
   * @param be The BusinessEntity.
   * @param channels The ChannelInfo(s) which will be used to construct SyncChannel(s).
   */
  public SyncBusinessEntity(BusinessEntity be, ChannelInfo[] channels)
  {
    setBusinessEntity(be);
    constructSyncChannels(channels);
  }
  
  // ****************** Setters & Getters ********************************

  public BusinessEntity getBusinessEntity()
  {
    return _be;
  }

  public void setBusinessEntity(BusinessEntity be)
  {
    _be = be;
  }

  public SyncChannel[] getChannels()
  {
    return _channels;
  }

  public void setChannels(SyncChannel[] channels)
  {
    _channels = channels;
  }

  public boolean isDeleted()
  {
    if (_be != null)
      return _be.isDeleted();
    else
      return false;
  }

  public void setDeleted(boolean deleted)
  {
    if (_be != null)
      _be.setDeleted(deleted);
  }

  public boolean isPublishable()
  {
    if (_be != null)
      return _be.isPublishable();
    else
      return false;
  }

  public void setPublishable(boolean publishable)
  {
    if (_be != null)
      _be.setPublishable(publishable);
  }

  public boolean isSyncToServer()
  {
    if (_be != null)
      return _be.isSyncToServer();
    else
      return false;
  }

  public void setSyncToServer(boolean sync)
  {
    if (_be != null)
      _be.setSyncToServer(sync);
  }
  
  // **************** Overrides DataObject *********************************

  public void preSerialize()
  {
    _be.preSerialize();

    if (_channels != null)
    {
      for (int i=0; i<_channels.length; i++)
        _channels[i].preSerialize();
    }
  }

  public void postSerialize()
  {
    _be.postSerialize();

    if (_channels != null)
    {
      for (int i=0; i<_channels.length; i++)
        _channels[i].postSerialize();
    }
  }

  public void postDeserialize()
  {
    if (_be != null)
      _be.postDeserialize();

    if (_channels != null)
    {
      for (int i=0; i<_channels.length; i++)
        _channels[i].postDeserialize();
    }
  }

  // **************** Implement AbstractSyncModel *************************

  /**
   * Synchronize the contents of the BusinessEntity. If the BusinessEntity
   * does not already exist in the database, a record will be created for it.
   * Otherwise, the contents will be updated to the existing database record.
   * Encapsulated channels will also be synchronized.<p>
   * By default, the synchronization mode is: <pre>
   *   IS_PARTNER | GT_PARTNER
   * </pre>
   *
   * @exception Throwable Error in creating or updating to database.
   * @since 2.0 I6
   */
  public void sync()
    throws Throwable
  {
    // default is IS_PARTNER AND GRIDTALK_PARTNER AND CANNOT_DELETE
    sync(MODE_IS_PARTNER | MODE_GT_PARTNER);
    
    /*030908NSL
    BusinessEntity existBe = getBizMgr().findBusinessEntity(
                               _be.getEnterpriseId(), _be.getBusEntId());

    _be.setPartner(true);
    _be.setPartnerCategory(IBusinessEntity.CATEGORY_GRIDTALK);
    _be.setCanDelete(false);

    if (existBe == null) //new be
    {
      //add the be
      Long beUID = getBizMgr().createBusinessEntity(_be);
      _be.setUId(beUID.intValue());
    }
    else
    {
      //set the uids of the be & whitepage & update
      _be.getWhitePage().setUId(existBe.getWhitePage().getUId());
      _be.getWhitePage().setBeUId(new Long(existBe.getUId()));
      copyFields(_be, existBe, BE_SYNC_FIELDS);
      getBizMgr().updateBusinessEntity(existBe);

      _be.setUId(existBe.getUId());
    }

    // @todo set State based on GridNode state 

    syncChannels(_be.getEnterpriseId());
    */
  }

  /**
   * Supports combinations of MODE_IS_PARTNER, MODE_GT_PARTNER and MODE_CAN_DELETE.
   * @see com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel#sync(int)
   */
  public void sync(int mode) throws Throwable
  {
    _be.setCanDelete(
      isSet(mode, MODE_CAN_DELETE));
    _be.setPartner(isSet(mode, MODE_IS_PARTNER));
    if (_be.isPartner())
    {
      _be.setPartnerCategory(
        isSet(mode, MODE_GT_PARTNER)?
        IBusinessEntity.CATEGORY_GRIDTALK :
        IBusinessEntity.CATEGORY_OTHERS);
    }

    BusinessEntity existBe = getBizMgr().findBusinessEntity(
                               _be.getEnterpriseId(), _be.getBusEntId());

    //_be.setDomainIdentifiers(getDomainIdentifiers());
    if (existBe == null) //new be
    {
      //add the be
      Long beUID = getBizMgr().createBusinessEntity(_be);
      _be.setUId(beUID.intValue());
    }
    else
    {
      //set the uids of the be & whitepage & update
      _be.getWhitePage().setUId(existBe.getWhitePage().getUId());
      _be.getWhitePage().setBeUId(new Long(existBe.getUId()));
      copyFields(_be, existBe, BE_SYNC_FIELDS);
      existBe.setDomainIdentifiers(_be.getDomainIdentifiers()); //make sure beUids are set
      getBizMgr().updateBusinessEntity(existBe);

      _be.setUId(existBe.getUId());
    }

    /**@todo set State based on GridNode state */

    //syncChannels(_be.getEnterpriseId(), mode);
    syncChannels(mode);

  }

  // **************************** Own methods **********************************
  /**
   * Set the EnterpriseId of the BusinessEntity. This is done so for backward compatibility
   * reason. It does no harm to current version which comes with the EnterpriseId, since
   * the EnterpriseId should always follow the GridNodeID. 
   * 
   * @param enterpriseID the EnterpriseID (GridNodeID) of the BusinessEntity.
   */
  public void setEnterpriseID(String enterpriseID)
  {
    _be.setEnterpriseId(enterpriseID);
    
    //also set int the channels
    if (_channels != null)
    {
      for (int i=0; i<_channels.length; i++)
      {
        _channels[i].setEnterpriseID(enterpriseID);
      }
    }
  }
    
  /**
   * Construct the SyncChannel objects for the ChannelInfo(s) that are assigned
   * to this BusinessEntity to be synchronized.
   */
  private void constructSyncChannels()
  {
    try
    {
      Long[] channelUIDs =
        (Long[])ServiceLookupHelper.getEnterpriseHierarchyMgr().getChannelsForBizEntity(
            (Long)_be.getKey()).toArray(new Long[0]);

      SyncChannel[] syncChannels = new SyncChannel[channelUIDs.length];
      for (int i=0; i<channelUIDs.length; i++)
      {
        ChannelInfo channel = getChannelMgr().getChannelInfo(channelUIDs[i]);

        syncChannels[i] = new SyncChannel(channel);
      }
      setChannels(syncChannels);
    }
    catch (Throwable t)
    {
      Logger.err("[SyncBusinessEntity.constructSyncChannels] Error ", t);
    }
  }

  /**
   * Construct the SyncChannel objects based on the specified ChannelInfo(s).
   * 
   * @param channels The ChannelInfo(s) to construct SyncChannel for.
   */
  private void constructSyncChannels(ChannelInfo[] channels)
  {
    SyncChannel[] syncChannels = new SyncChannel[channels.length];
    for (int i=0; i<channels.length; i++)
    {
      syncChannels[i] = new SyncChannel(channels[i]);
    }
    setChannels(syncChannels);
  }
  
  /**
   * Synchronize the contents for ChannelInfo(s) and their CommInfo(s),
   * SecurityInfo(s), and PackagingInfo(s).
   *
   * @param mode Mode of synchronization.
   * @return List of UIDs of the synchronized ChannelInfo(s).
   * @exception Throwable Error during synchronizing the contents.
   *
   * @since 2.0 I6
   */
  //private void syncChannels(String enterpriseId, int mode)
  private void syncChannels(int mode)
    throws Throwable
  {
    /*221102NSL do not need to rename anymore. uniqueness base on name + refId */
    //resolve name conflicts
    //String prefix = "GN"+enterpriseId;
    for (int i=0; i<_channels.length; i++)
    {
      //_channels[i].setNamePrefix(prefix);
      //050104NSL: Don't set enterpriseId here, it is already set earlier on
      //_channels[i].setEnterpriseID(enterpriseId);

      //synchronize
      _channels[i].sync(mode);
    }
  }

  /**
   * Obtain the UIDs of the ChannelInfo(s) that are now assigned to the
   * BusinessEntity being synchronized.
   */
  public ArrayList getChannelUIDs()
  {
    ArrayList channelUIDs = new ArrayList();

    //resolve name conflicts
    for (int i=0; i<_channels.length; i++)
    {
      channelUIDs.add(_channels[i].getChannel().getKey());
    }
    return channelUIDs;
  }

  /**
   * Get a handle to the BizRegistryManagerBean. No lookup would be done if
   * one has already been done before.
   *
   * @return A handle to BizRegistryManagerBean.
   * @exception Throwable Error in obtaining a handle to the BizRegistryManagerBean.
   *
   * @since 2.0 I6
   */
  private IBizRegistryManagerObj getBizMgr()
    throws Throwable
  {
    if (_bizRegMgr == null)
    {
      _bizRegMgr = ServiceLookupHelper.getBizRegManager();
    }

    return _bizRegMgr;
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