/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LayoutDefinitionReader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.xml;

import com.gridnode.pdip.app.gridform.model.LayoutDefinition;
import com.gridnode.pdip.app.gridform.model.SourceMap;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jaxen.jdom.JDOMXPath;
import org.saxpath.SAXPathException;
import org.saxpath.XPathSyntaxException;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

/**
 * Reader for parsing the layout definition file.
 *
 * Scope is package level. Use <code>XMLFacade</code> instead.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
class LayoutDefinitionReader
{
  private static final String PARSER      = "org.apache.xerces.parsers.SAXParser";
  private static final String XPATH_ROOT  = "layout-definition/association/root-element";
  private static final String XPATH_MAPS  = "layout-definition/source-mappings/map";
  private static final String ATTRIB_NAME = "name";
  private static final String ATTRIB_TYPE = "type";

  public LayoutDefinitionReader()
  {
  }

  public LayoutDefinition loadDefinition(File file) throws JDOMException, SAXPathException, IOException
  {
    SAXBuilder b = new SAXBuilder(PARSER);
    Document doc = b.build(file);

    LayoutDefinition definition = new LayoutDefinition();
    readAssociation(doc, definition);
    readMappings(doc, definition);

    return definition;
  }

  private void readAssociation(Document doc, LayoutDefinition definition) throws SAXPathException
  {
    definition.setRootElement(getValueAtXPath(XPATH_ROOT, doc));
  }

  private void readMappings(Document doc, LayoutDefinition definition) throws SAXPathException
  {
    ArrayList list = null;
    List nodes = getNodesAtXPath(XPATH_MAPS, doc);
    if (nodes != null)
    {
      list = new ArrayList();
      Iterator iterator = nodes.iterator();
      while (iterator.hasNext())
      {
        Object node = iterator.next();
        if (node instanceof Element)
        {
          Element element = (Element)node;
          SourceMap map = new SourceMap();
          map.setName(element.getAttributeValue(ATTRIB_NAME));
          map.setType(element.getAttributeValue(ATTRIB_TYPE));
          map.setXpath(element.getText());
          list.add(map);
        }
      }
    }
    definition.setMapping(list);
  }

  private String getValueAtXPath(String xpath, Document doc) throws XPathSyntaxException, SAXPathException
  {
    JDOMXPath xpathObj = new JDOMXPath(xpath);
    Object nodeObj = xpathObj.selectSingleNode(doc);
    if (nodeObj instanceof Element)
    {
      return ((Element)nodeObj).getText();
    }
    else if (nodeObj instanceof Attribute)
    {
      return ((Attribute)nodeObj).getValue();
    }
    else return null;
  }

  private List getNodesAtXPath(String xpath, Document doc) throws XPathSyntaxException, SAXPathException
  {
    JDOMXPath xpathObj = new JDOMXPath(xpath);
    return xpathObj.selectNodes(doc);
  }
}