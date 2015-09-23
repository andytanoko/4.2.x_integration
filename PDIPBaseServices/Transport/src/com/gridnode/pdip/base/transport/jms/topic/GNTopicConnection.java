/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNTopicConnection.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Dec 02 2002    Goh Kan Mun                 Created
 * Dec 04 2002    Goh Kan Mun                 Modified - Update CLASS_NAME.
 * Jan 06 2003    Goh Kan Mun                 Modified - To close jndi after use.
 *                                                     - change _configTransport to
 *                                                       protected access.
 *                                                     - store host and port instead
 *                                                       of jndi.
 */
package com.gridnode.pdip.base.transport.jms.topic;

import com.gridnode.pdip.base.transport.exceptions.*;
import com.gridnode.pdip.base.transport.helpers.ITransportConfig;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.util.JNDIFinder;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.*;
import javax.naming.NamingException;
import javax.naming.Context;

/**
 * This class is used to create a simple topic connection to a JMS router.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GNTopicConnection
{
  protected TopicConnection _topicConnection = null;
  private static final String CLASS_NAME = "GNTopicConnection";
  protected String _host = null;
  protected int _port = 0;
  protected String _connectionId = null;
  private static long _timeToLive = 300000l;
  protected static Configuration _configTransport = // Configuration for transport
          ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME);

  /**
   * Constructor
   *
   * @param           host      host IP address or url of the JMS router
   * @param           port      port use by the JMS router.
   * @param           username  login name to the JMS router
   * @param           password  password for login to the JMS router
   *
   * @exception       NamingException                     thrown when encounter a Naming error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   * @exception       InvalidCommInfoException       thrown when the commInfo is invalid.
   * @exception       GNTptWrongConfigException           thrown when the configuration is not correct.
   */
  public GNTopicConnection(String host, int port, String username, String password)
          throws JMSException, NamingException, GNTptWrongConfigException,
                 InvalidCommInfoException
  {
    _host = host;
    _port = port;
    JNDIFinder jndi = getExtJNDIInitialContext(host, port);
    _connectionId = host + ":" + port + ":" + username;
    _topicConnection = createTopicConnection(
                    jndi,
                    username,
                    password
                    );
    jndi.close();
    TptLogger.debugLog(
            CLASS_NAME,
            "Construct",
            "Connection " + _connectionId + " established"
            );
  }

  /**
   * To send the specified message to the specified topic.
   * @param           topicName             name of the destination topic
   * @param           msg                   Serializable message to be sent
   *
   * @exception       GNTptPersistenceConnectionException thrown when encounter a persistence
   *                                                      connection error.
   * @exception       NamingException                     thrown when encounter a Naming error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   */
  public void send(String topicName, Serializable msg)
         throws JMSException, NamingException, InvalidCommInfoException
  {
    if (checkNullEmpty(topicName)) //there has to be a topic name
      throw new InvalidCommInfoException("No topic name specified");
    JNDIFinder jndi = getExtJNDIInitialContext(_host, _port);
    Topic topic = (Topic) jndi.lookup(topicName, Topic.class);
    jndi.close();
    TopicSession topicSession = _topicConnection.createTopicSession(false,
                                  Session.AUTO_ACKNOWLEDGE);
    TopicPublisher topicPublisher = topicSession.createPublisher(null);
    ObjectMessage message = topicSession.createObjectMessage(msg);
    message.setStringProperty("GNSelector", topic.getTopicName() + "x");
    topicPublisher.setTimeToLive(_timeToLive);
    topicPublisher.publish(topic, message);
    topicPublisher.close();
    topicSession.close();
  }

  /**
   * To close this topic connection.
   *
   * @exception       JMSException    thrown when encounter a JMS error.
   */
  public void close() throws JMSException
  {
    _topicConnection.close();
    _topicConnection = null;
    TptLogger.debugLog(
            CLASS_NAME,
            "close",
            "connection " + _connectionId + "closed"
            );
  }

  /**
   * This method will return the JNDI look up object.
   *
   * @param           host      IP address or url of the jndi host
   * @param           portNum   port number of the jndi host
   *
   * @exception       NamingException                     thrown when encounter a Naming error.
   * @exception       InvalidCommInfoException       thrown when the commInfo is invalid.
   */
  protected JNDIFinder getExtJNDIInitialContext(final String host, final int portNum)
            throws InvalidCommInfoException, NamingException
  {
    String provider_url_protocol = _configTransport.getString(
        ITransportConfig.EXTMQ_PROVIDER_URL_PROTOCOL);
    int port = portNum;
    if (port <= 0) // use default port if port is 0 or -ve
      throw new InvalidCommInfoException("Port cannot be 0 or negative:" + port);
    if (checkNullEmpty(host))
      throw new InvalidCommInfoException("Host is " + host);

    int timeout = _configTransport.getInt(
        ITransportConfig.EXTMQ_PROVIDER_URL_TIMEOUT);

    Properties props = new Properties();
    props.put(Context.PROVIDER_URL, provider_url_protocol + "://" + host +
        ":" + port + "/timeout=" + timeout);

    props.put(Context.INITIAL_CONTEXT_FACTORY, _configTransport.getString(
        ITransportConfig.EXTMQ_INITIAL_CONTEXT_FACTORY));
    return new JNDIFinder(props);
  }

  /**
   * This method will create a <code>TopicConnection</code>.
   *
   * @param           jndi      jndi for look up.
   * @param           userName  login name to the JMS router
   * @param           password  password for login to the JMS router
   *
   * @exception       NamingException                     thrown when encounter a Naming error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   * @exception       GNTptWrongConfigException           thrown when the configuration is not correct.
   */
  private TopicConnection createTopicConnection(JNDIFinder jndi, String userName, String password)
          throws GNTptWrongConfigException, NamingException, JMSException
  {
    String tcfName = _configTransport.getString(ITransportConfig.EXTMQ_TOPIC_CONNECTION_FACTORY);
    if (checkNullEmpty(tcfName))
      throw new GNTptWrongConfigException("No topic connection factory configuration");
    TopicConnectionFactory connectionFactory = (TopicConnectionFactory) jndi.lookup(tcfName,
        TopicConnectionFactory.class);
    // establish the connection and send the message
    if (checkNullEmpty(userName))
      return connectionFactory.createTopicConnection(); // Create connection without authentication
    return connectionFactory.createTopicConnection(userName, password);
  }

  /**
   * To check whether the specified <code>String</code> is an empty String or null.
   *
   *@return          true if the specified is null or "", false otherwise.
   */
  private boolean checkNullEmpty(String item)
  {
    return (item == null || item.equals(""));
  }

}
