/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultMessageTemplateManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created (so long ago! sure it wasnt 2003?)
 * 2003-05-14     Andrew Hill         Handle log messageType create and updates
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2005-01-05			SC									Add method newMessageProperty
 * 2006-05-08     Neo Sok Lay         Hide P2P message templates
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.Logger;
import com.gridnode.gtas.events.alert.CreateMessageTemplateEvent;
import com.gridnode.gtas.events.alert.DeleteMessageTemplateEvent;
import com.gridnode.gtas.events.alert.GetMessageTemplateEvent;
import com.gridnode.gtas.events.alert.GetMessageTemplateListEvent;
import com.gridnode.gtas.events.alert.GetSubstitutionListEvent;
import com.gridnode.gtas.events.alert.UpdateMessageTemplateEvent;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.AssertUtil;

/**
 * SC (11 Jan 06):
 * Message template has a field called MessageProperty. It is embeded entity. 
 * In (DefaultAbstractManager.java:1060), embedded entity container is cast to Vector. 
 * The intial design is to have MessageProperty objects inside ArrayList.
 * Since DefaultAbstractManager.java is a parent class and many classes extend from this, 
 * I was unwilling to change implementation in it from Vector to ArrayList.
 * So for embeded enity, the container must be Vector.
 */
class DefaultMessageTemplateManager extends DefaultAbstractManager
  implements IGTMessageTemplateManager
{
	private Long[] _excludeUids = {-16L, -17L, -18L, -19L, -20L, -21L};
	
  DefaultMessageTemplateManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_MESSAGE_TEMPLATE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTMessageTemplateEntity messageTemplate = (IGTMessageTemplateEntity)entity;

      IEvent event = null; //20030514AH
  
      String messageType = messageTemplate.getFieldString(IGTMessageTemplateEntity.MESSAGE_TYPE);
      if (IGTMessageTemplateEntity.MSG_TYPE_LOG.equals(messageType))
      { //20030514AH
      	Long uid = (Long)messageTemplate.getFieldValue(IGTMessageTemplateEntity.UID);
      	String location = messageTemplate.getFieldString(IGTMessageTemplateEntity.LOCATION); //20030514AH
      	Boolean isAppend = (Boolean)messageTemplate.getFieldValue(IGTMessageTemplateEntity.APPEND); //20030414AH
      	String message = messageTemplate.getFieldString(IGTMessageTemplateEntity.MESSAGE);
        event = new UpdateMessageTemplateEvent(uid, messageType, location, isAppend, message);
      }
      else if (IGTMessageTemplateEntity.MSG_TYPE_EMAIL.equals(messageType))
      {
      	Long uid = (Long)messageTemplate.getFieldValue(IGTMessageTemplateEntity.UID);
        String name = messageTemplate.getFieldString(IGTMessageTemplateEntity.NAME);
        String contentType = messageTemplate.getFieldString(IGTMessageTemplateEntity.CONTENT_TYPE);
        String fromAddr = messageTemplate.getFieldString(IGTMessageTemplateEntity.FROM_ADDR);
        String toAddr = messageTemplate.getFieldString(IGTMessageTemplateEntity.TO_ADDR);
        String ccAddr = messageTemplate.getFieldString(IGTMessageTemplateEntity.CC_ADDR);
        String subject = messageTemplate.getFieldString(IGTMessageTemplateEntity.SUBJECT);
        String message = messageTemplate.getFieldString(IGTMessageTemplateEntity.MESSAGE);
        event = new UpdateMessageTemplateEvent(uid,
                                              name,
                                              contentType,
                                              messageType,
                                              fromAddr,
                                              toAddr,
                                              ccAddr,
                                              subject,
                                              message);
      }
      else if (IGTMessageTemplateEntity.MSG_TYPE_JMS.equals(messageType))
      {
      	Long uid = (Long)messageTemplate.getFieldValue(IGTMessageTemplateEntity.UID);
        String name = messageTemplate.getFieldString(IGTMessageTemplateEntity.NAME);
        Long jmsDestinationUid = (Long) messageTemplate.getFieldValue(IGTMessageTemplateEntity.JMS_DESTINATION);
        String message = messageTemplate.getFieldString(IGTMessageTemplateEntity.MESSAGE);
        
        /* MessageProperty */
      	Collection c = (Collection) messageTemplate.getFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES);
      	Collection mapC = convertMessagePropertyEntitiesToMaps(c);
      	
      	event = new UpdateMessageTemplateEvent(uid, name, messageType,
																		jmsDestinationUid,
																		mapC,  message);
      }
      else
      {
      	AssertUtil.fail("cannot reach here");
      }
      

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTMessageTemplateEntity messageTemplate = (IGTMessageTemplateEntity)entity;
      String messageType = messageTemplate.getFieldString(IGTMessageTemplateEntity.MESSAGE_TYPE);

      
 
      IEvent event = null; //20030514AH
  
      if(IGTMessageTemplateEntity.MSG_TYPE_LOG.equals(messageType))
      { //20030514AH
      	String name = messageTemplate.getFieldString(IGTMessageTemplateEntity.NAME);
      	String location = messageTemplate.getFieldString(IGTMessageTemplateEntity.LOCATION);
      	Boolean isAppend = (Boolean)messageTemplate.getFieldValue(IGTMessageTemplateEntity.APPEND);
      	String message = messageTemplate.getFieldString(IGTMessageTemplateEntity.MESSAGE);
        event = new CreateMessageTemplateEvent(name, messageType, location, isAppend, message);
      }
      else if (IGTMessageTemplateEntity.MSG_TYPE_EMAIL.equals(messageType))
      {
      	String name = messageTemplate.getFieldString(IGTMessageTemplateEntity.NAME);
        String contentType = messageTemplate.getFieldString(IGTMessageTemplateEntity.CONTENT_TYPE);
        String fromAddr = messageTemplate.getFieldString(IGTMessageTemplateEntity.FROM_ADDR);
        String toAddr = messageTemplate.getFieldString(IGTMessageTemplateEntity.TO_ADDR);
        String ccAddr = messageTemplate.getFieldString(IGTMessageTemplateEntity.CC_ADDR);
        String subject = messageTemplate.getFieldString(IGTMessageTemplateEntity.SUBJECT);
        String message = messageTemplate.getFieldString(IGTMessageTemplateEntity.MESSAGE);
        event = new CreateMessageTemplateEvent( name,
                                                contentType,
                                                messageType,
                                                fromAddr,
                                                toAddr,
                                                ccAddr,
                                                subject,
                                                message);
      }
      else if (IGTMessageTemplateEntity.MSG_TYPE_JMS.equals(messageType))
      {
      	String name = messageTemplate.getFieldString(IGTMessageTemplateEntity.NAME);
      	Long jmdDestinationUid = (Long) messageTemplate.getFieldValue(IGTMessageTemplateEntity.JMS_DESTINATION);
      	String message = messageTemplate.getFieldString(IGTMessageTemplateEntity.MESSAGE);
      	
      	/* MessageProperty */
      	Collection c = (Collection) messageTemplate.getFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES);
      	Collection mapC = convertMessagePropertyEntitiesToMaps(c);
      	
      	event = new CreateMessageTemplateEvent(name, messageType,
																		jmdDestinationUid,
																		mapC, message);
      }

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_MESSAGE_TEMPLATE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_MESSAGE_TEMPLATE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetMessageTemplateEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
  	if (_session.isNoP2P())
  	{
  		//NSL20060508 hide p2p message templates
  		FilterConnector conn = null;
  		if (filter == null)
  		{
  			filter = new DataFilterImpl();
  		}
  		else
  		{
  			conn = filter.getAndConnector();
  		}
  		filter.addDomainFilter(conn, IGTMessageTemplateEntity.UID, Arrays.asList(_excludeUids), true);
  	}
    return new GetMessageTemplateListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteMessageTemplateEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_MESSAGE_TEMPLATE.equals(entityType))
    {
      return new DefaultMessageTemplateEntity();
    }
    else if(IGTEntity.ENTITY_MESSAGE_PROPERTY.equals(entityType))
    {
    	return new DefaultMessagePropertyEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  public Map getSubstitutionList()
   throws GTClientException
  {
    try
    {
      GetSubstitutionListEvent event = new GetSubstitutionListEvent();
      Map substitutionList = (Map)handleEvent(event);
      return substitutionList;
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to get substitution list", e);
    }
  }
  
  
  protected void setDefaultFieldValues(AbstractGTEntity entity)
    throws GTClientException
  { //20030514AH
    String type = entity.getType();
    if( IGTEntity.ENTITY_MESSAGE_TEMPLATE.equals(type))
    {
      entity.setNewFieldValue(IGTMessageTemplateEntity.APPEND, Boolean.TRUE);
    }
  }
  
  public IGTMessagePropertyEntity newMessageProperty() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_MESSAGE_PROPERTY);
    entity.setNewEntity(true);
    return (IGTMessagePropertyEntity) entity;
  }

  private Collection convertMessagePropertyEntitiesToMaps(Collection entityCollection) throws GTClientException
  {
  	int size = entityCollection.size();
  	Collection ret = new Vector(size);
  	Iterator it = entityCollection.iterator();
  	while (it.hasNext())
  	{
  		IGTMessagePropertyEntity entity = (IGTMessagePropertyEntity) it.next();
  		Map map = convertMessagePropertyEntitiesToMapsHelper(entity);
  		ret.add(map);
  	}
  	return ret;
  }
  
  private HashMap convertMessagePropertyEntitiesToMapsHelper(IGTMessagePropertyEntity entity) throws GTClientException
  {
  	HashMap map = new HashMap();
  	Number[] fieldIds = {IGTMessagePropertyEntity.KEY, IGTMessagePropertyEntity.TYPE, IGTMessagePropertyEntity.VALUE};
  	for (int i = 0; i < fieldIds.length; i++)
  	{
  		Object obj = entity.getFieldValue(fieldIds[i]);
  		map.put(fieldIds[i], obj);
  	}
  	return map;
  }
  
  private void log(String message)
  {
  	Logger.log("[DefaultMessageTemplateManager] " + message);
  }
}