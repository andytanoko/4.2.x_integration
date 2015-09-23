/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentFlowNotifyMgr.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15, 2007    Tam Wei Xiang       Created
 * Nov 21, 2007    Tam Wei Xiang       Make the connection factory being initialised
 *                                     in the static block instead of ejbCreate().
 *                                     This is necessary since at the time of ejbCreate(),
 *                                     the fail over can happen and cause the lookup
 *                                     on the connectionFactory, destination failed.
 */
package com.gridnode.pdip.framework.notification.ejb;

import java.rmi.RemoteException;
import javax.ejb.SessionContext;


import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.ILog;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.INotification;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.SystemUtil;

/**
 * This class broadcast the INotification and ensure the jms session is under a new jta transaction.
 * Main purpose is to resolve the defect GNDB00028357
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class NotifierMgrBean implements SessionBean
{
  private SessionContext _ctxt;
  private static String LOG_CATEGORY = ILog.NOTIFIER;
  private static final String CONFIG_NAME        = "notifier";
  private static final String CONNECTION_JNDI    = "jms.connection.jndi";
  private static final String NOTIFIER_DEST_JNDI = "notifier.dest.jndi";
  private static final String LOG_CAT            = "NOTIFIER";
  private static ConnectionFactory _factory;
  private static Destination _dest;
  
  static
  {
      System.out.println("Static init the notifier mgr bean");
      init(); //TWX: put as static init block instead of the ejbCreate() method due to
              //the ejb instance can be created by the container during the fail over time.
              //The JMS destination may not be able to be lookup and that instance
              //will not be usabled.
  }
  
  public void broadCastNotification(INotification notification) throws Exception
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
    catch (JMSException ex)
    {
      Log.warn(LOG_CAT, "Error in broadcast !", ex);
      throw new Exception(ex.toString(), ex); //so that jms retry sender can correctly catch the exception 
    }
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
  
  private static void init()
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
  
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
    StringBuffer buf = new StringBuffer("[");
    buf.append(DocumentFlowNotifyHandler.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
    Log.error(errorCode, LOG_CATEGORY, buf.toString(), t);
  }
  
  public void ejbCreate() throws CreateException
  {
  }
  
  public void ejbActivate() throws EJBException, RemoteException
  {
  }

  public void ejbPassivate() throws EJBException, RemoteException
  {
  }

  public void ejbRemove() throws EJBException, RemoteException
  {
  }

  public void setSessionContext(SessionContext ctxt) throws EJBException,
                                                    RemoteException
  {
    _ctxt = ctxt;
  }

}
