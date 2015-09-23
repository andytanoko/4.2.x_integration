/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MimeConverter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 27, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.model.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to convert the byte array which represent a file content in Mime
 * format.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class MimeConverter
{
  private static MimeConverter _mimeConverter = new MimeConverter();
  private static final String CLASS_NAME = "MimeConverter";
  private Logger _logger = null;
  private final String BASE64_ENCODING = "base64";
  
  private MimeConverter() 
  {
    _logger = getLogger();
  }
  
  public static MimeConverter getInstance()
  {
    return _mimeConverter;
  }
  
  private void addHeadersToBodyPart(Properties headerProperties, MimeBodyPart bodyPart) throws MessagingException
  {
    if(headerProperties != null && headerProperties.size() > 0)
    {
      Enumeration headerEnum = headerProperties.keys();
      while(headerEnum.hasMoreElements())
      {
        String propertyKey = (String)headerEnum.nextElement();
        bodyPart.addHeader(propertyKey, headerProperties.getProperty(propertyKey));
      }
    }
  }
  
  /**
   * Convert array of BusinessDocument into MimeFormat and return as a byte array
   * @param businessDocs
   * @return
   * @throws Exception
   */
  public byte[] convertToBase64Mime(BusinessDocument[] businessDocs) throws Exception
  {
    String methodName = "convertToBase64Mime";
    
    if(businessDocs != null && businessDocs.length > 0)
    {
      MimeMultipart multiPart = new MimeMultipart();
      for(BusinessDocument busDoc :  businessDocs)
      {
        if(busDoc.getDoc() != null && busDoc.getDoc().length > 0)
        {
          MimeBodyPart bodyPart = new MimeBodyPart();
          
          ByteArrayDataSource ds = new ByteArrayDataSource(busDoc.getDoc(), "text/plain");
          bodyPart.setDataHandler(new DataHandler(ds));
          
          addHeadersToBodyPart(getBase64DefHeading(), bodyPart);
          bodyPart.setDisposition(getDefContentDisposition());
          //bodyPart.setText(new String(convertByteArrToBase64(busDoc.getDoc())));
          bodyPart.setFileName(busDoc.getFilename());
          multiPart.addBodyPart(bodyPart);
        }
      }
      return convertMultipartToByteArr(multiPart);
    }
    return null;
  }
  
  private byte[] convertMultipartToByteArr(MimeMultipart multiPart) throws IOException, MessagingException
  {
    ByteArrayOutputStream arrOut = new ByteArrayOutputStream();
    byte[] convertedArr = null;
    try
    {
      multiPart.writeTo(arrOut);
      convertedArr = arrOut.toByteArray();
    }
    finally
    {
      if(arrOut != null)
      {
        arrOut.close();
      }
    }
    return convertedArr;
  }
  
  /**
   * Return the default base64 heading
   * @return
   */
  private Properties getBase64DefHeading()
  {
    Properties headingProperties = new Properties();
    headingProperties.setProperty("Content-Type", "text/plain");
    headingProperties.setProperty("Content-Transfer-Encoding", "base64");
    return headingProperties;
  }
  
  private String getDefContentDisposition()
  {
    return "attachment";
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
