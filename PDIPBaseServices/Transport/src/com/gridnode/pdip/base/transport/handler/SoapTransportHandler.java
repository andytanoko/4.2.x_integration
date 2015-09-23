/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapTransportHandler.java
 *
 ****************************************************************************
 * Date            Author                  Changes
 ****************************************************************************
 * SEP 23 2003     Jagadeesh               Created
 */
package com.gridnode.pdip.base.transport.handler;

import java.util.Hashtable;
import java.util.Iterator;

import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.comminfo.SoapCommInfo;
import com.gridnode.pdip.base.transport.exceptions.InvalidCommInfoException;
import com.gridnode.pdip.base.transport.exceptions.InvalidProtocolException;
import com.gridnode.pdip.base.transport.exceptions.GNTptPersistenceConnectionException;
import com.gridnode.pdip.base.transport.exceptions.GNTptWrongConfigException;
import com.gridnode.pdip.base.transport.exceptions.GNTransportException;
import com.gridnode.pdip.base.transport.helpers.GNTransportPayload;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.base.transport.soap.ISoapServiceHandler;
import com.gridnode.pdip.base.transport.soap.SoapServiceHandlerRegistry;
import com.gridnode.pdip.framework.exceptions.ApplicationException;


public class SoapTransportHandler implements ITransportHandler
{

	private static final String CLASS_NAME = "SoapTransportHandler";

  public SoapTransportHandler()
  {
  }

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
                GNTransportException
	{
		try
		{
			Iterator handlers = SoapServiceHandlerRegistry.getRegisteredServiceHandlers();
		  //Iterate through all Registered Handlers to invoke the appropriate servicehandler.
			while (handlers.hasNext())
			{
				ISoapServiceHandler handler =  (ISoapServiceHandler)handlers.next();
				GNTransportPayload payLoad = new GNTransportPayload(header,dataToSend,fileData);
				try
				{
					handler.setCommInfo(commInfo);
					//Object serviceReturn = 
					handler.invokeService(payLoad);
					return;
				}
				catch (GNTransportException ex)
				{
					TptLogger.debugLog(getClassName(),"send()","[Service Not Supported by"+
						"['"+handler.getClass().getName()+"']");
				}
			}
		}
		catch (ApplicationException ex)
		{
			throw new GNTransportException(ex.getLocalizedMessage(),ex);
		}
	}

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
                GNTransportException
	{
		TptLogger.infoLog(getClassName(),"connect()","[Method not supported at this time]");
	}

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
                GNTransportException
	{
		TptLogger.infoLog(getClassName(),"connect()","[Method not supported at this time]");
	}

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
                GNTransportException
	{
		TptLogger.infoLog(getClassName(),"connect()","[Method not supported at this time]");
	}

	/**
	 *
	 */
	public static void main(String[] args) throws Exception
	{
		SoapTransportHandler handler = new SoapTransportHandler();
		handler.send(new SoapCommInfo("http://localhost:1234/axis/services/SoapMessageReceiverService"),
		new Hashtable(),
		new String[]{},
		new byte[]{}
		);
	}

	private String getClassName()
	{
	  return CLASS_NAME;
	}
}