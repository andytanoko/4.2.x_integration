/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ByteArrayCompresser.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 19, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ByteArrayCompresser
{
  
  public static byte[] zipFile(byte[] fileArray, String filename) throws Exception
  {
    ByteArrayInputStream in = new ByteArrayInputStream(fileArray);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ZipOutputStream zipOut = new ZipOutputStream(out);
    
    try
    {
      if(filename == null)
      {
        filename = "byteArray"; //put a dummy name
      }
      ZipEntry entry = new ZipEntry(filename);
      zipOut.putNextEntry(entry);
    
      int readSoFar = 0;
      byte[] buffer = new byte[512];
      while( (readSoFar = in.read(buffer) ) != -1)
      {
        zipOut.write(buffer, 0, readSoFar);
      }
      
      zipOut.closeEntry();
      return out.toByteArray();
    }
    catch(Exception ex)
    {
      throw ex;
    }
    finally
    {
      if(in != null)
      {
        in.close();
      }
      if(zipOut != null)
      {
        zipOut.close();
      }
      if(out != null)
      {
        out.close();
      }
    }
  }
}
