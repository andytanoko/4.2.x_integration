/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAlertTriggerManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-06     Andrew Hill         Created
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 * 2006-05-08     Neo Sok Lay         Hide P2P alert triggers
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Arrays;
import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.alert.CreateAlertTriggerEvent;
import com.gridnode.gtas.events.alert.DeleteAlertTriggerEvent;
import com.gridnode.gtas.events.alert.GetAlertTriggerEvent;
import com.gridnode.gtas.events.alert.GetAlertTriggerListEvent;
import com.gridnode.gtas.events.alert.UpdateAlertTriggerEvent;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultAlertTriggerManager extends DefaultAbstractManager
  implements IGTAlertTriggerManager
{
  private static final String RECIPIENT_DELIMITER = ":"; //Seperates TYPE and VALUE in a recipient String
  
  private String[] _excludeAlertTypes = 
  {
    IGTAlertTypeEntity.NAME_GRIDMASTER_CONNECTION_ACTIVITY,
  	IGTAlertTypeEntity.NAME_GRIDMASTER_CONNECTION_LOST,
  	IGTAlertTypeEntity.NAME_GRIDMASTER_RECONNECTION,
  	IGTAlertTypeEntity.NAME_PARTNER_ACTIVATION,
  	IGTAlertTypeEntity.NAME_PARTNER_UNCONTACTABLE,
  };
  
  DefaultAlertTriggerManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ALERT_TRIGGER, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTAlertTriggerEntity at = (IGTAlertTriggerEntity)entity;
      
      Long uid              = at.getUidLong();
      String docType        = at.getFieldString(IGTAlertTriggerEntity.DOC_TYPE);
      String partnerType    = at.getFieldString(IGTAlertTriggerEntity.PARTNER_TYPE);
      String partnerGroup   = at.getFieldString(IGTAlertTriggerEntity.PARTNER_GROUP);
      String partnerId      = at.getFieldString(IGTAlertTriggerEntity.PARTNER_ID);
      Long alertUid         = (Long)at.getFieldValue(IGTAlertTriggerEntity.ALERT_UID);
      Boolean isEnabled     = (Boolean)at.getFieldValue(IGTAlertTriggerEntity.IS_ENABLED);
      Boolean isAttachDoc   = (Boolean)at.getFieldValue(IGTAlertTriggerEntity.IS_ATTACH_DOC);
      Collection recipients = (Collection)at.getFieldValue(IGTAlertTriggerEntity.RECIPIENTS);
      
      IEvent event = new UpdateAlertTriggerEvent( uid,
                                                  docType,
                                                  partnerType,
                                                  partnerGroup,
                                                  partnerId,
                                                  alertUid,
                                                  isEnabled,
                                                  isAttachDoc,
                                                  recipients);

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
      IGTAlertTriggerEntity at = (IGTAlertTriggerEntity)entity;
            
      Integer level         = (Integer)at.getFieldValue(IGTAlertTriggerEntity.LEVEL);
      String alertType      = at.getFieldString(IGTAlertTriggerEntity.ALERT_TYPE);
      String docType        = at.getFieldString(IGTAlertTriggerEntity.DOC_TYPE);
      String partnerType    = at.getFieldString(IGTAlertTriggerEntity.PARTNER_TYPE);
      String partnerGroup   = at.getFieldString(IGTAlertTriggerEntity.PARTNER_GROUP);
      String partnerId      = at.getFieldString(IGTAlertTriggerEntity.PARTNER_ID);
      Long alertUid         = (Long)at.getFieldValue(IGTAlertTriggerEntity.ALERT_UID);
      Boolean isEnabled     = (Boolean)at.getFieldValue(IGTAlertTriggerEntity.IS_ENABLED);
      Boolean isAttachDoc   = (Boolean)at.getFieldValue(IGTAlertTriggerEntity.IS_ATTACH_DOC);
      Collection recipients = (Collection)at.getFieldValue(IGTAlertTriggerEntity.RECIPIENTS);
      
      IEvent event = new CreateAlertTriggerEvent( level,
                                                  alertType,
                                                  docType,
                                                  partnerType,
                                                  partnerGroup,
                                                  partnerId,
                                                  alertUid,
                                                  isEnabled,
                                                  isAttachDoc,
                                                  recipients);


      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ALERT_TRIGGER;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ALERT_TRIGGER;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetAlertTriggerEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
  	if (_session.isNoP2P())
  	{
  		//NSL20060508 hide p2p alert triggers
  		FilterConnector conn = null;
  		if (filter == null)
  		{
  			filter = new DataFilterImpl();
  		}
  		else
  		{
  			conn = filter.getAndConnector();
  		}
  		filter.addDomainFilter(conn, IGTAlertTriggerEntity.ALERT_TYPE, Arrays.asList(_excludeAlertTypes), true);
  	}
    return new GetAlertTriggerListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  {
    //Currently all the other delete events only support a single Long parameter.
    //The DeleteAlertTriggerEvent is the odd one out - supporting only a collection parameter.
    //Ideally this would be the signature for all the delete events as its more efficient than firing
    //several individual events for a multiple delete. Until then however, this I am not prepared to refactor
    //just for this one event - hence the coding attrocity below: 
    /*20030718AH - co: ArrayList uidCollection = new ArrayList(1);
    uidCollection.add(uid);*/
    //20030718AH: Finally the rest of the events have caught up and we can use the constructor as intended :-)
    return new DeleteAlertTriggerEvent(uids); //20030718AH
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ALERT_TRIGGER.equals(entityType))
    {
      return new DefaultAlertTriggerEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
  
  public String extractRecipientType(String recipient)
  {
    if(recipient == null) return null;
    int index = recipient.indexOf(RECIPIENT_DELIMITER);
    if( index < 1 )
    {
      //If the : wasnt found or is the first character then we have bad data
      throw new IllegalArgumentException("Bad recipient string:" + recipient);
    }
    //Return everything left of the delimeter
    return recipient.substring(0, index ); 
  }
  
  public String extractRecipientValue(String recipient)
  {
    if(recipient == null) return null;
    int index = recipient.indexOf(RECIPIENT_DELIMITER);
    if( (index < 0) || (index == recipient.length() -1) )
    {
      //If the : wasnt found or is the last character then we have bad data
      throw new IllegalArgumentException("Bad recipient string:" + recipient);
    }
    //Return everything right of the delimeter
    return recipient.substring( index + 1 ); 
  }
  
  public String makeRecipient(String recipientType, String recipientValue)
  {
    if (recipientType == null)
      throw new NullPointerException("recipientType is null");
    if (recipientValue == null)
      throw new NullPointerException("recipientValue is null");
    return recipientType + RECIPIENT_DELIMITER + recipientValue; 
  }
  
  protected void setDefaultFieldValues(AbstractGTEntity entity)
    throws GTClientException
  {
    String type = entity.getType();
    
    if(IGTEntity.ENTITY_ALERT_TRIGGER.equals(type) )
    {
      entity.setNewFieldValue(IGTAlertTriggerEntity.IS_ENABLED, Boolean.TRUE );
    }
    else
    {
      throw new UnsupportedOperationException("Manager "
                                              + this
                                              + " cannot set default field values for " + entity);
    }
  }
}