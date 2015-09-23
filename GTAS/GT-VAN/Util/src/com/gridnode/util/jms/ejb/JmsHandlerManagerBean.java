/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsSenderManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 26, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.util.jms.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TopicConnectionFactory;
import javax.naming.NamingException;

import com.gridnode.util.jms.JMSFailedMsgDAO;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jms.JmsSenderException;
import com.gridnode.util.jms.model.JMSFailedMsg;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class JmsHandlerManagerBean implements SessionBean
{
  /**
   * 
   */
  private static final long serialVersionUID = -4252246491378752824L;
  private SessionContext _ctxt;
  
  private Logger _logger = null;
  
  public void sendWithNewTrans(Serializable obj, Hashtable msgProperties, Properties sendProperties) throws JmsSenderException
  {
    send(obj, msgProperties, sendProperties);
  }
  
  /**
   * 
   * @param obj
   * @param msgProperties
   * @param sendProperties
   * @param targetMsgType Allow to specify the JMS msg type 
   * @throws JmsSenderException
   */
  public void send(Serializable obj, Hashtable msgProperties, Properties sendProperties) throws JmsSenderException
  {
    String mtdName = "send";
    
    Connection conn = null;
    try
    {
      _logger.logEntry(mtdName+" send via jms mgr", null);
      System.out.println(mtdName+" send via jms mgr");
      
      JndiFinder finder = getJndiFinder(sendProperties);
      Destination dest = getDestination(finder, sendProperties);
      Class connFactoryClass = (dest instanceof Queue)? QueueConnectionFactory.class : TopicConnectionFactory.class;
      
      conn = createConnection(sendProperties.getProperty(JmsSender.CONN_FACTORY), connFactoryClass,
                              sendProperties.getProperty(JmsSender.CONN_USER, null), sendProperties.getProperty(JmsSender.CONN_PWD),
                              finder);
      Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Message msg = createMessage(session, obj, sendProperties);
      
      addMessageProperties(msg, msgProperties);
      
      MessageProducer producer = session.createProducer(dest);
      producer.send(msg);
    }
    catch (NamingException ex)
    {
      String errorMsg = "JNDI Error";
      _logger.logWarn(mtdName, null, errorMsg, ex);
      throw new JmsSenderException(errorMsg,ex);
    }
    catch (JMSException ex)
    {
      String errorMsg = "JMS Error";
      _logger.logWarn(mtdName, null, errorMsg, ex);
      throw new JmsSenderException(errorMsg,ex);
    }
    catch (Throwable t)
    {
      String errorMsg = "Unexpected Error";
      _logger.logWarn(mtdName, null, errorMsg, t);
      throw new JmsSenderException(errorMsg, t);      
    }
    finally
    {
      closeConnection(conn);
      _logger.logExit(mtdName, null);
    }
  }
  
  public void saveFailedJMS(Serializable obj, Hashtable msgProperties, Properties sendProperties, String category)
  {
    String mn = "saveFailedJMS";
    String destName = ""; 
    if(sendProperties != null)  
    {
      destName = sendProperties.getProperty(JmsSender.DESTINATION);
    }
    JMSFailedMsg msg = new JMSFailedMsg(destName, obj, msgProperties, category, sendProperties);
    JMSFailedMsgDAO dao = new JMSFailedMsgDAO();
    dao.persistFailedJMS(msg);
    _logger.logMessage(mn, null, "After persist failed jms.");
  }
  
  public Collection<JMSFailedMsg> retrieveFailedJMS(int maxRetry, String category, int numRetry)
  {
    JMSFailedMsgDAO dao = new JMSFailedMsgDAO();
    return dao.retrieveFailedJMSMsgs(maxRetry, category, numRetry);
  }
  
  public void updateFailedJMS(JMSFailedMsg msg, int maxRetry)
  {
    String method = "updateFailedJMS";
    msg.setRetryCount(msg.getRetryCount()+1);
    
    int retryCount = msg.getRetryCount();
    if(retryCount >= maxRetry)
    {
      _logger.logMessage(method, null, "Update failed jms:  max retry threshold reached. Delete the failed jms: "+msg);
      deleteFailedJMS(msg);
      return;
    }
    else
    {
      msg.setRetryCount(retryCount++);
    }
    JMSFailedMsgDAO dao = new JMSFailedMsgDAO();
    dao.updateFailedJMS(msg);
  }
  
  public void deleteFailedJMS(JMSFailedMsg msg)
  { 
    JMSFailedMsgDAO dao = new JMSFailedMsgDAO();
    dao.deleteFailedJMS(msg);
  }
  
  /**
   * Handle the failed jms by resending them to their destination.
   * @param msg The failed jms we persisted in DB for retry.
   */
  public void handleFailedJMS(JMSFailedMsg msg) throws Exception
  {
    String method = "handleFailedJMS";
    try
    {
      _logger.debugMessage(method, null, "Start handling failed jms: "+msg);
      send(msg.getMsgObj(), msg.getMsgProps(), msg.getSendProps());
      deleteFailedJMS(msg);
      _logger.debugMessage(method, null, "Successfull sent failed jms, and deleted it: "+msg);
    }
    catch(Exception ex)
    {
      _ctxt.setRollbackOnly();
      _logger.logWarn( method, null, "Error in handling the failed JMS", ex);
      throw ex;
    }
  }
  
  private JndiFinder getJndiFinder(Properties lookupProps) throws NamingException
  {
    return new JndiFinder(lookupProps);
  }
  
  private Destination getDestination(JndiFinder finder, Properties destProps) throws NamingException
  {
    return (Destination)finder.lookup(destProps.getProperty(JmsSender.DESTINATION), Destination.class);
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
  
  private Message createMessage(Session session, Serializable obj, Properties jmsTypProps) throws JMSException
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
      String msgMode = getPrimitiveMsgMode(jmsTypProps);
      if (JmsSender.MODE_TEXT.equalsIgnoreCase(msgMode))
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
      else if (JmsSender.MODE_STREAM.equalsIgnoreCase(msgMode))
      {
        StreamMessage msg = session.createStreamMessage();
        msg.writeObject(obj);
        return msg;
      }
      else if (JmsSender.MODE_BYTES.equalsIgnoreCase(msgMode))
      {
        BytesMessage msg = session.createBytesMessage();
        msg.writeObject(obj);
        return msg;
      }
    }
    
    //otherwise, objectMessage
    return session.createObjectMessage(obj);    
  }
  
  private String getPrimitiveMsgMode(Properties msgProps)
  {
    return msgProps.getProperty(JmsSender.PRIMITIVE_MSG_MODE, JmsSender.MODE_TEXT);
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
  
  public void ejbCreate() throws CreateException
  {
    _logger = getLogger();
  }
  
  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#ejbActivate()
   */
  public void ejbActivate() throws EJBException, RemoteException
  {
    _logger = getLogger();
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(LoggerManager.TYPE_UTIL, "JmsHandlerManagerBean");
  }
  
  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#ejbPassivate()
   */
  public void ejbPassivate() throws EJBException, RemoteException
  {
    _logger = null;
  }

  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#ejbRemove()
   */
  public void ejbRemove() throws EJBException, RemoteException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
   */
  public void setSessionContext(SessionContext ctxt) throws EJBException,
                                                    RemoteException
  {
    _ctxt = ctxt;
  }

}
