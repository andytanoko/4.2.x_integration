package com.gridnode.pdip.base.transport.soap;

public interface ISoapServiceConstants
{
	public static final String URI_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";

	public static final String NS_XSD = "http://www.w3.org/2001/XMLSchema";

	public static final String URI_ENCODING_PROPERTY = "javax.xml.rpc.encodingstyle.namespace.uri";

	public static final String DEFAULT_SERVICE_NAME = "SoapMessageReceiverService";

	public static final String DEFAULT_SERVICE_PORT = "SoapMessageReceiverService";

	public static final String DEFAULT_SERVICE_OPERATION_NAME = "receiveMessage";
}