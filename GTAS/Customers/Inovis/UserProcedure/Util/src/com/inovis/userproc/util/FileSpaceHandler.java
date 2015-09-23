/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileSpaceHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 14, 2010   Tam Wei Xiang       Created
 * Jan 03, 2011   Tam Wei Xiang       Allow the user to configure the UP to instantiate
 *                                    particular concrete TransformerFactory class.
 */
package com.inovis.userproc.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.gridnode.util.SystemUtil;

/**
 * The utility to remove the blank line in a file containing character.
 * 
 * @author Tam Wei Xiang
 * @since GT4.1.4.3
 */
public class FileSpaceHandler
{
//  public void handleBlankLine(String filePath) throws Exception
//  {
//    File file = new File(filePath);
//    if(! file.exists() )
//    {
//      throw new IllegalArgumentException("File space handling failed, the given file="+filePath+" is not exists.");
//    }
//    if(file.isDirectory())
//    {
//      throw new IllegalArgumentException("File space handling failed, the given file is a directory!");
//    }
//    
//    File outFile = new File(getTempDirPath()+generateRandomFilename());
//    FileReader reader = null;
//    BufferedReader buffReader = null;
//    FileWriter writer = null;
//    BufferedWriter buffWriter = null;
//    
//    try
//    {
//      reader = new FileReader(file); 
//      buffReader = new BufferedReader(reader);
//      
//      
//      writer = new FileWriter(outFile);
//      buffWriter = new BufferedWriter(writer);
//      
//      String s = null;
//      while( (s = buffReader.readLine()) != null)
//      {
//        if(! s.trim().equals(""))
//        {
//          buffWriter.write(s);
//          buffWriter.newLine();
//        }
//      }
//    } 
//    catch(Exception ex)
//    {
//      throw new Exception("File space handling failed, error in removing blankline from file="+filePath, ex);
//    } 
//    finally
//    {
//      if(buffWriter != null)
//      {
//        buffWriter.close();
//      }
//      
//      if(buffReader != null)
//      {
//        buffReader.close();
//      }
//    }
//    
//    file.delete();
//    
//    boolean isRenameSuccess = renameFile(file, outFile);
//    if(! isRenameSuccess)
//    {
//      throw new Exception("File space handling failed, can not rename from "+outFile.getAbsolutePath()+" to "+file.getAbsolutePath());
//    }
//  }
//
  public void handleBlankLineXml(String filePath, Boolean isIndent, String dtdDir) throws Exception
  {
    handleBlankLineXml(filePath, isIndent, dtdDir, null);
  }
  
  /**
   * Remove the blank line in the xml file. Allow to set the concrete transformer factory implementation.
   * @param filePath The full path to the xml document
   * @param isIndent whether to set the indentation to the output xml
   * @param dtdDir The DTD directory path
   * @param transformerFactoryName The concrete transformer factory full qualify class name. The supported class includes
   *                               i)   com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl
   *                               ii)  com.icl.saxon.TransformerFactoryImpl
   *                               iii) org.apache.xalan.processor.TransformerFactoryImpl. Not recommended to use as it corrupt the xml file containing special character.
   * @throws Exception thrown if there is issue on removing the blank line.
   */
  public void handleBlankLineXml(String filePath, Boolean isIndent, String dtdDir, String transformerFactoryName) throws Exception
  {
    File file = new File(filePath);
    if(! file.exists() )
    {
      throw new IllegalArgumentException("File space handling failed, the given file="+filePath+" is not exists.");
    }
    if(file.isDirectory())
    {
      throw new IllegalArgumentException("File space handling failed, the given file is a directory!");
    }
    
    File outFile = new File(getTempDirPath()+generateRandomFilename());
    
    FileInputStream in = null;
    BufferedInputStream buffIn = null;
    
    
    try
    {
      in = new FileInputStream(file);
      buffIn = new BufferedInputStream(in);
      
      Document xmlFile = parseXmlFile(buffIn, getWorkingDir()+"/"+dtdDir);
      DocumentType docType = xmlFile.getDoctype();
      removeWhitespace(xmlFile.getDocumentElement());
      
      TransformerFactory tFactory = null;
      
      if(transformerFactoryName == null || transformerFactoryName.trim().length() == 0)
      {
        tFactory = TransformerFactory.newInstance();
      }
      else
      {
        
        //System.setProperty("javax.xml.transform.TransformerFactory", transformerName);
        //Note: we do not follow the recommended way to initiate the TransformerFactory instance
        //        option 1: set the System property
        //        option 2: set the META-INF/services/javax.xml.transform.TransformerFactory
        //      The reason is for option 1, it is setting the property in a global scope, which have potentially
        //                                  impacting those classes that depend on concrete TransformerFactory (SAXON impl or XALAN impl).
        //                                  We may run into trouble by setting the TransformerFactory into particular implementation.
        //                    for option 2, there is no effect, the UP still initiate using the org Apache XALAN impl. It seem like
        //                                  the xalan.jar under the jboss/lib/endorsed is loaded first.
        
        Class<? extends TransformerFactory> transFactoryClass = Class.forName(transformerFactoryName).asSubclass(TransformerFactory.class);
        tFactory = transFactoryClass.newInstance();
      }
      
      Transformer serializer = tFactory.newTransformer();
      
      if(isIndent)
      {
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
      }
      
      if(docType != null)
      {
        serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());
      }
      
      serializer.transform(new DOMSource(xmlFile), new StreamResult(outFile));
      
    } 
    catch(Exception ex)
    {
      throw new Exception("File space handling failed, error in removing blankline from file="+filePath, ex);
    } 
    finally
    {
      if(buffIn != null)
      {
        buffIn.close();
      }
      
      if(in != null)
      {
        in.close();
      }
      
      
    }
    
    boolean isRenameSuccess = renameFile(file, outFile);
    if(! isRenameSuccess)
    {
      throw new Exception("File space handling failed, can not rename from "+outFile.getAbsolutePath()+" to "+file.getAbsolutePath());
    }
  }
  
  private String getTempDirPath()
  {
    
    return System.getProperty("java.io.tmpdir");
  }
  
  private String generateRandomFilename()
  {
    return UUID.randomUUID().toString();
  }
  
  private boolean renameFile(File sourceFile, File targetFile)
  {
    return targetFile.renameTo(sourceFile);
  }
  
  /**
   * 
   * @param in
   * @param systemId The location that is storing the dtd
   * @return
   * @throws Exception
   */
  private Document parseXmlFile(BufferedInputStream in, String systemId) throws Exception
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      
      return db.parse(in, systemId);
    } catch(Exception ex)
    {
      throw new Exception("Failed to parse xml file", ex);
    }

  }
  
  //removing the while space, next line item, it will be no formatting
  private static void removeWhitespace(Element parent) {
    NodeList nodes = parent.getChildNodes();
   
    for (int i = nodes.getLength()-1; i >= 0; i--) {
      Node node = nodes.item(i);
      
      if (node instanceof Element) 
      {
        removeWhitespace((Element)node);
      } 
      else if (node instanceof Text) 
      {
        if (((Text)node).getData().trim().equals("")) 
        {
          parent.removeChild(node);
        }
      }
    }
  }
  
  private String getWorkingDir()
  {
    return SystemUtil.getWorkingDirPath();
  }
  
  public static void main(String[] args) throws Exception
  {
    
      String source = "c:/elemicaDoc.xml";
      FileSpaceHandler handler = new FileSpaceHandler();
      //handler.handleBlankLine(source);
      handler.handleBlankLineXml(source, true, "","com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
  }
}


