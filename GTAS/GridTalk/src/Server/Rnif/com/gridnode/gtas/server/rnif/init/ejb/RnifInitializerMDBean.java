/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifInitializerMDBean.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Oct 28 2003      Guo Jianyu              Created
 * May 03 2006      Neo Sok Lay             To support entityEventTopic
 */
package com.gridnode.gtas.server.rnif.init.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.server.rnif.helpers.SchemaManager;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.entity.EntityEvent;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.FacadeLogger;

public class RnifInitializerMDBean
  implements MessageListener, MessageDrivenBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6337562101455710943L;
	private transient FacadeLogger _logger = null;
  private MessageDrivenContext _mdx = null;

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
    _logger = FacadeLogger.getLogger("RnifInitializerMDBean", "RNIF.INIT");
  }

  public void ejbRemove()
  {
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message message)
  {
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      _logger.logEntry(methodName, params);
      if (message.propertyExists("command"))
      {
      	String command = message.getStringProperty("command");
      	if ("START".equals(command))
      		startInitialisation();
      }
      else
      {
      	//NSL20060503 EntityChange
      	EntityEvent event = (EntityEvent)((ObjectMessage)message).getObject();
        IEntity entity = event.getEntity();
        int eventType = event.getEventType();
        handleEntityChange(entity, eventType);
      }
    }
    catch (Throwable ex)
    {
      _logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      _logger.logExit(methodName, params);
    }
  }

  /**
   * Handles the entity change event
   * @param entity
   * @param eventType
   */
  private void handleEntityChange(IEntity entity, int eventType)
  {
    String methodName   = "handleEntityChange";
    Object[] params     = new Object[] {entity, eventType};

    try
    {
      _logger.logEntry(methodName, params);

      //update the affected schemas
      if (ProcessDef.ENTITY_NAME.equals(entity.getEntityName()))
      {
      	ProcessDef processDef = (ProcessDef)entity;
      	if (eventType == EntityEvent.UPDATED)
      	{ //in case the Rnif version is changed
      		SchemaManager.getInstance().generateSchema();
      	}
      	else
      	{
      		SchemaManager.getInstance().generateSchema(processDef.getRNIFVersion());
      	}
      }
    }
    catch (Exception ex)
    {
      _logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      _logger.logExit(methodName, params);
    }
  }
  
  /**
   * Starts the intialisation sequence for Rnif module.
   */
  private void startInitialisation()
    throws Throwable
  {
    String methodName   = "startInitialisation";
    Object[] params     = new Object[] {};

    try
    {
      _logger.logEntry(methodName, params);

      //Regenerate the schemas
      SchemaManager.getInstance().generateSchema();
    }
    catch (Exception ex)
    {
      _logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      _logger.logExit(methodName, params);
    }
  }
}
