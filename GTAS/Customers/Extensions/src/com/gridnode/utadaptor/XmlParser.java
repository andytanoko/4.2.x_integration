/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XmlParser.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27 2003    Neo Sok Lay         Created
 */
package com.gridnode.utadaptor;

import java.io.Reader;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 * Parser for XML documents. Provide simple utilities to get node values base on
 * Xpaths.
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class XmlParser
{
  private Reader _inputSource;
  private Document _document;

  /**
   * Constructs a XmlParser
   *
   * @param inputSource A Reader that provides the Input source.
   * @throws Exception Error when parsing the source to construct an Xml document.
   */
  public XmlParser(Reader inputSource) throws Exception
  {
    _inputSource = inputSource;
    parse();
  }

  private void parse() throws Exception
  {
    SAXBuilder builder = new SAXBuilder();
    _document = builder.build(new InputSource(_inputSource));
  }

  /**
   * Get the Node specified by the xpath.
   * @param xpath The Xpath.
   * @return The Element object for the Node.
   * @throws Exception Error performing Xpath queries.
   */
  public Element getNode(String xpath) throws Exception
  {
    JDOMXPath xpathObj = new JDOMXPath(xpath);
    return (Element) xpathObj.selectSingleNode(_document);
  }

  /**
   * Get the Value indicated by the specified xpath.
   *
   * @param xpath The Xpath.
   * @return The value of the node indicated by the xpath, or <b>null</b> if
   * no such Xpath exists.
   * @throws Exception Error extracting value from the specified xpath.
   */
  public String getValueAtXPath(String xpath) throws Exception
  {
    Element element = getNode(xpath);
    if (element != null)
    {
      return element.getTextTrim();
    }
    return null;
  }

}