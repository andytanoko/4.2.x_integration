/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSRetrySender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 8, 2007   Tam Wei Xiang       Created
 * Jul 20,2008   Tam Wei Xiang       #69: Added method to send/broadcast the JMS msg
 *                                   not under  a new transaction.
 */
package com.gridnode.pdip.framework.jms;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.jms.JMSException;
import javax.naming.NameNotFoundException;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.jms.JMSFailedMsg;
import com.gridnode.pdip.framework.db.jms.JMSFailedMsgHelper;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.JMSFailureException;
import com.gridnode.pdip.framework.exceptions.NestingException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.jms.ejb.IJMSMsgHandlerHome;
import com.gridnode.pdip.framework.jms.ejb.IJMSMsgHandlerLocalHome;
import com.gridnode.pdip.framework.jms.ejb.IJMSMsgHandlerLocalObj;
import com.gridnode.pdip.framework.jms.ejb.IJMSMsgHandlerObj;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.notification.INotification;
import com.gridnode.pdip.framework.notification.Notifier;
import com.gridnode.pdip.framework.notification.ejb.INotifierMgrLocalHome;
import com.gridnode.pdip.framework.notification.ejb.INotifierMgrLocalObj;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.SystemUtil;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class JMSRetrySender
{
  private static final String JMS_DEF_SEND = "jms.send.mode.def";
  private static final String JMS_NUM_RETRY = "jms.num.retry";
  private static final String JMS_RETRY_INTERVAL = "jms.retry.interval";
  private static final String FRAMEWORK_JMS_CONFIG = "jms";
  private static final String JMS_DEST_TYPE_TOPIC = "Topic";
  private static final String JMS_DEST_TYPE_QUEUE = "Queue";
  
  private static boolean _isSendViaDefMode = true; //To indicate the JMS sending mode(either via normal class or wrapped inside a session bean)
  private static int _retryCount;
  private static long _sleepFor;
  
  
  static 
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(FRAMEWORK_JMS_CONFIG);
    
    _retryCount = config.getInt(JMS_NUM_RETRY, 10);
    _sleepFor = config.getLong(JMS_RETRY_INTERVAL) == 0L ? 10000 : config.getLong(JMS_RETRY_INTERVAL);
    _isSendViaDefMode = config.getBoolean(JMS_DEF_SEND, true);
    
    Log.log(Log.FRAMEWORK, " Retrieve jms props from "+FRAMEWORK_JMS_CONFIG+" --> JMS Sending mode: "+(_isSendViaDefMode? "Default": "With Retry")+ "Retry Count: "+_retryCount+" retry interval: "+_sleepFor+" ms");
  }
  
  public static boolean isSendViaDefMode()
  {
    return _isSendViaDefMode;
  }
  
  /**
   * Send the JMS message (under new transaction) with Retry mechanism enable when encounter JMS related error. If the retry count has exceeded,
   * that JMS will be stored into DB for later retry.
   * @param destName
   * @param msgObj
   * @param msgProps
   * @param jmsSendProps
   * @throws Throwable
   */
  public static void sendMessageWithPersist(String destName, Serializable msgObj, Hashtable msgProps, Hashtable<String,String> jmsSendProps) throws Throwable
  {
    boolean isRetryEnabled = _retryCount <= 0 ? false : true;
    int retryCount = _retryCount;
    Throwable th1 = null;
    
    
    do
    {
      try
      {
        getJMSMgr().sendMessage(jmsSendProps, destName, msgObj, msgProps);
        retryCount = -10;
      }
      catch(Throwable th)
      {
        if(isRetryableException(th) && isRetryEnabled)
        {
          Log.warn(Log.FRAMEWORK, "Send: JMS exception found. Reconnect after "+_sleepFor/1000+" num retry left: "+retryCount, th);
          
          try
          {
            Thread.sleep(_sleepFor);
          }
          catch(Exception ex)
          {
          
          }
          Log.debug(Log.FRAMEWORK, "Send: JMS exception catched: wake up !");
          th1 = th;
          
        }
        else
        {
          Log.warn( Log.FRAMEWORK, "Send: Non Retryable exception found. No retry will be performed.", th);
          throw th;
        }
      }
    }while(retryCount-- > 0);
    
    
    if(isRetryEnabled  && retryCount == -1 && th1 != null)
    {
      Log.log(Log.FRAMEWORK, "Send: JMS retry exhausted. exception is: "+th1.getMessage(), th1);
      dumpFailedJMS(JMS_DEST_TYPE_QUEUE, jmsSendProps, destName, msgObj, msgProps);
    }
  }
  
  /**
   * Send the JMS message (not under new transaction) with Retry mechanism enable when encounter JMS related error
   * @param destName
   * @param msgObj
   * @param msgProps
   * @param jmsSendProps
   * @throws Throwable
   */
  public static void sendMessage(String destName, Serializable msgObj, Hashtable msgProps, Hashtable<String,String> jmsSendProps) throws Throwable
  {
    boolean isRetryEnabled = _retryCount <= 0 ? false : true;
    int retryCount = _retryCount;
    Throwable th1 = null;

    do
    {
      try
      {
        JMSSender.deliverJMSMessage(jmsSendProps, destName, msgObj, msgProps);
        retryCount = -10;
      }
      catch(Throwable th)
      {
        if(isRetryableException(th) && isRetryEnabled)
        {
          Log.warn(Log.FRAMEWORK, "Send: JMS exception found. Reconnect after "+_sleepFor/1000+" num retry left: "+retryCount, th);
          
          try
          {
            Thread.sleep(_sleepFor);
          }
          catch(Exception ex)
          {
          
          }
          Log.debug(Log.FRAMEWORK, "Send: JMS exception catched: wake up !");
          th1 = new JMSFailureException(th);
          
        }
        else
        {
          Log.warn( Log.FRAMEWORK, "Send: Non Retryable exception found. No retry will be performed.", th);
          throw th;
        }
      }
    }while(retryCount-- > 0);
    
    
    if(isRetryEnabled  && retryCount == -1 && th1 != null)
    {
      Log.log(Log.FRAMEWORK, "Send: JMS retry exhausted. exception is: "+th1.getMessage(), th1);
      throw th1;
    }
  }
  
  /**
   * Send the JMS message (under new transaction) with Retry mechanism enable when encounter JMS related error
   * @param destName
   * @param msgObj
   * @param msgProps
   * @param jmsSendProps
   * @param isLocal
   * @throws Throwable
   */
  public static void sendMessage(String destName, Serializable msgObj, Hashtable msgProps, Hashtable<String,String> jmsSendProps,
                                          boolean isLocal) throws Throwable
  {
    boolean isRetryEnabled = _retryCount <= 0 ? false : true;
    int retryCount = _retryCount;
    Throwable th1 = null;
   
    
    do
    {
      try
      {
        sendJMS(isLocal, jmsSendProps, destName, msgObj, msgProps);
        retryCount = -10;
      }
      catch(Throwable th)
      {
        if(isRetryableException(th) && isRetryEnabled)
        {
          Log.warn(Log.FRAMEWORK, "sendMessageWithRetry: JMS exception found. Reconnect after "+_sleepFor/1000+" num retry left: "+retryCount, th);
          
          try
          {
            Thread.sleep(_sleepFor);
          }
          catch(Exception ex)
          {
          
          }
          Log.debug(Log.FRAMEWORK, "sendMessageWithRetry: JMS exception catched: wake up !");
          th1 = new JMSFailureException(th);
          
        }
        else
        {
          Log.warn(Log.FRAMEWORK, "sendMessageWithRetry: Non Retryable exception found. No retry will be performed.", th);
          throw th;
        }
      }
    }while(retryCount-- > 0);
    
    
    if(isRetryEnabled  && retryCount == -1 && th1 != null)
    {
      Log.debug(Log.FRAMEWORK, "sendMessageWithRetry: JMS retry exhausted. exception is: "+th1.getMessage(), th1);
      throw th1;
    }
  }
  
  /**
   * Send the JMS message (not under new transaction) with Retry mechanism enable when encounter JMS related error.
   * @param configName the JMS properties config name
   * @param destName
   * @param msgObj
   * @param msgProps
   * @throws Throwable
   */
  public static void sendMessage(String configName, String destName, Serializable msgObj, Hashtable msgProps) throws Throwable
  {
    boolean isRetryEnabled = _retryCount <= 0 ? false : true;
    int retryCount = _retryCount;
    Throwable th1 = null;
    
    do
    {
      try
      {
        JMSSender.sendMessage(configName, destName, msgObj, msgProps);
        retryCount = -10;
      }
      catch(Throwable th)
      {
        if(isRetryableException(th) && isRetryEnabled)
        {
          Log.warn( Log.FRAMEWORK, "Send: JMS exception found. Reconnect after "+_sleepFor/1000+" num retry left: "+retryCount, th);
          
          try
          {
            Thread.sleep(_sleepFor);
          }
          catch(Exception ex)
          {
          
          }
          Log.debug(Log.FRAMEWORK, "Send: JMS exception catched: wake up !");
          th1 = new JMSFailureException(th);
          
        }
        else
        {
          Log.warn( Log.FRAMEWORK, "Send: Non Retryable exception found. No retry will be performed.", th);
          throw th;
        }
      }
    }while(retryCount-- > 0);
    
    
    if(isRetryEnabled  && retryCount == -1 && th1 != null)
    {
      Log.debug(Log.FRAMEWORK, "Send: JMS retry exhausted. exception is: "+th1.getMessage(), th1);
      throw th1;
    }
  }
  
  /**
   * Send the JMS message (under new transaction) with Retry mechanism enable when encounter JMS related error. If the retry count has exceeded,
   * that JMS will be stored into DB for later retry.
   * @param configName The JMS properties config name
   * @param destName
   * @param msgObj
   * @param msgProps
   * @throws Throwable
   */
  public static void sendMessageWithPersist(String configName, String destName, Serializable msgObj, Hashtable msgProps) throws Throwable
  {
    boolean isRetryEnabled = _retryCount <= 0 ? false : true;
    int retryCount = _retryCount;
    Throwable th1 = null;
    
    
    do
    {
      try
      {
        getJMSMgr().sendMessage(configName, destName, msgObj, msgProps);
        retryCount = -10;
      }
      catch(Throwable th)
      {
        if(isRetryableException(th) && isRetryEnabled)
        {
          Log.warn( Log.FRAMEWORK, "Send: JMS exception found. Reconnect after "+_sleepFor/1000+" num retry left: "+retryCount, th);
          
          try
          {
            Thread.sleep(_sleepFor);
          }
          catch(Exception ex)
          {
          
          }
          Log.debug(Log.FRAMEWORK, "Send: JMS exception catched: wake up !");
          th1 = th;
          
        }
        else
        {
          Log.warn( Log.FRAMEWORK, "Send: Non Retryable exception found. No retry will be performed.", th);
          throw th;
        }
      }
    }while(retryCount-- > 0);
    
    
    if(isRetryEnabled  && retryCount == -1 && th1 != null)
    {
      Log.debug(Log.FRAMEWORK, "Send: JMS retry exhausted. exception is: "+th1.getMessage(), th1);
      dumpFailedJMS(JMS_DEST_TYPE_QUEUE, getJmsSetupPropsKey(configName), destName, msgObj, msgProps);
    }
  }
  
  /**
   * Broadcast the notification under a new transaction. Whenever encouter JMS retryable error, it will perform retry until max retry has
   * reached. If it happen, the JMS msg will be stored in the DB.
   * @param notification
   * @throws Exception
   */
  public static void broadcastWithPersist(INotification notification)
    throws Exception
  {
    boolean isRetryEnabled = _retryCount <= 0 ? false : true;
    int retryCount = _retryCount;
    Exception ex1 = null;
    
    
    do
    {
      try
      {
        getLocalNotifierMgr().broadCastNotification(notification); 
        retryCount = -10;
      }
      catch(Exception ex)
      {
        
        if(isRetryEnabled && isRetryableException(ex))
        {
          ex1 = ex;
          Log.warn( Log.FRAMEWORK, "Broadcast: JMS exception found. Reconnect after "+_sleepFor/1000+" num retry left: "+retryCount, ex);
          try
          {
            Thread.sleep(_sleepFor);
          }
          catch (Exception e) 
          {
          
          }
          Log.debug(Log.FRAMEWORK, "Broadcast: JMS exception catched: wake up !");
        }
        else
        {
          Log.warn(Log.FRAMEWORK, "Broadcast: Non Retryable exception found. No retry will be performed.", ex);
          throw ex;
        }
        
      }
    }while(retryCount-- > 0);
    
    if(isRetryEnabled && retryCount==-1 && ex1 != null)
    {
      Log.debug(Log.FRAMEWORK, "Broadcast: JMS retry send exhausted. Dumping the out JMS message for later retry.", ex1);
      
      Hashtable<String, String> msgProps = new Hashtable<String, String>();
      msgProps.put("id", notification.getNotificationID());
      msgProps.put(SystemUtil.HOSTID_PROP_KEY, SystemUtil.getHostId());
      String[] properties = notification.getPropertyKeys();
      for (int i=0; i<properties.length; i++)
      {
        Log.debug(Log.FRAMEWORK, "Broadcast: Adding msg props : key: "+properties[i]+" value: "+ notification.getProperty(properties[i]));
        msgProps.put(properties[i], notification.getProperty(properties[i]));
      }
      
      //debug
      Hashtable<String, String> jmsConfigProps = Notifier.getInstance().getJmsSetupPropsKey();
      Log.debug(Log.FRAMEWORK, "JMSConfigProps is: "+jmsConfigProps);
      String destName = jmsConfigProps.get(JMSSender.JMS_DEST_NAME);
      
      dumpFailedJMS(JMS_DEST_TYPE_TOPIC,Notifier.getInstance().getJmsSetupPropsKey(), destName , notification, msgProps);
    }
  }
  

  /**
   * TWX 23072008 Add in broadcast with retry (not under new transaction)
   * @param notification
   * @throws Exception
   */
  public static void broadcast(INotification notification)
  	throws Exception
  {
	  boolean isRetryEnabled = _retryCount <= 0 ? false : true;
	  int retryCount = _retryCount;
	  Exception ex1 = null;
	  
	  do
	  {
	    try
	    {
	      Notifier.getInstance().broadcast(notification); 
	      retryCount = -10;
	    }
	    catch(Exception ex)
	    {
	      
	      if(isRetryEnabled && isRetryableException(ex))
	      {
	        ex1 = new JMSFailureException(ex);
	        Log.warn( Log.FRAMEWORK, "Broadcast: JMS exception found. Reconnect after "+_sleepFor/1000+" num retry left: "+retryCount, ex);
	        try
	        {
	          Thread.sleep(_sleepFor);
	        }
	        catch (Exception e) 
	        {
	        
	        }
	        Log.debug(Log.FRAMEWORK, "Broadcast: JMS exception catched: wake up !");
	      }
	      else
	      {
	        Log.warn(Log.FRAMEWORK, "Broadcast: Non Retryable exception found. No retry will be performed.", ex);
	        throw ex;
	      }
	      
	    }
	  }while(retryCount-- > 0);
	  
	  if(isRetryEnabled && retryCount==-1 && ex1 != null)
	  {
	    Log.debug(Log.FRAMEWORK, "Broadcast: JMS retry send exhausted. Throw up ex.", ex1);
	    
	    throw ex1;
	  }
  }
  
  /**
   * Broadcast the JMS msg under a new transaction. It will perform retry if encoutner retryable exception. If exceed the max retry,
   * exception will be propageted up.
   * @param notification
   * @throws Exception
   */
  public static void broadcastInNewTrans(INotification notification)
    throws Exception
  {
    boolean isRetryEnabled = _retryCount <= 0 ? false : true;
    int retryCount = _retryCount;
    Exception ex1 = null;
    
    do
    {
      try
      {
        getLocalNotifierMgr().broadCastNotification(notification); 
        retryCount = -10;
      }
      catch(Exception ex)
      {
        
        if(isRetryEnabled && isRetryableException(ex))
        {
          ex1 = new JMSFailureException(ex);
          Log.warn( Log.FRAMEWORK, "Broadcast: JMS exception found. Reconnect after "+_sleepFor/1000+" num retry left: "+retryCount, ex);
          try
          {
            Thread.sleep(_sleepFor);
          }
          catch (Exception e) 
          {
          
          }
          Log.debug(Log.FRAMEWORK, "Broadcast: JMS exception catched: wake up !");
        }
        else
        {
          Log.warn(Log.FRAMEWORK, "Broadcast: Non Retryable exception found. No retry will be performed.", ex);
          throw ex;
        }
        
      }
    }while(retryCount-- > 0);
    
    if(isRetryEnabled && retryCount==-1 && ex1 != null)
    {
      Log.debug(Log.FRAMEWORK, "Broadcast: JMS retry send exhausted. Throw up ex.", ex1);
      
      throw ex1;
    }
  }
  
  // ALGO TEST
  /*
  public static void broadcast2(INotification notification)
  throws Exception
{
  boolean isRetryEnabled =  true;
  int retryCount = 1;
  Exception ex1 = null;
  
  do
  {
    try
    {
      //getLocalNotifierMgr().broadCastNotification(notification);
      
      int i = Math.abs((new Random()).nextInt()) % 2;
      if(i == 0)
      {
        retryCount = 0;
        throw new JMSException("JMS Exception simulated ex");
      }
      else
      {
        //getLocalNotifierMgr().broadCastNotification(notification);
        System.out.println("Calling notifier mgr");
        retryCount = -10;
      }
    }
    catch(Exception ex)
    {
      
      if(isRetryEnabled && isRetryableException(ex))
      {
        ex1 = ex;
        System.out.println("Broadcast: JMS exception found. Reconnect after "+_sleepFor/1000+" num retry left: "+retryCount+" "+ex.getMessage());
        try
        {
          Thread.sleep(_sleepFor);
        }
        catch (Exception e) 
        {
        
        }
        System.out.println("Broadcast: JMS exception catched: wake up !");
      }
      else
      {
        System.out.println("Broadcast: Non JMS exception found. No retry will be performed."+ ex);
        throw ex;
      }
      
    }
  }while(retryCount-- > 0);
  
  System.out.println("RetryCount "+retryCount);
  
  if(isRetryEnabled && retryCount == -1 && isRetryableException(ex1))
  {
    System.out.println("Dumping out failed jms");
  }
} */
  
  private static INotifierMgrLocalObj getLocalNotifierMgr() throws Exception
  {
    return (INotifierMgrLocalObj)ServiceLocator.instance(
                                                        ServiceLocator.CLIENT_CONTEXT).getObj(
                                                                                              INotifierMgrLocalHome.class.getName(),
                                                                                              INotifierMgrLocalHome.class,
                                                                                              new Object[0]);
  }
  
  private static void dumpFailedJMS(String destType, Hashtable<String,String> configProps, String destName, Serializable msgObj, Hashtable msgProps) throws Exception
  {
    JMSFailedMsg failedMsg = new JMSFailedMsg(destType, configProps, destName, msgObj, msgProps);
    
    //use DB
    try
    {
      Log.debug(Log.FRAMEWORK, "Persist failed jms to DB. Failed JMS: "+failedMsg);
      JMSFailedMsgHelper helper = JMSFailedMsgHelper.getInstance();
      helper.persistFailedJMSMsg(failedMsg);
    }
    catch(Exception ex)
    {
      Log.error("", Log.FRAMEWORK, "Failed to persist the failed jms to DB.", ex);
      throw ex;
    }
  }
  
  public static boolean isRetryableException(Throwable th)
  {
    if(th == null)
    {
      Log.debug(Log.FRAMEWORK, "isRetryableException Unknown exception !");
      return false;
    }
    else if(th instanceof JMSException)
    {
      Log.debug(Log.FRAMEWORK, "isRetryableException JMSException detected.");
      return true;
    }
    else if(th instanceof NameNotFoundException)
    {
      Log.debug(Log.FRAMEWORK, "isRetryableException:  NameNotFoundException catched");
      return true;
    }
    else if(th instanceof NestingException)
    {
      NestingException nex = (NestingException)th;
      Class nextedExceptionClass = nex.getNestedExceptionClass();
      if(NameNotFoundException.class.getSimpleName().equals(nextedExceptionClass.getSimpleName()))
      {
        Log.debug(Log.FRAMEWORK, "isRetryableException: GT nestedException: NameNotFoundException catched");
        return true;
      }
      else if(JMSException.class.getSimpleName().equals(nextedExceptionClass.getSimpleName()))
      {
        Log.debug(Log.FRAMEWORK, "isRetryableException: GT nestedException: JMSException catched");
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      Throwable ex = th.getCause();
      return isRetryableException(ex);
    }
  }
  
  private static void sendJMS(boolean isLocal, Hashtable jmsSendProps, String destName, Serializable msgObj,Hashtable<String,String> msgProps) throws Exception
  {
    Log.debug(Log.FRAMEWORK, "sendJMS: isRemote:"+isLocal);
    if(! isLocal)
    {
      getRemoteJMSMgr().sendMessage(jmsSendProps, destName, msgObj, msgProps);
    }
    else
    {
      getJMSMgr().sendMessage(jmsSendProps, destName, msgObj, msgProps);
    }
  }
  
  private static IJMSMsgHandlerLocalObj getJMSMgr() throws ServiceLookupException, CreateException, RemoteException
  {
    IJMSMsgHandlerLocalHome home = (IJMSMsgHandlerLocalHome)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
                                                           IJMSMsgHandlerLocalHome.class.getName(), IJMSMsgHandlerLocalHome.class
                                                           );
    return home.create();
  }
  
  private static IJMSMsgHandlerObj getRemoteJMSMgr() throws ServiceLookupException, CreateException, RemoteException
  {
    IJMSMsgHandlerHome home = (IJMSMsgHandlerHome)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
                                                           IJMSMsgHandlerHome.class.getName(), IJMSMsgHandlerHome.class
                                                           );
    return home.create();
  }
  
  private static Hashtable<String,String> getJmsSetupPropsKey(String configName) throws ApplicationException
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(configName);
    Properties prop = config.getProperties();
    Log.debug(Log.FRAMEWORK, "getJmsSetupPropsKey: "+prop);
    
    if(prop != null && prop.size() == 0)
    {
      throw new ApplicationException("No properties file can be found given configName: "+configName);
    }
    
    Hashtable<String, String> configProps = new Hashtable<String,String>();
    Enumeration keys = prop.propertyNames();
    while(keys.hasMoreElements())
    {
      String key = (String)keys.nextElement();
      configProps.put(key, (String)prop.get(key));
    }
    
    return configProps;
  }
}
