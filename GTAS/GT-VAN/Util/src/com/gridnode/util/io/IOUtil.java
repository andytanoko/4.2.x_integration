/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IOUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 9, 2006    i00107              Created
 */

package com.gridnode.util.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author i00107
 * This class handles the common IO access.
 */
public class IOUtil
{

  public static byte[] read(InputStream is) throws IOException
  {
    return IOUtils.toByteArray(is);
  }
  
  /**
   * Writes a byte array to a file creating the file if it does not exist.
   * 
   * @param f The file to write to
   * @param content The content to write to file
   * @throws IOException Thrown if encountered I/O error.
   */
  public static void write(File f, byte[] content) throws IOException
  {
    FileUtils.writeByteArrayToFile(f, content);
  }
  
  /**
   * Writes a String to a file creating the file if it does not exist.
   * 
   * @param f The file to write to
   * @param line The content to write to file
   * @throws IOException
   */
  public static void write(File f, String line) throws IOException
  {
    FileUtils.writeStringToFile(f, line, null);
  }
}
