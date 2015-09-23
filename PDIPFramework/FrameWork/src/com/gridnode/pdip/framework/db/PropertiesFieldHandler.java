/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PropertiesFieldHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 16, 2006    Neo Sok Lay       Created
 */
package com.gridnode.pdip.framework.db;

import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import org.exolab.castor.mapping.GeneralizedFieldHandler;



/**
 * TWX: It is a GeneralizedFieldHandler which contain the conversion method for
 *      handling the field Properties in a certain entity. 
 *      Since castor xml doesn't support the serialize/deserialize java.util.Properties.
 *      We need such a handler to help us to do conversion so that castor xml know
 *      how to handle the properties.
 *
 * @author Neo Sok Lay
 * @since GT 4.0
 */
public class PropertiesFieldHandler extends GeneralizedFieldHandler
{

	public PropertiesFieldHandler()
	{
		super();
		setCollectionIteration(false);
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponGet(java.lang.Object)
	 */
	@Override
	public Object convertUponGet(Object arg0)
	{
		// TODO Auto-generated method stub
		if (arg0 instanceof Properties)
		{
			return convertPropertiesToString((Properties)arg0);
		}
		return arg0;
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponSet(java.lang.Object)
	 */
	@Override
	public Object convertUponSet(Object arg0)
	{
		// TODO Auto-generated method stub
		if (arg0 instanceof String)
		{
			return convertStringToProperties((String)arg0);
		}
		return arg0;
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#getFieldType()
	 */
	@Override
	public Class getFieldType()
	{
		// TODO Auto-generated method stub
		return Properties.class;
	}

	@Override
	public Object newInstance(Object arg0) throws IllegalStateException
	{
		return new Properties();
	}
	
	/**
	 * //TWX: Output format  key=value; ... key=value; or empty string
	 * @param pro
	 * @return 
	 */
	private String convertPropertiesToString(Properties pro)
	{
		StringBuffer str = new StringBuffer();
		if(pro!=null)
		{
			Enumeration propertyNames = pro.propertyNames();
			while(propertyNames.hasMoreElements())
			{
				String key = (String)propertyNames.nextElement();
				str.append(key+"="+pro.getProperty(key)+";");
			}
		}
		return str.toString();
	}
	
	/**
	 * TWX: Accepted String format: key=value; ... key=value; or empty string
	 * @param s
	 * @return
	 */
	private Properties convertStringToProperties(String s)
	{
		
		if(s== null)
		{
			return new Properties();
		}
		StringTokenizer st = new StringTokenizer(s, ";");
		Properties pro = new Properties();
		while(st.hasMoreElements())
		{
			String property = st.nextToken();
			int equalSign = property.lastIndexOf("=");
			pro.setProperty(property.substring(0,equalSign), property.substring(equalSign+1));
		}
		return pro;
	}
	
	
}
