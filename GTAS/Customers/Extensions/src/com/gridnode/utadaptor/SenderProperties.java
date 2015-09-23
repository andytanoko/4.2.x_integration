/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SenderProperties.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Neo Sok Lay         Created
 * Jan 14 2004    Neo Sok Lay         Add response.doctype, partner.id, response.send.conf
 */
package com.gridnode.utadaptor;

import com.gridnode.ext.util.ExtProperties;

/**
 * This is a wrapper over a Java properties to provide accessor methods
 * for properties specific to the Sender program.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since GT 2.3 I1
 */
public class SenderProperties extends ExtProperties
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5794046880113589324L;

	/**
   * Property key: payload.file
   */
  public static final String KEY_PAYLOAD_FILE = "payload.file";

  /**
   * Property key: adaptor.dir
   */
  public static final String KEY_ADAPTOR_RUN_DIR = "adaptor.dir";

  /**
   * Property key: adaptor.conf
   */
  public static final String KEY_ADAPTOR_CONF_FILE = "adaptor.conf";

  /**
   * Property key: response.save.dir
   */
  public static final String KEY_RESPONSE_SAVE_DIR = "response.save.dir";

  /**
   * Property key: response.file.suffix
   */
  public static final String KEY_RESPONSE_FILE_SUFFIX = "response.file.suffix";

  /**
   * Property key: response.doctype
   */
  public static final String KEY_RESPONSE_DOC_TYPE = "response.doctype";

  /**
   * Property key: partner.id
   */
  public static final String KEY_PARTNER_ID = "partner.id";

  /**
   * Property key: response.send.conf
   */
  public static final String KEY_RESPONSE_SEND_CONF = "response.send.conf";

  public SenderProperties()
  {
  }

  /**
   * Get the payload.file property value.
   * @return payload.file property value, or <b>null<b> if propery is
   * not specified.
   */
  public String getPayloadFile()
  {
    return getProperty(KEY_PAYLOAD_FILE, null);
  }

  /**
   * Get the adaptor.dir property value.
   * @return adaptor.dir property value, or <b>null<b> if propery is
   * not specified.
   */
  public String getAdaptorRunDir()
  {
    return getProperty(KEY_ADAPTOR_RUN_DIR, null);
  }

  /**
   * Get the adaptor.conf property value.
   * @return adaptor.conf property value, or <b>null<b> if propery is
   * not specified.
   */
  public String getAdaptorConfigFile()
  {
    return getProperty(KEY_ADAPTOR_CONF_FILE, null);
  }

  /**
   * Get the response.save.dir property value.
   * @return response.save.dir property value, or <b>null<b> if propery is
   * not specified.
   */
  public String getResponseSaveDir()
  {
    return getProperty(KEY_RESPONSE_SAVE_DIR, null);
  }

  /**
   * Get the response.file.suffix property value.
   * @return response.file.suffix property value, or <b>null<b> if propery is
   * not specified.
   */
  public String getResponseFileSuffix()
  {
    return getProperty(KEY_RESPONSE_FILE_SUFFIX, null);
  }

  /**
   * Get the response.doctype property value.
   * @return response.doctype property value, or <b>null<b> if propery is
   * not specified.
   */
  public String getResponseDocType()
  {
    return getProperty(KEY_RESPONSE_DOC_TYPE, null);
  }

  /**
   * Get the partner.id property value.
   * @return partner.id property value, or <b>null<b> if propery is
   * not specified.
   */
  public String getPartnerId()
  {
    return getProperty(KEY_PARTNER_ID, null);
  }

  /**
   * Get the response.send.conf property value.
   * @return response.send.conf property value, or <b>null<b> if propery is
   * not specified.
   */
  public String getResponseSendConfig()
  {
    return getProperty(KEY_RESPONSE_SEND_CONF, null);
  }

}