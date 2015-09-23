/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsRetrySender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 31, 2007   Tam Wei Xiang       Created
 * Jul 29, 2008   Tam Wei Xiang       #69 Modified retrySend(...).Propagate up 
 *                                        the JMSFailureException if we have exceeded 
 *                                        the max retry.
 */
package com.gridnode.util.jms;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.rmi.PortableRemoteObject;

import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.exceptions.JMSFailureException;
import com.gridnode.util.jms.ejb.IJmsHandlerManagerHome;
import com.gridnode.util.jms.ejb.IJmsHandlerManagerLocalHome;
import com.gridnode.util.jms.ejb.IJmsHandlerManagerLocalObj;
import com.gridnode.util.jms.ejb.IJmsHandlerManagerObj;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * 
 * 
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class JmsRetrySender
{
  public static final String JMS_RETRY_CAT = "failed.jms";
  public static final String JMS_RETRY_COUNT = "max.retry";
  public static final String JMS_RETRY_INTERVAL = "retry.interval";
  public static final String JMS_RETRY_ENABLED = "is.send.via.def"; //Indicate the jms sending mode (true: with jms retry & wrapped inside a session bean  false: default send mode via normal class) 
  public static final String JMS_LOOKUP_IS_LOCAL = "is.lookup.localcontext";
  
  private static Properties _jmsRetryProps = null;
  private JndiFinder _finder;
  private boolean _isUseLocalContext = true;
  
  private static boolean _isSendViaDefMode;
  private static Logger _logger = getLogger();
  
  
  public JmsRetrySender()
  {
    if(_jmsRetryProps == null)
    {
      init();
    }
  }
  
  public JmsRetrySender(Properties retryProps)
  {
    _jmsRetryProps = retryProps;
    _isUseLocalContext = Boolean.getBoolean(_jmsRetryProps.getProperty(JMS_LOOKUP_IS_LOCAL, "true"));
    _isSendViaDefMode = new Boolean (_jmsRetryProps.getProperty(JMS_RETRY_ENABLED, "true"));
  }
  
  private static Logger getLogger()
  {
      return LoggerManager.getOneTimeInstance().getLogger(LoggerManager.TYPE_UTIL, "JmsRetrySender");
    //return null;
  }
  
  private synchronized void init()
  {
    if(_jmsRetryProps == null)
    {
      _logger.debugMessage("init", null, "Init from DB");
      ConfigurationStore configStore = ConfigurationStore.getInstance();
      _jmsRetryProps = configStore.getProperties(JMS_RETRY_CAT);
      _isSendViaDefMode = new Boolean (_jmsRetryProps.getProperty(JMS_RETRY_ENABLED, "true"));
      _isUseLocalContext = Boolean.getBoolean(_jmsRetryProps.getProperty(JMS_LOOKUP_IS_LOCAL, "true"));
    }
  }
  
  /**
   * Delivered the jms msg to the destination(Queue/Topic) given in the sendProps. If the jms destination can't be looked up or encounter any
   * JMSException, it will perform retry until the send out is successfull or until exceeded the retry count. 
   * 
   * If the isPersistedToDB is true, after the max retry has reached, the obj that need to be delivered will be persisted to db.
   * 
   * @param obj The obj that need to be send/broadcast
   * @param msgProperties The message properties that we will set into the constructed JMS message.
   * @param category Indicate which component has send out the jms.
   * @param sendProps Indicate the send properties of the jms msg (eg the Destination jndi name etc.)
   * @param isPersistedToDB indicate whether to persist the jms msg to DB after reached max retry.
   * @throws JmsSenderException throws if the nested exception is neither javax.jms.JmsException nor NameNotFoundException or we
   *                            failed to lookup JmsHandlerManagerBean
   */
  public void retrySendWithPersist(Serializable obj, Hashtable msgProperties, String category, Properties sendProps, Boolean isPersistedToDB) throws JmsSenderException 
  {
    Logger logger = _logger;
    String method = "retrySendWithPersist";
    
    int retryCount = Integer.parseInt(_jmsRetryProps.getProperty(JMS_RETRY_COUNT, "10"));
    long retryInterval = new Long(_jmsRetryProps.getProperty(JMS_RETRY_INTERVAL, "10000"));
    boolean isRetryEnabled = retryCount <= 0 ? false : true;
    
    logger.debugMessage(method, null, "Retrieve retryCount: "+retryCount+" retryInterval: "+retryInterval);
    
    Throwable th1 = null;
    IJmsHandlerManagerLocalObj jmsMgr = null;
    
    do
    {
      try
      {
          sendMsg(obj, msgProperties, sendProps);
          retryCount = -10;
      }
      catch(Throwable th)
      {
        
        if(isRetryEnabled && isRetryableException(th))//a swtich to turn off the persisting of failed jms
        {
            logger.logWarn(method, null, "JMS exception catched: wait for "+retryInterval/1000+" sec. Retry count left: "+retryCount, th);
            try
            {
              Thread.sleep(retryInterval);
            }
            catch(Exception e)
            {
          
            }
            logger.logMessage(method, null, "JMS exception catched: wake up !");
            th1 = th;
        }
        else
        {
          //ERROR enter here
          
          logger.logMessage(method, null, "Error in sending the JMS msg with msgProps: "+msgProperties);
          throw new JmsSenderException("Error in sending the JMS msg with msgProps: "+msgProperties+" sendProps: "+sendProps, th);
        }
      }
    }while(retryCount-- > 0);
    
    if(isRetryEnabled && retryCount == -1 && th1 != null && isPersistedToDB)
    {
      logger.logWarn( method, null, "JMS retry exhausted. Persisting to DB. exception is: "+th1.getMessage(), th1);
      try
      {
        persistMsg(obj, msgProperties, sendProps, category);
      }
      catch(Exception ex)
      {
        throw new JmsSenderException("Failed to persist failedJMS to DB,", ex);
      }
    }
    else if(isRetryEnabled && retryCount == -1 && th1 != null)
    {
      throw new JmsSenderException("Error in sending the JMS msg with msgProps: "+msgProperties+" sendProps: "+sendProps, th1);
    }
  }
  
  /**
   * Delivered the jms msg to the destination(Queue/Topic) given in the sendProps. If the jms destination can't be looked up or encounter any
   * JMSException, it will perform retry until the send out is successfull or until exceeded the retry count.
   * @param obj The obj that need to be send/broadcast
   * @param msgProperties The message properties that we will set into the constructed JMS message.
   * @param category Indicate which component has send out the jms.
   * @param sendProps Indicate the send properties of the jms msg (eg the Destination jndi name etc.)
   * @throws JmsSenderException throws if the nested exception is neither javax.jms.JmsException nor NameNotFoundException or we
   *                            failed to lookup JmsHandlerManagerBean
   */
  public void retrySend(Serializable obj, Hashtable msgProperties, String category, Properties sendProps) throws JmsSenderException
  {
    Logger logger = _logger;
    String method = "retrySend";
    
    int retryCount = Integer.parseInt(_jmsRetryProps.getProperty(JMS_RETRY_COUNT, "10"));
    long retryInterval = new Long(_jmsRetryProps.getProperty(JMS_RETRY_INTERVAL, "10000"));
    boolean isRetryEnabled = retryCount <= 0 ? false : true;
    
    logger.debugMessage(method, null, "Retrieve retryCount: "+retryCount+" retryInterval: "+retryInterval);
    
    Throwable th1 = null;
    
    do
    {
      try
      {
          JmsSender sender = new JmsSender(LoggerManager.getOneTimeInstance());
          sender.setSendProperties(sendProps);
          sender.send(obj, msgProperties);
          
          retryCount = -10;
      }
      catch(Throwable th)
      {
        
        if(isRetryEnabled && isRetryableException(th))//a swtich to turn off the persisting of failed jms
        {
            logger.logError("", method, null, "JMS exception catched: wait for "+retryInterval/1000+" sec. Retry count left: "+retryCount, th);
            try
            {
              Thread.sleep(retryInterval);
            }
            catch(Exception e)
            {
          
            }
            logger.logMessage(method, null, "JMS exception catched: wake up !");
            th1 = new JMSFailureException(th);
        }
        else
        {
          //ERROR enter here
          
          logger.logMessage(method, null, "Error in sending the JMS msg with msgProps: "+msgProperties);
          throw new JmsSenderException("Error in sending the JMS msg with msgProps: "+msgProperties+" sendProps: "+sendProps, th);
        }
      }
    }while(retryCount-- > 0);
    
    if(isRetryEnabled && retryCount == -1 && th1 != null)    
    {
      throw new JmsSenderException("Error in sending the JMS msg with msgProps: "+msgProperties+" sendProps: "+sendProps, th1);
    }
  }
  
  private void sendMsg(Serializable obj,Hashtable msgProperties, Properties sendProps) throws Exception
  {
    String method = "sendMsg";
    if(_isUseLocalContext)
    {
      _logger.debugMessage(method, null, "Use localContext: Before getJMS MGR: Using local mgr");
      getLocalJmsMgr().send(obj, msgProperties, sendProps);
      _logger.debugMessage(method, null, "Use localContext: After getJMS MGR: Using local mgr");
    }
    else
    {
      _logger.debugMessage(method, null, "Use RemoteContext: Before getJMS MGR: Using remote mgr");
      getJmsMgr().send(obj, msgProperties, sendProps);
      _logger.debugMessage(method, null, "Use RemoteContext: After getJMS MGR: Using remote mgr");
    }
  }
  
  private void persistMsg(Serializable obj, Hashtable msgProperties, Properties sendProps, String category) throws Exception
  {
    
    _logger.debugMessage("persistMsg", null, "Persist msg in localContext? "+_isUseLocalContext);
    if(_isUseLocalContext)
    {
      getLocalJmsMgr().saveFailedJMS(obj, msgProperties, sendProps, category);
    }
    else
    {
      getJmsMgr().saveFailedJMS(obj, msgProperties, sendProps, category);
    }
  }
  
  public boolean isRetryableException(Throwable th)
  {
    String mn = "isRetryableException";
    Logger logger = _logger;
    
    if(th == null)
    {
      logger.debugMessage(mn, null, "isRetryableException Unknown exception !");
      return false;
    }
    else if(th instanceof JMSException)
    {
      logger.debugMessage(mn, null, "isRetryableException JMSException detected.");
      return true;
    }
    else if(th instanceof NameNotFoundException)
    {
      logger.debugMessage(mn, null, "isRetryableException NameNotFound detected");
      return true;
    }
    else
    {
      Throwable ex = th.getCause();
      return isRetryableException(ex);
    }
  }
  
  private IJmsHandlerManagerLocalObj getLocalJmsMgr() throws Exception
  {
    JndiFinder lookup = getJNDIFinder() == null? new JndiFinder(null) : getJNDIFinder();
    IJmsHandlerManagerLocalHome home = (IJmsHandlerManagerLocalHome)lookup.lookup(IJmsHandlerManagerLocalHome.class.getName(), IJmsHandlerManagerLocalHome.class);
    return home.create();
  }
  
  private IJmsHandlerManagerObj getJmsMgr() throws Exception
  {
    JndiFinder lookup = getJNDIFinder() == null? new JndiFinder(null) : getJNDIFinder();
    IJmsHandlerManagerHome home = (IJmsHandlerManagerHome)lookup.lookup(IJmsHandlerManagerHome.class.getName(), IJmsHandlerManagerHome.class);
    return home.create();
  }
  
  public void setJNDIFinder(JndiFinder finder)
  {
    _finder = finder;
  }
  
  public JndiFinder getJNDIFinder()
  {
    return _finder;
  }
  
  public boolean isSendViaDefMode()
  {
    return _isSendViaDefMode;
  }
  
  public static void main(String[] args) throws Exception
  {
    Context context = new InitialContext(getProps());
    Object obj = context.lookup(IJmsHandlerManagerHome.class.getName()+"_HC");
    IJmsHandlerManagerHome home = (IJmsHandlerManagerHome)PortableRemoteObject.narrow(obj, IJmsHandlerManagerHome.class);
  }
  
  private static Properties getProps()
  {
    Properties props = new Properties();
    props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
    props.setProperty("java.naming.provider.url", "192.168.213.240:1100");
    props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
    return props;
  }
}
