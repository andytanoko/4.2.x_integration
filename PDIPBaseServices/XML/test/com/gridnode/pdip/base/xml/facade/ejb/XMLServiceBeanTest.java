/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLServiceBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Koh Han Sing        Modified to confront to coding standard
 */
package com.gridnode.pdip.base.xml.facade.ejb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class XMLServiceBeanTest extends TestCase
{
  IXMLServiceLocalHome _home;
  IXMLServiceLocalObj _remote;
//  ConvertorControllerTest _controller;

  public XMLServiceBeanTest(String name)
  {
    super(name);
//    _controller = new ConvertorControllerTest();
  }

  public static Test suite()
  {
    return new TestSuite(XMLServiceBeanTest.class);
  }

  public void setUp()
  {
    try
    {
//      _home = (IXMLServiceLocalHome)ServiceLookup.getInstance(
//              ServiceLookup.LOCAL_CONTEXT).getHome(IXMLServiceLocalHome.class);
//      assertNotNull("home null", _home);
//      _remote = _home.create();
//      assertNotNull("remote null", _remote);
      _remote = (IXMLServiceLocalObj)ServiceLocator.instance(
                  ServiceLocator.LOCAL_CONTEXT).getObj(
                  IXMLServiceLocalHome.class.getName(),
                  IXMLServiceLocalHome.class,
                  new Object[0]);
    }
    catch(Exception ex)
    {
      log("Exception creating ejb", ex);
    }
  }
/*
  public void xtestConvertPositionalTextToXML()
  {
    try
    {
      _remote.convert("../data/input/PositionalText.txt",
                      "../data/output/PositionalText.xml",
                      "../data/rules/PositionalTextToXML.xml",
                      _controller);
    }
    catch (Exception ex)
    {
      log("Exception in testConvertPositionalTextToXML", ex);
      assertTrue("Exception in testConvertPositionalTextToXML", false);
    }
  }

  public void xtestConvertExcelToXML()
  {
    try
    {
      _remote.convert("../data/input/Excel.xls",
                      "../data/output/Excel.xml",
                      "../data/rules/ExcelToXML.xml",
                      _controller);

      Element root = XMLDocumentUtility.getRoot("../data/output/Excel.xml");

      Element header = root.getChild("header");
      assertNotNull("Header is null", header);
      Element version = header.getChild("version");
      assertNotNull("version is null", version);
      assertEquals("version is not correct", version.getText(), "1");
      Element document = header.getChild("document");
      assertNotNull("document is null", document);
      assertEquals("document is not correct", document.getText(), "Excel.xls");
      Element partnerID = header.getChild("partnerID");
      assertNotNull("partnerID is null", partnerID);
      assertEquals("partnerID is not correct", partnerID.getText(), "1102");
      Element recDate = header.getChild("recDate");
      assertNotNull("recDate is null", recDate);
      assertEquals("recDate is not correct", recDate.getText(), "@now");

      Element records = root.getChild("records");
      assertNotNull("Records is null", records);
      List children = records.getChildren();
      int noOfRecs = children.size();
      assertEquals("Wrong number of records", noOfRecs, 2);

      Element record1 = (Element)children.get(0);
      Element item1 = record1.getChild("item");
      assertNotNull("item1 is null", item1);
      assertEquals("item1 is not correct", item1.getText(), "1");
      Element cust_no1 = record1.getChild("cust_no");
      assertNotNull("cust_no1 is null", cust_no1);
      assertEquals("cust_no1 is not correct", cust_no1.getText(), "CUST01");
      Element supplier_no1 = record1.getChild("supplier_no");
      assertNotNull("supplier_no1 is null", supplier_no1);
      assertEquals("supplier_no1 is not correct", supplier_no1.getText(), "SUPP01");
      Element unit_price1 = record1.getChild("unit_price");
      assertNotNull("unit_price1 is null", unit_price1);
      assertEquals("unit_price1 is not correct", unit_price1.getText(), "1.000000");
      Element quantity1 = record1.getChild("quantity");
      assertNotNull("quantity1 is null", quantity1);
      assertEquals("quantity1 is not correct", quantity1.getText(), "100");
      Element amt1 = record1.getChild("amt");
      assertNotNull("amt1 is null", amt1);
      assertEquals("amt1 is not correct", amt1.getText(), "100.00");
      Element rec_date1 = record1.getChild("rec_date");
      assertNotNull("rec_date1 is null", rec_date1);
      assertEquals("rec_date1 is not correct", rec_date1.getText(), "1/1/2002");
      Element cd1 = record1.getChild("cd");
      assertNotNull("cd1 is null", cd1);
      assertEquals("cd1 is not correct", cd1.getText(), "31/01/2002");
      Element remarks1 = record1.getChild("remarks");
      assertNotNull("remarks1 is null", remarks1);
      assertEquals("remarks1 is not correct", remarks1.getText(), "Remark1");
      Element cust1 = record1.getChild("cust");
      assertNotNull("cust1 is null", cust1);
      assertEquals("cust1 is not correct", cust1.getText(), "AT0001");
      Element po_no1 = record1.getChild("po_no");
      assertNotNull("po_no1 is null", po_no1);
      assertEquals("po_no1 is not correct", po_no1.getText(), "11111-0");
      Element currency1 = record1.getChild("currency");
      assertNotNull("currency1 is null", currency1);
      assertEquals("currency1 is not correct", currency1.getText(), "USD");
      Element area_code1 = record1.getChild("area_code");
      assertNotNull("area_code1 is null", area_code1);
      assertEquals("area_code1 is not correct", area_code1.getText(), "USA");
      Element brand1 = record1.getChild("brand");
      assertNotNull("brand1 is null", brand1);
      assertEquals("brand1 is not correct", brand1.getText(), "BRAND1");
      Element ship_to_code1 = record1.getChild("ship_to_code");
      assertNotNull("ship_to_code1 is null", ship_to_code1);
      assertEquals("ship_to_code1 is not correct", ship_to_code1.getText(), "01");
      Element store1 = record1.getChild("store");
      assertNotNull("store1 is null", store1);
      assertEquals("store1 is not correct", store1.getText(), "STORE1");

      Element record2 = (Element)children.get(1);
      Element item2 = record2.getChild("item");
      assertNotNull("item2 is null", item2);
      assertEquals("item2 is not correct", item2.getText(), "2");
      Element cust_no2 = record2.getChild("cust_no");
      assertNotNull("cust_no2 is null", cust_no2);
      assertEquals("cust_no2 is not correct", cust_no2.getText(), "CUST02");
      Element supplier_no2 = record2.getChild("supplier_no");
      assertNotNull("supplier_no2 is null", supplier_no2);
      assertEquals("supplier_no2 is not correct", supplier_no2.getText(), "SUPP02");
      Element unit_price2 = record2.getChild("unit_price");
      assertNotNull("unit_price2 is null", unit_price2);
      assertEquals("unit_price2 is not correct", unit_price2.getText(), "2.000000");
      Element quantity2 = record2.getChild("quantity");
      assertNotNull("quantity2 is null", quantity2);
      assertEquals("quantity2 is not correct", quantity2.getText(), "200");
      Element amt2 = record2.getChild("amt");
      assertNotNull("amt2 is null", amt1);
      assertEquals("amt2 is not correct", amt2.getText(), "400.00");
      Element rec_date2 = record2.getChild("rec_date");
      assertNotNull("rec_date2 is null", rec_date2);
      assertEquals("rec_date2 is not correct", rec_date2.getText(), "2/2/2002");
      Element cd2 = record2.getChild("cd");
      assertNotNull("cd2 is null", cd2);
      assertEquals("cd2 is not correct", cd2.getText(), "28/02/2002");
      Element remarks2 = record2.getChild("remarks");
      assertNotNull("remarks2 is null", remarks2);
      assertEquals("remarks2 is not correct", remarks2.getText(), "Remark2");
      Element cust2 = record2.getChild("cust");
      assertNotNull("cust2 is null", cust2);
      assertEquals("cust2 is not correct", cust2.getText(), "AT0002");
      Element po_no2 = record2.getChild("po_no");
      assertNotNull("po_no2 is null", po_no2);
      assertEquals("po_no2is not correct", po_no2.getText(), "22222-0");
      Element currency2 = record2.getChild("currency");
      assertNotNull("currency2 is null", currency2);
      assertEquals("currency2 is not correct", currency2.getText(), "YEN");
      Element area_code2 = record2.getChild("area_code");
      assertNotNull("area_code2 is null", area_code2);
      assertEquals("area_code2 is not correct", area_code2.getText(), "JPN");
      Element brand2 = record2.getChild("brand");
      assertNotNull("brand2 is null", brand2);
      assertEquals("brand2 is not correct", brand2.getText(), "BRAND2");
      Element ship_to_code2 = record2.getChild("ship_to_code");
      assertNotNull("ship_to_code2 is null", ship_to_code2);
      assertEquals("ship_to_code2 is not correct", ship_to_code2.getText(), "02");
      Element store2 = record2.getChild("store");
      assertNotNull("store2 is null", store2);
      assertEquals("store2 is not correct", store2.getText(), "STORE2");
   }
    catch (Exception ex)
    {
      log("Exception in testConvertExcelToXML", ex);
      assertTrue("Exception in testConvertExcelToXML", false);
    }
  }

  public void xtestConvertDelimitedTextToXML()
  {
    try
    {
      _remote.convert("../data/input/DelimitedText.txt",
                      "../data/output/DelimitedText.xml",
                      "../data/rules/DelimitedTextToXML.xml",
                      _controller);
    }
    catch (Exception ex)
    {
      log("Exception in testConvertDelimitedTextToXML", ex);
      assertTrue("Exception in testConvertDelimitedTextToXML", false);
    }
  }

  public void xtestConvertFixedLengthDataToXML()
  {
    try
    {
      _remote.convert("../data/input/FixedLengthData.txt",
                      "../data/output/FixedLengthData.xml",
                      "../data/rules/FixedLengthDataToXML.xml",
                      _controller);
    }
    catch (Exception ex)
    {
      log("Exception in testConvertFixedLengthDataToXML", ex);
      assertTrue("Exception in testConvertFixedLengthDataToXML", false);
    }
  }
*/

/**  public void xtestExtract()
  {
    try
    {
      ArrayList list = _remote.extractXPathFromFile("../data/input/SORTED_3C3.xml");
      assertNotNull("ArrayList is null", list);
      if (list != null)
      {
        for(int i = 0; i < list.size(); i++)
        {
          String name = (String)list.get(i);
          log("XPath = "+name);
        }
      }
    }
    catch (Exception ex)
    {
      log("Exception in testExtract", ex);
      assertTrue("Exception in testExtract", false);
    }
  }
*/

/*  public void xtestTransform()
  {
    try
    {
      Hashtable params = new Hashtable();
      params.put("RECEIVER", "AMK10165");
      File outFile =
        _remote.transform("../data/xsl/rnSPLIT_AMKBCI_RN_CON.xsl",
                          "../data/input/SORTED_3C3.xml",
                          params);
      log(";P outFile = " + outFile.getAbsolutePath());
      File newFile = new File("Transformed.xml");
      outFile.renameTo(newFile);
    }
    catch(Exception ex)
    {
      log("Exception in testTransform", ex);
      assertTrue("Exception in testTransform", false);
    }
  }

  public void testSplitting()
  {
    try
    {
      ArrayList fileList =
        _remote.splitXML("../data/xsl/rnSPLIT_AMKBCI_RN_CON.xsl",
                         "../data/input/SORTED_3C3.xml",
                         "AMKBCI/Pip3C3InvoiceNotification/toRole/PartnerRoleDescription/PartnerDescription/BusinessDescription/SeagateProprietaryVendorNumber",
                         "RECEIVER");
      for (int i = 0; i < fileList.size(); i++)
      {
        File outFile = (File)fileList.get(i);
        log(";P outFile = " + outFile.getAbsolutePath());
        File newFile = new File("Splitted"+i+".xml");
        outFile.renameTo(newFile);
      }
    }
    catch(Exception ex)
    {
      log("Exception in testTransform", ex);
      assertTrue("Exception in testTransform", false);
    }
  }


  public void xtestValidator()
  {
    try
    {
      ArrayList list =
        _remote.validate("../data/input/SORTED_3C3.xml",
                         "../data/input/3C3_MS_R01_00_InvoiceNotification.xml",
                         "../data/input/3C3_MS_R01_00_InvoiceNotification.dtd",
                         30);
      assertNotNull("Validator List Null",list);
      if (list != null)
      {
        for(int i = 0; i < list.size(); i++)
        {
          log("Value is = "+list.get(i));
        }
      }
    }
    catch (Exception ex)
    {
      log("Exception in testValidator", ex);
      assertTrue("Exception in testValidator", false);
    }
  }

*/

  public void testXML()
  {
    try
    {
      _remote.appendXML("c:/temp/NodeLockInfo.xml", "c:/temp/NodeLockInfo.xml");

//    _remote.convert();
//    _remote.extractDTDFilename();
//    _remote.getDocument();
//    _remote.getRoot();
//    _remote.getRootName();
//    _remote.getXPathValues();
//    _remote.newDocument();
//    _remote.newDocumentType();
//    _remote.newElement();
//    _remote.newNamespace();
//    _remote.splitXML();
//    _remote.transform();
//    _remote.validate();
//    _remote.validateDTD();
//    _remote.validateSchema();
//    _remote.writeToFile();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public static void main(String args[]) throws Exception
  {
    junit.textui.TestRunner.run(suite());
    //junit.swingui.TestRunner.run(XMLServiceBeanTest.class);
  }

  private void log(String msg)
  {
    Log.log("TEST", msg);
    System.out.println(msg);
  }

  private void log(String msg, Throwable ex)
  {
    Log.log("TEST", msg, ex);
    System.out.println(msg);
    ex.printStackTrace();
  }
}

