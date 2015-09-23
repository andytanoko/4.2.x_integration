/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NodeInfoGenerator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.nodelock;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.net.InetAddress;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NodeInfoGenerator
{
  private static final String INFO1  = "Info1";
  private static final String INFO2  = "Info2";
  private static final String INFO3  = "Info3";

  private String _osName;
  private String _osVersion;
  private String _machineName;

  public NodeInfoGenerator()
  {
    this.getSystemInfo();
  }

  public static void main(String[] args)
  {
    NodeInfoGenerator generator = new NodeInfoGenerator();
    generator.writeToFile();
    System.exit(0);
  }

  private void getSystemInfo()
  {
    try
    {
      _osName      = System.getProperty("os.name");
      _osName      = WaxEngine.waxOn(_osName, INodeLockConstant.KEY);
      _osVersion   = System.getProperty("os.version");
      _osVersion   = WaxEngine.waxOn(_osVersion, INodeLockConstant.KEY);
      _machineName = InetAddress.getLocalHost().getHostName();
      _machineName = WaxEngine.waxOn(_machineName, INodeLockConstant.KEY);
    }
    catch (Exception ex)
    {
      System.out.println("Error retreiving system information");
      ex.printStackTrace(System.out);
    }
  }

  private void writeToFile()
  {
    try
    {
      Element root = new Element("NodeLockInfo");
      Element value1 = new Element(INFO1).setText(_osName);
      Element value2 = new Element(INFO2).setText(_osVersion);
      Element value3 = new Element(INFO3).setText(_machineName);
      root.addContent(value1);
      root.addContent(value2);
      root.addContent(value3);

      FileOutputStream fos = new FileOutputStream("NodeLockInfo.xml");
      XMLOutputter outputter = new XMLOutputter();
      outputter.output(root, fos);
      fos.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error creating nodelock file");
      ex.printStackTrace(System.out);
    }
  }

  public void readNodeLockFile(String fullPathFilename)
    throws FileNotFoundException, JDOMException, IOException
  {
    FileInputStream fis = new FileInputStream(fullPathFilename);
    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(fis);
    Element root = doc.getRootElement();
    _osName = root.getChildText(INFO1);
    _osName = WaxEngine.waxOff(_osName, INodeLockConstant.KEY);
    _osVersion = root.getChildText(INFO2);
    _osVersion = WaxEngine.waxOff(_osVersion, INodeLockConstant.KEY);
    _machineName = root.getChildText(INFO3);
    _machineName = WaxEngine.waxOff(_machineName, INodeLockConstant.KEY);
  }

  public String getOsName()
  {
    return _osName;
  }

  public String getOsVersion()
  {
    return _osVersion;
  }

  public String getMachineName()
  {
    return _machineName;
  }

}