/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPart.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 08 Aug  2001    LSH                 Initial creation GT 1.1
 * 14 June 2002    Jagadeesh           GTAS(2.0)-Modified to throw SecurityServicesException.
 * 13 Nov  2007    Tam Wei Xiang       1) Add method to generate the ContentID for the compressed-mime
 *                                     2) Add method isCompressed() to determine the IPart 
 *                                        is in compressed formed.
 */



package com.gridnode.pdip.base.security.mime;

import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;

import javax.mail.BodyPart;
import java.io.File;

/**
 * Title:        GridNode Security
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author
 * @version 1.0
 */

public interface IPart extends IMailpart
{
  public BodyPart getBodyPart() throws SecurityServiceException;
  public void setContentType(String contentType) throws SecurityServiceException;
  public void setContent(Object content, String type) throws SecurityServiceException;
  public void setContent(IMime content) throws SecurityServiceException;
  public boolean isMultipart();
  public IMime getMultipart() throws SecurityServiceException;
  public void setFilename(String filename);
  public String getFilename();
  public void setAttachement(String filename) throws SecurityServiceException;
  public void setAttachement(File file) throws SecurityServiceException;
  public File getAttachement() throws SecurityServiceException;
  public File getAttachement(String targetFilename) throws SecurityServiceException;
  public File getAttachement(File targetFile) throws SecurityServiceException;
  public File getAttachement(File parent, String child) throws SecurityServiceException;

  /**
   * Is this Part of the specified MIME type? This method compares only the
   * primaryType and subType. The parameters of the content types are ignored.
   * For example, this method will return true when comparing a Part of content
   * type "text/plain" with "text/plain; charset=foobar".
   * If the subType of mimeType is the special character '*', then the subtype
   * is ignored during the comparison.
   *
   * @param mimeType
   * @return
   *
   * @since 1.1
   */
  public boolean isMimeType(String mimeType) throws SecurityServiceException;
  public boolean isEncrypted();
  public void setText(String text);
  
  //TWX To support RNIF2.0 compression
  public void setContentIDForCompression(IPart decompressedPart) throws SecurityServiceException;
  public boolean isCompressed();
}
