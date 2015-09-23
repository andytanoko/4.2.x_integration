/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDataDepositoryReader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.xml;

import javax.xml.parsers.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import java.io.File;
import java.io.IOException;

/**
 * Reader for parsing loads a xml file into a <code>XMLDataDepository</code>
 * class.
 *
 * Scope is package level. Use <code>XMLFacade</code> instead.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
class XMLDataDepositoryReader
{
  private static final String PARSER = "org.apache.xerces.parsers.SAXParser";

  public XMLDataDepositoryReader()
  {
  }

  public XMLDataDepository loadDepository(File file) throws JDOMException, IOException
  {
    SAXBuilder b = new SAXBuilder(PARSER);
    Document doc = b.build(file);

    XMLDataDepository depository = new XMLDataDepository();
    depository.setDocument(doc);

    return depository;
  }
}