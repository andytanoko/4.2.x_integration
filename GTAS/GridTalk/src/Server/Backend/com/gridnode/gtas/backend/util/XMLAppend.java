/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLAppend.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 * Sep 29 2005    Neo Sok Lay         change of JDOM XMLOutputter syntax
 */
package com.gridnode.gtas.backend.util;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Iterator;

public class XMLAppend
{
  final static private String PARSER = "org.apache.xerces.parsers.SAXParser";

  public static void appendXML(File original, File newFile)
  {
    SAXBuilder docBuilder = new SAXBuilder(PARSER, false);
    try
    {
      Document orgDoc = docBuilder.build(original);
      Element orgRoot = orgDoc.getRootElement();
      Document newDoc = docBuilder.build(newFile);
      Element newRoot = newDoc.getRootElement();
      List newChildren = newRoot.getChildren();
      Iterator cIterator = newChildren.iterator();
      while(cIterator.hasNext())
      {
        Element newChild = (Element)cIterator.next();
        Element temp = (Element)newChild.clone();
        orgRoot.addContent(temp);
      }
      FileOutputStream fos = new FileOutputStream(original);
      XMLOutputter xmlout = new XMLOutputter(Format.getPrettyFormat());
      //xmlout.setNewlines(true);
      //xmlout.setIndent(true);
      xmlout.output(orgDoc, fos);
      fos.flush();
      fos.close();
    }
    catch(Exception e)
    {
      e.printStackTrace(System.out);
    }
  }
}
