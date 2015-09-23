/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XmlObjectDeserializer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 22 2002    Neo Sok Lay         Created
 * May 20 2003    Koh Han Sing        Added method to use other mapping files
 * Sep 30 2005    Neo Sok Lay         Castor-XML 0.9.9 requires explicit set
 *                                    to ignore extra elem & attribs in xml file
 *                                    during unmarshalling.
 * Feb 17 2006    Neo Sok Lay         Set Unmarshaller to preserve whitespace
 *                                    to prevent stripping off whitespaces in element content.
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing exception up                                                                      
 */
package com.gridnode.pdip.framework.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import com.gridnode.pdip.framework.log.Log;

/**
 * This deserializer class is capable of deserializing any object from
 * xml format, providing that the target class type of the object is within
 * the class loader of the application.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0 I4
 */
public class XmlObjectDeserializer
{
  public XmlObjectDeserializer()
  {
  }

  /**
   * Deserialize an xml file into an object.
   *
   * @param objClass The desired class type of the object.
   * @return sourceFile The Xml file to deserialize from.
   * @exception Exception Error in deserializing the file into object. For
   * example, the file may not exist or it is not a valid xml document.
   *
   * @since 2.0 I4
   */
  public Object deserialize(Class objClass, String sourceFile)
    throws Exception
  {
    return deserialize(objClass, new BufferedReader(new FileReader(sourceFile)));
  }

  /**
   * Deserialize from a reader an xml stream of data into an object.
   *
   * @param objClass The desired class type of the deserialized object.
   * @param reader The reader to read xml data from.
   * @return The deserialized object.
   * @exception Exception Error in deserializing into object. For example,
   * the reader fails to read xml data.
   *
   * @since 2.0 I4
   */
  public Object deserialize(Class objClass, Reader reader)
    throws Exception
  {
    Object deser = null;

    try
    {
      boolean useMapping = ObjectMappingRegistry.getInstance().hasLoadedMappings();

      Unmarshaller unmarshaller = new Unmarshaller(objClass);

      if (useMapping)
        unmarshaller.setMapping(
          ObjectMappingRegistry.getInstance().getMappingLoader());

      unmarshaller.setIgnoreExtraAttributes(true); //NSL20050930
      unmarshaller.setIgnoreExtraElements(true); //NSL20050930
      unmarshaller.setWhitespacePreserve(true); //NSL20060217
      deser = unmarshaller.unmarshal(reader);
    }
		catch (MarshalException e)
		{
      throw new Exception("Castor marshalling error: "+e.getMessage());
		}
		catch (ValidationException e)
		{
      throw new Exception("Castor validation error: "+e.getMessage());
		}
		catch (MappingException e)
		{
      throw new Exception("Castor mapping error: "+e.getMessage());
		}
    catch (Throwable ex)
    {
      throw new Exception("Unexpected error: "+ex.getMessage());
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

    return deser;
  }

  /**
   * Deserialize an xml file into an object with the given mapping.
   *
   * @param objClass The desired class type of the object.
   * @param sourceFile The Xml file to deserialize from.
   * @param mapping The mapping to use to deserialize the xml file.
   * @return The deserialized object.
   * @exception Exception Error in deserializing the file into object. For
   * example, the file may not exist or it is not a valid xml document.
   *
   * @since 2.1 I1
   */
  public Object deserialize(Class objClass, String sourceFile, Mapping mapping)
    throws Exception
  {
    return deserialize(objClass, new BufferedReader(new FileReader(sourceFile)), mapping);
  }

  /**
   * Deserialize from a reader an xml stream of data into an object.
   *
   * @param objClass The desired class type of the deserialized object.
   * @param reader The reader to read xml data from.
   * @param mapping The mapping to use to deserialize the xml file.
   * @return The deserialized object.
   * @exception Exception Error in deserializing into object. For example,
   * the reader fails to read xml data.
   *
   * @since 2.1 I1
   */
  public Object deserialize(Class objClass, Reader reader, Mapping mapping)
    throws Exception
  {
    Object deser = null;

    try
    {
      Unmarshaller unmarshaller = new Unmarshaller(mapping);
      unmarshaller.setIgnoreExtraAttributes(true); //NSL20050930
      unmarshaller.setIgnoreExtraElements(true); //NSL20050930
      unmarshaller.setWhitespacePreserve(true); //NSL20060217
      deser = unmarshaller.unmarshal(reader);
    }
    catch (Exception ex)
    {
      Log.warn(Log.DB, "[XmlObjectDeserializer.deserialize]", ex);
      throw new Exception("Unable to deserialize object from xml");
    }
    finally
    {
      try
      {
        reader.close();
      }
      catch (Exception ex2)
      {
      	Log.warn(Log.DB, "[XmlObjectDeserializer.deserialize] Unable to close reader", ex2);
      }
    }

    return deser;
  }
}