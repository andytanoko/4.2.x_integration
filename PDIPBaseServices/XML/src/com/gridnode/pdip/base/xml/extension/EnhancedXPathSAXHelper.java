/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnhancedXPathSAXHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 24 2003    Koh Han Sing        Created
 * Dec 09 2003    Koh Han Sing        Remove debugging statements so as to
 *                                    increase performance.
 */
package com.gridnode.pdip.base.xml.extension;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gridnode.xml.helpers.XPathSAXHelperHandler;

/**
 * This class contains extends from the XPathSAXHelper and added some extra
 * functionalities.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class EnhancedXPathSAXHelper
{

  public EnhancedXPathSAXHelper()
  {
    super();
  }

  public static List getValuesAtXPathUnique(String filename, String xpath)
    throws Exception
  {
    ArrayList uniqueList = new ArrayList();
    List values = getValuesAtXPath(filename, xpath);
    for (Iterator i = values.iterator(); i.hasNext(); )
    {
      String value = i.next().toString();
      if (uniqueList.indexOf(value) == -1)
      {
        uniqueList.add(value);
      }
    }
    return uniqueList;
  }

  public static String getRootName(String filename)
  throws IOException, SAXException, ParserConfigurationException
  {
    SAXParser parser = new SAXParser();
    //QuickGNEntityResolver resolver = new QuickGNEntityResolver();
    HashSet hashSet = new HashSet();
    XPathSAXHelperHandler xpathHandler = new XPathSAXHelperHandler(hashSet);
    parser.setContentHandler(xpathHandler);
    parser.parse(new InputSource(new FileInputStream(filename)));
    return xpathHandler.getRootName();
  }

  public static String getValueAtXPath(String filename, String xPath)
  throws IOException, SAXException, ParserConfigurationException
  {
    List values = getValuesAtXPath(filename, xPath);
    return (String)values.get(0);
  }

  public static List getValuesAtXPath(String filename, String xPath)
  throws IOException, SAXException, ParserConfigurationException
  {
    SAXParser parser = new SAXParser();
    QuickGNEntityResolver resolver = new QuickGNEntityResolver();
    parser.setEntityResolver(resolver);
    String xPathIndexed = getIndexedXPath(xPath);
    HashSet hashSet = new HashSet();
    hashSet.add(xPathIndexed);
    XPathSAXHelperHandler xpathHandler = new XPathSAXHelperHandler(hashSet);
    parser.setContentHandler(xpathHandler);
    parser.parse(new InputSource(new FileInputStream(filename)));
    return xpathHandler.getValues(xPathIndexed);
  }

  private static String getIndexedXPath(String xPath)
  {
    StringTokenizer st = new StringTokenizer(xPath, "/");
    StringBuffer sb = new StringBuffer();
    int size = st.countTokens() - 1;
    for(int i = 0; i < size; i++)
    {
      String temp = st.nextToken();
      sb.append(temp);
      if(temp.indexOf("[") == -1)
      {
        sb.append("[1]");
      }
      sb.append("/");
    }
    sb.append(st.nextToken());
    return sb.toString();
  }
}