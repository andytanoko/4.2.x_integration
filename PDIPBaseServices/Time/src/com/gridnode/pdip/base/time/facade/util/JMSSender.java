// %1023788047887:com.gridnode.pdip.base.time.util%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 * Jan 19 2007    Neo Sok Lay         Use ServiceLocator for JNDI lookup
 * Jan 29 2007    Neo Sok Lay         Change to use generic Destination instead of Topic
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.facade.util;

import com.gridnode.pdip.base.time.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.jms.*;

public class JMSSender
{
  private static final String LogCat = "BASE.TIME";
  private static final String JMS = "time.jms";
  private static final String CONNECTION_JNDI = "jms.connection.jndi";
  private static final String SCHEDULE_JNDI = "time.schedule.jndi";
  private static final String INVOKE_JNDI = "time.invoke.jndi";
  private static final String MISSED_JNDI="time.missed.jndi";

  private static Configuration _jmsConfig;
  ConnectionFactory factory;
  //Connection connection;
  public Destination scheduleDest;
  public Destination invokeDest;
  public Destination missedDest;
  private static JMSSender _sender = null;

  static
  {
    try
    {
      loadJMSConfig();
    }
    catch (Exception ex)
    {
      Log.error(ILogErrorCodes.TIME_JMSSENDER_INITIALIZATION,
                LogCat, "[JMSSerder.init] Error in loading JMS Config from file!", ex);
    }
  }

  protected static void loadJMSConfig()
                               throws Exception
  {
    _jmsConfig = ConfigurationManager.getInstance().getConfig(JMS);
    Log.debug(LogCat, "Jms Config="+_jmsConfig.toString());

  }

  private JMSSender()
  {
    try
    {
      /*
      InitialContext context = ServiceLookup.getInstance(
                                   ServiceLookup.LOCAL_CONTEXT).getInitialContext();
      factory = (TopicConnectionFactory)context.lookup(
                    _jmsConfig.getString(CONNECTION_JNDI));
      connection = factory.createTopicConnection();
      scheduleTopic = (Topic)context.lookup(
                          _jmsConfig.getString(SCHEDULE_JNDI));
      missedTopic = (Topic)context.lookup(_jmsConfig.getString(MISSED_JNDI));
      invokeTopic = (Topic)context.lookup(_jmsConfig.getString(INVOKE_JNDI));
      */
      JNDIFinder finder = ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getJndiFinder();
      factory = (ConnectionFactory)finder.lookup(_jmsConfig.getString(CONNECTION_JNDI), ConnectionFactory.class);
      //connection = factory.createConnection();
      scheduleDest = (Destination)finder.lookup(_jmsConfig.getString(SCHEDULE_JNDI), Destination.class);
      missedDest = (Destination)finder.lookup(_jmsConfig.getString(MISSED_JNDI), Destination.class);
      invokeDest = (Destination)finder.lookup(_jmsConfig.getString(INVOKE_JNDI), Destination.class);
    }
    catch (Throwable ex)
    {
      Log.warn(LogCat, "Error in establish JMS connection!", ex);
      throw new EJBException(ex.toString());
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  static public JMSSender instance()
  {
    if (_sender == null)
    {
      synchronized (JMSSender.class)
      {
        if (_sender == null)
          _sender = new JMSSender();
      }
    }
    return _sender;
  }

  /**
   * DOCUMENT ME!
   *
   * @param queue DOCUMENT ME!
   * @param value DOCUMENT ME!
   * @param ctx DOCUMENT ME!
   */
  public void sendMsgTo(Destination dest, Serializable value, EJBContext ctx)
  {
    sendMsgTo(dest, value, null, ctx);
  }

  /**
   * DOCUMENT ME!
   *
   * @param queue DOCUMENT ME!
   * @param value DOCUMENT ME!
   * @param propertyMap DOCUMENT ME!
   * @param ctx DOCUMENT ME!
   */
  public void sendMsgTo(Destination dest, Serializable value, HashMap propertyMap,
                        EJBContext ctx)
  {
    Session session = null;
    Connection conn = null;
    try
    {
      conn = factory.createConnection();
      session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
      MessageProducer sender = session.createProducer(dest);
      ObjectMessage msg = session.createObjectMessage();
      if (propertyMap != null && !propertyMap.isEmpty())
      {
        Set mapSet = propertyMap.entrySet();
        Iterator iter = mapSet.iterator();
        while (iter.hasNext())
        {
          Map.Entry element = (Map.Entry)iter.next();
          String ekey = (String)element.getKey();
          String evalue = (String)element.getValue();
          msg.setStringProperty(ekey, evalue);
        }
      }
      msg.setObject(value);
      sender.send(msg);
      Log.debug(LogCat, "msgsend is " + msg);
    }
    catch (JMSException ex)
    {
      Log.warn(LogCat, "Error in sendMsgTo !", ex);
      if (ctx != null)
        ctx.setRollbackOnly();
      throw new EJBException(ex.toString());
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
          Log.warn(LogCat, "Error in close session !", ex);
        }
      }
    }
  }
}