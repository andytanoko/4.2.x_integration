/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncBizEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 21 2002    Neo Sok Lay         Created
 * Oct 02 2002    Neo Sok Lay         Delegate some logic of synchronizing the
 *                                    resources to the Sync<Resource>Delegate.
 * Jan 06 2004    Neo Sok Lay         Remove setting for respondChannel.
 */
package com.gridnode.gtas.server.enterprise.sync.handlers;

import java.io.File;

import com.gridnode.gtas.server.enterprise.exceptions.SynchronizationFailException;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncResourceDelegate;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncResourceHandler;
import com.gridnode.gtas.server.enterprise.sync.SyncMessage;
import com.gridnode.gtas.server.enterprise.sync.models.SyncBusinessEntity;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

/**
 * Handler for synchronizing BusinessEntity(s).
 * <p>
 * The handler is capable of synchronizing one BusinessEntity at a time. On
 * synchronizing, the related resources like ChannelInfo(s) (+ CommInfo(s) +
 * PackagingInfo(s) + SecurityInfo(s) + Certificate(s) ) are also synchronized.
 * <p>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class SyncBizEntityHandler
  extends AbstractSyncResourceHandler
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 351338128000783105L;
	private boolean _receiveAckSucceed = false;
  private boolean _handleReceiveSucceed = false;

  //private Long    _respondChannel;

  /**
   * Clear any states kept within the handler (excluding configuration).
   * Subclasses may keep states within the handler until this method is called.
   * This method is only called when a synchronization transaction has ended.
   * Subclasses should be refrained from calling this method on other
   * occasions.
   *
   * @since 2.0 I4
   */
  protected void clearState()
  {
    _receiveAckSucceed = false;
    _handleReceiveSucceed = false;
    //_respondChannel = null;
  }

  /**
   * Handle a received Synchronization content message.
   *
   * @param msg The content message received.
   *
   * @since 2.0 I4
   */
  protected void handleReceive(SyncMessage msg)
    throws SynchronizationFailException
  {
    _handleReceiveSucceed = false;

    File[] receivedFiles = msg.getFilePayload();
    checkReceivedFilePayload(receivedFiles);

    /*021002NSL Delegate to SyncBizEntityHelper to handle synchronization
      to database.
    ArrayList channels = new ArrayList();
    BusinessEntity recBe = deserialize(receivedFiles, channels);

    syncContent(recBe, channels);
    */

    SyncBusinessEntity recBe = deserialize(receivedFiles);
    getDelegate().startSynchronize(recBe);

    /*060104NSL: Not required anymore
    //for mock scenario, cache the deserialize channel for sending the ack respond.
    if (recBe.getChannels() != null && recBe.getChannels().length > 0)
    {
      ChannelInfo defChannel = (ChannelInfo)recBe.getChannels()[0].getChannel();
      _respondChannel = (Long)defChannel.getKey();
    }
    // ------------------ end mock implementation --------------------------//
    */
    _handleReceiveSucceed = true;
  }

  /**
   * Handle a received Acknowledgement receipt message.
   *
   * @param ackMsg The acknowledge message received.
   *
   * @since 2.0 I4
   */
  protected void handleReceiveAck(SyncMessage ackMsg)
    throws SynchronizationFailException
  {
    String[] data = ackMsg.getDataPayload();
    if (data != null && data.length > 0)
    {
      Logger.log("[SyncBizEntityHandler.handleReceiveAck] " +
        "Received acknowledgement message " + ackMsg.getThisMsgID() +
        " for synchronizing BusinessEntity, status: " +data[0]);

      _receiveAckSucceed = Boolean.valueOf(data[0]).booleanValue();
      if (!_receiveAckSucceed)
        throw SynchronizationFailException.recipientSyncFailed();
    }
    else
    {
      Logger.warn("[SyncBizEntityHandler.handleReceiveAck] "+
        "Illegal data payload for acknowledgement");
      _receiveAckSucceed = false;
      throw SynchronizationFailException.illegalPayloadFormat();
    }
  }

  /**
   * Handle a message for sending. Typical requirement for subclasses implementations
   * is to add the payloads (data and file, if applicable) to be sent.
   * This will only be called for outgoing content messages.
   *
   * @param message The outgoing content message.
   * @param sendResourceUID The UID of the resource that is undergoing
   * synchronization via the message.
   * @param toEnterpriseID ID of recipient enterprise.
   *
   * @since 2.0 I4
   */
  protected void handleSend(SyncMessage message, Long sendResourceUID,
    String toEnterpriseID)
    throws SynchronizationFailException
  {
    //serialize the businessentity, channel + comminfo (+security+packaginginfo)

    try
    {
      BusinessEntity be = ServiceLookupHelper.getBizRegManager().findBusinessEntity(
                            sendResourceUID);

      /*021002NSL Construct a SyncBusinessEntity which handles serialization of
        all dependent objects.
      ArrayList channels =
        new ArrayList(ServiceLookupHelper.getEnterpriseHierarchyMgr().getChannelsForBizEntity(
            sendResourceUID));
      */

      SyncBusinessEntity syncBe = new SyncBusinessEntity(be);

      //Wrap around with a SyncGridNode object ** for Backward Compatibility **
      SyncGridNode gn = new SyncGridNode(getGridNode(be.getEnterpriseId()));
      gn.setBusinessEntities(new SyncBusinessEntity[] {syncBe});

      File[] filePayloads = new File[1]; /*021002NSL All in one file.
                                          + channels.size()];
                                          */

      String beFN = getUniqueSerFilename("BE", toEnterpriseID);
      //be.serialize(beFN);
      gn.serialize(beFN);
      filePayloads[0] = new File(beFN);

      /*021002NSL Put all objects in a single file above.
      String channelFN = null;
      for (int i=1; i<filePayloads.length; i++)
      {
        channelFN = getUniqueSerFilename("Channel", toEnterpriseID);
        ChannelInfo channel = ServiceLookupHelper.getChannelManager().getChannelInfo(
                                (Long)channels.get(i-1));

        channel.serialize(channelFN);
        filePayloads[i] = new File(channelFN);
      }
      */

      //add file payload
      message.setFilePayload(filePayloads);
    }
    catch (Throwable t)
    {
      Logger.warn("[SyncBizEntityHandler.handleSend] "+
        "Fail to serialize BusinessEntity and related resources!", t);
      throw SynchronizationFailException.serializeResourceFailed();
    }
  }

  /**
   * Handle an acknowledgement message in respond to a received content message.
   * Typical requirement for subclasses implementations is to: (a) add the payloads
   * (data and file, if applicable) to be sent; (b) set the channel to be used
   * for sending the acknowledgement message. This will only
   * be called for outgoing acknowledge messages.
   *
   * @param ackMsg The outgoing acknowledgement.
   * @param receivedMsg The received content message.
   *
   * @since 2.0 I4
   */
  protected void handleSendAck(SyncMessage ackMsg, SyncMessage receivedMsg)
    throws SynchronizationFailException
  {
    String[] data = {String.valueOf(_handleReceiveSucceed)};

    ackMsg.setDataPayload(data);

    /*060104NSL: not required anymore
    // ---------------- mock implmentation, to be removed ----------------
    //however, for mock scenario, use the cached deserialized channel
    ackMsg.setSendChannel(_respondChannel);
    */
  }

  /**
   * Returns an instance of SyncBizEntityDelegate.
   * @see AbstractSyncResourceHandler#getDelegate
   */
  protected AbstractSyncResourceDelegate getDelegate()
  {
    return new SyncBizEntityDelegate();
  }

  // ****************** Own Methods ************************************

  /**
   * Check whether expected number of files are received.
   *
   * @param receivedFiles The files received.
   * @exception SynchronizationFailException Number of files received are
   * incorrect.
   * @since 2.0 I4
   */
  private void checkReceivedFilePayload(File[] receivedFiles)
    throws SynchronizationFailException
  {
    if (receivedFiles == null || receivedFiles.length < 1)
    {
      Logger.warn("[SyncBizEntityHandler.checkReceivedFilePayload] "+
        "Illegal file payload for content message!");
      throw SynchronizationFailException.illegalPayloadFormat();
    }
  }

  /**
   * Deserialize the received files into BusinessEntity and Channel(s).
   *
   * @param receivedFiles The files received.
   * @param channels The placeholder for the deserialized Channel(s).
   * @return The deserialized BusinessEntity
   * @exception SynchronizationFailException Fail to deserialize the received
   * files into respective objects.
   *
   * @since 2.0 I4
   */
/*021002NSL replaced by deserialize(File[])
  private BusinessEntity deserialize(File[] receivedFiles, ArrayList channels)
    throws SynchronizationFailException
  {
    BusinessEntity tmpBe = new BusinessEntity();

    try
    {
      //deserialize businessentity+whitepage
      tmpBe = (BusinessEntity)tmpBe.deserialize(
                receivedFiles[0].getAbsolutePath());

      //deserialize channel+comminfo(+securityinfo+packaginginfo)
      ChannelInfo tmpChannel = new ChannelInfo();
      for (int i=1; i<receivedFiles.length; i++)
      {
        channels.add(tmpChannel.deserialize(receivedFiles[i].getAbsolutePath()));
      }
      return tmpBe;
    }
    catch (Throwable t)
    {
      Logger.err("[SyncBizEntityHandler.deserialize] " +
        "Fail to deserialize BusinessEntity and related resources!", t);
      throw SynchronizationFailException.deserializeResourceFailed();
    }
  }
*/
  /**
   * Deserialize the received files into SyncBusinessEntity.
   *
   * @param receivedFiles The files received.
   * @return The deserialized SyncBusinessEntity
   * @exception SynchronizationFailException Fail to deserialize the received
   * files into SyncBusinessEntity objects.
   *
   * @since 2.0 I6
   */
  private SyncBusinessEntity deserialize(File[] receivedFiles)
    throws SynchronizationFailException
  {
    SyncGridNode tmpGn = new SyncGridNode();

    try
    {
      //deserialize businessentity+whitepage+channels
      tmpGn = (SyncGridNode)tmpGn.deserialize(
                receivedFiles[0].getAbsolutePath());

      // ensure that the Enterprise Id in BE is set the same as the GridNode ID
      SyncBusinessEntity be = tmpGn.getBusinessEntities()[0];
      be.setEnterpriseID(tmpGn.getGridnode().getID());

      return be;
    }
    catch (Throwable t)
    {
      Logger.warn("[SyncBizEntityHandler.deserialize] " +
        "Fail to deserialize SyncBusinessEntity!", t);
      throw SynchronizationFailException.deserializeResourceFailed();
    }
  }


  /**
   * Synchronize the contents of the BusinessEntity and ChannelInfo(s).
   * Synchronization:-<p>
   * Non-existing database objects will be added, otherwise updated. Unused
   * existing database objects will be deleted. Association links between
   * database objects will also be updated.
   *
   * @param be The BusinessEntity to synchronize content.
   * @param channels List of ChannelInfo(s) to synchronize contents.
   * @exception SynchronizationFailException Fails to synchronize the contents
   * of the database objects.
   * @since 2.0 I4
   */
/*021002NSL Replaced by Delegating to SyncBizEntityHelper
  private void syncContent(BusinessEntity be, ArrayList channels)
    throws SynchronizationFailException
  {
    try
    {
      SyncBizEntityHelper syncBeHelper = new SyncBizEntityHelper();
      SyncChannelHelper syncCnHelper   = new SyncChannelHelper();

      //synchronize the business entity + whitepage
      syncBeHelper.syncBe(be);
      //synchronize the channels + comminfos
      ArrayList channelList = syncCnHelper.syncChannels(channels, be.getEnterpriseId());
      //update the be-channel links
      updateBeChannelLinks((Long)be.getKey(), channelList);
      //delete unused channels & comminfos
      syncCnHelper.deleteUnusedChannels(be.getEnterpriseId());
      syncCnHelper.deleteUnusedCommInfo(be.getEnterpriseId());
    }
    catch (Throwable t)
    {
      Logger.err("[SyncBizEntityHandler.syncContent] " +
        "Fail to synchronize contents of BusinessEntity and related resources!", t);
      throw SynchronizationFailException.syncContentFailed();
    }
  }
*/
  /**
   * Update the BusinessEntity-ChannelInfo resource links.
   *
   * @param beUID The UID of the BusinessEntity.
   * @param newChannelLinks The UIDs of the ChannelInfo(s) that the BusinessEntity
   * will now associate with.
   * @exception Throwable Error during the update.
   * @since 2.0 I4
   */
/*021002NSL Moved to SyncBizEntityHelper
  private void updateBeChannelLinks(
    Long beUID, Collection newChannelLinks)
    throws Throwable
  {
    //update be-channels links
    ServiceLookupHelper.getEnterpriseHierarchyMgr().setChannelsForBizEntity(
     beUID, newChannelLinks);
  }
*/

  /**
   * Get the GridNode entity that the BusinessEntity to be sync'ed belongs to.
   * If none exists (will happen only during development time), a mock one will
   * be returned.
   *
   * @param enterpriseID The enterpriseID from the BusinessEntity.
   * @return The GridNode entity retrieved based on the enterpriseID, or mock
   * GridNode entity created with minimal information populated.
   *
   * @since 2.0 I6
   */
  private GridNode getGridNode(String enterpriseID)
  {
    GridNode gn = null;
    try
    {
      gn = ServiceLookupHelper.getGridNodeManager().findGridNodeByID(enterpriseID);
    }
    catch (Exception ex)
    {
      Logger.warn("[SyncBizEntityHandler.getGridNode] Unable to find GridNode "+enterpriseID, ex);
    }

    if (gn == null)
    {
      // fake one
      gn = new GridNode();
      gn.setID(enterpriseID);
      gn.setName("GridNode "+enterpriseID);
    }

    return gn;
  }

}