/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNBodyPart.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 08 Aug  2001    Lim Soon Hsiung     Initial creation GT 1.1
 * 17 June 2002    Jagadeesh           GTAS.2.0 Included to throw SecurityServiceException.
 * 27 Jun 2002     Lim Soon Hsiung     Change writeToStream() to output with messageHeader
 * 27 Aug 2002     Lim Soon Hsiung     Modify setContentID implementation for PTX issue
 * 09 Dec 2002     Jagadeesh           Included in GT-AS.
 * 02 Dec 2005     Neo Sok Lay         Change to use Java assertion instead of Util.assert.
 * 12 Mar 2007     Neo Sok Lay         Use UUID for unique filename.
 * 12 Nov 2007     Tam Wei Xiang       Add method 1) isCompressed()
 *                                                2) setContentIDForCompression(IPart)
 * 04 Jan 2008    Tam Wei Xiang       Added writeContentToStream(...) for write out the content of the Part.
 */

package com.gridnode.pdip.base.security.mime;


import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;

import javax.mail.internet.*;
import javax.mail.*;
import javax.activation.*;
import java.io.*;
import com.gridnode.pdip.framework.file.helpers.*;
import com.gridnode.pdip.framework.util.UUIDUtil;

/**
 * @author Lim Soon Hsiung
 * @version 1.1
 */
public class GNBodypart implements IPart //,IMimeExceptionValue
{
  private static final String CONTENT_DISPOSITION = "Content-Disposition";
  protected MimeBodyPart mbp;
  protected MimeMessage msg;

  private boolean isMultipart = false;
  private boolean isSigned = false;
  private boolean isEncrypted = false;
  private boolean isCompressed = false;

  //private boolean generateContentID = false;
  private boolean readOnly = false;

  public GNBodypart(MimeMultipart multipart) throws SecurityServiceException
  {
    try
    {
      createEmptyBodyPart();
      setContent(multipart);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to create GNBodypart from MimeMultipart",ex);
//      GNException.throwEx(EX_CREATE_MIME,
//                          "Unable to create GNBodypart from MimeMultipart",
//                          ex);
    }
  }

  public GNBodypart(MimeBodyPart bodypart)
  {
    this.mbp = bodypart;

 /*    try
    {
      String contentID = mbp.getHeader("Content-ID", null);
      if(contentID == null)
        setContentID();
    }
    catch (Exception ex)
    {
    }
 */
  }

  public GNBodypart(String pathname, String contentType, String encoding) throws SecurityServiceException
  {
    this(new File(pathname), contentType, encoding);
  }

  public GNBodypart(File content, String contentType, String encoding) throws SecurityServiceException
  {
    try
    {
      createEmptyBodyPart();
      setContent(content, contentType);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to create GNBodypart from file: " +
                              content + ", Content-Type=" + contentType,ex);

//      GNException.throwEx(EX_CREATE_MIME,
//                          "Unable to create GNBodypart from file: " +
//                              content + ", Content-Type=" + contentType,
//                          ex);
    }
  }

  public GNBodypart()
  {
    createEmptyBodyPart();
  }

  public GNBodypart(byte[] content, String contentType) throws SecurityServiceException
  {
    try
    {
      createEmptyBodyPart();
      setContent(createFile(content), contentType);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to create GNBodypart from byte array: Content-Type="
                                          +contentType,ex);

//      GNException.throwEx(EX_CREATE_MIME,
//                          "Unable to create GNBodypart from byte array: Content-Type=" +
//                            contentType,
//                          ex);
    }
  }

  public BodyPart getBodyPart()
  {
    return mbp;
  }

  public String getContentString()
  {
    String str = null;
    try
    {
      str = getByteArrayOutputStream().toString();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return str;
  }
  public byte[] getContentByte() throws SecurityServiceException
  {
    return getContentByte(true);
  }

  public byte[] getContentByte(boolean withMsgHeader) throws SecurityServiceException
  {
    byte[] b = null;
    try
    {
      b = getByteArrayOutputStream(withMsgHeader).toByteArray();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return b;
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

  public File getContentFile(File file) throws SecurityServiceException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      writeToStream(fos);

    }
    catch (SecurityServiceException gnEx)
    {
      throw gnEx;
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException(ex);
//      GNException.throwEx(EX_GENERAL,
//                          ex);
    }
    return file;
  }

  public File getContentFile(String filename) throws SecurityServiceException
  {
    return getContentFile(new File(filename));
  }

  public String getDescription()
  {
    String description = null;
    try
    {
      description = mbp.getDescription();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return description;
  }

  public String getContentType()
  {
    String contentType = null;
    try
    {
      contentType = mbp.getContentType();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return contentType;
  }

  public void setDescription(String description) throws SecurityServiceException
  {
    try
    {
      mbp.setDescription(description);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to set description: " + description,ex);
//      GNException.throwEx(EX_GENERAL,
//                          "Unable to set description: " + description,
//                          ex);
    }

  }

  public void setContentType(String contentType) throws SecurityServiceException
  {
    if(contentType == null)
      return;
    try
    {
      mbp.removeHeader("Content-Type");
      mbp.setHeader("Content-Type", contentType);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to set Content-Type: " + contentType,ex);
//      GNException.throwEx(EX_GENERAL,
//                          "Unable to set Content-Type: " + contentType,
//                          ex);
    }

  }

  public String[] getHeader(String name) throws SecurityServiceException
  {
    String[] header = null;
    try
    {
      header = mbp.getHeader(name);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to get header values for " + name,ex);
    }

    return header;
  }
  
  public void writeContentToStream(OutputStream os) throws SecurityServiceException
  {
	  try
	  {
		  mbp.writeTo(os);
	  }
	  catch(Exception ex)
	  {
		  throw new SecurityServiceException("Unable to write body part content to output stream.", ex);
	  }
  }
  
  public void writeToStream(OutputStream os) throws SecurityServiceException
  {
    writeToStream(os, true);
  }

  public void writeToStream(OutputStream os, boolean withMsgHeader)
    throws SecurityServiceException
  {
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
        MimeMultipart mmp = new MimeMultipart();
        mmp.addBodyPart(mbp);
        msg.setContent(mmp );
        msg.saveChanges();
      }
      msg.writeTo(os, ignoreList);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Wrtie to stream error",ex);
//      GNException.throwEx(EX_GENERAL,
//                          "Wrtie to stream error",
//                          ex);
    }
  }

  void setContent(Multipart content) throws SecurityServiceException
  {
    try
    {
      isMultipart = true;
      mbp.setContent(content);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to set content into bodypart",ex);
//      GNException.throwEx(EX_GENERAL,
//                          "Unable to set content into bodypart",
//                          ex);
    }
  }

  public void setContent(IMime content) throws SecurityServiceException
  {
    setContent(content.getMultipart());
  }

  public void setContent(Object content, String contentType)
  {
    try
    {
      if(content instanceof File)
      {
        FileDataSource fds = new FileDataSource((File)content);

        mbp.setDataHandler(new DataHandler(fds));
        setContentType(contentType);
      }
      else if(content instanceof byte[])
      {
        FileDataSource fds = new FileDataSource(createFile((byte[])content));

        mbp.setDataHandler(new DataHandler(fds));
        setContentType(contentType);
      }
      else
      mbp.setContent(content, contentType);
//       mbp.setContent(content, "text/plain");
      checkIsMultipart();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

  }

  public void setText(String text)
  {
    try
    {
      mbp.setText(text);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }


  /*
  private InputStream getInputStream(File content, String encoding)
    throws SecurityServiceException
  {
    FileInputStream is = null;

    try
    {
      is = new FileInputStream(content);
//      if(encoding != null && !"".equals(encoding))
//        os = MimeUtility.encode(os, encoding);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Error in getting file input stream from file: " +content,ex);
//      GNException.throwEx(EX_UNKNOWN, "Error in getting file input stream from file: " +
//                          content,
//                          ex);
    }

    return is;
  }*/

  public void addHeader(String headerName, String headerValue)
    throws SecurityServiceException
  {
    try
    {
      mbp.addHeader(headerName, headerValue);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Error in setting header: " + headerName + "=" + headerValue,ex);
//      GNException.throwEx(EX_UNKNOWN,
//                          "Error in setting header: " + headerName + "=" + headerValue,
//                          ex);
    }

  }

  public IMime getMultipart() throws SecurityServiceException
  {
    if(!isMultipart())
    {
      throw new SecurityServiceException("Not a multipart body");
    }
    GNMimepart multip = null;
    try
    {
      //multip = new GNMimepart( (MimeMultipart)mbp.getContent());
      multip = new GNMimepart(mbp);
      multip.setReadOnly(readOnly);
    }
    catch (SecurityServiceException gnEx)
    {
      throw gnEx;
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("error in getting multipart",ex);
    }

    return multip;

  }

  public boolean isMultipart()
  {
    return checkIsMultipart();
  }


  boolean checkIsMultipart()
  {
    try
    {
      isMultipart = mbp.isMimeType("multipart/*");

    }
    catch (Exception ex)
    {
      isMultipart = false;
      ex.printStackTrace();
    }
    return isMultipart;
  }

  public boolean isSigned()
  {
    try
    {
      isSigned = mbp.isMimeType("multipart/signed");
    }
    catch (Exception ex)
    {
      isSigned = false;
      ex.printStackTrace();
    }
    return isSigned;
  }

  public boolean isEncrypted()
  {
    try
    {
      isEncrypted = mbp.isMimeType("application/pkcs7-mime");
    }
    catch (Exception ex)
    {
      isEncrypted = false;
      ex.printStackTrace();
    }
    return isEncrypted;
  }
  
  //TWX 12 Nov 2007
  public boolean isCompressed()
  {
	try
	{
	  String contentType = mbp.getContentType();
	  if(contentType == null)
	  {
		  isCompressed = false;
	  }
	  else
	  {
		  ContentType cType = new ContentType(contentType);
		  String smimeTypeValue = cType.getParameter("smime-type");
		  
		  if(smimeTypeValue != null && smimeTypeValue.equalsIgnoreCase("compressed-data"))
		  {
			  isCompressed = true;
		  }
		  else
		  {
			  isCompressed = false;
		  }
	  }
	}
	catch(Exception ex)
	{
	  isCompressed = false;
	  ex.printStackTrace();
	}
	return isCompressed;
  }
  
  private File createFile(byte[] data)
  {
    File file = null;
    FileOutputStream fo = null;
    try
    {
      file = File.createTempFile("~mail"+UUIDUtil.getRandomUUIDInStr(), null);
      file.deleteOnExit();
      fo = new FileOutputStream(file);
      fo.write(data);
      fo.close();
  //    System.out.println("Temp File: " + file.getAbsolutePath());

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return file;
  }

  public Object getContent() throws SecurityServiceException
  {
    return getContent(OUTPUT_STRING);
  }

  public Object getContent(int outputType) throws SecurityServiceException
  {
    Object obj = null;
    try
    {
      obj = mbp.getContent();
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to get content from bodypart",ex);
//      GNException.throwEx(EX_GENERAL,
//                          "Unable to get content from bodypart",
//                          ex);
    }

    return GNMimeUtility.convertContent(obj, outputType);
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
  public boolean isMimeType(String mimeType) throws SecurityServiceException
  {
    boolean b = false;
    try
    {
      b = mbp.isMimeType(mimeType);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to determime mime-type: " + mimeType,ex);
//      GNException.throwEx(EX_GENERAL,
//                          "Unable to determime mime-type: " + mimeType,
//                          ex);
    }

    return b;
  }

  public synchronized void setParameter(String name, String value) throws SecurityServiceException
  {
    try
    {
      ContentType contenttype = new ContentType(mbp.getContentType());
      contenttype.setParameter(name, value);
      setContentType(contenttype.toString());
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to set ContentType parameter! : [" + name
                          + "=" + value + "]",ex);
//      GNException.throwEx(EX_GENERAL,
//                          "Unable to set ContentType parameter! : [" +
//                              name + "=" + value + "]",
//                          ex);
    }

  }

  public synchronized void removeParameter(String name) throws SecurityServiceException
  {
    try
    {
      ContentType contenttype = new ContentType(mbp.getContentType());
      ParameterList pList = contenttype.getParameterList();
      pList.remove(name);
      String contentType = (new ContentType(contenttype.getPrimaryType(), contenttype.getSubType(), pList)).toString();
      setContentType(contentType);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to remove ContentType parameter! parameter name=" + name,ex);
//      GNException.throwEx(EX_GENERAL,
//                          "Unable to remove ContentType parameter! parameter name=" + name,
//                          ex);
    }
  }

  public void setAttachement(String filename) throws SecurityServiceException
  {
    setAttachement(new File(filename));
  }

  public void setAttachement(File file) throws SecurityServiceException
  {
    setContent(file, GNMimeUtility.findContentTypeForXtension(file));
    setParameter("name", file.getName());
    setFilename(file.getName());
  }

  public File getAttachement() throws SecurityServiceException
  {
    return getAttachement(new File(getFilename()));
  }

  public File getAttachement(String targetFilename) throws SecurityServiceException
  {
    return getAttachement(new File(targetFilename));
  }

  public File getAttachement(File targetFile) throws SecurityServiceException
  {
    byte[] content = (byte[])getContent(OUTPUT_BYTE_ARRAY);
    try
    {
      FileOutputStream fos = new FileOutputStream(targetFile);
      fos.write(content);
      fos.close();
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to get attachement out of bodypart", ex);
//      GNException.throwEx(EX_GENERAL,"Unable to get attachement out of bodypart", ex);
    }

    return targetFile;
  }

  public File getAttachement(File parent, String child) throws SecurityServiceException
  {
    return getAttachement(new File(parent, child));
  }

  public void setFilename(String filename)
  {
    try
    {
      mbp.setFileName(filename);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public String getFilename()
  {
    String filename = null;
    try
    {
      filename = mbp.getFileName();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return filename;
  }

/*  public String getFilename()
  {
    String filename = null;
    try
    {
      String[] headers = mbp.getHeader("Content-Disposition");
      String hdr = null;
      int index = -1;
      for (int i = 0; i < headers.length; i++)
      {
        index = headers[i].indexOf("filename=");
        if(index != -1)
        {
          hdr = headers[i].substring(index + "filename=".length());
          System.out.println("Filename Header 1 : [" + hdr + "]");
          index = hdr.indexOf(";");
          if(index != -1)
          {
            hdr = hdr.substring(0, index);
            System.out.println("Filename Header 2 : [" + hdr + "]");
          }
          filename = parseQuote(hdr);
          System.out.println("Final filename [" + filename + "]");
          break;
        }
      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return filename;
  }
*/

  public void setContentID()
  {
    try
    {
      mbp.removeHeader("Content-ID");
      mbp.setHeader("Content-ID", "<" +
            GNMimeUtility.getUniqueMessageIDValue() +
            ">");
    }
    catch (Exception ex)
    {
    }
  }
  
  /**
   * TWX 12 NOV 2007: For the RNIF compression, the content ID for the SMIME  
   * that contain the Compressed Content will be the content ID of the decompressed
   * data with additional "--z".
   * 
   * @param decompressedPart The part that is in decompressed format
   */
  public void setContentIDForCompression(IPart decompressedPart) throws SecurityServiceException
  {
	  try
	  {
		  String[] contentIDArr = decompressedPart.getHeader("Content-ID");
		  String contentID;
		  if(contentIDArr == null)
		  {
			  throw new SecurityServiceException("Can't find the ContentID from the decompressed mime part");
		  }
		  else
		  {
			  contentID = contentIDArr[0];
			  if(contentID.indexOf("<") == 0)
			  {
				  contentID = contentID.substring(1);
			  }
			  
			  if(contentID.indexOf(">") == (contentID.length() - 1))
			  {
				  contentID = contentID.substring(0, (contentID.length() - 1));
			  }
			  
			  contentID += "--z";
			  mbp.setHeader("Content-ID", "<" +contentID +">");
		  }
	  }
	  catch(Exception ex)
	  {
		  throw new SecurityServiceException("Unable to set Content-ID for compression mime part", ex);
	  }
  }
  
  private void createEmptyBodyPart()
  {
    mbp = new MimeBodyPart();
    setContentID();
  }
  /*
  private String parseQuote(String string)
  {
    string = string.trim();
    if(string.startsWith("\""))
    {
      string = string.substring(1);
    }

    if(string.endsWith("\""))
    {
      string = string.substring(0, string.length() - 1);
    }

    return string.trim();
  }*/
  /*
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

    System.out.println("Bodypart content: \n" + text);
    System.out.println("Bodypart end---------------------------------");

    System.out.println("****************** Byte test");
    String myData = "This is a byte[] test";
//    GNBodypart bd = new GNBodypart(myData.getBytes(), "text/plain");
    GNBodypart bd = new GNBodypart();
    bd.setContent(myData.getBytes(), "application/xml");
    text = bd.getContentString();

    System.out.println("Bodypart content: \n" + text);
    System.out.println("Bodypart end---------------------------------");
    text = (String)bd.getContent();
    System.out.println("Bodypart converted content: \n" + text);
    System.out.println("Bodypart end---------------------------------");
    byte[] bbbb = (byte[])bd.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
    text = new String(bbbb);
    System.out.println("Bodypart converted content: \n" + text);
    System.out.println("Bodypart end---------------------------------");

    bbbb = bd.getContentByte(false);
    text = new String(bbbb);
    System.out.println("Bodypart strip converted content: \n" + text);
    System.out.println("Bodypart end---------------------------------");

    GNBodypart bp2 = new GNBodypart();

    GNMimepart mmp = new GNMimepart();
    mmp.addPart(bd);
    mmp.addPart(bp);
    bp2.setContent(mmp);
    bbbb = bp2.getContentByte(false);
    text = new String(bbbb);
    System.out.println("Bodypart strip converted content for multipart content: \n" + text);
    System.out.println("Bodypart end---------------------------------");
   
  }
  catch (Exception ex)
  {
     ex.printStackTrace();
   }
 }*/

  void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }

  public boolean isReadOnly()
  {
    return readOnly;
  }
  
  public void setAttachmentFileName(String filename) throws SecurityServiceException
  {
  	//Util.assert(filename != null, "filename is null");
  	assert (filename!=null) : "filename is null";
  	try
    {
      mbp.setHeader(CONTENT_DISPOSITION, "attachment; filename=" + filename);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException(
      	"Unable to set attachment filename in MIME header: " + filename, ex);
    }
  }
  
  public String getAttachmentFilename() throws SecurityServiceException
  {
  	Logger.debug("GNBodypart::getAttachmentFilename");
  	try
  	{
	  	String[] ar = mbp.getHeader(CONTENT_DISPOSITION);
	  	if (ar == null)
	  	{
	  		return null;
	  	}
	  	//Util.assert(ar.length == 1, "ar.length != 1");
	  	assert (ar.length == 1) : "ar.length != 1";
	  	
	  	String value = ar[0];
	  	Logger.debug("value = " + value);
	  	if (value == null)
	  	{
	  	  return null;
	  	}
	  	int index = value.indexOf('=');
	  	return value.substring(index + 1);
	} catch (Exception e)
	{
	  throw new SecurityServiceException(
      	"Unable to get attachment filename in MIME header", e);
	}
  }
}
