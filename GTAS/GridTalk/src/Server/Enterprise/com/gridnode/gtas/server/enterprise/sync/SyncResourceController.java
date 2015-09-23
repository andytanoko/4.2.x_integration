/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncResourceController.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 2002    Neo Sok Lay         Created
 * Sep 06 2002    Neo Sok Lay         Move handlerMessage method to MessageReceiver.
 *                                    Not required to register the eventids.
 * Sep 22 2002    Neo Sok Lay         Retrieve EnterpriseID (GridnodeID) from
 *                                    GridNodeManagerBean.
 * Oct 01 2003    Neo Sok Lay         Pass fixed-size header array to ChannelManager when
 *                                    sending.
 * Jan 05 2004    Neo Sok Lay         Modify synchronization framework for backward compatibility
 *                                    with GT 1.x synchronization.
 */
package com.gridnode.gtas.server.enterprise.sync;

import java.io.File;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.enterprise.exceptions.SynchronizationFailException;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.ISyncResConfig;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.model.SharedResource;
import com.gridnode.gtas.server.enterprise.post.PostInstruction;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.rdm.helpers.IGtasConfig;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.file.access.FileAccess;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;

/**
 * Controller for synchronizing SharedResource(s) to a partner enterprise.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class SyncResourceController
  implements ISyncResConfig
{
  private static final SyncResourceController _self = new SyncResourceController();

  private final MessageFormat FILE_ID_FORMAT = new MessageFormat("syncResource_E{0}_T{1}_S{2}_R{3}");

  private Hashtable                     _resSyncTable;
  private Hashtable                     _msgTable;
  private String                        _thisEnterpriseID;
  private boolean                       _isTest;
  private String                        _serDir;
  //private Random                        _randGen = new Random(System.currentTimeMillis());

  private SyncResourceController()
  {
    _resSyncTable = new Hashtable();
    _msgTable = new Hashtable();
    initConfigurations();
  }

  /**
   * Get a singleton instance of the controller.
   *
   * @return
   *
   * @since 2.0
   */
  public static SyncResourceController getInstance()
  {
    return _self;
  }

  /**
   * Get the SyncTransactionHandler for a specific type of resource.
   *
   * @param resourceType The type of resource.
   * @return The SyncTransactionHandler for the resource type, or <b>null</b>
   * if resource type is not configured for synchronization.
   *
   * @since 2.0
   */
  private SyncTransactionHandler getTransactionHandler(String resourceType)
  {
    return (SyncTransactionHandler)_resSyncTable.get(resourceType);
  }

  /**
   * Get the message mapping for the specified Message id.
   *
   * @param msgId The message id
   * @return The message mapping for the message id. Only messages received will have
   * a mapping to the resource type(0) and ack msg id(1).
   */
  private String[] getMessageMap(String msgId)
  {
    return (String[])_msgTable.get(msgId);
  }

  // ************************ Send ***************************************

  /**
   * Send a SharedResource to an  enterprise.
   *
   * @param sharedRes The SharedResource to send.
   * @param destChannelUID The UID of the Channel to use to reach the targeted
   * receiving end.
   * @param routeChannelUID The UID of the Channel to use if the targeted
   * receiving end is not reachable through the specified channel. In which case,
   * The SharedResource will be routed, and the receiving end changes.
   * @exception
   * @since 2.0 I4
   */
  public void sendResource(
    SharedResource sharedRes, Long destChannelUID)
    throws SynchronizationFailException
  {
    SyncTransactionHandler trxHandler = getTransactionHandler(sharedRes.getResourceType());

    if (trxHandler == null)
      throw new SynchronizationFailException(
        "No handler to synchronize resource type: "+sharedRes.getResourceType() );

    AbstractSyncResourceHandler syncHandler = trxHandler.startTransaction();

    try
    {
      // determine the channel to use
      // --> check whether the targeted receiving end is contactable
      // --> if not, use routing if the routing channel.
      List msgIDOptions = null;
      Long useChannelUID = null;

      if (destChannelUID == null)
      {
        //direct to GM
        msgIDOptions = syncHandler.getConfiguration().getSendOnlyMsgID();
        if (msgIDOptions.size() == 0)
          throw new SynchronizationFailException("No SendOnlyMsgID configured!");
      }
      else
      {
        if (isChannelContactable(destChannelUID)) // online, direct-send
        {
          msgIDOptions = syncHandler.getConfiguration().getSendOrRouteMsgID();
          if (msgIDOptions.size() == 0)
                      throw new SynchronizationFailException("No SendOrRouteMsgID configured!");
          useChannelUID = destChannelUID;
        }
        else //offline --> route via GM
        {
          msgIDOptions = syncHandler.getConfiguration().getRouteMsgID();
                    if (msgIDOptions.size() == 0)
                      throw new SynchronizationFailException("No RouteMsgID configured!");
        }
      }

      // construct the outgoing SyncMessage
      String msgID = (String)msgIDOptions.get(CONTENT_MSGID_IDX);
      String ackMsgID = (String)msgIDOptions.get(ACK_MSGID_IDX);
Logger.debug("--- sendResource() msgID = "+msgID);
Logger.debug("--- sendResource() ackMsgID = "+ackMsgID);

      SyncMessage msg = new SyncMessage(msgID);
      msg.setAckMsgID(ackMsgID);
      msg.setResourceType(sharedRes.getResourceType());
      msg.setSharedResource(sharedRes.getUId());
      msg.setReplyTo(_thisEnterpriseID);
      msg.setSendTo(sharedRes.getToEnterpriseID());
      msg.setCheckSum(sharedRes.getSyncChecksum());

      // prepare payload for sending
      syncHandler.handleSend(msg, sharedRes.getResourceUID(), sharedRes.getToEnterpriseID());

      // set the transaction id and data for lookup later on
      msg.setTransId(syncHandler.getTransactionId());
      syncHandler.setTransactionData(msg);

      // checking send destination and send
      if (useChannelUID == null) // immediate dest is GM
      {
        msg.setSendChannel(destChannelUID); // will be null if final dest is GM, otherwise will be for GT
        if (isGridMasterOnline())
        {
           sendViaGridMaster(msg);
        }
        else
        {
          Logger.log("[SyncResourceController.sendResource] No Contactable Channel available to send via at the moment.");
        }
      }
      else // immediate dest is GT
      {
        msg.setSendChannel(useChannelUID);

        send(msg);
      }
    }
    finally
    {
      // if no transaction data set, means there's exception. so don't keep this transaction anymore
      if (syncHandler.getTransactionData() == null)
        trxHandler.endTransaction(syncHandler);
    }
  }

  /**
   * Check if the GridMaster is online.
   *
   * @return <b>true</b> if the GridMaster is online, <b>false</b> otherwise.
   */
  private boolean isGridMasterOnline()
  {
    try
    {
      return ServiceLookupHelper.getPostOffice().isGridMasterPostOfficeOpened();
    }
    catch (Exception e)
    {
      Logger.warn("[SyncResourceController.isGridMasterOnline] Error checking...", e);
      return false;
    }
  }


  /**
   * Send/Upload the synchronization content message to GridMaster.
   *
   * @param message The message to send.
   * @throws SynchronizationFailException Unable to send to GridMaster.
   */
  private void sendViaGridMaster(SyncMessage message)
    throws SynchronizationFailException
  {
    Logger.log("[SyncResourceController.sendViaGridMaster] Activated.");

    String eventId = message.getThisMsgID();
    String transId = message.getTransId();
    String sender = message.getReplyTo();
    String recipient = message.getSendTo();

    String[] params = { eventId, transId, sender, recipient };

    String fileId = FILE_ID_FORMAT.format(params);
    ChannelSendHeader header = new ChannelSendHeader(
                                   eventId, transId, null, fileId);
    header.setGridnodeHeaderInfo(sender, recipient, null);

    PostInstruction instruction = new PostInstruction();
    instruction.setDataPayload(message.getDataPayload());
    instruction.setEventID(eventId);
    instruction.setFilePayload(message.getFilePayload());
    instruction.setRecipientChannel(message.getSendChannel());
    instruction.setRecipientNodeID(recipient);
    instruction.setSenderNodeID(sender);
    instruction.setTransID(transId);
    instruction.setHeader(header);

    try
    {
      ServiceLookupHelper.getPostOffice().dropToGridMasterPostOffice(instruction);
    }
    catch (Exception e)
    {
      Logger.warn("[SyncResourceController.sendViaGridMaster] Error: "+e.getMessage());
      throw new SynchronizationFailException("Unable to send/upload to GridMaster!");
    }
  }

  /**
   * Send a synchronization content message.
   *
   * @param message The message to send.
   * @exception SynchronizationFailException Fail to send the message.
   * @since 2.0 I4
   */
  private void send(SyncMessage message)
    throws SynchronizationFailException
  {
    Logger.log("[SyncResourceController.send] Activated.");

    sendViaChannel(
      message.getSendChannel(),
      message.getDataPayload(),
      message.getFilePayload(),
      message.getThisMsgID(),
      message.getTransId(),
      message.getReplyTo(),
      message.getSendTo());
  }

  /**
   * Check if the Channel is contactable at this moment.
   * @param channelUID The UID of the ChannelInfo to check.
   * @return <b>true</b> if the Channel is contactable, or <b>false</b> otherwise or if error checking the
   * status.
   */
  private boolean isChannelContactable(Long channelUID)
  {
    boolean isContactable = false;
    if (channelUID != null)
    {
      //060104NSL: By right should ping the channel, but this is not implemented yet
      //Workaround is to go round trip to check the Gridnode's Connection status.
      try
      {
        String nodeId = getChannelRefId(channelUID);
        ConnectionStatus status = ActionHelper.getConnectionStatus(nodeId);
        isContactable = status.getStatusFlag() == ConnectionStatus.STATUS_ONLINE;
      }
      catch (Exception e)
      {
        Logger.warn("[SyncResourceController.isChannelContactable] Error checking status. Assuming not contactable", e);
      }
    }

    return isContactable;
  }

  /**
   * Get the ReferenceId of the specified Channel.
   *
   * @param channelUID The UID of the Channel.
   * @return The ReferenceId of the Channel.
   * @throws Exception If the specified Channel does not exists.
   */
  private String getChannelRefId(Long channelUID) throws Exception
  {
    ChannelInfo channel = ServiceLookupHelper.getChannelManager().getChannelInfo(channelUID);

    return channel.getReferenceId();
  }
  // ******************** Receive ********************************************

  /**
   * Handle the receiving of a SyncMessage.
   *
   * @param message The received message.
   * @exception
   * @since 2.0 I4
   */
  void receive(SyncMessage message)
    throws SynchronizationFailException
  {
    AbstractSyncResourceHandler syncHandler = SyncTransactionHandler.getSyncHandler(message.getTransId());
    if (syncHandler != null)
    {
      SyncMessage oriMsg = (SyncMessage)syncHandler.getTransactionData();

      // got the transaction back, this is an Ack msg
      if (message.getThisMsgID().equals(oriMsg.getAckMsgID()))
      {
        handleReceiveAck(message, oriMsg, syncHandler);
        return;
      }
    }

    // sync content message
    handleReceiveSync(message);

  }

  /**
   * Handle a received acknowledgement message. This should complete the synchronization cycle.
   *
   * @param receivedAck The received acknowledgement message
   * @param sentMsg The original sync content message that is sent.
   * @param syncHandler The Synchronization handler to use.
   */
  private void handleReceiveAck(SyncMessage receivedAck, SyncMessage sentMsg, AbstractSyncResourceHandler syncHandler)
  {
    Logger.log("[SyncResourceController.handleReceiveAck] Activated.");
    SyncTransactionHandler trxHandler = null;
    try
    {
      trxHandler = getTransactionHandler(sentMsg.getResourceType());

      syncHandler.handleReceiveAck(receivedAck);
      //no exception means synchronization ok.
      completeSynchronization(sentMsg.getSharedResource(), sentMsg.getCheckSum());
    }
    catch (Exception e)
    {
      Logger.error(ILogErrorCodes.GT_SYNC_RESOURCE_CONTROLLER,
                   "[SyncResourceController.handleReceiveAck]"+e.getMessage());
    }
    finally
    { // by hook or by crook, just end the transaction since the partner has sent the acknowledgement
      if (syncHandler.isInTransaction() && trxHandler != null)
        trxHandler.endTransaction(syncHandler);
    }
  }

  /**
   * Handle a received synchronization content message. An acknowledgement message should be sent back to
   * the initiating party on the status of the synchronization.
   *
   * @param receivedMsg The received synchronization content message.
   */
  private void handleReceiveSync(SyncMessage receivedMsg)
  {
    Logger.log("[SyncResourceController.handleReceiveSync] Activated.");
    SyncTransactionHandler trxHandler = null;
    AbstractSyncResourceHandler syncHandler = null;

    String[] mapping = getMessageMap(receivedMsg.getThisMsgID());
    try
    {
      trxHandler = getTransactionHandler(mapping[0]);

      syncHandler = trxHandler.startTransaction();
      syncHandler.handleReceive(receivedMsg);
    }
    catch (Exception e)
    {
      Logger.error(ILogErrorCodes.GT_SYNC_RESOURCE_CONTROLLER,
                   "[SyncResourceController.handleReceiveSync]"+e.getMessage());
    }

    //send acknowledgement
    if (syncHandler != null)
    {
      try
      {
        SyncMessage ackMsg = new SyncMessage(mapping[1]);

        syncHandler.handleSendAck(ackMsg, receivedMsg);

        ackMsg.setSendTo(receivedMsg.getReplyTo());
        ackMsg.setReplyTo(_thisEnterpriseID);
        ackMsg.setTransId(receivedMsg.getTransId());

        if (isViaGridMaster(receivedMsg))
        {
          sendViaGridMaster(ackMsg);
        }
        else
        {
          //get respond channel
          Long channelUID = getMasterChannelByRefId(receivedMsg.getReplyTo());
          ackMsg.setSendChannel(channelUID);
          sendAck(ackMsg);
        }
      }
      catch (Exception e)
      {
        Logger.error(ILogErrorCodes.GT_SYNC_RESOURCE_CONTROLLER,
                     "[SyncResourceController.handleReceiveSync]"+e.getMessage());
      }
      finally
      {
        if (syncHandler != null && syncHandler.isInTransaction())
          trxHandler.endTransaction(syncHandler);
      }
    }
  }


  /**
   * Check if the received sync content message is from the GridMaster
   *
   * @param receivedMsg The received message.
   * @return <b>true</b> if the Original Sender is present in the received data payload,
   * <b>false</b> otherwise.
   */
  private boolean isViaGridMaster(SyncMessage receivedMsg)
  {
    String[] receivedData = receivedMsg.getDataPayload();
    String oriSender = null;
    if (receivedData != null && receivedData.length>0)
      oriSender = receivedData[0];

    return oriSender != null;
  }

  /**
   * Get the Master Channel by the Reference Id.
   *
   * @param refId The Reference id.
   * @return The UID of the Master Channel found.
   * @throws Exception If no Master Channel found by the specified RefId.
   */
  private Long getMasterChannelByRefId(String refId) throws Exception
  {
    return ActionHelper.getMasterChannelUID(refId);
  }

  /**
   * Send an acknowledgement message in respond to a message received.
   *
   * @param message The acknowledgement message.
   * @exception SynchronizationFailException Fail to send the message.
   * @since 2.0 I4
   */
  private void sendAck(SyncMessage message)
    throws SynchronizationFailException
  {
    Logger.log("[SyncResourceController.sendAck] Activated.");

    sendViaChannel(
      message.getSendChannel(),
      message.getDataPayload(),
      message.getFilePayload(),
      message.getThisMsgID(),
      message.getTransId(),
      message.getReplyTo(),
      message.getSendTo());
  }

  /**
   * Send data and files via a channel.
   *
   * @param channelUID The UID of the ChannelInfo to use.
   * @param dataPayload The data payload to send
   * @param filePayload The file payload to send
   * @param eventID The Event ID (MsgID) to identify the send event.
   * @param sendTo The recipient of the message
   * @exception SynchronizationFailException Fail to send thru the specified
   * channel.
   * @since 2.0 I4
   */
  private void sendViaChannel(
    Long channelUID,
    String[] dataPayload,
    File[] filePayload,
    String eventID,
    String transID,
    String replyTo,
    String sendTo)
    throws SynchronizationFailException
  {
    if (!_isTest)
    {
      try
      {
        IChannelManagerObj mgr = ServiceLookupHelper.getChannelManager();
        ChannelInfo channel = mgr.getChannelInfo(channelUID);
        //050104NSL: Use the transId passed in
        //String transID = generateTransID();

        //060104NSL: standard format for FileID
        String fileID = FILE_ID_FORMAT.format(new String[]{eventID, transID, replyTo, sendTo});
        ChannelSendHeader header = new ChannelSendHeader(
                                       eventID, transID, null, fileID);
        header.setGridnodeHeaderInfo(replyTo, sendTo, null);

        mgr.send(channel, dataPayload, filePayload, header.getHeaderArray());
      }
      catch (Throwable t)
      {
        Logger.warn("[SyncResourceController.sendViaChannel] "+
          "Error sending message via channel!", t);
        throw new SynchronizationFailException(
          "Fail to send message thru designated Channel");
      }
    }
    else
      Logger.debug("[SyncResourceController.sendViaChannel] "+
        "Testing mode. No real sending initiated.");
  }

  /*050104NSL: Don't generate here
  private String generateTransID()
  {
    int transId = _randGen.nextInt();
    if (transId == Integer.MIN_VALUE)
      return "0";
    return String.valueOf(Math.abs(transId));
  }
  */
  /**
   * Complete the synchronization of a SharedResource ==> the SharedResource
   * is synchronized.
   *
   * @param sharedResourceUID the UID of the SharedResource.
   * @param checkSum The synchronization checksum of the data that has completed synchronization.
   *
   * @since 2.0 I4
   */
  private void completeSynchronization(long sharedResourceUID, long checkSum)
  {
    try
    {
      ServiceLookupHelper.getSharedResourceMgr().completeSynchronization(
        new Long(sharedResourceUID), checkSum);
    }
    catch (Throwable t)
    {
      Logger.warn("[SyncResourceController.completeSynchronization] " +
        "Unable to complete synchronization for SharedResource", t);
    }
  }

  // ******************* Initialization Configurations ***************************************

  /**
   * Initialize the configurations for SharedResource synchronization.
   *
   * @since 2.0 I4
   */
  private void initConfigurations()
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(
                             CONFIG_NAME);

    _isTest = config.getBoolean(IS_TEST);
    _serDir = getSerializeDir();

    List resTypes = config.getList(RESOURCE_TYPES, ",");

    for (Iterator i=resTypes.iterator(); i.hasNext(); )
    {
      String type = (String)i.next();
      String handlerClass = config.getString(SYNC_HANDLER+type, null);
      if (handlerClass != null && handlerClass.trim().length()>0)
      {
        try
        {
          SyncTransactionHandler trxHandler = new SyncTransactionHandler();
          _resSyncTable.put(type, trxHandler);

          configureSyncHandler(trxHandler, type, handlerClass, config);
        }
        catch (Exception ex)
        {
          Logger.warn("[SyncResourceController.initConfigurations] " +
            "Unable to configure synchronization handler for resource type "+
            type, ex);
        }
      }
      else
      {
        Logger.warn("[SyncResourceController.initConfigurations] "+
          "No synchronization handler set for resource type "+ type);
      }
    }

    _thisEnterpriseID = getThisEnterpriseID();
  }

  /**
   * Configure the synchronization handler for a resource type.
   *
   * @param trxHandler The SyncTransactionHandler for the resource type.
   * @param resType The resource type.
   * @param syncHandlerClass The class name of the synchronization handler.
   * @param config The configuration properties.
   * @exception
   * @since 2.0 I4
   */
  private void configureSyncHandler(SyncTransactionHandler trxHandler,
    String resType, String syncHandlerClass, Configuration config) throws Exception
  {
    List sendOrRouteMsgID = getMsgIDOptions(SEND_OR_ROUTE_MSGID+resType, config, 4);
    List sendOnlyMsgID    = getMsgIDOptions(SEND_ONLY_MSGID+resType, config, 4);
    List recOnlyMsgID     = getMsgIDOptions(RECEIVE_ONLY_MSGID+resType, config, 2);
    List routeMsgID       = getMsgIDOptions(ROUTE_MSGID+resType, config, 2);

    configureSendOrRouteMsgID(sendOrRouteMsgID, resType);
    configureSendOnlyMsgID(sendOnlyMsgID, resType);
    configureReceiveOnlyMsgID(recOnlyMsgID, resType);
    configureRouteMsgID(routeMsgID, resType);

    trxHandler.setSyncResourceHandler(syncHandlerClass, resType,
      sendOrRouteMsgID, sendOnlyMsgID, routeMsgID, recOnlyMsgID);
    trxHandler.setTest(_isTest);
    trxHandler.setSerializeDir(_serDir);

  }

  /**
   * Get a type of MsgID options.
   *
   * @param key The type of MsgID property key.
   * @return config The configuration properties to obtain the options.
   * @parame expectedOptions The expected number of options available for
   * the type of MsgID.
   * @exception If the number of options does not tally with the expectedOptions,
   * if any options is specified in the configuration properties.
   *
   * @since 2.0 I4
   */
  private List getMsgIDOptions(String key, Configuration config, int expectedOptions)
    throws Exception
  {
    List options = config.getList(key, ",");
    if (options.size() != expectedOptions && options.size() != 0)
      throw new Exception("Invalid configuration for ["+key+"]="+options);

    return options;
  }

  /**
   * Configure the MsgID for outgoing content messages that may be routed by
   * a third party.
   *
   * @param options The options for the MsgID:
   * <li>contentMsgID (send is allowed for content messages of this contentMsgID type),
   * <li>ackMsgID (receive is allowed for acknowledge messages of this ackMsgID type),
   * <li>destType (type of receiving end of the content message),
   * <li>receiveOption (true to allow receiving messages of the contentMsgID type).
   *
   * @since 2.0 I4
   */
  private void configureSendOrRouteMsgID(List options, String resType)
  {
    String contentMsgID = (String)options.get(CONTENT_MSGID_IDX);
    String ackMsgID  = (String)options.get(ACK_MSGID_IDX);
    //String destType  = (String)options.get(DEST_TYPE_IDX);
    Boolean recOption= new Boolean((String)options.get(REC_OPTION_IDX));

    //register IReceiveMessageHandler for mainMsgID and ackMsgID
    if (recOption.booleanValue())
    {
      //also can handle messages received with the msg id.
      _msgTable.put(contentMsgID, new String[]{resType, ackMsgID});
      //registerReceiveMessageEventID(contentMsgID);
    }

    //registerReceiveMessageEventID(ackMsgID, resType);
  }

  /**
   * Configure the MsgID for outgoing content messages that may not be routed by
   * a third party.
   *
   * @param options The options for the MsgID.
   * <li>contentMsgID (send is allowed for content messages of this contentMsgID type),
   * <li>ackMsgID (receive is allowed for acknowledge messages of this ackMsgID type),
   * <li>destType (type of receiving end of the content message),
   * <li>receiveOption (true to allow receiving messages of the contentMsgID type).
   *
   * @since 2.0 I4
   */
  private void configureSendOnlyMsgID(List options, String resType)
  {
    /*
    String contentMsgID = (String)options.get(CONTENT_MSGID_IDX);
    String ackMsgID  = (String)options.get(ACK_MSGID_IDX);
    //String destType  = (String)options.get(DEST_TYPE_IDX);
    Boolean recOption= new Boolean((String)options.get(REC_OPTION_IDX));

    //register IReceiveMessageHandler for mainMsgID and ackMsgID
    if (recOption.booleanValue())
    {
      //also can handle messages received with the msg id.
      _msgTable.put(contentMsgID, new String[]{resType, ackMsgID});
      //registerReceiveMessageEventID(contentMsgID);
    }

    //registerReceiveMessageEventID(ackMsgID);
    */
  }

  /**
   * Configure the MsgID for outgoing content messages that are routed by
   * a third party.
   *
   * @param options The options for the MsgID.
   * <li>contentMsgID (send is allowed for content messages of this contentMsgID type),
   * <li>ackMsgID (receive is allowed for acknowledge messages of this ackMsgID type).
   *
   * @since 2.0 I4
   */
  private void configureRouteMsgID(List options, String resType)
  {
    /*
    String contentMsgID = (String)options.get(CONTENT_MSGID_IDX);
    String ackMsgID  = (String)options.get(ACK_MSGID_IDX);

    //register IReceiveMessageHandler for ackMsgID
    registerReceiveMessageEventID(ackMsgID);
    */
  }

  /**
   * Configure the MsgID for incoming (only) content messages, i.e. no
   * outgoing content messages of the same contentMsgID.
   *
   * @param options The options for the MsgID.
   * <li>contentMsgID (receive is allowed for content messages of this contentMsgID type),
   * <li>ackMsgID (send is allowed for acknowledge message of this ackMsgID type).
   *
   * @since 2.0 I4
   */
  private void configureReceiveOnlyMsgID(List options, String resType)
  {
    String contentMsgID = (String)options.get(CONTENT_MSGID_IDX);
    String ackMsgID  = (String)options.get(ACK_MSGID_IDX);

    //register IReceiveMessageHandler for mainMsgID
    //registerReceiveMessageEventID(contentMsgID);
    _msgTable.put(contentMsgID, new String[]{resType, ackMsgID});
  }

  /**
   * @todo register with Channel module
   */
//  private void registerReceiveMessageEventID(String eventID)
//  {
//
//  }

  /**
   * Get ID for this Enterprise. This ID corresponds to the GridNode ID
   * of the GridNode setup for this GridTalk during product registration.
   */
  private String getThisEnterpriseID()
  {
    String enterpriseID = getMyGridNodeID();

    // if gridnode not setup, get it from configuration file.
    if (enterpriseID == null)
    {
      Configuration config = ConfigurationManager.getInstance().getConfig(
                               IGtasConfig.CONFIG_NAME);
      enterpriseID = config.getString(IGtasConfig.ENTERPRISE_ID);
    }

    return enterpriseID;
  }

  private String getMyGridNodeID()
  {
    String nodeID = null;
    try
    {
      nodeID = ServiceLookupHelper.getGridNodeManager().findMyGridNode().getID();
    }
    catch (Throwable t)
    {
      Logger.warn("[SyncResourceController.getMyGridNodeID]", t);
    }
    return nodeID;
  }

  private String getSerializeDir()
  {
    try
    {
      if (!FileUtil.exist(IPathConfig.PATH_TEMP, ""))
      {
        String path = FileUtil.getPath(IPathConfig.PATH_TEMP);
        FileAccess fA = new FileAccess(FileUtil.getDomain());
        fA.createFolder(path);
      }
      return FileUtil.getFile(IPathConfig.PATH_TEMP, "").getAbsolutePath();
    }
    catch (Exception ex)
    {
      Logger.warn("[SyncResourceController.getSerializeDir]", ex);
      return "";
    }
  }

}