package com.gridnode.pdip.base.transport.helpers;
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SMTPAgent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Mar 03 2003    Qingsong                 Created
 */
import java.io.*;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;


public class SMTPAgent
{
  private String serverurl;

  private java.util.Properties properties;
  private Session   session = null;
  private Transport transport = null;

  public SMTPAgent()
  {
    properties = System.getProperties();
    session = Session.getInstance(properties,null);
  }

  public SMTPAgent(String serverurl)
  {
    this();
    setServerurl(serverurl);
  }

  boolean connect() throws NoSuchProviderException, MessagingException
  {
      transport = session.getTransport("smtp");
      transport.connect(serverurl,"","");
      return true;
  }

    public MimeMessage CreateMimeMessage(
    String from,
    String to,
    String cc,
    String bcc,
    String subject,
    String text,
    String filename,
    byte[] text1) throws MessagingException, IOException
  {
    MimeMessage message = new MimeMessage(getSession());
    InternetAddress fromAddress = new InternetAddress(from);
    message.setFrom(fromAddress);

    InternetAddress[]  toAddress = InternetAddress.parse(to);
    message.setRecipients(Message.RecipientType.TO,toAddress);
    if(cc != null)
    {
        InternetAddress[]  ccAddress = InternetAddress.parse(cc);
        message.setRecipients(Message.RecipientType.CC,ccAddress);
    }
    if(bcc != null)
    {
      InternetAddress[]  bccAddress = InternetAddress.parse(bcc);
      message.setRecipients(Message.RecipientType.BCC,bccAddress);
    }
    message.setSubject(subject);
    MimeMultipart content = new MimeMultipart();
    if(text != null)
    {
      MimeBodyPart  body1 = new MimeBodyPart();
      body1.setText(text);
      content.addBodyPart(body1);
    }

    if(text1 != null)
    {
      MimeBodyPart  body2 = new MimeBodyPart();
      File tempfile = new File("HTTPChannelMsg.txt");
      FileOutputStream out = new FileOutputStream(tempfile);
      out.write(text1);
      out.close();
      FileDataSource fds= new FileDataSource(tempfile.getAbsolutePath());
      body2.setDataHandler(new DataHandler(fds));
      body2.setFileName(filename);
      content.addBodyPart(body2);
    }
    message.setContent(content);
    return message;
  }

  public MimeMessage CreateMessage(byte[] rawmessage) throws MessagingException
  {
    return CreateMessage(new ByteArrayInputStream(rawmessage));
  }

  public MimeMessage CreateMessage(String filename) throws FileNotFoundException, MessagingException
  {
      return CreateMessage(new FileInputStream(filename));
  }

  public MimeMessage CreateMessage(InputStream in) throws MessagingException
  {
      return new MimeMessage(getSession(), in);
  }

  static public boolean sendMessage(String serverurl, String from, String to,  String subject, String body,  String filename, byte[] attachment) throws IOException, MessagingException
  {
        boolean success = false;
        SMTPAgent agent = new SMTPAgent(serverurl);
        MimeMessage message = agent.CreateMimeMessage(from, to, null,null, subject, body, filename, attachment);
        success = agent.sendMessage(message);
        agent.close();
        return success;
  }

  public boolean sendMessage(Message message) throws NoSuchProviderException, MessagingException
  {
      if(transport == null && !connect())
        return false;
      transport.sendMessage(message, message.getAllRecipients());
      return true;
  }

  boolean close() throws MessagingException
  {
    boolean bSuccess = true;
    if(transport != null)
      transport.close();
    transport = null;
    session = null;
    return bSuccess;
  }
  public Session getSession()
  {
    return session;
  }
  public Transport getTransport()
  {
    return transport;
  }
  public void setServerurl(String serverurl)
  {
    this.serverurl = serverurl;
  }
  public void setTransport(Transport transport)
  {
    this.transport = transport;
  }
  public void setSession(Session session)
  {
    this.session = session;
  }
  public String getServerurl()
  {
    return serverurl;
  }
  public java.util.Properties getProperties()
  {
    return properties;
  }
  public void setProperties(java.util.Properties properties)
  {
    this.properties = properties;
  }
}