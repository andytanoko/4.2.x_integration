/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 19, 2007   i00107              Use ServiceLocator for JNDI Lookup
 * Jan 30 2007    i00107              Implement generic send to destination.
 * Feb 07 2007		Alain Ah Ming						Use new error codes    
 * Feb 09 2007    Neo Sok Lay         Change to not cache JMS connections.
 * Apr 04 2007    Neo Sok Lay         Allow setting of JMS message properties.                               
 * Apr 04 2007    Neo Sok Lay         Allow setting of JMS message properties.                               
 */
package com.gridnode.pdip.framework.jms;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import javax.jms.*;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * 
 * @author
 * @version GT 4.0 VAN
 * @since
 */
public class JMSSender {
    //public static final int TOPIC_TYPE=1;
    //public static final int QUEUE_TYPE=2;
 
    //public static final String DEFAULT_TOPIC_FACTORY="java:/ConnectionFactory";
    //public static final String DEFAULT_QUEUE_FACTORY="java:/ConnectionFactory";
    public static final String DEFAULT_CONN_FACTORY = "java:/ConnectionFactory";
    public static final String JNDI_SERVICE = "jms.jndiService";
    //private static final String QUEUE_FACTORY = "jms.queueConnFactoryName";
    //private static final String TOPIC_FACTORY = "jms.topicConnFactoryName";
    public static final String CONNECTION_USERNAME = "jms.connection.userName";
    public static final String CONNECTION_PASSWORD = "jms.connection.password";
    public static final String CONN_FACTORY = "jms.connFactoryName";
    public static final String JMS_CONFIG_NAME = "jms.config.name";
    public static final String JMS_DEST_NAME = "jms.dest.name";
    
    //static Hashtable<String,Connection> connHt=new Hashtable<String,Connection>();
    static Hashtable<String,Destination> destHt=new Hashtable<String,Destination>();

    public static void sendMessage(String configName, String destName, Serializable msgObj) throws SystemException
    {
      sendMessage(configName, destName, msgObj, null);
    }
    
    /**
     * Send a JMS Object Message to the specified destination
     * @param configName The name of configuration to get the JNDI lookup details of connection factory.
     * @param destName The destination name
     * @param msgObj The object message content to send
     * @param msgProps Option message properties to sent in the JMS message.
     * @throws SystemException
     */
    public static void sendMessage(String configName, String destName, Serializable msgObj, Hashtable msgProps) throws SystemException
    {
      try
      {
        Connection conn = (Connection)getConnection(configName, destName);
        //NSL20070209 Safe return, null will throw exception from getConnection()
        //if (conn != null)
        //{
          Session session = null;
          try
          {
            session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Destination dest = getDestination(configName, destName);
            MessageProducer producer = session.createProducer(dest);
            ObjectMessage msg = session.createObjectMessage(msgObj);
            
            //NSL20070404 Set the message properties
            if (msgProps != null)
            {
              for (Iterator i=msgProps.keySet().iterator(); i.hasNext(); )
              {
                Object key = i.next();
                msg.setObjectProperty(key.toString(), msgProps.get(key));
              }
            }
            producer.send(msg);
          }
          finally
          {
            /*
            if (session != null)
            {
              session.close();
            }
            */
            try
            {
              conn.close();
            }
            catch (Exception ex)
            {
              
            }
          }
        //}
      }
      catch (SystemException ex)
      {
        throw ex;
      }
      catch (Throwable t)
      {
        Log.log(Log.FRAMEWORK, "[JMSSender.sendMessage] Error ", t);
        throw new SystemException("[JMSSender.sendMessage] Error "+t.getMessage(), t);
      }
    }
    
    public static void deliverJMSMessage(Hashtable<String, String> jmsConfigProps, String destName, Serializable msgObj, Hashtable msgProps) throws SystemException
    {
      try
      {
        Connection conn = (Connection)getConnectionViaConfigProps(jmsConfigProps, destName);
        //NSL20070209 Safe return, null will throw exception from getConnection()
        //if (conn != null)
        //{
          Session session = null;
          try
          {
            session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Destination dest = getDestinationViaConfigProps(jmsConfigProps, destName);
            MessageProducer producer = session.createProducer(dest);
            ObjectMessage msg = session.createObjectMessage(msgObj);
            
            //NSL20070404 Set the message properties
            if (msgProps != null)
            {
              for (Iterator i=msgProps.keySet().iterator(); i.hasNext(); )
              {
                Object key = i.next();
                msg.setObjectProperty(key.toString(), msgProps.get(key));
              }
            }
            producer.send(msg);
          }
          finally
          {
            try
            {
              conn.close();
            }
            catch (Exception ex)
            {
              
            }
          }
        //}
      }
      catch (SystemException ex)
      {
        throw ex;
      }
      catch (Throwable t)
      {
        Log.log(Log.FRAMEWORK, "[JMSSender.deliverJMSMessage] Error ", t);
        throw new SystemException("[JMSSender.deliverJMSMessage] Error "+t.getMessage(), t);
      }
    }
    
    
    @Deprecated
    public static void sendMessageToTopic(String configName,String topicName, Serializable messageObj) throws SystemException  
    {
      /*
        try{
            TopicConnection tConn=(TopicConnection)getConnection(configName,topicName,TOPIC_TYPE);
            if(tConn!=null){
                TopicSession session = null;
                try{
                    session =tConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
                    Topic topic=(Topic)getDestination(configName,topicName);
                    TopicPublisher publisher=session.createPublisher(topic);
                    ObjectMessage msg=session.createObjectMessage((Serializable)messageObj);
                    publisher.publish(msg);
                }finally{
                    if(session!=null)
                        session.close();
                }
            }
        }catch(SystemException ex){
            throw ex;
        }catch(Throwable th){
            Log.log(Log.FRAMEWORK, "[JMSSender.sendMessageToTopic] Error ", th);
            throw new SystemException("[JMSSender.sendMessageToTopic] Error "+th.getMessage(),th);
        }
        */
      sendMessage(configName, topicName, messageObj);
    }

    @Deprecated
    public static void sendMessageToQueue(String configName,String queueName, Serializable messageObj) throws SystemException  
    {
      /*
        try{
            QueueConnection qConn=(QueueConnection)getConnection(configName,queueName,QUEUE_TYPE);
            if(qConn!=null){
                QueueSession session = null;
                try{
                    session =qConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                    Queue queue=(Queue)getDestination(configName,queueName);
                    QueueSender sender=session.createSender(queue);
                    ObjectMessage msg=session.createObjectMessage((Serializable)messageObj);
                    //sender.send(msg);
					sender.send(msg, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, 
						Message.DEFAULT_TIME_TO_LIVE);
                }finally{
                    if(session!=null)
                        session.close();
                }
            }
        }catch(SystemException ex){
            throw ex;
        }catch(Throwable th){
            Log.log(Log.FRAMEWORK, "[JMSSender.sendMessageToQueue] Error ", th);
            throw new SystemException("[JMSSender.sendMessageToQueue] Error "+th.getMessage(),th);
        }
        */
      sendMessage(configName, queueName, messageObj);
    }

    public static Destination getDestination(String propertiesFileKey,String destName) throws SystemException{
        Destination dest =  null;
        dest=(Destination)destHt.get(propertiesFileKey+"/"+destName);
        if(dest==null){
            synchronized(destHt){
                try{
                    Configuration config = ConfigurationManager.getInstance().getConfig(propertiesFileKey);
                    String jndiService = config.getString(JNDI_SERVICE);
                    //dest= (Destination) ServiceLookup.getInstance(jndiService).lookup(destName);
                    dest= (Destination) ServiceLocator.instance(jndiService).getJndiFinder().lookup(destName, Destination.class);
                    if(dest!=null)
                        destHt.put(propertiesFileKey+"/"+destName,dest);
                    else throw new JMSException("[JMSSender.getDestination] Unable to get Destination "+destName);
                }catch(Throwable th){
                    Log.warn(Log.FRAMEWORK, "[JMSSender.getDestination] Error", th);
                    throw new SystemException("[JMSSender.getDestination] Error ",th);
                }
            }
            Log.debug(Log.FRAMEWORK,"[JMSSender.getDestination] "+propertiesFileKey+"/"+destName+"="+dest.toString());
        }

        return dest;
    }
    
    public static Destination getDestinationViaConfigProps(Hashtable<String, String> jmsConfigProps, String destName) throws SystemException
    {
      Destination dest =  null;      
      
              try{
                  Log.debug(Log.FRAMEWORK, "getDestinationViaConfigProps");
                  String jndiService = jmsConfigProps.get(JNDI_SERVICE);
                  dest= (Destination) ServiceLocator.instance(jndiService).getJndiFinder().lookup(destName, Destination.class);
                  if(dest ==null )
                  {
                    throw new JMSException("[JMSSender.getDestinationViaConfigProps] Unable to get Destination "+destName);
                  }
              }catch(Throwable th){
                  Log.warn(Log.FRAMEWORK, "[JMSSender.getDestinationViaConfigProps] Error", th);
                  throw new SystemException("[JMSSender.getDestinationViaConfigProps] Error ",th);
              }
      return dest;
    }
    
    public static Connection getConnection(String propertiesFileKey,String destName)  throws SystemException 
    {
      Connection conn=null;
      /*NSL20070209
      conn=(Connection)connHt.get(propertiesFileKey+"/"+destName);
      if(conn==null)
      {
        synchronized(connHt)
        { */
          try
          {
            Configuration config = ConfigurationManager.getInstance().getConfig(propertiesFileKey);
            Properties prop = config.getProperties();

            String jndiService = prop.getProperty(JNDI_SERVICE);
            JNDIFinder finder = ServiceLocator.instance(jndiService).getJndiFinder();

            String userName=prop.getProperty(CONNECTION_USERNAME);
            String password=prop.getProperty(CONNECTION_PASSWORD);
            
            ConnectionFactory connFactory = (ConnectionFactory)finder.lookup(prop.getProperty(CONN_FACTORY,DEFAULT_CONN_FACTORY), ConnectionFactory.class);
            if (userName != null && password != null)
            {
              conn = connFactory.createConnection(userName,password);
            }
            else
            {
              conn = connFactory.createConnection();
            }
            /*
            if (conn != null)
            {
              connHt.put(propertiesFileKey+"/"+destName,conn);
            }
            else*/
            if (conn == null)
            {
              throw new JMSException("[JMSSender.getConnection] Unable to get Connection ");
            }
          }
          catch (Throwable th)
          {
            Log.log(Log.FRAMEWORK, "[JMSSender.getConnection] Error ", th);
            throw new SystemException("[JMSSender.getConnection] Error ", th);
          }
      /*    
        }
      }*/
      return conn;
    }
    
    public static Connection getConnectionViaConfigProps(Hashtable<String, String> jmsConfigProps, String destName) throws SystemException 
    {
          Connection conn=null;
          try
          {
            Log.log(Log.FRAMEWORK, "JMSSender: getConnectionViaConfigProps: "+jmsConfigProps);
            
            String jndiService = jmsConfigProps.get(JNDI_SERVICE);
            JNDIFinder finder = ServiceLocator.instance(jndiService).getJndiFinder();

            String userName=jmsConfigProps.get(CONNECTION_USERNAME);
            String password=jmsConfigProps.get(CONNECTION_PASSWORD);
            
            ConnectionFactory connFactory = (ConnectionFactory)finder.lookup(jmsConfigProps.get(CONN_FACTORY) == null? DEFAULT_CONN_FACTORY: jmsConfigProps.get(CONN_FACTORY), ConnectionFactory.class);
            if (userName != null && password != null)
            {
              conn = connFactory.createConnection(userName,password);
            }
            else
            {
              conn = connFactory.createConnection();
            }
            if (conn == null)
            {
              throw new JMSException("[JMSSender.getConnection] Unable to get Connection ");
            }
          }
          catch (Throwable th)
          {
            Log.log(Log.FRAMEWORK, "[JMSSender.getConnection] Error ", th);
            throw new SystemException("[JMSSender.getConnection] Error ", th);
          }
      return conn;
    }
    
    /*NSL20070130 replaced by above
    public static Connection getConnection(String propertiesFileKey,String destName,int type)  throws SystemException {
      Connection conn=null;
      conn=(Connection)connHt.get(propertiesFileKey+"/"+destName);
      if(conn==null){
          synchronized(connHt){
              try
              {
                  Configuration config = ConfigurationManager.getInstance().getConfig(propertiesFileKey);
                  Properties prop=config.getProperties();

                  String jndiService = prop.getProperty(JNDI_SERVICE);
                  //ServiceLookup serviceLookup = ServiceLookup.getInstance(jndiService);
                  JNDIFinder finder = ServiceLocator.instance(jndiService).getJndiFinder();

                  String userName=prop.getProperty(CONNECTION_USERNAME);
                  String password=prop.getProperty(CONNECTION_PASSWORD);
                  if(type==TOPIC_TYPE){
                      ConnectionFactory topicConnFactory = (ConnectionFactory)finder.lookup(prop.getProperty(TOPIC_FACTORY,DEFAULT_TOPIC_FACTORY));
                      if (userName != null && password != null)
                          conn = ((TopicConnectionFactory) topicConnFactory).createTopicConnection(userName, password);
                      else conn = ((TopicConnectionFactory) topicConnFactory).createTopicConnection();
                  } else {
                      ConnectionFactory queueConnFactory = (ConnectionFactory) finder.lookup(prop.getProperty(QUEUE_FACTORY,DEFAULT_QUEUE_FACTORY));
                      if (userName != null && password != null)
                          conn = ((QueueConnectionFactory) queueConnFactory).createQueueConnection(userName, password);
                      else conn = ((QueueConnectionFactory) queueConnFactory).createQueueConnection();
                  }
                  if(conn != null){
                      connHt.put(propertiesFileKey+"/"+destName,conn);
                  } else throw new JMSException("[JMSSender.getConnection] Unable to get Connection ");
              }
              catch (Throwable th)
              {
                  Log.log(Log.FRAMEWORK, "[JMSSender.getConnection] Error ", th);
                  throw new SystemException("[JMSSender.getConnection] Error ", th);
              }
          }
      }
      return conn;
    }
    */
    /*NSL20070209
    protected void finalize() throws Throwable
    {
        if(connHt!=null){
            Enumeration en=connHt.keys();
            while(en.hasMoreElements()){
                try
                {
                    Connection conn=(Connection)connHt.get(en.nextElement());
                    if(conn!=null)
                        conn.close();
                }
                catch (Throwable t)
                {
                }
            }
        }
        super.finalize();
    }*/


}
