/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNMimepart.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 08 Aug  2001    Lim Soon Hsiung     Initial creation GT 1.1
 * 07 June 2002    Jagadeesh           GTAS.2.0 - Modified to throw SecurityServiceException.
 * 27 Jun 2002     Lim Soon Hsiung     Add enableMsgHeader(boolean withMsgHeader)
 *                                     and isMsgHeaderEnabled() methods
 * 27 Aug 2002    Lim Soon Hsiung      Modify setContentID implementation for PTX issue
 * 04 Jan 2008    Tam Wei Xiang       Added writeContentToStream(...) for write out the content of the Part.
 */

package com.gridnode.pdip.base.security.mime;

import java.io.*;
import java.util.Hashtable;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;

/**
 * Title:        GridNode Security
 * Description:  GridNode Security Module
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Lim Soon Hsiung
 * @version 1.1
 */
public class GNMimepart implements IMime //,IMimeExceptionValue
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3514665232695720281L;
	private GNMultipart mmp;
  private MimeMessage msg;
  private Hashtable ht = new Hashtable();
  protected boolean isSigned = false;
  private MimeBodyPart bp;
  //  protected final boolean isEncrypted = false;

  private boolean withMsgHeader = false;
  private boolean generateContentID = false;
  private boolean readOnly = false;

  public GNMimepart(File file) throws SecurityServiceException
  {
    try
    {
      FileInputStream in = new FileInputStream(file);
      msg = new MimeMessage(null, in);
      MimeMultipart tmp = (MimeMultipart)msg.getContent();
      mmp = new GNMultipart(tmp);
      setReadOnly(true);
      mmp.setReadOnly(true);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to create GNMimepart from file: "+file,ex);
//      GNException.throwEx(EX_CREATE_MIME,
//                          "Unable to create GNMimepart from file: " + file,
//                          ex);
    }
  }


  public GNMimepart(MimeMultipart multipart) throws SecurityServiceException
  {
    mmp = GNMultipart.createInstance(multipart);
    bp = new MimeBodyPart();
    try
    {
      bp.setContent(multipart);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException(
      "Unable to create MimeBodyPart from MimeMultipart.",
      ex
      );
    }

  }

  public GNMimepart()
  {
    mmp = new GNMultipart("related");
  }


  public GNMimepart(MimeBodyPart bp) throws SecurityServiceException
  {
    this.bp = bp;
    try
    {
      mmp = new GNMultipart((MimeMultipart)bp.getContent());
      mmp.setReadOnly(readOnly);
    }
    catch (Exception ex)
    {
       throw new SecurityServiceException(
       "Unable to create GNMultipart from MimeBodyPart, " + ex.getMessage(),
       ex);
    }
  }

  public int getPartCount()
  {
    int count = 0;
    try
    {
      count = mmp.getCount();

    }
    catch (Exception ex)
    {
      count = -1;
      ex.printStackTrace();
    }
    return count;
  }

  public void addPart(IMailpart part) throws SecurityServiceException
  {
    if (part instanceof IMime)
    {
      addPart((IMime)part);
    }
    else if (part instanceof IPart)
    {
      addPart((IPart)part);
    }
    else
      throw new SecurityServiceException("Unsupported part object: " + part.getClass().getName());
//      GNException.throwEx(EX_GENERAL,
//                          "Unsupported part object: " + part.getClass().getName());

  }

  public void addPart(IPart part) throws SecurityServiceException
  {
    try
    {
      mmp.addBodyPart(part.getBodyPart());
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to add bodypart", ex);
//      GNException.throwEx(EX_GENERAL, "Unable to add bodypart", ex);
    }
  }

  public void addPart(IMime mpart) throws SecurityServiceException
  {
    try
    {
      MimeBodyPart mbp = new MimeBodyPart();
      mbp.setContent(mpart.getMultipart());
      ht.put(mpart, mbp);
      mmp.addBodyPart(mbp);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to add bodypart", ex);
//      GNException.throwEx(EX_GENERAL, "Unable to add bodypart", ex);
    }

  }

  public void removePart(IPart part) throws SecurityServiceException
  {
    try
    {
      mmp.removeBodyPart(part.getBodyPart());
    }
    catch (Exception ex)
    {
        throw new SecurityServiceException("Unable to remove bodypart", ex);
//      GNException.throwEx(EX_GENERAL, "Unable to remove bodypart", ex);
    }
  }

  public void removePart(IMime mpart) throws SecurityServiceException
  {
    try
    {
      MimeBodyPart mbp = (MimeBodyPart)ht.get(mpart);
      mmp.removeBodyPart(mbp);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to remove bodypart", ex);
//      GNException.throwEx(EX_GENERAL, "Unable to remove bodypart", ex);
    }
  }

  public void removePart(int index) throws SecurityServiceException
  {
    try
    {
      mmp.removeBodyPart(index);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to remove bodypart at " + index , ex);
//      GNException.throwEx(EX_GENERAL, "Unable to remove bodypart at " + index , ex);
    }
  }

  public Multipart getMultipart()
  {
    return mmp;
  }

  public IPart getPart(int index) throws SecurityServiceException
  {
    IPart rv = null;
    BodyPart part = null;
    try
    {
      part = mmp.getBodyPart(index);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to get body part at" + index, ex);
//      GNException.throwEx(EX_UNKNOWN, "Unable to get body part at" + index, ex);
    }

    if (part instanceof MimeBodyPart)
    {
      rv = new GNBodypart((MimeBodyPart)part);
    }
    else
    {
      throw new SecurityServiceException("Unable to parse body part at " + index +
                            " which is of class " + part.getClass().getName());
//
//      GNException.throwEx(EX_GENERAL ,
//                          "Unable to parse body part at " + index +
//                            " which is of class " + part.getClass().getName());
    }

    return rv;
  }

  public String getContentString() throws SecurityServiceException
  {
    String str = null;
//    try
//    {
      str = getByteArrayOutputStream().toString();
//    }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//    }
    return str;
  }

  public byte[] getContentByte() throws SecurityServiceException
  {
    return getContentByte(true);
  }

  public byte[] getContentByte(boolean withMsgHeader) throws SecurityServiceException
  {
    byte[] b = null;
//    try
//    {
      b = getByteArrayOutputStream(withMsgHeader).toByteArray();
//    }
//    catch (Exception ex)
//    {
//      GNException.throwEx(EX_GENERAL,
//                          "Unable to get byte array content" ,
//                          ex);
//    }
    return b;
  }

  public File getContentFile(File file) throws SecurityServiceException
  {
    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(file);
      writeToStream(fos);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to write to file", ex);
//      GNException.throwEx(EX_GENERAL, "Unable to write to file", ex);
    }
    finally
    {
      if(fos != null)
      {
        try
        {
          fos.close();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }

      }
    }
    return file;
  }

  public File getContentFile(String filename) throws SecurityServiceException
  {
    return getContentFile(new File(filename));
  }

  public String getDescription()
  {
//    return mmp.getDescription();
    return null;
  }

  public String getContentType()
  {
    return mmp.getContentType();
  }

  public void setDescription(String description)
  {
//    mmp.setDescription(description);
  }


  public void setSubType(String subType) throws SecurityServiceException
  {
    try
    {
      mmp.setSubType(subType);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to set sub-type: " +
                                          subType,ex);

//      GNException.throwEx(EX_GENERAL,
//                          "Unable to set sub-type: " +
//                          subType,
//                          ex);
    }

  }

  public void writeToStream(OutputStream os) throws SecurityServiceException
  {
    writeToStream(os, true);
  }

  public void writeToStream(OutputStream os, boolean withMsgHeader)
    throws SecurityServiceException
  {

    if(readOnly && bp != null)
    {
      writeToStreamRO(os);
      return;
    }
    String[] ignoreList = null;
    if(!withMsgHeader)
    {
      ignoreList = HDR_IGNORE_LIST;
    }

    try
    {
      if(msg == null)
      {
        msg = new MimeMessage((Session)null);
        msg.setContent(mmp);
        if(generateContentID)
        {
          setContentID(msg);
        }
        msg.saveChanges();
      }
      msg.writeTo(os, ignoreList);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Wrtie to stream error", ex);
//      GNException.throwEx(EX_GENERAL, "Wrtie to stream error", ex);
    }
  }

  private void writeToStreamRO(OutputStream os)
    throws SecurityServiceException
  {
    try
    {
      bp.writeTo(os);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Wrtie to stream error", ex);
    }
  }


  public ByteArrayOutputStream getByteArrayOutputStream() throws SecurityServiceException
  {
    return getByteArrayOutputStream(true);
  }

  public ByteArrayOutputStream getByteArrayOutputStream(boolean withMsgHeader)
    throws SecurityServiceException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//    try
//    {
      writeToStream(bos, withMsgHeader);
//    }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//    }
    return bos;
  }

  public void addHeader(String headerName, String headerValue)
  {
    /** @todo how to implement it ??? JavaMail don't have, why? */
  }

  public String[] getHeader(String headerName)
  {
    /** @todo how to implement it ??? JavaMail don't have, why? */
    return null;
  }

  public boolean isSigned()
  {
    try
    {
      isSigned = mmp.isMimeType("multipart/signed");
    }
    catch (Exception ex)
    {
      isSigned = false;
      ex.printStackTrace();
    }
    return isSigned;
  }
/*
  public boolean isEncrypted()
  {
    return isEncrypted;
  }
*/
  public Object getContent() throws SecurityServiceException
  {
    return getContent(OUTPUT_STRING);
  }

  public Object getContent(int outputType) throws SecurityServiceException
  {
    throw new SecurityServiceException("Method not supported yet");
//    GNException.throwEx(EX_GENERAL, "Method not supported yet");
//    return null;
  }

  public String getParameter(String name)
  {
    return mmp.getParameter(name);
  }

  public void setParameter(String name, String value) throws SecurityServiceException
  {
    mmp.setParameter(name, value);
  }

  public void removeParameter(String name) throws SecurityServiceException
  {
    mmp.removeParameter(name);
  }

  public IPart addAttachment(File file) throws SecurityServiceException
  {
    GNBodypart part = new GNBodypart();
    part.setAttachement(file);
    addPart(part);

    return part;
  }

  public void setContentID()
  {
      generateContentID = true;
  /*
    try
    {
      String contentID = msg.getHeader("Content-ID", null);
      if(contentID != null)
        return;

      msg.removeHeader("Content-ID");
      msg.setHeader("Content-ID", "<" +
            GNMimeUtility.getUniqueMessageIDValue() +
            ">");
    }
    catch (Exception ex)
    {
    }*/
  }


  public static void setContentID(MimeMessage mimeMsg)
  {
    try
    {
      mimeMsg.removeHeader("Content-ID");
      mimeMsg.setHeader("Content-ID", "<" +
            GNMimeUtility.getUniqueMessageIDValue() +
            ">");
    }
    catch (Exception ex)
    {
      System.out.println("[GNMimepart.setContentID] error ");
    }
  }


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
/*  public boolean isMimeType(String mimeType) throws GNException
  {
    boolean b = false;
    try
    {
      b = mmp.isMimeType(mimeType);
    }
    catch (Exception ex)
    {
      GNException.throwEx(exV, ex);
    }

    GNException.throwEx(exV, "Method not supported yet!!!");
    return b;
  }
*//*
  public static void main(String[] args)
  {
    try
    {
    //    GNBodypart bp = new GNBodypart("preamble.xml", "application/xml", null);
        GNBodypart bp = new GNBodypart();
    //    FileInputStream f = new FileInputStream("preamble.xml");
    //    FileDataSource fds = new FileDataSource("preamble.xml");
        File fds = new File("preamble.xml");
        String testStr = "This is a test String";
    //    byte[] b = testStr.getBytes();
    //    bp.setContent(testStr, null);
        bp.setContent(fds, "application/xml");
    //    bp.setContent("This is a test content", "text/plain");
        String text = bp.getContentString();

//        System.out.println("Bodypart content: \n" + text);
//        System.out.println("Bodypart end---------------------------------");
//
//        System.out.println("****************** Byte test");
        String myData = "This is a byte[] test";
    //    GNBodypart bd = new GNBodypart(myData.getBytes(), "text/plain");
        GNBodypart bd = new GNBodypart();
        bd.setContent(myData.getBytes(), "application/xml");
        text = bd.getContentString();

//        System.out.println("Bodypart content: \n" + text);
//        System.out.println("Bodypart end---------------------------------");
        text = (String)bd.getContent();
//        System.out.println("Bodypart converted content: \n" + text);
//        System.out.println("Bodypart end---------------------------------");
        byte[] bbbb = (byte[])bd.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
        text = new String(bbbb);
//        System.out.println("Bodypart converted content: \n" + text);
//        System.out.println("Bodypart end---------------------------------");

        GNMimepart mmp = new GNMimepart();
        mmp.setParameter("type", "application/xml");
        mmp.setParameter("start", "<Content-ID-for-Service-Header>");

        mmp.addPart(bp);
        mmp.addPart(bd);

        GNMimepart mmp3 = new GNMimepart();
        mmp3.setSubType("GridNode");
        GNBodypart bp2 = new GNBodypart();
        bp2.setContent("this is part One", "text/plain");
        GNBodypart bp3 = new GNBodypart();
        bp3.setContent("this is part Two", "text/plain");
        mmp3.addPart(bp2);
        mmp3.addPart(bp3);
        mmp.addPart(mmp3);

        text = new String(mmp.getContentByte(false));
        System.out.println("GNMimepart strip content: ---------------------\n" + text);
        System.out.println("GNMimepart end---------------------------------");

        text = mmp.getContentString();
        System.out.println("GNMimepart full content: ----------------------\n" + text);
        System.out.println("GNMimepart end---------------------------------");

        mmp.getContentFile("multipart.txt");
        File fff = new File("multipart.txt");
        GNMimepart mmp2 = new GNMimepart(fff);

        System.out.println("Getting contentString from read in file *********************************************************************************");
        text = mmp2.getContentString();
        System.out.println("GNMimepart 2 after read content: -------------\n" + text);
        System.out.println("GNMimepart end---------------------------------");

        System.out.println("Getting contentString from read in file *********************************************************************************");
        text = new String(mmp2.getContentByte(false));
        System.out.println("GNMimepart 2 after read strip content: -------------\n" + text);
        System.out.println("GNMimepart end---------------------------------");

        System.out.println();
        System.out.println();
        System.out.println("---------- type:    " + mmp2.getParameter("type"));
        System.out.println("---------- start:    " + mmp2.getParameter("start"));

        IMailpart imp = GNMimeUtility.generatePart(mmp2.getContentByte(true));
        text = new String(imp.getContentByte(false));
        System.out.println("GNMimeUtility.generatePart content: -------------\n" + text);
        System.out.println("GNMimeUtility.generatePart end---------------------------------");

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }*/
  /*
  private void printHeader(MimeMessage ms)
  {
    try
    {

      Enumeration res = ms.getAllHeaders();
      Header hdr = null;
      System.out.println("Printing headers **************************************");
      while (res.hasMoreElements())
      {
        hdr = (Header)res.nextElement();
        System.out.println("Header: " + hdr.getName() + " - " + hdr.getValue());
      }
      System.out.println("End Printing headers **************************************");

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }*/


  public void enableMsgHeader(boolean withMsgHeader)
  {
    this.withMsgHeader = withMsgHeader;
  }

  public boolean isMsgHeaderEnabled()
  {
    return withMsgHeader;
  }

  void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }

  public boolean isReadOnly()
  {
    return readOnly;
  }
  
  public void writeContentToStream(OutputStream os)throws SecurityServiceException
  {
	  try
	  {
		  mmp.writeTo(os);
	  }
	  catch(Exception ex)
	  {
		  throw new SecurityServiceException("Unable to write mmp part content to output stream.", ex);
	  }
  }
}