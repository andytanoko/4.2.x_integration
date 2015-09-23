/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLBeanUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 15, 2006        i00107             Created
 * Feb 13, 2006   Tam Wei Xiang       Added method xmlToBean(String, Class)
 * Mar 17 2007    Neo Sok Lay         Add: objToXml() and xmlToObj().
 */

package com.gridnode.util.xml;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.text.DateFormat;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.wutka.jox.JOXBeanReader;
import com.wutka.jox.JOXBeanWriter;

/**
 * @author i00107
 * This utility class handles XML <-> Bean conversions.
 */
public class XMLBeanUtil
{
  /**
   * Encode the specified bean to XML format. The bean can only be restored
   * using the decodeBean() method into the same type.
   * @param bean The bean to be encoded in XML
   * @return The bean encoded in XML, or <b>null</b> if problem encoding during
   * encoding.
   */
  public static String encodeBean(Object bean)
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(baos));
      encoder.writeObject(bean);
      encoder.close();
      return baos.toString();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Decode the specified XML into a bean object. The specified XML string <b>must</b>
   * be that produced by the encodeBean() method.
   * @param beanXml The XML string encoding the bean 
   * @return The decoded bean object, or <b>null</b> if problem encountered while decoding
   * the specified XML.
   */
  public static Object decodeBean(String beanXml)
  {
    try
    {
      XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new ByteArrayInputStream(beanXml.getBytes())));
      Object o = decoder.readObject();
      decoder.close();
      return o;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Produce a XML representation of the specified bean object. 
   * @param bean The bean to produce XML representation
   * @param rootName The root name to use for the XML
   * @return The produced XML, or <b>null</b> if problem encountered
   * while producing the XML for the specified bean. 
   */
  public static String beanToXml(Object bean, String rootName)
  {
    try
    {
      StringWriter sw = new StringWriter();
      
      JOXBeanWriter writer = new JOXBeanWriter(sw);
      writer.writeObject(rootName, bean);
      writer.close();
      return sw.toString();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }
  
  /**
   * Produce a XML representation of the specified bean object. 
   * @param bean The bean to produce XML representation
   * @param rootName The root name to use for the XML
   * @return The produced XML, or <b>null</b> if problem encountered
   * while producing the XML for the specified bean. 
   */
  public static String beanToXml(Object bean, String rootName, DateFormat dateFormat)
  {
    try
    {
      StringWriter sw = new StringWriter();
      
      JOXBeanWriter writer = new JOXBeanWriter(sw);
      writer.setDateFormat(dateFormat);
      
      writer.writeObject(rootName, bean);
      writer.close();
      return sw.toString();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }
  
  /**
   * Restore a bean from the specified XML. The bean restored <b>need not</b> 
   * be the same type as that used to produce the XML. Only matching properties are
   * restored to the specified bean instance.
   * @param beanXml The XML to restore from.
   * @param beanInstance The bean instance to restore the bean properties from XML.
   */
  public static void xmlToBean(String beanXml, Object beanInstance)
  {
    try
    {
      JOXBeanReader reader = new JOXBeanReader(new StringReader(beanXml));
      reader.readObject(beanInstance);
      reader.close();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  /**
   * Restore a bean from the specified XML. The bean restored <b>need not</b> 
   * be the same type as that used to produce the XML. Only matching properties are
   * restored to the specified bean instance.
   * Note: This method is for those classes that contain user-defined class as their instance variable.  
   * @param beanXml The XML to restore from.
   * @param beanClass The class of the bean to be restored from XML.
   * @return the instance which Class is the passed in beanClass or null if we fail to
   *         deserialize from the beanXml
   */
  public static Object xmlToBean(String beanXml, Class beanClass)
  {
    try
    {
      JOXBeanReader reader = new JOXBeanReader(new StringReader(beanXml));
      Object obj = reader.readObject(beanClass);
      reader.close();
      return obj;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }
  
  
  /**
   * Marshal the given obj into XML
   * @param obj the obj we will convert to xml
   * @return the obj in XML format or null if the marshal process is failed !
   */
  public static String objToXml(Object obj, String rootElem)
  {
    StringWriter writer = new StringWriter();
    
    try
    {
      Marshaller marshaller = new Marshaller(writer);
      if (rootElem != null)
      {
        marshaller.setRootElement(rootElem);
      }
      marshaller.marshal(obj);
      return writer.toString();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }
  
  /**
   * Unmarshal from XML to obj which class is the Class c
   * @param xml the XML content
   * @param c the target Class we use to instantiate the instance
   * @return the obj which intantiate from the given Class c or null
   *         if the unmarshal process is failed !
   */
  public static Object xmlToObj(String xml, Class c)
  {
    try
    {
      StringReader reader = new StringReader(xml);
      return Unmarshaller.unmarshal(c, reader);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }

  /*
  public static void main(String[] args)
  {
    TestBean bean = new TestBean();
    
    com.gridnode.util.config.Property prop = new com.gridnode.util.config.Property();
    prop.setCategory("category");
    prop.setKey("key");
    prop.setUid(new Integer(244));
    prop.setValue("value\r\nvalue2\r\nvalue4    \r\nvalue5");
    bean.setEmbedProp(prop);
    
    File inputFile = new File("D:/Dev/Views/i00107_GT_4.0_GTVAN/GTAS/GT-VAN/Util/test/data/mime-enc.data");
    try
    {
      
      BufferedInputStream buffIS = new BufferedInputStream(new FileInputStream(inputFile));
      byte[] buff = new byte[(int)inputFile.length()];
      buffIS.read(buff);
      bean.setBinaryData(buff);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    
    bean.setDate(new java.util.Date());
    long ts = System.currentTimeMillis();
    String xml = XMLBeanUtil.objToXml(bean, "test-bean2");
    //for (int i=0; i<10; i++)
    //{
      //XMLBeanUtil.objToXML(bean);
      //XMLBeanUtil.beanToXml(bean, "test-bean");
    //}
    long diff = System.currentTimeMillis() - ts;
    System.out.println(xml);
    System.out.println("Time taken(ms) = "+diff);
    
    //String xml = XMLBeanUtil.beanToXML(bean);
    //System.out.println(xml);
//    
//    TestBean desBean = (TestBean)XMLBeanUtil.xmlToBean(xml);
//    assert bean.equals(desBean);

    ts = System.currentTimeMillis();
    TestBean2 desBean2 = (TestBean2)XMLBeanUtil.xmlToObj(xml, TestBean2.class);
    diff = System.currentTimeMillis() - ts;
    assert bean.getBinaryData().equals(desBean2.getBinaryData());
    assert bean.getEmbedProp().getCategory().equals(desBean2.getEmbedProp().getCategory());
    assert bean.getEmbedProp().getKey().equals(desBean2.getEmbedProp().getKey());
    assert bean.getEmbedProp().getUid().equals(desBean2.getEmbedProp().getUid());
    assert bean.getEmbedProp().getValue().equals(desBean2.getEmbedProp().getValue());
    assert bean.getDate().getTime() == desBean2.getDate().getTime(); 
    System.out.println("Time taken(ms) = "+diff);
    
  } */
    
}


