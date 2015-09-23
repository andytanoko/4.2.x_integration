/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelSendHeader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 01 2003    Neo Sok Lay         Created
 * Dec 23 2003    Guo Jianyu          Added AS2 headers
 * Aug 30 2006    Neo Sok Lay         Add PAYLOAD_FILE_NAME
 * Oct 20 2006    Tam Wei Xiang       Added TracingID (for OnlineTracking)
 *                                    Modified perpareCommonHeader(...) to include
 *                                    tracingID
 */
package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.IAS2Headers;
import com.gridnode.pdip.framework.messaging.IRNHeaderConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a wrapper around the header information that is needed when sending messages
 * through the ChannelManager.
 *
 * @author Neo Sok Lay
 * @since GT 4.0
 */
public class ChannelSendHeader implements java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -887976778367039446L;
	public static int HEADER_SIZE = 26;
  public static int COMMON_HEADER_SIZE = 14;

  // general header info
  public static int EVENT_ID_POS = 0;
  public static int TRANSACTION_ID_POS = 1;
  public static int EVENT_SUB_ID_POS = 2;
  public static int FILE_ID_POS = 3;

  // gridnode header info
  public static int SENDER_NODEID_POS = 4;
  public static int RECIPIENT_NODEID_POS = 5;
  public static int GNCI_POS = 6;

  // message header info
  public static int PACKAGING_ENVELOPE_POS = 7;
  public static int DOCUMENT_TYPE_POS = 12;

  // registry header info
  public static int SENDER_UUID_POS = 8;
  public static int SENDER_REGISTRY_QUERY_URL = 9;
  public static int RECIPIENT_UUID_POS = 10;
  public static int RECIPIENT_REGISTRY_QUERY_URL = 11;
  
  //OnlineTracking Header Info
  public static int TRACING_ID = 13;
  
  // AS2 headers
  public static int MESSAGE_ID = 14;
  public static int AS2_FROM = 15;
  public static int AS2_TO = 16;
  public static int IS_ACK_REQ = 17;
  public static int IS_ACK_SIGNED = 18;
  public static int IS_ACK_SYNC = 19;
  public static int RETURN_URL = 20;
  public static int CONTENT_TYPE = 21;
  public static int SUBJECT = 22;
  public static int MIC = 23;
  public static int AUDIT_FILE_NAME = 24;  //used for RNIF also
  public static int PAYLOAD_FILE_NAME = 25;
  
  
  
  private String[] _array;

  /**
   * Constructs a ChannelSendHeader with the general header information specified.
   *
   * @param eventId EventId of the message
   * @param transactionId TransactionId of the message to send
   * @param eventSubId Event sub id of the message, if any. <b>null</b> if none.
   * @param fileId FileId for the file payloads, if any. <b>null</b> if none.
   */
  public ChannelSendHeader(
    String eventId,
    String transactionId,
    String eventSubId,
    String fileId)
  {
    _array = new String[HEADER_SIZE];
    _array[EVENT_ID_POS] = eventId;
    _array[EVENT_SUB_ID_POS] = eventSubId;
    _array[TRANSACTION_ID_POS] = transactionId;
    _array[FILE_ID_POS] = fileId;
  }

  /**
   * Sets the Gridnode header information. Applicable if both ends are GridNodes.
   *
   * @param senderNodeId GridnodeId of the sender GridTalk/GridMaster.
   * @param recipientNodeId GridnodeId of the recipient GridTalk/GridMaster.
   * @param gnci GridNode proprietary communication information string of sender.
   */
  public void setGridnodeHeaderInfo(
    String senderNodeId,
    String recipientNodeId,
    String gnci)
  {
    _array[SENDER_NODEID_POS] = senderNodeId;
    _array[RECIPIENT_NODEID_POS] = recipientNodeId;
    _array[GNCI_POS] = gnci;
  }

  /**
   * Sets the Registry header information. Applicable if the transport protocol is SOAP-HTTP.
   *
   * @param senderUuid UUID of the sender business entity.
   * @param senderRegistryQueryUrl Query URL of the registry that discovers the sender business entity.
   * @param recipientUuid UUID of the recipient business entity.
   * @param recipientRegistryQueryUrl Query URL of the registry that discovers the  recipient business entity.
   */
  public void setRegistryHeaderInfo(
    String senderUuid,
    String senderRegistryQueryUrl,
    String recipientUuid,
    String recipientRegistryQueryUrl)
  {
    _array[SENDER_UUID_POS] = senderUuid;
    _array[SENDER_REGISTRY_QUERY_URL] = senderRegistryQueryUrl;
    _array[RECIPIENT_UUID_POS] = recipientUuid;
    _array[RECIPIENT_REGISTRY_QUERY_URL] = recipientRegistryQueryUrl;
  }

  /**
   * Sets the message header information. Applicable for user document file payloads.
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

  /**
   * Sets the AS2 specific headers.
   * @param msgID Message ID of the outgoing AS2 message
   * @param AS2_From The sender's AS2 identifier
   * @param AS2_To The receiver's AS2 identifier
   * @param isAckReq Whether the ack is required
   * @param isAckSigned Whether the ack should be signed
   * @param isAckSync Whether the ack should be sent in synchronous mode
   * @param returnURL The URL to which the ack should be sent (applicable only when isAckSync is false)
   * @param contentType the MIME content type of the outgoing message
   * @param subject Subject in MIME header
   * @param digest Digest information
   * @param auditFilename Filename of the audit file
   * @param payloadFilename Filename of the original payload
   */
  public void setAS2Headers(String msgID, String AS2_From, String AS2_To, String isAckReq,
    String isAckSigned, String isAckSync, String returnURL, String contentType,
    String subject, String digest, String auditFileName, String payloadFilename)
  {
    _array[MESSAGE_ID] = msgID;
    _array[AS2_FROM] = AS2_From;
    _array[AS2_TO] = AS2_To;
    _array[IS_ACK_REQ] = isAckReq;
    _array[IS_ACK_SIGNED] = isAckSigned;
    _array[IS_ACK_SYNC] = isAckSync;
    _array[RETURN_URL] = returnURL;
    _array[CONTENT_TYPE] = contentType;
    _array[SUBJECT] = subject;
    _array[MIC] = digest;
    _array[AUDIT_FILE_NAME] = auditFileName;
    _array[PAYLOAD_FILE_NAME] = payloadFilename; //NSL20060830
  }

  /**
   * Currently only auditFileName is set.
   * TODO to add all other heaers
   * @param auditFileName
   */
  public void setRNHeaders(String auditFileName)
  {
    _array[AUDIT_FILE_NAME] = auditFileName;
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

  public static Map getAS2Headers(String[] AS2Headers)
  {
    Map headers = new HashMap();
    try
    {
      if (AS2Headers[AS2_FROM] != null)
      {
        headers.put(IAS2Headers.MESSAGE_ID, AS2Headers[MESSAGE_ID]);
        headers.put(IAS2Headers.AS2_FROM, AS2Headers[AS2_FROM]);
        headers.put(IAS2Headers.AS2_TO, AS2Headers[AS2_TO]);
        headers.put(IAS2Headers.IS_ACK_REQ, AS2Headers[IS_ACK_REQ]);
        headers.put(IAS2Headers.IS_ACK_SIGNED, AS2Headers[IS_ACK_SIGNED]);
        headers.put(IAS2Headers.IS_ACK_SYNC, AS2Headers[IS_ACK_SYNC]);
        headers.put(IAS2Headers.RETURN_URL, AS2Headers[RETURN_URL]);
        headers.put(IAS2Headers.CONTENT_TYPE, AS2Headers[CONTENT_TYPE]);
        headers.put(IAS2Headers.SUBJECT, AS2Headers[SUBJECT]);
        headers.put(IAS2Headers.MIC, AS2Headers[MIC]);
        headers.put(IAS2Headers.AUDIT_FILE_NAME, AS2Headers[AUDIT_FILE_NAME]);
        headers.put(IAS2Headers.PAYLOAD_FILENAME, AS2Headers[PAYLOAD_FILE_NAME]); //NSL20060830
      }

      return headers;
    }
    catch (ArrayIndexOutOfBoundsException ex)
    {
      ChannelLogger.warnLog(
        "ChannelSendHeader",
        "getAS2Header()",
        "[Header Array Accessed with Invalid Index or Index dose not exists]",
        ex);
      return headers;
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        "ChannelSendHeader",
        "getAS2Header()",
        "[Header Array Accessed with Invalid Index or Index dose not exists]",
        ex);
      return headers;
    }
  }

  public static Map getCommonHeaders(String[] blheader)
  {
    if (blheader == null || blheader.length == 0)
      return Collections.EMPTY_MAP;
    else
      return perpareCommonHeader(blheader);
  }

  public static String[] getCommonHeaders(Map commonHeaders)
  {
    ChannelSendHeader header =
      new ChannelSendHeader(
        (String) commonHeaders.get(ICommonHeaders.MSG_EVENT_ID),
        (String) commonHeaders.get(ICommonHeaders.MSG_TRANSACTION_ID),
        (String) commonHeaders.get(ICommonHeaders.MSG_EVENT_SUB_ID),
        (String) commonHeaders.get(ICommonHeaders.PAYLOAD_ID));

    header.setGridnodeHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.SENDER_BE_GNID),
      (String) commonHeaders.get(ICommonHeaders.RECIPENT_BE_GNID),
      (String) commonHeaders.get(ICommonHeaders.COMM_GNCI));

    header.setRegistryHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.SENDER_BE_UUID),
      (String) commonHeaders.get(ICommonHeaders.SENDER_BE_UDDI_URL),
      (String) commonHeaders.get(ICommonHeaders.RECIPENT_BE_UUID),
      (String) commonHeaders.get(ICommonHeaders.RECIPENT_BE_UDDI_URL));

    header.setMessageHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.PAYLOAD_GROUP),
      (String) commonHeaders.get(ICommonHeaders.PAYLOAD_TYPE));
    
    //TWX add tracingID into common header
    header.setOnlineTrackingHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.TRACING_ID));

    String[] array = header.getHeaderArray();
    String[] returnArray = new String[COMMON_HEADER_SIZE];
    System.arraycopy(array, 0, returnArray, 0, COMMON_HEADER_SIZE);
    return returnArray;
  }

  /**
   * Currently only commonHeaders and AUDIT_FILE_NAME are set. 
   * @param commonHeaders
   * @param RNHeaders
   * @return
   */
  public static String[] getCommonAndRNHeaders(Map commonHeaders, Map RNHeaders)
  {
    ChannelSendHeader header =
      new ChannelSendHeader(
        (String) commonHeaders.get(ICommonHeaders.MSG_EVENT_ID),
        (String) commonHeaders.get(ICommonHeaders.MSG_TRANSACTION_ID),
        (String) commonHeaders.get(ICommonHeaders.MSG_EVENT_SUB_ID),
        (String) commonHeaders.get(ICommonHeaders.PAYLOAD_ID));

    header.setGridnodeHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.SENDER_BE_GNID),
      (String) commonHeaders.get(ICommonHeaders.RECIPENT_BE_GNID),
      (String) commonHeaders.get(ICommonHeaders.COMM_GNCI));

    header.setRegistryHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.SENDER_BE_UUID),
      (String) commonHeaders.get(ICommonHeaders.SENDER_BE_UDDI_URL),
      (String) commonHeaders.get(ICommonHeaders.RECIPENT_BE_UUID),
      (String) commonHeaders.get(ICommonHeaders.RECIPENT_BE_UDDI_URL));

    header.setMessageHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.PAYLOAD_GROUP),
      (String) commonHeaders.get(ICommonHeaders.PAYLOAD_TYPE));
      
    header.setRNHeaders(
      (String)RNHeaders.get(IRNHeaderConstants.AUDIT_FILE_NAME));
    
    //TWX add tracingID into common header
    header.setOnlineTrackingHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.TRACING_ID));
    
    return header.getHeaderArray();
  }


  public static String[] getAllHeaders(Map commonHeaders, Map AS2Headers)
  {
    ChannelSendHeader header =
      new ChannelSendHeader(
        (String) commonHeaders.get(ICommonHeaders.MSG_EVENT_ID),
        (String) commonHeaders.get(ICommonHeaders.MSG_TRANSACTION_ID),
        (String) commonHeaders.get(ICommonHeaders.MSG_EVENT_SUB_ID),
        (String) commonHeaders.get(ICommonHeaders.PAYLOAD_ID));

    header.setGridnodeHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.SENDER_BE_GNID),
      (String) commonHeaders.get(ICommonHeaders.RECIPENT_BE_GNID),
      (String) commonHeaders.get(ICommonHeaders.COMM_GNCI));

    header.setRegistryHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.SENDER_BE_UUID),
      (String) commonHeaders.get(ICommonHeaders.SENDER_BE_UDDI_URL),
      (String) commonHeaders.get(ICommonHeaders.RECIPENT_BE_UUID),
      (String) commonHeaders.get(ICommonHeaders.RECIPENT_BE_UDDI_URL));

    header.setMessageHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.PAYLOAD_GROUP),
      (String) commonHeaders.get(ICommonHeaders.PAYLOAD_TYPE));

    header.setAS2Headers(
      (String)AS2Headers.get(IAS2Headers.MESSAGE_ID),
      (String)AS2Headers.get(IAS2Headers.AS2_FROM),
      (String)AS2Headers.get(IAS2Headers.AS2_TO),
      (String)AS2Headers.get(IAS2Headers.IS_ACK_REQ),
      (String)AS2Headers.get(IAS2Headers.IS_ACK_SIGNED),
      (String)AS2Headers.get(IAS2Headers.IS_ACK_SYNC),
      (String)AS2Headers.get(IAS2Headers.RETURN_URL),
      (String)AS2Headers.get(IAS2Headers.CONTENT_TYPE),
      (String)AS2Headers.get(IAS2Headers.SUBJECT),
      (String)AS2Headers.get(IAS2Headers.MIC),
      (String)AS2Headers.get(IAS2Headers.AUDIT_FILE_NAME),
      (String)AS2Headers.get(IAS2Headers.PAYLOAD_FILENAME) //NSL20060830
    );
    
    //TWX add tracingID into common header
    header.setOnlineTrackingHeaderInfo(
      (String) commonHeaders.get(ICommonHeaders.TRACING_ID));
    
    return header.getHeaderArray();
  }

  private static Map perpareCommonHeader(String[] header)
  {
    Map commonHeaders = new HashMap();
    try
    {
      commonHeaders.put(ICommonHeaders.MSG_EVENT_ID, header[EVENT_ID_POS]);
      commonHeaders.put(
        ICommonHeaders.MSG_TRANSACTION_ID,
        header[TRANSACTION_ID_POS]);
      commonHeaders.put(
        ICommonHeaders.MSG_EVENT_SUB_ID,
        header[EVENT_SUB_ID_POS]);
      commonHeaders.put(ICommonHeaders.PAYLOAD_ID, header[FILE_ID_POS]);
      /** @todo Check with Soklay - While Activatation Request SenderNodeId is null*/
      if (null == header[SENDER_NODEID_POS])
        commonHeaders.put(
          ICommonHeaders.SENDER_BE_GNID,
          ChannelServiceDelegate.getMyNodeId());
      else
        commonHeaders.put(
          ICommonHeaders.SENDER_BE_GNID,
          header[SENDER_NODEID_POS]);
      commonHeaders.put(
        ICommonHeaders.RECIPENT_BE_GNID,
        header[RECIPIENT_NODEID_POS]);
      commonHeaders.put(ICommonHeaders.COMM_GNCI, header[GNCI_POS]);
      commonHeaders.put(
        ICommonHeaders.PAYLOAD_TYPE,
        header[PACKAGING_ENVELOPE_POS]);
      commonHeaders.put(ICommonHeaders.SENDER_BE_UUID, header[SENDER_UUID_POS]);
      commonHeaders.put(
        ICommonHeaders.SENDER_BE_UDDI_URL,
        header[SENDER_REGISTRY_QUERY_URL]);
      commonHeaders.put(
        ICommonHeaders.RECIPENT_BE_UUID,
        header[RECIPIENT_UUID_POS]);
      commonHeaders.put(
        ICommonHeaders.RECIPENT_BE_UDDI_URL,
        header[RECIPIENT_REGISTRY_QUERY_URL]);
      commonHeaders.put(
        ICommonHeaders.PAYLOAD_GROUP,
        header[DOCUMENT_TYPE_POS]);
      commonHeaders.put(
        ICommonHeaders.MSG_PROCESS_ID,
        ChannelServiceDelegate.getMyNodeId());
      
      //TWX 20102006 Add the tracingID
        commonHeaders.put(ICommonHeaders.TRACING_ID, header[TRACING_ID]);
      
      return commonHeaders;
    }
    catch (ArrayIndexOutOfBoundsException ex)
    {
      ChannelLogger.warnLog(
        "ChannelSendHeader",
        "perpareCommonHeader()",
        "[Header Array Accessed with Invalid Index or Index dose not exists]",
        ex);
      return commonHeaders;
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        "ChannelSendHeader",
        "perpareCommonHeader()",
        "[Header Array Accessed with Invalid Index or Index dose not exists]",
        ex);
      return commonHeaders;
    }
  }
  
  //TWX 20102006 Added for OnlineTracking
  public void setOnlineTrackingHeaderInfo(String tracingID)
  {
    _array[TRACING_ID] = tracingID;
  }
  
}
