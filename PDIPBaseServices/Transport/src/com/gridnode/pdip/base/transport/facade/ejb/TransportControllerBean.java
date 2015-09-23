/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransportControllerBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * ??? ?? 2002    Jagadeesh, Jianyu       Created
 * Jun 21 2002    Kan Mun                 Modified - Throw the appropriate exception when error
 *                                                 - remove the feedback queue (for this layer as
 *                                                   it should throws exception directly back)
 * Oct 20 2002    Kan Mun                 Modified - Remove register/deregister listener methods.
 *                                                 - To cater for backward compatible.
 *                                                 - Refactor - remove unwanted process.
 * Dec 02 2002    Goh Kan Mun             Modified - To add a header for connect and connectAndListen
 *                                                   methods so as to inform the BL/Channel when
 *                                                   the persistence connection fails after connected.
 * Dec 03 2002    Goh Kan Mun             Modified - Removed unnecessary import classes.
 * Dec 11 2002    Goh Kan Mun             Modified - Change to use HandlerFactory.
 * Jan 07 2003    Goh Kan Mun             Modified - To close JNDIFinder after use (internally).
 */
package com.gridnode.pdip.base.transport.facade.ejb;


public class TransportControllerBean //implements SessionBean
{
/*  private SessionContext g_sessionContext;
  private static String CLASS_NAME = "TransportControllerBean";
  // Configuration for transport
  private Configuration configTransport = null;
  private TopicConnection appMQConnection = null;
  private TopicSession feedbackSession = null;
  private TopicSession sendSession = null;
  private TopicPublisher feedbackPublisher = null;
  private TopicPublisher sendPublisher = null;

  public void send(ICommInfo commInfo, Hashtable header, String dataToSend[],
         byte[] fileData)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTransportException
  {
    try
    {
      TptLogger.debugLog(CLASS_NAME, "send", "Sending...");
      new Hashtable();
      ITransportHandler handler = TransportHandlerFactory.getTransportHandler(commInfo.getProtocolType());
      handler.send(commInfo, header, dataToSend, fileData);
    }
    catch (InvalidCommInfoException ic)
    {
      throw ic;
    }
    catch (InvalidProtocolException ip)
    {
      throw ip;
    }
    catch (GNTransportException tpte)
    {
      throw tpte;
    }
    catch (Exception e)
    {
      throw new GNTransportException("Unable to send message", e);
    }
  }

  public void connect(ICommInfo commInfo, String[] header)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException

  {
    TptLogger.debugLog(CLASS_NAME, "connect", "connecting...");
    ITransportHandler handler = TransportHandlerFactory.getTransportHandler(commInfo.getProtocolType());
    handler.connect(commInfo, header);
  }

  public void connectAndListen(ICommInfo commInfo, String[] header)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException
  {
    TptLogger.debugLog(CLASS_NAME, "connectAndListen", "connecting...");
    ITransportHandler handler = TransportHandlerFactory.getTransportHandler(commInfo.getProtocolType());
    handler.connectAndListen(commInfo, header);
  }

  public void disconnect(ICommInfo commInfo)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException
  {
    TptLogger.debugLog(CLASS_NAME, "disconnect", "disconnecting...");
    ITransportHandler handler = TransportHandlerFactory.getTransportHandler(commInfo.getProtocolType());
    handler.disconnect(commInfo);
  }

  public void ejbCreate() throws CreateException
  {
    TptLogger.debug("ejbCreate", "start");

    //get the transport configuration object
    ConfigurationManager mgr = ConfigurationManager.getInstance();
    this.configTransport = mgr.getConfig(ITransportConfig.CONFIG_NAME);

    //create appMQConnection
    TptLogger.debug("ejbCreate", "Transport Configuration retrieved.");
    JNDIFinder jndi = null;
    try
    {
      Properties props = new Properties();
      props.put(Context.INITIAL_CONTEXT_FACTORY, configTransport.getString(
          ITransportConfig.APPSERVER_INITIAL_CONTEXT_FACTORY));
      props.put(Context.PROVIDER_URL, configTransport.getString(
          ITransportConfig.APPSERVER_PROVIDER_URL));
      props.put(Context.URL_PKG_PREFIXES, configTransport.getString(
          ITransportConfig.APPSERVER_URL_PKG_PREFIXES));
      jndi = new JNDIFinder(props);
      //JNDIService l_jndiservice = JNDIService.getInstance();
      TptLogger.debug("ejbCreate", "JNDI created.");
      String tcfName = configTransport.getString(ITransportConfig.APPSERVER_TOPIC_CONNECTION_FACTORY);
      TopicConnectionFactory tcf = (TopicConnectionFactory)jndi.lookup(
          tcfName, TopicConnectionFactory.class);
      TptLogger.debug("ejbCreate", "TransportControllerBean Conntection Established  ... ");

      String topicName = configTransport.getString(
          ITransportConfig.APPSERVER_TOPIC_APP_TO_BRIDGE);
      Topic sendTopic = (Topic)jndi.lookup(topicName, Topic.class);
      TptLogger.debug("ejbCreate", "Found Topic  ... ");

      appMQConnection = tcf.createTopicConnection();

      sendSession = appMQConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
      sendPublisher = sendSession.createPublisher(sendTopic);
    }
    catch(Exception e)
    {
      TptLogger.debug("ejbCreate", "Exception", e);
      throw new CreateException(e.getMessage());
    }
    finally
    {
      closeJNDIFinder(jndi);
    }
  }

  public void ejbRemove()
  {
    TptLogger.debug("ejbRemove", "TransportControllerBean: in ejbRemove");

    //close the jms connection
    try
    {
      if (sendPublisher != null)
        sendPublisher.close();
      if (sendSession != null)
        sendSession.close();
      if (appMQConnection != null)
        appMQConnection.close();
    }
    catch(JMSException e)
    {
      TptLogger.debug("ejbRemove",
          "Exception encountered when closing application MQ connection", e);
    }
    finally
    {
      appMQConnection = null;
      sendSession = null;
      sendPublisher = null;
    }
  }

  public void ejbActivate()
  {
    TptLogger.debug("ejbActivate", "TransportControllerBean: in ejbActivate");
  }

  public void ejbPassivate()
  {
    TptLogger.debug("ejbPassivate", "TransportControllerBean: in ejbPassivate");
  }

  public void setSessionContext(SessionContext p_scx)
  {
    this.g_sessionContext = p_scx;
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
      TptLogger.errorLog(CLASS_NAME,
              "closeJNDIFinder",
              "Unable to close JNDIFinder, wait for garbage collector...",
              ne
              );
    }
  }
*/
}
