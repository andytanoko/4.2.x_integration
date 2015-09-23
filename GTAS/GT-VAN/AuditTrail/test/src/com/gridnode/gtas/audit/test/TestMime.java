/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TestMime.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 19, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class TestMime {
  
  private static String user_dir = System.getProperty("user.dir");
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception{
    
    // TODO Auto-generated method stub
    Session session = getSession();
    MimeMessage msg = new MimeMessage(session);
    
    MimeMultipart multiPart = new MimeMultipart(); //default content type is mixed
    
    
    File udoc = new File(user_dir+ "/testData/udoc.tmp");
    byte[] byteArr = convertToByteArr(udoc);
    //String base64 = convertFromByteToBase64(byteArr);
    String base64 = new String(byteArr);
    
    //udoc
    
    MimeBodyPart bodyPart = new MimeBodyPart();
    
    
    bodyPart.setText(base64);
    bodyPart.setFileName(udoc.getName());
    bodyPart.setDisposition("attachment");
    bodyPart.setHeader("Content-Type", "text/plain");
    bodyPart.setHeader("Content-Transfer-Encoding", "base64");
    multiPart.addBodyPart(bodyPart);
    
    
    File pic = null;
    
    //attachment 2
    pic = new File(user_dir+"/testData/IMG_0523.jpg");
    
    
    byteArr = convertToByteArr(pic);
    File tempFile = File.createTempFile("mime-", null);
    FileOutputStream out = new FileOutputStream(tempFile);
    out.write(byteArr);
    out.close();
    
    //base64 = convertFromByteToBase64(byteArr);
    
    bodyPart = new MimeBodyPart();
    DataSource fileData = new FileDataSource(tempFile);
    bodyPart.setDataHandler(new DataHandler(fileData));
    bodyPart.setFileName(pic.getName());
    bodyPart.setHeader("Content-Type", "text/plain");
    bodyPart.setHeader("Content-Transfer-Encoding", "base64"); //WILL DO THE BASE64 ENCODING !!!
    multiPart.addBodyPart(bodyPart); 
    
    //write mime msg to file
    File output = new File(user_dir+ "/testMime.txt");
    //outputMimeMsg(multiPart, output);
    
    //write the content in multipart into file
    extractContent(multiPart);
  }
  
  private static String base64FileToStr(File base64) throws Exception
  {
    FileInputStream in = new FileInputStream(base64);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] byteArr = new byte[512];
    int read = 0;
    
    while( (read = in.read(byteArr)) > -1)
    {
      out.write(byteArr, 0, read);
    }
    out.flush();
    out.close();
    in.close();
    return out.toString();
  }
  
  //physical file to base 64
  private static File fileConvertToBase64(File f) throws Exception
  {
    FileInputStream in = new FileInputStream(f);
    File out = new File(user_dir+"/fileIn64.txt");
    OutputStream op = new FileOutputStream(out);
    op = MimeUtility.encode(op, "base64");
    byte[] byteArr = new byte[512];
    int readSoFar = 0;
    while( (readSoFar = in.read(byteArr)) > -1)
    {
      op.write(byteArr, 0, readSoFar);
    }
    op.flush();
    op.close();
    in.close();
    return out;
  }
  
  //base 64 file to decoding file
  private static void fileDecodeBase64File(File base64F) throws Exception
  {
    InputStream in = new FileInputStream(base64F);
    in = MimeUtility.decode(in, "base64");
    File out = new File(user_dir+"/fileOut.jpg");
    FileOutputStream op = new FileOutputStream(out);
    byte[] arr = new byte[512];
    int read= 0;
    while( (read = in.read(arr)) > -1 )
    {
      op.write(arr, 0 , read);
    }
    op.flush();
    op.close();
    in.close();
  }
  
  private static void fileReader(File f) throws Exception
  {
    FileReader fr = new FileReader(f);
    BufferedReader br = new BufferedReader(fr);
    String s = "";
    while( (s = br.readLine()) != null)
    {
      System.out.println("Read line is "+s);
    }
  }
  
  private static byte[] convertFromBase64(byte[] base64) throws Exception
  {
    InputStream input = new ByteArrayInputStream(base64);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    OutputStream output = new BufferedOutputStream(out);
    input = MimeUtility.decode(input, "base64");
    
    byte[] arr = new byte[512];
    int read = 0;
    while( ( read = input.read(arr))  > -1)
    {
      output.write(arr, 0, read);
    }
    output.flush();
    out.flush();
    return out.toByteArray();
  }
  
  private static MimeMessage extractFromFileToMimeMsg(File f) throws Exception
  {
    Session session = getSession();
    FileInputStream input = new FileInputStream(f);
    
    return new MimeMessage(session, input);
  }
  
  private static void extractContent(MimeMultipart multiPart) throws Exception
  {
    System.out.println("Part count is "+ multiPart.getCount());
    for(int i = 0; i < multiPart.getCount(); i++)
    {
      Part part = multiPart.getBodyPart(i);
      String disposition = part.getDisposition();
      if(disposition != null && 
          (Part.ATTACHMENT.equals(disposition)|| Part.INLINE.equals(disposition)))
      {
        
        System.out.println("Part filename is "+part.getFileName());
        saveFile(part.getFileName(), part.getInputStream());
      }
    }
  }
  
  private static void saveFile(String filename, InputStream input) throws Exception
  {
    FileOutputStream out = new FileOutputStream(new File(filename));
    byte[] byteArr = new byte[512];
    int readSoFar = 0;
    while( (readSoFar = input.read(byteArr)) > -1)
    {
      out.write(byteArr, 0, readSoFar);
    }
    out.flush();
    out.close();
    input.close();
  }
  
  private static void outputMimeMsg(MimeMultipart multipart, File outputFile) throws Exception
  {
    /*
    InputStream in = multipart.getInputStream();
      byte[] byteArr = new byte[512];
      int readSoFar = 0;
      
      OutputStream out = new FileOutputStream(outputFile);
      
      while( (readSoFar = in.read(byteArr)) > -1)
      {
        out.write(byteArr, 0, readSoFar);
      }
      out.flush();
      out.close();
      in.close(); */
    multipart.writeTo(new FileOutputStream(outputFile));
  }
  
  public static String convertFromByteToBase64(byte[] arr) throws Exception
  {
    try
    {
      ByteArrayInputStream in = new ByteArrayInputStream(arr);
      
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      OutputStream out = new BufferedOutputStream(output);
      out = MimeUtility.encode(out, "base64");
      int readSoFar = 0;
      byte[] byteArr = new byte[512];
      while( (readSoFar = in.read(byteArr)) > -1)
      {
        out.write(byteArr, 0, readSoFar);
      }
      out.flush();
      output.flush();
      return output.toString();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      throw ex;
    }
    finally
    {
      
    }
  }
  
  private static byte[] convertToByteArr(File f) throws Exception
  {
    FileInputStream in = new FileInputStream(f);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] b = new byte[512];
    int readSoFar = 0;
    while( (readSoFar = in.read(b)) > -1)
    {
      out.write(b, 0, readSoFar);
    }
    out.close();
    return out.toByteArray();
  }
  
  private static Session getSession()
  {
    String to = "2007@hotmail.com"; //args[0];
    String from = "2007@yahoo.com"; //args[1];
    String host = "localhost"; //args[2];
    boolean debug = true; //Boolean.valueOf(args[3]).booleanValue();
    Properties props = new Properties();
    props.put("mail.smtp.host", host);

    Session session = Session.getInstance(props, null);
    session.setDebug(debug);
    return session;
  }
  
  private static void printByteArr(byte[] arr)
  {
    for(int i = 0; i < arr.length; i++)
    {
      int check = i % 20;
      System.out.print(arr[i]+" ");
      
      if(check == 0)
      {
        System.out.println();
      }
    }
  }
}

