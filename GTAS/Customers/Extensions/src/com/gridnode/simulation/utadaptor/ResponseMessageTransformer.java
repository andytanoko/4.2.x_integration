/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseMessageTransformer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 15 2004    Neo Sok Lay         Created
 */
package com.gridnode.simulation.utadaptor;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileReader;
import java.io.StringWriter;

/**
 * Transformer of Message to ResponseMessage.
 *
 * @version GT 2.3 I1
 * @since GT 2.3 I1
 */
public class ResponseMessageTransformer
{
  private String _styleSheet;
  private TransformerFactory _factory;
  private Templates _templates;

  /**
   * Construct a ResponseMessageTransformer using the specified stylesheet file
   *
   * @param styleSheet Filename of the stylesheet file to use for transformation
   * @throws java.lang.Exception Error compiling the stylesheet.
   */
  public ResponseMessageTransformer(String styleSheet) throws Exception
  {
    _styleSheet = styleSheet;
    init();
  }

  /**
   * Initialize by compiling the stylesheet file for transformation
   *
   * @throws java.lang.Exception Unable to compile the stylesheet file
   */
  private void init() throws Exception
  {
    _factory = TransformerFactory.newInstance();
    _templates = _factory.newTemplates(new StreamSource(_styleSheet));

  }

  /**
   * Get the Response message for the specified message (in XML)
   *
   * @param message The XML message that will be transformed to obtain the
   * response message
   * @return The response message transformed from <code>message</code>
   * @throws java.lang.Exception Error performing the transformation.
   */
  public String getResponseMessage(String messageFile) throws Exception
  {
    StreamSource inputSource = new StreamSource(new FileReader(messageFile));
    Transformer transformer = _templates.newTransformer();

    StringWriter results = new StringWriter();
    _factory.setURIResolver(_factory.getURIResolver());
    transformer.transform(inputSource, new StreamResult(results));
    results.close();
    return results.toString();
  }
}