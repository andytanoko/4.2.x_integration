/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMime.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 08 Aug  2001    LSH                 Initial creation GT 1.1
 * 14 June 2002    Jagadeesh           GTAS(2.0)- Modified to include SecurityServiceException.
 * 27 Jun 2002    Lim Soon Hsiung     Add enableMsgHeader(boolean withMsgHeader)
 *    and isMsgHeaderEnabled() methods
 *
 */

package com.gridnode.pdip.base.security.mime;

import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;

import javax.mail.Multipart;
import java.io.File;
import java.io.Serializable;

public interface IMime extends IMailpart,Serializable
{
  public int getPartCount() throws SecurityServiceException;
  public void addPart(IMailpart part) throws SecurityServiceException;
  public void addPart(IPart part) throws SecurityServiceException;
  public void addPart(IMime mpart) throws SecurityServiceException;
  public void removePart(IPart part) throws SecurityServiceException;
  public void removePart(IMime mpart) throws SecurityServiceException;
  public void removePart(int index) throws SecurityServiceException;
  public IPart getPart(int index) throws SecurityServiceException;

  public Multipart getMultipart() throws SecurityServiceException;
  public void setSubType(String subType) throws SecurityServiceException;
  public String getParameter(String name);
  public IPart addAttachment(File file) throws SecurityServiceException;
  public void enableMsgHeader(boolean withMsgHeader);
  public boolean isMsgHeaderEnabled();

}
