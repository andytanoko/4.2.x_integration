/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultEmailConfigManager.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.alert.GetEmailConfigEvent;
import com.gridnode.gtas.events.alert.SaveEmailConfigEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class DefaultEmailConfigManager extends DefaultAbstractManager
implements IGTEmailConfigManager
{
  DefaultEmailConfigManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_EMAIL_CONFIG, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new UnsupportedOperationException("Update EmailConfig not supported");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new UnsupportedOperationException("Create EmailConfig not supported");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_EMAIL_CONFIG;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_EMAIL_CONFIG;
  }

  protected IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new UnsupportedOperationException("No get event for this type");
  }

  protected IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new UnsupportedOperationException("No get list event for this type");
  }

  protected IEvent getDeleteEvent(Collection uids)
    throws EventException
  { 
    throw new UnsupportedOperationException("No delete event for this type");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_EMAIL_CONFIG.equals(entityType))
    {
      return new DefaultEmailConfigEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  public Collection getAll() throws GTClientException
  {
    throw new UnsupportedOperationException("Manager does not support multiple entities");
  }
  
  public IGTEntity newEntity() throws GTClientException
  {
    return getEmailConfig();
  }
  
  public IGTEmailConfigEntity getEmailConfig() throws GTClientException
  {
    try
    {
      GetEmailConfigEvent event = new GetEmailConfigEvent();
      
      Map results = (Map)((DefaultGTSession)_session).fireEvent(event);     
      
      if(results == null)
      { // Internal sanity check
        throw new NullPointerException("GetEmailConfigEvent event returned null result map");
      }

      IGTEmailConfigEntity emailConfig = (IGTEmailConfigEntity)initEntityObjects(IGTEntity.ENTITY_EMAIL_CONFIG);
      emailConfig.setFieldValue(IGTEmailConfigEntity.SMTP_SERVER_HOST,   results.get(IGTEmailConfigEntity.SMTP_SERVER_HOST));
      emailConfig.setFieldValue(IGTEmailConfigEntity.SMTP_SERVER_PORT,   results.get(IGTEmailConfigEntity.SMTP_SERVER_PORT));
      emailConfig.setFieldValue(IGTEmailConfigEntity.AUTH_USER,          results.get(IGTEmailConfigEntity.AUTH_USER));
      emailConfig.setFieldValue(IGTEmailConfigEntity.AUTH_PASSWORD,      results.get(IGTEmailConfigEntity.AUTH_PASSWORD));
      emailConfig.setFieldValue(IGTEmailConfigEntity.RETRY_INTERVAL,     results.get(IGTEmailConfigEntity.RETRY_INTERVAL));
      emailConfig.setFieldValue(IGTEmailConfigEntity.MAX_RETRIES,        results.get(IGTEmailConfigEntity.MAX_RETRIES));
      emailConfig.setFieldValue(IGTEmailConfigEntity.SAVE_FAILED_EMAILS, results.get(IGTEmailConfigEntity.SAVE_FAILED_EMAILS));
      
      return emailConfig;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting EmailConfig entity", t);
    }    
  }
  public void saveEmailConfig(IGTEmailConfigEntity emailConfig) throws GTClientException
  { 
    try
    {
      Map emailConfigMap = new HashMap();
      emailConfigMap.put(IGTEmailConfigEntity.SMTP_SERVER_HOST,     emailConfig.getFieldValue(IGTEmailConfigEntity.SMTP_SERVER_HOST));
      emailConfigMap.put(IGTEmailConfigEntity.SMTP_SERVER_PORT,     emailConfig.getFieldValue(IGTEmailConfigEntity.SMTP_SERVER_PORT));
      emailConfigMap.put(IGTEmailConfigEntity.AUTH_USER,            emailConfig.getFieldValue(IGTEmailConfigEntity.AUTH_USER));
      emailConfigMap.put(IGTEmailConfigEntity.AUTH_PASSWORD,        emailConfig.getFieldValue(IGTEmailConfigEntity.AUTH_PASSWORD));
      emailConfigMap.put(IGTEmailConfigEntity.RETRY_INTERVAL,       emailConfig.getFieldValue(IGTEmailConfigEntity.RETRY_INTERVAL));
      emailConfigMap.put(IGTEmailConfigEntity.MAX_RETRIES,          emailConfig.getFieldValue(IGTEmailConfigEntity.MAX_RETRIES));
      emailConfigMap.put(IGTEmailConfigEntity.SAVE_FAILED_EMAILS,   emailConfig.getFieldValue(IGTEmailConfigEntity.SAVE_FAILED_EMAILS));
            
      SaveEmailConfigEvent event = new SaveEmailConfigEvent(emailConfigMap);
      
      //Map results = (Map)((DefaultGTSession)_session).fireEvent(event);     
      ((DefaultGTSession)_session).fireEvent(event);     
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving EmailConfig entity", t);
    }
  }

}
