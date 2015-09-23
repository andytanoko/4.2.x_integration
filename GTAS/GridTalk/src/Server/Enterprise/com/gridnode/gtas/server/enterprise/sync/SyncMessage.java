/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncMessage.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 21 2002    Neo Sok Lay         Created
 * Nov 25 2002    Neo Sok Lay         Add SendTo.
 * Jan 05 2004    Neo Sok Lay         Remove setReceivedData() and getSendData().
 *                                    Add transId, checkSum.
 */
package com.gridnode.gtas.server.enterprise.sync;

import java.io.Serializable;
import java.io.File;

/**
 * This is a wrapper for the data and file payloads received/sent during
 * synchronization. There are two types of messages:<p>
 * <ol>
 * <li>Synchronization content message</li>
 * <li>Acknowledgement receipt message for Synchronization content message</li>
 * </ol>
 * <p>
 * For the content message, the following attributes must be set:<p>
 * <ul>
 * <li>Resource Type</li>
 * <li>Ack Msg ID</li>     (for sending the receipt message in response)
 * <li>Shared Resource</li>
 * <li>Channel To Use</li> (for outgoing message)
 * <li>Reply To</li>
 * <li>Send To</li>
 * <li>CheckSum</li>
 * <li>Trans Id</li>
 * </ul>
 * <p>
 * For receipt message, the following attributes must be set:<p>
 * <ul>
 * <li>Channel To Use</li> (for outgoing message)
 * <li>Trans Id</li> (corresponding to the Trans id of the content message)
 * <li>Reply To</li> (the sender of the receipt message)
 * <li>Send To</li> (the recipient of the receipt message)
 * </ul>
 *
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class SyncMessage implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7790173154052876261L;
	private String[] _dataPayload;
  private File[]   _filePayload;
  private String   _resType;
  private String   _ackMsgID;
  private long     _sharedResUID;
  private Long     _channelToUse;
  private String   _thisMsgID;
  private String   _replyTo;
  private String   _sendTo;
  private String   _transId;
  private long    _checkSum;

  /**
   * Constructs a SyncMessage object
   *
   * @param thisMsgID The MsgID for this message. This is for identifying
   * the type of synchronization message being sent/received.
   */
  public SyncMessage(String thisMsgID)
  {
    _thisMsgID = thisMsgID;
  }

  /**
   * Get the MsgID for this message.
   *
   * @return the MsgID for this message.
   *
   * @since 2.0 I4
   */
  public String getThisMsgID()
  {
    return _thisMsgID;
  }

  /**
   * Set the data payload for this message.
   *
   * @param dataPayload The data payload of the message. This contains only
   * the business logic specific data that needs to be sent across to the
   * receiving party. For example, for receipt message, the data payload may
   * contain a status indicating whether the synchronization message was processed
   * successfully.
   *
   * @since 2.0 I4
   */
  public void setDataPayload(String[] dataPayload)
  {
    _dataPayload = dataPayload;
  }

  /**
   * Get the data payload for this message.
   *
   * @return The data payload for this message.
   *
   * @since 2.0 I4
   */
  public String[] getDataPayload()
  {
    return _dataPayload;
  }

  /**
   * Set the file payload for this message.
   *
   * @param filePayload The file payload of the message. This contains only
   * the business logic specific files that needs to be sent across to the
   * receiving party. For example, for content message, the file payload may
   * contain the business data serialized into one or more files.
   *
   * @since 2.0 I4
   */
  public void setFilePayload(File[] filePayload)
  {
    _filePayload = filePayload;
  }

  /**
   * Get the file payload for this message.
   *
   * @return The file payload of the message.
   *
   * @since 2.0
   */
  public File[] getFilePayload()
  {
    return _filePayload;
  }

  /**
   * Set the MsgID for the acknowledgement receipt message for this message.
   * This is only required if the message is a content message.
   *
   * @param ackMsgID MsgID for the acknowledgement receipt message for this message.
   *
   * @since 2.0 I4
   */
  public void setAckMsgID(String ackMsgID)
  {
    _ackMsgID = ackMsgID;
  }

  /**
   * Get the MsgID for the acknowledgement receipt message for this message.
   *
   * @return MsgID for the acknowledgement receipt message for this message, or
   * <b>null</b> if not applicable.
   *
   * @since 2.0 I4
   */
  public String getAckMsgID()
  {
    return _ackMsgID;
  }

  /**
   * Checks if this message is a receipt message. A message is a receipt message
   * if no acknowlegement receipt is required, i.e. Ack Msg ID is not set.
   *
   * @return <b>true</b> if this message is a receipt message, <b>false</b>
   * otherwise.
   *
   * @since 2.0 I4
   */
  public boolean isAck()
  {
    return _ackMsgID == null;
  }

  /**
   * Set the Channel to be used to send the message across to the receiving
   * party. Applicable only for outgoing messages.
   *
   * @param channelUID The UID of the ChannelInfo object to use.
   *
   * @since 2.0 I4
   */
  public void setSendChannel(Long channelUID)
  {
    _channelToUse = channelUID;
  }

  /**
   * Get the Channel to be used to send the message across to the receiving
   * party.
   *
   * @return The UID of the ChannelInfo object to use, or <b>null</b> if
   * the message is not an outgoing message.
   *
   * @since 2.0 I4
   */
  public Long getSendChannel()
  {
    return _channelToUse;
  }

  /**
   * Set the SharedResource that is under synchronization.
   *
   * @param sharedResUID The UID of the SharedResource under synchronization.
   *
   * @since 2.0 I4
   */
  public void setSharedResource(long sharedResUID)
  {
    _sharedResUID = sharedResUID;
  }

  /**
   * Get the SharedResource that is under synchronization.
   *
   * @return The UID of the SharedResource under synchronization.
   *
   * @since 2.0 I4
   */
  public long getSharedResource()
  {
    return _sharedResUID;
  }

  /**
   * Set the type of resource that is under synchronization. This is used for
   * determining what synchronization handler to use at the receiving end.
   *
   * @param resourceType The type of resource under synchronization.
   *
   * @since 2.0 I4
   */
  public void setResourceType(String resourceType)
  {
    _resType = resourceType;
  }

  /**
   * Get the type of resource that is under synchronization.
   *
   * @return The type of resource that is under synchronization.
   *
   * @since 2.0 I4
   */
  public String getResourceType()
  {
    return _resType;
  }

  /**
   * Set the receiving party if there is any acknowledgement receipt message
   * need to be send back in respond to this message.
   *
   * @param replyTo The receiving party of acknowledgement receipt to this
   * message, if any is required.
   *
   * @since 2.0 I4
   */
  public void setReplyTo(String replyTo)
  {
    _replyTo = replyTo;
  }

  /**
   * Get the receiving party if there is any acknowledgement receipt message
   * need to be send back in respond to this message.
   *
   * @return The receiving party of acknowledgement receipt to this
   * message, if any is required, or <b>null</b> if no acknowledgement is
   * required.
   *
   * @since 2.0 I4
   */
  public String getReplyTo()
  {
    return _replyTo;
  }

  /**
   * Set the receiving party for the message.
   *
   * @param sendTo The receiving party of the message.
   *
   * @since 2.0 I7
   */
  public void setSendTo(String sendTo)
  {
    _sendTo = sendTo;
  }

  /**
   * Get the receiving party for the message.
   *
   * @return the receiving party of the message.
   *
   * @since 2.0 I7
   */
  public String getSendTo()
  {
    return _sendTo;
  }

  /**
   * Get the formatted data payload for outgoing message. The Resource Type,
   * Ack Msg ID, Shared Resource, and Reply To will be included into the payload.
   * Note that no modification will be done to the original data payload.
   *
   * @return The formatted data payload for outgoing message.
   *
   * @since 2.0 I4.
   */
  /*050104NSL
  public String[] getSendData()
  {
    int payloadLength = getDataPayloadLength();

    String[] sendData = new String[payloadLength+4];
    if (payloadLength > 0)
      System.arraycopy(_dataPayload, 0, sendData, 0, payloadLength);

    sendData[payloadLength] = _resType;
    sendData[payloadLength+1] = _ackMsgID;
    sendData[payloadLength+2] = String.valueOf(_sharedResUID);
    sendData[payloadLength+3] = _replyTo;
    return sendData;
  }
  */
  /**
   * Get the length of the data payload (unformatted).
   *
   * @return The length of the unformatted data payload.
   *
   * @since 2.0 I4
   */
  /*050104NSL
  private int getDataPayloadLength()
  {
    return _dataPayload==null? 0 : _dataPayload.length;
  }
  */
  /**
   * Set the data payload for incoming message. This unformats the received data
   * into: Reply To, Shared Resource, Ack Msg ID, Resource Type, and the
   * original data payload.
   *
   * @param receivedData Formatted data payload for incoming message.
   *
   * @since 2.0 I4
   */
  /*050104NSL
  public void setReceivedData(String[] receivedData)
  {
    //TODO check received data length,
    _replyTo = receivedData[receivedData.length-1];
    _sharedResUID = Integer.parseInt(receivedData[receivedData.length-2]);
    _ackMsgID = receivedData[receivedData.length-3];
    _resType = receivedData[receivedData.length-4];

    _dataPayload = new String[receivedData.length-4];
    System.arraycopy(receivedData, 0, _dataPayload, 0, _dataPayload.length);
  }
  */
  /**
   * Get the transaction id
   * 
   * @return The transaction id
   */
  public String getTransId()
  {
    return _transId;
  }
  
  /**
   * Set the transaction id
   * 
   * @param transId The transaction id to set.
   */
  public void setTransId(String transId)
  {
    _transId = transId;
  }
  
  /**
   * Get the synchronization check sum.
   * 
   * @return The synchronization checksum set earlier, or 0 if not set.
   */
  public long getCheckSum()
  {
    return _checkSum;
  }
  
  /**
   * Sets the synchronization checksum
   * 
   * @param checkSum The synchronization checksum to set.
   */
  public void setCheckSum(long checkSum)
  {
    _checkSum = checkSum;
  }
}