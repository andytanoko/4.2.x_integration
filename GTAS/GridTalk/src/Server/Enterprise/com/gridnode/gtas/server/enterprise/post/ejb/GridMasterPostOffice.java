/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridMasterPostOffice.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay         Created
 * Nov 06 2002    Neo Sok Lay         Moved from GridNode module.
 * Nov 27 2002    Neo Sok Lay         Change from Queue to Topic
 *                                    *** suspected bug in JBossMQ for Queues
 *                                    *** often throw a unsupported JMSPriority
 *                                    *** warning and no message can be sent
 *                                    *** subsequently.
 *                                    *** Should be changed back to Queue if
 *                                    *** the release version does not have this
 *                                    *** problem.
 * Jan 23 2003    Neo Sok Lay         GNDB00012554: Extra createTopicConnection()
 *                                    which is not closed --> causes resources
 *                                    to be exhausted.
 * Jan 19 2007    Neo Sok Lay         Change to use ServiceLocator for JNDI Lookup.                                   
 */
package com.gridnode.gtas.server.enterprise.post.ejb;

import javax.jms.*;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.enterprise.post.PostInstruction;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class handles the sending messages to a topic, waiting to be sent to
 * the GridMaster.<p>
 *
 * When GridMasterPostOffice is being initialized, it reads from the <b>gridmaster
 * postman configuration file</b> the following properties:<p>
 * <li><code>jms.connection.jndi</code> - the jndi name to lookup the JMS connection
 * factory.</li>
 * <li><code>postman.topic.jndi</code> - the jndi name to lookup the topic for
 * GridMaster PostOffice.</li>
 *
 * The <b>gridmaster postman configuration file</b> will be obtained from the
 * {@link ConfigurationManager} via the <code>getConfig(config_name)</code>
 * method whereby the <code>config_name</code> is "gm.postman". Thus this entry
 * have to be specified in the <b>config.properties</b> file for the application.
 *
 *
 * @author Neo Sok Lay
 * @version 2.0 I6
 */
public class GridMasterPostOffice
{
  private static final String LOG_CAT            = "GM.POSTMAN";
  private static final String CONFIG_NAME        = "gm.postman";
  private static final String CONNECTION_JNDI    = "jms.connection.jndi";
  private static final String POSTMAN_TOPIC_JNDI = "postman.topic.jndi";

  private TopicConnectionFactory _factory;
  private TopicConnection _connection;
  private Topic _topic;

  private static GridMasterPostOffice _sender = null;
  private static final Object _lock = new Object();

  private GridMasterPostOffice()
  {
    try
    {
      //initialise the configurations for sending entity events.
      Configuration config = ConfigurationManager.getInstance().getConfig(
                               CONFIG_NAME);
      //NSL20070119 Change to use ServiceLocator, and Client context
      JNDIFinder finder = ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getJndiFinder();
      _factory = (TopicConnectionFactory)finder.lookup(config.getString(CONNECTION_JNDI), TopicConnectionFactory.class);
      _topic = (Topic)finder.lookup(config.getString(POSTMAN_TOPIC_JNDI), Topic.class);
      /*
      InitialContext context = ServiceLookup.getInstance(
                                 ServiceLookup.LOCAL_CONTEXT).getInitialContext();
      _factory = (TopicConnectionFactory)context.lookup(
                   config.getString(CONNECTION_JNDI));
      _topic = (Topic)context.lookup(config.getString(POSTMAN_TOPIC_JNDI)); */
      _connection = _factory.createTopicConnection();

    }
    catch (Throwable ex)
    {
      Log.error(ILogErrorCodes.GT_ENTERPRISE_CONNECTION_ERROR,
        LOG_CAT,
        "Error in establish JMS connection! No GridMaster PostOffice configured.",
        ex);
    }
  }

  /**
   * Get the single instance of the GridMasterPostOffice.
   *
   * @return An instance of the GridMasterPostOffice.
   */
  static GridMasterPostOffice getInstance()
  {
    if (_sender == null)
    {
      synchronized (_lock)
      {
        if (_sender == null)
          _sender = new GridMasterPostOffice();
      }
    }
    return _sender;
  }

  /**
   * Submit a Post Instruction to the GridMaster PostOffice topic.
   *
   * @param instruction The PostInstruction.
   */
  void post(PostInstruction instruction)
    throws Exception
  {
    if (_connection == null || _topic == null)
    {
      throw new Exception("Message not sent: PostOffice not configured properly");
    }

    TopicSession session = null;
    try
    {
      //_connection = _factory.createTopicConnection();
      session = _connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
      TopicPublisher sender = session.createPublisher(_topic);
      ObjectMessage msg = session.createObjectMessage();
      msg.setObject(instruction);
      msg.setStringProperty("eventId", instruction.getEventID());
      //** may add other properties for selector later on.
      sender.publish(msg);
      Log.debug(LOG_CAT, "Post Instruction is " + instruction);
    }
    catch (JMSException ex)
    {
      Log.warn(LOG_CAT, "Error in post !", ex);
      throw new Exception(ex.toString());
    }
    finally
    {
      if (session != null)
      {
        try
        {
          session.close();
        }
        catch (Exception ex)
        {
          Log.warn(LOG_CAT, "Error in close session !", ex);
        }
      }
    }
  }

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


}