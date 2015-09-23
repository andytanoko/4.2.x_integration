/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 24 2003    Koh Han Sing        Modified to use XML Facade and not
 *                                    directly call into XMLDocumentUtility
 * Jan 29 2004    Neo Sok Lay         Pass null to the suffix for File.createTempFile()
 *                                    to use the default temp file extension ".tmp".
 * Apr 29 2004    Neo Sok Lay         Add getRoot() with DTD.
 * Oct 18 2005    Lim Soon Hsiung     Add writeXmlFile() to write XML content (from mail part)
 *                                    to XML file.
 * Noc 09 2005    Lim Soon Hsiung     Add getXmlString() and getXmlEncoding()
 * Apr 19 2006    Neo Sok Lay         GNDB00026989: Change the max doc header length to 100.
 */
package com.gridnode.pdip.base.rnif.helper;

import com.gridnode.pdip.base.security.mime.IPart;
import com.gridnode.pdip.base.xml.exceptions.XMLException;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.base.xml.helpers.XMLServiceHandler;
//import com.gridnode.pdip.base.xml.helpers.XMLDocumentUtility;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

//import org.jdom.Element;
//import org.jdom.JDOMException;
import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNNamespace;
import com.gridnode.xml.adapters.GNXMLDocumentUtility;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringTokenizer;

public class XMLUtil
{

  static public int MAX_ERROR= 10;
  public static IXMLServiceLocalObj getXMLServiceMgr() throws ServiceLookupException
  {
    return (IXMLServiceLocalObj) ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getObj(
      IXMLServiceLocalHome.class.getName(),
      IXMLServiceLocalHome.class,
      new Object[0]);
  }

  /**
    * Private method to create a XML element.
    */
  static String formatElement(String elementname, String value)
  {
    StringBuffer buf= new StringBuffer();
    buf.append('<');
    buf.append(elementname);
    buf.append('>');
    buf.append(XMLUtil.normalize(value));
    buf.append("</");
    buf.append(elementname);
    buf.append('>');
    return buf.toString();
  }

  /** Normalizes the given string. */
  static String normalize(String s)
  {
    boolean canonical= false;
    StringBuffer str= new StringBuffer();

    int len= (s != null) ? s.length() : 0;
    for (int i= 0; i < len; i++)
    {
      char ch= s.charAt(i);
      switch (ch)
      {
        case '<' :
          {
            str.append("&lt;");
            break;
          }
        case '>' :
          {
            str.append("&gt;");
            break;
          }
        case '&' :
          {
            str.append("&amp;");
            break;
          }
        case '"' :
          {
            str.append("&quot;");
            break;
          }
        case '\r' :
        case '\n' :
          {
            if (canonical)
            {
              str.append("&#");
              str.append(Integer.toString(ch));
              str.append(';');
              break;
            }
            // else, default append char
          }
        default :
          {
            str.append(ch);
          }
      }
    }

    return (str.toString());

  } // normalize(String):String

  static public String padInteger(Integer num)
  {
    if (num == null)
      return null;
    String ret= num.toString();
    StringBuffer sb= new StringBuffer(ret);
    for (int i= 0; i < (9 - ret.length()); i++)
      sb.insert(0, '0');
    return (sb.toString());
  }

  static public File createTempFile(String fileCat, String content) throws IOException
  {
    //    String tempPath = FileUtil.getDomain()+"/"+
    //                      FileUtil.getPath(IRnifPathConfig.PATH_TEMP);
    //    String tempFilename = FileUtil.getUniqueFilename(IRnifPathConfig.PATH_TEMP,fileCat);
    //    File tempFile = new File(tempPath, tempFilename);
    File tempFile= File.createTempFile(fileCat, null);
    FileWriter writer= new FileWriter(tempFile);
    writer.write(content);
    writer.close();

    // LSH:09Nov05
//    FileOutputStream writer= new FileOutputStream(tempFile);
//    writer.write(content.getBytes(getXmlEncoding(content)));  // Must use the correct encoding specified by the XML content
//    writer.close();

    tempFile.deleteOnExit();
    return tempFile;
  }

 static public File createTempFile(String fileCat, byte[] content) throws IOException
 {
    File tempFile= File.createTempFile(FileUtil.removeExtension(fileCat), null);
    FileOutputStream writer= new FileOutputStream(tempFile);
    writer.write(content);
    writer.close();
    tempFile.deleteOnExit();
    return tempFile;
 }

  static public String createFile(String fileCat, String fileName, File filecontent)
    throws FileNotFoundException, FileAccessException
  {
    InputStream inputstream= new FileInputStream(filecontent);

    String resfileName= FileUtil.create(fileCat, fileName, inputstream);

    return resfileName;
  }

  public static String extractDTDName(String content)
    throws XMLException, ServiceLookupException
  {
    //String dtd= XMLDocumentUtility.extractDTDFilename(content);
    String dtd = getXMLServiceMgr().extractDTDFilename(content);
    return dtd;
  }

  static public void validateDTD(String contentFileName, String dtdpath, List errlist)
  {
    try
    {
      getXMLServiceMgr().validateDTD(contentFileName, dtdpath, dtdpath);
    }
    catch (Throwable ex)
    {
      Logger.warn("error in validatae validate DTD: contentFileName=" + contentFileName +
       ";dtdpath=" + dtdpath, ex);
      String errMsg= ex.getMessage();
      errlist.add(errMsg);
    }
  }

  static public void validateDictionary(
    String contentFileName,
    String dictpath,
    String dtdpath,
    List errlist)
  {
    List dicErrList= null;
    try
    {
      dicErrList= getXMLServiceMgr().validate(contentFileName, dictpath, dtdpath, MAX_ERROR);
    }
    catch (Throwable ex)
    {
      Logger.warn("error in validatae dictionary: contentFileName=" + contentFileName +
       ";dictpath="+ dictpath+ ";dtdpath=" + dtdpath, ex);
      String errMsg= ex.getMessage();
      errlist.add(errMsg);
    }
    if (dicErrList != null && !dicErrList.isEmpty())
      errlist.addAll(dicErrList);
  }

  static public void validateSchema(
    String contentFileName,
    String schemapath,
    String dtdpath,
    List errlist)
  {
//@@todo validateSchema
    try
    {
      getXMLServiceMgr().validateSchema(contentFileName, schemapath, dtdpath);
    }
    catch (Throwable ex)
    {
      Logger.warn("error in validatae validate Schema: contentFileName=" + contentFileName +
       ";schemapath="+ schemapath, ex);
      String errMsg= ex.getMessage();
      errlist.add(errMsg);
    }
  }

  static public GNElement getRoot(File xmlfile)
    throws FileNotFoundException, FileAccessException,
           XMLException, ServiceLookupException

  {

    GNElement root;
//    root=
//      XMLDocumentUtility.getRoot(
//        xmlfile,
//        FileUtil.getDomain() + "/" + FileUtil.getPath(IRnifPathConfig.PATH_DTD));
    root=
      getXMLServiceMgr().getRoot(xmlfile);
//        FileUtil.getDomain() + "/" + FileUtil.getPath(IRnifPathConfig.PATH_DTD));
    return root;
  }

  public static GNElement getRoot(File xmlFile, String dtdName)
    throws FileAccessException, XMLException
  {
    String dtdPath = FileUtil.getFile(IRnifPathConfig.PATH_DTD, dtdName).getAbsolutePath();
    return XMLServiceHandler.getInstance().getRoot(xmlFile, dtdPath);
  }

  static public  String extractXPathValue(String filefullPath, String xpath) throws XMLException, ServiceLookupException
  {
    List res = getXMLServiceMgr().getXPathValues(filefullPath, xpath);
    if(res == null || res.isEmpty())
      return null;
    return (String)res.get(0);
  }

  static public GNNamespace getNamespace(String prefix, String uri)
  {
    try
    {
      return getXMLServiceMgr().newNamespace(prefix, uri);
    }
    catch (Exception ex)
    {
      Logger.warn("error in getting namespace =" + prefix +
       ":" + uri, ex);
      return null;
    }
  }

  /**
   * Extract out the XML content from a mail part and write it to a file.
   * This method will invoke the <code>writeXmlFile(IPart part, String outputFile, boolean indent, boolean newline)</code>
   * where <code>indent</code> is defaulted to <code>true</code> and <code>newline</code>
   * is defaulted to <code>false</code>.
   *
   * @param part the mail part that contains the XML document
   * @param outputFile the utput filename
   * @throws Exception
   */
  static public void writeXmlFile(IPart part, String outputFile) throws Exception
  {
    writeXmlFile(part, outputFile, true, false);
  }

  /**
   * Extract out the XML content from a mail part and write it to a file.
   *
   * @param part the mail part that contains the XML document
   * @param outputFile the utput filename
   * @param indent boolean
   * @param newline boolean
   * @throws Exception
   */
  static public void writeXmlFile(IPart part, String outputFile, boolean indent, boolean newline) throws Exception
  {
    InputStream inputStream = (InputStream)part.getContent(IPart.OUTPUT_INPUTSTREAM);
    byte[] buffer = new byte[inputStream.available()];
    int realSize = inputStream.read(buffer);

    byte[] newBuf = new byte[realSize];
    System.arraycopy(buffer, 0, newBuf, 0, realSize);
    com.gridnode.xml.adapters.GNDocument gdoc =
      GNXMLDocumentUtility.getDocument(new ByteArrayInputStream(newBuf));

    GNXMLDocumentUtility.writeToFile(gdoc, outputFile, indent, newline);
  }

  static public String getXmlString(byte[] data) {
    return getXmlString(data, getXmlEncoding(new String(data)));
  }

  static public String getXmlString(byte[] data, String encoding) {
    String rv = null;

    try {
      rv = new String(data, encoding);
    }
    catch (Throwable ex) {
      Logger.warn("Unable to decode XML data with encoding [" + encoding + "]", ex);
      rv = new String(data);
    }

    return rv;
  }

  static public String getXmlEncoding(byte[] xmlContent) {
    //int length = (xmlContent.length>40) ? 40 : xmlContent.length;
  	int length = (xmlContent.length>100) ? 100 : xmlContent.length;
    String str = new String(xmlContent, 0, length);

    return getXmlEncoding(str);
  }

  static public String getXmlEncoding(String xmlContent) {
    String encoding = null; //"UTF-8";  // defaul XML encoding
    int beginIndex = xmlContent.indexOf("<?xml");
    int endIndex = xmlContent.indexOf("?>");

    if(beginIndex == -1)
    {
      // Not a XML document, return system encoding
      return System.getProperty("file.encoding");
    }

    String hder = xmlContent.substring(beginIndex + 2, endIndex);
//    System.out.println("Header [" + hder + "]");
    StringTokenizer hdr = new StringTokenizer(hder, " ");
//    System.out.println("Start checking for encoding string");
    while (hdr.hasMoreTokens()) {
      String token = hdr.nextToken();
//      System.out.println("Token [" + token + "]");
      if (token.toLowerCase().startsWith("encoding")) {
        int index = token.indexOf('=');
        if (index != -1) {
          // Happy scenario e.g. [encoding="UTF-8"]
          if (token.length() > index + 1) {
            String enc = token.substring(index + 1);
            if (enc.startsWith("\"") || enc.startsWith("'")) {
              enc = enc.substring(1);
            }
            if (enc.endsWith("\"") || enc.endsWith("'")) {
              enc = enc.substring(0, enc.length() - 1);
            }

            if (enc.length() != 0) {
              encoding = enc;
            }
          }
        }
        else {
          token = hdr.nextToken();
          if (token.startsWith("=")) {
            if (token.length() > 1) {
              // Unhappy scenario 1 e.g. [encoding ="UTF-8"]
              String enc = token.substring(1);
              if (enc.startsWith("\"") || enc.startsWith("'")) {
                enc = enc.substring(1);
              }
              if (enc.endsWith("\"") || enc.endsWith("'")) {
                enc = enc.substring(0, enc.length() - 1);
              }

              if (enc.length() != 0) {
                encoding = enc;
              }

            }
            else {
              // Unhappy scenario 2 e.g. [encoding = "UTF-8"]
              token = hdr.nextToken();
              String enc = token;
              if (enc.startsWith("\"") || enc.startsWith("'")) {
                enc = enc.substring(1);
              }
              if (enc.endsWith("\"") || enc.endsWith("'")) {
                enc = enc.substring(0, enc.length() - 1);
              }

              if (enc.length() != 0) {
                encoding = enc;
              }
            }
          }
        }

        break;
      }
    }
//    System.out.println("Encoding = [" + encoding + "]");

    // UTF-8 is defaulf XML encoding
    return (encoding == null) ? "UTF-8" : encoding;
  }
}
