/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Notifier.java
 * Refactored from com.gridnode.gtas.server.notify.Notifier.java
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 02 2003    Neo Sok Lay         Created
 * Jan 31 2007    Neo Sok Lay         Use local context for notifier
 *                                    Change topic to generic destination.
 * Feb 07 2007		Alain Ah Ming				Use new error codes
 * Feb 14 2007    Neo Sok Lay         Set hostid for message selection.
 * May 16 2007    Tam Wei Xiang       GNDB00028357 Fixed the issue where the broadcasting
 *                                    of the jms message is failed due to there
 *                                    is an underlying DB transaction rollback.
 *                                    We will be associating a new transaction while
 *                                    broadcasting the jms message
 * July 23 2008   Tam Wei Xiang       #69: Rollback the change in GNDB00028357, and also preserve
 *                                         the logic of broadcasting under new transaction.
 */
package com.gridnode.pdip.framework.notification;

import java.util.Hashtable;
import java.util.Properties;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.ObjectMappingRegistry;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.jms.JMSSender;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.notification.ejb.INotifierMgrHome;
import com.gridnode.pdip.framework.notification.ejb.INotifierMgrLocalHome;
import com.gridnode.pdip.framework.notification.ejb.INotifierMgrLocalObj;
import com.gridnode.pdip.framework.notification.ejb.INotifierMgrObj;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.SystemUtil;

import javax.jms.*;
import javax.naming.NamingException;

/**
 * This class handles broadcasting notifications to a Notifier topic. Interested
 * parties would need to implement a Message Driven Bean or a Message Listener
 * and subscribe to the Notifier topic.<p>
 *
 * When Notifier is being initialized, it reads from the <b>notifier
 * configuration file</b> the following properties:<p>
 * <li><code>jms.connection.jndi</code> - the jndi name to lookup the JMS connection
 * factory.</li>
 * <li><code>notifier.dest.jndi</code> - the jndi name to lookup the destination for
 * Notifier.</li>
 *
 * The <b>notifier configuration file</b> will be obtained from the
 * {@link ConfigurationManager} via the <code>getConfig(config_name)</code>
 * method whereby the <code>config_name</code> is "notifier". Thus this entry
 * have to be specified in the <b>config.properties</b> file for the application.
 *
 * @author Neo Sok Lay
 * @version GT 4.0 VAN
 * @since GT 2.2
 */
public class Notifier
{
  private static final String LOG_CAT            = "NOTIFIER";
  private static final String CONFIG_NAME        = "notifier";
  private static final String CONNECTION_JNDI    = "jms.connection.jndi";
  private static final String NOTIFIER_DEST_JNDI = "notifier.dest.jndi";
  
  private ConnectionFactory _factory;
  //private Connection _connection;
  private Destination _dest;

  private static final Notifier _sender = new Notifier();
  private static Hashtable<String, String> _jmsSetupPropsKey;
  
  protected Notifier()
  {
  	String mn = "<init>";
    try
    {
      //initialise the configurations for sending notifications
      Configuration config = ConfigurationManager.getInstance().getConfig(
                               CONFIG_NAME);
      
      _factory = (ConnectionFactory)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
                   config.getString(CONNECTION_JNDI), ConnectionFactory.class);
      _dest = (Destination)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
                   config.getString(NOTIFIER_DEST_JNDI),
                   Destination.class);
      _jmsSetupPropsKey = new Hashtable<String, String>();
      _jmsSetupPropsKey.put(JMSSender.JNDI_SERVICE, ServiceLocator.CLIENT_CONTEXT);
      _jmsSetupPropsKey.put(JMSSender.CONN_FACTORY, config.getString(CONNECTION_JNDI));
      _jmsSetupPropsKey.put(JMSSender.JMS_DEST_NAME,config.getString(NOTIFIER_DEST_JNDI));
      
      //_connection = _factory.createConnection();
    }
    
		catch (ServiceLookupException ex)
		{
      logError(ILogErrorCodes.NOTIFICATION_GENERIC,
               mn,
               "Failed to initialize JNDI Finder. Error: "+ex.getMessage(),
               ex);
		}
    catch (Throwable ex)
    {
      logError(ILogErrorCodes.NOTIFICATION_GENERIC,
               mn,
               "Unexpected Error: "+ex.getMessage(),
               ex);
    }
  }

  /**
   * Get the single instance of the Notifier.
   *
   * @return An instance of the Notifier.
   */
  public static Notifier getInstance()
  {
    return _sender;
  }

  /**
   * Ask the Notifier to broadcast the specified Notification. A new transaction(with transaction attr 'RequiresNew' )
   * will be created while broadcasting the notification. 
   * 
   * The lookup of the handler will be based on local jndi
   *
   * @param notification The notification message to broadcast.
   */
  public void broadcastInNewTrans(INotification notification)
    throws Exception
  {
    getLocalNotifierMgr().broadCastNotification(notification);
  }
  
  /**
   * Ask the Notifier to broadcast the specified Notification. A new transaction(with transaction attr 'RequiresNew' )
   * will be created while broadcasting the notification.
   * 
   * With support for local jndi lookup or remote jndi lookup.
   * 
   * @param notification The notification message to broadcast.
   * @param isLocal local jndi lookup if true. Otherwise, remote jndi lookup
   * @throws Exception
   */
  public void broadcastInNewTrans(INotification notification, boolean isLocal)
    throws Exception
  {
    if(isLocal)
    {
      getLocalNotifierMgr().broadCastNotification(notification);
    }
    else
    {
      getNotifierMgr().broadCastNotification(notification);
    }
  }
  
  /**
   * Direct broadcasting the notification without going through an additional transaction. 
   * @param notification The notification message to broadcast.
   * @throws Exception
   */
  public void broadcast(INotification notification) throws Exception
  {
    if (_dest == null)
    {
      throw new Exception("Notification not sent: Notifier not configured properly");
    }
    
    Session session = null;
    Connection conn = null;
    try
    {
      conn = _factory.createConnection();
      session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer producer = session.createProducer(_dest);
      ObjectMessage msg = session.createObjectMessage();
      msg.setObject(notification);

      // setup the message selector options
      msg.setStringProperty("id", notification.getNotificationID());
      msg.setStringProperty(SystemUtil.HOSTID_PROP_KEY, SystemUtil.getHostId()); //NSL20070214
      String[] properties = notification.getPropertyKeys();
      for (int i=0; i<properties.length; i++)
        msg.setStringProperty(properties[i], notification.getProperty(properties[i]));

      producer.send(msg);
      Log.debug(LOG_CAT, "Notification is " + notification);
    }
    catch(Exception th)
    {
      Log.warn(LOG_CAT, "Error in broadcast !", th);
      throw th;
    }
    /*
    catch (JMSException ex)
    {
      Log.warn(LOG_CAT, "Error in broadcast !", ex);
      throw new Exception(ex.toString());
    }*/
    finally
    {
      if (conn != null)
      {
        try
        {
          conn.close();
        }
        catch (Exception ex)
        {
          Log.warn(LOG_CAT, "Error in close session !", ex);
        }
      }
    }
  }
  
  private static INotifierMgrObj getNotifierMgr() throws Exception
  {
    return (INotifierMgrObj)ServiceLocator.instance(
                                                        ServiceLocator.CLIENT_CONTEXT).getObj(
                                                                                              INotifierMgrHome.class.getName(),
                                                                                              INotifierMgrHome.class,
                                                                                              new Object[0]);
  }
  
  private static INotifierMgrLocalObj getLocalNotifierMgr() throws Exception
  {
    return (INotifierMgrLocalObj)ServiceLocator.instance(
                                                        ServiceLocator.CLIENT_CONTEXT).getObj(
                                                                                              INotifierMgrLocalHome.class.getName(),
                                                                                              INotifierMgrLocalHome.class,
                                                                                              new Object[0]);
  }
  
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(Notifier.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
  	Log.error(errorCode, LOG_CAT, buf.toString(), t);
  }

  /*
  protected void finalize() throws java.lang.Throwable
  {
    if (_connection != null)
    {
      try
      {
        _connection.close();
      }
      catch (Exception ex)
      {
        Log.warn(LOG_CAT, "Error in close connection !", ex);
      }

    }
  }*/
  
  public Hashtable<String, String> getJmsSetupPropsKey()
  {
    return _jmsSetupPropsKey;
  }
}