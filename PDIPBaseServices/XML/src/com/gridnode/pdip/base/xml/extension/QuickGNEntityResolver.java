/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: QuickGNEntityResolver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.xml.extension;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class QuickGNEntityResolver implements EntityResolver
{
  //static private String CLASSNAME = "GNEntityResolver";
  static private String FILE = "file:///";
  private String alternateSystemID;
  private String filename;
  //private String loadedFile;

  public QuickGNEntityResolver()
  {
    super();
  }

  public QuickGNEntityResolver(String systemID)
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
    File tempFile = new File(filePath);
    if(tempFile.isDirectory())
    {
      filePath = filePath + File.separator + this.filename;
    }
    File file = new File(filePath);
    if(file.exists())
    {
      inputSource = new InputSource(new FileInputStream(file));
      //loadedFile = filePath;
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