package com.gridnode.pdip.base.security.mime.smime.helpers;
import java.io.*;

import javax.mail.*;
import javax.mail.internet.*;

import com.gridnode.pdip.framework.util.UUIDUtil;
/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 10 Nov 2003    Zou Qingsong        Created. 
 * 12 Mar 2007    Neo Sok Lay         Use UUID for unique filename.
 */

public class SMimeHelper
{
  public static final int OUTPUT_STRING = 0;
  public static final int OUTPUT_BYTE_ARRAY = 1;
  public static final int OUTPUT_INPUTSTREAM = 2;
  public static final int OUTPUT_RAW = 3;
  public static final int OUTPUT_FILE = 4;
  public static final String ENCODING_BASE64 = "base64";
  public static final String ENCODING_BINARY = "binary";
  public static final String ENCODING_7BIT = "7bit";

  public SMimeHelper()
  {
  }

  static public MimePart setContent(MimePart part, Object content, String contentType) throws MessagingException
  {
      part.setContent(content, contentType);
      if(contentType != null && contentType.length() > 0)
        part = setContentType(part, contentType);
      return part;
  }

  static public MimeMessage setContent(MimeMessage part, Object content, String contentType) throws MessagingException
  {
        part.setContent(content, contentType);
        if(contentType != null && contentType.length() > 0)
          part = (MimeMessage)setContentType(part, contentType);
        part.saveChanges();
        return part;
  }

 static public MimePart setContentType(MimePart part, String type) throws MessagingException
 {
      part.removeHeader("Content-Type");
      part.setHeader("Content-Type", type);
      return part;
 }

 static public Multipart setContentType(Multipart part, String type) throws MessagingException
 {
      mime_multipart m = new mime_multipart(part);
      m.setContentType(type);
      return m;
 }

  static public MimePart removeParameter(MimePart part, String name) throws MessagingException
  {
      ContentType contenttype = new ContentType(part.getContentType());
      ParameterList pList = contenttype.getParameterList();
      pList.remove(name);
      String contentType = (new ContentType(contenttype.getPrimaryType(), contenttype.getSubType(), pList)).toString();
      return setContentType(part, contentType);
  }

  private static byte[] shorten(byte[] data, int len)
  {
    byte[] rv = new byte[len];

    System.arraycopy(data, 0, rv, 0, len);

    return rv;
  }

  public static Object convertContent(Object content, int outputType) throws MessagingException, IOException
  {
    if(outputType == OUTPUT_RAW)
        return content;
    InputStream in = null;
    int inLen = -1;
    Object rv = null;
    byte[] b = null;
      if (content instanceof String)
      {
        System.out.println("[convertContent] content instanceof String");
        if(outputType == OUTPUT_STRING)
          return content;
        in = new ByteArrayInputStream(((String)content).getBytes());
        inLen = ((String)content).length();
      }
      else if (content instanceof MimeMultipart)
      {
        System.out.println("[convertContent] content instanceof MimeMultipart");
        b = getBytesFromMime(createPart((MimeMultipart)content));
        if(outputType == OUTPUT_BYTE_ARRAY)
          return b;
        in = new ByteArrayInputStream(b);
        inLen = b.length;
      }
      else if (content instanceof InputStream)
      {
        System.out.println("[convertContent] content instanceof InputStream");
        if(outputType == OUTPUT_INPUTSTREAM)
          return content;
        in = (InputStream)content;
        inLen = in.available();
      }
      else if (content instanceof byte[])
      {
        System.out.println("[convertContent] content instanceof byte[]");
        if(outputType == OUTPUT_BYTE_ARRAY)
          return content;
        in = new ByteArrayInputStream((byte[])content);
        inLen = in.available();
      }
      else
        throw new MessagingException("Unsupported object for conversion");

      int readLen = -1;
      switch (outputType)
      {
        case OUTPUT_BYTE_ARRAY:
          b = new byte[inLen];
          readLen = in.read(b);
          if(readLen < inLen)
          {
            b = shorten(b , readLen);
          }
          rv = b;
          break;
        case OUTPUT_FILE:
          break;
        case OUTPUT_INPUTSTREAM:
          break;
        case OUTPUT_RAW:
          break;

        case OUTPUT_STRING:
          b = new byte[inLen];
          readLen = in.read(b);
          if(readLen < inLen)
          {
            b = shorten(b , readLen);
          }
          String str = new String(b);
          rv = str;
          break;
        default:
          throw new MessagingException("Unsupported output type");
      }
      in.close();
      return rv;
  }

  static public byte[] getContentBytesFromMime(MimePart content) throws MessagingException, IOException
  {
    return (byte[])convertContent(content.getContent(),OUTPUT_BYTE_ARRAY);
  }

  static public byte[] getBytesFromMime(MimePart content) throws MessagingException, IOException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    content.writeTo(out);
    return out.toByteArray();
  }

  static public byte[][] getBytesFromMime(MimePart[] content) throws MessagingException, IOException
  {
      if(content == null || content.length <= 0)
        return null;
      byte[][] parts = new byte[content.length][0];
      for(int i = 0; i < content.length;i++)
        parts[i] = getBytesFromMime(content[i]);
      return parts;
  }

  static public boolean isSomeType(MimePart mbp, String mimetype, String paraName, String paraValue)
  {
    try
    {
      if(!mbp.isMimeType(mimetype))
        return false;
      ContentType contenttype = new ContentType(mbp.getContentType());
      if(paraName != null && paraName.length() > 0)
        return paraValue.equals(contenttype.getParameter(paraName));
      else
        return true;
    }
    catch (Exception ex)
    {
      return false;
    }
  }

  static public boolean isMultipart(MimePart mbp)
  {
    return isSomeType(mbp, "multipart/*", null, null);
  }

  static public boolean isSigned(MimePart mbp)
  {
      return isSomeType(mbp, "multipart/signed", null, null);
  }

  static public boolean isEncrypted(MimePart mbp)
  {
    return isSomeType(mbp, "application/pkcs7-mime", pkcs7_mime.SMIME_TYPE_PARAM, pkcs7_mime.ENCRYPTED_SMIME_TYPE_VALUE);
  }

  static public boolean isCompressed(MimePart mbp)
  {
    return isSomeType(mbp, "application/pkcs7-mime", pkcs7_mime.SMIME_TYPE_PARAM, pkcs7_mime.COMPRESSED_SMIME_TYPE_VALUE);
  }

  static public  MimePart setParameter(MimePart part, String name, String value) throws MessagingException, ParseException
  {
      ContentType contenttype = new ContentType(part.getContentType());
      contenttype.setParameter(name, value);
      return setContentType(part, contenttype.toString());
  }


  static public MimeMultipart setParameter(MimeMultipart part, String name, String value) throws MessagingException
  {
      mime_multipart mul = new mime_multipart(part);
      mul.setParameter(name, value);
      return mul;
  }

  static public MimeBodyPart[] createPart(Object[] content, String contentType) throws MessagingException
  {
      if(content == null || content.length <= 0)
        return null;
      MimeBodyPart[] parts = new MimeBodyPart[content.length];
      for(int i = 0; i < content.length;i++)
        parts[i] = createPart(content[i], contentType);
      return parts;
  }

  static public MimeBodyPart createPart(byte[] content) throws MessagingException
  {
    return createPart(new ByteArrayInputStream(content));
  }

  static public MimeBodyPart createPart(InputStream in) throws MessagingException
  {
    return new MimeBodyPart(in);
  }

  static public MimeBodyPart createPart(Object content, String contentType) throws MessagingException
  {
      if(contentType != null && contentType.compareToIgnoreCase("text/plain") == 0 && (content instanceof byte[]))
        return (MimeBodyPart)setContent(new MimeBodyPart(), new String((byte[])content), contentType);
      else
        {
          if(contentType == null && (content instanceof byte[]))
            return createPart((byte[])content);
          else
            return (MimeBodyPart)setContent(new MimeBodyPart(), content, contentType);
        }
  }


  static public MimeBodyPart createPart(Multipart mm) throws MessagingException
  {
    return createPart(mm, mm.getContentType());
  }

  static public MimeMessage createMessage(Multipart mm) throws MessagingException
  {
    return setContent(createMessage(), mm, mm.getContentType());
  }

 static public MimeMessage createMessage(InputStream in) throws MessagingException
 {
    Session session = Session.getDefaultInstance(System.getProperties(), null);
    return new MimeMessage(session, in);
 }

 static public MimeMessage createMessage(byte[] content) throws MessagingException
 {
    return createMessage(new ByteArrayInputStream(content));
 }

 static public MimeMessage createMessage() throws MessagingException
 {
    Session session = Session.getDefaultInstance(System.getProperties(), null);
    return new MimeMessage(session);
 }

 static public MimeMessage createMessage(byte[] content, String contentType) throws MessagingException
 {
    MimeMessage body = createMessage(content);
    if(contentType != null && contentType.length() > 0)
      return (MimeMessage)setContentType(body, contentType);
    else
      return body;
 }

 static public String getContentEncoding(MimePart mm) throws MessagingException
  {
      return mm.getEncoding();
  }

 static public MimePart setContentEncoding(MimePart mm, String encoding) throws MessagingException
  {
      if(isMultipart(mm))
        return mm;
      mm.setHeader("Content-Transfer-Encoding", encoding);
      return mm;
  }

 static public MimePart checkSetContentEncoding(MimePart mm, String encoding) throws MessagingException
  {
      if(!hasContentEncoding(mm))
        return setContentEncoding(mm, encoding);
      else
        return mm;
  }

 static public MimeBodyPart checkSetContentEncoding(MimeBodyPart mm, String encoding) throws MessagingException
  {
      if(!hasContentEncoding(mm))
        return (MimeBodyPart)setContentEncoding(mm, encoding);
      else
        return mm;
  }

 static public boolean hasContentEncoding(MimePart mm) throws MessagingException
 {
    String en = getContentEncoding(mm);
    return (en != null) && (en.trim().length() > 0);
 }

 static public MimeMessage createMessage(MimePart mm) throws MessagingException, IOException
  {
    return setContent(createMessage(), mm.getContent(), mm.getContentType());
  }

  static public byte[] getBytesFromFile(String file) throws FileNotFoundException, IOException
  {
    return getBytesFromFile(new File(file));
  }

  static public byte[] getBytesFromFile(File file) throws FileNotFoundException, IOException
  {
      FileInputStream inputFile = new FileInputStream (file);
      byte[] byteArray = new byte[(int)inputFile.available()];
      inputFile.read (byteArray, 0, byteArray.length);
      inputFile.close();
      return byteArray;
  }

  static public File[] createFile(byte[][] content) throws FileNotFoundException ,IOException
  {
      if(content == null || content.length <= 0)
        return null;
      File[] parts = new File[content.length];
      for(int i = 0; i < content.length;i++)
        parts[i] = createFile(content[i]);
      return parts;
  }


  static public File createFile(File file, byte[] data) throws FileNotFoundException, IOException
  {
      FileOutputStream fo = new FileOutputStream(file);
      fo.write(data);
      fo.close();
      return file;
  }

  static public File createFile(byte[] data) throws FileNotFoundException, IOException
  {
      File file = File.createTempFile("~mail"+UUIDUtil.getRandomUUIDInStr(), null);
      file.deleteOnExit();
      return createFile(file, data);
  }
 }