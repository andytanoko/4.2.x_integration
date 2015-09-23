/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataObjectList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 30, 2005    Tam Wei Xiang       Created
 * Mar 03 2006    Neo Sok Lay         Use generics
 *                                    Extend from ArrayList instead of Vector
 */
package com.gridnode.pdip.framework.db;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * If an entity(MessageTemplate) is required to hold a collection of objs (eg MessageProperty), 
 * this class can be served as a container for those objs.
 * 
 * It support the serialize/deserialize the entire container(itself) into xml format provided that
 * the mapping file for the obj has been set in object-xml.map.
 * 
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class DataObjectList<E>
	extends ArrayList<E>
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -7771710914668751895L;
	protected transient ObjectXmlSerializer _xmlSerializer;
  protected transient XmlObjectDeserializer _xmlDeserializer;
	
	public DataObjectList()
	{
		_xmlSerializer = new ObjectXmlSerializer();
		_xmlDeserializer = new XmlObjectDeserializer();
	}
	
	public DataObjectList(Collection<? extends E> dataObjects)
	{
		clear();
		addAll(dataObjects);
	}
	
	public void addDataObjects(Collection<? extends E> dataObjects)
	{
		clear();
		addAll(dataObjects);
	}
	
	public Collection<E> getDataObjectList()
	{
		return this;
	}
	
	public void serialize(StringWriter writer)
		throws Exception
	{
		if(_xmlSerializer ==null)
		{
			_xmlSerializer = new ObjectXmlSerializer();
		}
		_xmlSerializer.serialize(this, writer);
	}
	
	public DataObjectList deserialize(StringReader reader)	
		throws Exception
	{
		if(_xmlDeserializer == null)
		{
			_xmlDeserializer = new XmlObjectDeserializer();
		}
		
		return (DataObjectList)_xmlDeserializer.deserialize(this.getClass(), reader);
	}
	
}
