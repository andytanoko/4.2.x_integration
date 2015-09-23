/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISoapServiceHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * SEP 23 2003     Jagadeesh               Created
 * Mar 12 2007    Neo Sok Lay             Use UUID for unique filename.
 */

package com.gridnode.pdip.base.transport.soap;


import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.exceptions.GNTransportException;
import com.gridnode.pdip.base.transport.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.transport.helpers.GNTransportPayload;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.framework.util.UUIDUtil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.encoding.XMLType;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;



/**
 * This class invokes "Default WebService" published by "GridTalk", hence there
 * are static references to SERVICE_NAME, SERVICE_PORT, SERVICE_OPERATION_NAME.
 *
 * A JAX-RPC Client is build using DII Approch,a more generic web-service client
 * would ideally lookup above required parameters from a UDDI Registry.
 *
 * A way of extensibility to achive above mentioned goal could be to implement
 * ISoapServiceHandler and find the information from a UDDI Registry.
 *
 *
 */


public class DefaultSoapServiceHandler implements ISoapServiceHandler
{
	private ICommInfo _commInfo = null;
	private static final String FILENAME_PREFIX = "~SOAPENC";
	private static final String CLASS_NAME = "DefaultSoapServiceHandler";
	private static final String UNDEFINED_HEADER  = "UNDEFINED_VALUE";

  public DefaultSoapServiceHandler(ICommInfo commInfo)
  {
		_commInfo = commInfo;
	}

	public void setCommInfo(ICommInfo commInfo)
	{
	  _commInfo = commInfo;
	}

/**
 * This method is implemented by all ServiceHandlers to invoke a webservice
 *
 *
 * @param payLoad
 * @return
 * @throws GNTransportException -
 *
 * @author Jagadeesh.
 * @since 2.2
 *
 */


	public Object invokeService(GNTransportPayload payLoad)
		throws GNTransportException
	{
		Object serviceReturn = null;
		try
		{
			if (payLoad != null)
			  serviceReturn = callDefaultWebService(payLoad);
		  else
				throw new NullPointerException("GNTransportPayload Cannot be Null");
		}
		catch(RemoteException rEx)
		{
			TptLogger.warnLog(getClassName(),"invokeService",rEx.getLocalizedMessage());
			throw new GNTransportException("Could Not invoke WEBSERVICE with EndPoint ["+_commInfo.getURL()+"]",rEx);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			TptLogger.warnLog(getClassName(),"invokeService",ex.getLocalizedMessage());
			throw new GNTransportException("Could Not invoke WEBSERVICE with EndPoint ["+_commInfo.getURL()+"]",ex);
		}
		return serviceReturn;
	}

	private Object callDefaultWebService(GNTransportPayload payLoad) throws Exception
	{
		try
		{
			DataHandler dataHandler = getDataHandlerFromPayLoad(payLoad.getFileContent());
			String[] attributeNames = ISoapHeaderKeys.HEADER_NAMES;
			String[] attributeValues = getAttributeValues(payLoad.getHeader()); 	

			ServiceFactory serviceFactory = ServiceFactory.newInstance();
			Service service = serviceFactory.createService(new QName(
												ISoapServiceConstants.DEFAULT_SERVICE_NAME));

			QName port = new QName(ISoapServiceConstants.DEFAULT_SERVICE_PORT);
			Call serviceCall = service.createCall(port);
			serviceCall.setProperty(ISoapServiceConstants.URI_ENCODING_PROPERTY,
															ISoapServiceConstants.URI_ENCODING
															);
			serviceCall.setProperty(Call.SOAPACTION_USE_PROPERTY,new Boolean(true));
			serviceCall.setTargetEndpointAddress(_commInfo.getURL());
			serviceCall.setOperationName(new QName(ISoapServiceConstants.DEFAULT_SERVICE_OPERATION_NAME));
			serviceCall.addParameter("art1",
				XMLType.SOAP_ARRAY,
				ParameterMode.IN);
			serviceCall.addParameter("arg2",
				XMLType.SOAP_ARRAY,
				ParameterMode.IN);
			serviceCall.addParameter("arg3",
				new QName("javax.xml.rpc.encoding.SerializerFactory"),
				ParameterMode.IN);
			serviceCall.setReturnType(new QName(""));

			//Test Purpose only... Can be removed later.
			infoLog(attributeNames);
			infoLog(attributeValues);

			//Object obj = 
			serviceCall.invoke(new Object[]{attributeNames,attributeValues,dataHandler});
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}

		//serviceCall.invokeOneWay(new Object[]{dataHandler});
		return new Object();
	}

	/**
	 * @param hashtable
	 * @return
	 */
	private String[] getAttributeValues(Hashtable header)
	{
		String[] headerValues = new String[ISoapHeaderKeys.HEADER_NAMES.length]; 
		for (int i=0;i<ISoapHeaderKeys.HEADER_NAMES.length;i++)
		{
			String value = (String)header.get(ISoapHeaderKeys.HEADER_NAMES[i]);
			headerValues[i] = ( value == null ? UNDEFINED_HEADER : value ); 
		}
		return headerValues;
	}
	
	private void infoLog(String[] values)
	{
		if (values != null)
		{
			for (int i=0;i<values.length;i++)
				TptLogger.infoLog(CLASS_NAME,"infoLog()",values[i]);
		}
		else
			TptLogger.infoLog(CLASS_NAME,"infoLog()","[Values Passed is Null]");
	}
	
	

	private DataHandler getDataHandlerFromPayLoad(byte[] payLoad) throws Exception
	{
		DataSource source = new FileDataSource(writeBytesToFile(payLoad));
		return new DataHandler(source);
	}


	private File writeBytesToFile(byte[] payLoad)
		throws IOException
	{
    if (payLoad != null)
		{
			File f = File.createTempFile(FILENAME_PREFIX+UUIDUtil.getRandomUUIDInStr(),".tmp");
			f.deleteOnExit();
			FileOutputStream ous = new FileOutputStream(f);
			BufferedOutputStream bufOus = new BufferedOutputStream(ous);
			bufOus.write(payLoad);
			bufOus.flush();
			bufOus.close();
			return f;
		}
		else
			throw new IOException("["+getClassName()+"][writeBytesToFile()]["+
				"PayLoad Cannot be NULL]");
	}

	private String getClassName()
	{
	  return CLASS_NAME;
	}

  public DefaultSoapServiceHandler()
  {
	}

		/** @todo Below is template code can be removed. */
		/*
		DataHandler dataHandler = new DataHandler(new FileDataSource(new File("c:/app.txt")));
		ServiceFactory factory = ServiceFactory.newInstance();
		Service service = factory.createService(new QName("SoapMessageReceiverService"));
		QName port = new QName("SoapMessageReceiverService");
		Call call = service.createCall(port);
		call.setProperty("javax.xml.rpc.encodingstyle.namespace.uri",
									   "http://schemas.xmlsoap.org/soap/encoding/");
    call.setProperty(Call.SOAPACTION_USE_PROPERTY, new Boolean(true));
		call.setTargetEndpointAddress("http://localhost:8080/axis/services/SoapMessageReceiverService");
		call.setOperationName(new QName("receiveMessage"));
		call.addParameter("arg1",new QName("javax.xml.rpc.encoding.SerializerFactory"),
		//new QName("org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory"),
		ParameterMode.IN);
		//call.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		//call.invoke(new Object[]{dataHandler});
		call.invokeOneWay(new Object[]{dataHandler});
		*/


}