/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LayoutTemplateReader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 * Jun 10 2002    Daniel D'Cotta      Added functionality to return W3C Document
 */
package com.gridnode.pdip.app.gridform.xml;

import com.gridnode.pdip.app.gridform.model.LayoutTemplate;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import javax.xml.parsers.*;
import java.io.*;

/**
 * Reader for parsing the layout template file. The template must be in XHTML
 * format, thus a well formed XML document.
 *
 * Scope is package level. Use <code>XMLFacade</code> instead.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
class LayoutTemplateReader
{
  private static final String PARSER = "org.apache.xerces.parsers.SAXParser";

  public LayoutTemplateReader()
  {
  }

  public LayoutTemplate loadTemplate(File file) throws JDOMException, IOException
  {
    SAXBuilder b = new SAXBuilder(PARSER);
    Document doc = b.build(file);

    LayoutTemplate template = new LayoutTemplate();
    template.setDocument(doc);
    template.parseDocument();

    return template;
  }

  public String generateString(LayoutTemplate template) throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    XMLOutputter outputter = new XMLOutputter();
    outputter.output(template.getDocument().getRootElement(), baos);
    return baos.toString();
  }

  // 20020610 DDJ: Added method
  public org.w3c.dom.Document generateW3CDocument(LayoutTemplate template) throws org.jdom.JDOMException
  {
    org.jdom.Document jdomDoc = template.getDocument();
    org.w3c.dom.Document w3cDoc = convertToDOM(jdomDoc);
    return w3cDoc;
  }

  // 20020610 DDJ: Added method
  protected org.w3c.dom.Document convertToDOM(org.jdom.Document jdomDoc) throws JDOMException
  {
    DOMOutputter outputter = new DOMOutputter();
    return outputter.output(jdomDoc);
  }
}