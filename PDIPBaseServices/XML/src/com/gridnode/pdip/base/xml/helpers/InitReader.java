/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InitReader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 15, 2005    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.xml.helpers;

import com.gridnode.pdip.base.xml.exceptions.XMLException;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.io.File;

/**
 * This class will load all the child element of a xml file into memory.
 * 
 * The format of the xml file exptect by this class is it contain a root element,
 * then an arbitary number of child elements. For one child element , it contains one 
 * key attribute and the value correspond to that child element.
 * 
 * With reference to com.gridnode.gtas.backend.util.InitManager   .
 * 
 * @author Tam Wei Xiang
 */
public class InitReader
{
	private Hashtable<String,String> _hTable;
	private final String _PARSER = "org.apache.xerces.parsers.SAXParser";
	
	public InitReader()
	{
		_hTable = new Hashtable<String,String>();
	}
	
	/**
	 * Read an xml file into memory. The format expected pls see above for detail.
	 * @param xml
	 * @throws Exception
	 */
	public void loadProperties(File xml)
		throws Exception
	{
		try
		{
			readFile(xml);
		}
		catch(Exception ex)
		{
			Logger.warn("[InitReader.loadProperties(File)]", ex);
			throw new XMLException("[InitReader.loadProperties(File)] Error occured while reading xml file into memory.", ex);
		}
	}
	
/**
	 * Read an xml file into memory. The format expected pls see above for detail.
	 * @param absoluteFilename
	 * @throws Exception
	 */
	public void loadProperties(String absoluteFilename)
		throws Exception
	{
		try
		{
			File f = new File(absoluteFilename);
			readFile(f);
		}
		catch(Exception ex)
		{
			Logger.warn("[InitReader.loadProperties(String)]", ex);
			throw new XMLException("[InitReader.loadProperties(String)] Error occured while reading xml file into memory.", ex);
		}
	}
	
	private void readFile(File xml)
		throws Exception
	{
		SAXBuilder builder = new SAXBuilder(_PARSER,false);
		Document doc = builder.build(xml);
		Element rootEle = doc.getRootElement();
		readContent(rootEle);
	}
	
	private void readContent(Element ele)
	{
		String key = "";
		String value = "";
		List childElementList = ele.getChildren();
		Iterator i = childElementList.iterator();
		while(i.hasNext())
		{
			Element childEle = (Element)i.next();
			key = childEle.getAttributeValue(((Attribute)childEle.getAttributes().get(0)).getName());
			value = childEle.getText();
			Logger.log("[InitReader.readContent] xpath key is "+key+" xpath value is "+value,null);
			_hTable.put(key,value);
		}
	}
	
	/**
	 * Get an enumeration of the elements
	 * @return Enumeration of Key as String
	 */
	public Enumeration getKeys()
	{
		return _hTable.keys();
	}
	
	/**
	 * Get the content of the element which value match wih the given key
	 * @param key 
	 * @return Get the value for the specified key
	 */
	public String getProperty(String key)
	{
		if(_hTable.containsKey(key))
		{
			return _hTable.get(key).toString();
		}
		return null;
	}
}
