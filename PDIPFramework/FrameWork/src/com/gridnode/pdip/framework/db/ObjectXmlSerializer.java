/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ObjectXmlSerializer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 22 2002    Neo Sok Lay         Created
 * May 20 2003    Koh Han Sing        Added method to use other mapping files
 * Feb 07 2007		Alain Ah Ming				Log warning message if error is not handled
 */
package com.gridnode.pdip.framework.db;

import com.gridnode.pdip.framework.log.Log;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.apache.xml.serialize.Method;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.Writer;

/**
 * This serializer class is capable of serializing any object into XML format.
 * If a class xml mapping file is provided for the object, it would be serialized
 * according to the format specified in the mapping file.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0 I4
 */
public class ObjectXmlSerializer
  extends XMLSerializer
{
  public static final String ENCODING_UTF8  = "UTF-8";
  public static final String ENCODING_UTF16 = "UTF-16";

  /**
   * Construct an ObjectXmlSerializer instance which serialize objects
   * to xml using the default encoding: UTF-8.
   *
   * @since 2.0 I4
   */
  public ObjectXmlSerializer()
  {
    this(ENCODING_UTF8);
  }

  /**
   * Construct an ObjectXmlSerializer instance which serialize objects to
   * xml using the specified encoding method.
   *
   * @param encoding The encoding to use.
   *
   * @since 2.0 I4
   */
  public ObjectXmlSerializer(String encoding)
  {
    super();
    OutputFormat format = new OutputFormat(Method.XML, encoding, true);
    format.setLineWidth(1000);
    setOutputFormat(format);
  }

  /**
   * Serialize an object to an output file.
   *
   * @param dataObj The object to serialize
   * @param outputFile The output filename.
   * @exception Exception Error in serializing the object.
   * @since 2.0 I4
   */
  public void serialize(Object dataObj, String outputFile)
    throws Exception
  {
    serialize(dataObj, new BufferedWriter(new FileWriter(outputFile)));
  }

  /**
   * Serialize an object to a Writer.
   *
   * @param dataObj The object to serialize
   * @param writer The writer.
   * @throws Exception 
   * @exception Exception Error in serializing the object.
   * @since 2.0 I4
   */
  public void serialize(Object dataObj, Writer writer)
    throws Exception
  {
    boolean useMapping = ObjectMappingRegistry.getInstance().hasLoadedMappings();

    try
    {
      setOutputCharStream(writer);

      Marshaller marshaller = new Marshaller(asDocumentHandler());

      if (useMapping)
        marshaller.setMapping(ObjectMappingRegistry.getInstance().getMappingLoader());

      marshaller.setMarshalAsDocument(true);
      marshaller.marshal(dataObj);
    }
		catch (MarshalException e)
		{
			throw new Exception("Castor marshalling error: "+e.getMessage(), e);
		}
		catch (ValidationException e)
		{
			throw new Exception("Castor validation error: "+e.getMessage(), e);
		}
		catch (MappingException e)
		{
			throw new Exception("Castor mapping error: "+e.getMessage(), e);
		}
    catch (Throwable ex)
    {
      throw new Exception("Unexpected Error: "+ex.getMessage(), ex);
    }
    finally
    {
      writer.flush();
      writer.close();
    }
  }

  /**
   * Serialize an object to an output file with the mapping.
   *
   * @param dataObj The object to serialize
   * @param outputFile The output filename.
   * @param mapping The mapping to use.
   * @exception Exception Error in serializing the object.
   * @since 2.1 I1
   */
  public void serialize(Object dataObj, String outputFile, Mapping mapping)
    throws Exception
  {
    serialize(dataObj, new BufferedWriter(new FileWriter(outputFile)), mapping);
  }

  /**
   * Serialize an object to a Writer.
   *
   * @param dataObj The object to serialize
   * @param writer The writer.
   * @param mapping The mapping to use.
   * @exception Exception Error in serializing the object.
   * @since 2.1 I1
   */
  public void serialize(Object dataObj, Writer writer, Mapping mapping)
    throws Exception
  {
    try
    {
      setOutputCharStream(writer);

      Marshaller marshaller = new Marshaller(asDocumentHandler());

      marshaller.setMapping(mapping);

      marshaller.setMarshalAsDocument(true);
      marshaller.marshal(dataObj);
    }
    catch (Exception ex)
    {
      Log.warn(Log.DB, "[ObjectXmlSerializer.serialize]", ex);
      throw new Exception("Unable to serialize object to xml");
    }
    finally
    {
      writer.flush();
      writer.close();
    }
  }

}