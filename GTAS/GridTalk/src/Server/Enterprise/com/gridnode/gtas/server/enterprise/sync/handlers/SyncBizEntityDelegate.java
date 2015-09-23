/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncBizEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 02 2002    Neo Sok Lay         Created
 * Sep 08 2003    Neo Sok Lay         Add methods:
 *                                    - startSynchronize(syncModel, mode)
 *                                    - updateChannels(SyncBusinessEntity)
 * Jan 15 2004    Neo Sok Lay         Set master channel if one is not present
 *                                    for synchronizing, like the GT 1 case.
 */
package com.gridnode.gtas.server.enterprise.sync.handlers;

import com.gridnode.gtas.server.enterprise.exceptions.SynchronizationFailException;
import com.gridnode.gtas.server.enterprise.facade.ejb.IResourceLinkManagerObj;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.gtas.server.enterprise.model.ResourceLink;
import com.gridnode.gtas.server.enterprise.sync.models.SyncChannel;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncResourceDelegate;
import com.gridnode.gtas.server.enterprise.sync.models.SyncBusinessEntity;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Delegate class to assist in synchronizing the contents of a BusinessEntity,
 * and its attached ChannelInfo(s).
 * <p>
 * Note that an instance of this helper class should be short-lived. That is,
 * it should not be used for long period of time. It should be disposed of
 * as soon as it is not required during a synchronization session.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class SyncBizEntityDelegate
  extends    AbstractSyncResourceDelegate
{
  private IChannelManagerObj        _channelMgr;
  private IResourceLinkManagerObj   _resLinkMgr;

  /**
   * Accepts a SyncBusinessEntity for synchronization.
   *
   * @see AbstractSyncResourceDelegate#startSynchronize
   */
  public void startSynchronize(AbstractSyncModel syncModel)
    throws SynchronizationFailException
  {
    try
    {
      SyncBusinessEntity syncBe = (SyncBusinessEntity)syncModel;

      // try to retrieve the master channel and set into SyncBusinessEntity
      // this is for receiving synchronization from GridTalk 1 which don't
      // send the channels across. In this case, the GT 1 partner's master
      // channel should be automatically assigned
      setMasterChannel(syncBe);

      //synchronize the BusinessEntity & Channels
      syncBe.sync();

      updateChannels(syncBe);

      /*030908NSL
      //update the be-channel links
      ArrayList channelList = syncBe.getChannelUIDs();
      updateBeChannelLinks((Long)syncBe.getBusinessEntity().getKey(), channelList);

      //delete unused channels & comminfos
      String enterpriseID = syncBe.getBusinessEntity().getEnterpriseId();
      deleteUnusedChannels(enterpriseID);
      deleteUnusedProfiles(enterpriseID);
      */
    }
    catch (Throwable t)
    {
      Logger.warn("[SyncBizEntityDelegate.startSynchronize] " +
        "Fail to synchronize contents of BusinessEntity and related resources!", t);
      throw SynchronizationFailException.syncContentFailed();
    }

  }

  /**
   * @see com.gridnode.gtas.server.enterprise.sync.AbstractSyncResourceDelegate#startSynchronize(com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel, int)
   */
  public void startSynchronize(AbstractSyncModel syncModel, int mode)
    throws SynchronizationFailException
  {
    try
    {
      SyncBusinessEntity syncBe = (SyncBusinessEntity)syncModel;

      // try to retrieve the master channel and set into SyncBusinessEntity
      // this is for receiving synchronization from GridTalk 1 which don't
      // send the channels across. In this case, the GT 1 partner's master
      // channel should be automatically assigned
      setMasterChannel(syncBe);

      //synchronize the BusinessEntity & Channels using the specified mode
      syncBe.sync(mode);

      updateChannels(syncBe);
    }
    catch (Throwable t)
    {
      Logger.warn("[SyncBizEntityDelegate.startSynchronize] " +
        "Fail to synchronize contents of BusinessEntity and related resources!", t);
      throw SynchronizationFailException.syncContentFailed();
    }
  }

  /**
   * Updates the Be-Channel links and remove any unused Channels and associated
   * profiles.
   *
   * @param syncBe The SyncBusinessEntity model
   * @throws Throwable Error in updating.
   */
  private void updateChannels(SyncBusinessEntity syncBe)
    throws Throwable
  {
    //update the be-channel links
    ArrayList channelList = syncBe.getChannelUIDs();
    updateBeChannelLinks((Long)syncBe.getBusinessEntity().getKey(), channelList);

    //delete unused channels & comminfos
    String enterpriseID = syncBe.getBusinessEntity().getEnterpriseId();
    deleteUnusedChannels(enterpriseID);
    deleteUnusedProfiles(enterpriseID);
  }

  /**
   * Update the BusinessEntity-ChannelInfo resource links.
   *
   * @param beUID The UID of the BusinessEntity.
   * @param newChannelLinks The UIDs of the ChannelInfo(s) that the BusinessEntity
   * will now associate with.
   * @exception Throwable Error during the update.
   * @since 2.0 I6
   */
  private void updateBeChannelLinks(Long beUID, Collection newChannelLinks)
    throws Throwable
  {
    //update be-channels links
    ServiceLookupHelper.getEnterpriseHierarchyMgr().setChannelsForBizEntity(
     beUID, newChannelLinks);
  }

  /**
   * Delete those ChannelInfo(s) that are not in used anymore for an enterprise.
   *
   * @param enterpriseId The ID of the enterprise.
   * @exception Throwable Error in performing the operation.
   * @since 2.0 I6
   */
  private void deleteUnusedChannels(String enterpriseId)
    throws Throwable
  {
    Logger.debug("[SyncBizEntityDelegate.deleteUnusedChannels] Enter: "+enterpriseId);

    //get all channelInfos for the enterprise
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
      enterpriseId, false);

    Collection channelUIDs = getChannelMgr().getChannelInfoUIDs(filter);

    for (Iterator i=channelUIDs.iterator(); i.hasNext(); )
    {
      Long uid = (Long)i.next();

      //determine if any links to this channelinfo
      DataFilterImpl rlFilter = new DataFilterImpl();
      rlFilter.addSingleFilter(null, ResourceLink.TO_TYPE, rlFilter.getEqualOperator(),
        IResourceTypes.CHANNEL, false);
      rlFilter.addSingleFilter(rlFilter.getAndConnector(), ResourceLink.TO_UID,
        rlFilter.getEqualOperator(), uid, false);

      Collection existLinks = getResourceLinkMgr().getResourceLinkUIDsByFilter(rlFilter);

      //if not, delete it
      if (existLinks.isEmpty())
      {
        Logger.debug("[SyncBizEntityDelegate.deleteUnusedChannels] To delete channel: "+uid);
        getChannelMgr().deleteChannelInfo(uid);
      }
      else
        Logger.debug("[SyncBizEntityDelegate.deleteUnusedChannels] Retaining channel: "+uid);
    }
  }

  /**
   * Delete those profiles (CommInfo, SecurityInfo, PackagingInfo) that are not
   * in used anymore for an enterprise.
   *
   * @param enterpriseId The ID of the enterprise.
   * @exception Throwable Error in performing the operation.
   * @since 2.0 I6
   */
  private void deleteUnusedProfiles(String enterpriseId)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
      enterpriseId, false);

    Collection channels = getChannelMgr().getChannelInfo(filter);

    ArrayList usedCommInfoUIDs = new ArrayList();
    ArrayList usedPackagingInfoUIDs = new ArrayList();
    ArrayList usedSecurityInfoUIDs = new ArrayList();
    for (Iterator i=channels.iterator(); i.hasNext(); )
    {
      ChannelInfo channel = (ChannelInfo)i.next();
      usedCommInfoUIDs.add(channel.getTptCommInfo().getKey());
      usedPackagingInfoUIDs.add(channel.getPackagingProfile().getKey());
      usedSecurityInfoUIDs.add(channel.getSecurityProfile().getKey());
    }

    //delete those for the enterprise but not used.
    if (!usedCommInfoUIDs.isEmpty())
    {
      DataFilterImpl ciFilter = new DataFilterImpl();
      ciFilter.addSingleFilter(null, CommInfo.REF_ID, ciFilter.getEqualOperator(),
        enterpriseId, false);
      ciFilter.addDomainFilter(ciFilter.getAndConnector(), CommInfo.UID,
        usedCommInfoUIDs, true);

      Collection toDelete = getChannelMgr().getCommInfoUIDs(ciFilter);
      for (Iterator i=toDelete.iterator(); i.hasNext(); )
      {
        getChannelMgr().deleteCommInfo((Long)i.next());
      }
    }

    if (!usedPackagingInfoUIDs.isEmpty())
    {
      DataFilterImpl piFilter = new DataFilterImpl();
      piFilter.addDomainFilter(null, PackagingInfo.UID,
        usedPackagingInfoUIDs, true);
      piFilter.addSingleFilter(piFilter.getAndConnector(),
        PackagingInfo.REF_ID, piFilter.getEqualOperator(), enterpriseId, false);

      Collection toDelete = getChannelMgr().getPackagingInfoUIDs(piFilter);
      for (Iterator i=toDelete.iterator(); i.hasNext(); )
      {
        getChannelMgr().deletePackigingInfo((Long)i.next());
      }
    }

    if (!usedSecurityInfoUIDs.isEmpty())
    {
      DataFilterImpl siFilter = new DataFilterImpl();
      siFilter.addDomainFilter(null, SecurityInfo.UID,
        usedSecurityInfoUIDs, true);
      siFilter.addSingleFilter(siFilter.getAndConnector(),
        SecurityInfo.REF_ID, siFilter.getEqualOperator(), enterpriseId, false);

      Collection toDelete = getChannelMgr().getSecurityInfoUIDs(siFilter);
      for (Iterator i=toDelete.iterator(); i.hasNext(); )
      {
        getChannelMgr().deleteSecurityInfo((Long)i.next());
      }
    }
  }

  /**
   * Try to set the enterprise's Master Channel if one is not present in
   * the synchronization model.
   *
   * @param syncBe SyncBusinessEntity that is to be synchronized.
   * @throws Exception Error retrieving the Master Channel.
   */
  private void setMasterChannel(SyncBusinessEntity syncBe) throws Exception
  {
    if (syncBe.getChannels() == null || syncBe.getChannels().length==0)
    {
      ChannelInfo channel = getMasterChannel(syncBe.getBusinessEntity().getEnterpriseId());
      SyncChannel syncChannel = new SyncChannel(channel);
      syncBe.setChannels(new SyncChannel[]{syncChannel});
    }
  }

  /**
   * Get the Master Channel for an enterprise.
   *
   * @param enterpriseId The enterprise Id
   * @return The retrieved Master Channel
   * @throws Throwable Error retrieving the master channel, or master channel not found.
   */
  private ChannelInfo getMasterChannel(String enterpriseId) throws Exception
  {
    Long channelUid = ActionHelper.getMasterChannelUID(enterpriseId);
    return getChannelMgr().getChannelInfo(channelUid);
  }

  /**
   * Get a handle to the ResourceLinkManagerBean. No lookup would be done if
   * one has already been done before.
   *
   * @return A handle to ResourceLinkManagerBean.
   * @exception Throwable Error in obtaining a handle to the ResourceLinkManagerBean.
   *
   * @since 2.0 I6
   */
  private IResourceLinkManagerObj getResourceLinkMgr()
    throws Throwable
  {
    if (_resLinkMgr == null)
    {
      _resLinkMgr = ServiceLookupHelper.getResourceLinkMgr();
    }

    return _resLinkMgr;
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
    throws Exception
  {
    if (_channelMgr == null)
    {
      _channelMgr = ServiceLookupHelper.getChannelManager();
    }

    return _channelMgr;
  }

}