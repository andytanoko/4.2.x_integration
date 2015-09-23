/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelReceiveHeader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 01 2003    Neo Sok Lay         Created
 * Jun 07 2004    Neo Sok Lay         Modified: getMessageHeaders() should not
 *                                    modify the headers Map passed in. It should
 *                                    construct a new Map then make the modification
 *                                    on the new Map. Should check against the field
 *                                    values in ICommonHeaders instead of field names.
 * Mar 22 2006    Neo Sok Lay         Add SENDER_PARTNER, SENDER_BEID, SENDER_DUNS,
 *                                    RECIPIENT_BEID, RECIPIENT_DUNS.      
 * Oct 23 2006    Tam Wei Xiang       Added TracingID for OnlineTracking
 * Sep 30 2010    Tam Wei Xiang       #1897 - Added connection type                                                                
 */
package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.framework.messaging.ICommonHeaders;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a wrapper around the header information that is returned by the
 * ChannelManager when receiving messages (via the ReceiveMessageHandler).
 *
 * @author Neo Sok Lay
 * @since GT 4.0
 */
public class ChannelReceiveHeader implements java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2336539475394736251L;

	public static int HEADER_SIZE = 21;

  // general header info
  public static int EVENT_ID_POS = 0;
  public static int TRANSACTION_ID_POS = 1;
  public static int EVENT_SUB_ID_POS = 4;
  public static int PROCESS_ID_POS = 5;
  public static int FILE_ID_POS = 6;
  public static int CHANNEL_NAME_POS = 7;

  // gridnode header info
  public static int RECIPIENT_NODEID_POS = 2;
  public static int SENDER_NODEID_POS = 3;

  // message header info
  public static int PACKAGING_ENVELOPE_POS = 8;
  public static int DOCUMENT_TYPE_POS = 13;

  // registry header info
  public static int SENDER_UUID_POS = 9;
  public static int SENDER_REGISTRY_QUERY_URL = 10;
  public static int RECIPIENT_UUID_POS = 11;
  public static int RECIPIENT_REGISTRY_QUERY_URL = 12;
  public static int SENDER_BEID_POS = 14;
  public static int SENDER_DUNS_POS = 15;
  public static int RECIPIENT_BEID_POS = 16;
  public static int RECIPIENT_DUNS_POS = 17;
  public static int SENDER_PARTNER_POS = 18;
  
  //OnlineTracking info
  public static int TRACING_ID = 19;
  
  //Connection type info
  public static int INCOMING_CONNECTION_TYPE = 20;
  
  private String[] _array;

  /**
   * Constructs a ChannelSendHeader with the general header information specified.
   *
   * @param eventId EventId of the message
   * @param transactionId TransactionId of the message to send
   * @param eventSubId Event sub id of the message, if any. <b>null</b> if none.
   * @param fileId FileId for the file payloads, if any. <b>null</b> if none.
   * @param processId ProcessId for file-splitting, if any. <b>null</b> if none.
   * @param channelName Name of the Channel to use for handling the receive, if any. <b>null</b> if none.
   */
  public ChannelReceiveHeader(
    String eventId,
    String transactionId,
    String eventSubId,
    String fileId,
    String processId,
    String channelName)
  {
    _array = new String[HEADER_SIZE];
    _array[EVENT_ID_POS] = eventId;
    _array[EVENT_SUB_ID_POS] = eventSubId;
    _array[TRANSACTION_ID_POS] = transactionId;
    _array[FILE_ID_POS] = fileId;
    _array[PROCESS_ID_POS] = processId;
    _array[CHANNEL_NAME_POS] = channelName;
  }

  /**
   * Sets the Gridnode header information
   *
   * @param senderNodeId GridnodeId of the sender GridTalk/GridMaster.
   * @param recipientNodeId GridnodeId of the recipient GridTalk/GridMaster.
   */
  public void setGridnodeHeaderInfo(
    String senderNodeId,
    String recipientNodeId)
  {
    _array[SENDER_NODEID_POS] = senderNodeId;
    _array[RECIPIENT_NODEID_POS] = recipientNodeId;
  }

  /**
   * Sets the Registry header information
   *
   * @param senderBeId ID of the sender business entity
   * @param senderDuns DUNS of the sender business entity
   * @param senderUuid UUID of the sender business entity.
   * @param senderRegistryQueryUrl Query URL of the registry that discovers the sender business entity.
   * @param recipientBeId ID of the recipient business entity
   * @param recipientDuns ID of the recipient business entity
   * @param recipientUuid UUID of the recipient business entity.
   * @param recipientRegistryQueryUrl Query URL of the registry that discovers the  recipient business entity.
   */
  public void setRegistryHeaderInfo(
    String senderBeId,
    String senderDuns,
    String senderUuid,
    String senderRegistryQueryUrl,
    String recipientBeId,
    String recipientDuns,
    String recipientUuid,
    String recipientRegistryQueryUrl)
  {
  	_array[SENDER_BEID_POS] = senderBeId;
  	_array[SENDER_DUNS_POS] = senderDuns;
    _array[SENDER_UUID_POS] = senderUuid;
    _array[SENDER_REGISTRY_QUERY_URL] = senderRegistryQueryUrl;
    _array[RECIPIENT_BEID_POS] = recipientBeId;
    _array[RECIPIENT_DUNS_POS] = recipientDuns;
    _array[RECIPIENT_UUID_POS] = recipientUuid;
    _array[RECIPIENT_REGISTRY_QUERY_URL] = recipientRegistryQueryUrl;
  }

  public void setPartnerHeaderInfo(String senderPartnerId)
  {
  	_array[SENDER_PARTNER_POS] = senderPartnerId;
  }
  
  /**
   * Sets the message header information
   *
   * @param documentType Document Type of the user document to send.
   * @param packagingEnvelopeType Packaging envelope type for the user document file.
   */
  public void setMessageHeaderInfo(
    String documentType,
    String packagingEnvelopeType)
  {
    _array[DOCUMENT_TYPE_POS] = documentType;
    _array[PACKAGING_ENVELOPE_POS] = packagingEnvelopeType;
  }
  
  public void setOnlineTrackingHeaderInfo(String tracingID)
  {
    _array[TRACING_ID] = tracingID;
  }
  
  public void setIncomingConnectionType(String connectionType)
  {
    _array[INCOMING_CONNECTION_TYPE] = connectionType;
  }
  
  /**
   * Gets all the header information in a String array.
   *
   * @return Array of String(s) containing the header information in order as specified by the
   * position constants for each piece of header info.
   */
  public String[] getHeaderArray()
  {
    return _array;
  }

  public static Map getCommonHeaders(Map header)
  {
    Map commonHeaders = new HashMap();
    Field[] fields = ICommonHeaders.class.getFields();
    for (int i = 0; i < fields.length; i++)
    {
      try
      {
        Object headerKey = fields[i].get(null);
        commonHeaders.put(headerKey, header.get(headerKey));
      }
      catch (IllegalAccessException ex)
      {
        System.err.print(
          "[Argument]" + fields[i].getName() + "] was inaccessible");
      }
    }
    return commonHeaders;
  }

  public static Map getMessageHeaders(Map header)
  {
    /*040607NSL -- this will modify the original header!!!! should not use getName()!!!
    Field[] fields = ICommonHeaders.class.getFields();
    for (int i = 0; i < fields.length; i++)
    {
      String headerKey = fields[i].getName();
      if (header.containsKey(headerKey))
        header.remove(headerKey);
    }
    return new HashMap(header);
    */
    
    HashMap map = new HashMap(header);
    Field[] fields = ICommonHeaders.class.getFields();
    for (int i = 0; i < fields.length; i++)
    {
      try
      {
        Object headerKey = fields[i].get(null);
        if (map.containsKey(headerKey))
          map.remove(headerKey);
      }
      catch (Exception ex)
      {
        System.err.print(
          "[Argument]" + fields[i].getName() + "] was inaccessible");
      }
    }
    return map; 
  }
}
