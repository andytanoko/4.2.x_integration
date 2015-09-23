/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Jan 05 2006    i00107              Able to send different type of messages
 *                                    for primitive types, String, and byte[]
 * Mar 05 2007		Alain Ah Ming				Added error code
 * Mar 07 2007		Alain Ah Ming				Removed 'throws Exception' from method signature
 * Mar 07 2007		Alain Ah Ming				Throw exception from send method and log warning instead of error
 * Apr 26 2007    i00107              Add constructor with LoggerManager. Use LoggerManager specified
 *                                    for logging.
 * Jun 11 2007    Tam Wei Xiang       Support the msg selector
 */

package com.gridnode.util.jms;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.jms.*;
import javax.naming.NamingException;

import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;


/**
 * @author i00107
 * This class is responsible for handling the sending of JMS messages.
 */
public class JmsSender
{
  public static final String DESTINATION = "destination.jndi";
  public static final String CONN_FACTORY = "connection.factory.jndi";
  public static final String CONN_USER = "connection.user";
  public static final String CONN_PWD = "connection.pwd";
  public static final String PRIMITIVE_MSG_MODE = "primitive.msg.mode";
  
  public static final String MODE_TEXT = "text";
  public static final String MODE_BYTES = "bytes";
  public static final String MODE_STREAM = "stream";
  public static final String MODE_OBJECT = "object";
  
  private Properties _props;
  private LoggerManager _logMgr;
  
  public JmsSender()
  {
  }
  
  public JmsSender(LoggerManager logMgr)
  {
    _logMgr = logMgr;
  }
 
  /**
   * Set the properties needed for a send to the target destination.
   * The properties should contain the necessary properties for JNDI connection to
   * the target JMS provider and the destination name.
   * @param props
   */
  public void setSendProperties(Properties props)
  {
    _props = props;
  }
  
  private synchronized Logger getLogger()
  {
    if (_logMgr == null)
    {
      return LoggerManager.getInstance().getLogger(LoggerManager.TYPE_UTIL, "JmsSender");
    }
    else
    {
      return _logMgr.getLogger(LoggerManager.TYPE_UTIL, "JmsSender");
    }
  }
  public void send(Serializable obj) throws JmsSenderException
  {
    send(obj, null);
  }
  
  /**
   * Send a JMS Message containing the specified object to the destination.
   * The send properties must be set before calling this method.
   *  
   * @param obj The object to send.
   * @throws JmsSenderException Unable to perform the send, probably due to JMS Exception
   * or NamingException 
   */
  public void send(Serializable obj, Hashtable msgProperties) throws JmsSenderException 
  {
    Logger logger = getLogger();
    //Logger logger = LoggerManager.getInstance().getLogger(LoggerManager.TYPE_UTIL, "JmsSender");
    String mtdName = "send";
    //Object[] params = {obj};
    
    Connection conn = null;
    try
    {
      //logger.logEntry(mtdName, params);
      
      JndiFinder finder = getJndiFinder();
      Destination dest = getDestination(finder);
      Class connFactoryClass = (dest instanceof Queue)? QueueConnectionFactory.class : TopicConnectionFactory.class;
      
      conn = createConnection(_props.getProperty(CONN_FACTORY), connFactoryClass,
                              _props.getProperty(CONN_USER, null), _props.getProperty(CONN_PWD),
                              finder);
      Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Message msg = createMessage(session, obj);
      
      addMessageProperties(msg, msgProperties);
      
      MessageProducer producer = session.createProducer(dest);
      producer.send(msg);
    }
    catch (NamingException ex)
    {
    	String errorMsg = "JNDI Error";
      logger.logWarn(mtdName, null, errorMsg, ex);
      throw new JmsSenderException(errorMsg,ex);
    }
    catch (JMSException ex)
    {
    	String errorMsg = "JMS Error";
      logger.logWarn(mtdName, null, errorMsg, ex);
      throw new JmsSenderException(errorMsg,ex);
    }
    catch (Throwable t)
    {
    	String errorMsg = "Unexpected Error";
      logger.logWarn(mtdName, null, errorMsg, t);
      throw new JmsSenderException(errorMsg, t);    	
    }
    finally
    {
      closeConnection(conn);
      //logger.logExit(mtdName, params);
    }
  }
  
  private void addMessageProperties(Message msg, Hashtable msgProps) throws JMSException
  {
    if(msgProps == null)
    {
      return;
    }
    
    Enumeration enu = msgProps.keys();
    while(enu.hasMoreElements())
    {
      String msgPropKey = (String)enu.nextElement();
      Object msgPropValue = msgProps.get(msgPropKey);
      msg.setObjectProperty(msgPropKey, msgPropValue);
    }
  }
  
  private String getPrimitiveMsgMode()
  {
    return _props.getProperty(PRIMITIVE_MSG_MODE, MODE_TEXT);
  }
  
  private Message createMessage(Session session, Serializable obj) throws JMSException
  {
    if (obj instanceof Map)
    {
      MapMessage msg = session.createMapMessage();
      Map objMap = (Map)obj;
      Set keys = objMap.keySet();
      for (Object key : keys)
      {
        msg.setObject(String.valueOf(key), objMap.get(key));
      }
      return msg;
    }

    if (obj.getClass().isPrimitive() || obj instanceof String || obj instanceof byte[])
    {
      String msgMode = getPrimitiveMsgMode();
      if (MODE_TEXT.equalsIgnoreCase(msgMode))
      {
        String str;
        if (obj instanceof byte[])
        {
          str = new String((byte[])obj);
        }
        else
        {
          str = String.valueOf(obj);
        }
        return session.createTextMessage(str);
      }
      else if (MODE_STREAM.equalsIgnoreCase(msgMode))
      {
        StreamMessage msg = session.createStreamMessage();
        msg.writeObject(obj);
        return msg;
      }
      else if (MODE_BYTES.equalsIgnoreCase(msgMode))
      {
        BytesMessage msg = session.createBytesMessage();
        msg.writeObject(obj);
        return msg;
      }
    }
    
    //otherwise, objectMessage
    return session.createObjectMessage(obj);    
  }
  
  private Destination getDestination(JndiFinder finder) throws NamingException
  {
    return (Destination)finder.lookup(_props.getProperty(DESTINATION), Destination.class);
  }
  
  private JndiFinder getJndiFinder() throws NamingException
  {
    return new JndiFinder(_props, _logMgr);
  }
  
  private Connection createConnection(String factoryName, Class factoryClass, String user, String pwd, JndiFinder finder)
    throws NamingException, JMSException
  {
    ConnectionFactory factory = (ConnectionFactory)finder.lookup(factoryName, factoryClass);
    if (user == null)
    {
      return factory.createConnection();
    }
    else
    {
      return factory.createConnection(user, pwd);
    }
  }

  private void closeConnection(Connection conn)
  {
    if (conn != null)
    {
      try
      {
        conn.close();
      }
      catch (Exception ex)
      {
        
      }
    }
    
  }
}
