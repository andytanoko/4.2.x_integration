/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InitManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 * Sep 29 2005    Neo Sok Lay         Change of JDOM XMLOutputter syntax.
 */
package com.gridnode.gtas.backend.util;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 * This class is used to extract and modifiy data in the GridTalk ini file
 *
 * @since Pilot dated 15/11/2000
 * @author WKH, KHS
 *
 */

public class InitManager
{
  final static private String PARSER = "org.apache.xerces.parsers.SAXParser";
  /** The name of the ini file */
  private String      iniFilename = "gridtalkInit.xml";
  /** The storage for the content of the ini file when retriving */
  private Hashtable   initValues;

  /** Constructor */
  public InitManager()
  {
    super();
    initValues = new Hashtable();
  }

  /** Returns an Enumeration of the elements */
  public Enumeration getKeys()
  {
    return initValues.keys();
  }

  /** Reads the content of the default ini file "gridtalkInit.xml" into memory */
  public boolean loadProperties()
  {
    try
    {
      File iniFile = new File(iniFilename);
      readFile(iniFile);
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return false;
    }
  }

  /** Reads the content of the ini file specified into memory */
  public boolean loadProperties(String filename)
  {
    iniFilename = filename;
    try
    {
      File iniFile = new File(iniFilename);
      readFile(iniFile);
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return false;
    }
  }

  /** Reads the content of the ini file specified into memory */
  public boolean loadProperties(File iniFile)
  {
    try
    {
      readFile(iniFile);
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return false;
    }
  }

  private void readFile(File iniFile) throws Exception
  {
    SAXBuilder docBuilder = new SAXBuilder(PARSER, false);
//    File iniFile = new File(iniFilename);
    Document doc = docBuilder.build(iniFile);
    Element root = doc.getRootElement();
    readContent(root);
  }

  /** Save the default ini file "gridtalkInit.xml" */
  public boolean saveProperties()
  {
    try
    {
      saveFile();
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return false;
    }
  }

  /** Save the ini file specified */
  public boolean saveProperties(String filename)
  {
    iniFilename = filename;
    try
    {
      saveFile();
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return false;
    }
  }

  /** Get the content of the element with attribute equals to key */
  public String getProperty(String key)
  {
    if (initValues.containsKey(key))
    {
      return initValues.get(key).toString();
    }
    return null;
  }

  /** Set the content of the element with attribute key to value */
  public void setProperty(String key, String value)
  {
    if(key != null && !key.equals("") && value != null)
    {
      initValues.put(key, value);
    }
  }

  public void setHashtable(Hashtable table)
  {
    initValues = table;
  }

  private void saveFile() throws Exception
  {
    Element root = new Element("Init");
    Document docu = new Document(root);
    Enumeration enu = initValues.keys();
    while(enu.hasMoreElements())
    {
      String key = (String)enu.nextElement();
      String value = (String)initValues.get(key);
      Element propertyNode = new Element("Property");
      propertyNode.setAttribute("key", key);
      propertyNode.addContent(value);
      root.addContent(propertyNode);
    }
    FileOutputStream fos = new FileOutputStream(iniFilename);
    //NSL20050929 Change of JDOM XMLOutputter syntax
    Format format = Format.getPrettyFormat();
    XMLOutputter xmlout = new XMLOutputter(format);
    //xmlout.setNewlines(true);
    //xmlout.setIndent(true);
    xmlout.output(docu, fos);
    fos.flush();
    fos.close();
  }

  private void readContent(Element el)
  {
    String key;
    String value;
    List nodeList = el.getChildren();
    Iterator iterator = nodeList.iterator();
    while (iterator.hasNext())
    {
      Element element = (Element)iterator.next();
      key = element.getAttributeValue(((Attribute)
        element.getAttributes().get(0)).getName());
      value = element.getText();
      initValues.put(key, value);
    }
  }
}
