/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityEventSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 13 2002    Neo Sok Lay         Created
 * Jan 19 2006    Neo Sok Lay         Use ServiceLocator for JNDI Lookup.
 * Jan 31 2007    Neo Sok Lay         Change Topic to generic destination.
 *                                    Use local context for lookup.
 * Feb 07 2007		Alain Ah Ming				Use new error codes
 * Feb 14 2007    Neo Sok Lay         Add hostid for message selection.
 */
package com.gridnode.pdip.framework.db.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.entity.EntityEvent;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.SystemUtil;

import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.jms.*;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import java.util.List;

/**
 * This class handles the notification of EntityEvent(s) via Jms messaging.
 * There should need to have only one instance of EntityEventSender in an
 * application to handle such task.<p>
 *
 * When EntityEventSender is being initialized, it reads from the <b>entity
 * configuration file</b> the following properties:<p>
 * <li><code>jms.connection.jndi</code> - the jndi name to lookup the JMS connection
 * factory.</li>
 * <li><code>event.dest.jndi</code> - the jndi name to lookup the destination for
 * entity events.</li>
 * <li><code>interested.entities</code> - list the names of the entities that
 * are of interest (separated by comma). Only events pertaining to the listed
 * entities will be sent.</li>
 *
 * The <b>entity configuration file</b> will be obtained from the
 * {@link ConfigurationManager} via the <code>getConfig(config_name)</code>
 * method whereby the <code>config_name</code> is "entity". Thus this entry
 * have to be specified in the <b>config.properties</b> file for the application.
 *
 *
 * @author Neo Sok Lay
 * @version GT 4.0 VAN
 */
public class EntityEventSender
{
  private static final String LOG_CAT            = "DB.EVENT.SENDER";
  private static final String ENTITY_CONFIG_NAME = "entity";
  private static final String CONNECTION_JNDI    = "jms.connection.jndi";
  private static final String EVENT_DEST_JNDI   = "event.dest.jndi";
  private static final String INTERESTED_ENTITIES= "interested.entities";

  private ConnectionFactory _factory;
  //private Connection _connection;
  private Destination _eventDest;
  private List _interestedEntities;

  private static EntityEventSender _sender = null;
  private static final Object _lock = new Object();

  private EntityEventSender()
  {
    try
    {
      //initialise the configurations for sending entity events.
      Configuration config = ConfigurationManager.getInstance().getConfig(
                               ENTITY_CONFIG_NAME);
      /*
      InitialContext context = ServiceLookup.getInstance(
                                 ServiceLookup.LOCAL_CONTEXT).getInitialContext();
      _factory = (TopicConnectionFactory)context.lookup(
                   config.getString(CONNECTION_JNDI));
      _connection = _factory.createTopicConnection();
      _eventTopic = (Topic)context.lookup(config.getString(EVENT_TOPIC_JNDI));
      */
      //NSL20070131
      JNDIFinder finder = ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getJndiFinder();
      _factory = (ConnectionFactory)finder.lookup(config.getString(CONNECTION_JNDI), ConnectionFactory.class);
      //_connection = _factory.createConnection();
      _eventDest = (Destination)finder.lookup(config.getString(EVENT_DEST_JNDI), Destination.class);

      _interestedEntities = config.getList(INTERESTED_ENTITIES, ",");
      Log.log(LOG_CAT, "InteerestedEntities:"+_interestedEntities);
    }
		catch (ServiceLookupException e)
		{
			Log.error(ILogErrorCodes.ENTITY_EVENT_SENDER_INIT, LOG_CAT, "JNDIFinder lookup failure: "+e.getMessage(),e);
		}
		catch (NameNotFoundException e)
		{
			Log.error(ILogErrorCodes.ENTITY_EVENT_SENDER_INIT, LOG_CAT, "JNDI lookup failure: "+e.getMessage(),e);
		}
		catch (NamingException e)
		{
			Log.error(ILogErrorCodes.ENTITY_EVENT_SENDER_INIT, LOG_CAT, "JNDI lookup failure: "+e.getMessage(),e);
		}
    catch (Throwable ex)
    {
      Log.error(
                ILogErrorCodes.ENTITY_EVENT_SENDER_INIT,
        LOG_CAT,
        "Unexpected error: "+ex.getMessage(),
        ex);
    }
  }

  /**
   * Get the single instance of the EntityEventSender.
   *
   * @return An instance of the EntityEventSender.
   */
  public static EntityEventSender getInstance()
  {
    if (_sender == null)
    {
      synchronized (_lock)
      {
        if (_sender == null)
          _sender = new EntityEventSender();
      }
    }
    return _sender;
  }

  /**
   * Send an EntityEvent for created entity.
   *
   * @param source The source from which the event is initiated from.
   * @param entity The created entity.
   * @param ctx The EJBContext from which this send is initiated, if any. This
   * is used for transacted messaging.
   */
  public void sendCreatedEvent(Object source, IEntity entity, EJBContext ctx)
  {
    if (isOfInterest(entity.getEntityName()))
    {
      EntityEvent event = new EntityEvent(source, EntityEvent.CREATED, entity);
      sendEvent(event, ctx, false);
    }
  }

  /**
   * Send an EntityEvent for deleted entity.
   *
   * @param source The source from which the event is initiated from.
   * @param entity The deleted entity.
   * @param ctx The EJBContext from which this send is initiated, if any. This
   * is used for transacted messaging.
   */
  public void sendDeletedEvent(Object source, IEntity entity, EJBContext ctx)
  {
    if (isOfInterest(entity.getEntityName()))
    {
      EntityEvent event = new EntityEvent(source, EntityEvent.DELETED, entity);
      sendEvent(event, ctx, false);
    }
  }

  /**
   * Send an EntityEvent for updated entity.
   *
   * @param source The source from which the event is initiated from.
   * @param entity The updated entity (with new values).
   * @param ctx The EJBContext from which this send is initiated, if any. This
   * is used for transacted messaging.
   */
  public void sendUpdatedEvent(Object source, IEntity entity, EJBContext ctx)
  {
    if (isOfInterest(entity.getEntityName()))
    {
      EntityEvent event = new EntityEvent(source, EntityEvent.UPDATED, entity);
      sendEvent(event, ctx, false);
    }
  }

  /**
   * Send an EntityEvent to interested listeners for changes to the type
   * of entity.
   *
   * @param event The EntityEvent object.
   * @param ctx The EJB context from which this method is invoked.
   * @param transacted <b>true<b> to use transacted sessions.
   */
  public void sendEvent(EntityEvent event, EJBContext ctx, boolean transacted)
  {
    if (_eventDest == null)
    {
      Log.error(ILogErrorCodes.ENTITY_EVENT_SENDER_SEND, LOG_CAT, "No JMS destination found");
      return;
    }

    Session session = null;
    Connection conn = null;
    try
    {
      conn = _factory.createConnection();
      session = conn.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
      MessageProducer producer = session.createProducer(_eventDest);
      ObjectMessage msg = session.createObjectMessage();
      msg.setObject(event);
      msg.setIntProperty("EventType", event.getEventType());
      msg.setStringProperty("EntityType", event.getEntity().getEntityName());
      msg.setStringProperty(SystemUtil.HOSTID_PROP_KEY, SystemUtil.getHostId()); //NSL20070214
      producer.send(msg);
      Log.debug(LOG_CAT, "Message sent is " + msg);
    }
    catch (JMSException ex)
    {
      Log.warn(LOG_CAT, "Error in sendEvent !", ex);
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
          Log.warn(LOG_CAT, "Error in close connection !", ex);
        }
      }
    }
  }

  /**
   * Checks if an entity is of interest by any listener for its events.
   *
   * @param entityName The name of the entity.
   * @return <b>true</b> if the <code>entityName</code> is set in the
   * <code>interested.entities</code> property of the <b>entity configuration
   * file</b>.
   */
  protected boolean isOfInterest(String entityName)
  {
    return (_interestedEntities==null?false:_interestedEntities.contains(entityName));
  }

  /*NSL20070206
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


}
