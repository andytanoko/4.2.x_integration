package com.gridnode.pdip.base.security.mime;
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMailPart.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 10 Aug 2001    Lim Soon Hsiung     Initial creation GT 1.1
 * 14 Jun 2002    Jagadeesh           GTAS-2.0(Methods to throws SecurityServiceException).
 * 27 Jun 2002    Lim Soon Hsiung     Expose the getByteArrayOutputStream(boolean withMsgHeader)
 *  and writeToStream(OutputStream os, boolean withMsgHeader) method
 * 27 Aug 2002    Lim Soon Hsiung     Added setContentID for Printronix defect
 * 04 Jan 2008    Tam Wei Xiang       Added writeContentToStream(...) for write out the content of the Part.
 */


import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;

import java.io.*;

public interface IMailpart
{
  public static final int OUTPUT_STRING = 0;
  public static final int OUTPUT_BYTE_ARRAY = 1;
  public static final int OUTPUT_INPUTSTREAM = 2;
  public static final int OUTPUT_RAW = 3;
  public static final int OUTPUT_FILE = 4;

  public static final String[] HDR_IGNORE_LIST = {"Message-ID", "Mime-Version"};

  public Object getContent() throws SecurityServiceException;
  public Object getContent(int outputType) throws SecurityServiceException;
  public String getContentString() throws SecurityServiceException;
  public byte[] getContentByte() throws SecurityServiceException;
  public byte[] getContentByte(boolean withMsgHeader) throws SecurityServiceException;
  public File getContentFile(File file) throws SecurityServiceException;
  public File getContentFile(String filename) throws SecurityServiceException;
  public String[] getHeader(String headerName) throws SecurityServiceException;
  public void addHeader(String headerName, String headerValue) throws SecurityServiceException;
  public void setParameter(String name, String value) throws SecurityServiceException;
  public void removeParameter(String name) throws SecurityServiceException;

  public String getDescription() throws SecurityServiceException;
  public String getContentType() throws SecurityServiceException;
  public void setDescription(String description) throws SecurityServiceException;
  public void writeToStream(OutputStream os) throws SecurityServiceException;
  public ByteArrayOutputStream getByteArrayOutputStream() throws SecurityServiceException;
  public ByteArrayOutputStream getByteArrayOutputStream(boolean withMsgHeader) throws SecurityServiceException;

  public boolean isSigned();
  public void writeToStream(OutputStream os, boolean withMsgHeader) throws SecurityServiceException;
  public void writeContentToStream(OutputStream os)throws SecurityServiceException;

  /**
   * @since 1.3
   */
  public void setContentID();

}