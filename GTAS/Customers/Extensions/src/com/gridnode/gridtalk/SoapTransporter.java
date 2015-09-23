/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapTransporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 03 2003    Neo Sok Lay         Created
 */
package com.gridnode.gridtalk;

import com.gridnode.ext.util.ValidationUtil;
import com.gridnode.ext.util.exceptions.ValidationException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.encoding.XMLType;

import java.io.File;

/**
 * Transporter specialized for transmission via SOAP.
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class SoapTransporter
{

  public static final String SOAP_ENCODING_URI =
    "http://schemas.xmlsoap.org/soap/encoding/";
  public static final String SOAP_NAMESPACE_URI =
    "http://www.w3.org/2001/XMLSchema";
  public static final String SOAP_ENCODING_STYLE_PROPERTY =
    "javax.xml.rpc.encodingstyle.namespace.uri";
  public static final String SERIALIZER_FACTORY =
    "javax.xml.rpc.encoding.SerializerFactory";

  public SoapTransporter()
  {
  }

  private void validateTransporterProperties(InboundTransporterProperties props)
    throws ValidationException
  {
    // validate the properties
    ValidationUtil.checkNonBlank(
      InboundTransporterProperties.KEY_SOAP_INBOUND_ENDPOINT_URL,
      props.getSoapServiceEndpointUrl());
    ValidationUtil.checkNonBlank(
      InboundTransporterProperties.KEY_SOAP_INBOUND_SERVICE,
      props.getSoapServiceName());
    ValidationUtil.checkNonBlank(
      InboundTransporterProperties.KEY_SOAP_INBOUND_SERVICE_OPERATION,
      props.getSoapServiceOperation());
    ValidationUtil.checkNonBlank(
      InboundTransporterProperties.KEY_SOAP_INBOUND_SERVICE_PORT,
      props.getSoapServicePort());
  }

  /**
   * Invoke a service for pushing a file to the target endpoint service.
   * It is assume that the endpoint service accepts 3 parameters:<p>
   * <OL>
   *   <LI>headerNames: Array of String(s) specifying the channel headers</LI>
   *   <LI>headerValues: Array of String(s) specifying values corresponding to headerNames.</LI>
   *   <LI>filePayload: DataHandler for the payload file to transmit.</LI>
   * </OL>
   *
   * @param props The InboundTransporterProperties containing properties
   * include SOAP transport specific properties.
   * @throws Exception Error in the service invocation, or invalid
   * properties for SOAP transport.
   */
  public void invokeInboundService(InboundTransporterProperties props)
    throws Exception
  {
    validateTransporterProperties(props);

    //prepare
    DataHandler dataHandler = getDataHandler(props.getPayloadFile());

    String[] attributeNames = props.getHeaderAttributes();
    String[] attributeValues = props.getHeaderAttributeValues(attributeNames);

    QName serviceName = new QName(props.getSoapServiceName());
    QName port = new QName(props.getSoapServicePort());
    QName opName = new QName(props.getSoapServiceOperation());
    QName serFactory = new QName(SERIALIZER_FACTORY);
    QName blank = new QName("");
    String endPoint = props.getSoapServiceEndpointUrl();

    //set parameters
    ServiceFactory serviceFactory = ServiceFactory.newInstance();
    Service service = serviceFactory.createService(serviceName);

    Call serviceCall = service.createCall(port);
    serviceCall.setProperty(SOAP_ENCODING_STYLE_PROPERTY, SOAP_ENCODING_URI);
    serviceCall.setProperty(Call.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
    serviceCall.setProperty(Call.SOAPACTION_URI_PROPERTY, "");
    serviceCall.setTargetEndpointAddress(endPoint);
    serviceCall.setOperationName(opName);
    serviceCall.addParameter(
      "headerNames",
      XMLType.SOAP_ARRAY,
      ParameterMode.IN);
    serviceCall.addParameter(
      "headerValues",
      XMLType.SOAP_ARRAY,
      ParameterMode.IN);
    serviceCall.addParameter("filePayload", serFactory, ParameterMode.IN);
    serviceCall.setReturnType(blank);

    //invoke
    serviceCall.invoke(
      new Object[] { props.stripHeaderPrefix(attributeNames), attributeValues, dataHandler });
  }

  private DataHandler getDataHandler(String payloadFile) throws Exception
  {
    return new DataHandler(new FileDataSource(new File(payloadFile)));
  }

}