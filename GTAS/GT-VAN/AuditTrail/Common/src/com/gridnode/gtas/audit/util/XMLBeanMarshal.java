/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLBeanMarshal.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 1, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.util;

import java.io.StringReader;
import java.io.StringWriter;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class XMLBeanMarshal
{
  /**
   * Marshal the given obj into XML
   * @param obj the obj we will convert to xml
   * @return the obj in XML format or null if the marshal process is failed !
   */
  public static String objToXML(Object obj)
  {
    StringWriter writer = new StringWriter();
    
    try
    {
      Marshaller marshaller = new Marshaller(writer);
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
}
