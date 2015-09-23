/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DirReader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 03 2002    Koh Han Sing        Add in support for attachment
 */
package com.gridnode.gtas.backend.sender;

import com.gridnode.gtas.backend.util.Log;

import org.jdom.*;
import org.jdom.input.*;

import java.util.*;
import java.io.*;

public class DirReader
{
  final static private String PARSER    = "org.apache.xerces.parsers.SAXParser";
  final static private String LOCATION_TAG = "Location";
  final static private String NAME_TAG     = "Name";
  final static private String DIR_TAG      = "Directory";
  final static private String INFO_TAG     = "InfoFile";
  final static private String ATT_DIR_TAG  = "AttachmentDir";

  private static final String CATEGORY = "DirReader";

  private String      dirFile = "Mapping.Rule";
  private Hashtable   dirInfo;

  public DirReader()
  {
//    log("DirReader");
  }

  public DirReader(String file)
  {
//    log("DirReader(String)");
    this.dirFile = file;
  }

  public Hashtable readFile()

  {
//    log("readFile");
    try
    {
      SAXBuilder docBuilder = new SAXBuilder(PARSER, false);
      File iniFile = new File(this.dirFile);
      Document doc = docBuilder.build(iniFile);
      Element root = doc.getRootElement();
      return readContent(root);
    }
    catch(NullPointerException e)
    {
      logErr("Null Pointer", e);
    }
    catch(Exception e)
    {
      logErr("Exception in readFile", e);
    }
    return null;
  }


  private Hashtable readContent(Element el)
  {
//    log("Root = " + el.getName());
    Hashtable fileInfo = new Hashtable();
    List locationList = el.getChildren(LOCATION_TAG);
    Iterator locationIterator = locationList.iterator();
    while (locationIterator.hasNext())
    {
      Element location = (Element)locationIterator.next();
//      log("Name = " + location.getName());
      String directory = location.getChildText(DIR_TAG);
      String attachmentDir = location.getChildText(ATT_DIR_TAG);
      String infoFile = location.getChildText(INFO_TAG);
//      log("Dir = " + directory + " Info = " + infoFile);
      ArrayList values = new ArrayList();
      values.add(attachmentDir);
      values.add(infoFile);
      fileInfo.put(directory, values);
    }
    return fileInfo;
  }


  private void logErr(String msg, Exception e)
  {
    Log.err(CATEGORY, msg, e);
//    System.out.println(msg);
  }

  private void log(String msg)
  {
    Log.log(CATEGORY, msg);
//    System.out.println(msg);
  }
}
