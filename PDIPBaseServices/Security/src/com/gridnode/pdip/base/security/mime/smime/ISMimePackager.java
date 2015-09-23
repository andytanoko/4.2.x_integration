package com.gridnode.pdip.base.security.mime.smime;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import com.gridnode.pdip.base.security.mime.smime.exceptions.GNSMimeException;
/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 10 Nov 2003    Zou Qingsong        Created. *
 */

public interface ISMimePackager extends ISMimeBasic
{
  public static final int SCOPE_NONE = -1;
  public static final int SCOPE_ALL = 0;
  public static final int SCOPE_PAYLOAD = 1;
  public static final int SCOPE_ATTACHMENT = 2;

  public void setSignScope(int scope);
  public int getSignScope();
  public void disableSign();

  public void setEncryptScope(int scope);
  public int getEncryptScope();
  public void disableEncryption();

  public void setCompressScope(int scope);
  public int getCompressScope();
  public void disableCompress();

  public void setContent(MimeBodyPart content);
  public MimeBodyPart getContent();
  public void setAttachments(MimeBodyPart[] attachments);
  public MimeBodyPart[] getAttachments();

  public MimeMessage packDocument() throws GNSMimeException;
  public MimeMessage getPackedMessage();

  public byte[] compress(byte[] content) throws GNSMimeException;
  public byte[] encrypt(byte[] content) throws GNSMimeException;
  public byte[] sign(byte[] content) throws GNSMimeException;

  public BodyPart compress(BodyPart content) throws GNSMimeException;
  public BodyPart encrypt(BodyPart content) throws GNSMimeException;
  public Multipart sign(BodyPart content) throws GNSMimeException;
}