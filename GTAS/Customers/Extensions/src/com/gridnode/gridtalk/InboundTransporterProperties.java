/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InboundTransporterProperties.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 03 2003    Neo Sok Lay         Created
 * Jan 15 2004    Neo Sok Lay         Add method: getWorkingDir()
 *                                    Access default properties under working dir.
 */
package com.gridnode.gridtalk;

import com.gridnode.ext.util.ExtProperties;
import com.gridnode.ext.util.StringUtil;

import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Wrapper for the properties to configure the InboundTransporter behavior.
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class InboundTransporterProperties extends ExtProperties
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3081329673173912641L;

	/**
   * Key for the InboundTransporterProperties file (for overriding
   * the default properties).
   */
  public static final String KEY_TRANSPORTER_PROPERTIES =
    "inbound.transporter.properties";

  /**
   * Key for the log file to output print statements
   */
  public static final String KEY_TRANSPORTER_LOG = "inbound.transporter.log";

  /**
   * Key for the inbound transport method.
   */
  public static final String KEY_TRANSPORT_METHOD =
    "inbound.transporter.method";

  /**
   * Key for the payload file to transmit.
   */
  public static final String KEY_PAYLOAD_FILE = "inbound.payload.file";

  /**
   * Key for the property to store the unique id for the payload file.
   */
  public static final String KEY_PAYLOAD_FILEID = "inbound.payload.file.id";

  /**
   * Key for the prefix to use for generating the unique id for the payload file.
   */
  public static final String KEY_PAYLOAD_FILEID_PREFIX = "inbound.payload.file.id.prefix";

  /**
   * Key for the directory to keep the successfully tranmitted payload files
   */
  public static final String KEY_PAYLOAD_SUCCESS_DIR = "inbound.payload.success.dir";

  /**
   * Key for the directory to keep the fail to transmit payload files
   */
  public static final String KEY_PAYLOAD_FAIL_DIR = "inbound.payload.fail.dir";

  /**
   * Key for the directory to get the payload files to transmit
   */
  public static final String KEY_PAYLOAD_DIR = "inbound.payload.dir";

  /**
   * Prefix for the key for Headers to transmit to the GridTalk receive
   * channel.
   */
  public static final String KEY_HEADER_ATTR_PREFIX = "header.";

  /**
   * Specific for SOAP transport method. The name of the receive service
   * in GridTalk.
   */
  public static final String KEY_SOAP_INBOUND_SERVICE =
    "transport.soap.inbound.service.name";

  /**
   * Specific for SOAP transport method. The name of the receive service port
   * in GridTalk.
   */
  public static final String KEY_SOAP_INBOUND_SERVICE_PORT =
    "transport.soap.inbound.service.port";

  /**
   * Specific for SOAP transport method. The name of the receive service operation
   * to be called in GridTalk.
   */
  public static final String KEY_SOAP_INBOUND_SERVICE_OPERATION =
    "transport.soap.inbound.service.op";

  /**
   * Specific for SOAP transport method. The EndPoint address of the service
   * hosted in GridTalk.
   */
  public static final String KEY_SOAP_INBOUND_ENDPOINT_URL =
    "transport.soap.inbound.service.endpoint";

  /**
   * SOAP transport method.
   */
  public static final String TRANSPORT_METHOD_SOAP = "soap";

  /**
   * Filename for the default Inbound Transporter Properties that
   * will be used to initialize the default behaviour of the
   * InboundTransporter.
   */
  public static final String DEFAULT_TRANSPORTER_PROPERTIES =
    "inbound-transporter.properties";

  /**
   * Default prefix to use for generating the unique file id for the payload file.
   */
  public static final String DEFAULT_FILEID_PREFIX = "Sibtpt_T${now}_F";

  /**
   * Substitution property key for the NOW timestamp.
   */
  public static final String NOW_SUBST_PROPERTY = "${now}";

  /**
   * Constructs an instance of InboundTransporterProperties.
   * The defalt InboundTransporterProperties file will be loaded.
   *
   * @throws Exception Error loading the default InboundTransporterProperties
   * file.
   */
  public InboundTransporterProperties() throws Exception
  {
    loadDefaultPropertiesFile();
  }

  public static String getWorkingDir()
  {
    return System.getProperty("user.dir");
  }

  private void loadDefaultPropertiesFile() throws Exception
  {
    File propsFile = new File(getWorkingDir(), DEFAULT_TRANSPORTER_PROPERTIES);
    if (propsFile.exists())
      load(new FileInputStream(propsFile));
  }

  /**
   * Load the InboundTransporterProperties specified in
   * the <code>inbound.transporter.properties</code> property.
   *
   * @throws Exception Error loading the properties file specified.
   */
  public void loadOverridingPropertiesFile() throws Exception
  {
    String overridingPropsFile = getPropertiesFile();
    if (!StringUtil.isBlank(overridingPropsFile))
    {
      load(new FileInputStream(overridingPropsFile));
    }
  }

  /**
   * Get the property value specified in the
   * <code>inbound.transporter.properties</code> property.
   *
   * @return Value of <code>inbound.transporter.properties</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getPropertiesFile()
  {
    return getProperty(KEY_TRANSPORTER_PROPERTIES, null);
  }

  /**
   * Get the property value specified in the
   * <code>inbound.transporter.log</code> property.
   *
   * @return Value of <code>inbound.transporter.log</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getLogFile()
  {
    return getProperty(KEY_TRANSPORTER_LOG, null);
  }

  /**
   * Get the property value specified in the
   * <code>inbound.transporter.method</code> property.
   *
   * @return Value of <code>inbound.transporter.method</code> property.
   * Returns <code>soap</code> if the property is not specified.
   */
  public String getTransportMethod()
  {
    return getProperty(KEY_TRANSPORT_METHOD, TRANSPORT_METHOD_SOAP);
  }

  /**
   * Get the property value specified in the
   * <code>inbound.payload.file</code> property.
   *
   * @return Value of <code>inbound.payload.file</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getPayloadFile()
  {
    return getProperty(KEY_PAYLOAD_FILE, null);
  }

  /**
   * Get the property value specified in the
   * <code>inbound.payload.file.id</code> property.
   *
   * @return Value of <code>inbound.payload.file.id</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getPayloadFileIdProperty()
  {
    return getProperty(KEY_PAYLOAD_FILEID, null);
  }

  /**
   * Get the property value specified in the
   * <code>inbound.payload.file.id.prefix</code> property.
   *
   * @return Value of <code>inbound.payload.file.id.prefix</code> property.
   * Returns the <b>DEFAULT_FILEID_PREFIX</b> value if the property is not specified.
   */
  public String getPayloadFileIdPrefix()
  {
    return getProperty(KEY_PAYLOAD_FILEID_PREFIX, DEFAULT_FILEID_PREFIX);
  }

  /**
   * Get the property value specified in the
   * <code>inbound.payload.dir</code> property.
   *
   * @return Value of <code>inbound.payload.dir</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getPayloadDir()
  {
    return getProperty(KEY_PAYLOAD_DIR, null);
  }

  /**
   * Get the property value specified in the
   * <code>inbound.payload.success.dir</code> property.
   *
   * @return Value of <code>inbound.payload.success.dir</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getPayloadSuccessDir()
  {
    return getProperty(KEY_PAYLOAD_SUCCESS_DIR, null);
  }

  /**
   * Get the property value specified in the
   * <code>inbound.payload.fail.dir</code> property.
   *
   * @return Value of <code>inbound.payload.fail.dir</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getPayloadFailDir()
  {
    return getProperty(KEY_PAYLOAD_FAIL_DIR, null);
  }

  /**
   * Get the property value specified in the
   * <code>transport.soap.inbound.service.name</code> property.
   *
   * @return Value of  <code>transport.soap.inbound.service.name</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getSoapServiceName()
  {
    return getProperty(KEY_SOAP_INBOUND_SERVICE, null);
  }

  /**
   * Get the property value specified in the
   * <code>transport.soap.inbound.service.port</code> property.
   *
   * @return Value of  <code>transport.soap.inbound.service.port</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getSoapServicePort()
  {
    return getProperty(KEY_SOAP_INBOUND_SERVICE_PORT, null);
  }

  /**
   * Get the property value specified in the
   * <code>transport.soap.inbound.service.op</code> property.
   *
   * @return Value of  <code>transport.soap.inbound.service.op</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getSoapServiceOperation()
  {
    return getProperty(KEY_SOAP_INBOUND_SERVICE_OPERATION, null);
  }

  /**
   * Get the property value specified in the
   * <code>transport.soap.inbound.service.endpoint</code> property.
   *
   * @return Value of  <code>transport.soap.inbound.service.endpoint</code> property.
   * <b>null</b> if the property is not specified.
   */
  public String getSoapServiceEndpointUrl()
  {
    return getProperty(KEY_SOAP_INBOUND_ENDPOINT_URL, null);
  }

  /**
   * Get the property keys of all Header attributes.
   * A Header attribute has key prefix of <code>header.</code>
   *
   * @return Array of property keys for the Header attributes.
   */
  public String[] getHeaderAttributes()
  {
    ArrayList headerAttrs = new ArrayList();
    Enumeration keys = this.keys();
    while (keys.hasMoreElements())
    {
      String key = (String) keys.nextElement();
      if (key.startsWith(KEY_HEADER_ATTR_PREFIX))
        headerAttrs.add(key);
    }
    return (String[]) headerAttrs.toArray(new String[headerAttrs.size()]);
  }

  /**
   * Get the values of all Header attributes.
   *
   * @param headerAttrs Array of the Header attribute property keys.
   * @return Array of values corresponding to each specified Header attribute.
   */
  public String[] getHeaderAttributeValues(String[] headerAttrs)
  {
    String[] values = new String[headerAttrs.length];
    for (int i = 0; i < headerAttrs.length; i++)
    {
      values[i] = getProperty(headerAttrs[i], "");
    System.out.println("Header["+headerAttrs[i]+"]= "+values[i]);
    }
    return values;
  }

  /**
   * Strip off the Header prefix from the Header attribute names.
   *
   * @param headerAttrs Array of header attribute names. It is
   * assumed that the names starts with the <code>header.</code> header
   * prefix.
   * @return Array of header attribute names with header prefix stripped off.
   */
  public String[] stripHeaderPrefix(String[] headerAttrs)
  {
    String[] stripped = new String[headerAttrs.length];
    for (int i=0; i<headerAttrs.length; i++)
    {
      stripped[i] = headerAttrs[i].substring(KEY_HEADER_ATTR_PREFIX.length());
      System.out.println("Stripping for "+headerAttrs[i] + " to "+stripped[i]);
    }

    return stripped;
  }
}