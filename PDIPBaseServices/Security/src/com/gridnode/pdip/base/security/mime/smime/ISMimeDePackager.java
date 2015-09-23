package com.gridnode.pdip.base.security.mime.smime;
import javax.mail.BodyPart;
import javax.mail.Multipart;
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

public interface ISMimeDePackager extends ISMimeBasic
{
  public MimeMessage dePackDocument() throws GNSMimeException;

  public void        setExpandMessage(boolean isExpand);
  public void        setMessage(MimeMessage message);
  public MimeMessage getMessage();
  public MimeMessage expandMessage(MimeMessage m) throws GNSMimeException;
  public MimeMessage getDePackedMessage();

  public byte[] deCompress(byte[] content) throws GNSMimeException;
  public byte[] decrypt(byte[] content) throws GNSMimeException;
  public byte[] verify(byte[] content, byte[] signature) throws GNSMimeException;

  public BodyPart deCompress(BodyPart content) throws GNSMimeException;
  public BodyPart deCrypt(BodyPart content) throws GNSMimeException;
  public BodyPart verify(Multipart content) throws GNSMimeException;
}