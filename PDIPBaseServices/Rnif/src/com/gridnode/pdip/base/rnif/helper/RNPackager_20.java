/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNPackager_20.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 21 2003      Guo Jianyu              Copied from the previous RNPackager
 * Jul 23 2003      Guo Jianyu              Changed all "\n" to the canonical form of "\r\n"
 * Jan 29 2004      Neo Sok Lay             Remove file extension in prefix for
 *                                          File.createTempFile().
 * Apr 05 2004      Guo Jianyu              Bug fix: GNDB00017554. Avoided extra
 *                                          timezone conversion in makeDeliveryHeader()
 * Nov 09 2005      Lim Soon Hsiung         Fix XML encoding issue during canonicalize()
 * Jul 09 2007      Neo Sok Lay             GNDB00028407: 
 *                                          - handle writing optional tags: sender/receiverDomain, sender/receiverLocationID, etc
 *                                          - indent output headers nicely
 * Jul 16 2007      Neo Sok Lay             Write optional tags: actionIdentity/messageStandard/FreeFormText and 
 *                                          actionIdentity/standardVersion/VersionIdentifier if specified.                                         
 * Nov 14 2007      Tam Wei Xiang           Support RNIF compression.
 * Jul 28 2009      Tam Wei Xiang           #560 - modified packFiles2Mime(..) to set the private key
 * Aug 11 2009      Tam Wei Xiang           #842 - modified packFiles2Mime(..)
 * Oct 06 2009      Tam Wei Xiang           #1053 - Add optional field in DeliveryHeader and ServiceHeader
 */
package com.gridnode.pdip.base.rnif.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.base.rnif.model.IRNConstant_20;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.mime.GNBodypart;
import com.gridnode.pdip.base.security.mime.IMime;
import com.gridnode.pdip.base.security.mime.IPart;
import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

/**
 * The RNIF2.0 packager
 *
 *
 * @version 1.0
 * @since 1.0
 */
public class RNPackager_20 extends RNPackager
                           implements IRNConstant_20
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1193737220863137572L;

	public RNPackager_20()
  {
  }

  public File[][] packDoc(RNPackInfo packInfo, File[] payloads) throws RosettaNetException
  {
/*    File infoFile= payloads[0];
    RNPackInfo packInfo= new RNPackInfo();
    try
    {
      packInfo= (RNPackInfo) packInfo.deserialize(infoFile.getAbsolutePath());
      if (packinfoArray != null)
        packinfoArray[0]= packInfo;
    }
    catch (Exception ex)
    {
      Logger.err(ex);
      throw RosettaNetException.pkgMesgGenErr("Cannot read PackInfo file--" + ex.getMessage());
    }
*/
    File tmpauditfile= null;
    File createdAuditFile = null;
    try
    {
      IMime envelope= packFiles2Mime(payloads, packInfo);
      String auditfilename= packInfo.getUDocFileName();
      Logger.debug("Udoc File Name is " + auditfilename);
      //tmpauditfile= File.createTempFile(auditfilename + "_audit", ".xml");
      tmpauditfile= File.createTempFile(FileUtil.removeExtension(auditfilename)+"_audit", null);

      byte[] content= (byte[]) envelope.getContentByte(false); //(IPart.OUTPUT_BYTE_ARRAY);

      OutputStream fos= new FileOutputStream(tmpauditfile);
      fos.write(content);

      fos.close();

      Logger.debug("tmpauditfile=" + tmpauditfile.getAbsolutePath());
      auditfilename= XMLUtil.createFile(IRnifPathConfig.PATH_AUDIT, auditfilename, tmpauditfile);
      createdAuditFile=FileUtil.getFile(IRnifPathConfig.PATH_AUDIT,auditfilename);
      Logger.debug("Written to [" + auditfilename + "]");

    }
    catch (Exception ex)
    {
      Logger.warn(ex);
      throw RosettaNetException.pkgMesgGenErr(ex);
    }

    File[][] res= new File[][] {{ tmpauditfile },{createdAuditFile}};
    return res;
  }

  protected String replace(String orig, String oldStr, String newStr)
  {
    int start = 0;
    int pos = orig.indexOf(oldStr, start);
    int old_len = oldStr.length();
    int new_len = newStr.length();
    while (pos>=0)
    {
      orig = orig.substring(0, pos) + newStr + orig.substring(pos + old_len);
      start = pos + new_len;
      pos = orig.indexOf(oldStr, start);
    }
    return orig;
  }

  protected byte[] canonicalize(File file) throws RosettaNetException
  {
    try
    {
      FileInputStream fis = new FileInputStream(file);
      byte[] contentBytes = new byte[new Long(file.length()).intValue()];
      fis.read(contentBytes);
      fis.close();
      // LSH:09Nov2005
//      String contentStr = new String(contentBytes);
      String encoding = XMLUtil.getXmlEncoding(contentBytes);
      String contentStr = XMLUtil.getXmlString(contentBytes, encoding);
      StringTokenizer tokenizer = new StringTokenizer(contentStr, "\r\n");
      StringBuffer result = new StringBuffer();
      while (tokenizer.hasMoreTokens())
      {
        String nextToken = tokenizer.nextToken();
        nextToken = replace(nextToken, "\r", "\r\n");
        nextToken = replace(nextToken, "\n", "\r\n");
        result.append(nextToken);
        result.append("\r\n");
      }

      return result.toString().getBytes(encoding);
    }
    catch(Throwable e)
    {
      Logger.warn("Error canonicalizing", e);
      throw RosettaNetException.pkgMesgGenErr("Error canonicalizing service contents: "
        + e.getMessage());
    }
  }

  protected IMime packFiles2Mime(File[] payloads, RNPackInfo packInfo)
    throws SecurityServiceException, RosettaNetException
  {
    boolean doCompress = packInfo.getIsCompressRequired(); //TWX 09112007
    boolean doSign= packInfo.getIsEnableSignature();
    boolean doEncrypt= packInfo.getIsEnableEncryption();
    //boolean doAudit= true;
    boolean encryptSvcHeader= !packInfo.getIsOnlyEncryptPayload();

    String digestAlg= packInfo.getDigestAlgorithm();
    String encryptAlg= packInfo.getEncryptionAlgorithm();
    int encryptAlgLength= packInfo.getEncryptionAlgorithmLength();

    String recipientDUNS= packInfo.getReceiverGlobalBusIdentifier();
    RNCertInfo securityInfo= new RNCertInfo();
    SMimeFactory factory= null;
    try
    {
      if (_infoFinder != null && (doSign || doEncrypt))
      {
        securityInfo= _infoFinder.getSecurityInfoFromDUNS(recipientDUNS);
        factory=
          SMimeFactory.newInstance(
            getOwnSignCert(securityInfo), getPartnerEncryptCert(securityInfo));

        //20090728 TWX, #560 set the private key for signing purpose
        factory.setPrivateKey(GridCertUtilities.loadPrivateKeyFromString(securityInfo.get_ownSignCertificate().getPrivateKey()));
      }
      else
      {
        factory= SMimeFactory.newInstance(null, null);
      }
    }
    catch (Exception ex)
    {
      Logger.warn(ex);
      throw RosettaNetException.unpMesgGenErr(
        "Error in finding Security info based on recipientDUNS:" + recipientDUNS);
    }

    factory.setDigestAlgorithm(digestAlg);
    factory.setEncryptionAlgorithm(encryptAlg);
    factory.setEncryptionLevel(encryptAlgLength);

    //@@todo SMimeMgr.getSMimeFactory(mypartner);
    IMime envelope= factory.createMime();
    // IMime payloadcontainer = factory.createMime();
    // create the Preamble
    IPart preamble= factory.createPart(this.makePreamble(packInfo), PART_CONTENT_TYPE);
    // preamble.setContentID();
    preamble.addHeader(CONTENT_LOC, PREAMBLE_CL);
    envelope.addPart(preamble);
    // create the Delivery Header
    IPart dheader= factory.createPart(this.makeDeliveryHeader(packInfo), PART_CONTENT_TYPE);
    dheader.addHeader(CONTENT_LOC, DELIVERY_HEADER_CL);
    //    dheader.setContentID();
    envelope.addPart(dheader);

    // get the Payload
    File udocfile= (File) payloads[UDOC_INDEX];
    if (!udocfile.exists())
    {
      RosettaNetException.pkgMesgGenErr("Cannot find UDoc file");
    }

    // temporary measure to read the file into a String before forming the
    // Part. Dependent on MIME ability to put a File into a Part without
    // needing to encode it.
    IPart scontent= null;
    if (udocfile.length() > 0)
    {
      scontent= factory.createPart(canonicalize(udocfile), PART_CONTENT_TYPE);
    }
    else
    {
      RosettaNetException.pkgMesgGenErr("Error Reading original UDoc file");
    }

    //    scontent.setContentID();
    scontent.addHeader(CONTENT_LOC, SERVICE_CONTENT_CL);

    // Koh Han Sing 20020503
    IPart[] attTable= createAttachmentIParts(payloads, packInfo, factory);

    // create the Service Header
    IPart sheader=
      factory.createPart(this.makeServiceHeader(packInfo, attTable), PART_CONTENT_TYPE);
    //    sheader.setContentID();
    sheader.addHeader(CONTENT_LOC, SERVICE_HEADER_CL);
    //envelope.addPart( sheader);

    Logger.debug("Is Compress Enabled: "+doCompress);
    
    if(doCompress)
    {
    	//Nov 14 2007 TWX: For now, we are not supporting the compression on 1) attachment only 2) service content only 3) on a particular attachment
    	//We compress everything.
    	//We compress the MIME part content corresponding to the service content and attachment (no Content-Transfer-Encoding has been set)
    	
    	Logger.debug("Compressing service content");
    	IPart tempSContent = scontent;
    	
    	//scontent = factory.compress(scontent);
    	scontent = factory.compressPartContent(scontent);
    	
    	scontent.setContentIDForCompression(tempSContent);
    	tempSContent = null;
    	
    	attTable = compressAttachment(attTable, factory);
    }
    if (doEncrypt)
    {
      IMime payload= factory.createMime();
      if (encryptSvcHeader)
      {
        payload.addPart(sheader);
        payload.addPart(scontent);
        payload= addAttachments(payload, attTable); //KHS 20020503
        //_logger.debug( "    to encrypt content: " + sheader.getHeader("Content-Location")[0]);
      }
      else
      {
        envelope.addPart(sheader);
        payload.addPart(scontent);
        payload= addAttachments(payload, attTable); //KHS 20020503
      }
      IPart part= factory.encrypt(payload); //, encryptAlg, encryptAlgLength);
      envelope.addPart(part);
    }
    else
    {
      envelope.addPart(sheader);
      envelope.addPart(scontent);
      envelope= addAttachments(envelope, attTable); //KHS 20020503
    }
    //IPart scontent = factory.createPart( udocfile, PART_CONTENT_TYPE, null);
    //envelope.addPart( scontent); // removed because the createPart for
    // file source is not done yet.

    envelope.setSubType("related; type=\"application/xml\"");
    Logger.debug("  content-type = " + envelope.getContentType());

    if (packInfo.getIsNonRepudiationRequired() && !packInfo.getIsSignalDoc())
    {
      DigestGenerator dg= new DigestGenerator();
      byte[] envelopeContent= envelope.getContentByte(false); //20090811 twx, #842 digest generated is not matched, we need to exclude the header
      String digest= dg.getEncodedDigest(envelopeContent, digestAlg);
      packInfo.setMsgDigest(digest);
      if (_infoFinder != null)
        _infoFinder.setPackInfo(packInfo);
    }

    // Sign the envelope
    IMime finalEnvelope= null;
    if (doSign)
    {
      try
      {
        finalEnvelope= factory.sign(envelope); //, digestAlg);
      }
      catch (SecurityServiceException ex)
      {
        Logger.warn("Signing error:", ex);
        throw RosettaNetException.pkgMesgGenErr("Error signing message");
      }
    }
    else
    {
      finalEnvelope= envelope;
    }
    return finalEnvelope;
  }

  /**
   * Returns the RosettaNet Preamble constructed from the values
   * in the GridDocument.
   */
  String makePreamble(RNPackInfo info)
  {
    StringBuffer result= new StringBuffer();
    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
    result.append("<!DOCTYPE Preamble SYSTEM \"" + PREAMBLE_DTD + "\">\r\n");
//    result.append("<!-- Generated by " + this.getClass().toString() + ".makePreamble() -->\r\n");
    result.append("<Preamble>\r\n");
    result.append("  <standardName>\r\n    ");
    result.append(XMLUtil.formatElement("GlobalAdministeringAuthorityCode", ADMIN_AUTH_CODE));
    result.append("\r\n  </standardName>\r\n");
    result.append("  <standardVersion>\r\n    ");
    result.append(XMLUtil.formatElement("VersionIdentifier", RNIF_VERSION));
    result.append("\r\n  </standardVersion>\r\n");
    result.append("</Preamble>\r\n");
    return result.toString();
  }

  /**
     * Returns the RosettaNet Delivery Header constructed from the values
     * in the GridDocument.
     */
  String makeDeliveryHeader(RNPackInfo packInfo) throws RosettaNetException
  {
    // calculate date
    Date msgsent= packInfo.getDTSendStart();
    if (null == msgsent)
      msgsent= new Date();
    DateFormat df= new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'");

    //df.setTimeZone(new SimpleTimeZone(0, ROSETTANET_TIMEZONE));
    String messageDateTime= df.format(msgsent);

    StringBuffer result= new StringBuffer();
    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
    result.append("<!DOCTYPE DeliveryHeader SYSTEM \"" + DELIVERY_HEADER_DTD + "\">\r\n");
//    result.append(
//      "<!-- Generated by " + this.getClass().toString() + ".makeDeliveryHeader() -->\r\n");
    result.append("<DeliveryHeader>\r\n");
    // secure transport flag
    result.append("  <isSecureTransportRequired>\r\n    ");
    String value= packInfo.getIsSecureTptRequired() ? "Yes" : "No";
    result.append(XMLUtil.formatElement("AffirmationIndicator", value));
    result.append("\r\n  </isSecureTransportRequired>\r\n");
    // message sent
    result.append("  <messageDateTime>\r\n    ");
    result.append(XMLUtil.formatElement("DateTimeStamp", messageDateTime));
    result.append("\r\n  </messageDateTime>\r\n");
    // receiver id
    String domain;
    //Integer businessID;
    String businessIDString;
    String location;
    result.append("  <messageReceiverIdentification>\r\n");
    result.append("    <PartnerIdentification>\r\n");
    domain= (String) packInfo.getFieldValue(RNPackInfo.RECEIVER_DOMAIN);
    if (null != domain)
    {
      result.append("      <domain>\r\n        ");
      result.append(XMLUtil.formatElement("FreeFormText", domain));
      result.append("\r\n      </domain>\r\n");
    }
    businessIDString= (String) packInfo.getFieldValue(RNPackInfo.RECEIVER_GLOBAL_BUS_IDENTIFIER);
    if (null == businessIDString)
    {
      throw RosettaNetException.pkgDhdrGDocErr("Missing ReceiverGlobalBusIdentifier");
    }
    else
    {
      result.append("      ");
      result.append(XMLUtil.formatElement("GlobalBusinessIdentifier", businessIDString));
      result.append("\r\n");
    }
    location= (String) packInfo.getFieldValue(RNPackInfo.RECEIVER_LOCATION_ID);
    if (null != location)
    {
      result.append("      <locationID>\r\n        ");
      result.append(XMLUtil.formatElement("Value", location));
      result.append("\r\n      </locationID>\r\n");
    }
    result.append("    </PartnerIdentification>\r\n");
    result.append("  </messageReceiverIdentification>\r\n");
    // sender id
    result.append("  <messageSenderIdentification>\r\n");
    result.append("    <PartnerIdentification>\r\n");
    domain= (String) packInfo.getFieldValue(RNPackInfo.SENDER_DOMAIN);
    if (null != domain)
    {
      result.append("      <domain>\r\n        ");
      result.append(XMLUtil.formatElement("FreeFormText", domain));
      result.append("\r\n      </domain>\r\n");
    }
    businessIDString= (String) packInfo.getFieldValue(RNPackInfo.SENDER_GLOBAL_BUS_IDENTIFIER);
    if (null == businessIDString)
    {
      throw RosettaNetException.pkgDhdrGDocErr("Missing SenderGlobalBusIdentifier");
    }
    else
    {
      result.append("      ");
      result.append(XMLUtil.formatElement("GlobalBusinessIdentifier", businessIDString));
      result.append("\r\n");
    }
    location= (String) packInfo.getFieldValue(RNPackInfo.SENDER_LOCATION_ID);
    if (null != location)
    {
      result.append("      <locationID>\r\n        ");
      result.append(XMLUtil.formatElement("Value", location));
      result.append("\r\n      </locationID>\r\n");
    }
    result.append("    </PartnerIdentification>\r\n");
    result.append("  </messageSenderIdentification>\r\n");
    // message tracking
    result.append("  <messageTrackingID>\r\n");
    String messageTrackingID=
      (String) packInfo.getFieldValue(RNPackInfo.DELIVERY_MESSAGE_TRACKING_ID);
    if (null == messageTrackingID)
    {
      throw RosettaNetException.pkgDhdrGDocErr("messageTrackingID.InstanceIdentifier");
    }
    else
    {
      result.append("    ");
      result.append(XMLUtil.formatElement("InstanceIdentifier", messageTrackingID));
      result.append("\r\n");
    }
    result.append("  </messageTrackingID>\r\n");
    result.append("</DeliveryHeader>\r\n");
    return result.toString();
  }

  /**
   * Returns the RosettaNet Service Header constructed from the values
   * in the GridDocument.
   */
  String makeServiceHeader(RNPackInfo packInfo, IPart[] attTable) throws RosettaNetException
  {
    String value;
    //Integer i;
    String intValue;

    StringBuffer result= new StringBuffer();
    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
    result.append("<!DOCTYPE ServiceHeader SYSTEM \"" + SERVICE_HEADER_DTD + "\">\r\n");
//    result.append("<!-- Generated by " + this.getClass().toString() + ".makeServiceHeader() -->\r\n");
    result.append("<ServiceHeader>\r\n");
    result.append("  <ProcessControl>\r\n");
    result.append("    <ActivityControl>\r\n");

    value= (String) packInfo.getFieldValue(RNPackInfo.BUS_ACTIVITY_IDENTIFIER);
    if (null != value)
    {
      result.append("      ");
      result.append(XMLUtil.formatElement("BusinessActivityIdentifier", value));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("BusinessActivityIdentifier");
    }
    result.append("      <MessageControl>\r\n");
    result.append("        <fromRole>\r\n");
    value= (String) packInfo.getFieldValue(RNPackInfo.FROM_GLOBAL_PARTNER_ROLE_CLASS_CODE);
    if (null != value)
    {
      result.append("          ");
      result.append(XMLUtil.formatElement("GlobalPartnerRoleClassificationCode", value));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("fromRole.GlobalPartnerRoleClassificationCode");
    }
    result.append("        </fromRole>\r\n");
    result.append("        <fromService>\r\n");
    value= (String) packInfo.getFieldValue(RNPackInfo.FROM_GLOBAL_BUS_SERVICE_CODE);
    if (null != value)
    {
      result.append("          ");
      result.append(XMLUtil.formatElement("GlobalBusinessServiceCode", value));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("fromService.GlobalBusinessServiceCode");
    }
    result.append("        </fromService>\r\n");

    if (!packInfo.getIsRequestMsg())
    {
      result.append("        <inReplyTo>\r\n          <ActionControl>\r\n            <ActionIdentity>\r\n");
      value= (String) packInfo.getFieldValue(RNPackInfo.IN_REPLY_TO_GLOBAL_BUS_ACTION_CODE);
      if (null != value)
      {
        result.append("              ");
        result.append(XMLUtil.formatElement("GlobalBusinessActionCode", value));
        result.append("\r\n");
      }
      else
      {
        throw RosettaNetException.pkgShdrGDocErr(
          "inReplyTo.ActionControl.ActionIdentity.GlobalBusinessActionCode");
      }

      //NSL20070709 To retain version identifier from original action message
      value = packInfo.getInReplyToVersionIdentifier();
      if (null != value) 
      {
        result.append("              <standardVersion>\r\n                ");
        result.append(XMLUtil.formatElement("VersionIdentifier", value));
        result.append("\r\n              </standardVersion>\r\n");
      } 

      result.append("            </ActionIdentity>\r\n");

      result.append("            <messageTrackingID>\r\n");
      value= (String) packInfo.getFieldValue(RNPackInfo.SERVICE_MESSAGE_TRACKING_ID);
      if (null != value)
      {
        result.append("              ");
        result.append(XMLUtil.formatElement("InstanceIdentifier", value));
        result.append("\r\n");
      }
      else
      {
        throw RosettaNetException.pkgShdrGDocErr("inReplyTo.messageTrackingID.InstanceIdentifier");
      }
      result.append("            </messageTrackingID>\r\n");

      result.append("          </ActionControl>\r\n        </inReplyTo>\r\n");
    }

    result.append("        <Manifest>\r\n");
    // Koh Han Sing 20020506
    boolean hasAttachment= (attTable != null) && (attTable.length > 0);
    if (hasAttachment)
    {
      int noOfAttachments= attTable.length;
      for (int j= 0; j < noOfAttachments; j++)
      {
        IPart attIPart= attTable[j];
        result.append("          <Attachment>\r\n");

        result.append("            <GlobalMimeTypeQualifierCode>");
        result.append(ATT_TYPE);
        result.append("</GlobalMimeTypeQualifierCode>\r\n");

        result.append("            <UniversalResourceIdentifier>");
        String[] contentIDs= null;
        try
        {
          contentIDs= attIPart.getHeader("Content-ID");
        }
        catch (SecurityServiceException ex)
        {
          throw RosettaNetException.pkgMesgGenErr(ex);
        }
        if ((contentIDs != null) && (contentIDs.length != 0))
        {
          Logger.debug("contentID[0] = *" + contentIDs[0] + "*");
          String contentID= contentIDs[0];
          result.append("cid:");
          result.append(contentID.substring(1, (contentID.length() - 1)));
        }
        else
        {
          throw RosettaNetException.pkgShdrGDocErr("Attachment parts have no Content-ID header");
        }
        result.append("</UniversalResourceIdentifier>\r\n");

        result.append("          </Attachment>\r\n");
      }
      result.append("          <numberOfAttachments>\r\n            ");
      result.append(
        XMLUtil.formatElement("CountableAmount", new Integer(noOfAttachments).toString()));
      result.append("\r\n          </numberOfAttachments>\r\n");
    }
    else
    {
      result.append("          <numberOfAttachments>\r\n            ");
      result.append(XMLUtil.formatElement("CountableAmount", "0"));
      result.append("\r\n          </numberOfAttachments>\r\n");
    }

    result.append("          <ServiceContentControl>\r\n");

    if (!packInfo.getIsSignalDoc())
    {
      result.append("            <ActionIdentity>\r\n");

      value= (String) packInfo.getFieldValue(RNPackInfo.ACTION_IDENTITY_GLOBAL_BUS_ACTION_CODE);
      if (null != value)
      {
        result.append("              ");
        result.append(XMLUtil.formatElement("GlobalBusinessActionCode", value));
        result.append("\r\n");
      }
      else
      {
        throw RosettaNetException.pkgShdrGDocErr("ActionIdentity.GlobalBusinessActionCode");
      }

      //NSL20070716 Output optional fields if present
      value = packInfo.getActionIdentityToMessageStandard();
      if (null != value) 
      {
        result.append("              <messageStandard>\r\n                ");
        result.append(XMLUtil.formatElement( "FreeFormText", value));
        result.append("\r\n              </messageStandard>\r\n");
      }
      
      value = packInfo.getActionIdentityVersionIdentifier();
      if (null != value) 
      {
        result.append("              <standardVersion>\r\n                ");
        result.append(XMLUtil.formatElement( "VersionIdentifier", value));
        result.append("\r\n              </standardVersion>\r\n");
      } 
      result.append("            </ActionIdentity>\r\n");
    }
    else
    {
      result.append("            <SignalIdentity>\r\n");

      value= packInfo.getFieldValue(RNPackInfo.SIGNAL_IDENTITY_GLOBAL_BUS_SIGNAL_CODE);
      if (null != value)
      {
        result.append("              ");
        result.append(XMLUtil.formatElement("GlobalBusinessSignalCode", value));
        result.append("\r\n");
      }
      else
      {
        throw RosettaNetException.pkgShdrGDocErr("SignalIdentity.GlobalBusinessSignalCode");
      }

      value= packInfo.getFieldValue(RNPackInfo.SIGNAL_IDENTITY_VERSION_IDENTIFIER);
      if (null != value)
      {
        result.append("              ");
        result.append(XMLUtil.formatElement("VersionIdentifier", value));
        result.append("\r\n");
      }
      else
      {
        throw RosettaNetException.pkgShdrGDocErr("SignalIdentity.VersionIdentifier");
      }

      result.append("            </SignalIdentity>\r\n");
    }

    result.append("          </ServiceContentControl>\r\n");
    result.append("        </Manifest>\r\n");

    result.append("        <toRole>\r\n");
    value= packInfo.getFieldValue(RNPackInfo.TO_GLOBAL_PARTNER_ROLE_CLASS_CODE);
    if (null != value)
    {
      result.append("          ");
      result.append(XMLUtil.formatElement("GlobalPartnerRoleClassificationCode", value));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("toRole.GlobalPartnerRoleClassificationCode");
    }
    result.append("        </toRole>\r\n");

    result.append("        <toService>\r\n");
    value= packInfo.getFieldValue(RNPackInfo.TO_GLOBAL_BUS_SERVICE_CODE);
    if (null != value)
    {
      result.append("          ");
      result.append(XMLUtil.formatElement("GlobalBusinessServiceCode", value));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("toService.GlobalBusinessServiceCode");
    }
    result.append("        </toService>\r\n");
    result.append("      </MessageControl>\r\n");
    result.append("    </ActivityControl>\r\n");

    value= packInfo.getFieldValue(RNPackInfo.GLOBAL_USAGE_CODE);
    if (null != value)
    {
      result.append("    ");
      result.append(XMLUtil.formatElement("GlobalUsageCode", value));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("GlobalUsageCode");
    }

    result.append("    <pipCode>\r\n");
    value= packInfo.getFieldValue(RNPackInfo.PIP_GLOBAL_PROCESS_CODE);
    if (null != value)
    {
      result.append("      ");
      result.append(XMLUtil.formatElement("GlobalProcessIndicatorCode", value));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("pipCode.GlobalProcessIndicatorCode");
    }
    result.append("    </pipCode>\r\n");

    result.append("    <pipInstanceId>\r\n");
    value= packInfo.getFieldValue(RNPackInfo.PIP_INSTANCE_IDENTIFIER);
    if (null != value)
    {
      result.append("      ");
      result.append(XMLUtil.formatElement("InstanceIdentifier", value));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("pipInstanceId.InstanceIdentifier");
    }
    result.append("    </pipInstanceId>\r\n");

    result.append("    <pipVersion>\r\n");
    value= packInfo.getFieldValue(RNPackInfo.PIP_VERSION_IDENTIFIER);
    if (null != value)
    {
      result.append("      ");
      result.append(XMLUtil.formatElement("VersionIdentifier", value));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("pipVersion.VersionIdentifier");
    }
    result.append("    </pipVersion>\r\n");

    result.append("    <KnownInitiatingPartner>\r\n");
    result.append("      <PartnerIdentification>\r\n");

//  TWX 20091005 #1053: Some optional field in ServiceHeader is missing
    String originator = packInfo.getProcessOriginatorId();
    boolean isOriginator = "SELF".equals(originator) ? true : false;
    
    //TWX #1053: given the originatorID to determine the domain value.
    if(isOriginator)
    {
//    NSL20070709 Temporary get from sender domain, since now it's fixed value
      value = packInfo.getFieldValue(RNPackInfo.SENDER_DOMAIN);
    }
    else
    {
      value = packInfo.getFieldValue(RNPackInfo.RECEIVER_DOMAIN); 
    }

    if (null != value)
    {
      result.append("        <domain>\r\n          ");
      result.append(XMLUtil.formatElement("FreeFormText", value));
      result.append("\r\n        </domain>\r\n");
    }
    
    //i = doc.getPartnerGlobalBusIdentifier();
    intValue= packInfo.getFieldValue(RNPackInfo.PARTNER_GLOBAL_BUS_IDENTIFIER);
    if (null != intValue)
    {
      result.append("        ");
      result.append(XMLUtil.formatElement("GlobalBusinessIdentifier", intValue));
      result.append("\r\n");
    }
    else
    {
      throw RosettaNetException.pkgShdrGDocErr("PartnerIdentification.GlobalBusinessIdentifier");
    }
    

    
    if(isOriginator)
    {
      value = packInfo.getFieldValue(RNPackInfo.SENDER_LOCATION_ID);
    }
    else
    {
      value = packInfo.getFieldValue(RNPackInfo.RECEIVER_LOCATION_ID);
    }
    if (null != value)
    {
      result.append("        <locationID>\r\n          ");
      result.append(XMLUtil.formatElement("Value", value));
      result.append("\r\n        </locationID>\r\n");
    }
    
    result.append("      </PartnerIdentification>\r\n");
    result.append("    </KnownInitiatingPartner>\r\n");

    result.append("  </ProcessControl>\r\n");
    result.append("</ServiceHeader>\r\n");
    return result.toString();
  }

  private IPart[] createAttachmentIParts(
    File[] payloads,
    RNPackInfo packInfo,
    SMimeFactory factory)
    throws SecurityServiceException, SecurityServiceException, RosettaNetException
  {
    int length= payloads.length;
    if (length <= ATTACH_INDEX)
      return null;

    IPart[] res= new IPart[length - ATTACH_INDEX];
    for (int i= ATTACH_INDEX; i < length; i++)
    {
      File attfile= payloads[i];
      IPart attachmentPart= null;
      if (attfile.length() > 0)
      {
//        attachmentPart= factory.createPart(attfile, ATT_TYPE, null);

    	  Logger.log("prepareAttachment starts");
    	  attachmentPart = prepareAttachment(attfile, factory);
    	  Logger.log("prepareAttachment ends");
        //        attachmentPart.setContentID();
      }
      else
      {
        throw RosettaNetException.pkgMesgGenErr("Error Reading attachment file");
      }
      res[i - ATTACH_INDEX]= attachmentPart;
    }
    return res;
  }

  /**
   * Add the list of attachments IParts into the MIME envelope
   */
  private IMime addAttachments(IMime iMime, IPart[] attTable) throws SecurityServiceException
  {
    if (attTable != null)
    {
      for (int i= 0; i < attTable.length; i++)
      {
        IPart attachmentIPart= attTable[i];
        iMime.addPart(attachmentIPart);
      }
    }
    return iMime;
  }

  private IPart prepareAttachment(File file, SMimeFactory factory)
  {
	 Logger.debug("RNPackager_20::prepareAttachment");
	 Logger.debug("attachment filename = " + file.getName());
	 try
	 {
		 IPart part = factory.createPart(file, ATT_TYPE, null);
		 GNBodypart gnBodyPart = (GNBodypart) part;
		 gnBodyPart.setAttachmentFileName(file.getName());
		 return gnBodyPart;
	 } catch (Exception e)
	 {
		 Logger.warn(e);
		 return null;
	 }
  }
  
  /**
   * TWX 09112007 Support the compression on RNIF attachment
   * @param attachment The list of attachment
   * @param factory The instance of SmimeFactory
   * @return the list of attachment in compressed format
   * @throws SecurityServiceException if compression failed
   */
  private IPart[] compressAttachment(IPart[] attachment, SMimeFactory factory) throws SecurityServiceException
  {
	  if(attachment == null || attachment.length == 0)
	  {
		  return null;
	  }
	  
	  IPart[] compressAttachments = new IPart[attachment.length];
	  for(int i = 0; i < attachment.length; i++)
	  {
		  IPart tempAttachment = attachment[i];
		  compressAttachments[i] = factory.compressPartContent(attachment[i]);
		  compressAttachments[i].setContentIDForCompression(tempAttachment);
	  }
	  return compressAttachments;
  }
}
