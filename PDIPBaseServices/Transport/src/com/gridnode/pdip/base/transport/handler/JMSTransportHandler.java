/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSTransportHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 21 2002    Jadadeesh               Created
 * Nov 28 2002    Kan Mun                 Implemented the methods.
 * Dec 03 2002    Kan Mun                 Modified - Remove the backward compatible
 *                                                   dependency on GNTransportPayload.
 * Dec 04 2002    Kan Mun                 Modified - Send feedback after the send.
 * Dec 05 2002    Jagadeesh               Modified - Send feedback after send - to
 *                                        move to App Services - Channel Layer.
 */


package com.gridnode.pdip.base.transport.handler;

import java.io.Serializable;
import java.util.Hashtable;

import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.comminfo.IJMSCommInfo;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
import com.gridnode.pdip.base.transport.exceptions.*;
import com.gridnode.pdip.base.transport.helpers.GNBackwardPayload;
import com.gridnode.pdip.base.transport.helpers.GNTransportPayload;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.base.transport.jms.GNJMSController;

public class JMSTransportHandler implements ITransportHandler
{

  private static final String CLASS_NAME = "JMSTransportHandler";

  public JMSTransportHandler()
  {
  }

  public void send(ICommInfo commInfo, Hashtable header, String dataToSend[],
         byte[] fileData)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTransportException
  {
    //boolean status = true;
    //String msg = null;
    try
    {
      TptLogger.debugLog(CLASS_NAME, "send", "Sending to queue....");
      IJMSCommInfo tptCommInfo = getTptCommInfo(commInfo);
      GNTransportPayload payload = new GNTransportPayload(
                         header,
                         dataToSend,
                         fileData
                         );
      Serializable actualPayload = null;
      if (tptCommInfo.getTptImplVersion().startsWith("02"))
      {
        TptLogger.debugLog(
            CLASS_NAME,
            "send",
            "Backward compatible version: " + tptCommInfo.getTptImplVersion());
        actualPayload = GNBackwardPayload.getBackwardCompatiblePayload(
                      tptCommInfo.getTptImplVersion(),
                      payload
                      );
      }
      else
        actualPayload = payload;
      GNJMSController.getInstance().sendMessage(tptCommInfo, actualPayload);
      TptLogger.debugLog(CLASS_NAME, "send", "Send to queue success");
    }
    catch (InvalidCommInfoException ic)
    {
      //status = false;
      //msg = ic.getLocalizedMessage();
      throw ic;
    }
    catch (InvalidProtocolException ip)
    {
      //status = false;
      //msg = ip.getLocalizedMessage();
      throw ip;
    }
    catch (GNTransportException tpte)
    {
      //status = false;
      //msg = tpte.getLocalizedMessage();
      throw tpte;
    }
    catch (Exception e)
    {
      //status = false;
      //msg = e.getLocalizedMessage();
      throw new GNTransportException("Unable to send message", e);
    }
  }

  public void connect(ICommInfo commInfo, String[] header)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException

  {
    TptLogger.debugLog(CLASS_NAME, "connect", "connecting...");
    IJMSCommInfo tptCommInfo = getTptCommInfo(commInfo);
    GNJMSController.getInstance().createPersistenceConnection(
          tptCommInfo,
          header
          );
  }

  public void connectAndListen(ICommInfo commInfo, String[] header)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException
  {
    TptLogger.debugLog(CLASS_NAME, "connectAndListen", "connecting...");
    IJMSCommInfo tptCommInfo = getTptCommInfo(commInfo);
    GNJMSController.getInstance().createPersistenceListener(
          (IJMSCommInfo) tptCommInfo,
          header
          );
    TptLogger.debugLog(CLASS_NAME, "connect", "Successfully");
  }

  public void disconnect(ICommInfo commInfo)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException
  {
    TptLogger.debugLog(CLASS_NAME, "disconnect", "disconnecting...");
    ICommInfo tptCommInfo = getTptCommInfo(commInfo);
    if (tptCommInfo.getProtocolType().equals(ICommInfo.JMS))
      GNJMSController.getInstance().closePersistenceConnection((JMSCommInfo)tptCommInfo);
    else
      throw new InvalidProtocolException("Unrecognized protocol: " + tptCommInfo.getProtocolType());
  }

  private IJMSCommInfo getTptCommInfo(ICommInfo commInfo)
          throws InvalidProtocolException, InvalidCommInfoException
  {
    if (commInfo == null)
      throw new InvalidCommInfoException("CommInfo is null");
    if (commInfo.getProtocolType() == null)
      throw new InvalidProtocolException("Unrecognized commInfo type: " + commInfo.getProtocolType());
    else if (commInfo.getProtocolType().equals(ICommInfo.JMS) &&
         commInfo instanceof IJMSCommInfo)
    {
      TptLogger.debugLog(CLASS_NAME, "getTptCommInfo", "Info instance of JMSCommInfo");
      return ((IJMSCommInfo)commInfo);
    }
    else
    {
      throw new InvalidProtocolException("Unrecognized commInfo type: " + commInfo.getProtocolType());
    }
  }

}
