/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITransportHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 21 2002    Jagadeesh               Created
 * Dec 04 2002    Jagadeesh               Modified - Header from String[] to Hashtable.
 */


package com.gridnode.pdip.base.transport.handler;

import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.exceptions.*;

import java.util.Hashtable;

public interface ITransportHandler
{
  public static final String HTTP_RESPONSE_CODE = "httpResponseCode";
  
  /**
   *  send() send's the message to the destination in an Asynchronous fashion. The message is
   *  delivered to the destination specified  in commInfo.
   *
   * @param commInfo - contains all network and destination specific information
   * @param header - The header information required to send the message.
   * @param fileData -- the file data to be sent
   * @param dataToSend - data value's that need to be sent.
   * @throws GTTransportException - if send process fails due to some error or invalid paramaters.
   * @throws RemoteException - if system level or RMI level error occurs.
   *
   */

  public void send(ICommInfo commInfo, Hashtable header, String dataToSend[], byte[] fileData)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTransportException;

  /**
   * This Service is invoked to make a Persistent connection to the specified destination
   *
   * @param commInfo - contains all network and destination specific information
   * @throws GTTransportException - if connect fails due to some error or invalid parameters.
   * @throws RemoteException - if system level or RMI level error occurs.
   */

  public void connect(ICommInfo commInfo, String[] header)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException;

  /**
   * This function establishes a Persistent connection to the specified destination
   * and listens to the specified topic/queue.
   * If the connection is already there, it simply starts listening to the topic/queue.
   *
   * @param commInfo - contains all network and destination specific information
   */

  public void connectAndListen(ICommInfo commInfo, String[] header)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException;

  /**
   * This service is invoked to disconnect the Persistent Connection made using the
   * Connect Service.
   *
   * @param commInfo - contains all network and destination specific information
   * @throws GTTransportException - if disconnect fails due to some error or invalid parameters.
   * @throws RemoteException - if system level or RMI level error occurs.
   */


  public void disconnect(ICommInfo commInfo)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException;


}