/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TptMappingRegistry.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Dec 25 2003    Jagadeesh              Created
 * Sep 29 2005    Neo Sok Lay             Change to use XMLServiceHandler from 
 *                                        BaseServices-XML instead of direct using
 *                                        JDOM.
 */

package com.gridnode.pdip.base.transport.helpers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.gridnode.pdip.base.xml.exceptions.XMLException;
import com.gridnode.pdip.base.xml.helpers.XMLServiceHandler;
import com.gridnode.xml.adapters.GNElement;

/**
 * TptMappingRegistry is a registry of message_type to Header-Value pair collection.
 *
 * A registry exists per protocol, a xml mapping file is read using jdom parsing.
 *
 * @author Jagadeesh
 * @since 2.3
 * @see TptMappingFactory.
 *
 */

public class TptMappingRegistry implements ITptMappingRegistry
{
	private Map _tptHeaderMapping = new HashMap();

	public TptMappingRegistry(String mappingxml) throws XMLException
	{
		parseMessageToHeaderMapping(mappingxml);
	}

	private void parseMessageToHeaderMapping(String tptProtoclMappingxml)
		throws XMLException
	{
		parseDocument(tptProtoclMappingxml);
	}

	public TptMapping getTptMapping(String messageType)
		throws IllegalArgumentException
	{
		if (_tptHeaderMapping.get(messageType) == null)
                  return null;
		return (TptMapping) _tptHeaderMapping.get(messageType);
	}

	private void parseDocument(String fileName) throws XMLException
	{
		//NSL20050929 Use XMLServiceHandler instead of JDOM
		//org.jdom.input.DOMBuilder domBuilder = new org.jdom.input.DOMBuilder();
		//Element root = domBuilder.build(new File(fileName)).getRootElement();
		XMLServiceHandler xmlService = XMLServiceHandler.getInstance();
		GNElement root = xmlService.getRoot(fileName, null);
		List packType = root.getChildren(ITptMappingRegistry.PACKAGE_TYPE);
		for (int i = 0; i < packType.size(); i++)
		{
			Element element = (Element) packType.get(i);
			if (element.getAttribute(ITptMappingRegistry.ATTRIBUTE_TYPE) != null)
			{
				String packageType = element.getAttribute(ITptMappingRegistry.ATTRIBUTE_TYPE).getValue();
				_tptHeaderMapping.put(packageType, getTptMapping(element));
			}
		}
	}

	private TptMapping getTptMapping(Element packElement)
	{
		List child = packElement.getChildren();
		TptMapping mapping = new TptMapping();
		for (int i = 0; i < child.size(); i++)
		{
			Element children = (Element) child.get(i);
			if (children.getText() != null)
			{
				mapping.setMessageTptHeader(
					children.getChildText(ITptMappingRegistry.MESSAGE_HEADER_KEY),
					children.getChildText(ITptMappingRegistry.TPT_HEADER_KEY));
			}
		}
		return mapping;
	}

}