/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailNotifier.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 20, 2009   Tam Wei Xiang       Created
 * Mar 20, 2009   Tam Wei Xiang       Added "FolderKey" to alert template
 * Aug 31, 2010   Tam Wei Xiang       #1753: Modified triggerIsEnforceAttachmentFailedEmail(..), 
 *                                           triggerFTPPullFailedEmail(..).
 */
package com.gridnode.ftp.notification;

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.3 (GTVAN)
 */
public class EmailNotifier
{
  private String _emailPropsPath = "";
  private Properties _emailProps = null;
  public static final String MAIL_SMTP_HOST = EmailSender.MAIL_SMTP_HOST;
  public static final String MAIL_SMTP_PORT = EmailSender.MAIL_SMTP_PORT;
  public static final String MAIL_SMTP_AUTH = EmailSender.MAIL_SMTP_AUTH;
  public static final String MAIL_SMTP_USER = EmailSender.MAIL_SMTP_USER;
  public static final String MAIL_SMTP_AUTH_PWD = EmailSender.MAIL_SMTP_AUTH_PWD;
  public static final String MAIL_FROM = EmailSender.MAIL_FROM;
  public static final String MAIL_RECIPIENTS = EmailSender.MAIL_RECIPIENTS;
  public static final String MAIL_SUBJECT = "mail.subject";
  public static final String MAIL_BODY = "mail.body";
  
  //added by Ming Qian for attachment retrieval 20100816
  public static final String MAIL_ATTACHMENT_FROM = EmailSender.MAIL_ATTACHMENT_FROM;
  public static final String MAIL_ATTACHMENT_RECIPIENTS = EmailSender.MAIL_ATTACHMENT_RECIPIENTS;
  public static final String MAIL_ATTACHMENT_SUBJECT = "mail.attachment.subject";
  public static final String MAIL_ATTACHMENT_BODY = "mail.attachment.body";
  //end of added by Ming Qian for attachment retrieval 20100816
  
  public static final String DATE_FORMAT = "E MMM dd HH:mm:ss z yyyy ";
  
  public static void main(String[] args)
  {
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
    System.out.println(format.format(new Date()));
    System.out.println(new Date());
  }
  
  public EmailNotifier(String emailPropsPath) 
  {
    setEmailPropsPath(emailPropsPath);
  }
  
  public EmailNotifier(Properties emailProps)
  {
    setEmailProps(emailProps);
  }
  
  public Properties getEmailProps()
  {
    return _emailProps;
  }
  
  public void setEmailProps(Properties emailProps)
  {
    _emailProps = emailProps;
  }
  
  public String getEmailPropsPath()
  {
    return _emailPropsPath;
  }
  
  public void setEmailPropsPath(String emailPropsPath)
  {
    _emailPropsPath = emailPropsPath;
  }
  
  public void triggerEmail(String udocFilename, String messageID, String tracingID, String errorMsg, String stackTrace,
                           String folderKey) throws Exception
  {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    Object[] substitudeValue = new Object[]{udocFilename, messageID, tracingID, errorMsg, stackTrace, formatter.format(new Date()),
                                            folderKey};
    Properties emailProps = loadEmailProps(); 
    
    String mailSubject = emailProps.getProperty(EmailNotifier.MAIL_SUBJECT);
    String mailBody = emailProps.getProperty(EmailNotifier.MAIL_BODY);
    
    mailSubject = MessageFormat.format(mailSubject, substitudeValue);
    mailBody = MessageFormat.format(mailBody, substitudeValue);
    List<String> mailRecipient = parseRecipientList(emailProps.getProperty(EmailNotifier.MAIL_RECIPIENTS), ";");
    
    EmailSender sender = new EmailSender();
    sender.setSendProperties(emailProps);
    sender.send(mailRecipient, mailSubject, mailBody);
  }
  
  //end of added by Ming Qian for attachment retrieval 20100816
  public void triggerIsEnforceAttachmentFailedEmail(String hostname, int port, String filename, String attachmentFilename, String fileExtension, String documentFolder, String attachmentFolder, String ftpPropsPath,
                                                    String ftpUserAcc) throws Exception
  {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    Object[] substitudeValue = new Object[]{formatter.format(new Date()), hostname, port, filename, attachmentFilename, fileExtension, documentFolder, attachmentFolder, ftpPropsPath, ftpUserAcc};
    Properties emailProps = loadEmailProps(); 
    
    String mailSubject = emailProps.getProperty(EmailNotifier.MAIL_ATTACHMENT_SUBJECT);
    String mailBody = emailProps.getProperty(EmailNotifier.MAIL_ATTACHMENT_BODY);
    mailBody = MessageFormat.format(mailBody, substitudeValue);
    List<String> mailRecipient = parseRecipientList(emailProps.getProperty(EmailNotifier.MAIL_ATTACHMENT_RECIPIENTS), ";");
    EmailSender sender = new EmailSender();
    sender.setSendProperties(emailProps);
    sender.send(mailRecipient, mailSubject, mailBody);
  }
  //end of added by Ming Qian for attachment retrieval 20100816
  
  public void triggerFTPPullFailedEmail(String errorMsg, String stackTrace, String ftpBackendProps) throws Exception
  {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    Object[] substitudeValue = new Object[]{formatter.format(new Date()), errorMsg, stackTrace, ftpBackendProps};
    Properties emailProps = loadEmailProps(); 
    
    String mailSubject = emailProps.getProperty(EmailNotifier.MAIL_SUBJECT);
    String mailBody = emailProps.getProperty(EmailNotifier.MAIL_BODY);
    mailBody = MessageFormat.format(mailBody, substitudeValue);
    List<String> mailRecipient = parseRecipientList(emailProps.getProperty(EmailNotifier.MAIL_RECIPIENTS), ";");
    
    EmailSender sender = new EmailSender();
    sender.setSendProperties(emailProps);
    sender.send(mailRecipient, mailSubject, mailBody);
  }
  
  private Properties loadEmailProps() throws Exception
  {
    if(getEmailProps() == null)
    {
      return initMailPropsFromFile(new File(getEmailPropsPath()));
    }
    return getEmailProps();
  }
  
  /**
   * @param emailPropsFile
   * @return
   * @throws Exception
   */
  private Properties initMailPropsFromFile(File emailPropsFile) throws Exception
  {
    Properties emailProps = new Properties();
    try
    {
      emailProps.load(new FileInputStream(emailPropsFile));
      
      
      if(isEmptyProperty(emailProps.getProperty(EmailNotifier.MAIL_SMTP_HOST)))
      {
        throw new IllegalArgumentException("SMTP server configuration is empty");
      }
      
      if(isEmptyProperty(emailProps.getProperty(EmailNotifier.MAIL_SMTP_PORT)))
      {
        throw new IllegalArgumentException("SMTP server port configuration is empty");
      }
      
      if(isEmptyProperty(emailProps.getProperty(EmailNotifier.MAIL_FROM)))
      {
        throw new IllegalArgumentException("SMTP email from is empty");
      }
      
      if(isEmptyProperty(emailProps.getProperty(EmailNotifier.MAIL_RECIPIENTS)))
      {
        throw new IllegalArgumentException("Mail Recipient list is empty");
      }
      
      if(isEmptyProperty(emailProps.getProperty(EmailNotifier.MAIL_SUBJECT)))
      {
        throw new IllegalArgumentException("Mail Subject is empty");
      }
      
      if(isEmptyProperty(emailProps.getProperty(EmailNotifier.MAIL_BODY)))
      {
        throw new IllegalArgumentException("Mail Body is empty");
      }
      
      return emailProps;
      
    }
    catch(Exception ex)
    {
      throw new Exception("Configuration can't be initialised properly", ex);
    }
  }
  
  private boolean isEmptyProperty(String property)
  {
    return property == null || property.trim().length() == 0;
  }
  
  private List<String> parseRecipientList(String recipientList, String delim)
  {
    ArrayList<String> recipients = new ArrayList<String>();
    StringTokenizer st = new StringTokenizer(recipientList, delim);
    while(st.hasMoreTokens())
    {
      recipients.add(st.nextToken());
    }
    return recipients;
  }
}
