/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITransportConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * ??? ?? ????    Guo Jianyu             Created
 * Aug 19 2002    Jagadeesh              Modified - Added Topic information for
 *                                       Channel Module.
 * Nov 07 2002    Goh Kan Mun            Modified - Removed unused configuration.
 *                                                - Added subscriber
 * Apr 28 2003    Qingsong               Added: Fields for Http Transport
 * Oct 17 2003    Neo Sok Lay             Added constants for transport handler and
 *                                        commInfo.
 *
 * Dec 25 2003    Jagadeesh              Added: Support for Message-TptHeader Mapping.
 *
 * Feb 26 2004	  Guo Jianyu		         Added TRANSPORT_HTTP_SYNC_TIMEOUT
 * Jan 12 2007    Neo Sok Lay            Added TRANSPORT_HTTP_SERVLET_PROXYFILE.
 * Jan 29 2007    Neo Sok Lay            Rename APPSERVER_TOPIC* constants.
 * May 04 2007    Neo Sok Lay            Change TRANSPORT_HTTPS_TRUSSTORE_NAME to TRANSPORT_HTTPS_TRUSTSTORE_NAME,
 *                                              transport.https.trusstore.name to transport.https.truststore.name
 */

package com.gridnode.pdip.base.transport.helpers;

public interface ITransportConfig
{
  public static final String CONFIG_NAME = "transport";
  
  public static final String SOAP_SERVICES_CONFIG_NAME =
    "transport.soap.services";

  //value type: String
  public static final String SCHEME = "scheme";

  //value type: String
  public static final String APPSERVER_INITIAL_CONTEXT_FACTORY =
    "appserver.jndi.initial_context_factory";
  //value type: String
  public static final String APPSERVER_PROVIDER_URL =
    "appserver.jndi.provider_url";
  //value type: a colon-separated list of Strings
  public static final String APPSERVER_URL_PKG_PREFIXES =
    "appserver.jndi.url_pkg_prefixes";
  //value type: String
  public static final String APPSERVER_CONNECTION_FACTORY =
    "appserver.jndi.connection_factory";
  //value type: String
  public static final String APPSERVER_DESTINATION_APP_TO_BRIDGE =
    "appserver.jndi.destination.app_to_bridge";
  //value type: String
  public static final String APPSERVER_DESTINATION_BRIDGE_TO_APP =
    "appserver.jndi.destination.bridge_to_app";
  //value type: String
  public static final String APPSERVER_DESTINATION_TRANSPORT_FEEDBACK =
    "appserver.jndi.destination.transport_feedback";

  /**
   * Handler for particular protocol type. To be appended by the protocol
   * type before obtaining the handler class.
   */
  public static final String HANDLER_PREFIX = "handler.";

  /**
   * CommInfo implementation for particular protocol type. To be appended
   * by the protocol type before obtaining the implementation class name.
   */
  public static final String COMMINFO_PREFIX = "comminfo.";

  //value type: String
  public static final String EXTMQ = "extmq";
  //value type: String
  public static final String EXTMQ_VERSION = "extmq.version";

  //value type: String
  public static final String EXTMQ_PROVIDER_URL_PROTOCOL =
    "extmq.jndi.provider_url.protocol";
  //value type: String
  public static final String EXTMQ_PROVIDER_URL_TIMEOUT =
    "extmq.jndi.provider_url.timeout";
  //value type: String
  public static final String EXTMQ_INITIAL_CONTEXT_FACTORY =
    "extmq.jndi.initial_context_factory";
  //value type: String
  public static final String EXTMQ_TOPIC_CONNECTION_FACTORY =
    "extmq.jndi.topic_connection_factory";
  //value type: String
  public static final String EXTMQ_TOPIC_SUBSCRIBER_NOLOCAL =
    "extmq.jndi.topic_subscriber_nolocal";

  public static final String TRANSPORT_HTTP_SERVLET_USE_CONFIGFILE =
    "transport.http.servlet.use_configfile";
  //value type: boolean
  public static final String TRANSPORT_HTTP_SERVLET_CONFIGFILE =
    "transport.http.servlet.configfile";
  //value type: String
  public static final String TRANSPORT_HTTP_SERVLET_USERNAME =
    "transport.http.servlet.username";
  //value type: String
  public static final String TRANSPORT_HTTP_SERVLET_PASSWORD =
    "transport.http.servlet.passwd";
  //value type: String
  public static final String TRANSPORT_HTTP_SERVLET_LOGFILE =
    "transport.http.servlet.logfile";
  //value type: String
  public static final String TRANSPORT_HTTP_SERVLET_LOGHEADER =
    "transport.http.servlet.logheader";
  //value type: boolean
  public static final String TRANSPORT_HTTP_SERVLET_LOGLEVEL =
    "transport.http.servlet.loglevel";
  //value type: string
  public static final String TRANSPORT_HTTP_SENDER_URL =
    "transport.http.sender.url";
  /*NSL20070112 Replaced by proxy config file.
  //value type: String
  public static final String TRANSPORT_HTTP_HTTP_PROXY_USERNAME =
    "transport.http.http_proxy_username";
  //value type: String
  public static final String TRANSPORT_HTTP_HTTP_PROXY_PASSWORD =
    "transport.http.http_proxy_password";
  //value type: String
  public static final String TRANSPORT_HTTP_HTTP_PROXY_PAC =
    "transport.http.http_proxy_pac";
  //value type: String
  public static final String TRANSPORT_HTTP_HTTP_PROXY_URL =
    "transport.http.http_proxy_url";
  //value type: String
  public static final String TRANSPORT_HTTP_HTTP_PROXY_PORT =
    "transport.http.http_proxy_port";
  */  
  public static final String TRANSPORT_HTTP_SERVLET_PROXYFILE =
    "transport.http.servlet.proxyfile";

  //value type: String
  public static final String TRANSPORT_HTTPS_TRUSTSTORE_EXPORT_HANDLER =
    "transport.https.truststore.export.handler";
  //value type: string
  public static final String TRANSPORT_HTTPS_KEYSTORE_EXPORT_HANDLER =
    "transport.https.keystore.export.handler";
  //value type: string
  public static final String TRANSPORT_HTTPS_AUTHOR_SERVER =
    "transport.https.author.server";
  //value type: boolean
  public static final String TRANSPORT_HTTPS_AUTHOR_CLIENT =
    "transport.https.author.client";
  //value type: boolean
  public static final String TRANSPORT_HTTPS_VERIFY_SERVER =
    "transport.https.verify.server";
  //value type: boolean
  public static final String TRANSPORT_HTTPS_KEYSTORE_NAME =
    "transport.https.keystore.name";
  //value type: String
  public static final String TRANSPORT_HTTPS_KEYSTORE_PASSWORD =
    "transport.https.keystore.password";
  //value type: String
  public static final String TRANSPORT_HTTPS_TRUSTSTORE_NAME =
    "transport.https.truststore.name";
  //value type: String
  public static final String TRANSPORT_HTTPS_TRUSTSTORE_PASSWORD =
    "transport.https.truststore.password";
  //value type: String

  public static final String TRANSPORT_HTTP_EMAIL_ALERT_ISENABLE =
    "transport.http.email_alert.isenable";
  //value type: boolean
  public static final String TRANSPORT_HTTP_EMAIL_ALERT_SERVER =
    "transport.http.email_alert.server";
  //value type: string
  public static final String TRANSPORT_HTTP_EMAIL_ALERT_RECEIVER =
    "transport.http.email_alert.receiver";
  //value type: string
  public static final String TRANSPORT_HTTP_EMAIL_ALERT_SENDER =
    "transport.http.email_alert.sender";
  //value type: string
  public static final String TRANSPORT_HTTP_EMAIL_ALERT_SUBJECT =
    "transport.http.email_alert.subject";
  //value type: string
  public static final String TRANSPORT_HTTP_EMAIL_ALERT_CONTENT =
    "transport.http.email_alert.content";
  //value type: string
  public static final String TRANSPORT_HTTP_EMAIL_ALERT_ATTACHMENT =
    "transport.http.email_alert.attachment";
  //value type: int
  public static final String TRANSPORT_HTTP_EMAIL_ALERT_ATTACHMENT_NAME =
    "transport.http.email_alert.attachment_name";
  //value type: string
  public static final String TRANSPORT_HTTP_SYNC_TIMEOUT =
    "transport.http.sync.timeout";
  //value type: int
  
  //function types of msg received by bridge from appserver MQ
  public static final int SEND = 1; //a normal send
  public static final int CONNECT = 2; //set up a permanent connection
  public static final int CONNECT_AND_LISTEN = 3;
  //set up a permanent connection and
  //listen to a topic
  public static final int DISCONNECT = 4; //close a permanent connection

  //    //the following are constants defined for feedback messages
  //    public static final int UNDEFINED = -9999;
  //    //errors
  //    public static final int NULL_MESSAGE_ERR = 1000;
  //    public static final int NULL_OBJ_MESSAGE_ERR = 1001;
  //    public static final int NO_DESTINATION_ERR = 1002;
  //    public static final int NO_TOPIC_CONNECTION_FACTORY_CONFIG_ERR = 1003;
  //    public static final int WRONG_MESSAGE_TYPE_ERR = 1004;
  //    public static final int EXCEPTION_ERR = 1005;
  //    public static final int NO_HOST_ERR = 1006;
  //
  //    //status
  //    public static final int MESSAGE_SENT = 1;
  //    public static final int CONNECTION_SETUP = 2;
  //    public static final int CONNECTION_CLOSED = 3;
  //

 public static final String HEADER_MAPPING_PREFIX = "header.mapping.";
}