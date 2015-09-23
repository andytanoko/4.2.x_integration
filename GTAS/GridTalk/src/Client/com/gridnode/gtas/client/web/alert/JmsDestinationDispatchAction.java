/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 28 Dec 2005		SC									Created
 * 16 Jan 2006		SC									Support viewing of jms destination
 */
package com.gridnode.gtas.client.web.alert;

import java.util.*;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.FieldValidator;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.pdip.framework.util.AssertUtil;
import java.util.regex.*;

/**
 * SC: 6 Jan 06:
 * For lookup properties field, the database has fieldmetainfo as type = other. The server layer returns this as Properties object.  
 * I asked for this field to be change to an entity instead because the implementation will be consistent. That was not granted. 
 * So to deal with this I invent this special algo to handle this field:
 * 
 * 1. When user enters this page (JmsDestinationDispatchAction), retrieve lookup properties (Properties object) from server layer. 
 *    Convert Properties to entities and AForm objects.
 * 2. As user add or delete lookup properties in this page, synchornize AForm objects and entities.
 * 3. When user exits this page, convert entities to Propeties. Save Properties in server layer.  
 *
 * I created the entities so that I can use BindingFieldPropertyRenderer to render lookup properties on this page.
 */
public class JmsDestinationDispatchAction extends EntityDispatchAction2
{
//  protected static final String ADD_CONDITION     = "addJmsDestination";
//  protected static final String REMOVE_CONDITION  = "removeJmsDestination";

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_JMS_DESTINATION;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new JmsDestinationRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext) throws GTClientException
	{
		return edit ? IDocumentKeys.JMS_DESTINATION_UPDATE : IDocumentKeys.JMS_DESTINATION_VIEW;
	}

	protected ActionForm createActionForm(ActionContext actionContext) throws GTClientException
	{
		return new JmsDestinationAForm();
	}

	protected int getIGTManagerType(ActionContext actionContext) throws GTClientException
	{
		return IGTManager.MANAGER_JMS_DESTINATION;
	}

	protected void validateActionForm(ActionContext actionContext,
																		IGTEntity entity,
																		ActionForm form,
																		ActionErrors actionErrors) throws GTClientException
	{
		IGTJmsDestinationEntity jd = (IGTJmsDestinationEntity) entity;
		JmsDestinationValidator validator = new JmsDestinationValidator(
																																		actionContext,
																																		jd, form,
																																		actionErrors);
		validator.validate();
	}

	/* entity processing methods */
	
  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
	  throws GTClientException
	{
		IGTJmsDestinationEntity jd = (IGTJmsDestinationEntity) entity;
		jd.setFieldValue(IGTJmsDestinationEntity.TYPE, IGTJmsDestinationEntity.QUEUE);
		jd.setFieldValue(IGTJmsDestinationEntity.RETRY_INTERVAL, new Integer(5));
		jd.setFieldValue(IGTJmsDestinationEntity.MAXIMUM_RETRIES, new Integer(20));
		jd.setFieldValue(IGTJmsDestinationEntity.LOOKUP_PROPERTIES, new Properties());
		jd.setFieldValue(IGTJmsDestinationEntity.DELIVERY_MODE, IGTJmsDestinationEntity.DELIVERY_MODE_DEFAULT);
		jd.setFieldValue(IGTJmsDestinationEntity.PRIORITY, IGTJmsDestinationEntity.PRIORITY_DEFAULT);
	}
  
  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    /* set jms destination fields */
  	JmsDestinationAForm form = (JmsDestinationAForm) actionContext.getActionForm();
  	IGTJmsDestinationEntity jd = (IGTJmsDestinationEntity) entity;
  	
  	/* entity already exists in db and there is no lookup properties, lookup properties field will be null */
  	Properties p = (Properties) jd.getFieldValue(IGTJmsDestinationEntity.LOOKUP_PROPERTIES);
  	if (p == null)
  	{
  		jd.setFieldValue(IGTJmsDestinationEntity.LOOKUP_PROPERTIES, new Properties());
  	}
  	
  	form.setNewEntity(jd.isNewEntity());
  	form.setName(jd.getFieldString(IGTJmsDestinationEntity.NAME));
  	form.setType(jd.getFieldString(IGTJmsDestinationEntity.TYPE));
  	form.setJndiName(jd.getFieldString(IGTJmsDestinationEntity.JNDI_NAME));
  	form.setDeliveryMode(jd.getFieldString(IGTJmsDestinationEntity.DELIVERY_MODE));
  	form.setPriority(jd.getFieldString(IGTJmsDestinationEntity.PRIORITY));
  	form.setConnectionFactoryJndi(jd.getFieldString(IGTJmsDestinationEntity.CONNECTION_FACTORY_JNDI));
  	form.setConnectionUser(jd.getFieldString(IGTJmsDestinationEntity.CONNECTION_USER));
  	form.setConnectionPassword(jd.getFieldString(IGTJmsDestinationEntity.CONNECTION_PASSWORD));
  	form.setRetryInterval(jd.getFieldString(IGTJmsDestinationEntity.RETRY_INTERVAL));
  	form.setMaximumRetries(jd.getFieldString(IGTJmsDestinationEntity.MAXIMUM_RETRIES));

    /* set lookupProperties */
    if(entity.isNewEntity() == false)
    {
      int size = LookupPropertiesUtil.getPropertiesLength(jd);
      for(int i = 0; i < size; i++)
      {
        form.addNewLookupPropertiesAForm();
      }      
    }
    
    ArrayList ar = LookupPropertiesUtil.convertPropertiesToEntityArray(jd);
    jd.setFieldValue(IGTJmsDestinationEntity.VIRTUAL_LOOKUP_PROPERTIES, ar);
    LookupPropertiesAForm[] lookupPropertiesForms = form.getLookupProperties();
    for(int i = 0; i < ar.size(); i++)
    {
    	IGTLookupPropertiesEntity lpEntity = (IGTLookupPropertiesEntity) ar.get(i);
    	lookupPropertiesForms[i].setLp_name(lpEntity.getFieldString(IGTLookupPropertiesEntity.NAME));
    	lookupPropertiesForms[i].setLp_value(lpEntity.getFieldString(IGTLookupPropertiesEntity.VALUE));
    }
  }
  
  protected void performUpdateProcessing(ActionContext actionContext)
	  throws GTClientException
	{
		IGTJmsDestinationEntity jd = (IGTJmsDestinationEntity) getEntity(actionContext);
	  JmsDestinationAForm form = (JmsDestinationAForm) actionContext.getActionForm();
	  if (form == null) 
	  {
	  	return;
	  }
	  
	  String updateAction = form.getUpdateAction();
	  if(IGlobals.ADD_UPDATE_ACTION.equals(updateAction))
	  {
	    LookupPropertiesUtil.addNewLookupProperties(jd, form);
	  }
	  else if(IGlobals.REMOVE_UPDATE_ACTION.equals(updateAction))
	  {
	  	LookupPropertiesUtil.removeSelectedLookupProperties(jd, form);
	  }
	  
	  form.setUpdateAction(null);
	}
  
  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    JmsDestinationAForm form = (JmsDestinationAForm) actionContext.getActionForm();
    IGTJmsDestinationEntity jd = (IGTJmsDestinationEntity) entity;
    
    /* save all fields expect lookup properties */
    jd.setFieldValue(IGTJmsDestinationEntity.NAME, form.getName());
    jd.setFieldValue(IGTJmsDestinationEntity.TYPE, new Integer(form.getType()));
    jd.setFieldValue(IGTJmsDestinationEntity.JNDI_NAME, form.getJndiName());
    jd.setFieldValue(IGTJmsDestinationEntity.JNDI_NAME, form.getJndiName());
    jd.setFieldValue(IGTJmsDestinationEntity.DELIVERY_MODE, new Integer(form.getDeliveryMode()));
    jd.setFieldValue(IGTJmsDestinationEntity.PRIORITY, new Integer(form.getPriority()));
    jd.setFieldValue(IGTJmsDestinationEntity.CONNECTION_FACTORY_JNDI, form.getConnectionFactoryJndi());
    jd.setFieldValue(IGTJmsDestinationEntity.CONNECTION_USER, form.getConnectionUser());
    jd.setFieldValue(IGTJmsDestinationEntity.CONNECTION_PASSWORD, form.getConnectionPassword());
    jd.setFieldValue(IGTJmsDestinationEntity.RETRY_INTERVAL, new Integer(form.getRetryInterval()));
    jd.setFieldValue(IGTJmsDestinationEntity.MAXIMUM_RETRIES, new Integer(form.getMaximumRetries()));
    
    /* save lookup properties */
    LookupPropertiesUtil.saveAFormAsProperties(jd, form);
  }
}