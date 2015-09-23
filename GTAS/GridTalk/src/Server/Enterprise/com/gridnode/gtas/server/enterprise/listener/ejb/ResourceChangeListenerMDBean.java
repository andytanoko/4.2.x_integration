/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceChangeListenerMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 13 2002    Neo Sok Lay         Created
 * Sep 30 2002    Neo Sok Lay         Delegate to EntityResourceMapping to
 *                                    determine resource type.
 * Jan 07 2004    Neo Sok Lay         Delegate sharedResource updates to
 *                                    SyncResourceDelegate on BusinessEntity updated/created.
 * Mar 24 2004    Neo Sok Lay         Remove resourcelink for Partner if Partner
 *                                    state is changed to Deleted.
 * Nov 18 2005    Neo Sok Lay         Delegate the handlers to ResourceChangeDelegate.                                   
 */
package com.gridnode.gtas.server.enterprise.listener.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.server.enterprise.helpers.EntityResourceMapping;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ResourceChangeDelegate;
import com.gridnode.pdip.framework.db.entity.EntityEvent;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This is a message-driven bean that, on receiving a message of EntityEvent
 * topic, will handle the events by delegating to the EnterpriseHierarchyManagerBean
 * to perform specific tasks based on the events.
 *
 * @author Neo Sok Lay
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class ResourceChangeListenerMDBean
  implements MessageDrivenBean, MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1468446569636323483L;
	private MessageDrivenContext _mdx = null;

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove()
  {
    _mdx = null;
  }

  public void ejbCreate()
  {
  }

  /**
   * On message received, assumes that the message is an ObjectMessage containing
   * an EntityEvent object. Otherwise, the message is ignored.<p>
   *
   * @param msg The received message.
   */
  public void onMessage(Message msg)
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {msg};

    try
    {
      logger.logEntry(methodName, params);

      EntityEvent event = (EntityEvent)((ObjectMessage)msg).getObject();

      IEntity entity = event.getEntity();
      handleChange(event.getEventType(), entity);
    }
    catch (Throwable ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handles an entity event. Reflection is used to invoke the concrete method
   * to handle the entity event.
   *
   * @param eventType The type of the event.
   * @param entity The entity that is manipulateed.
   * @throws Throwable
   */
  private void handleChange(int eventType, IEntity entity)
    throws Throwable
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handleChange";
    Object[] params     = new Object[]
                          {
                            new Integer(eventType),
                            entity
                          };

    try
    {
      logger.logEntry(methodName, params);

      String opType = getOpType(eventType);
      String resType = getResourceType(entity.getEntityName());
      String handlerMethod = "handle"+resType+opType;
      //Method method = getClass().getDeclaredMethod(
      Method method = ResourceChangeDelegate.class.getDeclaredMethod(
                        handlerMethod,
                        new Class[] {IEntity.class});
      //method.invoke(this, new Object[] {entity});
      method.invoke(new ResourceChangeDelegate(), new Object[] {entity});
    }
    catch (NoSuchMethodException ex)
    {
      //no method defined to handle change in resource
      logger.logMessage(methodName, null, "No such method: "+ex.getMessage());
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Get the operation type based on the event type.
   * @param eventType The type of the event.
   * @return The operation type determined from the event type. Only interested
   * event types are returned with valid operation types. Other event types are
   * returned with "Unknown".
   */
  private String getOpType(int eventType)
  {
    String type = "Unknown";

    switch (eventType)
    {
      case EntityEvent.CREATED: type = "Created"; break;
      case EntityEvent.DELETED: type = "Deleted"; break;
      case EntityEvent.UPDATED: type = "Updated"; break;
    }

    return type;
  }

  /**
   * Get the resource type based on the entity name.
   *
   * @param entityName The name of the entity.
   * @return The resource type determined from the entity name. If the entity
   * is not of interest as a resource type, "UnknownRes" would be returned.
   */
  private String getResourceType(String entityName)
  {
    /* 300902NSL Delegate to EntityResourceMapping class to determine the resource
       type.
    String resType = "UnknownRes";

    if (BusinessEntity.ENTITY_NAME.equals(entityName))
    {
      resType = IResourceTypes.BUSINESS_ENTITY;
    }
    else if (Partner.ENTITY_NAME.equals(entityName))
    {
      resType = IResourceTypes.PARTNER;
    }
    else if (UserAccount.ENTITY_NAME.equals(entityName))
    {
      resType = IResourceTypes.USER;
    }
    else if (ChannelInfo.ENTITY_NAME.equals(entityName))
    {
      resType = IResourceTypes.CHANNEL;
    }
    else if (GridNode.ENTITY_NAME.equals(entityName))
    {
      resType = IResourceTypes.GRIDNODE;
    }

    return resType;
    */
    return EntityResourceMapping.getInstance().getResourceType(entityName);
  }


}
