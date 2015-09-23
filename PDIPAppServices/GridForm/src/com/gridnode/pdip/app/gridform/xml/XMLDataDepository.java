/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDataDepository.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.xml;

import com.gridnode.pdip.app.gridform.model.*;
import com.gridnode.pdip.app.gridform.exceptions.*;

import org.jdom.*;
import org.jaxen.jdom.JDOMXPath;

/**
 * Implementing class of the DataDepository that represents a XML document as
 * the data source.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
class XMLDataDepository extends DataDepository
{
  private Document _document;

  /**
   * Constructs an instance.
   *
   * @since 2.0
   */
  public XMLDataDepository()
  {
    this(null);
  }

  /**
   * Constructs an instance that wraps around a XML document.
   *
   * @param Document The JDOM representation of an XML document.
   * @since 2.0
   */
  public XMLDataDepository(Document document)
  {
    setDocument(document);
  }

  /**
   * Set the document.
   *
   * @param document The document to set.
   * @since 2.0
   */
  public void setDocument(Document document)
  {
    _document = document;
  }

  /**
   * Get the document.
   *
   * @return The document.
   * @since 2.0
   */
  public Document getDocument()
  {
    return _document;
  }

  /**
   * Get the name of the root element.
   *
   * @return The name of the root element.
   * @since 2.0
   */
  public Object getID()
  {
    try
    {
      return _document.getRootElement().getName();
    }
    catch (Throwable ex)
    {
      return null;
    }
  }

  /**
   * Set the value at the XPath.
   *
   * @param identifier The XPath.
   * @param value The value to be set at the XPath.
   * @since 2.0
   */
  public void setValue(Object identifier, Object value) throws InvalidAccessException
  {
    try
    {
      Object nodeObj = getNode(identifier.toString());
      if (nodeObj instanceof Element)
      {
        ((Element)nodeObj).setText(value.toString());
      }
      else if (nodeObj instanceof Attribute)
      {
        ((Attribute)nodeObj).setValue(value.toString());
      }
    }
    catch (Throwable ex)
    {
      throw new InvalidAccessException("XMLDataDepository.setValue(xpath, value) Error", ex);
    }
  }

  /**
   * Get the value at the XPath.
   *
   * @param identifier The XPath.
   * @return The value at the XPath.
   * @since 2.0
   */
  public Object getValue(Object identifier) throws InvalidAccessException
  {
    Object value = null;
    try
    {
      Object nodeObj = getNode(identifier.toString());
      if (nodeObj instanceof Element)
      {
        value = ((Element)nodeObj).getText();
      }
      else if (nodeObj instanceof Attribute)
      {
        value = ((Attribute)nodeObj).getValue();
      }
    }
    catch (Throwable ex)
    {
      throw new InvalidAccessException("XMLDataDepository.getValue(xpath) Error", ex);
    }
    return value;
  }

  /**
   * Find the node as indicated by the XPath.
   *
   * @param xpath The XPath.
   * @return The node object as pointed by the XPath.
   * @since 2.0
   */
  private Object getNode(String xpath) throws XMLException
  {
    try
    {
      JDOMXPath xpathObj = new JDOMXPath(xpath);
      return xpathObj.selectSingleNode(_document);
    }
    catch (Throwable ex)
    {
      throw new XMLException("XMLDataDepository.getNode(xpath) Error", ex);
    }
  }
}