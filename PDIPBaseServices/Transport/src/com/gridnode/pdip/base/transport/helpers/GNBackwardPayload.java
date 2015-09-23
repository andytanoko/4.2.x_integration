/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNBackwardPayload.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Dec 03 2002    Goh Kan Mun                 Created.
 * Dec 04 2002    Goh Kan Mun                 Modified - Fixed bug, check object.
 * Dec 05 2002    Jagadeesh                   Modified -
 * Jan 30 2003    Goh Kan Mun                 Modified - Add GridNodeCommInfo String
 *                                                      (Backward compatible for file splitting).
 *                                                     - Set eventType from Channel instead of 0.
 */

package com.gridnode.pdip.base.transport.helpers;

import java.io.Serializable;
import java.util.Hashtable;

import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.transport.GN_DL_ObjectToSend_v1;
import com.gridnode.transport.communication.ConnectionLayerInformationSent_v1;
import com.gridnode.transport.communication.GridEvent;
import com.gridnode.transport.communication.TptLayerInformationSent_v1;

/**
 * This class provides static methods to retrieve backward compatible object from
 * <code>GNTransport</code> object and vice versa. It also provide a method to check
 * whether a <code>Serializable</code> object is a backward compatible object.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GNBackwardPayload
{
  /**
   * Check whether a <code>Serializable</code> object is a backward compatible object.
   *
   * @param           payload   the <code>Serializable</code> object to be tested.
   *
   * @return          true if the <code>Serializable</code> object is a backward
   *                  compatible object, false otherwise.
   */
  public static boolean isBackwardCompatiblePayload(Serializable payload)
  {
    if (payload instanceof GN_DL_ObjectToSend_v1)
      return true;
    else
      return false;
  }

  /**
   * Converts a <code>GNTransportPayload</code> to a backward compatible object
   * in <code>Serializable</code> form.
   *
   * @param           commInfoVersion   version of communication information used for conversion.
   * @param           payload           the <code>GNTransportPayload</code> object to be converted.
   *
   * @return          a backward compatible <code>Serializable</code> object.
   *
   * @exception       Exception thrown when the conversion fails.
   */
  public static Serializable getBackwardCompatiblePayload(String commInfoVersion,
         GNTransportPayload payload)
         throws Exception
  {
    Hashtable header = payload.getHeader();
    //String s;
    int eventSubId=0;
    int processId=0;

    //Possible Null Header values for below two headers.
     if (null != header.get(ICommonHeaders.MSG_EVENT_SUB_ID))
      eventSubId = new Integer((String)header.get(ICommonHeaders.MSG_EVENT_SUB_ID)).intValue();
     if (null != header.get(ICommonHeaders.MSG_PROCESS_ID))
      processId = new Integer((String)header.get(ICommonHeaders.MSG_PROCESS_ID)).intValue();
     String gnci = (String)header.get(ICommonHeaders.COMM_GNCI);


    int eventId = new Integer((String)header.get(ICommonHeaders.MSG_EVENT_ID)).intValue();
    int transId = new Integer((String)header.get(ICommonHeaders.MSG_TRANSACTION_ID)).intValue();
    Integer recptNodeId = Integer.valueOf((String)header.get(ICommonHeaders.RECIPENT_BE_GNID));
    Integer senderNodeId = Integer.valueOf((String)header.get(ICommonHeaders.SENDER_BE_GNID));

    TptLogger.debugLog("","getBackwardCompatiblePayload()","[EventId]["+eventId+"]");
    TptLogger.debugLog("","getBackwardCompatiblePayload()","[transId]["+transId+"]");
    TptLogger.debugLog("","getBackwardCompatiblePayload()","[recptNodeId]["+recptNodeId+"]");
    TptLogger.debugLog("","getBackwardCompatiblePayload()","[senderNodeId]["+senderNodeId+"]");
    TptLogger.debugLog("","getBackwardCompatiblePayload()","[processID]["+processId+"]");
    TptLogger.debugLog("","getBackwardCompatiblePayload()","[EventSubId]["+eventSubId+"]");
    TptLogger.debugLog("","getBackwardCompatiblePayload()","[GNCI]["+gnci+"]");


//    int eventSubId = new Integer((String)header.get(ICommonHeaders.MSG_EVENT_SUB_ID)).intValue();
//    int processId = new Integer((String)header.get(ICommonHeaders.MSG_PROCESS_ID)).intValue();
//    String gnci = (String)header.get(ICommonHeaders.COMM_GNCI);
    int eventType = 0;
    if (header.get(ITransportConstants.EVENT_TYPE) != null)
      eventType = new Integer((String) header.get(ITransportConstants.EVENT_TYPE)).intValue();
    GridEvent event = new GridEvent(
              senderNodeId,
              recptNodeId,
              eventId,
              payload.getData(),
              "EN",
              transId,
              processId,
              eventType,
              eventSubId,
              gnci
              );
    event.setVersionNumber(commInfoVersion);
    String fileId = (String)header.get(ICommonHeaders.PAYLOAD_ID);
    TptLogger.debugLog("", "", event.getEventString());
    TptLayerInformationSent_v1 tptInfoSent = new TptLayerInformationSent_v1(payload.getFileContent(), fileId, event);
    ConnectionLayerInformationSent_v1 connInfoSent = new ConnectionLayerInformationSent_v1(tptInfoSent);
    return new GN_DL_ObjectToSend_v1(connInfoSent.toByteArray());
  }

  /**
   * Converts a backward compatible object in <code>Serializable</code> form
   * to a <code>GNTransportPayload</code> object.
   *
   * @param           obj           the <code>Serializable</code> object to be converted.
   *
   * @return          a <code>GNTransportPayload</code> object.
   *
   * @exception       Exception thrown when the conversion fails.
   */
  public static GNTransportPayload getGNTransportPayload(Serializable obj)
         throws Exception
  {
    if (obj == null || !isBackwardCompatiblePayload(obj))
      throw new Exception("unrecognized object message received: " + obj);
    GN_DL_ObjectToSend_v1 payload = (GN_DL_ObjectToSend_v1) obj;
    Serializable o = payload.getObject();
    byte[] b = (byte[])o;
    ConnectionLayerInformationSent_v1 info = (ConnectionLayerInformationSent_v1) ConnectionLayerInformationSent_v1.fromByteArray(b);
    TptLayerInformationSent_v1 tptInfoSent = (TptLayerInformationSent_v1) info.getData();
    GridEvent event = tptInfoSent.getGridEvent();
    Hashtable header = new Hashtable();
    header.put(ICommonHeaders.MSG_EVENT_ID, String.valueOf(event.getEventID()));
    header.put(ICommonHeaders.MSG_TRANSACTION_ID,String.valueOf(event.getFunctionID()));
    header.put(ICommonHeaders.RECIPENT_BE_GNID, event.getRecipientNodeID() == null ?
                                                      new String("0") : String.valueOf(event.getRecipientNodeID()));

    header.put(ICommonHeaders.SENDER_BE_GNID,event.getSenderNodeID() == null ?
                                                  new String("0") : String.valueOf(event.getSenderNodeID()));

    header.put(ICommonHeaders.MSG_EVENT_SUB_ID,String.valueOf(event.getEventSubID()));
    header.put(ICommonHeaders.MSG_PROCESS_ID,String.valueOf(event.getProcessID()));
    header.put(ICommonHeaders.PAYLOAD_ID,tptInfoSent.getFileName()== null ?
                                    ITransportConstants.UNDEFINED_FILE_ID : tptInfoSent.getFileName());
    if (event.getGNCI() != null)
      header.put(ICommonHeaders.COMM_GNCI, event.getGNCI());
    header.put(ITransportConstants.EVENT_TYPE, String.valueOf(event.getEventType()));
    byte[] fileContent = tptInfoSent.getFileContents();
    String[] data = event.getEventData();
    return new GNTransportPayload(header, data, fileContent);

  }

}