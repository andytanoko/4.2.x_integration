/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITransportServiceObj.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * ??? ?? 2002    Jadadeesh, Jianyu       Created
 * Jun 21 2002    Goh Kan Mun             Modified - To change the method declaration.
 * Oct 20 2002    Goh Kan Mun             Modified - To change the method declaration.
 *                                                 - To throw specific Exception.
 *                                                 - To remove register/deregister listener.
 * Dec 02 2002    Goh Kan Mun             Modified - To add a header for connect and connectAndListen
 *                                                   methods so as to inform the BL/Channel when
 *                                                   the persistence connection fails after connected.
 */

package com.gridnode.pdip.base.transport.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.exceptions.*;
import com.gridnode.pdip.framework.messaging.Message;

/**
 * This RemoteInterface is the TransportController - or a TransportFacade which
 * details the methods the client/BL can invoke., and list's all the services
 * the Transport Module.
 *
 * To invoke the services of the Transport Module., this interface's needs to be
 * deployed in the App Server.
 *
 * And to call the Services on Transport Facade..
 *
 * ITransportServiceHome home = (ITransportServiceHome)PortableRemoteObject.narrow(ObjectRef,ITransportServiceHome.class);
 *
 * ITransportServiceObj l_transportFacade = home.create();
 *
 * l_transportFacade.send(.....);
 */

public interface ITransportServiceObj extends EJBObject
{

  /**
   *  send() send's the message to the destination in an Asynchronous fashion. The message is
   *  delivered to the destination specified  in commInfo.
   *
   * @param commInfo - contains all network and destination specific information
   * @param header - The header information required to send the message.
   * @param fileData -- the file data to be sent
   * @param dataToSend - data value's that need to be sent.
   * @throws InvalidProtocolException - if the protocol is invalid.
   * @throws GNTransportException - if send process fails due to some error or invalid paramaters.
   * @throws RemoteException - if system level or RMI level error occurs.
   *
   */

 /* public void send(ICommInfo commInfo, Hashtable header, String dataToSend[], byte[] fileData)
         throws InvalidProtocolException,
                GNTransportException,
                RemoteException;
 */
  /**
   *  send() send's the message to the destination in an Asynchronous fashion. The message is
   *  delivered to the destination specified  in commInfo.
   *
   * @param commInfo - contains all network and destination specific information
   * @param message - a wrapper object for the headers, payload and data
   *
   * @throws InvalidProtocolException - if the protocol is invalid.
   * @throws GNTransportException - if send process fails due to some error or invalid paramaters.
   * @throws RemoteException - if system level or RMI level error occurs.
   *
 */
  public Message send(ICommInfo commInfo, Message message)
    throws InvalidProtocolException,
       InvalidCommInfoException,
       GNTransportException,
       RemoteException;


  /**
   * This method is invoked to make a Persistent connection to the specified destination
   *
   * @param commInfo - contains all network and destination specific information
   * @param header - contains information that the Channel/BL needs when informing exception.
   * @throws InvalidCommInfoException - if invalid commInfo used.
   * @throws InvalidProtocolException - if invalid protocol used.
   * @throws GNTptPersistenceConnectionException - if error occurs when creating persistence connection.
   * @throws GNTptWrongConfigException - if configuration is not correct.
   * @throws GTTransportException - if connect fails due to some error or invalid parameters.
   * @throws RemoteException - if system level or RMI level error occurs.
   */

  public void connect(ICommInfo commInfo, String[] header)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException,
                RemoteException;

  /**
   * This method establishes a Persistent connection to the specified destination
   * and listens to the specified topic/queue.
   * If the connection is already there, it simply starts listening to the topic/queue.
   *
   * @param commInfo - contains all network and destination specific information
   * @param header - contains information that the Channel/BL needs when informing exception.
   * @throws InvalidCommInfoException - if invalid commInfo used.
   * @throws InvalidProtocolException - if invalid protocol used.
   * @throws GNTptPersistenceConnectionException - if error occurs when creating persistence connection.
   * @throws GNTptWrongConfigException - if configuration is not correct.
   * @throws GTTransportException - if connect fails due to some error or invalid parameters.
   * @throws RemoteException - if system level or RMI level error occurs.
   */

  public void connectAndListen(ICommInfo commInfo, String[] header)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException,
                RemoteException;

  /**
   * This method is invoked to disconnect the Persistent Connection made using the
   * Connect Service.
   *
   * @param commInfo - contains all network and destination specific information
   * @throws InvalidCommInfoException - if invalid commInfo used.
   * @throws InvalidProtocolException - if invalid protocol used.
   * @throws GNTptPersistenceConnectionException - if error occurs when creating persistence connection.
   * @throws GNTptWrongConfigException - if configuration is not correct.
   * @throws GTTransportException - if connect fails due to some error or invalid parameters.
   * @throws RemoteException - if system level or RMI level error occurs.
   */

  public void disconnect(ICommInfo commInfo)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException,
                RemoteException;



}