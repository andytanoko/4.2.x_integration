/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Sep 30 2005    Neo Sok Lay         Castor-XML 0.9.9 requires explicit set
 *                                    to ignore extra elem & attribs in xml file
 *                                    during unmarshalling.
 * Dec 22 2005    Neo Sok Lay         Load mapping using URL instead of String
 * Feb 17 2006    Neo Sok Lay         Set Unmarshaller to preserve whitespace
 *                                    to prevent stripping off whitespaces in element content.
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception                                                                      
 */
package com.gridnode.pdip.framework.db;

import java.io.*;
import java.util.Hashtable;

import org.apache.xml.serialize.Method;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;

/**
 * This data handler handles JDO<-->XML data object conversions.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 1.0a build 0.9.9.6
 */
public class XMLHandler implements IDataHandler
{
  private static Hashtable<String,Mapping> _xmlMap = new Hashtable<String,Mapping>();

  public XMLHandler()
  {
  }

  /**
   * Load a XML mapping configuration file for use in handling later on.
   *
   * @param xmlName Name for the type mapping configuration
   * @param mappingFile Name of the configuration file
   * @exception Exception Error loading due to configuration file not found
   * or invalid configuration file, etc.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static void loadXML(String xmlName, String mappingFile)
    throws Exception
  {
    if (_xmlMap.containsKey(xmlName))
      return;

    try
    {
      Mapping mapping = new Mapping( XMLHandler.class.getClassLoader() );
      //mapping.loadMapping( "file:///"+mappingFile );
      mapping.loadMapping(new File(mappingFile).toURL()); //NSL20051222
      _xmlMap.put(xmlName, mapping);
    }
    catch (Exception ex)
    {
      Log.warn(Log.DB, "[XMLHandler.loadXML]", ex);
      throw new Exception("Unable to load mapping file '"+mappingFile+"'");
    }
  }
  /**
   * Write a data object (JDO) to xml output file.
   *
   * @param xmlName Name of the type of mapping configuration to use for
   * handling the data object.
   * @param dataObj The data object
   * @param outputFile Name of file to write to
   *
   * @see #write(String,DataObject,Writer)
   *
   * @since 1.0a build 0.9.9.6
   */
  public static void write(
    String xmlName, DataObject dataObj, String outputFile)
    throws Exception
  {
    write(xmlName, dataObj, new BufferedWriter(new FileWriter(outputFile)));
  }

  /**
   * Write a data object (JDO) to xml output.
   *
   * @param xmlName Name of the type of mapping configuration to use for
   * handling the data object.
   * @param dataObj The data object
   * @param writer The writer to use for output
   *
   * @see #write(String,DataObject[],Writer,String)
   *
   * @since 1.0a build 0.9.9.6
   */
  public static void write(String xmlName, DataObject dataObj, Writer writer)
    throws Exception
  {
    write(xmlName, new DataObject[]{ dataObj }, writer, "");
  }

  public static void write(Mapping mapping, DataObject dataObj, Writer writer)
    throws Exception
  {
    write(mapping, new DataObject[]{ dataObj }, writer, "");
  }

  /**
   * Write an array of data objects (JDO) to xml output file.
   *
   * @param xmlName Name of the type of mapping configuration to use for
   * handling the data object.
   * @param dataObj The data objects
   * @param outputFile Name of file to write to
   * @param rootElement The root element of the output xml document
   *
   * @see #write(String,DataObject[],Writer,String)
   *
   * @since 1.0a build 0.9.9.6
   */
  public static void write(
    String xmlName, DataObject[] dataObj, String outputFile, String rootElement)
    throws Exception
  {
    write(xmlName, dataObj, new FileWriter(outputFile), rootElement);
  }

  /**
   * Write an array of data objects (JDO) to xml output file.
   *
   * @param xmlName Name of the type of mapping configuration to use for
   * handling the data object.
   * @param dataObj The data objects
   * @param writer The writer to use for output
   * @param rootElement The root element of the output xml document
   * @exception Exception Error writing the data objects
   *
   * @since 1.0a build 0.9.9.6
   */
//  public static void write(
//    String xmlName, DataObject[] dataObj, Writer writer, String rootElement)
//    throws Exception
//  {
//    Mapping mapping = (Mapping)_xmlMap.get(xmlName);
//    if (mapping == null)
//      return;
//    try
//    {
//      XMLSerializer ser =
//        new XMLSerializer(new OutputFormat(Method.XML, "UTF-8", true));;
//      ser.setOutputCharStream(writer);
//
//      ContentHandler handler = ser.asContentHandler();;
//      Marshaller marshaller = new Marshaller(ser.asDocumentHandler());
//      marshaller.setMapping(mapping);
//      marshaller.setMarshalAsDocument(true);
//
//      //mark start of document
//      handler.startDocument();
//      //mark start of root
//      if (rootElement != null && rootElement.length()!=0)
//        handler.startElement(null, null, rootElement, null);
//      //write each data object
//      for (int i=0; i<dataObj.length; i++)
//        marshaller.marshal(dataObj[i]);
//      //mark end of root
//      if (rootElement != null && rootElement.length()!=0)
//        handler.endElement(null, null, rootElement);
//      //mark end of document
//      handler.endDocument();
//    }
//    catch (Exception ex)
//    {
//      Log.err(Log.DB, "[XMLHandler.write]", ex);
//      throw new Exception("Unable to write objects to xml");
//    }
//    finally
//    {
//      writer.flush();
//      writer.close();
//    }
//  }

  public static void write(
    Mapping mapping, DataObject[] dataObj, Writer writer, String rootElement)
    throws Exception
  {
    try
    {
      XMLSerializer ser =
        new XMLSerializer(new OutputFormat(Method.XML, "UTF-8", true));
      ser.setOutputCharStream(writer);

      ContentHandler handler = ser.asContentHandler();
      Marshaller marshaller = new Marshaller(ser.asDocumentHandler());
//      marshaller.setRootElement(rootElement);
      marshaller.setMapping(mapping);
      marshaller.setMarshalAsDocument(true);
//      marshaller.setNamespaceMapping("xsi", Marshaller.XSI_NAMESPACE);
//      marshaller.setMarshalExtendedType(true);
      marshaller.setLogWriter(new PrintWriter(System.out));

      //mark start of document
      handler.startDocument();
      //mark start of root
      if (rootElement != null && rootElement.length()!=0)
        handler.startElement(null, null, rootElement, null);
      //write each data object
      for (int i=0; i<dataObj.length; i++)
        marshaller.marshal(dataObj[i]);
      //mark end of root
      if (rootElement != null && rootElement.length()!=0)
        handler.endElement(null, null, rootElement);
      //mark end of document
      handler.endDocument();
    }
    catch (Exception ex)
    {
      Log.warn(Log.DB, "[XMLHandler.write]", ex);
      throw new Exception("Unable to write objects to xml");
    }
    finally
    {
      writer.flush();
      writer.close();
    }
  }

  public static void write(
    String xmlName, DataObject[] dataObj, Writer writer, String rootElement)
    throws Exception
  {
    try
    {
//      XMLSerializer ser =
//        new XMLSerializer(new OutputFormat(Method.XML, "UTF-8", true));;
//      ser.setOutputCharStream(writer);

//      ContentHandler handler = ser.asContentHandler();;
      Marshaller marshaller = new Marshaller(writer);
//      marshaller.setMapping(mapping);
      marshaller.setMarshalAsDocument(true);

      //mark start of document
//      handler.startDocument();
      //mark start of root
//      if (rootElement != null && rootElement.length()!=0)
//        handler.setRootElement(rootElement);

//      marshaller.setMarshalExtendedType(false);
//        handler.startElement(null, null, rootElement, null);
      //write each data object
      for (int i=0; i<dataObj.length; i++)
        marshaller.marshal(dataObj[i]);
      //mark end of root
//      if (rootElement != null && rootElement.length()!=0)
//        handler.endElement(null, null, rootElement);
      //mark end of document
//      handler.endDocument();
    }
    catch (Exception ex)
    {
      Log.warn(Log.DB, "[XMLHandler.write]", ex);
      throw new Exception("Unable to write objects to xml");
    }
    finally
    {
      writer.flush();
      writer.close();
    }
  }

  /**
   * Reads a data object from a xml document.
   *
   * @param xmlName Name of the type mapping configuration to use to handle
   * the data object
   * @param objClass The required class type of the data object
   * @param reader The reader use for reading the xml document
   * @return The data object read, or <B>null</B> if no mapping configuration
   * found or unable to read tge data object from <I>reader</I>.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static DataObject read(String xmlName, Class objClass, Reader reader)
  {
//    Mapping mapping = (Mapping)_xmlMap.get(xmlName);
//    if (mapping == null)
//      return null;
    try
    {
      Unmarshaller unmarshaller = new Unmarshaller( objClass );
      unmarshaller.setIgnoreExtraAttributes(true); //NSL20050930
      unmarshaller.setIgnoreExtraElements(true); //NSL20050930
      unmarshaller.setWhitespacePreserve(true); //NSL20060217
//      unmarshaller.setMapping( mapping );
      return (DataObject) unmarshaller.unmarshal( reader );
    }
    catch (Exception ex)
    {
      Log.warn(Log.DB, "[XMLHandler.read]", ex);
      return null;
    }
    finally
    {
      try
      {
        reader.close();
      }
      catch (Exception ex2)
      {
      }
    }
  }

  public static DataObject read(Mapping mapping, Class objClass, Reader reader)
  {
  	String mn = "read";
    try
    {
      Unmarshaller unmarshaller = new Unmarshaller( objClass );
      unmarshaller.setMapping( mapping );
      unmarshaller.setIgnoreExtraAttributes(true); //NSL20050930
      unmarshaller.setIgnoreExtraElements(true); //NSL20050930
      unmarshaller.setWhitespacePreserve(true); //NSL20060217
      return (DataObject) unmarshaller.unmarshal( reader );
    }
		catch (MarshalException e)
		{
			logError(ILogErrorCodes.OBJECT_UNMARSHALL, mn, "Castor marshalling error: "+e.getMessage(),e);
      return null;
		}
		catch (MappingException e)
		{
			logError(ILogErrorCodes.OBJECT_UNMARSHALL, mn, "Castor mapping error: "+e.getMessage(),e);
      return null;
		}
		catch (ValidationException e)
		{
			logError(ILogErrorCodes.OBJECT_UNMARSHALL, mn, "Castor validation error: "+e.getMessage(),e);
      return null;
		}
    catch (Exception ex)
    {
      logError(ILogErrorCodes.OBJECT_UNMARSHALL, mn, "Unexpected error: ", ex);
      return null;
    }
    finally
    {
      try
      {
        reader.close();
      }
      catch (Exception ex2)
      {
      	Log.warn(Log.DB, "[XMLHandler.read] Unable to close reader", ex2);
      }
    }
  }

  /**
   * Reads a data object from a xml document.
   *
   * @param xmlName Name of the type mapping configuration to use to handle
   * the data object
   * @param objClass The required class type of the data object
   * @param sourceFile The xml document
   * @return The data object read, or <B>null</B> if no mapping configuration
   * found or unable to read tge data object from <I>sourceFile</I>.
   * @exception FileNotFoundException <I>sourceFile</I> does not exists
   * physically.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static DataObject read(
    String xmlName, Class objClass, String sourceFile)
    throws FileNotFoundException
  {
    return read(
           xmlName, objClass,
           new BufferedReader(new FileReader(sourceFile)));
  }
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(XMLHandler.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
  	Log.error(errorCode, Log.DB, buf.toString(), t);
  }

//  public static void main(String[] args)
//  {
//    try
//    {
//      DataTypeFilter tFilter = new DataTypeFilter();
//      tFilter.setAndOr("and");
//      tFilter.setType("type");
//      DataFilter filter = new DataFilter();
//      filter.setConnector("and");
//      filter.setNegate(false);
//      ValueFilter vFilter = new ValueFilter();
//      vFilter.setFilterField("FieldA");
//      vFilter.setOperator("=");
//      vFilter.setSingleValue("valueA");
//      filter.setValueFilter(vFilter);
//      tFilter.setFilter(filter);
//
//      ObjectXmlSerializer ser = new ObjectXmlSerializer();
//      XmlObjectDeserializer deser = new XmlObjectDeserializer();
//
//      ser.serialize(tFilter, "testxml-1.txt");
//
//      ser.serialize(vFilter, new FileWriter("testxml-2.txt"));
//
//      Object deserObj1 = deser.deserialize(DataTypeFilter.class, "testxml-1.txt" );
//      Object deserObj2 = deser.deserialize(ValueFilter.class,
//        "testxml-2.txt");
//
//      System.out.println("DeserObj1="+tFilter);
//      System.out.println("DeserObj2="+vFilter);
//    }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//    }
//
//  }

}