/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseFileGenerator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26 2003    Koh Han Sing        Created
 * Sep 29 2005    Neo Sok Lay         change of JDOM XMLOutputter syntax
 */
package com.gridnode.gtas.server.registration.nodelock;

import com.gridnode.gtas.server.registration.product.ProductKey;
import com.gridnode.gtas.server.registration.helpers.Logger;
import com.gridnode.gtas.server.registration.exceptions.InvalidLicenseFileException;
import com.gridnode.gtas.server.registration.exceptions.NodeLockException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LicenseFileGenerator
{
  public static final String LICENSE_ROOT = "License";
  public static final String KEY1 = "Key1";
  public static final String KEY2 = "Key2";
  public static final String KEY3 = "Key3";
  public static final String KEY4 = "Key4";
  public static final String KEY5 = "Key5";
  public static final String KEY6 = "Key6";
  public static final String KEY7 = "Key7";
  public static final String NODE_ID = "NodeId";
  public static final String START_DATE = "StartDate";
  public static final String END_DATE = "EndDate";

  private ProductKey _productKey = null;
  private String _keyF1 = "";
  private String _keyF2 = "";
  private String _keyF3 = "";
  private String _keyF4 = "";
  private String _osName = "";
  private String _osVersion = "";
  private String _machineName = "";

  public static File generateFile(ProductKey productKey, String key1,
                                  String key2, String key3, String key4,
                                  String osName, String osVersion,
                                  String machineName, int nodeId)
                                  throws Exception
  {
    Element root = new Element(LICENSE_ROOT);
    Element eKey1 = new Element(KEY1).setText(key1);
    root.addContent(eKey1);
    Element eKey2 = new Element(KEY2).setText(key2);
    root.addContent(eKey2);
    Element eKey3 = new Element(KEY3).setText(key3);
    root.addContent(eKey3);
    Element eKey4 = new Element(KEY4).setText(key4);
    root.addContent(eKey4);
    osName = WaxEngine.waxOn(osName, INodeLockConstant.KEY);
    Element eKey5 = new Element(KEY5).setText(osName);
    root.addContent(eKey5);
    osVersion = WaxEngine.waxOn(osVersion, INodeLockConstant.KEY);
    Element eKey6 = new Element(KEY6).setText(osVersion);
    root.addContent(eKey6);
    machineName = WaxEngine.waxOn(machineName, INodeLockConstant.KEY);
    Element eKey7 = new Element(KEY7).setText(machineName);
    root.addContent(eKey7);
    Element eNodeId = new Element(NODE_ID).setText(String.valueOf(nodeId));
    root.addContent(eNodeId);

    String startDate = ""+productKey.getStartDay()+"/"+productKey.getStartMth()+"/"+productKey.getStartYear();
    String endDate = ""+productKey.getEndDay()+"/"+productKey.getEndMth()+"/"+productKey.getEndYear();

    Element eStartDate = new Element(START_DATE).setText(startDate);
    root.addContent(eStartDate);
    Element eEndDate = new Element(END_DATE).setText(endDate);
    root.addContent(eEndDate);

    String filename = ""+nodeId+".key";
    File licenseFile = new File(filename);
    FileOutputStream fos = new FileOutputStream(licenseFile);
    //NSL20050929
    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
    //outputter.setNewlines(true);
    //outputter.setIndent(true);
    outputter.output(root, fos);
    return licenseFile;
  }

  public void validateLicenseFile(String licenseFile)
    throws Exception
  {
    try
    {
      FileInputStream fis = new FileInputStream(licenseFile);
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(fis);
      Element root = doc.getRootElement();

      getProductKey(root);
      getNodeInfo(root);
    }
    catch (Exception ex)
    {
      Logger.warn("[LicenseFileGenerator.validateLicenseFile]", ex);
      throw new InvalidLicenseFileException(ex);
    }

    Logger.debug("[LicenseFileGenerator.validateLicenseFile] _productKey.getCategory() = "+_productKey.getCategory());
    if (!_productKey.getCategory().equals(INodeLockConstant.GNL))
    {
      if (!NodeLockUtil.validateNode(_osName, _osVersion, _machineName))
      {
        throw new NodeLockException();
      }
    }
    else
    {
      Logger.log("[LicenseFileGenerator.validateLicenseFile] GNL license, no validation required");
    }
  }

  public static void main(String[] oafoert)
  {
    try
    {
      LicenseFileGenerator file = new LicenseFileGenerator();
      file.validateLicenseFile("c:/temp/NSong.WMA");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public ProductKey getProductKey()
  {
    return _productKey;
  }

  public String getProductKeyF1()
  {
    return _keyF1;
  }

  public String getProductKeyF2()
  {
    return _keyF2;
  }

  public String getProductKeyF3()
  {
    return _keyF3;
  }

  public String getProductKeyF4()
  {
    return _keyF4;
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

  private void getProductKey(Element root) throws Exception
  {
    _keyF1 = root.getChildText(KEY1);
    _keyF2 = root.getChildText(KEY2);
    _keyF3 = root.getChildText(KEY3);
    _keyF4 = root.getChildText(KEY4);
    int nodeId = Integer.parseInt(root.getChildText(NODE_ID));

    _productKey = ProductKey.getProductKey(_keyF1+_keyF2+_keyF3+_keyF4, nodeId);
  }

  private void getNodeInfo(Element root) throws Exception
  {
    _osName = root.getChildText(KEY5);
    _osVersion = root.getChildText(KEY6);
    _machineName = root.getChildText(KEY7);
  }
}