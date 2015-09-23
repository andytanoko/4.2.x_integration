/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapMessageReceiverService.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Sep 25 2002    Jagadeesh               Created
 * Nov 29 2005    Neo Sok Lay             Use common Jndi properties for lookup
 * Jan 29 2007    Neo Sok Lay             Use Queue instead of Topic gor Appserver JMS
 * Apr 17 2007		Alain Ah Ming						JMSException not thrown from initApp()
 * Oct 04 2010    Tam Wei Xiang           #1897 - Added new web service method
 *                                                for external client to upload file into GT.
 */

package com.gridnode.pdip.base.transport.soap.service;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jms.*;
import javax.naming.NamingException;

//import org.apache.axis2.AxisFault;
//import org.apache.axis2.context.MessageContext;
//import org.apache.axis2.context.OperationContext;
//import org.apache.axis2.wsdl.WSDLConstants;

import com.gridnode.pdip.base.transport.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.transport.handler.ISoapMessageReceiveService;
import com.gridnode.pdip.base.transport.helpers.GNTransportPayload;
import com.gridnode.pdip.base.transport.helpers.ITransportConfig;
import com.gridnode.pdip.base.transport.helpers.ITransportConstants;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.base.transport.soap.exception.GtasWsException;
import com.gridnode.pdip.base.transport.soap.model.MessageMetaInfo;
import com.gridnode.pdip.base.transport.soap.service.handler.ISoapContextHandler;
import com.gridnode.pdip.base.transport.soap.service.handler.SoapContextFactory;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.jms.JMSSender;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.UUIDUtil;

/**
 * This class is Piviot Message Receiver for all Messages received by SOAP-HTTP,
 * as this class is registered with SOAP Provider.
 *
 * The messages received by SoapMessageReceiverService is routed to GridTalk primary
 * message listener service, as to enable GridTalk to process inbound message.
 * 	
 * This class is essentially registered with SOAP Engine,or in other words it is
 * "deployed" in SOAP Engine.  
 *
 * @author Jagadeesh.
 * @since 2.2
 */

public class SoapMessageReceiverService implements ISoapMessageReceiveService
{
	private static final String CLASS_NAME = "SoapMessageReceiverService";

	private static Configuration _appTptConfig =
		ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME);

	//private static Connection _appConnection = null;
	private static Destination _appDest = null;
  private static ConnectionFactory _connFactory = null;

//  private MessageContext inMessageContext;
  
	static {
		try
		{
			initApp();
		} catch (NamingException e)
		{
			TptLogger.errorLog(ILogErrorCodes.TPT_SOAP_MSG_RCVR_SVC_INIT,
				CLASS_NAME,
				"staticInitilaziton",
				"Could Not Initilize SoapMessageReceiver: "+e.getMessage(),
				e);
		} 
//		catch (JMSException e)
//		{
//			TptLogger.errorLog(ILogErrorCodes.TPT_SOAP_MSG_RCVR_SVC_INIT,
//				CLASS_NAME,
//				"staticInitilaziton",
//				"Could Not Initilize SoapMessageReceiver: "+e.getMessage(),
//				e);
//		}
	}

	public SoapMessageReceiverService()
	{
	}
  
  
  
	public void receiveMessage(
		String[] attributeNames,
		String[] attributeValues,
		DataHandler receiveMessageParam)
		throws GtasWsException
	{
		try
		{
			TptLogger.infoLog(
				CLASS_NAME,
				"receiveMessage()",
				"[In receiveMessage Begin]");
			if (receiveMessageParam != null)
			{
				debugLogHeaders(attributeNames, attributeValues);
				Hashtable headers =
					getHeaderFromNameValuePair(attributeNames, attributeValues);
				DataSource dataSource = receiveMessageParam.getDataSource();
				byte[] fileContent = getFileContent(dataSource.getInputStream());
				GNTransportPayload payLoad =
					new GNTransportPayload(headers, null, fileContent);
				postMessageToApplicationReceiver(payLoad);
			} else {
				TptLogger.infoLog(
					CLASS_NAME,
					"receiveMessage",
					"[Message Receive is NULL]");
			}					
			TptLogger.infoLog(
				CLASS_NAME,
				"receiveMessage()",
				"[In receiveMessage End]");
		} catch (Exception ex)
		{
			TptLogger.warnLog(
				CLASS_NAME,
				"receiveMessage()",
				"[Could Not Process Receive Message]",
				ex);
			throw new GtasWsException(ex.getLocalizedMessage(), ex);
		}
	}

  /**
   * #1897 - Receive the message delivered from external client
   * @param msgInfo The meta-info of the message
   * @param receiveMessageParam The content of the message
   * @throws GtasWsException thrown if failed to process the incoming data content or encountered internal server error.
   */
  public void receiveMessage2(MessageMetaInfo msgInfo, DataHandler receiveMessageParam) throws GtasWsException
  {
    String mn = "receiveMessage2";
    TptLogger.infoLog(CLASS_NAME, mn,"");
    
    ISoapContextHandler contextHandler = SoapContextFactory.getSoapContextHandler(SoapContextFactory.AXIS_CONTEXT_HANDLER); 
    String authentiatedUser = contextHandler.getAuthenticatedUsername();
    TptLogger.debugLog(CLASS_NAME, mn, "receive message "+msgInfo+" from user: "+authentiatedUser);
    
    Hashtable<String,String> headers = new Hashtable<String,String>();
    
    //Sender ID
    headers.put(ICommonHeaders.SENDER_ID, authentiatedUser); 
    
    //derive the recipient BE ID. For IB web services, the deriving of the recipient BE
    //via Recipient_BE_ID is sufficient.
    headers.put(ICommonHeaders.RECIPENT_BE_ID, msgInfo.getReceiver());
    headers.put(ICommonHeaders.COMM_CONNECTION_TYPE, ITransportConstants.INCOMING_CONNECTION_TYPE_DIRECT);
    
    //Doc Type
    headers.put(ICommonHeaders.PAYLOAD_GROUP, msgInfo.getDocType());
    
    //set default package type
    headers.put(ICommonHeaders.PAYLOAD_TYPE, ITransportConstants.PACKAGE_TYPE_NONE);
    headers.put(ICommonHeaders.MSG_EVENT_ID, "3200");
    
    TptLogger.debugLog(CLASS_NAME, mn, "Package type: "+headers.get(ICommonHeaders.PAYLOAD_TYPE));
    
    DataSource dataSource = receiveMessageParam.getDataSource();
    byte[] fileContent;
    try
    {
      fileContent = getFileContent(dataSource.getInputStream());
    }
    catch (IOException e)
    {
      TptLogger.warnLog(CLASS_NAME, mn, "Failed to retrieve content from DataHandler "+e.getMessage(), e);
      throw new GtasWsException("Failed to retrieve content from DataHandler "+e.getMessage());
    }
    
    try
    {
      File tempFile = saveContentToTemp(fileContent);
      GNTransportPayload payload = new GNTransportPayload(headers, new String[0], tempFile);
      postMessageToApplicationReceiver(payload);
    }
    catch (JMSException e)
    {
      TptLogger.errorLog(ILogErrorCodes.TPT_SOAP_RECEIVE_EXCEPTION_HANDLE, CLASS_NAME, mn, "Failed to delegate to the app receiver "+e.getMessage(), e);
      throw new GtasWsException("Encounter internal server error, please try again");
    }
    catch (FileAccessException e)
    {
      TptLogger.errorLog(ILogErrorCodes.TPT_SOAP_RECEIVE_EXCEPTION_HANDLE, CLASS_NAME, mn, "Failed to create temp file "+e.getMessage(), e);
      throw new GtasWsException("Encounter internal server error, please try again");
    }
    catch (Exception e)
    {
      TptLogger.errorLog(ILogErrorCodes.TPT_SOAP_RECEIVE_EXCEPTION_HANDLE, CLASS_NAME, mn, "Unexpected error: failed to delegate to the app receiver "+e.getMessage(), e);
      throw new GtasWsException("Encounter internal server error, please try again");
    }
  }
  
  private File saveContentToTemp(byte[] content) throws FileAccessException
  {
    String filename = UUIDUtil.getRandomUUIDInStr()+".tmp";
    ByteArrayInputStream inStream = new ByteArrayInputStream(content);
    String tempFilename = FileUtil.create(IPathConfig.PATH_TEMP, filename, inStream);
    File tempFile = FileUtil.getFile(IPathConfig.PATH_TEMP, tempFilename);
    
    TptLogger.debugLog(CLASS_NAME, "saveContentToTemp", "Save incoming soap msg payload to file "+((tempFile == null)? "": tempFile.getAbsolutePath()));
    
    return tempFile;
  }
  
	/**
	 * @param attributeNames
	 * @param attributeValues
	 * @return
	 */
	private Hashtable getHeaderFromNameValuePair(
		String[] attributeNames,
		String[] attributeValues)
	{
		Hashtable header = new Hashtable();
		for (int i = 0; i < attributeNames.length; i++)
		{
			String key = attributeNames[i];
			String value = attributeValues[i];
			header.put(key, value);
		}
		return header;
	}

	/**
	 * @param attributeNames
	 * @param attributeValues
	 */
	private void debugLogHeaders(
		String[] attributeNames,
		String[] attributeValues)
	{
		if (attributeNames != null && attributeValues != null)
		{
			for (int i = 0; i < attributeNames.length; i++)
				TptLogger.debugLog(
					CLASS_NAME,
					"debugLogHeaders",
					"[Attribute Name][" + attributeNames[i] + "]");
			for (int i = 0; i < attributeValues.length; i++)
				TptLogger.debugLog(
					CLASS_NAME,
					"debugLogHeaders",
					"[Attribute Name][" + attributeValues[i] + "]");
		}

	}

	private byte[] getFileContent(InputStream inStream) throws IOException
	{
		DataInputStream dataInputStream = new DataInputStream(inStream);
		int size = dataInputStream.available();
		byte[] readBytes = new byte[size];
		//int bytesRead = 
		inStream.read(readBytes, 0, readBytes.length);
		TptLogger.debugLog(
			CLASS_NAME,
			"getFileContent()",
			"[Bytes Read Length][" + readBytes.length + "]");
		return readBytes;

	}

	private void postMessageToApplicationReceiver(GNTransportPayload payLoad)
		throws JMSException, Exception
	{
    if(!JMSRetrySender.isSendViaDefMode()) //handle for cluster environment in case failed over is in process
    {
      String destName = _appTptConfig.getString(ITransportConfig.APPSERVER_DESTINATION_BRIDGE_TO_APP);
      
      Hashtable<String,String> sendProps = new Hashtable<String,String>();
      sendProps.put(JMSSender.CONN_FACTORY, _appTptConfig.getString(ITransportConfig.APPSERVER_CONNECTION_FACTORY));
      sendProps.put(JMSSender.JMS_DEST_NAME, destName);
      sendProps.put(JMSSender.JNDI_SERVICE, ServiceLocator.CLIENT_CONTEXT);
      
      try
      {
        JMSRetrySender.sendMessage(destName, payLoad, null, sendProps);
      }
      catch (Throwable e)
      {
        TptLogger.errorLog(ILogErrorCodes.TPT_JMS_EXCEPTION_HANDLE, CLASS_NAME,"postMessageToApplicationReceiver", "Error encountered", e);
        return;
      }
      return;
    }
    
    else
    {
      Connection appConnection = null;
      try
      {
        appConnection = _connFactory.createConnection();
        Session session =
          appConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(_appDest);
        ObjectMessage message = session.createObjectMessage(payLoad);
        producer.send(message);
      }
      catch (Exception ex)
      {
        TptLogger.warnLog(CLASS_NAME,"postMessageToApplicationReceiver", "Error encountered", ex);
        if (appConnection != null)
        {
          try
          {
            appConnection.close();
          }
          catch (Exception e)
          {
            TptLogger.warnLog(CLASS_NAME,"postMessageToApplicationReceiver", "Unable to close MQ connection", ex);
          }
        }
      }
    }
	}

	private static void initApp() throws NamingException
	{
		JNDIFinder findHelper = new JNDIFinder(getContextProperties());
    //NSL20070129 Use generic Destination instead of Topic or Queue
		_connFactory =
			(ConnectionFactory) findHelper.lookup(
				_appTptConfig.getString(
					ITransportConfig.APPSERVER_CONNECTION_FACTORY),
				ConnectionFactory.class);
		//_appConnection = connectionFactory.createConnection();
		_appDest =
			(Destination) findHelper.lookup(
				_appTptConfig.getString(ITransportConfig.APPSERVER_DESTINATION_BRIDGE_TO_APP),
				Destination.class);
	}

	private static Properties getContextProperties()
	{
    /*
		// Enough of the world is now in place that we can risk
		// initializing the properties.
		Properties props = new Properties();
		props.put(
			Context.INITIAL_CONTEXT_FACTORY,
			_appTptConfig.getString(
				ITransportConfig.APPSERVER_INITIAL_CONTEXT_FACTORY));
		props.put(
			Context.PROVIDER_URL,
			_appTptConfig.getString(ITransportConfig.APPSERVER_PROVIDER_URL));
		props.put(
			Context.URL_PKG_PREFIXES,
			_appTptConfig.getString(ITransportConfig.APPSERVER_URL_PKG_PREFIXES));
		return props;
    */
    //NSL20051129 Use common jndi properties for lookup
    return ConfigurationManager.getInstance().getConfig(IFrameworkConfig.FRAMEWORK_JNDI_CONFIG).getProperties();
	}



}