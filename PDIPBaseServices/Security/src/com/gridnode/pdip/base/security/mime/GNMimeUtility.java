/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNMimeUtility.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 16 Aug  2001    Lim Soon Hsiung     Initial creation for GT 1.1
 * 17 June 2002    Jagadeesh           GTAS2.0 - Included to throw SecurityServicesException.
 * 09 Dec  2002    Jagadeesh           Added - Methods from GT 1.3.8(MIME Security).
 * Mar 12 2007    Neo Sok Lay         Use UUID for unique filename.
 */

package com.gridnode.pdip.base.security.mime;

import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.framework.util.UUIDUtil;


import javax.mail.internet.*;
import javax.mail.Session;
import javax.activation.FileDataSource;
import java.io.*;

/**
 * @author Lim Soon Hsiung
 * @version 1.1
 */

public class GNMimeUtility
{
  public static final String GENERIC_CONTENT_TYPE = "application/octet-stream";
  static final String[][] CONTENT_TYPE = {
    {GENERIC_CONTENT_TYPE,            "exe"},
    {"application/xml",               "xml"},
    {"text/plain",                    "text" , "c", "cc", "cpp","c++", "h", "pl", "txt", "java", "el"},
    {"application/msword",            "doc"},
    {"application/vnd.ms-excel",      "xls", "xlb"},
    {"application/vnd.ms-powerpoint", "ppt", "pps"},
    {"application/zip",               "zip"},
    {"application/pdf",               "pdf"},
    {"image/jpeg",                    "jfif", "jfif-tbnl", "jpe", "jpg", "jpeg"},
    {"image/gif",                     "gif"},
    {"text/html",                     "htm", "html"},
    {"message/rfc822",                "mime", "eml"},
    {"video/x-sgi-movie",             "movie", "mv"},
    {"application/x-troff-msvideo",   "avi"},
    {"video/quicktime",               "mov", "qt"},
    {"video/mpeg",                    "mpg", "mpeg", "mpe"},
    {"text/x-setext",                 "etc"},
    {"text/tab-separated-values",     "tsv"},
    {"image/png",                     "png"},
    {"image/x-xwindowdump",           "xwd"},
    {"image/x-xbitmap",               "xbm", "xpm"},
    {"image/tiff",                    "tif", "tiff"},
    {"audio/x-wav",                   "wav"},
    {"audio/basic",                   "snd", "au"},
    {"application/x-tar",             "tar"},
    {"application/x-gtar",            "gtar"},
    {"application/rtf",               "rtf"},
    {"application/postscript",        "eps", "ai", "ps"},
//    {"", ""},
//    {"", ""},
//    {"", ""},
//    {"", ""},
//    {"", ""},
//    {"", ""},

  };


//  private static final ExceptionValue exV = new ExceptionValue("GNMimeUtility Exception");

  private GNMimeUtility()
  {
  }

  public static void main(String[] args)
  {
    GNMimeUtility GNMimeUtility1 = new GNMimeUtility();
  }

  public static Object convertContent(Object content, int outputType) throws SecurityServiceException
  {
    if(outputType == IMailpart.OUTPUT_RAW)
      return content;
    InputStream in = null;
    int inLen = -1;
    Object rv = null;
    byte[] b = null;
  try
  {
    if (content instanceof String)
    {
      System.out.println("[convertContent] content instanceof String");
      if(outputType == IMailpart.OUTPUT_STRING)
        return content;

      b = ((String)content).getBytes();
      in = new ByteArrayInputStream(b);
      inLen = b.length;
    }
    
    else if (content instanceof MimeMultipart)
    {
      System.out.println("[convertContent] content instanceof MimeMultipart");
      GNMimepart mp = new GNMimepart((MimeMultipart)content);
      b = mp.getContentByte();
      if(outputType == IMailpart.OUTPUT_BYTE_ARRAY)
        return b;

      in = new ByteArrayInputStream(b);
      inLen = b.length;
    }
    else if (content instanceof InputStream)
    {
      System.out.println("[convertContent] content instanceof InputStream");
      if(outputType == IMailpart.OUTPUT_INPUTSTREAM)
        return content;

      in = (InputStream)content;
      inLen = in.available();
    }
    else if (content instanceof byte[])
    {
      System.out.println("[convertContent] content instanceof bytearray");
      if (outputType == IMailpart.OUTPUT_BYTE_ARRAY)
        return content;
      
      b = (byte[])content;  
      in = new ByteArrayInputStream(b);
      inLen = b.length;  
    }
    else
      throw new SecurityServiceException("Unsupported object for conversion"); 

    int readLen = -1;
    switch (outputType)
    {
      case IMailpart.OUTPUT_BYTE_ARRAY:
//        b = new byte[in.available()];
        b = new byte[inLen];
        readLen = in.read(b);
        if(readLen < inLen)
        {
          b = shorten(b , readLen);
        }
        rv = b;
        break;
  			
      case IMailpart.OUTPUT_FILE:
        break;

      case IMailpart.OUTPUT_INPUTSTREAM:
        rv = in;
        break;

      case IMailpart.OUTPUT_RAW:
        break;

      case IMailpart.OUTPUT_STRING:
//        b = new byte[in.available()];
        b = new byte[inLen];
        readLen = in.read(b);
        if(readLen < inLen)
        {
          b = shorten(b , readLen);
        }
        String str = new String(b);
        rv = str;
        break; 
      default:
        throw new SecurityServiceException("Unsupported output type");
//        GNException.throwEx(exV, "Unsupported output type");
       // break;
    }
    in.close();

}
catch (Exception ex) {
  ex.printStackTrace();
}
    return rv;

  }

  public static IMailpart generatePart(byte[] partData) throws SecurityServiceException
  {
    GNMimepart mp = null;
    try
    {

    File file = getFileFromByteArray(partData);
/*
    FileDataSource fds = new FileDataSource(file);
    MimeMultipart mmp = new MimeMultipart(fds);
    mp = new GNMimepart(mmp);
*/
    mp = new GNMimepart(file);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to create part",ex);
//      GNException.throwEx(exV, "Unable to create part", ex);
    }

    return mp;
  }

  public static File getFileFromByteArray(byte[] data) throws SecurityServiceException
  {

    File file = null;
    try
    {
      file = File.createTempFile("~gnmime"+UUIDUtil.getRandomUUIDInStr(), null);
      file.deleteOnExit();
      FileOutputStream fo = new FileOutputStream(file);
      fo.write(data);
      fo.close();
    }
    catch (Exception ex)
    {
        throw new SecurityServiceException("File generation error",ex);
    //      GNException.throwEx(exV, "File generation error", ex);
    }
    return file;
  }

  public static String extensionforFile(File file)
  {
    String filename = file.getName();
    int index = filename.lastIndexOf(".");
    String ext = (index == -1)? "" : filename.substring(index + 1);
//System.out.println("[GNMimeUtility.extensionforFile] extension for file: " + file + " is " + ext);
    return ext;
  }

  public static String findContentTypeForXtension(File file)
  {
    return findContentTypeForXtension(extensionforFile(file));
  }

  public static String findContentTypeForXtension(String extension)
  {
//    String contentType = null;

    for (int i = 0; i < CONTENT_TYPE.length; i++)
    {
      for (int j = 1; j < CONTENT_TYPE[i].length; j++)
      {
        if(CONTENT_TYPE[i][j].equalsIgnoreCase(extension))
        {
//          System.out.println("[GNMimeUtility.extensionforFile] Content-type for extension " + extension + " is " + CONTENT_TYPE[i][0]);
          return CONTENT_TYPE[i][0];
        }
      }
    }

    return GENERIC_CONTENT_TYPE;
  }

  public static String getUniqueMessageIDValue()
  {
    return getUniqueMessageIDValue(null);
  }

  public static String getUniqueMessageIDValue(Session session)
  {
    Object object = null;
    InternetAddress internetaddress
        = InternetAddress.getLocalAddress(session);
    String string;
    if (internetaddress != null)
        string = internetaddress.getAddress();
    else
        string = "javamailuser@localhost";
    StringBuffer stringbuffer = new StringBuffer();
    stringbuffer.append(stringbuffer.hashCode()).append('.').append
        (System.currentTimeMillis()).append
        ('.').append
        ("GridNode.").append(string);
    return stringbuffer.toString();
  }

  private static byte[] shorten(byte[] data, int len)
  {
    byte[] rv = new byte[len];

    System.arraycopy(data, 0, rv, 0, len);

    return rv;
  }
  
  public static byte[] getBytes(InputStream inputstream) throws IOException
  {
    int bufferSize = 1024;
    byte[] data;
    if (inputstream instanceof ByteArrayInputStream)
    {
      bufferSize = inputstream.available();
      data = new byte[bufferSize];
      inputstream.read(data, 0, bufferSize);
    }
    else
    {
      ByteArrayOutputStream bytearrayoutputstream
          = new ByteArrayOutputStream();
      data = new byte[bufferSize];
      int b;
      while ((b = inputstream.read(data, 0, bufferSize)) != -1)
          bytearrayoutputstream.write(data, 0, b);
      data = bytearrayoutputstream.toByteArray();
    }
    return data;
  }

}