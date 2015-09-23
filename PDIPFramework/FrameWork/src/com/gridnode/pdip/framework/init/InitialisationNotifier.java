/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InitialisationNotifier.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 04 2003    Neo Sok Lay         Created
 * Jan 19 2007    Neo Sok Lay         Ignore the Initial Context passed into Constructor.
 *                                    Use ServiceLocator for JNDI lookup.
 * Jan 31 2007    Neo Sok Lay         Change Topic to generic Destination                                   
 * Feb 07 2007		Alain Ah Ming				Use new error codes
 * Feb 14 2007    Neo Sok Lay         Add hostid for message selection.
 */
package com.gridnode.pdip.framework.init;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.SystemUtil;

/**
 * This class handles broadcasting of application initialisation command to
 * all interested modules. This allow the respective modules to do some initialisation
 * work that require resume from last stopped state.<p>
 * Interested modules would need to implement a Message Driven Bean or a
 * Message Listener and subscribe to the InitialisationNotifier topic.<p>
 *
 * When InitialisationNotifier is being initialized, it reads from the <b>initialiser
 * configuration file</b> the following properties:<p>
 * <li><code>jms.connection.jndi</code> - the jndi name to lookup the JMS connection
 * factory.</li>
 * <li><code>notifier.dest.jndi</code> - the jndi name to lookup the destination for
 * Notifier.</li>
 *
 * The <b>initialiser configuration file</b> will be obtained from the
 * {@link ConfigurationManager} via the <code>getConfig(config_name)</code>
 * method whereby the <code>config_name</code> is "notifier". Thus this entry
 * have to be specified in the <b>config.properties</b> file for the application.
 *
 *
 * @author Neo Sok Lay
 * @version GT 4.0 VAN
 */
public class InitialisationNotifier
{
  private static final String LOG_CAT            = "INITALISER";
  private static final String CONFIG_NAME        = "initialiser";
  private static final String CONNECTION_JNDI    = "jms.connection.jndi";
  private static final String INITIALISER_DEST_JNDI = "initialiser.dest.jndi";

  private ConnectionFactory _factory;
  //private Connection _connection;
  private Destination _dest;

  //private static InitialisationNotifier _sender = null;
  //private static final Object _lock = new Object();

  public InitialisationNotifier(InitialContext ctx)
  {
  	String mn = "<init>";
    try
    {
      Configuration config = ConfigurationManager.getInstance().getConfig(
                               CONFIG_NAME);

      
      JNDIFinder finder = ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getJndiFinder();
      /*
      _factory = (TopicConnectionFactory)finder.lookup(config.getString(CONNECTION_JNDI), TopicConnectionFactory.class);
      _topic = (Topic)finder.lookup(config.getString(INITIALISER_TOPIC_JNDI), Topic.class);
      _connection = _factory.createTopicConnection();
      */
      _factory = (ConnectionFactory)finder.lookup(config.getString(CONNECTION_JNDI));
      _dest = (Destination)finder.lookup(config.getString(INITIALISER_DEST_JNDI));
    }
		catch (ServiceLookupException ex)
		{
      logError(ILogErrorCodes.INITIALISATION_NOTIFIER_INIT,
               mn,
               "Failed to initialize JNDI Finder. Error: "+ex.getMessage(),
               ex);
		}
		catch (NamingException ex)
		{
      logError(ILogErrorCodes.INITIALISATION_NOTIFIER_INIT,
               mn,
               "Failed to look up JMS connection factory or destination. Error: "+ex.getMessage(),
               ex);
		}
    catch (Throwable ex)
    {
      logError(ILogErrorCodes.INITIALISATION_NOTIFIER_INIT,
               mn,
               "Unexpected Error: "+ex.getMessage(),
               ex);
    }
  }

  /**
   * Get the single instance of the Notifier.
   *
   * @return An instance of the Notifier.
   *//*
  public static InitialisationNotifier getInstance(InitialContext ctx)
  {
    if (_sender == null)
    {
      synchronized (_lock)
      {
        if (_sender == null)
          _sender = new InitialisationNotifier(ctx);
      }
    }
    return _sender;
  }*/

  /**
   * Ask the Notifier to broadcast the specified Notification.
   *
   * @param instruction The PostInstruction.
   */
  public void broadcastInitialisation()
    throws Exception
  {
    if (_dest == null)
    {
      throw new Exception("Initialisation command not sent: Initialiser not configured properly");
    }

    Session session = null;
    Connection conn = null;
    try
    {
      conn = _factory.createConnection();
      session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer producer = session.createProducer(_dest);
      Message msg = session.createMessage();

      // setup the message selector options
      msg.setStringProperty("command", "START");
      msg.setStringProperty(SystemUtil.HOSTID_PROP_KEY, SystemUtil.getHostId()); //NSL20070214

      producer.send(msg);
    }
    catch (JMSException ex)
    {
      Log.warn(LOG_CAT, "Error in broadcast !", ex);
      throw new Exception(ex.toString());
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
          Log.warn(LOG_CAT, "Error in close connection !", ex);
        }
      }
    }
  }
  
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(InitialisationNotifier.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
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
  }
  */
}
