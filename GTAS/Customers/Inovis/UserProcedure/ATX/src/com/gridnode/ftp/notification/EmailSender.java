/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 20, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.ftp.notification;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.gridnode.util.StringUtil;
import com.gridnode.util.exceptions.ILogErrorCodes;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * Copy from GTVAN com.gridnode.util.mail.EmailSender. Modified so that it won't load properties
 * from GTVAN DB.
 * @author Tam Wei Xiang
 * @version GT 4.1.3 (GTVAN)
 */
public class EmailSender
{
  //property keys for the SMTP server settings
  public static final String MAIL_SMTP_HOST = "mail.smtp.host";
  public static final String MAIL_SMTP_PORT = "mail.smtp.port";
  public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
  public static final String MAIL_SMTP_USER = "mail.smtp.user";
  public static final String MAIL_SMTP_AUTH_PWD = "mail.smtp.auth.pwd";
  public static final String MAIL_FROM = "mail.from";
  
  //added by Ming Qian for attachment retrieval 20100816
  public static final String MAIL_ATTACHMENT_FROM = "mail.attachment.from";
  
  public static final String MAIL_ATTACHMENT_RECIPIENTS = "mail.attachment.recipients";
  //end of added by Ming Qian for attachment retrieval 20100816
  
  //categories for configuration
  /**
   * Category for the default SMTP server settings
   */
  public static final String DEFAULT_SMTP_SERVER = "smtp.server.default";
  /**
   * Category for the Mail recipients settings
   */
  public static final String MAIL_RECIPIENTS = "mail.recipients";
  
  
  
  public static final String RECIPIENTS_DELIM = ";";
  
  private Logger _logger;
  private Properties _props;
  private MailAuthenticator _authenticator;
  
  public EmailSender()
  {
	//remove by Ming Qian for testing purposes
    _logger = LoggerManager.getOneTimeInstance().getLogger(LoggerManager.TYPE_UTIL, "EmailSender");
	//end of remove by Ming Qian for testing purposes
  }
  
  public void setSendProperties(Properties props)
  {
    _props = props;
    setAuth();
  }
  
  private void setAuth()
  {
    boolean auth = Boolean.valueOf(_props.getProperty(MAIL_SMTP_AUTH, "false"));
    if (auth)
    {
      String user = _props.getProperty(MAIL_SMTP_USER, null);
      String pass = _props.getProperty(MAIL_SMTP_AUTH_PWD, null);
      if (StringUtil.isBlank(user) || StringUtil.isBlank(pass))
      {
        _logger.logError(ILogErrorCodes.EMAIL_SENDER_SET_AUTH, "setAuth", null, "Mail authentication properties not set properly. Either user or password is not specified.", null);
      }
      else
      {
        _authenticator = new MailAuthenticator(user, pass);
      }
    }
    if (_authenticator == null)
    {
      _props.setProperty(MAIL_SMTP_AUTH, "false");
      _props.remove(MAIL_SMTP_USER);
      _props.remove(MAIL_SMTP_AUTH_PWD);
    }
  }
  
  public void send(List<String> recipient, String subject, String message)
  {
    String mtdName = "send";
    Object[] params = {recipient, subject, message};
    
    Address[] recipients = getRecipientAddresses(recipient);
    if (recipients.length == 0)
    {      
      //remove by Ming Qian for testing purposes	
      _logger.logError(ILogErrorCodes.EMAIL_SENDER_SEND, mtdName, params, "No recipient addresses found!", null);
      //remove by Ming Qian for testing purposes
      return;
    }
    
    Session mailSession = Session.getInstance(_props, _authenticator);
    try
    {
      Message mailMsg = createMessage(mailSession, recipients, subject, message);
      Transport.send(mailMsg);
    }
    catch (MessagingException ex)
    {
      _logger.logError(ILogErrorCodes.EMAIL_SENDER_SEND, mtdName, params, "Fail to send email", ex);
    }
  }
  
  //Regina Jan 22 2007: Added to support sending of attachment
  public void sendWithAttachments(List<String> recipients, String subject, String message, File[] files)
  {
    if(files!=null)
    {
      String mtdName = "sendWithAttachments";
      Object[] params = {recipients, subject, message, files};
      
      Address[] recipient = getRecipientAddresses(recipients);
      if (recipient.length == 0)
      {
        _logger.logError(ILogErrorCodes.EMAIL_SENDER_ATTACHEMENTS_SEND, mtdName, params, "No recipient addresses found!", null);
        return;
      }

      Session mailSession = Session.getInstance(_props, _authenticator);
      try
      {
        Message mailMsg = createMessageWithAttachments(mailSession, recipient, subject, message, files);
        Transport.send(mailMsg);
        _logger.logMessage("sendWithAttachments", params, "EMAIL SENT!");
      }
      catch (MessagingException ex)
      {
        _logger.logError(ILogErrorCodes.EMAIL_SENDER_ATTACHEMENTS_SEND, mtdName, params, "Fail to send email with attachments", ex);
      }
    }
    else
    {
      this.send(recipients, subject, message);
    }
  }
  
  //Regina Jan 22 2007: Added
  private Message createMessageWithAttachments(Session mailSession, Address[] recipients, String subject, String message, File[] files)
    throws MessagingException
  {
    MimeMessage msg = new MimeMessage(mailSession);
    Multipart mp = new MimeMultipart();    
    BodyPart body = new MimeBodyPart();
    body.setText(message);
    mp.addBodyPart(body);
    for(int i=0; i<files.length; i++)
    {
      MimeBodyPart mbp = new MimeBodyPart();
      DataSource ds = new FileDataSource(files[i]);
      mbp.setDataHandler(new DataHandler(ds));
      mbp.setFileName(ds.getName());
      mp.addBodyPart(mbp);
    }
    msg.addRecipients(RecipientType.TO, recipients);
    msg.setSubject(subject);          
    msg.setSentDate(new Date());
    msg.setContent(mp);    
    return msg;
  }
  
  private Message createMessage(Session mailSession, Address[] recipients, String subject, String message)
    throws MessagingException
  {
    MimeMessage msg = new MimeMessage(mailSession);
    
    msg.addRecipients(RecipientType.TO, recipients);
    msg.setSubject(subject);
    msg.setText(message);
    msg.setSentDate(new Date());
    return msg;
  }
  
  private Address[] getRecipientAddresses(List<String> strList)
  {
    List<Address> addrList = new ArrayList<Address>();
    for (String addrStr : strList)
    {
      try
      {
        addrList.add(new InternetAddress(addrStr));
      }
      catch (AddressException ex)
      {
        _logger.logError(ILogErrorCodes.EMAIL_SENDER_RECIPIENT_ADDRESSES, "getRecipientAddresses", null, "Invalid email address: "+addrStr, ex);
      }
    }
    return addrList.toArray(new Address[]{});
  }
  
  class MailAuthenticator extends Authenticator
  {
    String _username;
    String _password;

    public MailAuthenticator(String username, String password)
    {
      _username = username;
      _password = password;
    }

    public PasswordAuthentication getPasswordAuthentication()
    {
      return new PasswordAuthentication(_username, _password);
    }

  }
}
