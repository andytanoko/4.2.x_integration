/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransportServiceBean.java
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
 * Dec 18 2003    Jagadeesh               Modified - To Use new implementation of TransportHandlerFactory,
 *                                                   to wrap, GNXXXException with Exceptions without GN perfix.
 *                                                   This will later be sync with Handlers.
 * Nov 21 2005    Neo Sok Lay             Use JNDIFinder from ServiceLocator instead of creating one.
 * Jan 29 2007    Neo Sok Lay             Remove redundant codes for MQ connection.
 * Mac 01 2007    Tam Wei Xiang           Added in the http response code into the Message
 */
package com.gridnode.pdip.base.transport.facade.ejb;

import java.util.Hashtable;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.*;

import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.exceptions.*;
import com.gridnode.pdip.base.transport.handler.ITransportHandler;
import com.gridnode.pdip.base.transport.handler.TransportHandlerFactory;
import com.gridnode.pdip.base.transport.helpers.ITransportConfig;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class TransportServiceBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -941873551838417351L;
	private SessionContext g_sessionContext;
  private static final String CLASS_NAME = "TransportServiceBean";
  // Configuration for transport
//  private Configuration configTransport = null;
//  private Connection appMQConnection = null;
  //private TopicSession feedbackSession = null;
//  private Session sendSession = null;
  //private TopicPublisher feedbackPublisher = null;
//  private MessageProducer sendProducer = null;

/*  public void send(ICommInfo commInfo, Hashtable header, String dataToSend[],
         byte[] fileData)
         throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTransportException
  {
    try
    {
      TptLogger.debugLog(CLASS_NAME, "send", "Sending...");
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

 */

  public Message send(ICommInfo commInfo, Message message)
    throws InvalidProtocolException,
       InvalidCommInfoException,
       GNTransportException
  {
    try
    {
      TptLogger.debugLog(CLASS_NAME, "send()", "[Send Begin frm Tpt Facade]");
      if (message == null)
        throw new Exception("[Cannot Send a Null Message Object from TptFacade. "+
          "Please provide a valid Message or Not Null Message to Send]");
      //message = preProcessMessageHeaders(commInfo,message);
      ITransportHandler handler = TransportHandlerFactory.getTransportHandler(commInfo.getProtocolType());
      Hashtable header = getMapAsHashtable(message.getCommonHeaders());
      /** @todo Comment it for test purpose ... Later remember to uncomment this */
      header.putAll(getMapAsHashtable(message.getMessageHeaders()));
      handler.send(commInfo,header,message.getData(),message.getPayLoadData());
      
      //TWX 01032007 Get the HTTP response code from HTTPTransportHandler
      putHttpResponseCode(message, header);
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
    return message;

  }
  
  private void putHttpResponseCode(Message message, Hashtable header)
  {
    Integer httpResponseCode = (Integer)header.get(ITransportHandler.HTTP_RESPONSE_CODE);
    if(httpResponseCode != null)
    {
      message.setHttpResponseCode(httpResponseCode);
    }
  }
  
  /*
  private Message preProcessMessageHeaders(ICommInfo commInfo,Message message)
  {
      Map transMessageHeaders = getTransformedMessageHeaders(message,
                                                  commInfo.getProtocolType());
      Map commonHeaders = message.getCommonHeaders();
      //This below check was introduced bcoz of '\n' is not a valid character with Http Headers.
      if (commonHeaders.containsKey(ICommonHeaders.ENCRYPT_CERT_ISSUER_NAME))
        commonHeaders.put(ICommonHeaders.ENCRYPT_CERT_ISSUER_NAME,
            checkRemoveSplChars((String) commonHeaders.get(ICommonHeaders.ENCRYPT_CERT_ISSUER_NAME) )
          );
       if(commonHeaders.containsKey(ICommonHeaders.SIGN_CERT_ISSUER_NAME))
        commonHeaders.put(ICommonHeaders.SIGN_CERT_ISSUER_NAME,
            checkRemoveSplChars((String) commonHeaders.get(ICommonHeaders.SIGN_CERT_ISSUER_NAME) )
          );

      message.setCommonHeaders(commonHeaders);
      message.setMessageHeaders(transMessageHeaders);
      return message;
  }*/

  /**
   * The below algorithm is not a "standard conversion technique", and may not be necessary,
   * in context of GT Messages. Since when transport module is refactored, we intend to seperate
   * message and common headers, which are combined at the time of writing this method.
   * @param issuerName
   * @return
   *//*
  private String checkRemoveSplChars(String issuerName)
  {
      if (issuerName.indexOf('\n')>0)
      {
        TptLogger.debugLog("","checkRemoveSplChars()","[Replacing The Special Chars]");
        return issuerName.replace('\n',' ');
      }
      return issuerName;
  }
  */


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
//    TptLogger.debug("ejbCreate", "start");
//
//    //get the transport configuration object
//    ConfigurationManager mgr = ConfigurationManager.getInstance();
//    this.configTransport = mgr.getConfig(ITransportConfig.CONFIG_NAME);
//
//    //create appMQConnection
//    TptLogger.debug("ejbCreate", "Transport Configuration retrieved.");
//    JNDIFinder jndi = null;
//    try
//    {
//      //NSL20051121 Get the JNDIFinder from ServiceLocator only
//    	jndi = ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getJndiFinder();
//    	/*
//      Properties props = new Properties();
//      props.put(Context.INITIAL_CONTEXT_FACTORY, configTransport.getString(
//          ITransportConfig.APPSERVER_INITIAL_CONTEXT_FACTORY));
//      props.put(Context.PROVIDER_URL, configTransport.getString(
//          ITransportConfig.APPSERVER_PROVIDER_URL));
//      props.put(Context.URL_PKG_PREFIXES, configTransport.getString(
//          ITransportConfig.APPSERVER_URL_PKG_PREFIXES));
//      jndi = new JNDIFinder(props);
//      */
//      //JNDIService l_jndiservice = JNDIService.getInstance();
//      //TptLogger.debug("ejbCreate", "JNDI created.");
//      //NSL20070129 Use generic Destination instead of Queue or Topic
//      String cfName = configTransport.getString(ITransportConfig.APPSERVER_CONNECTION_FACTORY);
//      ConnectionFactory cf = (ConnectionFactory)jndi.lookup(cfName, ConnectionFactory.class);
//      TptLogger.debug("ejbCreate", "TransportServiceBean Conntection Established  ... ");
//
//      String destName = configTransport.getString(
//          ITransportConfig.APPSERVER_DESTINATION_APP_TO_BRIDGE);
//      Destination sendDest = (Destination)jndi.lookup(destName, Destination.class);
//      TptLogger.debug("ejbCreate", "Found Queue  ... ");
//
//      appMQConnection = cf.createConnection();
//
//      sendSession = appMQConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//      sendProducer = sendSession.createProducer(sendDest);
//    }
//    catch(Exception e)
//    {
//      TptLogger.debug("ejbCreate", "Exception", e);
//      throw new CreateException(e.getMessage());
//    }/*NSL20051121 Do not try to manage the life-cycle of the JNDIFinder
//    finally
//    {
//      closeJNDIFinder(jndi);
//    }*/
  }

  public void ejbRemove()
  {
//    TptLogger.debug("ejbRemove", "TransportServiceBean: in ejbRemove");
//
//    //close the jms connection
//    try
//    {
//      if (sendProducer != null)
//        sendProducer.close();
//      if (sendSession != null)
//        sendSession.close();
//      if (appMQConnection != null)
//        appMQConnection.close();
//    }
//    catch(JMSException e)
//    {
//      TptLogger.debug("ejbRemove",
//          "Exception encountered when closing application MQ connection", e);
//    }
//    finally
//    {
//      appMQConnection = null;
//      sendSession = null;
//      sendProducer = null;
//    }
  }

  public void ejbActivate()
  {
    TptLogger.debug("ejbActivate", "TransportServiceBean: in ejbActivate");
  }

  public void ejbPassivate()
  {
    TptLogger.debug("ejbPassivate", "TransportServiceBean: in ejbPassivate");
  }

  public void setSessionContext(SessionContext p_scx)
  {
    this.g_sessionContext = p_scx;
  }
  /*NSL20051121 Not needed anymore
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
  }*/

  private static Hashtable getMapAsHashtable(Map header)
  {
    Hashtable nonNullHeader = new Hashtable();
    if (header == null)
      return nonNullHeader;
    String[] keys = (String[])header.keySet().toArray(new String[]{});
    Object[] values = header.values().toArray();
    for (int i=0;i<keys.length;i++)
    {
      if ( keys[i] != null && values[i] != null)
        nonNullHeader.put(keys[i],values[i]); //Since Hashtable dose not allow Null Keys or Values.
    }
    return nonNullHeader;
  }
  /*
  private Map getTransformedMessageHeaders(Message message,String protocol)
  {
    TptLogger.debug(CLASS_NAME,"[getTransformedMessageHeaders()][Begin]");
    TptMappingRegistry registry = TptMappingFactory.getInstance().getMappingRegistry(protocol);
    if (null == registry)
    {
      TptLogger.debug(CLASS_NAME,"[getTransformedMessageHeaders()][No Transformation Done for Protocol Type]"+protocol);
      return message.getMessageHeaders(); //No transformation Done since no mapping exists.
    }
    else
    {
      String messageType = (String)message.getCommonHeaders().get(ICommonHeaders.PAYLOAD_TYPE);
      TptMapping mapping = registry.getTptMapping(messageType);
      TptLogger.debug(CLASS_NAME,"[getTransformedMessageHeaders()][Transformation Done for Protocol Type]"+protocol);
      if (null == mapping)
      {
        TptLogger.debug(CLASS_NAME,"[getTransformedMessageHeaders()][No Mapping exists for MessageType]"+messageType);
        return message.getMessageHeaders();
      }
      else
        return mapping.getOutBoundMappedHeader(message.getMessageHeaders());
    }
  }*/

}
