/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNJMSController.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * ??? ?? 2002    Jagadeesh, Jianyu       Created
 * Jun 21 2002    Goh Kan Mun             Modified - include the TPTLogger.
 * Nov 07 2002    Goh Kan Mun             Modified - class name.
 *                                                 - To keep track of persistence connections
 *                                                   and listeners.
 * Dec 02 2002    Goh Kan Mun             Modified - Change name from GNSMQBridgeController
 *                                                   to GNJMSController
 * Dec 03 2002    Goh Kan Mun             Modified - Remove sendFeedbackToApp(boolean,
 *                                                          String, GNTransportInfo)
 *                                                 - Change sendMessageToApp parameter.
 * Dec 04 2002    Goh Kan Mun             Modified - Update CLASS_NAME.
 *                                                 - Put back sendFeedbackToApp(boolean,
 *                                                          String, GNTransportInfo)
 *                                                 - Modified closeAllPersistenceConnections.
 * Jan 07 2003    Goh Kan Mun             Modified - Use JNDIFinder instead of InitialContext.
 *                                                 - Close JNDIFinder after use.
 * May 03 2004    Neo Sok Lay             If reusing persistent connection, do not add the listener
 *                                        if already exists for the topic.
 * Nov 21 2005    Neo Sok Lay             Lookup appserver topic/queue connection factory using
 *                                        common jndi.properties instead of transport.properties      
 * Jan 29 2007    Neo Sok Lay             Change Topic to Queue for Appserver JMS                                                                        
 */
package com.gridnode.pdip.base.transport.jms;

import com.gridnode.pdip.base.transport.exceptions.*;
import com.gridnode.pdip.base.transport.comminfo.IJMSCommInfo;
import com.gridnode.pdip.base.transport.helpers.*;
import com.gridnode.pdip.base.transport.jms.topic.GNPersistenceTopicConnection;
import com.gridnode.pdip.base.transport.jms.topic.GNTopicConnection;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.util.JNDIFinder;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.*;
import javax.naming.NamingException;

/**
 * This is a Singleton class that is used to stores persistence connections
 * and the JMSHandler invoke to do the actual connect, connectAndListen, send
 * and disconnect.
 *
 * @author Goh Kan Mun
 *
 * @version GT 2.3.3
 * @since 2.0
 */

public class GNJMSController
{
  private static Hashtable persistenceConnections = new Hashtable();
  private static GNJMSController instance = null;
  private static final String CLASS_NAME = "GNJMSController";
  private static long _timeToLive = 300000l;

  private static Configuration _configTransport = // Configuration for transport
          ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME);

  private static Connection _appMQConnection = null;

  /**
   * Constructor
   *
   * @exception       GNTransportException            thrown when the exception encountered.
   * @exception       GNTptWrongConfigException       thrown when the configuration is not correct.
   */
  private GNJMSController() throws GNTransportException, GNTptWrongConfigException
  {
    try
    {
      openAppConnection();
    }
    catch (JMSException jmse)
    {
      throw new GNTransportException(jmse);
    }
    catch (NamingException ne)
    {
      throw new GNTransportException(ne);
    }
  }

  /**
   * Close connection to application JMS.
   *
   * @exception       JMSException  thrown when a JMS error occurs.
   */
  private void closeAppConnection() throws JMSException
  {
    if (_appMQConnection == null)
    {
      TptLogger.debugLog(CLASS_NAME,
          "closeAppConnection",
          "Connection to Application is already closed");
      return; //already closed
    }
    TptLogger.debugLog(CLASS_NAME,
        "closeAppConnection",
        "Attempt to close connection to Application");
    _appMQConnection.close();
    _appMQConnection = null;
    TptLogger.debugLog(CLASS_NAME,
        "closeAppConnection",
        "Close connection to Application successful");
  }

  /**
   * This method is supposed to be called before this instance is removed.
   */
  protected void finalize()
  {
    TptLogger.debugLog(CLASS_NAME, "finalize", "");
    try
    {
      closeAppConnection();
      closeAllPersistenceConnections();
    }
    catch(JMSException e)
    {
        TptLogger.debugLog(CLASS_NAME,
            "finalize",
            "Exception",
            e);
    }
  }

  /**
   * This method is invoked to close all Persistence connections stored in this class.
   */
  private void closeAllPersistenceConnections()
  {
    Object[] connectionList = persistenceConnections.entrySet().toArray();
    IGNJMSPersistenceConnection connection = null;
    for (int i = 0; i < connectionList.length; i++)
    {
      try
      {
        connection = (IGNJMSPersistenceConnection) connectionList[i];
        if (connection != null)
          connection.close();
      }
      catch(JMSException e)
      {
        TptLogger.debugLog(CLASS_NAME,
            "closePersistenceExtConnections",
            "Exception",
            e);
      }
    }
    persistenceConnections.clear();
  }

  /**
   * Open connection to application JMS.
   *
   * @exception       JMSException      thrown when a JMS error occurs.
   * @exception       NamingException   thrown when encounter a Naming error.
   */
  private void openAppConnection() throws NamingException, JMSException
  {
    JNDIFinder jndi = null;
    try
    {
      if (_appMQConnection != null) // already open
      {
        TptLogger.debugLog(CLASS_NAME,
            "openAppConnection",
            "Connection to Application is already open");
        return;
      }
      TptLogger.debugLog(CLASS_NAME,
          "openAppConnection",
          "Attempt to open connection to Application");
      jndi = getAppJNDIInitialContext();
      String cfName = _configTransport.getString(
                ITransportConfig.APPSERVER_CONNECTION_FACTORY);
      ConnectionFactory cf = (ConnectionFactory) jndi.lookup(
                                      cfName, ConnectionFactory.class);
      _appMQConnection = cf.createConnection();
      TptLogger.debugLog(CLASS_NAME,
          "openAppConnection",
          "Open connection to Application successful");
    }
    finally
    {
      closeJNDIFinder(jndi);
    }
  }

  /**
   * Returns an instance of this class.
   *
   * @exception       GNTransportException            thrown when the exception encountered.
   * @exception       GNTptWrongConfigException       thrown when the configuration is not correct.
   */
  public static synchronized GNJMSController getInstance()
         throws GNTransportException, GNTptWrongConfigException
  {
    if (instance == null)
      instance = new GNJMSController();
    return instance;
  }

  /**
   * To retrieve the jndi for the application JMS router.
   *
   * @exception       NamingException   thrown when encounter a Naming error.
   */
  private JNDIFinder getAppJNDIInitialContext() throws NamingException
  {
    return new JNDIFinder(getAppProperties());
  }

  /**
   * To retrieve the properties for the application JMS router.
   *
   * @exception       NamingException   thrown when encounter a Naming error.
   */
  private Properties getAppProperties()
  {
  	//NSL20051121 Use jndi properties from single location
  	return ConfigurationManager.getInstance().getConfig(IFrameworkConfig.FRAMEWORK_JNDI_CONFIG).getProperties();
  	/*
    Properties props = new Properties();
    props.put(Context.INITIAL_CONTEXT_FACTORY,
              _configTransport.getString(ITransportConfig.APPSERVER_INITIAL_CONTEXT_FACTORY));
    props.put(Context.PROVIDER_URL,
              _configTransport.getString(ITransportConfig.APPSERVER_PROVIDER_URL));
    props.put(Context.URL_PKG_PREFIXES,
              _configTransport.getString(ITransportConfig.APPSERVER_URL_PKG_PREFIXES));
    return props;
    */
  }

  /**
   * To send a feedback message to the application JMS router.
   *
   * @param           success      whether the operation is successful.
   * @param           desc         the description to be passed to the Channel/BL.
   *//*
  private void sendFeedbackToApp(boolean success, String desc)
  {
    Feedback msgObj = new Feedback(success,desc);
    sendFeedbackToApp(msgObj);
  }*/

  /**
   * To send a feedback message to the application JMS router.
   *
   * @param           success      whether the operation is successful.
   * @param           desc         the description to be passed to the Channel/BL.
   */
  public void sendFeedbackToApp(boolean success, String desc, String[] header)
  {
    Feedback msgObj = new Feedback(success, desc, header);
    sendFeedbackToApp(msgObj);
  }

  /**
   * To send a feedback message to the application JMS router.
   *
   * @param           msgObj       the feedback message to the BL/Channel.
   */
  private void sendFeedbackToApp(Feedback msgObj)
  {
    JNDIFinder jndi = null;
    try
    {
      TptLogger.debugLog(CLASS_NAME, "sendFeedbackToApp", "in sendfeedbackmsg, msgObj is " + msgObj.toString());
      jndi = getAppJNDIInitialContext();
      String destName = _configTransport.getString(ITransportConfig.APPSERVER_DESTINATION_TRANSPORT_FEEDBACK);
      if (checkNullEmpty(destName))
        throw new GNTptWrongConfigException("No destination name specified");
      Destination dest = (Destination) jndi.lookup(destName, Destination.class);
      sendMessage(_appMQConnection, dest, destName, msgObj);
    }
    catch (Throwable t)
    {
      TptLogger.errorLog(ILogErrorCodes.TRANSPORT_FEEDBACK_SEND,
                         CLASS_NAME, "sendFeedbackToApp", "Error while sending feedback to application: "+t.getMessage(), t);
    }
    finally
    {
      closeJNDIFinder(jndi);
    }
  }

  /**
   * To send a message to the application JMS router.
   *
   * @exception       GNTransportException            thrown when the exception encountered.
   * @exception       GNTptWrongConfigException       thrown when the configuration is not correct.
   */
  public void sendMessageToApp(GNTransportPayload infoObject)
            throws GNTransportException, GNTptWrongConfigException
  {
    JNDIFinder jndi = null;
    try
    {
      TptLogger.debugLog(CLASS_NAME, "sendMessageToApp", "infoObject is " + infoObject.toString());
      jndi = getAppJNDIInitialContext();
      String destName = _configTransport.getString(ITransportConfig.APPSERVER_DESTINATION_BRIDGE_TO_APP);
      if (checkNullEmpty(destName))
        throw new GNTptWrongConfigException("No destination name specified");
      Destination dest = (Destination) jndi.lookup(destName, Destination.class);
      sendMessage(_appMQConnection, dest, destName, infoObject);
    }
    catch (NamingException ne)
    {
      throw new GNTransportException(ne);
    }
    catch (JMSException jmse)
    {
      throw new GNTransportException(jmse);
    }
    finally
    {
      closeJNDIFinder(jndi);
    }
  }

  /**
   * To send message to the specified JMS router specified by the router.
   *
   * @param           commInfo      communication information of the destination
   * @param           payload       the message to be sent.
   *
   * @exception       GNTransportException                 thrown when encounter a non transport error.
   * @exception       GNTptPersistenceConnectionException  thrown when encounter a persistence connection error.
   * @exception       InvalidCommInfoException        thrown when the commInfo is invalid.
   * @exception       GNTptWrongConfigException            thrown when the configuration is not correct.
   */
  public void sendMessage(IJMSCommInfo commInfo, Serializable payload)
            throws GNTransportException,
                   GNTptPersistenceConnectionException,
                   InvalidCommInfoException,
                   GNTptWrongConfigException
  {
    try
    {
      String persistenceConnectionKey = getPersistenceConnectionKey(commInfo);
      TptLogger.debugLog(CLASS_NAME, "sendMessage", "Using connection " + persistenceConnectionKey);

      if (isPersistenceConnection(persistenceConnectionKey))
        sendViaPersistenceConnection(persistenceConnectionKey, commInfo.getDestination(), payload);
      else
        sendViaNonPersistenceConnection(commInfo, payload);
    }
    catch (JMSException jmse)
    {
      throw new GNTransportException("Unable to send message to external Message Queue", jmse);
    }
    catch (NamingException ne)
    {
      throw new GNTransportException("Unable to send message to external Message Queue", ne);
    }
  }

  /**
   * To check whether a persistence connection exist.
   *
   * @param           persistenceConnectionKey  the id of a persistence connection.
   *
   * @return          true if the persistence connection exist.
   */
  private boolean isPersistenceConnection(String persistenceConnectionKey)
  {
    return persistenceConnections.containsKey(persistenceConnectionKey);
  }

  /**
   * To retrieve an existing persistence connection.
   *
   * @param           persistenceConnectionKey  the id of a persistence connection.
   *
   * @return          the persistence connection
   */
  private IGNJMSPersistenceConnection getPersistenceConnection(String persistenceConnectionKey)
  {
    return (IGNJMSPersistenceConnection) persistenceConnections.get(persistenceConnectionKey);
  }

  /**
   * To remove an existing persistence connection.
   *
   * @param           persistenceConnectionKey  the id of a persistence connection.
   *
   * @return          the persistence connection that is removed
   *
   * @exception       GNTptPersistenceConnectionException  thrown when the persistence connection does not exist.
   */
  private IGNJMSPersistenceConnection removePersistenceConnection(String persistenceConnectionKey)
            throws GNTptPersistenceConnectionException
  {
    if (!isPersistenceConnection(persistenceConnectionKey))
      throw new GNTptPersistenceConnectionException("Persistence connection does not exist");
    return (IGNJMSPersistenceConnection) persistenceConnections.remove(persistenceConnectionKey);
  }

  /**
   * To store a persistence connection.
   *
   * @param           persistenceConnectionKey  the id of a persistence connection.
   *
   * @return          the persistence connection that is removed
   *
   * @exception       GNTptPersistenceConnectionException  thrown when the persistence connection does not exist.
   */
  private void storePersistenceConnection(String persistenceConnectionKey,
            IGNJMSPersistenceConnection connection) throws GNTptPersistenceConnectionException
  {
    if (isPersistenceConnection(persistenceConnectionKey))
      throw new GNTptPersistenceConnectionException("Persistence connection already exist");
    persistenceConnections.put(persistenceConnectionKey, connection);
  }

  /**
   * To create a persistence connection.
   *
   * @param           commInfo    communication information for the creation.
   * @param           header      the header passed down by the BL / Channel to be sent back
   *                              when an error occured to this persistence connection
   *
   * @exception       GNTransportException                 thrown when encounter a non transport error.
   * @exception       GNTptPersistenceConnectionException  thrown when encounter a persistence connection error.
   * @exception       InvalidCommInfoException        thrown when the commInfo is invalid.
   * @exception       GNTptWrongConfigException            thrown when the configuration is not correct.
   */
  public IGNJMSPersistenceConnection createPersistenceConnection(
         IJMSCommInfo commInfo, String[] header)
          throws GNTptPersistenceConnectionException,
                 InvalidCommInfoException,
                 GNTptWrongConfigException,
                 GNTransportException
  {
    int destType = commInfo.getDestType();
    String hostname = commInfo.getHost();
    int port = commInfo.getPort();
    String username = commInfo.getUser();
    String password = commInfo.getPassword();
    return createPersistenceConnection(destType, hostname, port, username, password, header);
  }

  /**
   * To create a persistence connection.
   *
   * @param           destType  Destination type. Indication whether the destination is
   *                            a queue or a topic.
   * @param           hostname  host IP address or url of the JMS router
   * @param           port      port use by the JMS router.
   * @param           username  login name to the JMS router
   * @param           password  password for login to the JMS router
   * @param           header    the header passed down by the BL / Channel to be sent back
   *                            when an error occured to this persistence connection
   *
   * @exception       GNTransportException                 thrown when encounter a non transport error.
   * @exception       GNTptPersistenceConnectionException  thrown when encounter a persistence connection error.
   * @exception       InvalidCommInfoException        thrown when the commInfo is invalid.
   * @exception       GNTptWrongConfigException            thrown when the configuration is not correct.
   */
  private IGNJMSPersistenceConnection createPersistenceConnection(int destType,
          String hostname, int port, String username, String password, String[] header)
          throws GNTptPersistenceConnectionException,
                 InvalidCommInfoException,
                 GNTptWrongConfigException,
                 GNTransportException
  {
    IGNJMSPersistenceConnection connection = null;
    try
    {
      String persistenceConnectionKey = getPersistenceConnectionKey(
                                      destType,
                                      hostname,
                                      port,
                                      username
                                      );
      if (isPersistenceConnection(persistenceConnectionKey))
        return getPersistenceConnection(persistenceConnectionKey);
      if (destType == IJMSCommInfo.TOPIC)
      {
        connection = new GNPersistenceTopicConnection(
                                 hostname,
                                 port,
                                 username,
                                 password,
                                 header
                                 );
      }
      else if (destType == IJMSCommInfo.QUEUE)
        throw new InvalidCommInfoException("Queue not supported.");
      else
        throw new InvalidCommInfoException("Invalid destination type:" + destType);
      connection.setExceptionListener(persistenceConnectionKey);
      storePersistenceConnection(persistenceConnectionKey, connection);
      TptLogger.debugLog(CLASS_NAME, "createPersistenceConnect",
                                     "Successfully create to " + persistenceConnectionKey);
      return connection;
    }
    catch (JMSException jmse)
    {
      try
      {
        if (connection != null)
          connection.close();
      }
      catch (JMSException jmse2)
      {
        TptLogger.warnLog(CLASS_NAME, "createPersistenceConnect",
                                     "Unable to close connection.",
                                     jmse2);
      }
      throw new GNTransportException("Unable to create persistence connection", jmse);
    }
    catch (NamingException ne)
    {
      try
      {
        if (connection != null)
          connection.close();
      }
      catch (JMSException jmse2)
      {
        TptLogger.warnLog(CLASS_NAME, "createPersistenceConnect",
                                     "Unable to close connection.",
                                     jmse2);
      }
      throw new GNTransportException("Unable to create persistence connection", ne);
    }
  }

  /**
   * To create a persistence listener.
   *
   * @param           commInfo    communication information for the creation of the listener.
   * @param           header      the header passed down by the BL / Channel to be sent back
   *                              when an error occured to this persistence connection
   *
   * @exception       GNTransportException                 thrown when encounter a non transport error.
   * @exception       GNTptPersistenceConnectionException  thrown when encounter a persistence connection error.
   * @exception       InvalidCommInfoException        thrown when the commInfo is invalid.
   * @exception       GNTptWrongConfigException            thrown when the configuration is not correct.
   */
  public void createPersistenceListener(IJMSCommInfo commInfo, String[] header)
          throws InvalidCommInfoException, GNTptPersistenceConnectionException,
                 GNTptWrongConfigException, GNTransportException
  {
    try
    {
      String destination = commInfo.getDestination();
      String hostname = commInfo.getHost();
      TptLogger.debugLog(CLASS_NAME,
                         "createPersistenceListener",
                         "start listen to " + hostname + " on Destination "
                             + destination
                         );
      //set up a permanent connection and listen to a topic/queue
      //if the connection is already there, simply start the listener
      if (checkNullEmpty(destination)) //there has to be a topic name
        throw new InvalidCommInfoException("No topic name specified");
      IGNJMSPersistenceConnection connection = createPersistenceConnection(commInfo, header);
      //040503NSL: check if reusing the connection, don't add the listener if already listening
      if (!connection.containListener(destination)) 
        connection.addListener(destination);
      TptLogger.debugLog(CLASS_NAME, "createPersistenceListener",
                                     "success listen to " + hostname + " on Destination "
                                         + destination);
    }
    catch (JMSException jmse)
    {
      throw new GNTransportException("Unable to create persistence listener", jmse);
    }
    catch (NamingException ne)
    {
      throw new GNTransportException("Unable to create persistence listener", ne);
    }
  }

  /**
   * To close a persistence connection.
   *
   * @param           commInfo    communication information for the creation.
   *
   * @exception       GNTransportException                 thrown when encounter a non transport error.
   * @exception       GNTptPersistenceConnectionException  thrown when encounter a persistence connection error.
   */
  public void closePersistenceConnection(IJMSCommInfo commInfo)
         throws GNTptPersistenceConnectionException, GNTransportException
  {
    try
    {
      String persistenceConnectionKey = getPersistenceConnectionKey(commInfo);
      TptLogger.debugLog(CLASS_NAME, "closePersistenceConnection",
                                     "Removing connection to " + persistenceConnectionKey);
      if (persistenceConnections.containsKey(persistenceConnectionKey))
      {
        IGNJMSPersistenceConnection connection = removePersistenceConnection(persistenceConnectionKey);
        connection.close();
        TptLogger.debugLog(CLASS_NAME, "closePersistenceConnection",
                                     "Successfully remove connection to " + persistenceConnectionKey);
      }
      else
        throw new GNTptPersistenceConnectionException("Connection not found.");
    }
    catch (JMSException jmse)
    {
      throw new GNTransportException("Unable to close persistence connection", jmse);
    }
  }

  /**
   * Return the Persistence Connection key.
   * @param           destType  Destination type. Indication whether the destination is
   *                            a queue or a topic.
   * @param           host      host IP address or url of the JMS router
   * @param           port      port use by the JMS router.
   * @param           username  login name to the JMS router
   *
   * @return          the Persistence Connection key.
   */
  private String getPersistenceConnectionKey(int destType, String host, int port, String username)
  {
    return destType + ":" + host + ":" + port + ":" + username;
  }

  /**
   * Return the Persistence Connection key.
   * @param           commInfo  communication information for the persistence connection
   *
   * @return          the Persistence Connection key.
   */
  private String getPersistenceConnectionKey(IJMSCommInfo commInfo)
  {
    return getPersistenceConnectionKey(
           commInfo.getDestType(),
           commInfo.getHost(),
           commInfo.getPort(),
           commInfo.getUser()
           );
  }

  /**
   * To check whether the specified <code>String</code> is an empty String or null.
   *
   * @return          true if the specified is null or "", false otherwise.
   */
  private boolean checkNullEmpty(String item)
  {
    return (item == null || item.equals(""));
  }

  /**
   * To send a message to the specified destination.
   * @param           connection     the <code>Connection</code> used to do the send
   * @param           dest               the destination
   * @param           msg                 the <code>Serializable</code> message to be sent.
   */
  private void sendMessage(Connection connection, Destination dest, String destName,
         Serializable msg) throws JMSException
  {
    Session session = connection.createSession(false,
                                  Session.AUTO_ACKNOWLEDGE);
    MessageProducer producer = session.createProducer(null);
    ObjectMessage message = session.createObjectMessage(msg);
    message.setStringProperty("GNSelector", destName + "x");
    producer.setTimeToLive(_timeToLive);
    producer.send(dest, message);
    producer.close();
    session.close();
  }

  /**
   * This method is invoked when the persistence connection encounters an error.
   *
   * @param           connectionKey   the key for the persistence connection
   * @param           message         the message from the JMS layer.
   */
  public void handlePersistenceConnectionException(String connectionKey, String message)
  {
    try
    {
      if (isPersistenceConnection(connectionKey))
      {
        IGNJMSPersistenceConnection connection = removePersistenceConnection(connectionKey);
        connection.close();
        Feedback feedback = new Feedback(false, message, connection.getHeader());
        sendFeedbackToApp(feedback);
      }
      else
      {
        TptLogger.errorLog(ILogErrorCodes.TPT_JMS_CTLR_CONN_EXCEPTION_HANDLE,
                           CLASS_NAME,
                           "handlePersistenceConnectionException",
                           "Not a persistence connection: " + connectionKey + ", message = " + message
                           );
      }
    }
    catch (JMSException jmse)
    {
      TptLogger.errorLog(ILogErrorCodes.TPT_JMS_CTLR_CONN_EXCEPTION_HANDLE,
                         CLASS_NAME,
                         "handlePersistenceConnectionException",
                         "Unable to close JMS connection: "+jmse.getMessage(),
                         jmse
                         );
    }
    catch (GNTptPersistenceConnectionException pce)
    {
      TptLogger.errorLog(ILogErrorCodes.TRANSPORT_PERSISTANCE_INVOKE,
                         CLASS_NAME,
                         "handlePersistenceConnectionException",
                         "Unable to remove persistance connection: "+pce.getMessage(),
                         pce
                         );
    }
  }

  /**
   * This method is invoked to send the message via the Persistence Connection.
   * @param           persistenceConnectionKey  key for the persistence connection.
   * @param           destination               Topic / Queue name of the destination.
   * @param           payload                   the message to be sent.
   *
   * @exception       GNTptPersistenceConnectionException  thrown when encounter a persistence connection error.
   * @exception       InvalidCommInfoException        thrown when the commInfo is invalid.
   * @exception       JMSException      thrown when a JMS error occurs.
   * @exception       NamingException   thrown when encounter a Naming error.
   */
  private void sendViaPersistenceConnection(
         String persistenceConnectionKey, String destination, Serializable payload)
         throws InvalidCommInfoException, GNTptPersistenceConnectionException,
                JMSException, NamingException
  {
    IGNJMSPersistenceConnection connection = getPersistenceConnection(persistenceConnectionKey);
    connection.send(destination, payload);
  }

  /**
   * This method is invoked to send the message via the non persistence Connection.
   * @param           commInfo          communication information for the connection
   * @param           payload           the message to be sent.
   *
   * @exception       GNTptPersistenceConnectionException  thrown when encounter a persistence connection error.
   * @exception       InvalidCommInfoException        thrown when the commInfo is invalid.
   * @exception       JMSException      thrown when a JMS error occurs.
   * @exception       NamingException   thrown when encounter a Naming error.
   */
  private void sendViaNonPersistenceConnection(
         IJMSCommInfo commInfo, Serializable payload)
         throws InvalidCommInfoException, GNTptWrongConfigException,
                JMSException, NamingException
  {
    GNTopicConnection connection = null;
    try
    {
      if (commInfo.getDestType() == IJMSCommInfo.QUEUE)
        throw new InvalidCommInfoException("Queue not supported");
      else if (commInfo.getDestType() == IJMSCommInfo.TOPIC)
      {
        connection = new GNTopicConnection(
                             commInfo.getHost(),
                             commInfo.getPort(),
                             commInfo.getUser(),
                             commInfo.getPassword()
                             );
        connection.send(commInfo.getDestination(), payload);
      }
      else
        throw new InvalidCommInfoException("Invalid destination type:" +
              commInfo.getDestType());
    }
    finally
    {
      if (connection != null)
        connection.close();
    }
  }

  private void closeJNDIFinder(JNDIFinder jndi)
  {
    try
    {
      if (jndi != null)
        jndi.close();
    }
    catch (NamingException ne)
    {
      TptLogger.warnLog(CLASS_NAME,
              "closeJNDIFinder",
              "Unable to close JNDIFinder, wait for garbage collector...",
              ne
              );
    }
  }

}