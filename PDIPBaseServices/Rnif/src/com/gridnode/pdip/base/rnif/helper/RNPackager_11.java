/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNPackager_11.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 21 2003      Guo Jianyu              Created
 * Sep 25 2003      Guo Jianyu              Modification: extract GlobalProcessCode
 *                                          from packinfo rather than from the config file,
 *                                          which is no longer needed when the list of enumerated
 *                                          values for GlobalProcessCode is now maintained in
 *                                          the service header schema.
 * Oct 03 2003      Guo Jianyu              Bug fix: some elements in the preamble and service header
 *                                          were out of sequence.
 * Jan 29 2004      Neo Sok Lay             Remove file extension of prefix for File.createTempFile().
 * Apr 05 2004      Guo Jianyu              Bug Fix: GNDB00017554. Avoided extra
 *                                          timezone conversion in makePreamble()
 * Jan 07 2005      Guo Jianyu              Bug Fix: GNDB00025535: "GlobalTransactionCode" in RNIF1.1 service header is wrong for 0A1
 * Jul 28 2009      Tam Wei Xiang           #560 - modified packFiles2Mime(..) to set the private key
 */
package com.gridnode.pdip.base.rnif.helper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.base.rnif.model.IRNConstant_11;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.mime.IMime;
import com.gridnode.pdip.base.security.mime.IPart;
import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

/**
 * The packager for RNIF1.1 messages
 *
 * @author Guo Jianyu
 *
 * @version 1.0
 * @since 1.0
 */
public class RNPackager_11 extends RNPackager
                           implements IRNConstant_11, Serializable
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6074665799091890848L;
	protected SMimeFactory factory = null;
//  protected static Configuration config = ConfigurationManager.getInstance().getConfig(
//      IRnifConfig.CONFIG_NAME);
/*  protected static Hashtable processCodeTable = new Hashtable();
  static
  {
    processCodeTable.put("0A1","Notification of Failure");
    processCodeTable.put("0C1","Asynchronous Test Notification");
    processCodeTable.put("0C2","Asynchronous Test Request Confirmation");
    processCodeTable.put("0C3","Synchronous Test Notification");
    processCodeTable.put("0C4","Synchronous Test Request Confirmation");
    processCodeTable.put("2A1","Distribute New Product Information");
    processCodeTable.put("2A9","Query Technical Product Information");
    processCodeTable.put("2A12","Distribute Product Master");
    processCodeTable.put("3A1","Request Quote");
    processCodeTable.put("3A4","Request Purchase Order");
    processCodeTable.put("3A6","Distribute Order Status");
    processCodeTable.put("3A8","Request Purchase Order Change");
    processCodeTable.put("3A9","Request Purchase Order Cancellation");
  }
*/
  public RNPackager_11()
  {
  }

  public File[][] packDoc(RNPackInfo packInfo, File[] payloads)
          throws RosettaNetException
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
      IMime serviceMsg = packFiles2Mime(payloads, packInfo);
      String auditfilename= packInfo.getUDocFileName();
      Logger.debug("Udoc File Name is " + auditfilename);
      //tmpauditfile= File.createTempFile(auditfilename + "_audit", ".xml");
      tmpauditfile= File.createTempFile(FileUtil.removeExtension(auditfilename)+"_audit", null);

      byte[] content= (byte[]) serviceMsg.getContentByte(false);
      DataOutputStream fos = new DataOutputStream(new FileOutputStream(tmpauditfile));

      fos.writeInt(RN_VERSION_NUMBER); //RNIF version
      fos.writeInt(content.length);    //length of RNO content

      fos.write(content);              //the actual RNO content

      boolean doSign= packInfo.getIsEnableSignature();

      if (doSign)
      {
        byte[] signature = factory.sign(content);
        fos.writeInt(signature.length);
        fos.write(signature);
      }
      else
      {
        fos.writeInt(0);
      }

      fos.close();

      Logger.debug("tmpauditfile=" + tmpauditfile.getAbsolutePath());
      auditfilename= XMLUtil.createFile(IRnifPathConfig.PATH_AUDIT, auditfilename, tmpauditfile);
      createdAuditFile=FileUtil.getFile(IRnifPathConfig.PATH_AUDIT,auditfilename);
      Logger.debug("Written to [" + createdAuditFile + "]");

    }
    catch (Exception ex)
    {
      Logger.warn(ex);
      throw RosettaNetException.pkgMesgGenErr(ex);
    }

    File[][] res= new File[][] {{ tmpauditfile },{createdAuditFile}};
    return res;

  }

  private IMime packFiles2Mime(File[] payloads, RNPackInfo packInfo)
      throws SecurityServiceException, RosettaNetException
  {
    boolean doSign= packInfo.getIsEnableSignature();
    String digestAlg= packInfo.getDigestAlgorithm();

    String recipientDUNS= packInfo.getReceiverGlobalBusIdentifier();
    RNCertInfo securityInfo= new RNCertInfo();

    try
    {
      Logger.debug("## recipientDUNS is " + recipientDUNS);
      if ((_infoFinder != null) && doSign)
      {
        securityInfo= _infoFinder.getSecurityInfoFromDUNS(recipientDUNS);
        factory=
          SMimeFactory.newInstance(
           getOwnSignCert(securityInfo), getPartnerEncryptCert(securityInfo));
        
        //20090728 TWX, set the private key for signing purpose
        factory.setPrivateKey(GridCertUtilities.loadPrivateKeyFromString(securityInfo.get_ownSignCertificate().getPrivateKey()));
//        Logger.debug("## infoFinder is not null, ownSignCert is " + securityInfo.get_ownSignCertificate()
//          + " partnerEncrptCert is " + securityInfo.get_partnerEncryptCertificate());
      }
      else
      {
        Logger.debug("infoFinder is null");
        factory= SMimeFactory.newInstance(null, null);
      }
    }
    catch (Exception ex)
    {
      Logger.warn(ex);
      throw RosettaNetException.unpMesgGenErr(
        "Error in finding Security info based on recipientDUNS:" + recipientDUNS);
    }

    factory.setDigestAlgorithm(digestAlg); // no effect on the signature generation

    IMime serviceMsg = factory.createMime();

    String RNIFVersion = packInfo.getRnifVersion();
    if (RNIFVersion.equals(PROTOCOL_CIDX))
      serviceMsg.setParameter("type", "Application/x-ChemXML");
    else
      serviceMsg.setParameter("type", "Application/x-RosettaNet");

    // create the Preamble
    IPart preamble= factory.createPart(this.makePreamble(packInfo), PART_CONTENT_TYPE);
    preamble.setParameter("RNSubType", PREAMBLE_HEADER);
    preamble.setDescription("Preamble of business message");

    serviceMsg.addPart(preamble);

    // create the Service Header
    IPart sheader=
      factory.createPart(this.makeServiceHeader(packInfo), PART_CONTENT_TYPE);
    sheader.setParameter("RNSubType", SERVICE_HEADER);
    sheader.setDescription("Service header of business message");
    serviceMsg.addPart(sheader);

    // create the service content
    File udocfile= (File) payloads[UDOC_INDEX];
    if (!udocfile.exists())
    {
      RosettaNetException.pkgMesgGenErr("Cannot find UDoc file");
    }

    IPart scontent= null;
    if (udocfile.length() > 0)
    {
      scontent= factory.createPart(udocfile, PART_CONTENT_TYPE, null);
    }
    else
    {
      RosettaNetException.pkgMesgGenErr("Error Reading original UDoc file");
    }

    scontent.setParameter("RNSubType", SERVICE_CONTENT);
    scontent.setDescription("Service content of business message");

    serviceMsg.addPart(scontent);

    return serviceMsg;

//    envelope.setSubType("related; type=\"application/xml\"");
//    Logger.debug("  content-type = " + envelope.getContentType());

/*    if (packInfo.getIsNonRepudiationRequired() && !packInfo.getIsSignalDoc())
    {
      DigestGenerator dg= new DigestGenerator();
      byte[] envelopeContent= envelope.getContentByte(true);
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
        Logger.err("Signing error:", ex);
        throw RosettaNetException.pkgMesgGenErr("Error signing message");
      }
    }
    else
    {
      finalEnvelope= envelope;
    }
    return finalEnvelope;
*/
  }

  private String makePreamble(RNPackInfo packInfo) throws RosettaNetException
  {
    StringBuffer result= new StringBuffer();
    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    result.append("<!DOCTYPE Preamble SYSTEM \"" + PREAMBLE_DTD + "\">\n");
    result.append("<Preamble>\n");
    result.append("  ");

    Date msgSentTime = packInfo.getDTSendStart();
    if (msgSentTime == null)
      msgSentTime = new Date();
    DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'");
//    df.setTimeZone(new SimpleTimeZone(0, ROSETTANET_TIMEZONE));
    result.append(XMLUtil.formatElement("DateTimeStamp", df.format(msgSentTime)));
    result.append("\n  ");

    String RNIFVersion = packInfo.getRnifVersion();
    if (RNIFVersion.equals(PROTOCOL_CIDX))
      result.append(XMLUtil.formatElement("GlobalAdministeringAuthorityCode", CIDX_CODE));
    else
      result.append(XMLUtil.formatElement("GlobalAdministeringAuthorityCode", ADMIN_AUTH_CODE));

    String globalUsageCode = packInfo.getGlobalUsageCode();
    if (globalUsageCode == null)
      throw RosettaNetException.pkgPreambleGDocErr("GlobalUsageCode");
    else
    {
      result.append("\n  ");
      result.append(XMLUtil.formatElement("GlobalUsageCode", globalUsageCode));
    }

    result.append("\n  ");
    result.append(XMLUtil.formatElement("VersionIdentifier", RNIF_VERSION));
    result.append("\n</Preamble>\n");
    return result.toString();
  }

  private String makeServiceHeader(RNPackInfo packInfo) throws RosettaNetException
  {
    String value;
    //Integer i;
    //String intValue;

    StringBuffer result= new StringBuffer();
    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    result.append("<!DOCTYPE ServiceHeader SYSTEM \"" + SERVICE_HEADER_DTD + "\">\n");
    result.append("<ServiceHeader>\n");
    result.append(" <ProcessControl>\n");

    // ProcessIdentity
    result.append("   <ProcessIdentity>\n");
    result.append("     ");

//    String processIndicatorCode = packInfo.getPIPGlobalProcessCode();
//    value = config.getString(IRnifConfig.GLOBAL_PROCESS_INDICATOR_CODE + processIndicatorCode);
    value = packInfo.getBusActivityIdentifier();
    if (null != value)
      result.append(XMLUtil.formatElement("GlobalProcessCode", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("GlobalProcessCode");

    result.append("\n     ");
    value = packInfo.getPIPGlobalProcessCode();
    if (null != value)
      result.append(XMLUtil.formatElement("GlobalProcessIndicatorCode", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("GlobalProcessIndicatorCode");

    result.append("\n     <initiatingPartner>\n");
    result.append("       ");
    value = packInfo.getPartnerGlobalBusIdentifier();
    if (null != value)
      result.append(XMLUtil.formatElement("GlobalBusinessIdentifier", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("initiatingPartner.GlobalBusinessIdentifier");

    result.append("\n     </initiatingPartner>\n");

    result.append("     ");
    value = packInfo.getPIPInstanceIdentifier();
    if (null != value)
      result.append(XMLUtil.formatElement("InstanceIdentifier", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("ProcessIdentity.InstanceIdentifier");

    result.append("\n     ");
    value = packInfo.getPIPVersionIdentifier();
    if (null != value)
      result.append(XMLUtil.formatElement("VersionIdentifier", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("VersionIdentifier");

/*
    value = packInfo.getProcessIdentityDesc();
    if (null != value)
    { // this description is optional
      result.append("     <description>\n");
      String lang = packInfo.getProcessIdentityDescLang();
      if (lang == null)
        lang = "en";
      result.append("       <FreeFormText xml:lang=\"" + lang + "\">" +
            value + "</FreeFormText>\n");
      result.append("     </description>\n");
    }
*/
    result.append("\n   </ProcessIdentity>\n");

    // ServiceRoute
    result.append("   <ServiceRoute>\n");
    result.append("     <fromService>\n");
    result.append("       <BusinessServiceDescription>\n");

    result.append("         ");
    value = packInfo.getFromGlobalBusServiceCode();
    if (null != value)
      result.append(XMLUtil.formatElement("GlobalBusinessServiceCode", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("fromService.GlobalBusinessServiceCode");

    result.append("\n       </BusinessServiceDescription>\n");
    result.append("     </fromService>\n");

    result.append("     <toService>\n");
    result.append("       <BusinessServiceDescription>\n");
    result.append("         ");
    value = packInfo.getToGlobalBusServiceCode();
    if (null != value)
      result.append(XMLUtil.formatElement("GlobalBusinessServiceCode", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("toService.GlobalBusinessServiceCode");

    result.append("\n       </BusinessServiceDescription>\n");
    result.append("     </toService>\n");
    result.append("   </ServiceRoute>\n");

    //TransactionControl
    result.append("   <TransactionControl>\n");
    int attemptCount = packInfo.getAttemptCount();
    if (attemptCount <= 0)
      attemptCount = 1;
    result.append("     ");
    result.append(XMLUtil.formatElement("AttemptCount", String.valueOf(attemptCount)));

    result.append("\n     <PartnerRoleRoute>\n");
    result.append("       <fromRole>\n");
    result.append("         <PartnerRoleDescription>\n");
    result.append("           ");
    value = packInfo.getFromGlobalPartnerRoleClassCode();
    if (null != value)
      result.append(XMLUtil.formatElement("GlobalPartnerRoleClassificationCode", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("fromRole.GlobalPartnerRoleClassificationCode");

    result.append("\n         </PartnerRoleDescription>\n");
    result.append("       </fromRole>\n");
    result.append("       <toRole>\n");
    result.append("         <PartnerRoleDescription>\n");
    result.append("           ");
    value = packInfo.getToGlobalPartnerRoleClassCode();
    if (null != value)
      result.append(XMLUtil.formatElement("GlobalPartnerRoleClassificationCode", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("toRole.GlobalPartnerRoleClassificationCode");

    result.append("\n         </PartnerRoleDescription>\n");
    result.append("       </toRole>\n");
    result.append("     </PartnerRoleRoute>\n");

    result.append("     <TransactionIdentity>\n");
    value = packInfo.getBusActivityIdentifier();
    result.append("       ");
    if (null != value)
    {
      //Jianyu - 07/01/2005 this is a dirty fix to insert the spec-compliant value for
      //0A1, a perfect manifestation of the stupidity of RNIF1.1
      if (value.equals("Notification of Failure"))
          value = "Distribute Notification of Failure";
      result.append(XMLUtil.formatElement("GlobalTransactionCode", value));
    }
    else
      throw RosettaNetException.pkgShdrGDocErr("GlobalTransactionCode");

    result.append("\n       ");
    value = packInfo.getProcessTransactionId();
    if (null != value)
      result.append(XMLUtil.formatElement("InstanceIdentifier", value));
    else
      throw RosettaNetException.pkgShdrGDocErr("TransactionIdentity.InstanceIdentifier");

/*    value = packInfo.getTransactionIdentityDesc();
    if (null != valule)
    { // this description is optional
      result.append("       <description>\n");
      String lang = packInfo.getTransactionIdentityDescLang();
      if (lang == null)
        lang = "en";
      result.append("         <FreeFormText xml:lang=\"" + lang + "\">" +
            value + "</FreeFormText>\n");
      result.append("       </description>/n");
    }
*/
    result.append("\n     </TransactionIdentity>\n");

    if (packInfo.getIsSignalDoc()) // SignalControl
    {
      result.append("     <SignalControl>\n");
      result.append("       <inResponseTo>\n");
      result.append("         <ActionIdentity>\n");

      result.append("           ");
      value = packInfo.getInReplyToGlobalBusActionCode();
      if ( value != null)
        result.append(XMLUtil.formatElement("GlobalBusinessActionCode", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("inResponseTo.ActionIdentity.GlobalBusinessActionCode");

      result.append("\n           ");
      value = packInfo.getProcessActionId();
      if ( value != null)
        result.append(XMLUtil.formatElement("InstanceIdentifier", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("inResponseTo.ActionIdentity.InstanceIdentifier");

      /**
       *  Note: the following optional "VersionIdentifier" element is present in the DTD
       *  but not in the ServiceHeaderMessageGuideLine. Though RNIF1.1 spec states
       *  the message guideline should prevail in event of any conflicts between DTD
       *  and the message guideline, we still have to include this element, purely for
       *  interop with Intel as they, adhering to the DTD rather than the message
       *  guideline, have this element in their RN messages.
       *
       *  *Temporary solution -- to be revised / discussed with Intel later
       */
/*      value = packInfo.getInReplyToVersionIdentifier();
      if (value != null)
      { // This "VersionIdentifier" element is optional
        result.append("           ");
        result.append(XMLUtil.formatElement("VersionIdentifier", value));
      }
*/
      result.append("\n         </ActionIdentity>\n");
      result.append("       </inResponseTo>\n");

      result.append("       ");

      // Hack for interop -- generate a unique ID for the acknowledgement
      value = getUniqueID();

      if ( value != null)
        result.append(XMLUtil.formatElement("InstanceIdentifier", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("SignalControl.InstanceIdentifier");

      result.append("\n       <PartnerRoute>\n");
      result.append("         <fromPartner>\n");
      result.append("           <PartnerDescription>\n");
      result.append("             <BusinessDescription>\n");
      result.append("               ");
      value = packInfo.getSenderGlobalBusIdentifier();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalBusinessIdentifier", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("fromPartner.GlobalBusinessIdentifier");

      result.append("\n             </BusinessDescription>\n");
      result.append("             ");
      value = packInfo.getFromGlobalPartnerClassCode();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalPartnerClassificationCode", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("fromPartner.GlobalPartnerClassificationCode");

      result.append("\n           </PartnerDescription>\n");
      result.append("         </fromPartner>\n");

      result.append("         <toPartner>\n");
      result.append("           <PartnerDescription>\n");
      result.append("             <BusinessDescription>\n");
      result.append("               ");
      value = packInfo.getReceiverGlobalBusIdentifier();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalBusinessIdentifier", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("toPartner.GlobalBusinessIdentifier");

      result.append("\n             </BusinessDescription>\n");
      result.append("             ");
      value = packInfo.getToGlobalPartnerClassCode();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalPartnerClassificationCode", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("toPartner.GlobalPartnerClassificationCode");

      result.append("\n           </PartnerDescription>\n");
      result.append("         </toPartner>\n");
      result.append("       </PartnerRoute>\n");
      result.append("       <SignalIdentity>\n");
      result.append("         ");
      value = packInfo.getSignalIdentityGlobalBusSignalCode();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalBusinessSignalCode", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("GlobalBusinessSignalCode");

      result.append("\n         ");
      value = packInfo.getSignalIdentityVersionIdentifier();
      if (value != null)
        result.append(XMLUtil.formatElement("VersionIdentifier", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("SignalIdentity.VersionIdentifier");

      result.append("\n       </SignalIdentity>\n");
      result.append("     </SignalControl>\n");
    }
    else //ActionControl
    {
      result.append("     <ActionControl>\n");
      result.append("       <ActionIdentity>\n");
      result.append("         ");
      value = packInfo.getActionIdentityGlobalBusActionCode();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalBusinessActionCode", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("ActionControl.GlobalBusinessActionCode");

      result.append("\n         ");
      value = packInfo.getProcessActionId();
      if (value != null)
        result.append(XMLUtil.formatElement("InstanceIdentifier", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("ActionControl.InstanceIdentifier");

      result.append("\n         ");

      // VersionIdentifier -- we use the PIP version identifier
      value = packInfo.getPIPVersionIdentifier();
      if (value != null)
        result.append(XMLUtil.formatElement("VersionIdentifier", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("ActionControl.VersionIdentifier");

/*      value = packInfo.getActionIdentityDesc();
      if (value != null)
      { // this description is optional
        result.append("         <description>\n");
        String lang = packInfo.getActionIdentityDescLang();
        if (lang == null)
          lang = "en";
        result.append("           <FreeFormText xml:lang=\"" + lang + "\">" +
            value + "</FreeFormText>\n");
        result.append("         </description>\n");
      }
*/
      result.append("\n       </ActionIdentity>\n");

      result.append("       ");
      value = getDocFunctionCode(packInfo);
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalDocumentFunctionCode", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("GlobalDocumentFunctionCode");

      result.append("\n");
      if (value.equals("Response"))
      { // inResponseTo must be present
        result.append("       <inResponseTo>\n");
        result.append("         <ActionIdentity>\n");
        result.append("           ");
        value = packInfo.getInReplyToGlobalBusActionCode();
        if (value != null)
          result.append(XMLUtil.formatElement("GlobalBusinessActionCode", value));
        else
          throw RosettaNetException.pkgShdrGDocErr("ActionControl.inResponseTo.GlobalBusinessActionCode");

        result.append("\n           ");

        value = packInfo.getInResponseToActionID();
        if (value != null)
          result.append(XMLUtil.formatElement("InstanceIdentifier", value));
        else
          throw RosettaNetException.pkgShdrGDocErr("ActionControl.inResponseTo.InstanceIdentifier");

        result.append("\n         </ActionIdentity>\n");
        result.append("       </inResponseTo>\n");

      }

      result.append("       <PartnerRoute>\n");
      result.append("         <fromPartner>\n");
      result.append("           <PartnerDescription>\n");
      result.append("             <BusinessDescription>\n");
      result.append("               ");
      value = packInfo.getSenderGlobalBusIdentifier();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalBusinessIdentifier", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("ActionControl.fromPartner.GlobalBusinessIdentifier");

      result.append("\n             </BusinessDescription>\n");
      result.append("             ");
      value = packInfo.getFromGlobalPartnerClassCode();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalPartnerClassificationCode", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("ActionControl.fromPartner.GlobalPartnerClassificationCode");

      result.append("\n           </PartnerDescription>\n");
      result.append("         </fromPartner>\n");

      result.append("         <toPartner>\n");
      result.append("           <PartnerDescription>\n");
      result.append("             <BusinessDescription>\n");
      result.append("               ");
      value = packInfo.getReceiverGlobalBusIdentifier();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalBusinessIdentifier", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("ActionControl.toPartner.GlobalBusinessIdentifier");

      result.append("\n             </BusinessDescription>\n");
      result.append("             ");
      value = packInfo.getToGlobalPartnerClassCode();
      if (value != null)
        result.append(XMLUtil.formatElement("GlobalPartnerClassificationCode", value));
      else
        throw RosettaNetException.pkgShdrGDocErr("ActionControl.toPartner.GlobalPartnerClassificationCode");

      result.append("\n           </PartnerDescription>\n");
      result.append("         </toPartner>\n");
      result.append("       </PartnerRoute>\n");

      // PerformanceControlRequest ?

      result.append("     </ActionControl>\n");
    }

    result.append("   </TransactionControl>\n");
    result.append("  </ProcessControl>\n");
    result.append("</ServiceHeader>\n");
    return result.toString();

  }

  /*
   * Derive the GlobalDocumentFunctionCode.
   * If the initiatingPartner is the fromPartner of this message, it's a request,
   * otherwise it's a response.
   */
  private String getDocFunctionCode(RNPackInfo packInfo)
  {
    if (packInfo.getSenderGlobalBusIdentifier().equals(
        packInfo.getPartnerGlobalBusIdentifier()))
      return "Request";
    else
      return "Response";
  }
}