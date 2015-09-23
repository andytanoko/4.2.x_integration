/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNDePackager_20.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 21 2003      Guo Jianyu              Copied from the previous RNDePackager
 * Jan 29 2004      Neo Sok Lay             Remove file extension for prefix before 
 *                                          creating temp file.
 *                                          Pass null for suffix to File.createTempFile()
 *                                          to use the default ".tmp" extension.
 * Apr 26 2004      Neo Sok Lay             GNDB00021906: To Read elements 
 *                                          using namespace.
 * Nov 09 2005	    Tam Wei Xiang	    Modified the method saveAttachment()                                         
 * Dec 12 2005      Tam Wei Xiang           Modified method: validateParts, unpackFile
 * May 03 2006      Neo Sok Lay             Always set IsRequestMsg to false for signal doc  
 * Mar 12 2007      Neo Sok Lay             Use UUID for unique filename. 
 * Apr 27 2007      Neo Sok Lay             GNDB00028338: get envelope content bytes from bodypart directly                                        
 * Jul 09 2007      Neo Sok Lay             GNDB00028407: Set Action Identity Version Identifier from unpackaged service header.                                       
 * Nov 13 2007      Tam Wei Xiang           Support the RNIF de-compression 
 * Jul 08 2009      Tam Wei Xiang           #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib                             
 * Oct 05 2009      Tam Wei Xiang           #1053 Merge changes (handle optional field) from GT402X                                       
 */
package com.gridnode.pdip.base.rnif.helper;

import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.rnif.exception.ILogErrorCodes;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.mime.IMime;
import com.gridnode.pdip.base.security.mime.IPart;
import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;
import com.gridnode.pdip.base.rnif.model.IRNConstant_20;
import com.gridnode.pdip.base.xml.exceptions.XMLException;
import com.gridnode.pdip.base.xml.helpers.XMLServiceHandler;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.base.security.mime.*;
import com.gridnode.pdip.framework.config.*;
import com.gridnode.pdip.base.security.exceptions.*;
import com.gridnode.pdip.base.security.helpers.GridCertUtilities;

import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNNamespace;
import com.gridnode.xml.adapters.GNXMLDocumentUtility;
import com.gridnode.pdip.base.security.mime.*;

import java.io.*;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.mail.BodyPart;
import javax.mail.Header;


/**
 * The RNIF2.0 Unpackager
 *
 *
 * @version GT 2.1.21
 * @since 1.0
 */
public class RNDePackager_20 extends RNDePackager implements IRNConstant_20
{
	public static final String MAPPING_FILENAME = "";
	public static final String ATTACHMENT_PATH = "document.path.attachment";
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7638520696103183133L;

	public RNDePackager_20()
  {
    try
    {
      /*040426NSL
      IXMLServiceLocalHome homeObj = (IXMLServiceLocalHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(IXMLServiceLocalHome.class);
      IXMLServiceLocalObj xmlService = homeObj.create();
      namespace = xmlService.newNamespace("rn2","http://www.rosettanet.org/RNIF/V02.00");
      */
      namespace = XMLServiceHandler.getInstance().newNamespace("","http://www.rosettanet.org/RNIF/V02.00");
    }
    catch(Exception e)
    {
      Logger.error(ILogErrorCodes.RNIF_2_0_DEPACKAGER_INITIALIZE,"[RNDePackager_20.init]Error initializing RNIF_NAMESPACE: "+e.getMessage(), e);
    }
  }


  protected File[] unpackFile(File rnfile, String udocfilename, RNPackInfo packInfo)
    throws RosettaNetException, SecurityServiceException
  {
    SMimeFactory factory= SMimeFactory.newInstance(null, null);
    IMime signedenvelope= null;
    Logger.debug("[RNDepackager_20.unpackFile] rnfile = "+rnfile.getAbsolutePath());
    signedenvelope= factory.generateMime(rnfile);
    boolean isSigned= false;
    IMime envelope= null;
    //IMime envelope2GenDigest = null;
    BodyPart envelope2GenDigest = null; //NSL20070427 

    if (signedenvelope.isSigned())
    {
      IPart part= signedenvelope.getPart(0);
      if (!part.isMultipart())
        throw RosettaNetException.unpMesgSignErr(
          "signedenvelope's part(0) must be a multipart mime!");
      envelope= part.getMultipart();
      //envelope2GenDigest = envelope;
      envelope2GenDigest = part.getBodyPart(); //NSL20070427
      isSigned= true;
    }
    else
    {
      envelope= signedenvelope;
    }

    if (envelope.getPartCount() <= 0)
    {
      throw RosettaNetException.unpMesgGenErr("Cannot read RosettaNet Message");
    }
    IPart part;
    //String dtd; 

    // Read preamble
    part= envelope.getPart(0);
    readPreamble(packInfo, part);

    // Read delivery header
    part= envelope.getPart(1);
    readDeliveryHeader(packInfo, part);

    String senderDUNS= packInfo.getSenderGlobalBusIdentifier();
    RNCertInfo securityInfo= new RNCertInfo();
    try
    {
      if (_infoFinder != null)
      {
        securityInfo= _infoFinder.getSecurityInfoFromDUNS(senderDUNS);
      }
    }
    catch (Exception ex)
    {
      Logger.warn(ex);
      //throw RosettaNetException.unpMesgGenErr("Error in finding Security info based on senderDUNS:" + senderDUNS );
    }
    
    X509Certificate ownCert = getOwnEncryptCert(securityInfo);
    X509Certificate partnerCert = getPartnerSignCert(securityInfo);
    PrivateKey privateKey = getOwnPrivateKey(securityInfo);
    factory=  SMimeFactory.newInstance(ownCert, partnerCert);
    factory.setPrivateKey(privateKey);

    //Logger.debug("ownCert is " + ownCert.getSubjectName().toString());
    if (isSigned)
    {
      RosettaNetException sigVerificationException =null;
      try
      {
        Logger.debug("signedenvelope is" + new String(signedenvelope.getContentByte(true)));
        signedenvelope= factory.generateMime(rnfile);
        Logger.debug("second signedenvelope is" + new String(signedenvelope.getContentByte(true)));
        envelope= ((IPart) factory.verify(signedenvelope)).getMultipart();
      }
      catch (SecurityServiceException ex)
      {
        Logger.warn(ex);
        X509Certificate pendingCert = getPendingCert(securityInfo.get_partnerSignCertificate());
        if (pendingCert == null)
        {
          Logger.debug("No pending cert found.");
          //throw RosettaNetException.unpMesgSignErr("Message verification failed." + ex.getMessage());
          sigVerificationException = RosettaNetException.unpMesgSignErr("Message verification failed." + ex.getMessage());
          envelope= signedenvelope.getPart(0).getMultipart();
        }
        else
        {
          Logger.debug("Pending cert found. Try verifying with pending cert...");
          try
          {
            factory.setPartnerCert(pendingCert);
            envelope = ((IPart) factory.verify(signedenvelope)).getMultipart();
          }
          catch(SecurityServiceException ex2)
          {
            Logger.warn(ex2);
            //throw RosettaNetException.unpMesgSignErr("Message verification failed." + ex2.getMessage());
            sigVerificationException = RosettaNetException.unpMesgSignErr("Message verification failed." + ex2.getMessage());
            envelope= signedenvelope.getPart(0).getMultipart();
          }
        }
      }
      packInfo.setSigVerificationException(sigVerificationException);
    }

    // read service header and service content
    IPart sheader, scontent;
    ArrayList attIPartList= new ArrayList();
    int count= envelope.getPartCount();
    if (count == 3)
    {
      // decrypt
      part= envelope.getPart(2);
      if (part.isEncrypted() && !part.isCompressed()) //TWX: the way we check whether the scontent is encrypted is based
                                                      //     on the existing of param "application/pkcs7-mime; however such
                                                      //     a param also exist in pure compressed rnif msg. so enforce
                                                      //     additional check to prevent unecessary decrypt.
      {
        //Logger.debug("Service content is encrypted. (3 parts)");
        IMime payloadcontainer = null;
        try
        {
          payloadcontainer = (IMime) factory.decrypt(part);
        }
        catch(Exception e)
        {
          Logger.warn("Decrypt failed", e);
          X509Certificate pendingCert = getPendingCert(securityInfo.get_ownEncryptCertificate());
          if (pendingCert == null)
          {
            Logger.debug("No pending cert found.");
            throw RosettaNetException.unpMesgDcryptErr("Error decrypting: " + e.getMessage());
          }
          else
          {
            Logger.debug("Pending cert found. Try decrypting with pending cert...");
            factory.setOwnCert(pendingCert);
            payloadcontainer = (IMime) factory.decrypt(part);
          }
        }
        
        sheader= payloadcontainer.getPart(0);
        scontent= payloadcontainer.getPart(1);
        
        Logger.debug("Decompressing service content?");
        scontent = decompress(scontent, factory);
        
        // Koh Han Sing 20020503
        int iPartCount= payloadcontainer.getPartCount();
        if (iPartCount > 2)
        {
          for (int i= 2; i < iPartCount; i++)
          {
        	//MARK
        	Logger.debug("Decompressing attachment?");
            attIPartList.add(decompress(payloadcontainer.getPart(i), factory));
          }
        }
      }
      else
      {
        sheader= null;
        scontent= null;
        throw RosettaNetException.unpMesgDcryptErr("Expected an encrypted message.");
        // alternative is to proceed without encryption
      }
    }
    else //must be 4 parts
      {
      sheader= envelope.getPart(2);
      scontent= envelope.getPart(3);
      
      if (scontent.isEncrypted() && ! scontent.isCompressed()) //TWX: the way we check whether the scontent is encrypted is based
    	                                                       //     on the existing of param "application/pkcs7-mime; however such
    	                                                       //     a param also exist in pure compressed rnif msg. so enforce
    	                                                       //     additional check to prevent unecessary decrypt.
      {
        //Logger.debug("Service content is encrypted. (4 parts)");
        IMime payloadcontainer = null;
        try
        {
          payloadcontainer = (IMime) factory.decrypt(scontent);
        }
        catch(Exception e)
        {
          Logger.warn("Decrypt failed", e);
          X509Certificate pendingCert = getPendingCert(securityInfo.get_ownEncryptCertificate());
          if (pendingCert == null)
          {
            Logger.debug("No pending cert found.");
            throw RosettaNetException.unpMesgDcryptErr("Error decrypting: " + e.getMessage());
          }
          else
          {
            Logger.debug("Pending cert found. Try decrypting with pending cert...");
            factory.setOwnCert(pendingCert);
            payloadcontainer = (IMime) factory.decrypt(scontent);
          }
        }
        
        Logger.debug("Decompressing service content?");
        scontent= decompress(payloadcontainer.getPart(0), factory);
        // Koh Han Sing 20020503
        int iPartCount= payloadcontainer.getPartCount();
        if (iPartCount > 1)
        {
          for (int i= 1; i < iPartCount; i++)
          {
        	Logger.debug("Decompressing attachment?");
            attIPartList.add(decompress(payloadcontainer.getPart(i), factory));
          }
        }
      }
      else
      {
        //Logger.debug("Service content not encrypted. (4 parts)");
        for (int i= 4; i < count; i++)
        {
          Logger.debug("Decompressing attachment?");
          attIPartList.add(decompress(envelope.getPart(i), factory));
        }
        Logger.debug("Decompressing service content?");
        scontent = decompress(scontent, factory);
      }
    }

    /* save attachments */
    File[] fileToReturnArray = null;
    try
    {
    	ArrayList filenameList = saveAttachment(attIPartList, udocfilename);
    	fileToReturnArray = getAttachmentFiles(filenameList);
    } catch (SecurityServiceException e)
    {
    	Logger.warn(e);
    } catch (FileAccessException e)
    {
    	Logger.warn(e);
    }
    
    // Read service header
    readServiceHeader(packInfo, sheader);

    // Attachment / manifest check is performed in the ValidateDocAction

    // Service Content - this is either the second part of a payload
    // container, or the fourth part of the normal envelope. The fourth
    // part may also be encrypted
    if (!SERVICE_CONTENT_CL.equals(scontent.getHeader(CONTENT_LOC)[0]))
    {
      throw RosettaNetException.unpSconReadErr(
        "Invalid Content-location header for Service Content");
    }

    File fileToReturn1 = null;
    try
    {
    	//TWX:  Ouput_Byte_Array instead of Output_InputStream
    	byte[] input = (byte[])scontent.getContent(IPart.OUTPUT_BYTE_ARRAY);
    	
    	com.gridnode.xml.adapters.GNDocument gdoc =
        GNXMLDocumentUtility.getDocument(new ByteArrayInputStream(input));
//      String contentstr= (String) scontent.getContent();
      //byte[] contentByte = scontent.getContentByte(false);
      //File tmpfile= File.createTempFile(udocfilename, null);

      // TODO need to move this implementation to the XMLUtil.writeXmlFile(...)
      File tmpfile= File.createTempFile(FileUtil.removeExtension(udocfilename), ".xml");
      udocfilename= tmpfile.getAbsolutePath();
      GNXMLDocumentUtility.writeToFile(gdoc, udocfilename, true, false);

      Logger.debug("wrote service content to [" + udocfilename + "]");
      fileToReturn1 = tmpfile;
    }
    catch (Exception ex)
    {
      throw RosettaNetException.unpMesgGenErr("Cannot write service content.");
    }
    
    int size = 2 + fileToReturnArray.length;
    File[] res = new File[size];
    res[1] = fileToReturn1;
    for (int i = 0; i < fileToReturnArray.length; i++)
    {
    	res[2 + i] = fileToReturnArray[i];
    }
    
    /* debug */
    Logger.debug("RNDePackager_20::unpackFile::print res");
    Logger.debug("res.length = " + res.length);
    for (int i = 0; i < res.length; i++)
    {
    	File file = res[i];
    	Logger.debug((file == null) ? null : file.getPath());
    }
//    /* add attachments */
//    for (int i = 0; i < filenameList.size(); i++)
//    {
//    	files.add(filenameList.get(i));
//    }
//    
//    File[] res= new File[files.size() + 1];
//    for (int i= 0; i < files.size(); i++)
//    {
//      File element= (File) files.get(i);
//      res[i + 1]= element;
//    }

    if (isSigned && !packInfo.getIsSignalDoc())
    {
      String digestAlg= factory.getExternalDigestAlgo(_infoFinder.getDigestAlgorithm(packInfo));
      
      DigestGenerator dg= new DigestGenerator();
      //byte[] envelopeContent= envelope2GenDigest.getContentByte(false);
      
      try
      {
        //NSL20070427 Get content directly from bodypart
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        envelope2GenDigest.writeTo(bos);
        byte[] envelopeContent = bos.toByteArray();
        
        String digest= dg.getEncodedDigest(envelopeContent, digestAlg);
        packInfo.setMsgDigest(digest);
        Logger.debug("Generated digest is [" + digest + "]");
      }
      catch (Exception ex)
      {
        Logger.warn("Unable to generate digest", ex);
        throw RosettaNetException.unpMesgGenErr("Cannot compute digest: "+ex.getLocalizedMessage());
      }
    }

    return res;
  }
  
  //20090708 TWX retrieve the own private key
  private PrivateKey getOwnPrivateKey(RNCertInfo securityInfo)
  {
    Certificate ownCert = securityInfo.get_ownSignCertificate();
    if(ownCert == null)
    {
      return null;
    }
    String privateKeyInString = ownCert.getPrivateKey();
    return GridCertUtilities.loadPrivateKeyFromString(privateKeyInString);
  }


  //
  private IPart decompress(IPart part, SMimeFactory factory) throws SecurityServiceException, RosettaNetException
  {
	  if(part.isCompressed())
	  {
		
		IPart decompressPart;
		try
		{
			decompressPart = factory.deCompressPartContent(part);
		}
		catch(Exception ex)
		{
			throw RosettaNetException.unpSconUnCerr("Decompress failed: "+ex.getMessage());
		}
		
		return decompressPart;
	  }
	  else
	  {
	    //Logger.debug("Part is not compressed");
	    return part;
	  }
  }

  File validateParts(
    IPart part,
    String contentLocHeader,
    String filePrefix,
    String dtdname,
    String dicname,
    String schemaname,
    String readExName,
    String valExName)
    throws RosettaNetException
  {
  	
    String content= null;
    String tempfile= null;
    File tmpfile= null;
    try
    {
      if (!contentLocHeader.equals(part.getHeader(CONTENT_LOC)[0]))
      {
        throw new RosettaNetException(
          readExName,
          "Invalid Content-location header for " + filePrefix + ".");
      }

//    TWX:  Ouput_Byte_Array instead of Output_InputStream
      byte[] input  = (byte[])part.getContent(IPart.OUTPUT_BYTE_ARRAY);
      
      
      com.gridnode.xml.adapters.GNDocument gdoc
        = GNXMLDocumentUtility.getDocument(new ByteArrayInputStream(input));
      
      try
      {
        tmpfile= File.createTempFile(filePrefix+UUIDUtil.getRandomUUIDInStr(), null);
        tempfile= tmpfile.getAbsolutePath();
      }
      catch (IOException ex)
      {
        throw RosettaNetException.fileProcessErr("Cannot write to file.");
      }

      Logger.debug("Writing part to temp file " + tempfile);
      GNXMLDocumentUtility.writeToFile(gdoc, tempfile, true, false);
      FileInputStream fis = new FileInputStream(tmpfile);
      int fileSize = new Long(tmpfile.length()).intValue();
      byte[] tmpBuffer = new byte[fileSize];
      //int bytesRead = 
      fis.read(tmpBuffer);
      content = new String(tmpBuffer);
    }
    catch (SecurityServiceException ex)
    {
      throw new RosettaNetException(readExName, "Cannot read content for part " + filePrefix + ".");
    }
    catch(Exception e)
    {
      Logger.warn("Exception in getting content", e);
      throw new RosettaNetException(readExName, "Cannot read content for part " + filePrefix + ".");
    }

    String dtd;

    try
    {
      dtd= XMLUtil.extractDTDName(content);
    }
    catch (XMLException e)
    {
      throw new RosettaNetException(readExName, "Cannot extract DTD: " + e.getMessage());
    }
    catch (ServiceLookupException e)
    {
      throw new RosettaNetException(readExName, "Cannot extract DTD: " + e.getMessage());
    }

    if (null == dtd)
    {
      throw RosettaNetException.unpMesgGenErr("RosettaNet Message does not have a DTD.");
    }
    if (!dtdname.equals(dtd))
    {
      throw new RosettaNetException(readExName, "Preamble DTD is not valid.");
    }

    // Write the content to a temp file and validate it.
    String dtdpath= null;
    //    String dicpath = FileUtil.getFile(IRnifPathConfig.PATH_DICTIONARY, dicname).getAbsolutePath();
    String schemapath= null;
/*
    try
    {
      tmpfile= XMLUtil.createTempFile(filePrefix, content);
      tempfile= tmpfile.getAbsolutePath();

    }
    catch (IOException ex)
    {
      throw RosettaNetException.fileProcessErr("Cannot write to file.");
    }
*/
    try
    {
      dtdpath= FileUtil.getFile(IRnifPathConfig.PATH_DTD, dtdname).getAbsolutePath();
      //    dicpath = FileUtil.getFile(IRnifPathConfig.PATH_DICTIONARY, dicname).getAbsolutePath();
      schemapath= FileUtil.getFile(IRnifPathConfig.PATH_SCHEMA, schemaname).getAbsolutePath();
    }
    catch (FileAccessException ex)
    {
      throw new RosettaNetException(valExName, ex.getMessage());
    }
    Logger.debug(
      "RosettanetMessageConvertor.validateParts() - validating:\n"
        + "\tfile:"
        + tempfile
        + "\n\tdtd: "
        + dtdpath
        + "\n\tschema: "
        + schemapath
        + "\n");
    ArrayList errlist= new ArrayList();

    XMLUtil.validateDTD(tempfile, dtdpath, errlist);
    XMLUtil.validateSchema(tempfile, schemapath, dtdpath, errlist);

    if (errlist.isEmpty())
      return tmpfile;
    StringBuffer errbuf= new StringBuffer();
    Iterator i= errlist.iterator();
    while (i.hasNext())
    {
      errbuf.append((String) i.next());
      errbuf.append("\n");
    }
    throw new RosettaNetException(valExName, errbuf.toString());
    //return tmpfile;
  }

  /**
   * Reads a RosettaNet Preamble and updates the RNPackInfo.
   */
  void readPreamble(RNPackInfo doc, IPart preamble) throws RosettaNetException
  {
    validateParts(
      preamble,
      PREAMBLE_CL,
      PREAMBLE_FILE,
      PREAMBLE_DTD,
      PREAMBLE_DIC,
      PREAMBLE_XSD,
      RosettaNetException.UNP_PRMB_READERR,
      RosettaNetException.UNP_PRMB_VALERR);
  }

  /**
   * Reads a RosettaNet Delivery Header and updates the RNPackInfo.
   */
  void readDeliveryHeader(RNPackInfo profile, IPart dheader) throws RosettaNetException
  {
    File dheaderfile=
      validateParts(
        dheader,
        DELIVERY_HEADER_CL,
        DHEADER_FILE,
        DELIVERY_HEADER_DTD,
        DELIVERY_HEADER_DIC,
        DELIVERY_HEADER_XSD,
        RosettaNetException.UNP_DHDR_READERR,
        RosettaNetException.UNP_DHDR_VALERR);

    GNElement root= null;
    try
    {
      //root= XMLUtil.getRoot(dheaderfile);
      root = XMLUtil.getRoot(dheaderfile, DELIVERY_HEADER_DTD);
    }
    catch (Exception ex)
    {
      throw RosettaNetException.unpDhdrValErr(ex.toString());
    }
    checkElement(root, "root", RosettaNetException.UNP_DHDR_READERR);

    //newly a

    // messageReceiverIdentification
    Logger.debug("root name ="+ root.getName());
    List children = root.getChildren();
    Logger.debug("children size ="+ root.getChildren().size());
    for(int i = 0;i < children.size(); i++)
    {
      Logger.debug("child ="+ ((GNElement)children.get(i)).getName());

    }
    GNNamespace ns = root.getNamespace();
    Logger.debug("namespace ="+ ns);
    GNElement receiverid= getChild(root, "messageReceiverIdentification");
    checkElement(receiverid, "messageReceiverIdentification", RosettaNetException.UNP_DHDR_READERR);
    GNElement receiverpartner= getChild(receiverid, "PartnerIdentification");
    checkElement(
      receiverpartner,
      "messageReceiverIdentification.PartnerIdentification",
      RosettaNetException.UNP_DHDR_READERR);
    // messageSenderIdentification
    GNElement senderid= getChild(root, "messageSenderIdentification");
    checkElement(senderid, "messageSenderIdentification", RosettaNetException.UNP_DHDR_READERR);
    GNElement senderpartner= getChild(senderid, "PartnerIdentification");
    checkElement(
      senderpartner,
      "messageSenderIdentification.PartnerIdentification",
      RosettaNetException.UNP_DHDR_READERR);
    GNElement trackingid= getChild(root, "messageTrackingID");
    try
    {
      updatePackInfoOptionalField(
        profile,
        RNPackInfo.RECEIVER_DOMAIN,
        getChild(receiverpartner, "domain"), //NSL20070710
        "FreeFormText");
      updatePackInfoField(
        profile,
        RNPackInfo.RECEIVER_GLOBAL_BUS_IDENTIFIER,
        receiverpartner,
        "GlobalBusinessIdentifier");
      updatePackInfoOptionalField(
        profile,
        RNPackInfo.RECEIVER_LOCATION_ID,
        getChild(receiverpartner, "locationID"), //NSL20070710
        "Value");

      updatePackInfoOptionalField(
        profile,
        RNPackInfo.SENDER_DOMAIN,
        getChild(senderpartner, "domain"),
        "FreeFormText");
      updatePackInfoField(
        profile,
        RNPackInfo.SENDER_GLOBAL_BUS_IDENTIFIER, //NSL20070710
        senderpartner,
        "GlobalBusinessIdentifier");
      updatePackInfoOptionalField(
        profile,
        RNPackInfo.SENDER_LOCATION_ID,
        getChild(senderpartner, "locationID"), //NSL20070710
        "Value");
      updatePackInfoField(
        profile,
        RNPackInfo.DELIVERY_MESSAGE_TRACKING_ID,
        trackingid,
        "InstanceIdentifier");
    }
    catch (ValidationException ex)
    {
      throw RosettaNetException.unpDhdrValErr(ex.getMessage());
    }
    return;
  }

  /**
   * Reads a RosettaNet Service Header and updates the RNPackInfo.
   */
  void readServiceHeader(RNPackInfo packInfo, IPart sheader) throws RosettaNetException
  {

    File sheaderfile=
      validateParts(
        sheader,
        SERVICE_HEADER_CL,
        SHEADER_FILE,
        SERVICE_HEADER_DTD,
        SERVICE_HEADER_DIC,
        SERVICE_HEADER_XSD,
        RosettaNetException.UNP_SHDR_READERR,
        RosettaNetException.UNP_SHDR_VALERR);

    GNElement root= null;
    try
    {
      //root= XMLUtil.getRoot(sheaderfile);
      root= XMLUtil.getRoot(sheaderfile, SERVICE_HEADER_DTD);
    }
    catch (Exception ex)
    {
      throw RosettaNetException.unpShdrValErr(ex.getMessage());
    }

    checkElement(root, "root", RosettaNetException.UNP_SHDR_READERR);
    GNElement processControl= getChild(root, "ProcessControl");
    checkElement(processControl, "ProcessControl", RosettaNetException.UNP_SHDR_READERR);

    String versionIdentifier=
      getChild(getChild(processControl, "pipVersion"), 
        "VersionIdentifier").getText();
    Logger.debug("VersionIdentifier = [" + versionIdentifier + "]");

    String globalProcessIndicatorCode=
      getChild(getChild(processControl, "pipCode"),
        "GlobalProcessIndicatorCode").getText();
    Logger.debug("globalProcessIndicatorCode = [" + globalProcessIndicatorCode + "]");

    GNElement messageControl=
      getChild(getChild(processControl, "ActivityControl"),
        "MessageControl");
    checkElement(messageControl, "MessageControl", RosettaNetException.UNP_SHDR_READERR);
    GNElement inreplyto= getChild(messageControl, "inReplyTo");
    // inreplyto is an optional field.
    GNElement manifest= getChild(messageControl, "Manifest");
    checkElement(manifest, "MessageControl.Manifest", RosettaNetException.UNP_SHDR_READERR);
    GNElement initPartner= getChild(processControl, "KnownInitiatingPartner");
    if (null == initPartner)
    {
      initPartner= getChild(processControl, "UnknownInitiatingPartner");
    }
    if (null == initPartner)
    {
      throw RosettaNetException.unpShdrValErr(
        "Initiating partner Business identifier is not found");
    }

    try
    {
      updatePackInfoField(
        packInfo,
        RNPackInfo.PARTNER_GLOBAL_BUS_IDENTIFIER,
        getChild(initPartner, "PartnerIdentification"),
        "GlobalBusinessIdentifier");
      updatePackInfoField(
        packInfo,
        RNPackInfo.BUS_ACTIVITY_IDENTIFIER,
        getChild(processControl, "ActivityControl"),
        "BusinessActivityIdentifier");
      updatePackInfoField(
        packInfo,
        RNPackInfo.PIP_INSTANCE_IDENTIFIER,
        getChild(processControl, "pipInstanceId"),
        "InstanceIdentifier");
      updatePackInfoField(
        packInfo,
        RNPackInfo.FROM_GLOBAL_PARTNER_ROLE_CLASS_CODE,
        getChild(messageControl, "fromRole"),
        "GlobalPartnerRoleClassificationCode");
      updatePackInfoField(
        packInfo,
        RNPackInfo.FROM_GLOBAL_BUS_SERVICE_CODE,
        getChild(messageControl, "fromService"),
        "GlobalBusinessServiceCode");
      updatePackInfoField(
        packInfo,
        RNPackInfo.TO_GLOBAL_PARTNER_ROLE_CLASS_CODE,
        getChild(messageControl, "toRole"),
        "GlobalPartnerRoleClassificationCode");
      updatePackInfoField(
        packInfo,
        RNPackInfo.TO_GLOBAL_BUS_SERVICE_CODE,
        getChild(messageControl, "toService"),
        "GlobalBusinessServiceCode");
      updatePackInfoField(
        packInfo,
        RNPackInfo.GLOBAL_USAGE_CODE,
        processControl,
        "GlobalUsageCode");
      updatePackInfoField(
        packInfo,
        RNPackInfo.PIP_GLOBAL_PROCESS_CODE,
        getChild(processControl, "pipCode"),
        "GlobalProcessIndicatorCode");
      updatePackInfoField(
        packInfo,
        RNPackInfo.PIP_INSTANCE_IDENTIFIER,
        getChild(processControl, "pipInstanceId"),
        "InstanceIdentifier");
      updatePackInfoField(
        packInfo,
        RNPackInfo.PIP_VERSION_IDENTIFIER,
        getChild(processControl, "pipVersion"),
        "VersionIdentifier");
      if (null != inreplyto)
      {
        GNElement ractionIdentity = getChild(getChild(inreplyto, "ActionControl"), "ActionIdentity");
        updatePackInfoOptionalField(
          packInfo,
          RNPackInfo.IN_REPLY_TO_GLOBAL_BUS_ACTION_CODE,
          ractionIdentity,
          "GlobalBusinessActionCode");
        updatePackInfoOptionalField(
          packInfo,
          RNPackInfo.IN_REPLY_TO_VERSION_IDENTIFIER,
          getChild(ractionIdentity, "standardVersion"), //NSL20070709
          "VersionIdentifier");
        updatePackInfoOptionalField(
          packInfo,
          RNPackInfo.SERVICE_MESSAGE_TRACKING_ID,
          getChild(inreplyto, "messageTrackingID"),
          "InstanceIdentifier");
        packInfo.setIsRequestMsg(false);
      }
      GNElement svcContentControl= getChild(manifest, "ServiceContentControl");
      GNElement signalIdentity= getChild(svcContentControl, "SignalIdentity");
      GNElement actionIdentity= getChild(svcContentControl, "ActionIdentity");
      if (null != actionIdentity)
      {
        updatePackInfoField(
          packInfo,
          RNPackInfo.ACTION_IDENTITY_GLOBAL_BUS_ACTION_CODE,
          actionIdentity,
          "GlobalBusinessActionCode");
        
        //NSL20070709 Save Action Identity version identifier
        updatePackInfoOptionalField(
          packInfo,
          RNPackInfo.ACTION_IDENTITY_VERSION_IDENTIFIER,
          getChild(actionIdentity, "standardVersion"),
          "VersionIdentifier");
      }
      else
        if (null != signalIdentity)
        {
          updatePackInfoField(
            packInfo,
            RNPackInfo.SIGNAL_IDENTITY_GLOBAL_BUS_SIGNAL_CODE,
            signalIdentity,
            "GlobalBusinessSignalCode");
          updatePackInfoField(
            packInfo,
            RNPackInfo.SIGNAL_IDENTITY_VERSION_IDENTIFIER,
            signalIdentity,
            "VersionIdentifier");
          packInfo.setIsSignalDoc(true);
          packInfo.setIsRequestMsg(false); //NSL20060503 always set IsRequestMsg to false for signal doc
        }
        else
        {
          throw new ValidationException("ActionIdentity or SignalIdentity expected");
        }
      updatePackInfoAttachNum(
        packInfo,
        getChild(manifest, "numberOfAttachments"),
        "CountableAmount");
    }
    catch (ValidationException ex)
    {
      throw RosettaNetException.unpShdrValErr(ex.getMessage());
    }
  }

  void updatePackInfoAttachNum(RNPackInfo packInfo, GNElement parent, String elementName)
    throws ValidationException
  {
    if (null == parent)
      return;
    GNElement child= getChild(parent, elementName);
    if (null != child)
    {
      packInfo.setNumberOfAttas(Integer.parseInt(child.getText().trim()));
    }
  }

  private ArrayList saveAttachment(ArrayList ar, String udocfilename) throws SecurityServiceException
  {
	Logger.debug("RNDePackager_20::saveAttachment");
	
	ArrayList filenameList = new ArrayList();
	for (int i = 0; i < ar.size(); i++)
	{
		GNBodypart part = (GNBodypart) ar.get(i);
		String filename = null;
		filename = part.getAttachmentFilename();
		if (filename == null)
		{
			Logger.debug("filename == null");
			Logger.debug("udocfilename = " + udocfilename);
			filename = FileUtil.removeExtension(udocfilename) + "_attachment" + (i + 1) 
				+ getExtension(part);
		}
		Logger.debug(i + ": filename = " + filename);
		//filenameList.add(filename);
		
		//String str = (String) part.getContent();
		//byte[] bytes = str.getBytes();
		
		//TWX: The attachment may not be string, so we use byte instead
		byte[] bytes = (byte [])part.getContent(IPart.OUTPUT_BYTE_ARRAY);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try
		{
			filename = FileUtil.create(ATTACHMENT_PATH, filename, bais);
		} catch (FileAccessException e)
		{
			Logger.warn(e);
		}
		filenameList.add(filename);
	}
	Logger.debug("RNDePackager_20::saveAttachment ends");
	return filenameList;
  }
  
  /**
   * Each file must be attachment.
   * @return a array of File objects.
   */
  private File[] getAttachmentFiles(ArrayList filenameList) 
  		throws FileAccessException
  {
	  File[] ret = new File[filenameList.size()];
	  for (int i = 0; i < filenameList.size(); i++)
	  {
		  String filename = (String) filenameList.get(i);
		  ret[i] = FileUtil.getFile(ATTACHMENT_PATH, filename);
	  }
	  return ret;
  }
  
  private String getExtension(GNBodypart part)
  {
	  String contentType = part.getContentType();
	  Logger.debug("contentType = " + contentType);
	  Properties p = ConfigurationManager.getInstance().getConfig("contentType").getProperties();
	  String extension = (String) p.get(contentType);
	  String ret = (extension != null) ? "." + extension : ".att";
	  Logger.debug("ret = " + ret);
	  return ret;
  }
}
