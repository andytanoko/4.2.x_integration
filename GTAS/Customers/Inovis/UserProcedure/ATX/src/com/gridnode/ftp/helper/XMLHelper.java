/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 5, 2009    Tam Wei Xiang       Created
 */
package com.gridnode.ftp.helper;

import java.io.File;

import com.gridnode.xml.adapters.GNDocument;
import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNXMLDocumentUtility;
import com.inovis.userproc.util.FileUtil;

/**
 * @author Tam Wei Xiang
 * @version GT4.1.X
 * @since
 */
public class XMLHelper
{

  /**
   * Embed the given embeddedTag into the specified udocXMLFile. Currently we supporting embeded one additional tag.
   * 
   * 
   * @param embeddedTag The tag name we are going to add into the given xml file
   * @param embeddedContent The content we will specify into the given embedded tag
   * @param udocXMLFile The udoc file
   * @return Return udocFile with the embedded tag and embedded content.
   * @throws Exception
   */
  public static File embedXMLContent(String embeddedTag, String embeddedContent, File udocXMLFile) throws Exception
  {
    if(udocXMLFile == null || udocXMLFile.isDirectory() || ! udocXMLFile.exists())
    {
      throw new Exception("File is null or is a directory rather than a file or it is not exist !");
    }
    
    GNXMLDocumentUtility util = new GNXMLDocumentUtility();
    GNDocument doc = GNXMLDocumentUtility.getDocument(udocXMLFile);
    GNElement rootEle = doc.getRootElement();
    
    GNElement aiID = GNXMLDocumentUtility.newElement(embeddedTag);
    aiID.setText(embeddedContent);
    
    rootEle.addElement(aiID);
    
    //create to temp file
    File tempFolder = new File(System.getProperty("java.io.tmpdir"));
    
    //replace the temp file if exists
    byte[] dummyContent = (new String("")).getBytes();
    File tempUdocFile = FileUtil.saveFiles(tempFolder, udocXMLFile.getName(), dummyContent);
    
    GNXMLDocumentUtility.writeToFile(doc,tempUdocFile.getAbsolutePath() , true, true);
    
    return tempUdocFile;
  }
}
