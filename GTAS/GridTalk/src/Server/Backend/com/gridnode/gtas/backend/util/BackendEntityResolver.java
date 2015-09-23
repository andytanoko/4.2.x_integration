/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNDTDValidator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 23 2003    Wang Khong Hai      Changed to use new logging framework.
 */
package com.gridnode.gtas.backend.util;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BackendEntityResolver implements EntityResolver
{
  static private String CLASSNAME = "BackendEntityResolver";
  static private String FILE = "file:///";
  private String alternateSystemID;
  private String filename;

  public BackendEntityResolver()
  {
    super();
  }

  public BackendEntityResolver(String systemID)
  {
    super();
    this.alternateSystemID = correctFileSeparator(systemID);
  }

  public InputSource resolveEntity(String publicId, String systemId)
  throws FileNotFoundException
  {
    String temp = correctFileSeparator(systemId);
    int index = temp.lastIndexOf(File.separator);
    if(index != -1 && index != temp.length() - 1)
    {
      this.filename = temp.substring(index + 1);
    }
    InputSource inputSource = null;
    if(systemId != null)
    {
      inputSource = loadInputSource(systemId);
    }
    //System ID inputsource not loaded
    if(inputSource == null)
    {
      if(this.alternateSystemID != null)
      {
        inputSource = loadInputSource(this.alternateSystemID);
      }
    }
    //Alternate System ID inputsource not loaded
    if(inputSource == null)
    {
      inputSource = new InputSource(new StringReader(""));
    }
    return inputSource;
  }

  private InputSource loadInputSource(String systemId)
  throws FileNotFoundException
  {
    InputSource inputSource = null;
    String filePath = systemId;
    if(filePath.startsWith(FILE))
    {
      filePath = systemId.substring(FILE.length());
    }
    String temp = filePath.toLowerCase();
    File tempFile = new File(temp);
    if(tempFile.isDirectory())
    {
      filePath = filePath + File.separator + this.filename;
    }
    File file = new File(filePath);
    if(file.exists())
    {
      inputSource = new InputSource(new FileInputStream(file));
    }
    return inputSource;
  }

  private String correctFileSeparator(String path)
  {
    String newPath;
    if(File.separator.equals("/"))
    {
      newPath = path.replace('\\', '/');
    }
    else
    {
      newPath = path.replace('/', '\\');
    }
    return newPath;
  }
}