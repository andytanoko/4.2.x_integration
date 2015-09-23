/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BusinessDocumentHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 * Dec 26, 2006    Tam Wei Xiang       if isRequiredPack is true, we will include 
 *                                     the transactionDoc's filename or generate a 
 *                                     filename depending whether the content is File
 *                                     or byte[].
 */
package com.gridnode.gtas.audit.extraction.helper;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.util.io.IOUtil;

/**
 * It is reponsible to create the BusinessDocument instance
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class BusinessDocumentHelper
{
  private static String CLASS_NAME = "BusinessDocumentHelper";
  private static String DATE_FORMAT = "yyyyMMddHHmmssSSS";
  
  /**
   * Convenient method to create the BusinessDocument instances.
   * Note: if isRequiredPack is true, we will include the transactionDoc's filename.
   * @param transactionDoc The doc that we want to send to OTC. If the value is null, this method
   *                       assume no doc is required in the BusinessDocument instance.
   * @param isRequiredPack
   * @return
   */
  public static BusinessDocument createBusinessDocument(File transactionDoc, Boolean isRequiredPack) throws Exception
  {
    
    String filename = "";
    byte[] contentInByteArr = null;
 
    if(transactionDoc != null && ( ! transactionDoc.exists() || ! transactionDoc.isFile()))
    {
      throw new Exception("The given file is not exist or it is a directory rather than a file ! File path is "+transactionDoc.getAbsolutePath()+" isExist="+transactionDoc.exists());
    }
    else if(transactionDoc != null)
    {
      if(isRequiredPack)
      {
        filename = transactionDoc.getName();
      }
      contentInByteArr = convertToByteArr(transactionDoc);
    }
    else
    {
      throw new NullPointerException("The given file is null !");
    }
    
    return new BusinessDocument(contentInByteArr,filename, isRequiredPack);
  }
  
  /**
   * Note: if isRequiredPack is true, we will auto generated the filename with prefix bizDoc_ follow by the timestamp
   * @param doc
   * @param isRequiredPack
   * @return
   */
  public static BusinessDocument createBusinessDocument(byte[] doc, Boolean isRequiredPack) throws Exception
  {
    if(doc != null)
    {
      String filename = "";
      if(isRequiredPack)
      {
        filename = "bizDoc_"+getCurrentTimestamp();
      }
      return new BusinessDocument(doc, filename, isRequiredPack);
    }
    else
    {
      return null;
    }
  }
  
  private static byte[] convertToByteArr(File file) throws Exception
  {
    return IOUtil.read(new FileInputStream(file));
  }
  
  private static String getCurrentTimestamp() throws Exception
  {
    Calendar c = Calendar.getInstance();
    long currentTimeInMilli = System.currentTimeMillis();
    c.setTimeInMillis(currentTimeInMilli);
    
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    String s = formatter.format(c.getTime());
    return s;
  }
}
