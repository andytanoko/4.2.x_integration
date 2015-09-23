/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchivalTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 16, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.mail.internet.MimeMultipart;

import javax.activation.FileDataSource;
import javax.mail.Part;
import javax.mail.util.ByteArrayDataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.gridnode.gtas.audit.archive.scheduler.exception.ArchiveScheduleException;
import com.gridnode.gtas.audit.common.IGTArchiveConstant;
import com.gridnode.gtas.audit.model.TraceEventInfo;
import com.gridnode.gtas.audit.util.XMLBeanMarshal;
import com.gridnode.util.io.IOUtil;
import com.gridnode.util.jms.JmsSender;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
class ArchivalTest
{
  private static QueueConnectionFactory connFac = null;
  private static Queue archiveDelegateQueue = null;
  
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    File temp = File.createTempFile("", "");
    // TODO Auto-generated method stub
    
    /*
    Hashtable criteria = new Hashtable();
    Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_MONTH, 1);
    Date fromStartDate = c.getTime();
    
    c.set(Calendar.DAY_OF_MONTH, 20);
    Date toStartDate = c.getTime();
    
    criteria.put(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME, fromStartDate.getTime());
    criteria.put(IArchiveConstant.CRITERIA_TO_START_DATE_TIME, toStartDate.getTime());
    
    JmsSender sender = new JmsSender();
    sender.setSendProperties(getProperties());
    sender.send(criteria); */  
    
    /*
    BusinessDocument bizDocument = new BusinessDocument();
    
    byte[] fileArray = getFromFile(new File("c:/3C3_udoc.xml"));
    System.out.println("File size "+fileArray.length);
    
    
    bizDocument.setDoc(fileArray);
    bizDocument.setRequiredPack(true);
    bizDocument.setFilename("3C3.xml");
    
    MimeConverter converter = MimeConverter.getInstance();
    byte[] mimeMsg = converter.convertToBase64Mime(new BusinessDocument[]{bizDocument});
    
    //byte[] afterZip = zipFile(mimeMsg);
    //System.out.println("Zip array "+afterZip.length);
    
    //construct back to mime multipart
    
    //unzip(afterZip);
    MimeMultipart multipart = getMimeMultipart(new File("c:/3C3_unzip.xml"));
    System.out.println("content type: " +multipart.getContentType());
    
    System.out.println("Part count "+multipart.getCount());
    extractContent(multipart); */
    
    
    //send archive request to GT
    Date fromDate = getDate(2007, 3, 20, 19, 00, 52);
    Date toDate = getDate(2007, 5, 20, 19, 00, 54);
    
    Hashtable archiveCriteria = new Hashtable();
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_NAME, "archive started "+new Date());
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_DESCRIPTION, "archiveDesc");
    archiveCriteria.put(IGTArchiveConstant.IS_ENABLED_ARCHIVED_SEARCHED, false);
    archiveCriteria.put(IGTArchiveConstant.IS_ENABLED_RESTORE, true);
    archiveCriteria.put(IGTArchiveConstant.FROM_START_DATE_TIME, fromDate.getTime());
    archiveCriteria.put(IGTArchiveConstant.TO_START_DATE_TIME, toDate.getTime());
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_ID, "123456");
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_TYPE, IGTArchiveConstant.ARCHIVE_TYPE_PI);
    archiveCriteria.put(IGTArchiveConstant.CUSTOMER_ID, "DEF123");
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_OPERATION, "archive");
    archiveCriteria.put(IGTArchiveConstant.ARCHIVE_ORPHAN_RECORD, "true");
    
    sendArchiveMsg(archiveCriteria);
  }
  
  private static TraceEventInfo getInfoFromXML(File xml) throws Exception
  {
    String str = new String(IOUtil.read(new FileInputStream(xml)));
    System.out.println(str);
    TraceEventInfo info = (TraceEventInfo)XMLBeanMarshal.xmlToObj(str, TraceEventInfo.class);
    return info;
  }
  
  private static Date getDate(int year , int month, int day, int hour, int minute, int second)
  {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, minute);
    c.set(Calendar.SECOND, second);
    
    return c.getTime();
  }
  
  private static void sendArchiveMsg(Map archiveCriteria) throws Exception
  {
    Context ctx = getContext();
    connFac = lookupQueueConnectionFactory(ctx);
    archiveDelegateQueue = lookupQueue("queue/gtvan/gtat/archiveQueue", ctx);
    
    
    sendMsgToQueue(connFac, archiveDelegateQueue, archiveCriteria);
  }
  
  public static void sendMsgToQueue(QueueConnectionFactory connectionFactory, Queue queue, Map archiveCriteria) throws Exception
  {
    QueueConnection queueConnection = connectionFactory.createQueueConnection();
    QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    QueueSender queueSender = queueSession.createSender(queue);
    MessageProducer producer = queueSession.createProducer(queue);
    
    
    MapMessage mapMsg = queueSession.createMapMessage();
    Set keys = archiveCriteria.keySet();
    for(Iterator ite = keys.iterator(); archiveCriteria != null && archiveCriteria.size() > 0 && 
        ite.hasNext(); )
    {
      String key = (String)ite.next();
      mapMsg.setObject(key, archiveCriteria.get(key));
    }
    
    producer.send(mapMsg);
    queueConnection.close();
  }
  
  public static QueueConnectionFactory lookupQueueConnectionFactory(Context ctx) throws Exception
  {
    System.out.println("Looking up connection factory....");
    return (QueueConnectionFactory)ctx.lookup("java:/ConnectionFactory");
  }
  
  public static Queue lookupQueue(String queueName, Context ctx) throws Exception
  {
    System.out.println("Looking up queue "+queueName);
    return (Queue)ctx.lookup(queueName);
  }
  
  public static Context getContext() throws Exception
  {
    return new InitialContext(getJndiProperties());
  }
  
  public static Properties getJndiProperties()
  {
    Properties p = new Properties();
    p.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" ) ;
    p.setProperty("java.naming.provider.url","localhost:31099") ;
    p.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" ) ; 
    return p;
  }
  
  private static void saveMimeMsg(byte[] mimeMsg) throws Exception
  {
    File f = new File("c:/3C3_mime.xml");
    IOUtil.write(f, mimeMsg);
  }
  
  private static MimeMultipart getMimeMultipart(File mimeMsg) throws Exception
  {
    FileDataSource ds = new FileDataSource(mimeMsg);
    MimeMultipart multiPart = new MimeMultipart(ds);
    return multiPart;
  }
  
  private static MimeMultipart getMimeMultipart(byte[] mimeMsg) throws Exception
  {
    ByteArrayDataSource ds = new ByteArrayDataSource(mimeMsg, "text/plain");
    MimeMultipart multiPart = new MimeMultipart(ds);
    return multiPart;
  }
  
  public static byte[] zipFile(byte[] fileArray) throws Exception
  {
    ByteArrayInputStream in = new ByteArrayInputStream(fileArray);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ZipOutputStream zipOut = new ZipOutputStream(out);
    
    ZipEntry entry = new ZipEntry("a.xml");
    zipOut.putNextEntry(entry);
    
    int readSoFar = 0;
    byte[] buffer = new byte[512];
    while( (readSoFar = in.read(buffer) ) != -1)
    {
      zipOut.write(buffer, 0, readSoFar);
    }
    
    zipOut.closeEntry();
    zipOut.close();
    out.close();
    return out.toByteArray();
  }
  
  public static void unzip(byte[] zipArray) throws Exception
  {
    File outF = new File("c:/3C3_unzip.xml");
    FileOutputStream out = new FileOutputStream(outF);
    
    ByteArrayInputStream in = new ByteArrayInputStream(zipArray);
    ZipInputStream zipIn = new ZipInputStream(in);
    ZipEntry entry = null;
    while( (entry = zipIn.getNextEntry()) != null)
    {
      int readSoFar = 0;
      byte[] buffer = new byte[512];
      while( (readSoFar = zipIn.read(buffer)) != -1)
      {
        out.write(buffer, 0, readSoFar);
      }
    }
    
    out.close();
    in.close();
    zipIn.close();
  }
  
  public static byte[] getFromFile(File inputFile) throws Exception
  {
    return IOUtil.read(new FileInputStream(inputFile));
  }
  
  private static void extractContent(MimeMultipart multiPart) throws Exception
  {
    
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
  
  public static Properties getProperties()
  {
    Properties p = new Properties();
    p.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" ) ;
    p.setProperty("java.naming.provider.url","localhost:31099") ;
    p.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" ) ; 
    p.setProperty(JmsSender.CONN_FACTORY, "ConnectionFactory");
    p.setProperty(JmsSender.DESTINATION, "queue/gtvan/isat/archiveTrailDataQueue");
    return p;
  }
  
  public static void reflecCall() throws Throwable
  {
    Class c = Class.forName("com.gridnode.gtas.audit.test.ArchivalTest");
    Method m = c.getMethod("testEx", null);
    m.invoke(c, null);
  }
  
  public static void testEx() throws Throwable
  {
    throw new ArchiveScheduleException("test adsfa");
  }
}

