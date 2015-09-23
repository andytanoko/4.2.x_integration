/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncGridNodeDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 28 2002    Neo Sok Lay         Created
 * Sep 08 2003    Neo Sok Lay         Add methods:
 *                                    - startSynchronize(syncModel, mode)
 *                                    - syncBusinessEntities(syncBes)
 *                                    - syncBusinessEntities(syncBes, mode)
 * Jan 02 2004    Neo Sok Lay         Create default Master channel for SyncBusinessEntity
 *                                    that do not have channels attached.
 */
package com.gridnode.gtas.server.enterprise.sync.handlers;

import com.gridnode.gtas.server.channel.helpers.MasterChannelCreator;
import com.gridnode.gtas.server.enterprise.exceptions.SynchronizationFailException;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncResourceDelegate;
import com.gridnode.gtas.server.enterprise.sync.models.*;
import com.gridnode.gtas.server.enterprise.sync.models.SyncBusinessEntity;
import com.gridnode.gtas.server.enterprise.sync.models.SyncChannel;
import com.gridnode.gtas.server.enterprise.sync.models.SyncCommInfo;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.base.certificate.model.Certificate;

import java.io.File;

/**
 * Delegate class to assist in synchronizing the contents of a GridNode,
 * and its attached Business Entity(s).
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
public class SyncGridNodeDelegate extends AbstractSyncResourceDelegate
{
  private File _certFile;

  /**
   * Accepts a SyncGridNode for synchronization.
   *
   * @see AbstractSyncResourceDelegate#startSynchronize
   */
  public void startSynchronize(AbstractSyncModel syncModel)
    throws SynchronizationFailException
  {
    try
    {
      SyncGridNode syncGn = (SyncGridNode) syncModel;

      //synchronize the GridNode
      syncGn.sync();

      String enterpriseId = syncGn.getGridnode().getID();
      syncBusinessEntities(enterpriseId, syncGn.getBusinessEntities());

      /*030908NSL
      //synchronize the Business entities underneath
      SyncBusinessEntity[] syncBes = syncGn.getBusinessEntities();
      if (syncBes != null)
      {
        SyncBizEntityDelegate syncBeDelegate = new SyncBizEntityDelegate();
        for (int i=0; i<syncBes.length; i++)
        {
          syncBeDelegate.startSynchronize(syncBes[i]);
        }
      }
      */
    }
    catch (Throwable t)
    {
      Logger.err(
        "[SyncGridNodeDelegate.startSynchronize] "
          + "Fail to synchronize contents of GridNode and related resources!",
        t);
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
      SyncGridNode syncGn = (SyncGridNode) syncModel;

      //synchronize the GridNode
      syncGn.sync(mode);

      String enterpriseId = syncGn.getGridnode().getID();
      syncBusinessEntities(enterpriseId, syncGn.getBusinessEntities(), mode);
    }
    catch (Throwable t)
    {
      Logger.err(
        "[SyncGridNodeDelegate.startSynchronize] "
          + "Fail to synchronize contents of GridNode and related resources!",
        t);
      throw SynchronizationFailException.syncContentFailed();
    }
  }

  /**
   * Synchronize the BusinessEntity(s) of the GridNode.
   *
   * @param enterpriseId The enterprise id (gridnode id)
   * @param syncBes The SyncBusinessEntity models.
   * @throws Throwable Error synchronizing the BusinessEntity(s).
   */
  private void syncBusinessEntities(
    String enterpriseId,
    SyncBusinessEntity[] syncBes)
    throws Throwable
  {
    if (syncBes != null && syncBes.length > 0)
    {
      SyncChannel[] defaultChannels =
        createDefaultChannels(enterpriseId, syncBes[0]);

      SyncBizEntityDelegate syncBeDelegate = new SyncBizEntityDelegate();
      for (int i = 0; i < syncBes.length; i++)
      {
        if (defaultChannels != null)
          syncBes[i].setChannels(defaultChannels);
        syncBes[i].getBusinessEntity().setEnterpriseId(enterpriseId);
        syncBeDelegate.startSynchronize(syncBes[i]);
      }
    }
  }

  /**
   * Synchronize the BusinessEntity(s) of the GridNode.
   *
   * @param enterpriseId The enterprise id (gridnode id)
   * @param syncBes The SyncBusinessEntity models.
   * @param mode The mode of synchronization, as defined for the SyncBusinessEntity.
   * @throws Throwable Error synchronizing the BusinessEntity.
   */
  private void syncBusinessEntities(
    String enterpriseId,
    SyncBusinessEntity[] syncBes,
    int mode)
    throws Throwable
  {
    if (syncBes != null && syncBes.length > 0)
    {
      SyncChannel[] defaultChannels =
        createDefaultChannels(enterpriseId, syncBes[0]);

      SyncBizEntityDelegate syncBeDelegate = new SyncBizEntityDelegate();
      for (int i = 0; i < syncBes.length; i++)
      {
        if (defaultChannels != null)
          syncBes[i].setChannels(defaultChannels);
        syncBeDelegate.startSynchronize(syncBes[i], mode);
      }
    }
  }

  /**
   * Set the certificate file. This file will be used for creating default master certificate.
   *
   * @param certFile The certificate file.
   */
  public void setCertificateFile(File certFile)
  {
    _certFile = certFile;
  }

  /**
   * Create default channels for a SyncBusinessEntity if it has no channel attached.
   * If creation of default channels is necessary, a certificate file must be set beforehand.
   *
   * @param enterpriseId The enterprise Id (gridnode id)
   * @param syncBe The SyncBusinessEntity.
   * @return If <code>syncBe</code> does not have any SyncChannel(s), a default Master channel will
   * be created and returned. No modification is done to <code>syncBe</code>.
   * @throws Exception Error creating master certificate based on the certificate file set earlier.
   */
  private SyncChannel[] createDefaultChannels(
    String enterpriseId,
    SyncBusinessEntity syncBe)
    throws Exception
  {
    SyncChannel[] defaultChannels = null;
    //Assumption: no channels attached means the synchronization party is a GridTalk 1
    if (syncBe.getChannels() == null || syncBe.getChannels().length == 0)
    {
      MasterChannelCreator creator =
        new MasterChannelCreator(enterpriseId, true, true, _certFile);

      SyncChannel masterChannel =
        createSyncChannel(
          creator.getMasterChannel(),
          creator.getMasterCertificate());
      defaultChannels = new SyncChannel[] { masterChannel };
    }

    return defaultChannels;
  }

  /**
   * Create a SyncChannel based on the specified ChannelInfo and Certificate.
   *
   * @param channel The ChannelInfo that will be embedded in the SyncChannel created.
   * @param certificate The Certificate that will be embedded in the SyncCertificate, in SyncSecurityInfo,
   * in SyncChannel created. The Certificate is treated as the Encryption certificate.
   * @return The SyncChannel created.
   */
  private SyncChannel createSyncChannel(
    ChannelInfo channel,
    Certificate certificate) throws Exception
  {
    SyncChannel syncChannel = new SyncChannel();
    syncChannel.setChannel(channel);

    SyncCommInfo syncCommInfo = new SyncCommInfo();
    syncCommInfo.setCommInfo(channel.getTptCommInfo());
    syncChannel.setCommProfile(syncCommInfo);

    SyncPackagingInfo syncPkgInfo = new SyncPackagingInfo();
    syncPkgInfo.setPackagingInfo(channel.getPackagingProfile());
    syncChannel.setPackagingProfile(syncPkgInfo);

    SyncSecurityInfo syncSecInfo = new SyncSecurityInfo();
    syncSecInfo.setSecurityInfo(channel.getSecurityProfile());
    syncChannel.setSecurityProfile(syncSecInfo);

    SyncCertificate syncCert = new SyncCertificate(certificate);
    //syncCert.setCert(certificate);
    syncSecInfo.setEncryptionCert(syncCert);

    return syncChannel;
  }
}