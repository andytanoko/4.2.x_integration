/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LayoutTemplate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 * May 24 2002    Jared Low           Implements Serializable.
 */
package com.gridnode.pdip.app.gridform.model;

import com.gridnode.pdip.app.gridform.helpers.GFLog;

import org.jdom.*;
import java.io.Serializable;
import java.util.*;

/**
 * To be refactored. Should be designed similar to the DataDepository.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low.
 */
public class LayoutTemplate implements Serializable
{
  private static final String ATTRIB_FIELDNAME = "name";

  private Document _document;
  private List _namedElements;

  public LayoutTemplate()
  {
  }

  public void setDocument(Document document)
  {
    _document = document;
  }

  public Document getDocument()
  {
    return _document;
  }

  public void parseDocument()
  {
    _namedElements = new ArrayList();
    parseElement(_document.getRootElement(), _namedElements);
  }

  private void parseElement(Element element, List list)
  {
    Attribute attrib = element.getAttribute(ATTRIB_FIELDNAME);
    if (attrib != null)
    {
      list.add(element);
    }

    Iterator iterator = element.getChildren().iterator();
    while (iterator.hasNext())
    {
      Element child = (Element)iterator.next();
      parseElement(child, list);
    }
  }

  /**
   * Set value into the element as identified by the field name. Call <code>
   * parseDocument</code> first before invoking this method.
   *
   * @param field The field name.
   * @param value The value to be set into the field.
   * @since 2.0
   */
  public void setValue(String field, String value)
  {
    GFLog.log("[LayoutTemplate.setValue] Setting field: " + field + " with: " + value);
    if (_namedElements != null)
    {
      Iterator iterator = _namedElements.iterator();
      while (iterator.hasNext())
      {
        Element element = (Element)iterator.next();
        String name = element.getAttributeValue(ATTRIB_FIELDNAME);
        if (name != null && name.equals(field))
        {
          GFLog.log("[LayoutTemplate.setValue] Field: " + field + " found");
          setElementValue(element, value);
        }
      }
    }
  }

  /**
   * Set value into the element based on the element name.
   *
   * @param field The element.
   * @param value The value to be set into the field.
   * @since 2.0
   */
  private void setElementValue(Element element, String value)
  {
    if (element != null)
    {
      String name = element.getName();
      if (name.equals("a"))
      {
        GFLog.log("[LayoutTemplate.setElementValue] Setting value for <a> element");
        element.setText(value);
      }
      else if (name.equals("input"))
      {
        GFLog.log("[LayoutTemplate.setElementValue] Setting value for <input> element");
        element.setAttribute("value", value);
      }
    }
  }
}