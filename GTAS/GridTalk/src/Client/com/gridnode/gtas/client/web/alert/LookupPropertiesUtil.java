/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LookupPropertiesUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 3 Jan 05				SC									Created
 */
package com.gridnode.gtas.client.web.alert;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTJmsDestinationEntity;
import com.gridnode.gtas.client.ctrl.IGTLookupPropertiesEntity;
import com.gridnode.gtas.client.ctrl.IGTLookupPropertiesManager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.pdip.framework.util.AssertUtil;
import com.gridnode.pdip.framework.util.StringUtil;
import com.gridnode.pdip.framework.util.UtilString;

public class LookupPropertiesUtil
{
	public static ArrayList convertPropertiesToEntityArray(IGTJmsDestinationEntity entity) throws GTClientException
  {
  	Properties p = (Properties) entity.getFieldValue(IGTJmsDestinationEntity.LOOKUP_PROPERTIES);
  	AssertUtil.assertTrue(p != null);
  	IGTLookupPropertiesManager manager = (IGTLookupPropertiesManager) entity.getSession().getManager(IGTManager.MANAGER_VIRTUAL_LOOKUP_PROPERTIES);
  	int size = p.size();
  	ArrayList ret = new ArrayList(size);
  	Enumeration enumeration = p.propertyNames();
  	while (enumeration.hasMoreElements())
  	{
  		String name = (String) enumeration.nextElement();
  		String value = p.getProperty(name);
  		IGTLookupPropertiesEntity lookupProperties = manager.newLookupProperties();
  		lookupProperties.setFieldValue(IGTLookupPropertiesEntity.NAME, name);
  		lookupProperties.setFieldValue(IGTLookupPropertiesEntity.VALUE, value);
  		ret.add(lookupProperties);
  	}
  	return ret;
  }
	
	public static void saveAFormAsProperties(IGTJmsDestinationEntity entity, JmsDestinationAForm jdForm) throws GTClientException
	{
		LookupPropertiesAForm[] lpForms = jdForm.getLookupProperties();
		/* set lpForms into jms destination entity as properties field */
		Properties p = new Properties();
		for (int i = 0; i < lpForms.length; i++)
		{
			LookupPropertiesAForm lookupProperties = lpForms[i];
			String name = lookupProperties.getLp_name();
			String value = lookupProperties.getLp_value();
			if (StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(value))
			{
				p.setProperty(name, value);
			}
		}
		entity.setFieldValue(IGTJmsDestinationEntity.LOOKUP_PROPERTIES, p);
	}
	
	public static int getPropertiesLength(IGTJmsDestinationEntity entity) throws GTClientException
	{
		Properties p = (Properties) entity.getFieldValue(IGTJmsDestinationEntity.LOOKUP_PROPERTIES);
		return p.size();
	}
	
	public static void addNewLookupProperties(IGTJmsDestinationEntity entity) throws GTClientException
	{
		Properties p = (Properties) entity.getFieldValue(IGTJmsDestinationEntity.LOOKUP_PROPERTIES);
		p.setProperty("", "");
	}
	
	public static void addNewLookupProperties(IGTJmsDestinationEntity entity, JmsDestinationAForm form) throws GTClientException
  {
    // Add a new lookup property entity to jms destination entity
    IGTLookupPropertiesManager manager = (IGTLookupPropertiesManager) entity.getSession().getManager(IGTManager.MANAGER_VIRTUAL_LOOKUP_PROPERTIES);
    ArrayList lookupPropertiesList = (ArrayList) entity.getFieldValue(IGTJmsDestinationEntity.VIRTUAL_LOOKUP_PROPERTIES);
    lookupPropertiesList.add(manager.newLookupProperties()); 
    entity.setFieldValue(IGTJmsDestinationEntity.VIRTUAL_LOOKUP_PROPERTIES, lookupPropertiesList);

    // Add a new domainIdentifier to the action form
    form.addNewLookupPropertiesAForm();
  }
	

	public static void removeSelectedLookupProperties(IGTJmsDestinationEntity entity, JmsDestinationAForm form) throws GTClientException
	{
		// Remove selected domainIdentifiers to the entity
		ArrayList lookupPropertiesList = (ArrayList) entity.getFieldValue(IGTJmsDestinationEntity.VIRTUAL_LOOKUP_PROPERTIES);
		LookupPropertiesAForm[] lpForms = form.getLookupProperties();

		if (lpForms == null) 
		{
			return;
		}

		Iterator it = lookupPropertiesList.iterator();
		for (int i = 0; i < lpForms.length; i++)
		{
			it.next();
			if (lpForms[i].isSelected())
			{
				it.remove();
			}
		}
		entity.setFieldValue(IGTJmsDestinationEntity.VIRTUAL_LOOKUP_PROPERTIES, lookupPropertiesList);

		// Remove selected domainIdentifiers from the action form
		form.removeSelectedLookupProperties();
	}
}
